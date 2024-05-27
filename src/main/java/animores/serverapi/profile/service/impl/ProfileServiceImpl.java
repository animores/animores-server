package animores.serverapi.profile.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.profile.dto.request.ProfileCreateRequest;
import animores.serverapi.profile.dto.request.ProfileUpdateRequest;
import animores.serverapi.profile.dto.response.ProfileResponse;
import animores.serverapi.profile.repository.ProfileRepository;
import animores.serverapi.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    public List<ProfileResponse> getProfiles(Account account) {
        List<Profile> profiles = profileRepository.findAllByAccountId(account.getId());
        return profiles.stream().map(ProfileResponse::fromEntity).toList();
    }

    @Override
    public void createProfile(Account account, ProfileCreateRequest request) {

    }

    @Override
    public void checkProfile(Account account, Long profileId) {
        profileRepository.findByIdAndAccountId(profileId, account.getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_PROFILE));
    }

    @Override
    public void updateProfile(Long profileId, ProfileUpdateRequest request) {

    }

    @Override
    public void deleteProfile(Account account, Long profileId) {

    }
}
