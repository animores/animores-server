package animores.serverapi.diary.controller;

import animores.serverapi.account.entity.Account;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.Response;
import animores.serverapi.common.aop.UserInfo;
import animores.serverapi.diary.dto.AddDiaryReplyRequest;
import animores.serverapi.diary.dto.EditDiaryReplyRequest;
import animores.serverapi.diary.dto.RemoveDiaryReplyRequest;
import animores.serverapi.diary.service.DiaryReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@PreAuthorize("hasAuthority('USER')")
//@SecurityRequirement(name = "Authorization")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diary-reply")
public class DiaryReplyController {

    private final AccountService accountService;
    private final DiaryReplyService diaryReplyService;

    @UserInfo
    @PostMapping("")
    @Operation(summary = "대댓글 작성", description = "대댓글을 작성합니다.")
    public Response<Void> addDiaryReply(
        @RequestBody @Parameter(description = "대댓글 작성에 대한 요청 데이터", required = true) AddDiaryReplyRequest request) {
        Account account = accountService.getAccountFromContext();
        diaryReplyService.addDiaryReply(account, request);
        return Response.success(null);
    }

    @UserInfo
    @PatchMapping("/{replyId}")
    @Operation(summary = "대댓글 수정", description = "대댓글을 수정합니다.")
    public Response<Void> editDiaryReply(
        @PathVariable @Parameter(description = "대댓글 아이디", required = true) Long replyId,
        @RequestBody @Parameter(description = "대댓글 수정에 대한 요청 데이터", required = true) EditDiaryReplyRequest request) {
        Account account = accountService.getAccountFromContext();
        diaryReplyService.editDiaryReply(account, replyId, request);
        return Response.success(null);
    }

    @UserInfo
    @DeleteMapping("/{replyId}")
    @Operation(summary = "대댓글 삭제", description = "대댓글을 삭제합니다.")
    public Response<Void> removeDiaryReply(
        @PathVariable @Parameter(description = "대댓글 아이디", required = true) Long replyId,
        @RequestBody @Parameter(description = "대댓글 삭제에 대한 요청 데이터", required = true) RemoveDiaryReplyRequest request) {
        Account account = accountService.getAccountFromContext();
        diaryReplyService.removeDiaryReply(account, replyId, request);
        return Response.success(null);

    }
}
