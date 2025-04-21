package animores.serverapi.diary.repository;

import animores.serverapi.diary.dao.GetAllDiaryCommentDao;
import animores.serverapi.diary.dao.GetAllDiaryReplyDao;
import java.util.List;

public interface DiaryCommentCustomRepository {

    List<GetAllDiaryCommentDao> getAllDiaryComment(Long diaryId, int page, int size);

    List<GetAllDiaryReplyDao> getAllDiaryReply(Long commentId, int page, int size);

    Long getAllDiaryCommentCount(Long diaryId);
}
