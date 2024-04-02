package animores.serverapi.diary.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.EditDiaryRequest;
import animores.serverapi.diary.dao.GetAllDiary;
import animores.serverapi.diary.dto.GetAllDiaryResponse;
import animores.serverapi.diary.dao.GetCalendarDiary;
import animores.serverapi.diary.dto.GetCalendarDiaryResponse;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.repository.DiaryCustomRepository;
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
        Account account = accountRepository.findById(1L)
            .orElseThrow(NoSuchElementException::new);
        Profile profile = profileRepository.findById(1L)
            .orElseThrow(NoSuchElementException::new);

        diaryRepository.save(Diary.create(account, profile, request));
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
}
