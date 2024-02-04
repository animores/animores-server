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
import animores.serverapi.to_do.dto.response.PetResponse;
import animores.serverapi.to_do.dto.response.ToDoDetailResponse;
import animores.serverapi.to_do.dto.response.ToDoResponse;
import animores.serverapi.to_do.entity.PetToDoRelationship;
import animores.serverapi.to_do.entity.ToDo;
import animores.serverapi.to_do.repository.PetToDoRelationshipRepository;
import animores.serverapi.to_do.repository.ToDoRepository;
import animores.serverapi.to_do.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class ToDoServiceImpl implements ToDoService {
    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final ToDoRepository toDoRepository;
    private final PetRepository petRepository;
    private final PetToDoRelationshipRepository petToDoRelationshipRepository;

    @Override
    @Transactional
    public void createToDo(ToDoCreateRequest request) {
        //TODO: accountRepository.getReferenceById(1L) should be replaced with the actual account id
        Account account = accountRepository.getReferenceById(1L);
        //TODO: createProfile should be replaced with the actual profile
        Profile createProfile = profileRepository.getReferenceById(2L);
        //TODO: List.of(1L, 2L) should be replaced with the actual pet ids
        Set<Long> petIds = Set.of(1L, 2L);

        if(!petIds.containsAll(request.petIds())) {
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

        //TODO: ToDo 로부터 ToDoInstance 를 생성하는 로직이 필요합니다.
    }

    @Override
    @Transactional(readOnly = true)
    public List<ToDoResponse> getTodayToDo(Boolean done, List<Long> pets) {

        if (done != null && !done) {
            if (pets == null || pets.isEmpty()) {
                return List.of(
                        new ToDoResponse(1L, "산책", List.of(new PetResponse(1L, "두부")), false, LocalDate.now(), LocalTime.of(11, 0), true, null, "red", null, null),
                        new ToDoResponse(2L, "약", List.of(new PetResponse(2L, "호동이")), false, LocalDate.now(), LocalTime.of(13, 0), true, null, "yellow", null, null),
                        new ToDoResponse(3L, "유치원 가는 날", List.of(new PetResponse(3L, "삼바")), true, LocalDate.now(), null, false, null, "blue", null, null)
                );
            } else {
                return List.of(
                        new ToDoResponse(1L, "산책", List.of(new PetResponse(1L, "두부")), false, LocalDate.now(), LocalTime.of(11, 0), true, null, "red", null, null)
                );
            }
        } else {
            if (pets == null || pets.isEmpty()) {
                return List.of(
                        new ToDoResponse(1L, "산책", List.of(new PetResponse(1L, "두부")), false, LocalDate.now(), LocalTime.of(11, 0), true, null, "red", "아빠 사진1", LocalDateTime.of(2024, 1, 7, 11, 1, 1))
                );
            } else {
                return List.of(
                        new ToDoResponse(1L, "산책", List.of(new PetResponse(1L, "두부")), false, LocalDate.now(), LocalTime.of(11, 0), true, null, "red", "아빠 사진1", LocalDateTime.of(2024, 1, 7, 11, 1, 1))
                );
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ToDoResponse> getAllToDo(Boolean done, List<Long> pets) {
        if (done != null && !done) {
            if (pets == null || pets.isEmpty()) {
                return List.of(
                        new ToDoResponse(1L, "산책", List.of(new PetResponse(1L, "두부")), false, LocalDate.now(), LocalTime.of(11, 0), true, null, "red", null, null),
                        new ToDoResponse(2L, "약", List.of(new PetResponse(2L, "호동이")), false, LocalDate.now(), LocalTime.of(13, 0), true, null, "yellow", null, null),
                        new ToDoResponse(3L, "유치원 가는 날", List.of(new PetResponse(3L, "삼바")), true, LocalDate.now(), null, false, null, "blue", null, null)
                );
            } else {
                return List.of(
                        new ToDoResponse(1L, "산책", List.of(new PetResponse(1L, "두부")), false, LocalDate.now(), LocalTime.of(11, 0), true, null, "red", null, null)
                );
            }
        } else {
            if (pets == null || pets.isEmpty()) {
                return List.of(
                        new ToDoResponse(1L, "산책", List.of(new PetResponse(1L, "두부")), false, LocalDate.now(), LocalTime.of(11, 0), true, null, "red", "아빠 사진1", LocalDateTime.of(2024, 1, 7, 11, 1, 1))
                );
            } else {
                return List.of(
                        new ToDoResponse(1L, "산책", List.of(new PetResponse(1L, "두부")), false, LocalDate.now(), LocalTime.of(11, 0), true, null, "red", "아빠 사진1", LocalDateTime.of(2024, 1, 7, 11, 1, 1))
                );
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ToDoDetailResponse getToDoById(Long id) {
        return new ToDoDetailResponse(1L, "산책", List.of(new PetResponse(1L, "두부")), false, LocalDate.now(), LocalTime.of(11, 0), true, null, "red", null, null);
    }

    @Override
    @Transactional
    public ToDoDetailResponse updateToDoById(Long id, ToDoPatchRequest request) {
        return new ToDoDetailResponse(1L, "산책", List.of(new PetResponse(1L, "두부")), false, LocalDate.now(), LocalTime.of(11, 0), true, null, "red", null, null);
    }

    @Override
    @Transactional
    public void deleteToDoById(Long id) {
        toDoRepository.deleteById(id);
    }
}
