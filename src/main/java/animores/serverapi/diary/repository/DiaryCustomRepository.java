package animores.serverapi.diary.repository;

import animores.serverapi.diary.dao.GetAllDiaryDao;
import animores.serverapi.diary.dao.GetCalendarDiaryDao;
import com.querydsl.core.QueryResults;
import java.time.LocalDate;
import java.util.List;

public interface DiaryCustomRepository {

    List<GetAllDiaryDao> getAllDiary(Long accountId, int page, int size);

    QueryResults<GetCalendarDiaryDao> getCalendarDiary(Long accountId, LocalDate date);
}
