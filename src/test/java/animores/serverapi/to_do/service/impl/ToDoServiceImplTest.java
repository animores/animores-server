package animores.serverapi.to_do.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.pet.domain.Pet;
import animores.serverapi.pet.repository.PetRepository;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.profile.repository.ProfileRepository;
import animores.serverapi.to_do.entity.PetToDoRelationship;
import animores.serverapi.to_do.entity.ToDo;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import animores.serverapi.to_do.dto.response.ToDoResponse;
import animores.serverapi.to_do.repository.PetToDoRelationshipRepository;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        assertEquals("test", toDo.getTitle());

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
    void getTodayToDo_NotDoneNoPetFilter() {

        // given
        // when
        List<ToDoResponse> result = serviceUnderTest.getTodayToDo(false, null);

        // then
        assertEquals(3, result.size());

        assertEquals("산책", result.get(0).title());
        assertEquals("두부", result.get(0).pets().get(0).name());
        assertFalse(result.get(0).isAllDay());
        assertEquals(LocalDate.now(), result.get(0).date());
        assertEquals(LocalTime.of(11, 0), result.get(0).time());
        assertTrue(result.get(0).hasAlarm());
        assertNull(result.get(0).repeatString());
        assertEquals("red", result.get(0).color());
        assertNull(result.get(0).completeProfileImage());
        assertNull(result.get(0).completeDateTime());

        assertEquals("약", result.get(1).title());
        assertEquals("호동이", result.get(1).pets().get(0).name());
        assertFalse(result.get(1).isAllDay());
        assertEquals(LocalDate.now(), result.get(1).date());
        assertEquals(LocalTime.of(13, 0), result.get(1).time());
        assertTrue(result.get(1).hasAlarm());
        assertNull(result.get(1).repeatString());
        assertEquals("yellow", result.get(1).color());
        assertNull(result.get(1).completeProfileImage());
        assertNull(result.get(1).completeDateTime());

        assertEquals("유치원 가는 날", result.get(2).title());
        assertEquals("삼바", result.get(2).pets().get(0).name());
        assertTrue(result.get(2).isAllDay());
        assertEquals(LocalDate.now(), result.get(2).date());
        assertNull(result.get(2).time());
        assertFalse(result.get(2).hasAlarm());
        assertNull(result.get(2).repeatString());
        assertEquals("blue", result.get(2).color());
        assertNull(result.get(2).completeProfileImage());
        assertNull(result.get(2).completeDateTime());
    }

    @Test
    void getTodayToDo_DoneNoPetFilter() {
        // given
        // when
        List<ToDoResponse> result = serviceUnderTest.getTodayToDo(true, null);

        // then
        assertEquals(1, result.size());

        assertEquals("산책", result.get(0).title());
        assertEquals("두부", result.get(0).pets().get(0).name());
        assertFalse(result.get(0).isAllDay());
        assertEquals(LocalDate.now(), result.get(0).date());
        assertEquals(LocalTime.of(11, 0), result.get(0).time());
        assertTrue(result.get(0).hasAlarm());
        assertNull(result.get(0).repeatString());
        assertEquals("red", result.get(0).color());
        assertEquals("아빠 사진1", result.get(0).completeProfileImage());
        assertEquals(LocalDateTime.of(2024, 1, 7, 11, 1, 1), result.get(0).completeDateTime());
    }

    @Test
    void getTodayToDo_NotDonePetFilter() {
        // given
        // when
        List<ToDoResponse> result = serviceUnderTest.getTodayToDo(false, List.of(1L));

        // then
        assertEquals(1, result.size());

        assertEquals("산책", result.get(0).title());
        assertEquals("두부", result.get(0).pets().get(0).name());
        assertFalse(result.get(0).isAllDay());
        assertEquals(LocalDate.now(), result.get(0).date());
        assertEquals(LocalTime.of(11, 0), result.get(0).time());
        assertTrue(result.get(0).hasAlarm());
        assertNull(result.get(0).repeatString());
        assertEquals("red", result.get(0).color());
        assertNull(result.get(0).completeProfileImage());
        assertNull(result.get(0).completeDateTime());
    }

    @Test
    void getTodayToDo_DonePetFilter() {
        // given
        // when
        List<ToDoResponse> result = serviceUnderTest.getTodayToDo(true, List.of(1L));

        // then
        assertEquals(1, result.size());

        assertEquals("산책", result.get(0).title());
        assertEquals("두부", result.get(0).pets().get(0).name());
        assertFalse(result.get(0).isAllDay());
        assertEquals(LocalDate.now(), result.get(0).date());
        assertEquals(LocalTime.of(11, 0), result.get(0).time());
        assertTrue(result.get(0).hasAlarm());
        assertNull(result.get(0).repeatString());
        assertEquals("red", result.get(0).color());
        assertEquals("아빠 사진1", result.get(0).completeProfileImage());
        assertEquals(LocalDateTime.of(2024, 1, 7, 11, 1, 1), result.get(0).completeDateTime());
    }

    @Test
    void getAllToDo_NotDoneNoPetFilter() {
        // given
        // when
        List<ToDoResponse> result = serviceUnderTest.getAllToDo(false, null);

        // then
        assertEquals(3, result.size());

        assertEquals("산책", result.get(0).title());
        assertEquals("두부", result.get(0).pets().get(0).name());
        assertFalse(result.get(0).isAllDay());
        assertEquals(LocalDate.now(), result.get(0).date());
        assertEquals(LocalTime.of(11, 0), result.get(0).time());
        assertTrue(result.get(0).hasAlarm());
        assertNull(result.get(0).repeatString());
        assertEquals("red", result.get(0).color());
        assertNull(result.get(0).completeProfileImage());
        assertNull(result.get(0).completeDateTime());

        assertEquals("약", result.get(1).title());
        assertEquals("호동이", result.get(1).pets().get(0).name());
        assertFalse(result.get(1).isAllDay());
        assertEquals(LocalDate.now(), result.get(1).date());
        assertEquals(LocalTime.of(13, 0), result.get(1).time());
        assertTrue(result.get(1).hasAlarm());
        assertNull(result.get(1).repeatString());
        assertEquals("yellow", result.get(1).color());
        assertNull(result.get(1).completeProfileImage());
        assertNull(result.get(1).completeDateTime());

        assertEquals("유치원 가는 날", result.get(2).title());
        assertEquals("삼바", result.get(2).pets().get(0).name());
        assertTrue(result.get(2).isAllDay());
        assertEquals(LocalDate.now(), result.get(2).date());
        assertNull(result.get(2).time());
        assertFalse(result.get(2).hasAlarm());
        assertNull(result.get(2).repeatString());
        assertEquals("blue", result.get(2).color());
        assertNull(result.get(2).completeProfileImage());
        assertNull(result.get(2).completeDateTime());
    }

    @Test
    void getToDoById() {
        // given
        // when
        var result = serviceUnderTest.getToDoById(1L);

        // then
        assertEquals("산책", result.title());
        assertEquals("두부", result.pets().get(0).name());
        assertFalse(result.isAllDay());
        assertEquals(LocalDate.now(), result.date());
        assertEquals(LocalTime.of(11, 0), result.time());
        assertTrue(result.hasAlarm());
        assertNull(result.repeatString());
        assertEquals("red", result.color());
        assertNull(result.completeProfileImage());
        assertNull(result.completeDateTime());

    }

    @Test
    void updateToDoById() {
        // given
        ToDoPatchRequest request = new ToDoPatchRequest(
                1L,
                null,
                "산책",
                null,
                null,
                false,
                null,
                false,
                null);
        // when
        var result = serviceUnderTest.updateToDoById(1L, request);

        // then
        assertEquals("산책", result.title());
        assertEquals("두부", result.pets().get(0).name());
        assertFalse(result.isAllDay());
        assertEquals(LocalDate.now(), result.date());
        assertEquals(LocalTime.of(11, 0), result.time());
        assertTrue(result.hasAlarm());
        assertNull(result.repeatString());
        assertEquals("red", result.color());
        assertNull(result.completeProfileImage());
        assertNull(result.completeDateTime());
    }

    @Test
    void deleteToDoById() {
        // given

        // when
        serviceUnderTest.deleteToDoById(1L);
        ArgumentCaptor<ToDo> toDoArgumentCaptor = ArgumentCaptor.forClass(ToDo.class);

        // then
        verify(toDoRepository, times(1)).deleteById(1L);
    }
}