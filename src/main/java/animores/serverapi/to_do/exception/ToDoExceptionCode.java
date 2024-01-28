package animores.serverapi.to_do.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ToDoExceptionCode {

    EXAMPLE(400, "Example");
    private final int statusCode;
    private final String message;
}
