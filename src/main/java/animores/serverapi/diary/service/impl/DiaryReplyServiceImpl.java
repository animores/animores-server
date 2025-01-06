package animores.serverapi.diary.service.impl;

import animores.serverapi.account.entity.Account;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.common.service.AuthorizationService;
import animores.serverapi.diary.dto.AddDiaryReplyRequest;
import animores.serverapi.diary.dto.EditDiaryReplyRequest;
import animores.serverapi.diary.dto.RemoveDiaryReplyRequest;
import animores.serverapi.diary.entity.DiaryComment;
import animores.serverapi.diary.entity.DiaryReply;
import animores.serverapi.diary.repository.DiaryCommentRepository;
import animores.serverapi.diary.repository.DiaryReplyRepository;
import animores.serverapi.diary.service.DiaryReplyService;
import animores.serverapi.profile.domain.Profile;
import animores.serverapi.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryReplyServiceImpl implements DiaryReplyService {

    private final ProfileRepository profileRepository;
    private final AuthorizationService authorizationService;
    private final DiaryCommentRepository diaryCommentRepository;
    private final DiaryReplyRepository diaryReplyRepository;

    @Override
    @Transactional
    public void addDiaryReply(Account account, AddDiaryReplyRequest request) {
        Profile profile = findProfileById(request.profileId());
        DiaryComment diaryComment = findDiaryComment(request.diaryCommentId());

        diaryReplyRepository.save(DiaryReply.create(diaryComment, profile, request.content()));
    }

    @Override
    @Transactional
    public void editDiaryReply(Account account, Long replyId, EditDiaryReplyRequest request) {
        Profile profile = findProfileById(request.profileId());
        DiaryReply diaryReply = findDiaryReply(replyId);

        authorizationService.validateDiaryReplyAccess(diaryReply, profile);

        diaryReply.updateContent(request.content());
    }

    @Override
    @Transactional
    public void removeDiaryReply(Account account, Long replyId, RemoveDiaryReplyRequest request) {
        Profile profile = findProfileById(request.profileId());
        DiaryReply diaryReply = findDiaryReply(replyId);

        authorizationService.validateDiaryReplyAccess(diaryReply, profile);

        diaryReply.delete();
    }

    private Profile findProfileById(Long id) {
        return profileRepository.findById(id)
            .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_PROFILE));
    }

    public DiaryComment findDiaryComment(Long id) {
        return diaryCommentRepository.findByIdAndDeletedDtIsNull(id)
            .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_DIARY_COMMENT));
    }

    private DiaryReply findDiaryReply(Long id) {
        return diaryReplyRepository.findByIdAndDeletedDtIsNull(id)
            .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_DIARY_REPLY));
    }
}
