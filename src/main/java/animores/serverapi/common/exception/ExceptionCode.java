package animores.serverapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCode {

    //계정 관련 에러
    INVALID_REFRESH_TOKEN("", "해당 refresh token 을 찾을 수 없습니다"),
    INVALID_USER("", "해당 유저를 찾을 수 없습니다"),
    EXPIRED_AUTH_CODE("", "인증 코드가 만료되었습니다."),
    NOT_VERIFIED_EMAIL("", "이메일 인증이 되지 않았습니다."),
    PASSWORD_MISMATCH("", "비밀번호가 일치하지 않습니다."),
    PASSWORD_MATCH_BEFORE("", "기존 패스워드와 같습니다."),
    INVALID_PROFILE("", "해당 프로필을 찾을 수 없습니다."),
    UNAUTHORIZED_PROFILE_ACCESS("", "접근 권한이 없는 프로필입니다."),
    MAX_PROFILE_COUNT("", "프로필은 최대 6개까지 등록할 수 있습니다."),
    DUPLICATED_NICKNAME("", "중복된 닉네임 입니다."),

    // 이미지 관련 에러
    INVALID_IMAGE_TYPE("", "지원하지 않는 이미지 형식입니다."),

    //   to_do 관련 에러
    ILLEGAL_PET_IDS("error_code", "잘못된 펫이 입력되었습니다."),
    NOT_FOUND_TO_DO("", "해당 To Do를 찾을 수 없습니다."),
    INAPPROPRIATE_PROFILE_ACCESS("", "작성자 Profile 만 수정할 수 있습니다."),

    // 일지
    UNSUPPORTED_TYPE("", "지원하지 않는 파일 형식입니다."),
    NOT_FOUND_DIARY_MEDIA("", "해당 미디어를 찾을 수 없습니다."),
    UNAUTHORIZED_DIARY_ACCESS("", "접근 권한이 없는 다이어리입니다."),

    // 일지 관심
    NOT_FOUND_DIARY_LIKE("", "해당 Diary Like 를 찾을 수 없습니다."),

    // 일지 댓글
    NOT_FOUND_DIARY_COMMENT("", "해당 댓글을 찾을 수 없습니다."),
    UNAUTHORIZED_DIARY_COMMENT_ACCESS("", "접근 권한이 없는 댓글입니다."),

    // 일지 대댓글
    NOT_FOUND_DIARY_REPLY("", "해당 대댓글을 찾을 수 없습니다."),
    UNAUTHORIZED_DIARY_REPLY_ACCESS("", "접근 권한이 없는 대댓글입니다."),

    // 500 에러
    UNHANDLED_EXCEPTION("error_code", "알 수 없는 에러가 발생했습니다."),
    ;

    private final String errorCode;
    private final String message;
}
