package animores.serverapi.diary.service;

import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.EditDiaryRequest;
import animores.serverapi.diary.dto.GetAllDiary;
import java.util.List;

public interface DiaryService {

    List<GetAllDiary> getAllDiary(Long accountId);

    void getCalendarDiary(Long userId, String date);

    void addDiary(AddDiaryRequest request);

    void editDiary(Long diaryId, EditDiaryRequest request);

    void removeDiary(Long removeDiary);

}
