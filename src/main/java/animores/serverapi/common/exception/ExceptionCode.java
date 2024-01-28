package animores.serverapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionCode {

    EXAMPLE(400, "Example");
    private final int statusCode;
    private final String message;
}
