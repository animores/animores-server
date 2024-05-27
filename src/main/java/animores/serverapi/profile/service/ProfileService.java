package animores.serverapi.profile.service;

import animores.serverapi.account.domain.Account;
import animores.serverapi.profile.dto.request.ProfileCreateRequest;
import animores.serverapi.profile.dto.request.ProfileUpdateRequest;
import animores.serverapi.profile.dto.response.ProfileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfileService {
    List<ProfileResponse> getProfiles(Account account);

    void createProfile(Account account, ProfileCreateRequest request, MultipartFile profileImage);

    void checkProfile(Account account, Long profileId);

    void updateProfile(ProfileUpdateRequest request, MultipartFile profileImage);

    void deleteProfile(Long profileId);
}
