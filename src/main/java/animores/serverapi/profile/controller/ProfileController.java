package animores.serverapi.profile.controller;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.Response;
import animores.serverapi.common.aop.UserInfo;
import animores.serverapi.profile.dto.request.ProfileCreateRequest;
import animores.serverapi.profile.dto.request.ProfileUpdateRequest;
import animores.serverapi.profile.dto.response.ProfileResponse;
import animores.serverapi.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "프로필 목록 조회", description = "프로필 목록을 조회합니다.")
    public Response<List<ProfileResponse>> getProfiles() {
        Account account = accountService.getAccountFromContext();
        return Response.success(profileService.getProfiles(account));
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @UserInfo
    @Operation(summary = "프로필 생성", description = "프로필을 생성합니다.")
    public Response<Void> createProfile(@Valid @RequestPart ProfileCreateRequest request,
                              @RequestPart(required = false) MultipartFile profileImage) {
        Account account = accountService.getAccountFromContext();
        profileService.createProfile(account, request, profileImage);
        return Response.success(null);
    }

    @PutMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @UserInfo
    @Valid
    @Operation(summary = "프로필 수정", description = "프로필을 수정합니다.")
    public Response<Void> updateProfile(@Valid @RequestPart ProfileUpdateRequest request,
                              @RequestPart(required = false) MultipartFile profileImage) {
        Account account = accountService.getAccountFromContext();
        profileService.checkProfile(account, request.profileId());
        profileService.updateProfile(request, profileImage);
        return Response.success(null);
    }

    @DeleteMapping("/{profileId}")
    @UserInfo
    @Operation(summary = "프로필 삭제", description = "프로필을 삭제합니다.")
    public Response<Void> deleteProfile(@PathVariable Long profileId) {
        Account account = accountService.getAccountFromContext();
        profileService.checkProfile(account, profileId);
        profileService.deleteProfile(profileId);
        return Response.success(null);
    }
}
