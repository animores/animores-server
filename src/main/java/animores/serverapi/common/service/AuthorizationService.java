package animores.serverapi.common.service;

import animores.serverapi.account.domain.Account;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.profile.domain.Profile;

public interface AuthorizationService {

    void validateProfileAccess(Account account, Profile profile);

    void validateDiaryAccess(Diary diary, Profile profile);

}
