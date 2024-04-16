package animores.serverapi.diary.service.impl;

import static animores.serverapi.diary.entity.DiaryMediaType.I;
import static animores.serverapi.diary.entity.DiaryMediaType.V;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.diary.dao.GetAllDiaryDao;
import animores.serverapi.diary.dao.GetCalendarDiaryDao;
import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.EditDiaryRequest;
import animores.serverapi.diary.dto.GetAllDiaryResponse;
import animores.serverapi.diary.dto.GetCalendarDiaryResponse;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.entity.DiaryMedia;
import animores.serverapi.diary.entity.DiaryMediaType;
import animores.serverapi.diary.repository.DiaryCustomRepository;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@Service
public class DiaryServiceImpl implements DiaryService {

    private final S3Client s3Client;

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryCustomRepository diaryCustomRepository;
    private final DiaryMediaRepository diaryMediaRepository;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public GetAllDiaryResponse getAllDiary(int page, int size) {
        Long accountId = 1L;    // 나중에 인증 정보에서 가져오기 param으로 받지x
        Long profileId = 1L;

        List<GetAllDiaryDao> diaries = diaryCustomRepository.getAllDiary(accountId, profileId, page, size);
        Long totalCount = diaryCustomRepository.getAllDiaryCount(accountId);

        return new GetAllDiaryResponse(totalCount, diaries);
    }

    @Override
    public GetCalendarDiaryResponse getCalendarDiary(Long accountId, LocalDate date) {
        QueryResults<GetCalendarDiaryDao> diaries = diaryCustomRepository.getCalendarDiary(accountId,
            date);

        return new GetCalendarDiaryResponse(diaries.getTotal(), diaries.getResults());
    }

    @Override
    @Transactional
    public void addDiary(AddDiaryRequest request, List<MultipartFile> files) throws IOException {
        // 유저쪽 코드 병합되면 수정
        Account account = findAccountById(1L);
        Profile profile = findProfileById(1L);
        //

        if (files != null) {
            for (MultipartFile file : files) {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .key(file.getOriginalFilename())
                    .build();
                RequestBody requestBody = RequestBody.fromBytes(file.getBytes());
                s3Client.putObject(putObjectRequest, requestBody);
            }
        }

        Diary diary = diaryRepository.save(Diary.create(account, profile, request.content()));

        if (files != null) {
            List<DiaryMedia> diaryMedias = IntStream.range(0, files.size())
                .mapToObj(i -> {
                    MultipartFile file = files.get(i);
                    return DiaryMedia.create(diary, "/" + file.getOriginalFilename(), i,
                        checkType(file.getContentType()));
                })
                .collect(Collectors.toList());
            diaryMediaRepository.saveAll(diaryMedias);
        }
    }

    @Override
    @Transactional
    public void editDiary(Long diaryId, EditDiaryRequest request) {
        Diary diary = diaryRepository.findById(diaryId)
            .orElseThrow(NoSuchElementException::new);

        diary.update(request);
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

    public DiaryMediaType checkType(String type) {
        return switch (type) {
            case "image/png" -> I;
            case "video/mp4" -> V;
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }
}
