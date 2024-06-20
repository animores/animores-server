package animores.serverapi.diary.service.impl;

import static animores.serverapi.common.S3Path.DIARY_PATH;

import animores.serverapi.account.domain.Account;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.common.service.AuthorizationService;
import animores.serverapi.common.service.S3Service;
import animores.serverapi.diary.dao.GetAllDiaryCommentDao;
import animores.serverapi.diary.dao.GetAllDiaryDao;
import animores.serverapi.diary.dao.GetCalendarDiaryDao;
import animores.serverapi.diary.dto.AddDiaryLikeRequest;
import animores.serverapi.diary.dto.AddDiaryMediaRequest;
import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.CancelDiaryLikeRequest;
import animores.serverapi.diary.dto.EditDiaryContentRequest;
import animores.serverapi.diary.dto.EditDiaryMediaRequest;
import animores.serverapi.diary.dto.GetAllDiaryCommentResponse;
import animores.serverapi.diary.dto.GetAllDiaryResponse;
import animores.serverapi.diary.dto.GetCalendarDiaryResponse;
import animores.serverapi.diary.dto.RemoveDiaryRequest;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.entity.DiaryLike;
import animores.serverapi.diary.entity.DiaryMedia;
import animores.serverapi.diary.entity.DiaryMediaType;
import animores.serverapi.diary.repository.DiaryCommentCustomRepository;
import animores.serverapi.diary.repository.DiaryCustomRepository;
import animores.serverapi.diary.repository.DiaryLikeRepository;
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
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final S3Service s3Service;
    private final AuthorizationService authorizationService;

    private final ProfileRepository profileRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryCustomRepository diaryCustomRepository;
    private final DiaryMediaRepository diaryMediaRepository;
    private final DiaryMediaCustomRepository diaryMediaCustomRepository;
    private final DiaryLikeRepository diaryLikeRepository;
    private final DiaryCommentCustomRepository diaryCommentCustomRepository;

    @Override
    @Transactional(readOnly = true)
    public GetAllDiaryResponse getAllDiary(Account account, Long profileId, int page, int size) {
        Profile profile = findProfileById(profileId);
        authorizationService.validateProfileAccess(account, profile);

        List<GetAllDiaryDao> diaries = diaryCustomRepository.getAllDiary(account.getId(), profileId,
            page, size);
        Long totalCount = diaryCustomRepository.getAllDiaryCount(account.getId());

        return new GetAllDiaryResponse(totalCount, diaries);
    }

    @Override
    @Transactional(readOnly = true)
    public GetCalendarDiaryResponse getCalendarDiary(Account account, Long profileId,
        LocalDate date) {
        Profile profile = findProfileById(profileId);
        authorizationService.validateProfileAccess(account, profile);

        QueryResults<GetCalendarDiaryDao> diaries = diaryCustomRepository.getCalendarDiary(
            account.getId(), date);

        return new GetCalendarDiaryResponse(diaries.getTotal(), diaries.getResults());
    }

    @Override
    @Transactional
    public void addDiary(Account account, AddDiaryRequest request, List<MultipartFile> files) {
        Profile profile = findProfileById(request.profileId());
        authorizationService.validateProfileAccess(account, profile);

        if (files == null || files.isEmpty()) {
            throw new CustomException(ExceptionCode.EMPTY_FILE);
        }

        Diary diary = diaryRepository.save(Diary.create(account, profile, request.content()));

        if (files != null) {
            List<DiaryMedia> diaryMedias = createDiaryMedias_temp(diary, files);
            List<String> keys = extractUrlsFromDiaryMedias(diaryMedias);

            s3Service.uploadFilesToS3_temp(files, keys);

            diaryMediaRepository.saveAll(diaryMedias);
        }

    }

    @Override
    @Transactional
    public void editDiaryContent(Account account, Long diaryId, EditDiaryContentRequest request) {
        Profile profile = findProfileById(request.profileId());
        Diary diary = findDiaryById(diaryId);

        authorizationService.validateProfileAccess(account, profile);
        authorizationService.validateDiaryAccess(diary, profile);

        diary.updateContent(request.content());
    }

    @Transactional
    @Override
    public void addDiaryMedia(Account account, Long diaryId, AddDiaryMediaRequest request,
        List<MultipartFile> files) throws IOException {
        Profile profile = findProfileById(request.profileId());
        Diary diary = findDiaryById(diaryId);

        authorizationService.validateProfileAccess(account, profile);
        authorizationService.validateDiaryAccess(diary, profile);

        List<PutObjectRequest> putObjectRequests = s3Service.uploadFilesToS3(files, DIARY_PATH);

        diaryMediaRepository.saveAll(createDiaryMedias(diary, putObjectRequests));

        reorderDiaryMedia(diary.getId(), DiaryMediaType.I);
    }

    @Override
    @Transactional
    public void editDiaryMedia(Account account, Long diaryId, EditDiaryMediaRequest request,
        List<MultipartFile> files) throws IOException {
        Profile profile = findProfileById(request.profileId());
        Diary diary = findDiaryById(diaryId);

        authorizationService.validateProfileAccess(account, profile);
        authorizationService.validateDiaryAccess(diary, profile);

        List<DiaryMedia> mediaListToDelete = diaryMediaRepository.findByIdIn(request.mediaIds());
        if (mediaListToDelete.isEmpty()) {
            throw new CustomException(ExceptionCode.NOT_FOUND_DIARY_MEDIA);
        }
        s3Service.removeFilesFromS3(
            mediaListToDelete.stream()
                .map(DiaryMedia::getUrl)
                .toList()
        );
        diaryMediaRepository.deleteAll(mediaListToDelete);

        List<PutObjectRequest> putObjectRequests = s3Service.uploadFilesToS3(files, DIARY_PATH);
        diaryMediaRepository.saveAll(createDiaryMedias(diary, putObjectRequests));

        reorderDiaryMedia(diary.getId(), DiaryMediaType.I);
    }

    @Override
    @Transactional
    public void removeDiaryMedia(Account account, Long diaryId, EditDiaryMediaRequest request) {
        Profile profile = findProfileById(request.profileId());
        Diary diary = findDiaryById(diaryId);

        authorizationService.validateProfileAccess(account, profile);
        authorizationService.validateDiaryAccess(diary, profile);

        List<DiaryMedia> mediaListToDelete = diaryMediaRepository.findByIdIn(request.mediaIds());
        if (mediaListToDelete.isEmpty()) {
            throw new CustomException(ExceptionCode.NOT_FOUND_DIARY_MEDIA);
        }
        s3Service.removeFilesFromS3(
            mediaListToDelete.stream()
                .map(DiaryMedia::getUrl)
                .toList()
        );
        diaryMediaRepository.deleteAll(mediaListToDelete);

        reorderDiaryMedia(diary.getId(), DiaryMediaType.I);
    }

    @Override
    @Transactional
    public void removeDiary(Account account, Long diaryId, RemoveDiaryRequest request) {
        Profile profile = findProfileById(request.profileId());
        Diary diary = findDiaryById(diaryId);

        authorizationService.validateProfileAccess(account, profile);
        authorizationService.validateDiaryAccess(diary, profile);

        diary.delete();
    }

    @Override
    @Transactional
    public void addDiaryLike(Account account, Long diaryId, AddDiaryLikeRequest request) {
        Profile profile = findProfileById(request.profileId());
        Diary diary = findDiaryById(diaryId);

        authorizationService.validateProfileAccess(account, profile);

        diaryLikeRepository.save(DiaryLike.create(profile, diary));
    }

    @Override
    @Transactional
    public void cancelDiaryLike(Account account, Long diaryId, CancelDiaryLikeRequest request) {
        Profile profile = findProfileById(request.profileId());
        Diary diary = findDiaryById(diaryId);

        authorizationService.validateProfileAccess(account, profile);

        DiaryLike diaryLikeToDelete = diaryLikeRepository.findByDiaryIdAndProfileId(diary.getId(),
                profile.getId())
            .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_DIARY_LIKE));

        diaryLikeRepository.delete(diaryLikeToDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public GetAllDiaryCommentResponse getAllDiaryComment(Account account, Long diaryId,
        Long profileId, int page, int size) {
        Profile profile = findProfileById(profileId);
        authorizationService.validateProfileAccess(account, profile);

        List<GetAllDiaryCommentDao> comments = diaryCommentCustomRepository.getAllDiaryComment(
            profileId, page, size);
        Long totalCount = diaryCommentCustomRepository.getAllDiaryCommentCount(diaryId);

        return new GetAllDiaryCommentResponse(totalCount, comments);
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
            .mapToObj(i -> DiaryMedia.create(diary, putObjectRequests.get(i).key(), i,
                DiaryMediaType.checkType(putObjectRequests.get(i).contentType())))
            .toList();
    }

    private List<DiaryMedia> createDiaryMedias_temp(Diary diary, List<MultipartFile> files) {
        return IntStream.range(0, files.size())
            .mapToObj(i -> DiaryMedia.create(diary, DIARY_PATH + UUID.randomUUID(), i,
                DiaryMediaType.checkType(files.get(i).getContentType())))
            .toList();
    }

    private List<String> extractUrlsFromDiaryMedias(List<DiaryMedia> diaryMedias) {
        return diaryMedias.stream()
            .map(DiaryMedia::getUrl)
            .collect(Collectors.toList());
    }

    public void reorderDiaryMedia(Long diaryId, DiaryMediaType type) {
        List<DiaryMedia> mediaList = diaryMediaCustomRepository.getAllDiaryMediaToReorder(diaryId,
            type);

        for (int i = 0; i < mediaList.size(); i++) {
            mediaList.get(i).updateMediaOrder(i);
        }
    }
}
