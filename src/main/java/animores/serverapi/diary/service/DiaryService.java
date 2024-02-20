package animores.serverapi.diary.service;

import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.entity.Diary;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface DiaryService {

    List<Diary> getAllDiary();

    ResponseEntity<Void> addDiary(AddDiaryRequest addDiaryRequest);

}
