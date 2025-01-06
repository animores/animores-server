package animores.serverapi.diary.service;

import animores.serverapi.account.entity.Account;
import animores.serverapi.diary.dto.AddDiaryCommentRequest;
import animores.serverapi.diary.dto.EditDiaryCommentRequest;
import animores.serverapi.diary.dto.GetAllDiaryReplyResponse;
import animores.serverapi.diary.dto.RemoveDiaryCommentRequest;

public interface DiaryCommentService {

    public void addDiaryComment(Account account, AddDiaryCommentRequest request);

    public void editDiaryComment(Account account, Long commentId, EditDiaryCommentRequest request);

    public void removeDiaryComment(Account account, Long commentId,
        RemoveDiaryCommentRequest request);

    public GetAllDiaryReplyResponse getAllDiaryReply(Account account, Long commentId,
        Long profileId, int page, int size);

}
