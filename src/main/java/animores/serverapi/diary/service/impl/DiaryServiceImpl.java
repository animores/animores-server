package animores.serverapi.diary.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.EditDiaryRequest;
import animores.serverapi.diary.dao.GetAllDiaryDao;
import animores.serverapi.diary.dto.GetAllDiaryResponse;
import animores.serverapi.diary.dao.GetCalendarDiaryDao;
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

@RequiredArgsConstructor
@Service
public class DiaryServiceImpl implements DiaryService {

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryCustomRepository diaryCustomRepository;


    @Override
    @Transactional(readOnly = true)
    public GetAllDiaryResponse getAllDiary(int page, int size) {
        Long accountId = 1L;    // 나중에 인증 정보에서 가져오기 param으로 받지x
        Long profileId = 1L;

        List<GetAllDiaryDao> diaries = diaryCustomRepository.getAllDiary(accountId, profileId, page, size);
        Long totalCount = diaryCustomRepository.getAllDiaryCount(accountId);

        return new GetAllDiaryResponse(totalCount, diaries);
    }

    @Override
    @Transactional(readOnly = true)
    public GetCalendarDiaryResponse getCalendarDiary(Long accountId, LocalDate date) {
        QueryResults<GetCalendarDiaryDao> diaries = diaryCustomRepository.getCalendarDiary(accountId,
            date);

        return new GetCalendarDiaryResponse(diaries.getTotal(), diaries.getResults());
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
