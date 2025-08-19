package animores.serverapi.diary.repository;

import animores.serverapi.diary.dao.GetAllDiaryDao;
import animores.serverapi.diary.dao.GetCalendarDiaryDao;
import com.querydsl.core.QueryResults;
import java.time.LocalDate;
import java.util.List;

public interface DiaryCustomRepository {

    List<GetAllDiaryDao> getAllDiary(String accountId, Long profileId, int page, int size);

    Long getAllDiaryCount(String accountId);

    QueryResults<GetCalendarDiaryDao> getCalendarDiary(String accountId, LocalDate date);
}
