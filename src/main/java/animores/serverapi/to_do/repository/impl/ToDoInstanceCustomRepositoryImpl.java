package animores.serverapi.to_do.repository.impl;

import animores.serverapi.to_do.entity.ToDoInstance;
import animores.serverapi.to_do.repository.ToDoInstanceCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static animores.serverapi.to_do.entity.QToDoInstance.toDoInstance;

@Repository
@RequiredArgsConstructor
public class ToDoInstanceCustomRepositoryImpl implements ToDoInstanceCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;


	@Override
	public List<ToDoInstance> findAllByCompleteFalseAndTodayToDoIdIn(List<Long> toDoIds) {
		return jpaQueryFactory.select(
						Projections.fields(
								ToDoInstance.class,
								toDoInstance.id,
								toDoInstance.toDo,
								toDoInstance.time.min().as("time")
						)
				)
				.from(toDoInstance)
				.groupBy(toDoInstance.toDo)
				.having(toDoInstance.toDo.id.in(toDoIds))
				.where(
						toDoInstance.completeProfile.isNull().and(
								toDoInstance.time.between(
										LocalDateTime.of(
												LocalDateTime.now().getYear(),
												LocalDateTime.now().getMonth(),
												LocalDateTime.now().getDayOfMonth(),
												0,
												0,
												0
										),
										LocalDateTime.of(
												LocalDateTime.now().getYear(),
												LocalDateTime.now().getMonth(),
												LocalDateTime.now().getDayOfMonth() + 1,
												0,
												0,
												0
										)
								))
				)
				.fetch();
	}

	@Override
	public List<ToDoInstance> findAllByCompleteAndTodayToDoIdIn(List<Long> toDoIds) {
		return jpaQueryFactory.select(
						Projections.fields(
								ToDoInstance.class,
								toDoInstance.id,
								toDoInstance.toDo,
								toDoInstance.time.max().as("time"),
								toDoInstance.completeProfile,
								toDoInstance.completeTime
						)
				)
				.from(toDoInstance)
				.groupBy(toDoInstance.toDo)
				.having(toDoInstance.toDo.id.in(toDoIds))
				.where(
						toDoInstance.completeProfile.isNotNull().and(
								toDoInstance.time.between(
										LocalDateTime.of(
												LocalDateTime.now().getYear(),
												LocalDateTime.now().getMonth(),
												LocalDateTime.now().getDayOfMonth(),
												0,
												0,
												0
										),
										LocalDateTime.of(
												LocalDateTime.now().getYear(),
												LocalDateTime.now().getMonth(),
												LocalDateTime.now().getDayOfMonth() + 1,
												0,
												0,
												0
										)
								))
				)
				.fetch();
	}

	@Override
	public List<ToDoInstance> findAllByCompleteFalseAndToDoIdIn(List<Long> toDoIds) {
		return jpaQueryFactory.select(
						Projections.fields(
								ToDoInstance.class,
								toDoInstance.id,
								toDoInstance.toDo,
								toDoInstance.time.min().as("time")
						)
				)
				.from(toDoInstance)
				.groupBy(toDoInstance.toDo)
				.having(toDoInstance.toDo.id.in(toDoIds))
				.where(
						toDoInstance.completeProfile.isNull()
				)
				.fetch();
	}

	@Override
	public List<ToDoInstance> findAllByCompleteAndToDoIdIn(List<Long> toDoIds) {
		return jpaQueryFactory.select(
						Projections.fields(
								ToDoInstance.class,
								toDoInstance.id,
								toDoInstance.toDo,
								toDoInstance.time.max().as("time"),
								toDoInstance.completeProfile,
								toDoInstance.completeTime
						)
				)
				.from(toDoInstance)
				.groupBy(toDoInstance.toDo)
				.having(toDoInstance.toDo.id.in(toDoIds))
				.where(
						toDoInstance.completeProfile.isNotNull()
				)
				.fetch();
	}
}
