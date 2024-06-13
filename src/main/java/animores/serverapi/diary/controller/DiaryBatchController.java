package animores.serverapi.diary.controller;

import animores.serverapi.common.Response;
import animores.serverapi.diary.service.DiaryBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries/batch")
@RestController
public class DiaryBatchController {

    private final DiaryBatchService diaryBatchService;

    @Operation(summary = "일지 배치 생성", description = "일지를 배치로 생성합니다.")
    @PostMapping("/")
    public Response<Void> insertDiaryBatch(@RequestParam
                                           @Parameter(description = "배치 개수", required = true, example = "100")
                                           Integer count,
                                           @RequestParam
                                           @Parameter(description = "일지를 생성할 계정 id", required = true, example = "1")
                                           Long accountId) {
        diaryBatchService.insertDiaryBatch(count, accountId);
        return Response.success(null);
    }

    @Operation(summary = "일지 댓글 배치 생성", description = "일지 댓글을 배치로 생성합니다.")
    @PostMapping("/comment")
    public Response<Void> insertDiaryCommentBatch(@RequestParam
                                                  @Parameter(description = "배치 개수", required = true, example = "100")
                                                  Integer count,
                                                  @RequestParam
                                                  @Parameter(description = "일지 id", required = true, example = "1")
                                                  Long diaryId) {
        diaryBatchService.insertDiaryCommentBatch(count, diaryId);
        return Response.success(null);
    }

    @Operation(summary = "일지 좋아요 배치 생성", description = "일지 좋아요를 배치로 생성합니다. 입력 받은 계정 id 의 diary 들에 계정의 profile 들이 좋아요를 남깁니다.")
    @PostMapping("/like")
    public Response<Void> insertDiaryLikeBatch(@RequestParam
                                                   @Parameter(description = "배치 개수", required = true, example = "100")
                                                   Integer count,
                                               @RequestParam
                                                   @Parameter(description = "계정 id", required = true, example = "1")
                                                   Long accountId) {
        diaryBatchService.insertDiaryLikeBatch(count, accountId);
        return Response.success(null);
    }
}
