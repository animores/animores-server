package animores.serverapi.common.exception;

import animores.serverapi.common.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public Response<Void> handleCustomException(CustomException e) {
        return Response.error(e.getCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Void> handleValidationException(MethodArgumentNotValidException e) {
        return Response.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Response<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return Response.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("unhandled exception", e);
        return ResponseEntity.internalServerError().body("unhandled exception");
    }
}
