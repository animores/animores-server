package animores.serverapi.diary.dto;

import animores.serverapi.diary.dao.GetCalendarDiaryDao;
import java.util.List;
import lombok.Getter;

@Getter
public class GetCalendarDiaryResponse {

    private long totalCount;

    private List<GetCalendarDiaryDao> diaries;

    public GetCalendarDiaryResponse(long totalCount, List<GetCalendarDiaryDao> diaries) {
        this.totalCount = totalCount;
        this.diaries = diaries;
    }
}
