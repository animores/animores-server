package animores.serverapi.diary.controller;

import animores.serverapi.common.Response;
import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.EditDiaryRequest;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    // 일지 전체 목록 조회
    @GetMapping("/accounts/{accountId}")
    public Response<List<Diary>> getAllDiary(@PathVariable Long accountId) {
        return Response.success(diaryService.getAllDiary(accountId));
    }

    // 일지 요약 목록 조회
    @GetMapping("/{userId}/{date}")
    // @GetMapping("/accounts/{accountId}/{date}") querydsl 적용하면서 이거로 수정 예정
    public Response<Void> getCalendarDiary(@PathVariable Long userId, @PathVariable String date) {
        diaryService.getCalendarDiary(userId, date);
        return Response.success(null);
    }

    // 일지 등록
    @PostMapping("")
    public Response<Void> addDiary(@RequestBody AddDiaryRequest request) {
        diaryService.addDiary(request);
        return Response.success(null);
    }

    // 일지 수정
    @PatchMapping("/{diaryId}")
    public Response<Void> editDiary(@PathVariable Long diaryId, @RequestBody EditDiaryRequest request) {
        diaryService.editDiary(diaryId, request);
        return Response.success(null);
    }

    @DeleteMapping("/{diaryId}")
    public Response<Void> removeDiary(@PathVariable Long diaryId) {
        diaryService.removeDiary(diaryId);
        return Response.success(null);
    }

}
