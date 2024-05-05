package animores.serverapi.diary.service.impl;

import static animores.serverapi.common.S3Path.DIARY_PATH;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.common.service.S3Service;
import animores.serverapi.diary.dao.GetAllDiaryDao;
import animores.serverapi.diary.dao.GetCalendarDiaryDao;
import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.EditDiaryContentRequest;
import animores.serverapi.diary.dto.EditDiaryMediaRequest;
import animores.serverapi.diary.dto.GetAllDiaryResponse;
import animores.serverapi.diary.dto.GetCalendarDiaryResponse;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.entity.DiaryMedia;
import animores.serverapi.diary.entity.DiaryMediaType;
import animores.serverapi.diary.repository.DiaryCustomRepository;
import animores.serverapi.diary.repository.DiaryMediaCustomRepository;
import animores.serverapi.diary.repository.DiaryMediaRepository;
import animores.serverapi.diary.repository.DiaryRepository;
import animores.serverapi.diary.service.DiaryService;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.profile.repository.ProfileRepository;
import com.querydsl.core.QueryResults;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final S3Service s3Service;
    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryCustomRepository diaryCustomRepository;
    private final DiaryMediaRepository diaryMediaRepository;
    private final DiaryMediaCustomRepository diaryMediaCustomRepository;

    @Override
    @Transactional(readOnly = true)
    public GetAllDiaryResponse getAllDiary(int page, int size) {
        Long accountId = 1L;    // 나중에 인증 정보에서 가져오기 param으로 받지x
        Long profileId = 1L;

        List<GetAllDiaryDao> diaries = diaryCustomRepository.getAllDiary(accountId, profileId, page,
            size);
        Long totalCount = diaryCustomRepository.getAllDiaryCount(accountId);

        return new GetAllDiaryResponse(totalCount, diaries);
    }

    @Override
    @Transactional(readOnly = true)
    public GetCalendarDiaryResponse getCalendarDiary(Long accountId, LocalDate date) {
        QueryResults<GetCalendarDiaryDao> diaries = diaryCustomRepository.getCalendarDiary(
            accountId, date);

        return new GetCalendarDiaryResponse(diaries.getTotal(), diaries.getResults());
    }

    @Override
    @Transactional
    public void addDiary(AddDiaryRequest request, List<MultipartFile> files) throws IOException {
        // 유저쪽 코드 병합되면 수정
        Account account = findAccountById(1L);
        Profile profile = findProfileById(1L);
        //

        // files에서 "files"만 넘어오고 파일은 안담겨서 넘어왔을 경우 에러 처리 필요

        Diary diary = diaryRepository.save(Diary.create(account, profile, request.content()));

        if (files != null) {

            List<PutObjectRequest> putObjectRequests = s3Service.uploadFilesToS3(files, DIARY_PATH);

            List<DiaryMedia> diaryMedias = createDiaryMedias(diary, putObjectRequests);
            diaryMediaRepository.saveAll(diaryMedias);
        }

    }

    @Override
    @Transactional
    public void editDiaryContent(Long diaryId, EditDiaryContentRequest request) {
        Diary diary = findDiaryById(diaryId);
        // auth 적용 후 diary 작성자와 일치하는지 체크하는 코드 추가 예정

        diary.updateContent(request.content());
    }

    @Transactional
    @Override
    public void addDiaryMedia(Long diaryId, List<MultipartFile> files) throws IOException {
        Diary diary = findDiaryById(diaryId);
        // auth 적용 후 diary 작성자와 일치하는지 체크하는 코드 추가 예정

        List<PutObjectRequest> putObjectRequests = s3Service.uploadFilesToS3(files, DIARY_PATH);

        diaryMediaRepository.saveAll(createDiaryMedias(diary, putObjectRequests));

        reorderDiaryMedia(diary.getId(), DiaryMediaType.I);
    }

    @Override
    @Transactional
    public void editDiaryMedia(Long diaryId, EditDiaryMediaRequest request,
        List<MultipartFile> files) throws IOException {
        Diary diary = findDiaryById(diaryId);
        // auth 적용 후 diary 작성자와 일치하는지 체크하는 코드 추가 예정

        List<DiaryMedia> mediaListToDelete = diaryMediaRepository.findByIdIn(request.mediaIds());
        if (mediaListToDelete.isEmpty()) {
            throw new CustomException(ExceptionCode.NOT_FOUND_DIARY_MEDIA);
        }
        s3Service.removeFilesFromS3(generateKeysForS3Deletion(mediaListToDelete));
        diaryMediaRepository.deleteAll(mediaListToDelete);

        List<PutObjectRequest> putObjectRequests = s3Service.uploadFilesToS3(files, DIARY_PATH);

        diaryMediaRepository.saveAll(createDiaryMedias(diary, putObjectRequests));

        reorderDiaryMedia(diary.getId(), DiaryMediaType.I);
    }

    @Override
    @Transactional
    public void removeDiaryMedia(Long diaryId, EditDiaryMediaRequest request) {
        Diary diary = findDiaryById(diaryId);
        // auth 적용 후 diary 작성자와 일치하는지 체크하는 코드 추가 예정

        List<DiaryMedia> mediaListToDelete = diaryMediaRepository.findByIdIn(request.mediaIds());
        if (mediaListToDelete.isEmpty()) {
            throw new CustomException(ExceptionCode.NOT_FOUND_DIARY_MEDIA);
        }
        s3Service.removeFilesFromS3(generateKeysForS3Deletion(mediaListToDelete));
        diaryMediaRepository.deleteAll(mediaListToDelete);

        reorderDiaryMedia(diary.getId(), DiaryMediaType.I);
    }

    @Override
    @Transactional
    public void removeDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
            .orElseThrow(NoSuchElementException::new);

        diary.delete();
    }

    private Account findAccountById(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Account not found with id: " + id));
    }

    private Profile findProfileById(Long id) {
        return profileRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Profile not found with id: " + id));
    }

    private Diary findDiaryById(Long id) {
        return diaryRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Diary not found with id: " + id));
    }

    private List<DiaryMedia> createDiaryMedias(Diary diary,
        List<PutObjectRequest> putObjectRequests) {
        return IntStream.range(0, putObjectRequests.size())
            .mapToObj(i -> {
                return DiaryMedia.create(diary, putObjectRequests.get(i).key(), i,
                    DiaryMediaType.checkType(putObjectRequests.get(i).contentType()));
            })
            .collect(Collectors.toList());
    }

    public void reorderDiaryMedia(Long diaryId, DiaryMediaType type) {
        List<DiaryMedia> mediaList = diaryMediaCustomRepository.getAllDiaryMediaToReorder(diaryId,
            type);

        for (int i = 0; i < mediaList.size(); i++) {
            mediaList.get(i).updateMediaOrder(i);
        }
    }

    private List<ObjectIdentifier> generateKeysForS3Deletion(List<DiaryMedia> mediaList) {
        return mediaList.stream()
            .map(media -> ObjectIdentifier.builder()
                .key(media.getUrl())
                .build())
            .collect(Collectors.toList());
    }
}
