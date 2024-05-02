package animores.serverapi.to_do.service.impl;

import animores.serverapi.account.aop.UserInfo;
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
import animores.serverapi.to_do.dto.response.PetResponse;
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
import animores.serverapi.util.RequestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

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
    @UserInfo
    public void createToDo(ToDoCreateRequest request) {
        Account account = getAccountFromContext();
        Profile createProfile = profileRepository.getReferenceById(2L);

        Set<Long> accountPetIds = petRepository.findAllByAccount_id(account.getId())
                                                .stream()
                                                .map(Pet::getId)
                                                .collect(Collectors.toSet());

        if (!accountPetIds.containsAll(request.petIds())) {
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
    @UserInfo
    public List<ToDoResponse> getTodayToDo(Boolean done, List<Long> pets) {

        Set<Long> petIds = Set.of(1L, 2L);
        if (pets == null || pets.isEmpty()) {
            pets = new ArrayList<>(petIds);
        }

        if (!petIds.containsAll(pets)) {
            throw new CustomException(ExceptionCode.ILLEGAL_PET_IDS);
        }

        petRepository.findAllById(pets); //pets 초기화 용
        List<PetToDoRelationship> relationships = petToDoRelationshipRepository.findAllByPet_IdIn(pets);
        //to do id를 key로 하고, 그 to do 에 해당하는 petResonseList를 value로 하는 map
        Map<Long, List<PetResponse>> toDoIdPetResponseMap = relationships.stream()
                .collect(Collectors.groupingBy(petToDoRelationship -> petToDoRelationship.getToDo().getId(),
                        Collectors.mapping(petToDoRelationship -> new PetResponse(petToDoRelationship.getPet().getId(),
                                petToDoRelationship.getPet().getName()), Collectors.toList())));

        List<ToDoInstanceVo> toDoInstances;

        if (done == null) {
            toDoInstances = toDoInstanceRepository.findAllByTodayToDoIdIn(toDoIdPetResponseMap.keySet().stream().toList());
        } else if (done.equals(Boolean.TRUE)) {
            toDoInstances = toDoInstanceRepository.findAllByCompleteAndTodayToDoIdIn(toDoIdPetResponseMap.keySet().stream().toList());
        } else {
            toDoInstances = toDoInstanceRepository.findAllByCompleteFalseAndTodayToDoIdIn(toDoIdPetResponseMap.keySet().stream().toList());
        }

        return toDoInstances.stream()
                .map(toDoInstanceVo -> ToDoResponse.fromToDoInstanceVo(toDoInstanceVo, toDoIdPetResponseMap.get(toDoInstanceVo.toDo().id())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @UserInfo
    public List<ToDoResponse> getAllToDo(Boolean done, List<Long> pets) {
        Set<Long> petIds = Set.of(1L, 2L);
        if (pets == null || pets.isEmpty()) {
            pets = petIds.stream().sorted().toList();
        }

        if (!petIds.containsAll(pets)) {
            throw new CustomException(ExceptionCode.ILLEGAL_PET_IDS);
        }

        petRepository.findAllById(pets); //pets 초기화 용
        List<PetToDoRelationship> relationships = petToDoRelationshipRepository.findAllByPet_IdIn(pets);
        //to do id를 key로 하고, 그 to do 에 해당하는 petResonseList를 value로 하는 map
        Map<Long, List<PetResponse>> toDoIdPetResponseMap = relationships.stream()
                .collect(Collectors.groupingBy(petToDoRelationship -> petToDoRelationship.getToDo().getId(),
                        Collectors.mapping(petToDoRelationship -> new PetResponse(petToDoRelationship.getPet().getId(),
                                petToDoRelationship.getPet().getName()), Collectors.toList())));

        List<ToDoInstanceVo> toDoInstances;
        if (done == null) {
            toDoInstances = toDoInstanceRepository.findAllByToDoIdIn(toDoIdPetResponseMap.keySet().stream().toList());
        } else if (done.equals(Boolean.TRUE)) {
            toDoInstances = toDoInstanceRepository.findAllByCompleteAndToDoIdIn(toDoIdPetResponseMap.keySet().stream().toList());
        } else {
            toDoInstances = toDoInstanceRepository.findAllByCompleteFalseAndToDoIdIn(toDoIdPetResponseMap.keySet().stream().toList());
        }

        return toDoInstances.stream()
                .map(toDoInstanceVo -> ToDoResponse.fromToDoInstanceVo(toDoInstanceVo, toDoIdPetResponseMap.get(toDoInstanceVo.toDo().id())))
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    @UserInfo
    public ToDoDetailResponse getToDoById(Long id) {
        ToDo toDo = toDoRepository.findByIdAndAccount_Id(id, 1L).orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_TO_DO));
        return ToDoDetailResponse.fromToDo(toDo);
    }

    @Override
    @Transactional
    public ToDoDetailResponse updateToDoById(Long id, ToDoPatchRequest request) {
        //TODO: 1L should be replaced with the actual account id
        ToDo toDo = toDoRepository.findByIdAndAccount_Id(id, 1L).orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_TO_DO));

        //TODO: 실제 현재 로그인한 사용자의 프로필 아이디로 변경
        if (toDo.getCreateProfile().getId() != 2L) {
            throw new CustomException(ExceptionCode.INAPPROPRIATE_PROFILE_ACCESS);
        }

        if (!request.petIds().isEmpty()) {
            Set<Long> petIds = toDo.getPetToDoRelationships().stream().map(PetToDoRelationship::getPet).map(Pet::getId).collect(Collectors.toSet());
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
    public void deleteToDoById(Long id) {
        toDoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void checkToDo(Long toDoId) {
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

    private Account getAccountFromContext() {
        try {
            return (Account) RequestContextHolder.getRequestAttributes().getAttribute(
                    RequestConstants.ACCOUNT_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        } catch (NullPointerException e) {
            throw new CustomException(ExceptionCode.INVALID_USER);
        }
    }
}
