package animores.serverapi.common.exception;

import animores.serverapi.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Void> handleValidationException(MethodArgumentNotValidException e) {
        log.error("validation exception", e);
        return Response.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
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
