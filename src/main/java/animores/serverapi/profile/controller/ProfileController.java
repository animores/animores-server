package animores.serverapi.profile.controller;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.aop.UserInfo;
import animores.serverapi.profile.dto.request.ProfileCreateRequest;
import animores.serverapi.profile.dto.request.ProfileUpdateRequest;
import animores.serverapi.profile.dto.response.ProfileResponse;
import animores.serverapi.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileController {

    private final AccountService accountService;
    private final ProfileService profileService;

    @GetMapping("")
    @UserInfo
    public List<ProfileResponse> getProfiles() {
        Account account = accountService.getAccountFromContext();
        return profileService.getProfiles(account);
    }

    @PostMapping("")
    @UserInfo
    public void createProfile(@RequestBody ProfileCreateRequest request) {
        Account account = accountService.getAccountFromContext();
        profileService.createProfile(account, request);
    }

    @PutMapping("/{profileId}")
    @UserInfo
    public void updateProfile(@PathVariable Long profileId, @RequestBody ProfileUpdateRequest request) {
        Account account = accountService.getAccountFromContext();
        profileService.checkProfile(account, profileId);
        profileService.updateProfile(profileId, request);
    }

    @DeleteMapping("/{profileId}")
    @UserInfo
    public void deleteProfile(@PathVariable Long profileId) {
        Account account = accountService.getAccountFromContext();
        profileService.checkProfile(account, profileId);
        profileService.deleteProfile(account, profileId);
    }
}
