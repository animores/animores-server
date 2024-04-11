package animores.serverapi.diary.service;

import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.EditDiaryRequest;
import animores.serverapi.diary.dto.GetAllDiaryResponse;
import animores.serverapi.diary.dto.GetCalendarDiaryResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface DiaryService {

    GetAllDiaryResponse getAllDiary(int page, int size);

    GetCalendarDiaryResponse getCalendarDiary(Long accountId, LocalDate date);

    void addDiary(AddDiaryRequest request, List<MultipartFile> files) throws IOException;

    void editDiary(Long diaryId, EditDiaryRequest request);

    void removeDiary(Long removeDiary);

}
