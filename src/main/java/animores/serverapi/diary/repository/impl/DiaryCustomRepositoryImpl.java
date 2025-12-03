package animores.serverapi.diary.repository.impl;

import static animores.serverapi.diary.entity.QDiary.diary;
import static animores.serverapi.diary.entity.QDiaryComment.diaryComment;
import static animores.serverapi.diary.entity.QDiaryLike.diaryLike;
import static animores.serverapi.diary.entity.QDiaryMedia.diaryMedia;
import static animores.serverapi.diary.entity.QDiaryReply.diaryReply;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import animores.serverapi.diary.dao.GetAllDiaryDao;
import animores.serverapi.diary.dao.GetCalendarDiaryDao;
import animores.serverapi.diary.dao.QGetAllDiaryDao;
import animores.serverapi.diary.dao.QGetAllDiaryMediaDao;
import animores.serverapi.diary.repository.DiaryCustomRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DiaryCustomRepositoryImpl implements DiaryCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GetAllDiaryDao> getAllDiary(String accountId, Long profileId, int page, int size) {
        return jpaQueryFactory
            .from(diary)
            .leftJoin(diaryMedia)
            .on(diary.id.eq(diaryMedia.diary.id))
            .leftJoin(diaryLike)
            .on(diary.id.eq(diaryLike.diary.id).and(diaryLike.profile.id.eq(profileId)))
            .where(diary.account.id.eq(accountId).and(diary.deletedDt.isNull()))
            .orderBy(diary.id.desc())
            .offset((long) (page - 1) * size)
            .limit(size)
            .transform(
                groupBy(diary.id).list(
                    new QGetAllDiaryDao(
                        diary.id,
                        diary.content,
                        list(
                            new QGetAllDiaryMediaDao(diaryMedia.id, diaryMedia.url,
                                diaryMedia.mediaOrder, diaryMedia.type).skipNulls()
                        ),
                        createLikeYnExpression(diary.id, profileId),
                        Expressions.numberTemplate(Integer.class,
                            "({0})",
                            JPAExpressions
                                .select(diaryComment.count())
                                .from(diaryComment)
                                .where(
                                    diaryComment.diary.id.eq(diary.id)
                                        .and(diaryComment.deletedDt.isNull())
                                )
                        ),
                        Expressions.numberTemplate(Integer.class,
                            "({0})",
                            JPAExpressions
                                .select(diaryReply.count())
                                .from(diaryReply)
                                .where(
                                    diaryComment.diary.id.eq(diary.id)
                                        .and(diaryReply.deletedDt.isNull())
                                        .and(diaryComment.deletedDt.isNull())
                                )
                        ),
                        diary.createdAt,
                        diary.profile.id,
                        diary.profile.name,
                        diary.profile.imageUrl
                    )
                )
            );
    }

    @Override
    public List<GetAllDiaryDao> getDiaryByPeriod(String accountId, Long profileId,
        LocalDate startDate, LocalDate endDate, int page, int size) {
        return jpaQueryFactory
            .from(diary)
            .leftJoin(diaryMedia)
            .on(diary.id.eq(diaryMedia.diary.id))
            .leftJoin(diaryLike)
            .on(diary.id.eq(diaryLike.diary.id).and(diaryLike.profile.id.eq(profileId)))
            .where(
                diary.account.id.eq(accountId),
                diary.deletedDt.isNull(),
                profileCondition(profileId),
                dateCondition(startDate, endDate)
            )
            .orderBy(diary.id.desc())
            .offset((long) (page - 1) * size)
            .limit(size)
            .transform(
                groupBy(diary.id).list(
                    new QGetAllDiaryDao(
                        diary.id,
                        diary.content,
                        list(
                            new QGetAllDiaryMediaDao(diaryMedia.id, diaryMedia.url,
                                diaryMedia.mediaOrder, diaryMedia.type).skipNulls()
                        ),
                        createLikeYnExpression(diary.id, profileId),
                        Expressions.numberTemplate(Integer.class,
                            "({0})",
                            JPAExpressions
                                .select(diaryComment.count())
                                .from(diaryComment)
                                .where(
                                    diaryComment.diary.id.eq(diary.id)
                                        .and(diaryComment.deletedDt.isNull())
                                )
                        ),
                        Expressions.numberTemplate(Integer.class,
                            "({0})",
                            JPAExpressions
                                .select(diaryReply.count())
                                .from(diaryReply)
                                .where(
                                    diaryComment.diary.id.eq(diary.id)
                                        .and(diaryReply.deletedDt.isNull())
                                        .and(diaryComment.deletedDt.isNull())
                                )
                        ),
                        diary.createdAt,
                        diary.profile.id,
                        diary.profile.name,
                        diary.profile.imageUrl
                    )
                )
            );
    }

    public Long getAllDiaryCount(String accountId) {
        return jpaQueryFactory
            .select(diary.count())
            .from(diary)
            .where(diary.account.id.eq(accountId)
                .and(diary.deletedDt.isNull()))
            .fetchOne();
    }

    @Override
    public Long getDiaryByPeriodCount(String accountId, Long profileId, LocalDate startDate,
        LocalDate endDate) {
        return jpaQueryFactory
            .select(diary.count())
            .from(diary)
            .where(
                diary.account.id.eq(accountId),
                diary.deletedDt.isNull(),
                profileCondition(profileId),
                dateCondition(startDate, endDate)
            )
            .fetchOne();
    }

    @Override
    public QueryResults<GetCalendarDiaryDao> getCalendarDiary(String accountId, LocalDate date) {

        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.atTime(LocalTime.MAX);

        return jpaQueryFactory.select(
                Projections.fields(GetCalendarDiaryDao.class,
                    diary.id.as("diaryId"),
                    diary.content,
                    diary.createdAt,
                    diary.profile.id.as("profileId"),
                    diary.profile.name,
                    diary.profile.imageUrl
                )
            )
            .from(diary)
            .where(
                diary.account.id.eq(accountId)
                    .and(diary.createdAt.between(startDateTime, endDateTime))
            )
            .orderBy(diary.id.desc())
            .fetchResults();
    }

    private BooleanExpression profileCondition(Long profileId) {
        if (profileId == null) {
            return null;
        }
        return diary.profile.id.eq(profileId);
    }

    private BooleanExpression dateCondition(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return diary.createdAt.between(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
        }
        if (startDate != null) {
            return diary.createdAt.goe(startDate.atStartOfDay());
        }
        if (endDate != null) {
            return diary.createdAt.loe(endDate.atTime(LocalTime.MAX));
        }
        return null;
    }

    private BooleanExpression createLikeYnExpression(NumberPath<Long> diaryId, Long profileId) {
        return new CaseBuilder()
            .when(diaryLike.diary.id.eq(diaryId).and(diaryLike.profile.id.eq(profileId)))
            .then(true)
            .otherwise(false);
    }

}
