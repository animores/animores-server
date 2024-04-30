package animores.serverapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCode {

    INVALID_REFRESH_TOKEN("","해당 refresh token 을 찾을 수 없습니다" ),
    INVALID_USER("","해당 유저를 찾을 수 없습니다" ),
    EXPIRED_AUTH_CODE("", "인증 코드가 만료되었습니다."),
    //   to_do 관련 에러
    ILLEGAL_PET_IDS( "error_code","잘못된 펫이 입력되었습니다."),
    NOT_FOUND_TO_DO("", "해당 To Do를 찾을 수 없습니다."),
    INAPPROPRIATE_PROFILE_ACCESS("","작성자 Profile 만 수정할 수 있습니다." ),

    // 일지
    UNSUPPORTED_TYPE("", "지원하지 않는 파일 형식입니다."),

    // 500 에러
    UNHANDLED_EXCEPTION("error_code","알 수 없는 에러가 발생했습니다.");

    private final String errorCode;
    private final String message;
}
