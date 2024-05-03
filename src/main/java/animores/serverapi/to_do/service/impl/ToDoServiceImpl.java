package animores.serverapi.to_do.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.pet.domain.Pet;
import animores.serverapi.pet.repository.PetRepository;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.profile.repository.ProfileRepository;
import animores.serverapi.to_do.dao.GetToDoPageDao;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import animores.serverapi.to_do.dto.response.PetResponse;
import animores.serverapi.to_do.dto.response.ToDoDetailResponse;
import animores.serverapi.to_do.dto.response.ToDoPageResponse;
import animores.serverapi.to_do.entity.PetToDoRelationship;
import animores.serverapi.to_do.entity.ToDo;
import animores.serverapi.to_do.entity.ToDoInstance;
import animores.serverapi.to_do.repository.PetToDoRelationshipRepository;
import animores.serverapi.to_do.repository.ToDoInstanceRepository;
import animores.serverapi.to_do.repository.ToDoRepository;
import animores.serverapi.to_do.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ToDoServiceImpl implements ToDoService {
    private final ProfileRepository profileRepository;
    private final ToDoRepository toDoRepository;
    private final PetRepository petRepository;
    private final PetToDoRelationshipRepository petToDoRelationshipRepository;
    private final ToDoInstanceRepository toDoInstanceRepository;

    @Override
    @Transactional
    public void createToDo(Account account, ToDoCreateRequest request) {
        Profile createProfile = profileRepository.getReferenceById(request.profileId());
        ToDo toDo = ToDo.fromRequest(request, account, createProfile);
        toDo = toDoRepository.save(toDo);
        List<PetToDoRelationship> petToDoRelationships = new ArrayList<>();
        for (Long petId : request.petIds()) {
            Pet pet = petRepository.getReferenceById(petId);
            petToDoRelationships.add(new PetToDoRelationship(pet, toDo));
        }

        petToDoRelationshipRepository.saveAll(petToDoRelationships);
        toDoInstanceRepository.save(ToDoInstance.fromToDo(toDo));
    }


    @Override
    @Transactional(readOnly = true)
    public ToDoPageResponse getTodayToDo(Boolean done, List<Pet> pets, Integer page, Integer size) {
        List<Long> petIds = pets.stream().map(Pet::getId).toList();
        List<PetToDoRelationship> relationships = petToDoRelationshipRepository.findAllByPet_IdIn(petIds);
        //to do id를 key로 하고, 그 to do 에 해당하는 petResonseList를 value로 하는 map
        Map<Long, List<PetResponse>> toDoIdPetResponseMap = relationships.stream()
                .collect(Collectors.groupingBy(petToDoRelationship -> petToDoRelationship.getToDo().getId(),
                        Collectors.mapping(petToDoRelationship -> new PetResponse(petToDoRelationship.getPet().getId(),
                                petToDoRelationship.getPet().getName()), Collectors.toList())));

        GetToDoPageDao pageDao;

        if (done == null) {
            pageDao = toDoInstanceRepository.findAllByTodayToDoIdIn(toDoIdPetResponseMap.keySet().stream().toList(), page, size);
        } else if (done.equals(Boolean.TRUE)) {
            pageDao = toDoInstanceRepository.findAllByCompleteAndTodayToDoIdIn(toDoIdPetResponseMap.keySet().stream().toList(), page, size);
        } else {
            pageDao = toDoInstanceRepository.findAllByCompleteFalseAndTodayToDoIdIn(toDoIdPetResponseMap.keySet().stream().toList(), page, size);
        }

        return ToDoPageResponse.fromGetToDoPageDaoAndToDoPetMap(pageDao, toDoIdPetResponseMap);
    }

    @Override
    @Transactional(readOnly = true)
    public ToDoPageResponse getAllToDo(Boolean done, List<Pet> pets, Integer page, Integer size) {
        List<Long> petIds = pets.stream().map(Pet::getId).toList();
        List<PetToDoRelationship> relationships = petToDoRelationshipRepository.findAllByPet_IdIn(petIds);
        //to do id를 key로 하고, 그 to do 에 해당하는 petResonseList를 value로 하는 map
        Map<Long, List<PetResponse>> toDoIdPetResponseMap = relationships.stream()
                .collect(Collectors.groupingBy(petToDoRelationship -> petToDoRelationship.getToDo().getId(),
                        Collectors.mapping(petToDoRelationship -> new PetResponse(petToDoRelationship.getPet().getId(),
                                petToDoRelationship.getPet().getName()), Collectors.toList())));

        GetToDoPageDao pageDao;
        if (done == null) {
            pageDao = toDoInstanceRepository.findAllByToDoIdIn(toDoIdPetResponseMap.keySet().stream().toList(), page, size);
        } else if (done.equals(Boolean.TRUE)) {
            pageDao = toDoInstanceRepository.findAllByCompleteAndToDoIdIn(toDoIdPetResponseMap.keySet().stream().toList(), page, size);
        } else {
            pageDao = toDoInstanceRepository.findAllByCompleteFalseAndToDoIdIn(toDoIdPetResponseMap.keySet().stream().toList(), page, size);
        }

        return ToDoPageResponse.fromGetToDoPageDaoAndToDoPetMap(pageDao, toDoIdPetResponseMap);

    }

    @Override
    @Transactional(readOnly = true)
    public ToDoDetailResponse getToDoById(Long id, Long accountId) {
        ToDo toDo = toDoRepository.findByIdAndAccount_Id(id, accountId).orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_TO_DO));
        return ToDoDetailResponse.fromToDo(toDo);
    }

    @Override
    @Transactional
    public ToDoDetailResponse updateToDoById(Long id, ToDoPatchRequest request, Long accountId) {
        ToDo toDo = toDoRepository.findByIdAndAccount_Id(id, accountId).orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_TO_DO));

        if (!toDo.getCreateProfile().getId().equals(request.profileId())) {
            throw new CustomException(ExceptionCode.INAPPROPRIATE_PROFILE_ACCESS);
        }

        if (!request.petIds().isEmpty()) {
            Set<Long> petIds = toDo.getPetToDoRelationships().stream()
                    .map(PetToDoRelationship::getPet)
                    .map(Pet::getId)
                    .collect(Collectors.toSet());

            if (petIds.size() != request.petIds().size() || !petIds.containsAll(request.petIds())) {
                petToDoRelationshipRepository.deleteAllByToDo_Id(toDo.getId());

                List<PetToDoRelationship> petToDoRelationships = new ArrayList<>();
                for (Long petId : request.petIds()) {
                    Pet pet = petRepository.getReferenceById(petId);
                    petToDoRelationships.add(new PetToDoRelationship(pet, toDo));
                }
                toDo.setPetToDoRelationships(petToDoRelationships);
                petToDoRelationshipRepository.saveAll(petToDoRelationships);
            }
        }

        toDo.update(request);
        return ToDoDetailResponse.fromToDo(toDo);
    }

    @Override
    @Transactional
    public void deleteToDoById(Long id, Long profileId) {
        ToDo todo = toDoRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_TO_DO));

        if (!todo.getCreateProfile().getId().equals(profileId)) {
            throw new CustomException(ExceptionCode.INAPPROPRIATE_PROFILE_ACCESS);
        }
        toDoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void checkToDo(Long toDoId, Long accountId) {
        ToDoInstance toDoInstance = toDoInstanceRepository.findByToDo_IdAndCompleteProfileIsNull(toDoId)
                .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_TO_DO));

        Profile completeProfile = profileRepository.getReferenceById(2L);
        toDoInstance.setComplete(completeProfile);

        ToDoInstance nextToDoInstance = toDoInstance.getToDo().getNextToDoInstance();
        if (nextToDoInstance == null) {
            return;
        }
        toDoInstanceRepository.save(nextToDoInstance);
    }
}
