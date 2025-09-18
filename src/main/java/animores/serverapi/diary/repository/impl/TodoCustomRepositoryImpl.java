package animores.serverapi.diary.repository.impl;

import static animores.serverapi.to_do.entity.QToDo.toDo;
import static animores.serverapi.to_do.entity.QToDoInstance.toDoInstance;

import animores.serverapi.diary.repository.TodoCustomRepository;
import animores.serverapi.to_do.dao.GetTodosDao;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GetTodosDao> getTodos(Boolean completed, LocalDate start, LocalDate end, Integer page,
        Integer size) {
        List<GetTodosDao> res = jpaQueryFactory
            .select(Projections.constructor(GetTodosDao.class,
                toDo.id,
                toDo.date,
                toDo.time,
                toDo.isAllDay,
                toDo.content,
                toDo.tag,
                toDo.color,
                toDo.isUsingAlarm,
                toDo.unit,
                toDo.intervalNum
            ))
            .from(toDo)
            .leftJoin(toDoInstance).on(toDoInstance.toDo.id.eq(toDo.id))
            .where(
                completedCondition(completed),
                dateCondition(start, end)
            )
            .orderBy(toDo.date.desc())
            .offset((page != null && size != null) ? (long) (page * size) : 0)
            .limit(size != null ? size : Long.MAX_VALUE)
            .fetch();

        return res;
    }

    private BooleanExpression dateCondition(LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return toDo.date.between(start, end);
        }
        if (start != null) {
            return toDo.date.goe(start);
        }
        if (end != null) {
            return toDo.date.loe(end);
        }
        return null;
    }

    private BooleanExpression completedCondition(Boolean completed) {
        if (completed == null) {
            return null;
        }
        return completed
            ? toDoInstance.completeTime.isNotNull()
            : toDoInstance.completeTime.isNull();
    }
}
