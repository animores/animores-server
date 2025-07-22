package animores.serverapi.diary.repository.impl;

import static animores.serverapi.diary.entity.QDiaryComment.diaryComment;
import static animores.serverapi.diary.entity.QDiaryReply.diaryReply;
import static animores.serverapi.profile.entity.QProfile.profile;

import animores.serverapi.diary.dao.GetAllDiaryCommentDao;
import animores.serverapi.diary.dao.GetAllDiaryReplyDao;
import animores.serverapi.diary.repository.DiaryCommentCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DiaryCommentCustomRepositoryImpl implements DiaryCommentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GetAllDiaryCommentDao> getAllDiaryComment(Long diaryId,
        int page, int size) {
        return jpaQueryFactory.select(
                Projections.fields(GetAllDiaryCommentDao.class,
                    diaryComment.id.as("commentId"),
                    diaryComment.content,
                    diaryComment.createdAt,
                    profile.id.as("profileId"),
                    profile.name,
                    profile.imageUrl,
                    diaryReply.count().as("replyCount")
                )
            )
            .from(diaryComment)
            .leftJoin(profile)
            .on(profile.id.eq(diaryComment.profile.id))
            .leftJoin(diaryReply)
            .on(diaryComment.id.eq(diaryReply.diaryComment.id).and(diaryReply.deletedDt.isNull()))
            .where(diaryComment.diary.id.eq(diaryId).and(diaryComment.deletedDt.isNull()))
            .groupBy(diaryComment.id)
            .orderBy(diaryComment.id.desc())
            .offset((long) (page - 1) * size)
            .limit(size)
            .fetch();
    }

    @Override
    public List<GetAllDiaryReplyDao> getAllDiaryReply(Long commentId, int page, int size) {
        return jpaQueryFactory.select(
                Projections.fields(GetAllDiaryReplyDao.class,
                    diaryReply.id.as("replyId"),
                    diaryReply.content,
                    diaryReply.createdAt,
                    profile.id.as("profileId"),
                    profile.name,
                    profile.imageUrl
                )
            )
            .from(diaryReply)
            .leftJoin(profile)
            .on(profile.id.eq(diaryReply.profile.id))
            .where(diaryReply.diaryComment.id.eq(commentId).and(diaryReply.deletedDt.isNull()))
            .orderBy(diaryReply.id.desc())
            .offset((long) (page - 1) * size)
            .limit(size)
            .fetch();
    }

    @Override
    public Long getAllDiaryCommentCount(Long diaryId) {
        return jpaQueryFactory
            .select(diaryComment.count())
            .from(diaryComment)
            .where(diaryComment.diary.id.eq(diaryId))
            .fetchOne();
    }

}
