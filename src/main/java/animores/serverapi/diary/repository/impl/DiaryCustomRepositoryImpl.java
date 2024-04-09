package animores.serverapi.diary.repository.impl;

import static animores.serverapi.diary.entity.QDiary.diary;
import static animores.serverapi.diary.entity.QDiaryMedia.diaryMedia;
import static animores.serverapi.diary.entity.QDiaryWish.diaryWish;
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
import com.querydsl.core.types.dsl.NumberExpression;
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
    public List<GetAllDiaryDao> getAllDiary(Long accountId, Long profileId, int page, int size) {
        BooleanExpression wishYn = createWishYnExpression(profileId);

        List<GetAllDiaryDao> diaries = jpaQueryFactory
            .from(diary)
            .leftJoin(diaryWish)
            .on(diary.id.eq(diaryWish.diary.id))
            .leftJoin(diaryMedia)
            .on(diary.id.eq(diaryMedia.diary.id))
            .where(diary.account.id.eq(accountId))
            .orderBy(diary.id.desc())
            .offset(page - 1)
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
                        wishYn,
                        diary.createdAt,
                        diary.profile.id,
                        diary.profile.name,
                        diary.profile.imageUrl
                    )
                )
            );

        return diaries;
    }

    public Long getAllDiaryCount(Long accountId) {
        return jpaQueryFactory
            .select(diary.count())
            .from(diary)
            .where(diary.account.id.eq(accountId))
            .fetchOne();
    }

    @Override
    public QueryResults<GetCalendarDiaryDao> getCalendarDiary(Long accountId, LocalDate date) {

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

    private BooleanExpression createWishYnExpression(Long profileId) {
        return new CaseBuilder()
            .when(diaryWish.profile.id.eq(profileId))
            .then(true)
            .otherwise(false);
    }

}
