package animores.serverapi.diary.service;

public interface DiaryBatchService {

    void insertDiaryBatch(Integer count, Long accountId);

    void insertDiaryCommentBatch(Integer count, Long diaryId);

    void insertDiaryLikeBatch(Integer count, Long accountId);

    void insertDiaryMediaBatch(Integer count, Long accountId, Long maxDiaryId);
}