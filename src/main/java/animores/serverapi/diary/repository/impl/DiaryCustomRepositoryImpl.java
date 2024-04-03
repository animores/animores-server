package animores.serverapi.diary.repository.impl;

import static animores.serverapi.diary.entity.QDiary.diary;
import static animores.serverapi.diary.entity.QDiaryMedia.diaryMedia;

import animores.serverapi.diary.dao.GetAllDiary;
import animores.serverapi.diary.dao.GetAllDiaryMedia;
import animores.serverapi.diary.dao.GetCalendarDiary;
import animores.serverapi.diary.repository.DiaryCustomRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
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
    public QueryResults<GetAllDiary> getAllDiary(Long accountId, int page, int size) {
        QueryResults<GetAllDiary> diaries = jpaQueryFactory.select(
                Projections.fields(GetAllDiary.class,
                    diary.id.as("diaryId"),
                    diary.content,
                    diary.createdAt,
                    diary.profile.id.as("profileId"),
                    diary.profile.name,
                    diary.profile.imageUrl
                )
            )
            .from(diary)
            .where(diary.account.id.eq(accountId))
            .leftJoin(diary.media, diaryMedia)
            .distinct()
            .orderBy(diary.id.desc())
            .offset(page - 1)
            .limit(size)
            .fetchResults();

        diaries.getResults().forEach(diary -> {
            List<GetAllDiaryMedia> media = jpaQueryFactory
                .select(Projections.fields(GetAllDiaryMedia.class,
                    diaryMedia.id,
                    diaryMedia.url,
                    diaryMedia.mediaOrder,
                    diaryMedia.type
                ))
                .from(diaryMedia)
                .where(diaryMedia.diary.id.eq(diary.getDiaryId()))
                .orderBy(diaryMedia.mediaOrder.asc())
                .fetch();

            diary.setDiaryMedia(media);
        });

        return diaries;
    }

    @Override
    public QueryResults<GetCalendarDiary> getCalendarDiary(Long accountId, LocalDate date) {

        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.atTime(LocalTime.MAX);

        return jpaQueryFactory.select(
                Projections.fields(GetCalendarDiary.class,
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

}
