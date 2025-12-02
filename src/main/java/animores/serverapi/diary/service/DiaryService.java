package animores.serverapi.diary.service;

import animores.serverapi.account.entity.Account;
import animores.serverapi.diary.dto.AddDiaryLikeRequest;
import animores.serverapi.diary.dto.AddDiaryMediaRequest;
import animores.serverapi.diary.dto.AddDiaryRequest;
import animores.serverapi.diary.dto.CancelDiaryLikeRequest;
import animores.serverapi.diary.dto.EditDiaryContentRequest;
import animores.serverapi.diary.dto.EditDiaryMediaRequest;
import animores.serverapi.diary.dto.GetAllDiaryCommentResponse;
import animores.serverapi.diary.dto.GetAllDiaryResponse;
import animores.serverapi.diary.dto.GetCalendarDiaryResponse;
import animores.serverapi.diary.dto.RemoveDiaryRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface DiaryService {

    GetAllDiaryResponse getAllDiary(Account account, Long profileId, int page, int size);

    GetAllDiaryResponse getDiaryByPeriod(Account account, Long profileId, String start,
        String end, Integer page, Integer size);

    GetCalendarDiaryResponse getCalendarDiary(Account account, Long profileId, LocalDate date);

    void addDiary(Account account, AddDiaryRequest request, List<MultipartFile> files)
        throws IOException;

    void editDiaryContent(Account account, Long diaryId, EditDiaryContentRequest request);

    void addDiaryMedia(Account account, Long diaryId, AddDiaryMediaRequest request,
        List<MultipartFile> files) throws IOException;

    void editDiaryMedia(Account account, Long diaryId, EditDiaryMediaRequest request,
        List<MultipartFile> files) throws IOException;

    void removeDiaryMedia(Account account, Long diaryId, EditDiaryMediaRequest request);

    void removeDiary(Account account, Long diaryId, RemoveDiaryRequest request);

    void addDiaryLike(Account account, Long diaryId, AddDiaryLikeRequest request);

    void cancelDiaryLike(Account account, Long diaryId, CancelDiaryLikeRequest request);

    GetAllDiaryCommentResponse getAllDiaryComment(Account account, Long diaryId, Long profileId,
        int page, int size);

}
