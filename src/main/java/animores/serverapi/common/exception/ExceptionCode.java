package animores.serverapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCode {
    // TO DO related error
    ILLEGAL_PET_IDS( "error_code","잘못된 펫이 입력되었습니다."),
    UNHANDLED_EXCEPTION("error_code","알 수 없는 에러가 발생했습니다.");

    private final String errorCode;
    private final String message;
}
