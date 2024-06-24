package animores.serverapi.to_do.repository;

import animores.serverapi.to_do.dao.GetToDoPageDao;

import java.util.List;

public interface ToDoInstanceCustomRepository {

	GetToDoPageDao findAllByToDoInstanceTodayAndDoneAndPetListInPets(Boolean done, List<Long> petIdList, Integer page, Integer size);
	GetToDoPageDao findAllByToDoInstanceDoneAndPetListInPets(Boolean done, List<Long> petIdList, Integer page, Integer size);
}
