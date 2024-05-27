package animores.serverapi.profile.service;

import animores.serverapi.account.domain.Account;
import animores.serverapi.profile.dto.request.ProfileCreateRequest;
import animores.serverapi.profile.dto.request.ProfileUpdateRequest;
import animores.serverapi.profile.dto.response.ProfileResponse;

import java.util.List;

public interface ProfileService {
    List<ProfileResponse> getProfiles(Account account);

    void createProfile(Account account, ProfileCreateRequest request);

    void checkProfile(Account account, Long profileId);

    void updateProfile(Long profileId, ProfileUpdateRequest request);

    void deleteProfile(Account account, Long profileId);
}
