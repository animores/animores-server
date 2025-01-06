package animores.serverapi.diary.service;

import animores.serverapi.account.entity.Account;
import animores.serverapi.diary.dto.AddDiaryReplyRequest;
import animores.serverapi.diary.dto.EditDiaryReplyRequest;
import animores.serverapi.diary.dto.RemoveDiaryReplyRequest;

public interface DiaryReplyService {

    public void addDiaryReply(Account account, AddDiaryReplyRequest request);

    public void editDiaryReply(Account account, Long replyId, EditDiaryReplyRequest request);

    public void removeDiaryReply(Account account, Long replyId, RemoveDiaryReplyRequest request);

}
