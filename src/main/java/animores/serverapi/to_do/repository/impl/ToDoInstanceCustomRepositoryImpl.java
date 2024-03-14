package animores.serverapi.to_do.repository.impl;

import animores.serverapi.to_do.entity.vo.ToDoInstanceVo;
import animores.serverapi.to_do.entity.vo.ToDoVo;
import animores.serverapi.to_do.repository.ToDoInstanceCustomRepository;
import animores.serverapi.profile.domain.vo.ProfileVo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static animores.serverapi.to_do.entity.QToDoInstance.toDoInstance;

@Repository
@RequiredArgsConstructor
public class ToDoInstanceCustomRepositoryImpl implements ToDoInstanceCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;


	@Override
	public List<ToDoInstanceVo> findAllByCompleteFalseAndTodayToDoIdIn(List<Long> toDoIds) {
		return jpaQueryFactory.select(
						Projections.constructor(
								ToDoInstanceVo.class,
								toDoInstance.id,
								Projections.constructor(
										ToDoVo.class,
										toDoInstance.toDo.id,
										toDoInstance.toDo.isAllDay,
										toDoInstance.toDo.content,
										toDoInstance.toDo.tag,
										toDoInstance.toDo.color,
										toDoInstance.toDo.isUsingAlarm,
										toDoInstance.toDo.petToDoRelationships
								),
								toDoInstance.date.min().as("date"),
								toDoInstance.time.min().as("time"),
								Projections.constructor(
										ProfileVo.class,
										toDoInstance.completeProfile.name,
										toDoInstance.completeProfile.imageUrl
								),
								toDoInstance.completeTime
						)
				)
				.from(toDoInstance)
				.groupBy(toDoInstance.toDo)
				.where(
						toDoInstance.completeProfile.isNull()
						.and(toDoInstance.date.eq(LocalDate.now()))
						.and(toDoInstance.toDo.id.in(toDoIds)))
				.fetch();
	}

	@Override
	public List<ToDoInstanceVo> findAllByCompleteAndTodayToDoIdIn(List<Long> toDoIds) {
		return jpaQueryFactory.select(
						Projections.constructor(
								ToDoInstanceVo.class,
								toDoInstance.id,
								Projections.constructor(
										ToDoVo.class,
										toDoInstance.toDo.id,
										toDoInstance.toDo.isAllDay,
										toDoInstance.toDo.content,
										toDoInstance.toDo.tag,
										toDoInstance.toDo.color,
										toDoInstance.toDo.isUsingAlarm,
										toDoInstance.toDo.petToDoRelationships
								),
								toDoInstance.date.min().as("date"),
								toDoInstance.time.min().as("time"),
								Projections.constructor(
										ProfileVo.class,
										toDoInstance.completeProfile.name,
										toDoInstance.completeProfile.imageUrl
								),
								toDoInstance.completeTime
						)
				)
				.from(toDoInstance)
				.groupBy(toDoInstance.toDo.id)
				.where(
						toDoInstance.completeProfile.isNotNull()
						.and(toDoInstance.date.eq(LocalDate.now()))
						.and(toDoInstance.toDo.id.in(toDoIds))
				)
				.fetch();
	}

	@Override
	public List<ToDoInstanceVo> findAllByCompleteFalseAndToDoIdIn(List<Long> toDoIds) {
		return jpaQueryFactory.select(
						Projections.constructor(
								ToDoInstanceVo.class,
								toDoInstance.id,
								Projections.constructor(
										ToDoVo.class,
										toDoInstance.toDo.id,
										toDoInstance.toDo.isAllDay,
										toDoInstance.toDo.content,
										toDoInstance.toDo.tag,
										toDoInstance.toDo.color,
										toDoInstance.toDo.isUsingAlarm,
										toDoInstance.toDo.petToDoRelationships
								),
								toDoInstance.date.min().as("date"),
								toDoInstance.time.min().as("time"),
								Projections.constructor(
										ProfileVo.class,
										toDoInstance.completeProfile.name,
										toDoInstance.completeProfile.imageUrl
								),
								toDoInstance.completeTime
						)
				)
				.from(toDoInstance)
				.groupBy(toDoInstance.toDo)
				.where(
					toDoInstance.completeProfile.isNull()
					.and(toDoInstance.toDo.id.in(toDoIds))
				)
				.fetch();
	}

	@Override
	public List<ToDoInstanceVo> findAllByCompleteAndToDoIdIn(List<Long> toDoIds) {
		return jpaQueryFactory.select(
						Projections.constructor(
								ToDoInstanceVo.class,
								toDoInstance.id,
								Projections.constructor(
										ToDoVo.class,
										toDoInstance.toDo.id,
										toDoInstance.toDo.isAllDay,
										toDoInstance.toDo.content,
										toDoInstance.toDo.tag,
										toDoInstance.toDo.color,
										toDoInstance.toDo.isUsingAlarm,
										toDoInstance.toDo.petToDoRelationships
								),
								toDoInstance.date.min().as("date"),
								toDoInstance.time.min().as("time"),
								Projections.constructor(
										ProfileVo.class,
										toDoInstance.completeProfile.name,
										toDoInstance.completeProfile.imageUrl
								),
								toDoInstance.completeTime
						)
				)
				.from(toDoInstance)
				.groupBy(toDoInstance.toDo)
				.where(
					toDoInstance.completeProfile.isNotNull()
					.and(toDoInstance.toDo.id.in(toDoIds))
				)
				.fetch();
	}
}
