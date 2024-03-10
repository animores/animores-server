package animores.serverapi.diary.service;

import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.EditDiaryRequest;
import animores.serverapi.diary.entity.Diary;

import java.util.List;

public interface DiaryService {

    List<Diary> getAllDiary(Long accountId);

    void getCalendarDiary(Long userId, String date);

    void addDiary(AddDiaryRequest request);

    void editDiary(Long diaryId, EditDiaryRequest request);

    void removeDiary(Long removeDiary);

}
