package animores.serverapi.diary.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.EditDiaryRequest;
import animores.serverapi.diary.dto.GetAllDiary;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.repository.DiaryCustomRepository;
import animores.serverapi.diary.repository.DiaryRepository;
import animores.serverapi.diary.service.DiaryService;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.profile.repository.ProfileRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DiaryServiceImpl implements DiaryService {

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryCustomRepository diaryCustomRepository;


    @Override
    public List<GetAllDiary> getAllDiary(Long accountId) {
        List<GetAllDiary> diaries = diaryCustomRepository.getAllDiary(accountId);

        return diaries;
    }

    @Override
    public void getCalendarDiary(Long userId, String date) {
        // querydsl 사용해야될듯
    }

    @Override
    @Transactional
    public void addDiary(AddDiaryRequest request) {
        Account account = accountRepository.findById(request.accountId())
            .orElseThrow(NoSuchElementException::new);
        Profile profile = profileRepository.findById(request.profileId())
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
