package animores.serverapi.common.exception;

import animores.serverapi.to_do.exception.ToDoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @ExceptionHandler(ToDoException.class)
    public ResponseEntity<String> handleToDoException(ToDoException e) {
        return ResponseEntity.status(e.getCode().getStatusCode()).body(e.getCode().getMessage());
    }
}
