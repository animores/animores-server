package animores.serverapi.diary.repository.impl;

import static animores.serverapi.diary.entity.QDiaryMedia.diaryMedia;

import animores.serverapi.diary.entity.DiaryMedia;
import animores.serverapi.diary.entity.DiaryMediaType;
import animores.serverapi.diary.repository.DiaryMediaCustomRepository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.swagger.v3.oas.models.media.MediaType;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DiaryMediaCustomRepositoryImpl implements DiaryMediaCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<DiaryMedia> getAllDiaryMediaToReorder(Long diaryId, DiaryMediaType type) {
        return jpaQueryFactory
            .selectFrom(diaryMedia)
            .where(diaryMedia.diary.id.eq(diaryId))
            .orderBy(new CaseBuilder()
                    .when(diaryMedia.type.eq(type)).then(1)
                    .otherwise(0).desc(),
                diaryMedia.id.asc())
            .fetch();
    }
}
