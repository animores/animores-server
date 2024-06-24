package animores.serverapi.diary.service.impl;

import animores.serverapi.account.entity.Account;
import animores.serverapi.common.service.AuthorizationService;
import animores.serverapi.diary.dto.AddDiaryCommentRequest;
import animores.serverapi.diary.dto.EditDiaryCommentRequest;
import animores.serverapi.diary.dto.RemoveDiaryCommentRequest;
import animores.serverapi.diary.entity.Diary;
import animores.serverapi.diary.entity.DiaryComment;
import animores.serverapi.diary.repository.DiaryCommentRepository;
import animores.serverapi.diary.repository.DiaryRepository;
import animores.serverapi.diary.service.DiaryCommentService;
import animores.serverapi.profile.entity.Profile;
import animores.serverapi.profile.repository.ProfileRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryCommentServiceImpl implements DiaryCommentService {

    private final AuthorizationService authorizationService;

    private final ProfileRepository profileRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryCommentRepository diaryCommentRepository;

    @Override
    @Transactional
    public void addDiaryComment(Account account, AddDiaryCommentRequest request) {
        Profile profile = findProfileById(request.profileId());
        Diary diary = findDiaryById(request.diaryId());

        diaryCommentRepository.save(DiaryComment.create(diary, profile, request.content()));
    }

    @Override
    @Transactional
    public void editDiaryComment(Account account, Long commentId, EditDiaryCommentRequest request) {
        Profile profile = findProfileById(request.profileId());
        DiaryComment diaryComment = findDiaryCommentById(commentId);

        authorizationService.validateDiaryCommentAccess(diaryComment, profile);

        diaryComment.updateContent(request.content());
    }

    @Override
    @Transactional
    public void removeDiaryComment(Account account, Long commentId,
        RemoveDiaryCommentRequest request) {
        Profile profile = findProfileById(request.profileId());
        DiaryComment diaryComment = findDiaryCommentById(commentId);

        // 상위 댓글 먼저 삭제될 시 하위 댓글 어떻게 처리할지 고민
        // 1. 상위 댓글을 "삭제된 댓글입니다"로 보여주고 하위 댓글 노출 유지
        // 2. 상위 댓글과 함께 하위 댓글 삭제

        authorizationService.validateDiaryCommentAccess(diaryComment, profile);

        diaryComment.delete();
    }

    private Profile findProfileById(Long id) {
        return profileRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Profile not found with id: " + id));
    }

    private Diary findDiaryById(Long id) {
        return diaryRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Diary not found with id: " + id));
    }

    private DiaryComment findDiaryCommentById(Long id) {
        return diaryCommentRepository.findById(id)
            .orElseThrow(
                () -> new NoSuchElementException("Diary Comment not found with id: " + id));
    }
}
