package animores.serverapi.diary.dto;

import animores.serverapi.diary.dao.GetCalendarDiary;
import java.util.List;
import lombok.Getter;

@Getter
public class GetCalendarDiaryResponse {

    private long totalCount;

    private List<GetCalendarDiary> diaries;

    public GetCalendarDiaryResponse(long totalCount, List<GetCalendarDiary> diaries) {
        this.totalCount = totalCount;
        this.diaries = diaries;
    }
}
