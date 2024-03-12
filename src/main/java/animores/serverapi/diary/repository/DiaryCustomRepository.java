package animores.serverapi.diary.repository;

import animores.serverapi.diary.dao.GetAllDiary;
import animores.serverapi.diary.dao.GetCalendarDiary;
import com.querydsl.core.QueryResults;
import java.time.LocalDate;

public interface DiaryCustomRepository {

    QueryResults<GetAllDiary> getAllDiary(Long accountId);

    QueryResults<GetCalendarDiary> getCalendarDiary(Long accountId, LocalDate date);
}
