package animores.serverapi.diary.service;

import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.EditDiaryRequest;
import animores.serverapi.diary.entity.Diary;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface DiaryService {

    List<Diary> getAllDiary(Long accountId);

    void getCalendarDiary(Long userId, String date);

    ResponseEntity<Void> addDiary(AddDiaryRequest request);

    ResponseEntity<Void> editDiary(Long diaryId, EditDiaryRequest request);
    ResponseEntity<Void> removeDiary(Long removeDiary);

}
