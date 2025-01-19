package animores.serverapi.diary.dto;

import animores.serverapi.diary.dao.GetAllDiaryDao;
import java.util.List;
import lombok.Getter;

@Getter
public class GetAllDiaryResponse {

    private long totalCount;

    private List<GetAllDiaryDao> diaries;

    public GetAllDiaryResponse(long totalCount, List<GetAllDiaryDao> diaries) {
        this.totalCount = totalCount;
        this.diaries = diaries;
    }
}