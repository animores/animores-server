package animores.serverapi.common.exception;

import animores.serverapi.common.Response;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.validation.ConstraintViolationException;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Response<Void>> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.badRequest().body(Response.error(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Void>> handleValidationException(
        MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
            .body(Response.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Response<Void>> handleConstraintViolationException(
        ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(Response.error(e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<Void>> handleIllegalArgumentException(
        IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Response.error(e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response<Void>> handleSQLIntegrityConstraintViolationException(
        SQLIntegrityConstraintViolationException e) {
        return ResponseEntity.badRequest().body(Response.error("중복된 값이 존재합니다."));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Response<Void>> handleJwtException(
            JwtException e) {
        return ResponseEntity.status(401).body(Response.error(e.getMessage()));
    }
}
