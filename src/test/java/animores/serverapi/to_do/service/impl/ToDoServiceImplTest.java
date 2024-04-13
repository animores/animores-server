package animores.serverapi.to_do.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.pet.domain.Pet;
import animores.serverapi.pet.repository.PetRepository;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.profile.domain.vo.ProfileVo;
import animores.serverapi.profile.repository.ProfileRepository;
import animores.serverapi.to_do.dto.Repeat;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import animores.serverapi.to_do.dto.response.ToDoResponse;
import animores.serverapi.to_do.entity.PetToDoRelationship;
import animores.serverapi.to_do.entity.ToDo;
import animores.serverapi.to_do.entity.vo.ToDoInstanceVo;
import animores.serverapi.to_do.entity.vo.ToDoVo;
import animores.serverapi.to_do.repository.PetToDoRelationshipRepository;
import animores.serverapi.to_do.repository.ToDoInstanceRepository;
import animores.serverapi.to_do.repository.ToDoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToDoServiceImplTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private ToDoRepository toDoRepository;
    @Mock
    private PetRepository petRepository;
    @Mock
    private PetToDoRelationshipRepository petToDoRelationshipRepository;
    @Mock
    private ToDoInstanceRepository toDoInstanceRepository;
    @InjectMocks
    private ToDoServiceImpl serviceUnderTest;

    @Test
    void createToDoTest_Success() {
        //given
        Account account = new Account();
        given(accountRepository.getReferenceById(1L)).willReturn(account);

        Profile createProfile = new Profile();
        given(profileRepository.getReferenceById(2L)).willReturn(createProfile);

        given(petRepository.getReferenceById(1L)).willReturn(new Pet());
        given(petRepository.getReferenceById(2L)).willReturn(new Pet());
        when(toDoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ToDoCreateRequest request = new ToDoCreateRequest(
                List.of(1L, 2L),
                "test",
                null,
                null,
                null,
                true,
                null,
                false,
                null
        );
        ArgumentCaptor<ToDo> toDoArgumentCaptor = ArgumentCaptor.forClass(ToDo.class);
        ArgumentCaptor<List<PetToDoRelationship>> petToDoRelationshipArgumentCaptor = ArgumentCaptor.forClass(List.class);

        //when
        serviceUnderTest.createToDo(request);

        //then
        verify(toDoRepository, times(1)).save(toDoArgumentCaptor.capture());
        ToDo toDo = toDoArgumentCaptor.getValue();
        assertEquals("test", toDo.getContent());

        verify(petToDoRelationshipRepository, times(1)).saveAll(petToDoRelationshipArgumentCaptor.capture());
        List<PetToDoRelationship> petToDoRelationships = petToDoRelationshipArgumentCaptor.getValue();
        assertEquals(2, petToDoRelationships.size());
    }

    @Test
    @DisplayName("To Do 생성 fail test - 펫 아이디가 잘못된 경우 예외 발생")
    void createToDoTestFail_InvalidPetIds() {
        //given
        Account account = new Account();
        given(accountRepository.getReferenceById(1L)).willReturn(account);

        Profile createProfile = new Profile();
        given(profileRepository.getReferenceById(2L)).willReturn(createProfile);

        ToDoCreateRequest request = new ToDoCreateRequest(
                List.of(3L),
                "test",
                null,
                null,
                null,
                true,
                null,
                false,
                null
        );

        //when
        //then
        try {
            serviceUnderTest.createToDo(request);
            fail();
        } catch (Exception e) {
            assertEquals(ExceptionCode.ILLEGAL_PET_IDS.getMessage(), e.getMessage());
        }
    }

    @Test
    void getTodayToDo_Done() {
        // given
        Pet pet1 = new TestPet(1L, "두부");
        Pet pet2 = new TestPet(2L, "호동이");

        TestToDo toDo1 = new TestToDo(1L, null,"산책", LocalDate.now(), LocalTime.of(11, 0), false, "red", true, null);
        var relationships = List.of(
                new PetToDoRelationship(pet1, toDo1),
                new PetToDoRelationship(pet2, toDo1)
        );
        toDo1.setPetToDoRelationships(relationships);

        given(petRepository.findAllById(List.of(1L, 2L)))
                .willReturn(List.of(pet1, pet2));

        given(petToDoRelationshipRepository.findAllByPet_IdIn(List.of(1L, 2L)))
                .willReturn(relationships);

        given(toDoInstanceRepository.findAllByCompleteAndTodayToDoIdIn(List.of(1L)))
                .willReturn(List.of(
                        new ToDoInstanceVo(
                                1L,
                                ToDoVo.fromToDo(toDo1),
                                LocalDate.now(),
                                LocalTime.of(11, 0),
                                ProfileVo.fromProfile(new TestProfile( "아빠 사진1")),
                                LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 1, 1))
                )));


        // when
        List<ToDoResponse> result = serviceUnderTest.getTodayToDo(true, List.of(1L,2L));

        // then
        assertEquals(1, result.size());

        assertEquals("산책", result.get(0).title());
        assertEquals("두부", result.get(0).pets().get(0).name());
        assertEquals("호동이", result.get(0).pets().get(1).name());
        assertFalse(result.get(0).isAllDay());
        assertEquals(LocalDate.now(), result.get(0).date());
        assertEquals(LocalTime.of(11, 0), result.get(0).time());
        assertTrue(result.get(0).isUsingAlarm());
        assertEquals("red", result.get(0).color());
        assertEquals("아빠 사진1", result.get(0).completeProfileImage());
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.of(11,1,1)), result.get(0).completeDateTime());
    }

    @Test
    void getTodayToDo_NotDone() {
        // given
        Pet pet1 = new TestPet(1L, "두부");
        Pet pet2 = new TestPet(2L, "호동이");

        TestToDo toDo1 = new TestToDo(1L, null,"산책", LocalDate.now(), LocalTime.of(11, 0), false, "red", true, null);
        var relationships = List.of(
                new PetToDoRelationship(pet1, toDo1),
                new PetToDoRelationship(pet2, toDo1)
        );
        toDo1.setPetToDoRelationships(relationships);

        given(petRepository.findAllById(List.of(1L, 2L)))
                .willReturn(List.of(pet1, pet2));

        given(petToDoRelationshipRepository.findAllByPet_IdIn(List.of(1L, 2L)))
                .willReturn(relationships);

        given(toDoInstanceRepository.findAllByCompleteFalseAndTodayToDoIdIn(List.of(1L)))
                .willReturn(List.of(
                        new ToDoInstanceVo(
                                1L,
                                ToDoVo.fromToDo(toDo1),
                                LocalDate.now(),
                                LocalTime.of(11, 0),
                                null,
                                null
                        )));


        // when
        List<ToDoResponse> result = serviceUnderTest.getTodayToDo(false, List.of(1L, 2L));

        // then
        assertEquals(1, result.size());

        assertEquals("산책", result.get(0).title());
        assertEquals("두부", result.get(0).pets().get(0).name());
        assertFalse(result.get(0).isAllDay());
        assertEquals(LocalDate.now(), result.get(0).date());
        assertEquals(LocalTime.of(11, 0), result.get(0).time());
        assertTrue(result.get(0).isUsingAlarm());
        assertEquals("red", result.get(0).color());
        assertNull(result.get(0).completeProfileImage());
        assertNull(result.get(0).completeDateTime());
    }

    @Test
    void getAllToDo_NotDoneNoPetFilter() {
        // given
        Pet pet1 = new TestPet(1L, "두부");
        Pet pet2 = new TestPet(2L, "호동이");

        TestToDo toDo1 = new TestToDo(1L, null,"산책", LocalDate.now(), LocalTime.of(11, 0), false, "red", true, null);
        TestToDo toDo2 = new TestToDo(2L, null,"약", LocalDate.now(), LocalTime.of(13, 0), false, "yellow", true, null);
        TestToDo toDo3 = new TestToDo(3L, null,"유치원 가는 날", LocalDate.now(), null, true, "blue", false, null);

        var relationship1 =new PetToDoRelationship(pet1, toDo1);
        var relationship2 = new PetToDoRelationship(pet2, toDo2);
        var relationship3 = new PetToDoRelationship(pet2, toDo3);

        toDo1.setPetToDoRelationships(List.of(relationship1));
        toDo2.setPetToDoRelationships(List.of(relationship2));
        toDo3.setPetToDoRelationships(List.of(relationship3));

        given(petRepository.findAllById(List.of(1L, 2L)))
                .willReturn(List.of(pet1, pet2));

        given(petToDoRelationshipRepository.findAllByPet_IdIn(List.of(1L, 2L)))
                .willReturn(List.of(relationship1, relationship2, relationship3));

        given(toDoInstanceRepository.findAllByCompleteFalseAndToDoIdIn(List.of(1L,2L,3L)))
                .willReturn(List.of(
                        new ToDoInstanceVo(
                                1L,
                                ToDoVo.fromToDo(toDo1),
                                LocalDate.now(),
                                LocalTime.of(11, 0),
                                null,
                                null
                        ),
                        new ToDoInstanceVo(
                                2L,
                                ToDoVo.fromToDo(toDo2),
                                LocalDate.now(),
                                LocalTime.of(13, 0),
                                null,
                                null
                        ),
                        new ToDoInstanceVo(
                                3L,
                                ToDoVo.fromToDo(toDo3),
                                LocalDate.now(),
                                null,
                                null,
                                null
                        )
                ));

        // when
        List<ToDoResponse> result = serviceUnderTest.getAllToDo(false, null);

        // then
        assertEquals(3, result.size());

        assertEquals("산책", result.get(0).title());
        assertEquals("두부", result.get(0).pets().get(0).name());
        assertFalse(result.get(0).isAllDay());
        assertEquals(LocalDate.now(), result.get(0).date());
        assertEquals(LocalTime.of(11, 0), result.get(0).time());
        assertTrue(result.get(0).isUsingAlarm());
        assertEquals("red", result.get(0).color());
        assertNull(result.get(0).completeProfileImage());
        assertNull(result.get(0).completeDateTime());

        assertEquals("약", result.get(1).title());
        assertEquals("호동이", result.get(1).pets().get(0).name());
        assertFalse(result.get(1).isAllDay());
        assertEquals(LocalDate.now(), result.get(1).date());
        assertEquals(LocalTime.of(13, 0), result.get(1).time());
        assertTrue(result.get(1).isUsingAlarm());
        assertEquals("yellow", result.get(1).color());
        assertNull(result.get(1).completeProfileImage());
        assertNull(result.get(1).completeDateTime());

        assertEquals("유치원 가는 날", result.get(2).title());
        assertEquals("호동이", result.get(2).pets().get(0).name());
        assertTrue(result.get(2).isAllDay());
        assertEquals(LocalDate.now(), result.get(2).date());
        assertNull(result.get(2).time());
        assertFalse(result.get(2).isUsingAlarm());
        assertEquals("blue", result.get(2).color());
        assertNull(result.get(2).completeProfileImage());
        assertNull(result.get(2).completeDateTime());
    }

    @Test
    void getToDoById() {
        // given
        given(toDoRepository.findByIdAndAccount_Id(1L,1L))
                .willReturn(java.util.Optional.of(new TestToDo(1L,
                        List.of(new PetToDoRelationship(new TestPet(1L, "두부"), null)),
                        "산책",
                        LocalDate.now(),
                        LocalTime.of(11, 0),
                        false,
                        "red",
                        true,
                        null)));


        // when
        var result = serviceUnderTest.getToDoById(1L);

        // then
        assertEquals("산책", result.title());
        assertEquals("두부", result.pets().get(0).name());
        assertFalse(result.isAllDay());
        assertEquals(LocalDate.now(), result.date());
        assertEquals(LocalTime.of(11, 0), result.time());
        assertTrue(result.isUsingAlarm());
        assertNull(result.unit());
        assertEquals("red", result.color());
    }

    @Test
    void updateToDoById() {
        // given
        given(toDoRepository.findByIdAndAccount_Id(1L,1L))
                .willReturn(java.util.Optional.of(
                        new TestToDo(1L,
                                    List.of(new PetToDoRelationship(new TestPet(1L, "두부"), null)),
                                    "산책",
                                    LocalDate.now(),
                                    LocalTime.of(11, 0),
                                    false,
                                    "red",
                                    true,
                                    null)));

        ToDoPatchRequest request = new ToDoPatchRequest(
                List.of(1L),
                null,
                "산책가즈아",
                null,
                null,
                false,
                null,
                false);
        // when
        var result = serviceUnderTest.updateToDoById(1L, request);

        // then
        assertEquals("산책가즈아", result.title());
        assertEquals("두부", result.pets().get(0).name());
        assertFalse(result.isAllDay());
        assertEquals(LocalDate.now(), result.date());
        assertEquals(LocalTime.of(11, 0), result.time());
        assertFalse(result.isUsingAlarm());
        assertNull(result.unit());
        assertEquals("red", result.color());
    }

    @Test
    void deleteToDoById() {
        // given
        // when
        serviceUnderTest.deleteToDoById(1L);

        // then
        verify(toDoRepository, times(1)).deleteById(1L);
    }

    class TestProfile extends Profile {
        public TestProfile(String imageUrl) {
            super(null, null, null, imageUrl,null);
        }

        public TestProfile(Long id, String imageUrl) {
            super(id, null, null,  imageUrl,null);
        }
    }

    static class TestPet extends Pet {
        public TestPet(Long id, String name) {
            super(id, null,name, null, 0);
        }
    }

    class TestToDo extends ToDo {
        public TestToDo(Long id,
                        List<PetToDoRelationship> relationships,
                        String content,
                        LocalDate date,
                        LocalTime time,
                        boolean isAllDay,
                        String color,
                        boolean isUsingAlarm,
                        Repeat repeat) {

            super(id,
                    relationships,
                    null,
                    new TestProfile(2L, "아빠 사진1"),
                    date,
                    time,
                    isAllDay,
                    content,
                    null,
                    color,
                    isUsingAlarm,
                    repeat == null ? null : repeat.unit(),
                    repeat == null ? null : repeat.interval(),
                    repeat == null ? null : repeat.weekDays());
        }

        public void setPetToDoRelationships(List<PetToDoRelationship> petToDoRelationships) {
            super.setPetToDoRelationships(petToDoRelationships);
        }
    }
}