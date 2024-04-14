package animores.serverapi.diary.controller;

import animores.serverapi.common.Response;
import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.EditDiaryRequest;
import animores.serverapi.diary.dto.GetAllDiaryResponse;
import animores.serverapi.diary.dto.GetCalendarDiaryResponse;
import animores.serverapi.diary.service.DiaryService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    // 일지 목록 조회
    @GetMapping("")
    public Response<GetAllDiaryResponse> getAllDiary(@RequestParam("page") int page,
        @RequestParam("size") int size) {
        return Response.success(diaryService.getAllDiary(page, size));
    }

    // 일지 요약 목록 조회
    @GetMapping("/calendar")
    public Response<GetCalendarDiaryResponse> getCalendarDiary(
        @RequestParam("date") LocalDate date) {
        Long accountId = 1L;
        return Response.success(diaryService.getCalendarDiary(accountId, date));
    }

    // 일지 등록
    @PostMapping("")
    public Response<Void> addDiary(@RequestPart(name = "request") AddDiaryRequest request,
        @RequestPart(name="files", required = false) List<MultipartFile> files) throws IOException {
        diaryService.addDiary(request, files);
        return Response.success(null);
    }

    // 일지 수정
    @PatchMapping("/{diaryId}")
    public Response<Void> editDiary(@PathVariable Long diaryId,
        @RequestBody EditDiaryRequest request) {
        diaryService.editDiary(diaryId, request);
        return Response.success(null);
    }

    @DeleteMapping("/{diaryId}")
    public Response<Void> removeDiary(@PathVariable Long diaryId) {
        diaryService.removeDiary(diaryId);
        return Response.success(null);
    }

}
