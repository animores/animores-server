package animores.serverapi.diary.repository.impl;

import static animores.serverapi.diary.entity.QDiary.diary;
import static animores.serverapi.diary.entity.QDiaryContent.diaryContent;

import animores.serverapi.diary.dao.GetAllDiary;
import animores.serverapi.diary.dao.GetAllDiaryContent;
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
    public QueryResults<GetAllDiary> getAllDiary(Long accountId) {
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
            .leftJoin(diary.diaryContents, diaryContent)
            .where(diary.account.id.eq(accountId))
            .orderBy(diary.id.desc())
            .fetchResults();

        diaries.getResults().forEach(diary -> {
            System.out.println("diaryId");
            System.out.println(diary.getDiaryId());
            List<GetAllDiaryContent> contents = jpaQueryFactory
                .select(Projections.fields(GetAllDiaryContent.class,
                    diaryContent.id.as("contentId"),
                    diaryContent.url.as("contentUrl"),
                    diaryContent.contentOrder,
                    diaryContent.type
                ))
                .from(diaryContent)
                .where(diaryContent.diary.id.eq(diary.getDiaryId()))
                .orderBy(diaryContent.contentOrder.asc())
                .fetch();

            diary.setDiaryContents(contents);
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
