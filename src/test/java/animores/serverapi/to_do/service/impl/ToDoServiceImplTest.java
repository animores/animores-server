package animores.serverapi.to_do.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.pet.entity.Pet;
import animores.serverapi.pet.repository.PetRepository;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.profile.dao.ProfileVo;
import animores.serverapi.profile.repository.ProfileRepository;
import animores.serverapi.to_do.dao.GetToDoPageDao;
import animores.serverapi.to_do.dao.ToDoDao;
import animores.serverapi.to_do.dao.ToDoInstanceDao;
import animores.serverapi.to_do.dto.Repeat;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import animores.serverapi.to_do.dto.response.ToDoPageResponse;
import animores.serverapi.to_do.entity.PetToDoRelationship;
import animores.serverapi.to_do.entity.ToDo;
import animores.serverapi.to_do.repository.PetToDoRelationshipRepository;
import animores.serverapi.to_do.repository.ToDoInstanceRepository;
import animores.serverapi.to_do.repository.ToDoRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToDoServiceImplTest {
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

    private static final Long ACCOUNT_ID = 1L;
    private static final Long PROFILE_ID = 2L;
    private static final Long PET_1_ID = 3L;
    private static final Long PET_2_ID = 4L;


    @Test
    void createToDoTest_Success() {

        //given
        Account account = new TestAccount(ACCOUNT_ID);
        Profile createProfile = new Profile();
        given(profileRepository.getReferenceById(PROFILE_ID)).willReturn(createProfile);

        Pet pet1 = new TestPet(PET_1_ID, "두부");
        Pet pet2 = new TestPet(PET_2_ID, "호동이");
        given(petRepository.getReferenceById(PET_1_ID)).willReturn(pet1);
        given(petRepository.getReferenceById(PET_2_ID)).willReturn(pet2);
        when(toDoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ToDoCreateRequest request = new ToDoCreateRequest(
                PROFILE_ID,
                List.of(PET_1_ID, PET_2_ID),
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
        serviceUnderTest.createToDo(account, request);

        //then
        verify(toDoRepository, times(1)).save(toDoArgumentCaptor.capture());
        ToDo toDo = toDoArgumentCaptor.getValue();
        assertEquals("test", toDo.getContent());

        verify(petToDoRelationshipRepository, times(1)).saveAll(petToDoRelationshipArgumentCaptor.capture());
        List<PetToDoRelationship> petToDoRelationships = petToDoRelationshipArgumentCaptor.getValue();
        assertEquals(2, petToDoRelationships.size());

    }

    @Test
    void getTodayToDo_Done() {
        // given
        Pet pet1 = new TestPet(PET_1_ID, "두부");
        Pet pet2 = new TestPet(PET_2_ID, "호동이");

        TestToDo toDo1 = new TestToDo(1L, null,"산책", LocalDate.now(), LocalTime.of(11, 0), false, "red", true, null);
        var relationships = List.of(
                new PetToDoRelationship(pet1, toDo1),
                new PetToDoRelationship(pet2, toDo1)
        );
        toDo1.setPetToDoRelationships(relationships);

        given(petToDoRelationshipRepository.findAllByPet_IdIn(List.of(PET_1_ID, PET_2_ID)))
                .willReturn(relationships);

        given(toDoInstanceRepository.findAllByCompleteAndTodayToDoIdIn(List.of(1L),1,5))
                .willReturn(
                    new GetToDoPageDao(
                            1,
                            5,
                            1,
                            1,
                            List.of(
                                    new ToDoInstanceDao(
                                            1L,
                                            ToDoDao.fromToDo(toDo1),
                                            LocalDate.now(),
                                            LocalTime.of(11, 0),
                                            ProfileVo.fromProfile(new TestProfile("아빠 사진1")),
                                            LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 1, 1))
                                    ))
                    )
                        );


        // when
        var result = serviceUnderTest.getTodayToDo(true, List.of(pet1, pet2), 1, 5);

        // then
        assertEquals(1, result.toDoList().size());

        assertEquals("산책", result.toDoList().get(0).title());
        assertEquals("두부", result.toDoList().get(0).pets().get(0).name());
        assertEquals("호동이", result.toDoList().get(0).pets().get(1).name());
        assertFalse(result.toDoList().get(0).isAllDay());
        assertEquals(LocalDate.now(), result.toDoList().get(0).date());
        assertEquals(LocalTime.of(11, 0), result.toDoList().get(0).time());
        assertTrue(result.toDoList().get(0).isUsingAlarm());
        assertEquals("red", result.toDoList().get(0).color());
        assertEquals("아빠 사진1", result.toDoList().get(0).completeProfileImage());
        assertEquals(LocalDateTime.of(LocalDate.now(), LocalTime.of(11,1,1)), result.toDoList().get(0).completeDateTime());
    }

    @Test
    void getTodayToDo_NotDone() {
        // given
        Pet pet1 = new TestPet(PET_1_ID, "두부");
        Pet pet2 = new TestPet(PET_2_ID, "호동이");

        TestToDo toDo1 = new TestToDo(1L, null,"산책", LocalDate.now(), LocalTime.of(11, 0), false, "red", true, null);
        var relationships = List.of(
                new PetToDoRelationship(pet1, toDo1),
                new PetToDoRelationship(pet2, toDo1)
        );
        toDo1.setPetToDoRelationships(relationships);

        given(petToDoRelationshipRepository.findAllByPet_IdIn(List.of(PET_1_ID, PET_2_ID)))
                .willReturn(relationships);

        given(toDoInstanceRepository.findAllByCompleteFalseAndTodayToDoIdIn(List.of(1L),1,5))
                .willReturn(
                        new GetToDoPageDao(
                                1,
                                5,
                                1,
                                1,
                                List.of(
                                        new ToDoInstanceDao(
                                                1L,
                                                ToDoDao.fromToDo(toDo1),
                                                LocalDate.now(),
                                                LocalTime.of(11, 0),
                                                null,
                                                null
                                        ))
                        )
                );


        // when
        ToDoPageResponse result = serviceUnderTest.getTodayToDo(false, List.of(pet1, pet2), 1, 5);

        // then
        assertEquals(1, result.toDoList().size());
        assertEquals("산책", result.toDoList().get(0).title());
        assertEquals("두부", result.toDoList().get(0).pets().get(0).name());
        assertFalse(result.toDoList().get(0).isAllDay());
        assertEquals(LocalDate.now(), result.toDoList().get(0).date());
        assertEquals(LocalTime.of(11, 0), result.toDoList().get(0).time());
        assertTrue(result.toDoList().get(0).isUsingAlarm());
        assertEquals("red", result.toDoList().get(0).color());
        assertNull(result.toDoList().get(0).completeProfileImage());
        assertNull(result.toDoList().get(0).completeDateTime());
    }

    @Test
    void getAllToDo_NotDoneNoPetFilter() {
        // given
        Pet pet1 = new TestPet(PET_1_ID, "두부");
        Pet pet2 = new TestPet(PET_2_ID, "호동이");

        TestToDo toDo1 = new TestToDo(1L, null,"산책", LocalDate.now(), LocalTime.of(11, 0), false, "red", true, null);
        TestToDo toDo2 = new TestToDo(2L, null,"약", LocalDate.now(), LocalTime.of(13, 0), false, "yellow", true, null);
        TestToDo toDo3 = new TestToDo(3L, null,"유치원 가는 날", LocalDate.now(), null, true, "blue", false, null);

        var relationship1 =new PetToDoRelationship(pet1, toDo1);
        var relationship2 = new PetToDoRelationship(pet2, toDo2);
        var relationship3 = new PetToDoRelationship(pet2, toDo3);

        toDo1.setPetToDoRelationships(List.of(relationship1));
        toDo2.setPetToDoRelationships(List.of(relationship2));
        toDo3.setPetToDoRelationships(List.of(relationship3));

        given(petToDoRelationshipRepository.findAllByPet_IdIn(List.of(PET_1_ID, PET_2_ID)))
                .willReturn(List.of(relationship1, relationship2, relationship3));

        given(toDoInstanceRepository.findAllByCompleteFalseAndToDoIdIn(List.of(1L,2L,3L),1,5))
                .willReturn(
                        new GetToDoPageDao(
                                1,
                                5,
                                3,
                                1,
                                List.of(
                                new ToDoInstanceDao(
                                        1L,
                                        ToDoDao.fromToDo(toDo1),
                                        LocalDate.now(),
                                        LocalTime.of(11, 0),
                                        null,
                                        null
                                ),
                                new ToDoInstanceDao(
                                        2L,
                                        ToDoDao.fromToDo(toDo2),
                                        LocalDate.now(),
                                        LocalTime.of(13, 0),
                                        null,
                                        null
                                ),
                                new ToDoInstanceDao(
                                        3L,
                                        ToDoDao.fromToDo(toDo3),
                                        LocalDate.now(),
                                        null,
                                        null,
                                        null
                                )
                        )));

        // when
        ToDoPageResponse result = serviceUnderTest.getAllToDo(false, List.of(pet1,pet2), 1, 5);

        // then
        assertEquals(3, result.toDoList().size());

        assertEquals("산책", result.toDoList().get(0).title());
        assertEquals("두부", result.toDoList().get(0).pets().get(0).name());
        assertFalse(result.toDoList().get(0).isAllDay());
        assertEquals(LocalDate.now(), result.toDoList().get(0).date());
        assertEquals(LocalTime.of(11, 0), result.toDoList().get(0).time());
        assertTrue(result.toDoList().get(0).isUsingAlarm());
        assertEquals("red", result.toDoList().get(0).color());
        assertNull(result.toDoList().get(0).completeProfileImage());
        assertNull(result.toDoList().get(0).completeDateTime());

        assertEquals("약", result.toDoList().get(1).title());
        assertEquals("호동이", result.toDoList().get(1).pets().get(0).name());
        assertFalse(result.toDoList().get(1).isAllDay());
        assertEquals(LocalDate.now(), result.toDoList().get(1).date());
        assertEquals(LocalTime.of(13, 0), result.toDoList().get(1).time());
        assertTrue(result.toDoList().get(1).isUsingAlarm());
        assertEquals("yellow", result.toDoList().get(1).color());
        assertNull(result.toDoList().get(1).completeProfileImage());
        assertNull(result.toDoList().get(1).completeDateTime());

        assertEquals("유치원 가는 날", result.toDoList().get(2).title());
        assertEquals("호동이", result.toDoList().get(2).pets().get(0).name());
        assertTrue(result.toDoList().get(2).isAllDay());
        assertEquals(LocalDate.now(), result.toDoList().get(2).date());
        assertNull(result.toDoList().get(2).time());
        assertFalse(result.toDoList().get(2).isUsingAlarm());
        assertEquals("blue", result.toDoList().get(2).color());
        assertNull(result.toDoList().get(2).completeProfileImage());
        assertNull(result.toDoList().get(2).completeDateTime());
    }

    @Test
    void getToDoById() {
        // given

        given(toDoRepository.findByIdAndAccount_Id(1L,ACCOUNT_ID))
                .willReturn(Optional.of(new TestToDo(1L,
                        List.of(new PetToDoRelationship(new TestPet(PET_1_ID, "두부"), null)),
                        "산책",
                        LocalDate.now(),
                        LocalTime.of(11, 0),
                        false,
                        "red",
                        true,
                        null)));


        // when
        var result = serviceUnderTest.getToDoById(1L, 1L);

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
        given(toDoRepository.findByIdAndAccount_Id(1L,ACCOUNT_ID))
                .willReturn(java.util.Optional.of(
                        new TestToDo(1L,
                                    List.of(new PetToDoRelationship(new TestPet(PET_1_ID, "두부"), null)),
                                    "산책",
                                    LocalDate.now(),
                                    LocalTime.of(11, 0),
                                    false,
                                    "red",
                                    true,
                                    null)));

        ToDoPatchRequest request = new ToDoPatchRequest(
                PROFILE_ID,
                List.of(PET_1_ID),
                null,
                "산책가즈아",
                null,
                null,
                false,
                null,
                false);
        // when
        var result = serviceUnderTest.updateToDoById(1L, request, ACCOUNT_ID);

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
        given(toDoRepository.findById(1L))
                .willReturn(Optional.of(new TestToDo(1L,
                        List.of(new PetToDoRelationship(new TestPet(PET_1_ID, "두부"), null)),
                        "산책",
                        LocalDate.now(),
                        LocalTime.of(11, 0),
                        false,
                        "red",
                        true,
                        null)));
        // when
        serviceUnderTest.deleteToDoById(1L, PROFILE_ID);

        // then
        verify(toDoRepository, times(1)).deleteById(1L);
    }

    static class TestAccount extends Account {
        public TestAccount(Long id) {
            super(id, null, null, null, null, false);
        }
    }

    static class TestProfile extends Profile {
        public TestProfile(String imageUrl) {
            super(null, null, null, imageUrl,null, null);
        }

        public TestProfile(Long id, String imageUrl) {
            super(id, null, null,  imageUrl,null, null);
        }
    }

    static class TestPet extends Pet {
        public TestPet(Long id, String name) {
            super(id, null, null, null, name, null, 0, null);
        }
    }

    static class TestToDo extends ToDo {
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
                    new TestProfile(PROFILE_ID, "아빠 사진1"),
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