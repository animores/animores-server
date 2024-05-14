package animores.serverapi.diary.controller;

import animores.serverapi.common.Response;
import animores.serverapi.diary.dto.*;
import animores.serverapi.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<Void> addDiary(@RequestPart(name = "request") AddDiaryRequest request,
        @RequestPart(name="files", required = false) List<MultipartFile> files) throws IOException {
        diaryService.addDiary(request, files);
        return Response.success(null);
    }

    // 일지 내용 수정
    @PatchMapping("/{diaryId}")
    public Response<Void> editDiaryContent(@PathVariable Long diaryId,
        @RequestBody EditDiaryContentRequest request) {
        diaryService.editDiaryContent(diaryId, request);
        return Response.success(null);
    }

    // 일지 내용+미디어 수정
//    @PatchMapping("/{diaryId}")
//    public Response<Void> editDiaryContent(@PathVariable Long diaryId,
//        @RequestBody EditDiaryRequest request) {
//        diaryService.editDiaryContent(diaryId, request);
//        return Response.success(null);
//    }

    // 일지 미디어 추가
    @PostMapping("/{diaryId}/diary-media")
    public Response<Void> addDiaryMedia(@PathVariable Long diaryId,
        @RequestPart(name = "files") List<MultipartFile> files) throws IOException {
        diaryService.addDiaryMedia(diaryId, files);
        return Response.success(null);
    }

    // 일지 미디어 수정 (삭제+추가)
    @PutMapping("/{diaryId}/diary-media")
    public Response<Void> editDiaryMedia(@PathVariable Long diaryId,
        @RequestPart EditDiaryMediaRequest request,
        @RequestPart(name="files") List<MultipartFile> files) throws IOException {
        diaryService.editDiaryMedia(diaryId, request, files);
        return Response.success(null);
    }

    // 일지 미디어 삭제
    @DeleteMapping("/{diaryId}/diary-media")
    public Response<Void> removeDiaryMedia(@PathVariable Long diaryId,
        @RequestBody EditDiaryMediaRequest request) {
        diaryService.removeDiaryMedia(diaryId, request);
        return Response.success(null);
    }

    @DeleteMapping("/{diaryId}")
    public Response<Void> removeDiary(@PathVariable Long diaryId) {
        diaryService.removeDiary(diaryId);
        return Response.success(null);
    }

}
