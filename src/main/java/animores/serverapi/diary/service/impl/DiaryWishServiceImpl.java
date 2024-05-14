package animores.serverapi.diary.service.impl;

import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.diary.dto.AddDiaryWishRequest;
import animores.serverapi.diary.dto.CancelDiaryWishRequest;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.entity.DiaryWish;
import animores.serverapi.diary.repository.DiaryRepository;
import animores.serverapi.diary.repository.DiaryWishRepository;
import animores.serverapi.diary.service.DiaryWishService;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.profile.repository.ProfileRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DiaryWishServiceImpl implements DiaryWishService {

    private final ProfileRepository profileRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryWishRepository diaryWishRepository;

    @Override
    @Transactional
    public void addDiaryWish(AddDiaryWishRequest request) {
        Long profileId = 1L;    // 나중에 인증 정보에서 가져오기 param으로 받지x
        Profile profile = findProfileById(profileId);
        Diary diary = findDiaryById(request.diaryId());

        diaryWishRepository.save(DiaryWish.create(profile, diary));
    }

    @Override
    public void cancelDiaryWish(CancelDiaryWishRequest request) {
        Profile profile = findProfileById(request.profileId());
        Diary diary = findDiaryById(request.diaryId());
        // auth 적용 후 diary wish 등록한 사람과 일치하는지 체크하는 코드 추가 예정

        DiaryWish diaryWishToDelete = diaryWishRepository.findByDiaryIdAndProfileId(diary.getId(),
                profile.getId())
            .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_DIARY_WISH));

        diaryWishRepository.delete(diaryWishToDelete);
    }

    private Profile findProfileById(Long id) {  // 일지 수정, 삭제 모두 dev에 머지되면 코드 공통으로 분리
        return profileRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Profile not found with id: " + id));
    }

    private Diary findDiaryById(Long id) {
        return diaryRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Diary not found with id: " + id));
    }

    private DiaryWish findDiaryWishById(Long id) {
        return diaryWishRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Diary Wish not found with id: " + id));
    }
}
