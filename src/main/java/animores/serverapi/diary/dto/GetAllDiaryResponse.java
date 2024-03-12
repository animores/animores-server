package animores.serverapi.diary.dto;

import animores.serverapi.diary.dao.GetAllDiary;
import java.util.List;
import lombok.Getter;

@Getter
public class GetAllDiaryResponse {

    private long totalCount;

    private List<GetAllDiary> diaries;

    public GetAllDiaryResponse(long totalCount, List<GetAllDiary> diaries) {
        this.totalCount = totalCount;
        this.diaries = diaries;
    }
}
