package animores.serverapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCode {
    // TO DO related error
    ILLEGAL_PET_IDS( "error_code","잘못된 펫이 입력되었습니다."),

//   to_do 관련 에러
    NOT_FOUND_TO_DO("", "해당 To Do를 찾을 수 없습니다."),
    INAPPROPRIATE_PROFILE_ACCESS("","작성자 Profile 만 수정할 수 있습니다." ),
    UNHANDLED_EXCEPTION("error_code","알 수 없는 에러가 발생했습니다.");

    private final String errorCode;
    private final String message;
}
