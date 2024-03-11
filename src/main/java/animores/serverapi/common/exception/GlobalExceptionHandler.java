package animores.serverapi.common.exception;

import animores.serverapi.common.Response;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public Response<Void> handleValidationException(ValidationException e) {
        log.error("validation exception", e);
        return Response.error(e.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    public Response<Void> handleCustomException(CustomException e) {
        return Response.error(e.getCode());
    }

    @ExceptionHandler(RuntimeException.class)
    public Response<Void> handleRuntimeException(RuntimeException e) {
        log.error("unhandled exception", e);
        return Response.error(ExceptionCode.UNHANDLED_EXCEPTION);
    }
}
