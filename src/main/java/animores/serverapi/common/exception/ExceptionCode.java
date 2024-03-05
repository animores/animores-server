package animores.serverapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCode {
    // TO DO related error
    ILLEGAL_PET_IDS(400, "error_code","잘못된 펫이 입력되었습니다."),


    EXAMPLE(400, "error_code","Example");
    private final int httpsCode;
    private final String errorCode;
    private final String message;
}
