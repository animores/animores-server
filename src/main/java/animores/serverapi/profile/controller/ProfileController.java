package animores.serverapi.profile.controller;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.aop.UserInfo;
import animores.serverapi.profile.dto.request.ProfileCreateRequest;
import animores.serverapi.profile.dto.request.ProfileUpdateRequest;
import animores.serverapi.profile.dto.response.ProfileResponse;
import animores.serverapi.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profiles")
@PreAuthorize("hasAuthority('USER')")
@SecurityRequirement(name = "Authorization")
@Validated
public class ProfileController {

    private final AccountService accountService;
    private final ProfileService profileService;

    @GetMapping("")
    @UserInfo
    public List<ProfileResponse> getProfiles() {
        Account account = accountService.getAccountFromContext();
        return profileService.getProfiles(account);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @UserInfo
    public void createProfile(@Valid @RequestPart ProfileCreateRequest request,
                              @RequestPart(required = false) MultipartFile profileImage) {
        Account account = accountService.getAccountFromContext();
        profileService.createProfile(account, request, profileImage);
    }

    @PutMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @UserInfo
    @Valid
    public void updateProfile(@Valid @RequestBody ProfileUpdateRequest request,
                              @RequestPart(required = false) MultipartFile profileImage) {
        Account account = accountService.getAccountFromContext();
        profileService.checkProfile(account, request.profileId());
        profileService.updateProfile(request, profileImage);
    }

    @DeleteMapping("/{profileId}")
    @UserInfo
    public void deleteProfile(@PathVariable Long profileId) {
        Account account = accountService.getAccountFromContext();
        profileService.checkProfile(account, profileId);
        profileService.deleteProfile(profileId);
    }
}
