package animores.serverapi.common.service;

import animores.serverapi.account.entity.Account;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.entity.DiaryComment;
import animores.serverapi.diary.entity.DiaryReply;
import animores.serverapi.profile.entity.Profile;

public interface AuthorizationService {

    void validateProfileAccess(Account account, Profile profile);

    void validateDiaryAccess(Diary diary, Profile profile);

    void validateDiaryCommentAccess(DiaryComment diaryComment, Profile profile);

    void validateDiaryReplyAccess(DiaryReply diaryReply, Profile profile);

}
