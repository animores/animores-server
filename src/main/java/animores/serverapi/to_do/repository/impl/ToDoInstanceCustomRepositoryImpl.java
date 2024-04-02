package animores.serverapi.to_do.repository.impl;

import animores.serverapi.profile.domain.vo.ProfileVo;
import animores.serverapi.to_do.entity.vo.ToDoInstanceVo;
import animores.serverapi.to_do.entity.vo.ToDoVo;
import animores.serverapi.to_do.repository.ToDoInstanceCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static animores.serverapi.profile.domain.QProfile.profile;
import static animores.serverapi.to_do.entity.QToDo.toDo;
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
										toDo.id,
										toDo.isAllDay,
										toDo.content,
										toDo.tag,
										toDo.color,
										toDo.isUsingAlarm
								),
								toDoInstance.date.min().as("date"),
								toDoInstance.time.min().as("time"),
								Projections.constructor(
										ProfileVo.class,
										profile.name,
										profile.imageUrl),
								toDoInstance.completeTime
						)
				)
				.from(toDoInstance)
				.join(toDoInstance.toDo, toDo)
				.leftJoin(toDoInstance.completeProfile, profile)
				.groupBy(toDo)
				.where(toDo.id.in(toDoIds)
						.and(toDoInstance.date.eq(LocalDate.now()))
						.and(profile.isNull()))
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
										toDo.id,
										toDo.isAllDay,
										toDo.content,
										toDo.tag,
										toDo.color,
										toDo.isUsingAlarm
								),
								toDoInstance.date.min().as("date"),
								toDoInstance.time.min().as("time"),
								Projections.constructor(
										ProfileVo.class,
										profile.name,
										profile.imageUrl),
								toDoInstance.completeTime
						)
				)
				.from(toDoInstance)
				.join(toDoInstance.toDo, toDo)
				.leftJoin(toDoInstance.completeProfile, profile)
				.groupBy(toDo)
				.where(toDo.id.in(toDoIds)
						.and(toDoInstance.date.eq(LocalDate.now()))
						.and(profile.isNotNull())
				)
				.fetch();
	}

	@Override
	public List<ToDoInstanceVo> findAllByTodayToDoIdIn(List<Long> toDoIds) {
		return jpaQueryFactory.select(
						Projections.constructor(
								ToDoInstanceVo.class,
								toDoInstance.id,
								Projections.constructor(
										ToDoVo.class,
										toDo.id,
										toDo.isAllDay,
										toDo.content,
										toDo.tag,
										toDo.color,
										toDo.isUsingAlarm
								),
								toDoInstance.date.min().as("date"),
								toDoInstance.time.min().as("time"),
								Projections.constructor(
										ProfileVo.class,
										profile.name,
										profile.imageUrl),
								toDoInstance.completeTime
						)
				)
				.from(toDoInstance)
				.join(toDoInstance.toDo, toDo)
				.leftJoin(toDoInstance.completeProfile, profile)
				.groupBy(toDo)
				.where(toDo.id.in(toDoIds)
						.and(toDoInstance.date.eq(LocalDate.now())))
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
										toDo.id,
										toDo.isAllDay,
										toDo.content,
										toDo.tag,
										toDo.color,
										toDo.isUsingAlarm
								),
								toDoInstance.date.min().as("date"),
								toDoInstance.time.min().as("time"),
								Projections.constructor(
										ProfileVo.class,
										profile.name,
										profile.imageUrl),
								toDoInstance.completeTime
						)
				)
				.from(toDoInstance)
				.join(toDoInstance.toDo, toDo)
				.leftJoin(toDoInstance.completeProfile, profile)
				.groupBy(toDo)
				.where(toDo.id.in(toDoIds)
						.and(profile.isNull()))
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
										toDo.id,
										toDo.isAllDay,
										toDo.content,
										toDo.tag,
										toDo.color,
										toDo.isUsingAlarm
								),
								toDoInstance.date.min().as("date"),
								toDoInstance.time.min().as("time"),
								Projections.constructor(
										ProfileVo.class,
										profile.name,
										profile.imageUrl),
								toDoInstance.completeTime
						)
				)
				.from(toDoInstance)
				.join(toDoInstance.toDo, toDo)
				.leftJoin(toDoInstance.completeProfile, profile)
				.groupBy(toDo)
				.where(toDo.id.in(toDoIds)
						.and(profile.isNotNull()))
				.fetch();
	}

	@Override
	public List<ToDoInstanceVo> findAllByToDoIdIn(List<Long> toDoIds) {
		return jpaQueryFactory.select(
						Projections.constructor(
								ToDoInstanceVo.class,
								toDoInstance.id,
								Projections.constructor(
										ToDoVo.class,
										toDo.id,
										toDo.isAllDay,
										toDo.content,
										toDo.tag,
										toDo.color,
										toDo.isUsingAlarm
								),
								toDoInstance.date.min().as("date"),
								toDoInstance.time.min().as("time"),
								Projections.constructor(
										ProfileVo.class,
										profile.name,
										profile.imageUrl),
								toDoInstance.completeTime
						)
				)
				.from(toDoInstance)
				.join(toDoInstance.toDo, toDo)
				.leftJoin(toDoInstance.completeProfile, profile)
				.groupBy(toDo)
				.where(toDo.id.in(toDoIds))
				.fetch();
	}
}
