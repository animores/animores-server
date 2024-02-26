package animores.serverapi.diary.controller;

import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.EditDiaryRequest;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.service.DiaryService;
import jakarta.ws.rs.Path;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    // 일지 전체 목록 조회
    @GetMapping("/{accountId}")
    public List<Diary> getAllDiary(@PathVariable Long accountId) {
        return diaryService.getAllDiary(accountId);
    }

    // 일지 요약 목록 조회
    @GetMapping("/{userId}/{date}")
    public void getCalendarDiary(@PathVariable Long userId, @PathVariable String date) {
        diaryService.getCalendarDiary(userId, date);
    }

    // 일지 등록
    @PostMapping("")
    public ResponseEntity<Void> addDiary(@RequestBody AddDiaryRequest request) {
        return diaryService.addDiary(request);
    }

    // 일지 수정
    @PatchMapping("/{diaryId}")
    public ResponseEntity<Void> editDiary(@PathVariable Long diaryId, @RequestBody EditDiaryRequest request) {
        return diaryService.editDiary(diaryId, request);
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> removeDiary(@PathVariable Long diaryId) {
        return diaryService.removeDiary(diaryId);
    }

}
