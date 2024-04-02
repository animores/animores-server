package animores.serverapi.diary.service.impl;

import static animores.serverapi.diary.entity.DiaryMediaType.I;
import static animores.serverapi.diary.entity.DiaryMediaType.V;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.diary.dao.GetAllDiary;
import animores.serverapi.diary.dao.GetCalendarDiary;
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
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class DiaryServiceImpl implements DiaryService {

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryCustomRepository diaryCustomRepository;
    private final DiaryMediaRepository diaryMediaRepository;


    @Override
    public GetAllDiaryResponse getAllDiary(Long accountId) {
        QueryResults<GetAllDiary> diaries = diaryCustomRepository.getAllDiary(accountId);

        return new GetAllDiaryResponse(diaries.getTotal(), diaries.getResults());
    }

    @Override
    public GetCalendarDiaryResponse getCalendarDiary(Long accountId, LocalDate date) {
        QueryResults<GetCalendarDiary> diaries = diaryCustomRepository.getCalendarDiary(accountId,
            date);

        return new GetCalendarDiaryResponse(diaries.getTotal(), diaries.getResults());
    }

    @Override
    @Transactional
    public void addDiary(AddDiaryRequest request, List<MultipartFile> files) {
        // 유저쪽 코드 병합되면 수정
        Account account = findAccountById(1L);
        Profile profile = findProfileById(1L);
        //

        Diary diary = diaryRepository.save(Diary.create(account, profile, request.content()));

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            diaryMediaRepository.save(DiaryMedia.create(diary, file.getOriginalFilename(), i,
                checkType(file.getContentType())));
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
