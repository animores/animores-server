package animores.serverapi.diary.service;

public interface DiaryBatchService {

    void insertDiaryBatch(Integer count, String accountId);

    void insertDiaryCommentBatch(Integer count, Long diaryId);

    void insertDiaryLikeBatch(Integer count, String accountId);

    void insertDiaryMediaBatch(Integer count, String accountId, Long maxDiaryId);
}