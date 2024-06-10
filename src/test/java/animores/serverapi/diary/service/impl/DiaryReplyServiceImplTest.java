package animores.serverapi.diary.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.common.service.impl.AuthorizationServiceImpl;
import animores.serverapi.diary.dto.AddDiaryReplyRequest;
import animores.serverapi.diary.dto.EditDiaryReplyRequest;
import animores.serverapi.diary.dto.RemoveDiaryReplyRequest;
import animores.serverapi.diary.entity.DiaryReply;
import animores.serverapi.diary.repository.DiaryCommentRepository;
import animores.serverapi.diary.repository.DiaryReplyRepository;
import animores.serverapi.profile.entity.Profile;
import animores.serverapi.profile.repository.ProfileRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DiaryReplyServiceImplTest {

    private static final Long PROFILE_ID_1 = 1L;
    private static final Long PROFILE_ID_2 = 2L;
    private static final Long DIARY_COMMENT_ID = 3L;
    private static final Long DIARY_REPLY_ID = 4L;
    private static final String REPLY_CONTENT = "대댓글";
    private static final String UPDATED_CONTENT = "수정될 댓글";

    @InjectMocks
    private DiaryReplyServiceImpl diaryReplyService;

    @Mock
    private AuthorizationServiceImpl authorizationService;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private DiaryCommentRepository diaryCommentRepository;

    @Mock
    private DiaryReplyRepository diaryReplyRepository;

    private Profile profile1;
    private Profile profile2;
    private AddDiaryReplyRequest addRequest;
    private EditDiaryReplyRequest editRequest;
    private RemoveDiaryReplyRequest removeRequest;

    private DiaryReply diaryReply;

    @BeforeEach
    void setUp() {
        profile1 = new TestProfile(PROFILE_ID_1);
        profile2 = new TestProfile(PROFILE_ID_2);
        diaryReply = new TestDiaryReply(DIARY_REPLY_ID, profile1);

        addRequest = new AddDiaryReplyRequest(PROFILE_ID_2, DIARY_COMMENT_ID, REPLY_CONTENT);
        editRequest = new EditDiaryReplyRequest(PROFILE_ID_2, UPDATED_CONTENT);
        removeRequest = new RemoveDiaryReplyRequest(PROFILE_ID_2);
    }

    @Test
    void profileNotFound() {
        when(profileRepository.findById(PROFILE_ID_2)).thenReturn(Optional.empty());

        CustomException addException = assertThrows(CustomException.class, () -> {
            diaryReplyService.addDiaryReply(null, addRequest);
        });
        CustomException editException = assertThrows(CustomException.class, () -> {
            diaryReplyService.editDiaryReply(null, DIARY_REPLY_ID, editRequest);
        });
        CustomException removeException = assertThrows(CustomException.class, () -> {
            diaryReplyService.removeDiaryReply(null, DIARY_REPLY_ID, removeRequest);
        });

        assertEquals(ExceptionCode.INVALID_PROFILE, addException.getCode());
        assertEquals(ExceptionCode.INVALID_PROFILE, editException.getCode());
        assertEquals(ExceptionCode.INVALID_PROFILE, removeException.getCode());
    }

    @Test
    void diaryCommentNotFound() {
        when(profileRepository.findById(PROFILE_ID_2)).thenReturn(Optional.of(profile2));
        when(diaryCommentRepository.findByIdAndDeletedDtIsNull(DIARY_COMMENT_ID)).thenReturn(
            Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            diaryReplyService.addDiaryReply(null, addRequest);
        });

        assertEquals(ExceptionCode.NOT_FOUND_DIARY_COMMENT, exception.getCode());
    }

    @Test
    void editDiaryReplyNotAuthorized() {
        when(profileRepository.findById(PROFILE_ID_2)).thenReturn(Optional.of(profile2));
        when(diaryReplyRepository.findByIdAndDeletedDtIsNull(DIARY_REPLY_ID)).thenReturn(
            Optional.of(diaryReply));
        doThrow(new CustomException(ExceptionCode.UNAUTHORIZED_DIARY_REPLY_ACCESS))
            .when(authorizationService).validateDiaryReplyAccess(diaryReply, profile2);

        CustomException exception = assertThrows(CustomException.class, () -> {
            diaryReplyService.editDiaryReply(null, DIARY_REPLY_ID, editRequest);
        });

        assertEquals(ExceptionCode.UNAUTHORIZED_DIARY_REPLY_ACCESS, exception.getCode());
    }

    @Test
    void removeDiaryReplyNotAuthorized() {
        when(profileRepository.findById(PROFILE_ID_2)).thenReturn(Optional.of(profile2));
        when(diaryReplyRepository.findByIdAndDeletedDtIsNull(DIARY_REPLY_ID)).thenReturn(
            Optional.of(diaryReply));
        doThrow(new CustomException(ExceptionCode.UNAUTHORIZED_DIARY_REPLY_ACCESS))
            .when(authorizationService).validateDiaryReplyAccess(diaryReply, profile2);

        CustomException exception = assertThrows(CustomException.class, () -> {
            diaryReplyService.removeDiaryReply(null, DIARY_REPLY_ID, removeRequest);
        });

        assertEquals(ExceptionCode.UNAUTHORIZED_DIARY_REPLY_ACCESS, exception.getCode());
    }

    private static class TestProfile extends Profile {

        public TestProfile(Long id) {
            super(id, null, null, null, null, null);
        }
    }

    private static class TestDiaryReply extends DiaryReply {

        public TestDiaryReply(Long id, Profile profile) {
            super(id, null, profile, null, null);
        }
    }

}
