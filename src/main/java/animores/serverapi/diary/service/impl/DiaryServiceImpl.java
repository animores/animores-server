package animores.serverapi.diary.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.EditDiaryRequest;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.repository.DiaryRepository;
import animores.serverapi.diary.service.DiaryService;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.profile.repository.ProfileRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DiaryServiceImpl implements DiaryService {

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final DiaryRepository diaryRepository;


    @Override
    public List<Diary> getAllDiary(Long accountId) {
        //  querydsl로 수정예정
        List<Diary> accountDiaries = diaryRepository.findByAccountId(accountId);
//        .orElseThrow(() -> new NoSuchElementException());

        return null;
    }

    @Override
    public void getCalendarDiary(Long userId, String date) {
        // querydsl 사용해야될듯
    }

    @Override
    @Transactional
    public ResponseEntity<Void> addDiary(AddDiaryRequest request) {
        Account account = accountRepository.findById(request.accountId())
            .orElseThrow(() -> new NoSuchElementException());
        Profile profile = profileRepository.findById(request.profileId())
            .orElseThrow(() -> new NoSuchElementException());

        diaryRepository.save(Diary.create(account, profile, request));

        return ResponseEntity.status(HttpStatus.SC_OK).build();
    }

    @Override
    @Transactional
    public ResponseEntity<Void> editDiary(Long diaryId, EditDiaryRequest request) {
        Diary diary = diaryRepository.findById(diaryId)
            .orElseThrow(() -> new NoSuchElementException());

        diary.update(request);

        return ResponseEntity.status(HttpStatus.SC_OK).build();
    }

    @Override
    @Transactional
    public ResponseEntity<Void> removeDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
            .orElseThrow(() -> new NoSuchElementException());

        diary.delete();

        return ResponseEntity.status(HttpStatus.SC_OK).build();
    }
}
