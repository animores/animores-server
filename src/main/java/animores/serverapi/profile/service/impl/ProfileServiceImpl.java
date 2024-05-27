package animores.serverapi.profile.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.common.service.S3Service;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.profile.dto.request.ProfileCreateRequest;
import animores.serverapi.profile.dto.request.ProfileUpdateRequest;
import animores.serverapi.profile.dto.response.ProfileResponse;
import animores.serverapi.profile.repository.ProfileRepository;
import animores.serverapi.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;

import static animores.serverapi.common.S3Path.PROFILE_IMAGE_PATH;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final S3Service s3Service;
    private static final String DEFAULT_PROFILE_IMAGE_URL = PROFILE_IMAGE_PATH + "default_profile_image.png";

    @Override
    @Transactional(readOnly = true)
    public List<ProfileResponse> getProfiles(Account account) {
        List<Profile> profiles = profileRepository.findAllByAccountId(account.getId());
        return profiles.stream().map(ProfileResponse::fromEntity).toList();
    }

    @Override
    @Transactional
    public void createProfile(Account account, ProfileCreateRequest request, MultipartFile profileImage) {
        String imageUrl = DEFAULT_PROFILE_IMAGE_URL;

        if(profileImage != null) {
            try {
                imageUrl = s3Service.uploadFileToS3(profileImage, PROFILE_IMAGE_PATH).key();
            } catch (Exception e) {
                imageUrl = DEFAULT_PROFILE_IMAGE_URL;
            }
        }

        Profile profile = Profile.builder()
                .account(account)
                .imageUrl(imageUrl)
                .name(request.name())
                .build();

        profileRepository.save(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public void checkProfile(Account account, Long profileId) {
        profileRepository.findByIdAndAccountId(profileId, account.getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_PROFILE));
    }

    @Override
    @Transactional
    public void updateProfile(ProfileUpdateRequest request, MultipartFile profileImage) {
        Profile profile = profileRepository.findById(request.profileId()).orElseThrow(
                () -> new CustomException(ExceptionCode.INVALID_PROFILE));

        if (request.name() != null && !request.name().equals(profile.getName())) {
            profile.setName(request.name());
        }

        if(profileImage != null) {
            try {
                s3Service.removeFilesFromS3(List.of(profile.getImageUrl()));
                PutObjectRequest putObjectRequest = s3Service.uploadFileToS3(profileImage, PROFILE_IMAGE_PATH);
                profile.setImageUrl(putObjectRequest.key());
            } catch (Exception e) {
                log.error("Failed to update profile image: {}", e.getMessage());
            }
        }

    }

    @Override
    @Transactional
    public void deleteProfile(Long profileId) {
        profileRepository.deleteById(profileId);
    }
}
