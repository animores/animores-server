package animores.serverapi.diary.controller;

import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.service.DiaryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * 일지 전체 조회
     *
     * @return
     */
    @GetMapping("")
    public List<Diary> getAllDiary() {
        return diaryService.getAllDiary();
    }

    @PostMapping("")
    public ResponseEntity<Void> addDiary(@RequestBody AddDiaryRequest addDiaryRequest) {
        return diaryService.addDiary(addDiaryRequest);
    }

}
