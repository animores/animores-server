package animores.serverapi.profile.controller;

import animores.serverapi.account.entity.Account;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.Response;
import animores.serverapi.common.aop.UserInfo;
import animores.serverapi.profile.dto.request.ProfileCreateRequest;
import animores.serverapi.profile.dto.request.ProfileUpdateRequest;
import animores.serverapi.profile.dto.response.ProfileResponse;
import animores.serverapi.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/profiles")
@Validated
@UserInfo
public class ProfileController {

    private final AccountService accountService;
    private final ProfileService profileService;

    @GetMapping("")
    @Operation(summary = "프로필 목록 조회", description = "프로필 목록을 조회합니다.")
    public Response<List<ProfileResponse>> getProfiles() {
        Account account = accountService.getAccountFromContext();
        return Response.success(profileService.getProfiles(account));
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "프로필 생성", description = "프로필을 생성합니다.")
    public Response<Void> createProfile(@Valid @RequestPart ProfileCreateRequest request,
        @RequestPart(required = false) MultipartFile profileImage) {
        Account account = accountService.getAccountFromContext();
        profileService.createProfile(account, request, profileImage);
        return Response.success(null);
    }

    @PutMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Valid
    @Operation(summary = "프로필 수정",
        description = """
            프로필을 수정합니다.
            request에 isUpdateImage parameter 를 통해 image를 수정할지 여부를 결정할 수 있습니다.
            isUpdateImage 가 true 일 경우 profileImage 가 null 일 수도 있고 아닐 수도 있습니다.
            profileImage 가 null 일 경우 기존 이미지를 삭제하고 기본 이미지로 변경됩니다.
            profileImage 가 null 이 아닐 경우 새로운 이미지로 변경됩니다.
            """)
    public Response<Void> updateProfile(@Valid @RequestPart ProfileUpdateRequest request,
        @RequestPart(required = false) MultipartFile profileImage) {
        Account account = accountService.getAccountFromContext();
        profileService.checkProfile(account, request.profileId());
        profileService.updateProfile(request, profileImage);
        return Response.success(null);
    }

    @DeleteMapping("/{profileId}")
    @Operation(summary = "프로필 삭제", description = "프로필을 삭제합니다.")
    public Response<Void> deleteProfile(@PathVariable Long profileId) {
        Account account = accountService.getAccountFromContext();
        profileService.checkProfile(account, profileId);
        profileService.deleteProfile(profileId);
        return Response.success(null);
    }
}
