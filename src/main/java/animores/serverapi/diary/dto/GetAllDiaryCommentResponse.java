package animores.serverapi.diary.dto;

import animores.serverapi.diary.dao.GetAllDiaryCommentDao;
import java.util.List;
import lombok.Getter;

@Getter
public class GetAllDiaryCommentResponse {

    private long totalCount;

    private List<GetAllDiaryCommentDao> comments;

    public GetAllDiaryCommentResponse(long totalCount, List<GetAllDiaryCommentDao> comments) {
        this.totalCount = totalCount;
        this.comments = comments;
    }
}
