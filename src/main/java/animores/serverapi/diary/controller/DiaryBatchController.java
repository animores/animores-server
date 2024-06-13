package animores.serverapi.diary.controller;

import animores.serverapi.common.Response;
import animores.serverapi.diary.service.DiaryBatchService;
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

    @PostMapping("/")
    public Response<Void> insertDiaryBatch(@RequestParam Integer count, @RequestParam Long accountId) {
        diaryBatchService.insertDiaryBatch(count, accountId);
        return Response.success(null);
    }

    @PostMapping("/comment")
    public Response<Void> insertDiaryCommentBatch(@RequestParam Integer count, @RequestParam Long diaryId) {
        diaryBatchService.insertDiaryCommentBatch(count, diaryId);
        return Response.success(null);
    }

    @PostMapping("/like")
    public Response<Void> insertDiaryLikeBatch(@RequestParam Integer count, @RequestParam Long accountId) {
        diaryBatchService.insertDiaryLikeBatch(count, accountId);
        return Response.success(null);
    }
}
