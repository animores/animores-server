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

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static animores.serverapi.common.S3Path.PROFILE_IMAGE_PATH;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final S3Service s3Service;
    private static final int MAX_PROFILE_COUNT = 6;
    private static final Set<String> SUPPORTED_IMAGE_TYPE = Set.of("image/jpeg", "image/png");

    @Override
    @Transactional(readOnly = true)
    public List<ProfileResponse> getProfiles(Account account) {
        List<Profile> profiles = profileRepository.findAllByAccountIdAndDeletedAtIsNull(account.getId());
        return profiles.stream().map(ProfileResponse::fromEntity).toList();
    }

    @Override
    @Transactional
    public void createProfile(Account account, ProfileCreateRequest request, MultipartFile profileImage) {
        if(profileRepository.countByAccountIdAndDeletedAtIsNull(account.getId()) >= MAX_PROFILE_COUNT) {
            throw new CustomException(ExceptionCode.MAX_PROFILE_COUNT);
        }

        String imageUrl = null;

        if(profileImage != null) {
            try {
                checkContentType(profileImage);
                String fileName = UUID.randomUUID().toString();
                s3Service.uploadFileToS3(profileImage, PROFILE_IMAGE_PATH, fileName);
                imageUrl = PROFILE_IMAGE_PATH + fileName;
            } catch (Exception e) {
                log.error("Failed to upload profile image: {}", e.getMessage());
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
        profileRepository.findByIdAndAccountIdAndDeletedAtIsNull(profileId, account.getId())
                .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_PROFILE));
    }

    @Override
    @Transactional
    public void updateProfile(ProfileUpdateRequest request, MultipartFile profileImage) {
        Profile profile = profileRepository.findByIdAndDeletedAtIsNull(request.profileId()).orElseThrow(
                () -> new CustomException(ExceptionCode.INVALID_PROFILE));

        if (request.name() != null && !request.name().equals(profile.getName())) {
            profile.setName(request.name());
        }

        if (request.isUpdateImage()) {
            if (profileImage.isEmpty()) {
                profile.setImageUrl(null);
            } else {
                checkContentType(profileImage);
                try {
                    s3Service.removeFilesFromS3(List.of(profile.getImageUrl()));
                    String fileName = UUID.randomUUID().toString();
                    s3Service.uploadFileToS3(profileImage, PROFILE_IMAGE_PATH, fileName);
                    profile.setImageUrl(fileName);
                } catch (Exception e) {
                    log.error("Failed to update profile image: {}", e.getMessage());
                }
            }
        }
    }

    @Override
    @Transactional
    public void deleteProfile(Long profileId) {
        Profile profile = profileRepository.findByIdAndDeletedAtIsNull(profileId).orElseThrow(
                () -> new CustomException(ExceptionCode.INVALID_PROFILE)
        );

        s3Service.removeFilesFromS3(List.of(profile.getImageUrl()));
        profile.delete();
    }

    private void checkContentType(MultipartFile profileImage) {
        if (!SUPPORTED_IMAGE_TYPE.contains(profileImage.getContentType())) {
            throw new CustomException(ExceptionCode.INVALID_IMAGE_TYPE);
        }
    }
}
