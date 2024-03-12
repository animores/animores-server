package animores.serverapi.diary.repository.impl;

import static animores.serverapi.diary.entity.QDiary.diary;

import animores.serverapi.diary.dto.GetAllDiary;
import animores.serverapi.diary.repository.DiaryCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DiaryCustomRepositoryImpl implements DiaryCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GetAllDiary> getAllDiary(Long accountId) {
        return jpaQueryFactory.select(
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
            .orderBy(diary.id.desc())
            .fetch();
    }

}
