package animores.serverapi.to_do.repository.impl;

import animores.serverapi.profile.dao.ProfileVo;
import animores.serverapi.to_do.dao.GetToDoPageDao;
import animores.serverapi.to_do.dao.ToDoDao;
import animores.serverapi.to_do.dao.ToDoInstanceDao;
import animores.serverapi.to_do.repository.ToDoInstanceCustomRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static animores.serverapi.profile.domain.QProfile.profile;
import static animores.serverapi.to_do.entity.QPetToDoRelationship.petToDoRelationship;
import static animores.serverapi.to_do.entity.QToDo.toDo;
import static animores.serverapi.to_do.entity.QToDoInstance.toDoInstance;

@Repository
@RequiredArgsConstructor
public class ToDoInstanceCustomRepositoryImpl implements ToDoInstanceCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public GetToDoPageDao findAllByToDoInstanceTodayAndDoneAndPetListInPets(Boolean done, List<Long> petIdList, Integer page, Integer size) {
		int offset = (page - 1) * size;

		Predicate donePredicate;
		if (done == null) {
			donePredicate = Expressions.asBoolean(true).isTrue();
		} else {
			donePredicate = done ? toDoInstance.completeProfile.isNotNull() : toDoInstance.completeProfile.isNull();
		}

		int totalCount = jpaQueryFactory.select(toDo.id)
				.from(toDo)
				.join(petToDoRelationship).on(toDo.id.eq(petToDoRelationship.toDo.id))
				.join(toDoInstance).on(toDo.id.eq(toDoInstance.toDo.id))
				.where(
						toDoInstance.date.eq(LocalDate.now())
								.and(petToDoRelationship.pet.id.in(petIdList))
								.and(donePredicate)
				).groupBy(toDo.id)
				.fetch().size();


		return new GetToDoPageDao(
				page,
				size,
				totalCount,
				totalCount % size == 0 ? totalCount/size : totalCount/size + 1,
				jpaQueryFactory.select(
								Projections.constructor(
										ToDoInstanceDao.class,
										toDoInstance.id,
										Projections.constructor(
												ToDoDao.class,
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
						.from(toDo)
						.join(toDo.petToDoRelationships, petToDoRelationship)
						.join(toDo.toDoInstances, toDoInstance)
						.leftJoin(toDoInstance.completeProfile, profile)
						.where(
								toDoInstance.date.eq(LocalDate.now())
										.and(petToDoRelationship.pet.id.in(petIdList))
										.and(donePredicate)
						)
						.groupBy(toDo.id)
						.offset(offset)
						.limit(size)
						.fetch());
	}

	@Override
	public GetToDoPageDao findAllByToDoInstanceDoneAndPetListInPets(Boolean done, List<Long> petIdList, Integer page, Integer size) {
		int offset = (page - 1) * size;

		Predicate donePredicate;
		if (done == null) {
			donePredicate = Expressions.asBoolean(true).isTrue();
		} else {
			donePredicate = done ? toDoInstance.completeProfile.isNotNull() : toDoInstance.completeProfile.isNull();
		}

		int totalCount = jpaQueryFactory.select(toDo.id)
				.from(toDo)
				.join(petToDoRelationship).on(toDo.id.eq(petToDoRelationship.toDo.id))
				.join(toDoInstance).on(toDo.id.eq(toDoInstance.toDo.id))
				.where(
						toDoInstance.date.eq(LocalDate.now())
								.and(petToDoRelationship.pet.id.in(petIdList))
								.and(donePredicate)
				).groupBy(toDo.id)
				.fetch().size();


		return new GetToDoPageDao(
				page,
				size,
				totalCount,
				totalCount % size == 0 ? totalCount/size : totalCount/size + 1,
				jpaQueryFactory.select(
								Projections.constructor(
										ToDoInstanceDao.class,
										toDoInstance.id,
										Projections.constructor(
												ToDoDao.class,
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
						.from(toDo)
						.join(toDo.petToDoRelationships, petToDoRelationship)
						.join(toDo.toDoInstances, toDoInstance)
						.leftJoin(toDoInstance.completeProfile, profile)
						.where(
								petToDoRelationship.pet.id.in(petIdList)
										.and(donePredicate)
						)
						.groupBy(toDo.id)
						.offset(offset)
						.limit(size)
						.fetch());
	}
}
