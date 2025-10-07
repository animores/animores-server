package animores.serverapi.diary.repository.impl;

import static animores.serverapi.pet.entity.QPet.pet;
import static animores.serverapi.to_do.entity.QPetToDoRelationship.petToDoRelationship;
import static animores.serverapi.to_do.entity.QToDo.toDo;
import static animores.serverapi.to_do.entity.QToDoInstance.toDoInstance;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import animores.serverapi.account.entity.Account;
import animores.serverapi.diary.repository.TodoCustomRepository;
import animores.serverapi.to_do.dao.GetTodosDao;
import animores.serverapi.to_do.dao.QGetTodosDao;
import animores.serverapi.to_do.dao.QGetTodosPetDao;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TodoCustomRepositoryImpl implements TodoCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GetTodosDao> getTodos(Account account, Boolean completed, LocalDate start,
        LocalDate end, Integer page, Integer size) {

        List<GetTodosDao> todos = new ArrayList<>(
            jpaQueryFactory
                .from(toDo)
                .leftJoin(toDoInstance).on(toDoInstance.toDo.id.eq(toDo.id))
                .leftJoin(petToDoRelationship).on(petToDoRelationship.toDo.id.eq(toDo.id))
                .leftJoin(pet).on(petToDoRelationship.pet.id.eq(pet.id))
                .where(
                    toDo.account.id.eq(account.getId()),
                    completedCondition(completed),
                    dateCondition(start, end)
                )
                .orderBy(toDo.date.desc())
                .offset((page != null && size != null) ? (long) (page * size) : 0)
                .limit(size != null ? size : Long.MAX_VALUE)
                .transform(
                    groupBy(toDo.id).as(
                        new QGetTodosDao(
                            toDo.id,
                            toDo.date,
                            toDo.time,
                            toDo.isAllDay,
                            toDo.content,
                            toDo.tag,
                            toDo.color,
                            toDo.isUsingAlarm,
                            toDo.unit,
                            toDo.intervalNum,
                            list(
                                new QGetTodosPetDao(
                                    new CaseBuilder()
                                        .when(pet.id.isNotNull()).then(pet.id)
                                        .otherwise((Long) null),
                                    new CaseBuilder()
                                        .when(pet.id.isNotNull()).then(pet.name)
                                        .otherwise((String) null)
                                )
                            )
                        )
                    )
                )
                .values()
        );

        List<GetTodosDao> cleaned = todos.stream()
            .map(todo -> new GetTodosDao(
                todo.getId(),
                todo.getDate(),
                todo.getTime(),
                todo.getIsAllDay(),
                todo.getContent(),
                todo.getTag(),
                todo.getColor(),
                todo.getIsUsingAlarm(),
                todo.getUnit(),
                todo.getIntervalNum(),
                todo.getPets() == null
                    ? new ArrayList<>()
                    : todo.getPets().stream()
                        .filter(p -> p.getId() != null && p.getName() != null)
                        .toList()
            ))
            .toList();

        return cleaned;
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
