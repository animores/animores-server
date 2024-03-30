package animores.serverapi.to_do.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.pet.domain.Pet;
import animores.serverapi.pet.repository.PetRepository;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.profile.repository.ProfileRepository;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import animores.serverapi.to_do.dto.response.ToDoDetailResponse;
import animores.serverapi.to_do.dto.response.ToDoResponse;
import animores.serverapi.to_do.entity.PetToDoRelationship;
import animores.serverapi.to_do.entity.ToDo;
import animores.serverapi.to_do.entity.ToDoInstance;
import animores.serverapi.to_do.entity.vo.ToDoInstanceVo;
import animores.serverapi.to_do.repository.PetToDoRelationshipRepository;
import animores.serverapi.to_do.repository.ToDoInstanceRepository;
import animores.serverapi.to_do.repository.ToDoRepository;
import animores.serverapi.to_do.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ToDoServiceImpl implements ToDoService {
    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final ToDoRepository toDoRepository;
    private final PetRepository petRepository;
    private final PetToDoRelationshipRepository petToDoRelationshipRepository;
    private final ToDoInstanceRepository toDoInstanceRepository;

    @Override
    @Transactional
    public void createToDo(ToDoCreateRequest request) {
        //TODO: accountRepository.getReferenceById(1L) should be replaced with the actual account id
        Account account = accountRepository.getReferenceById(1L);
        //TODO: createProfile should be replaced with the actual profile
        Profile createProfile = profileRepository.getReferenceById(2L);
        //TODO: List.of(1L, 2L) should be replaced with the actual pet ids
        Set<Long> accountPetIds = Set.of(1L, 2L);

        if(!accountPetIds.containsAll(request.petIds())) {
            throw new CustomException(ExceptionCode.ILLEGAL_PET_IDS);
        }

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
    public List<ToDoResponse> getTodayToDo(Boolean done, List<Long> pets) {

        Set<Long> petIds = Set.of(1L, 2L);
        if (pets == null || pets.isEmpty()) {
            pets = new ArrayList<>(petIds);
        }

        if(!petIds.containsAll(pets)) {
            throw new CustomException(ExceptionCode.ILLEGAL_PET_IDS);
        }

        List<Pet> petList = petRepository.findAllById(pets);
        var petNameMap = petList.stream().collect(Collectors.toMap(Pet::getId, Pet::getName));
        List<PetToDoRelationship> relationships = petToDoRelationshipRepository.findAllByPet_IdIn(pets);

        if (done != null && !done) {

            List<ToDoInstanceVo> toDoInstances = toDoInstanceRepository.findAllByCompleteFalseAndTodayToDoIdIn(
                    relationships.stream()
                            .map(PetToDoRelationship::getToDo)
                            .map(ToDo::getId)
                            .collect(Collectors.toSet())
                            .stream().toList()
            );

            return toDoInstances.stream()
                    .map(toDoInstanceVo -> ToDoResponse.fromToDoInstanceVo(toDoInstanceVo, petNameMap))
                    .toList();

        } else {
            List<ToDoInstanceVo> toDOInstances = toDoInstanceRepository.findAllByCompleteAndTodayToDoIdIn(
                    relationships.stream()
                            .map(PetToDoRelationship::getToDo)
                            .map(ToDo::getId)
                            .collect(Collectors.toSet())
                            .stream().toList()
            );

            return toDOInstances.stream()
                    .map(toDoInstanceVo -> ToDoResponse.fromToDoInstanceVo(toDoInstanceVo, petNameMap))
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ToDoResponse> getAllToDo(Boolean done, List<Long> pets) {
        Set<Long> petIds = Set.of(1L, 2L);
        if (pets == null || pets.isEmpty()) {
            pets = petIds.stream().sorted().toList();
        }

        if(!petIds.containsAll(pets)) {
            throw new CustomException(ExceptionCode.ILLEGAL_PET_IDS);
        }

        List<Pet> petList = petRepository.findAllById(pets);
        var petNameMap = petList.stream().collect(Collectors.toMap(Pet::getId, Pet::getName));
        List<PetToDoRelationship> relationships = petToDoRelationshipRepository.findAllByPet_IdIn(pets);

        if (done != null && !done) {

            List<ToDoInstanceVo> toDOInstances = toDoInstanceRepository.findAllByCompleteFalseAndToDoIdIn(
                    relationships.stream()
                            .map(PetToDoRelationship::getToDo)
                            .map(ToDo::getId)
                            .collect(Collectors.toSet())
                            .stream().toList()
            );

            return toDOInstances.stream()
                    .map(toDoInstanceVo -> ToDoResponse.fromToDoInstanceVo(toDoInstanceVo, petNameMap))
                    .toList();

        } else {
            List<ToDoInstanceVo> toDoInstances = toDoInstanceRepository.findAllByCompleteAndToDoIdIn(
                    relationships.stream()
                            .map(PetToDoRelationship::getToDo)
                            .map(ToDo::getId)
                            .collect(Collectors.toSet())
                            .stream().toList()
            );

            return toDoInstances.stream()
                    .map(toDoInstanceVo -> ToDoResponse.fromToDoInstanceVo(toDoInstanceVo, petNameMap))
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ToDoDetailResponse getToDoById(Long id) {
        ToDo toDo = toDoRepository.findByIdAndAccount_Id(id,1L).orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_TO_DO));
        return ToDoDetailResponse.fromToDo(toDo);
    }

    @Override
    @Transactional
    public ToDoDetailResponse updateToDoById(Long id, ToDoPatchRequest request) {
        //TODO: 1L should be replaced with the actual account id
        ToDo toDo = toDoRepository.findByIdAndAccount_Id(id,1L).orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_TO_DO));

        //TODO: 실제 현재 로그인한 사용자의 프로필 아이디로 변경
        if(toDo.getCreateProfile().getId() != 2L) {
            throw new CustomException(ExceptionCode.INAPPROPRIATE_PROFILE_ACCESS);
        }

        //TODO: List.of(1L, 2L) should be replaced with the actual pet ids
        Set<Long> accountPetIds = Set.of(1L, 2L);
        if (!accountPetIds.containsAll(request.petIds())) {
            throw new CustomException(ExceptionCode.ILLEGAL_PET_IDS);
        }

        if(!request.petIds().isEmpty()) {
            Set<Long> petIds = toDo.getPetToDoRelationships().stream().map(PetToDoRelationship::getPet).map(Pet::getId).collect(Collectors.toSet());
            if(petIds.size() != request.petIds().size() || !petIds.containsAll(request.petIds())) {
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
    public void deleteToDoById(Long id) {
        toDoRepository.deleteById(id);
    }
}
