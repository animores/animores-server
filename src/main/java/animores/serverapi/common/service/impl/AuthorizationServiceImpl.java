package animores.serverapi.common.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.common.service.AuthorizationService;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.entity.DiaryComment;
import animores.serverapi.profile.domain.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    @Override
    public void validateProfileAccess(Account account, Profile profile) {
        if (!account.getId().equals(profile.getAccount().getId())) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_PROFILE_ACCESS);
        }
    }

    @Override
    public void validateDiaryAccess(Diary diary, Profile profile) {
        if (!diary.getProfile().getId().equals(profile.getId())) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_DIARY_ACCESS);
        }
    }

    @Override
    public void validateDiaryCommentAccess(DiaryComment diaryComment, Profile profile) {
        if (!diaryComment.getProfile().getId().equals(profile.getId())) {
            throw new CustomException(ExceptionCode.UNAUTHORIZED_DIARY_COMMENT_ACCESS);
        }
    }

}
