package animores.serverapi.common;

import animores.serverapi.common.exception.ExceptionCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;

@Getter
public class Response<T> {

    private final boolean success;
    @JsonInclude(Include.NON_NULL)
    private final T data;
    @JsonInclude(Include.NON_NULL)
    private final ErrorResponse error;

    private Response(boolean success, T data, ErrorResponse error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    private Response() {
        this.success = false;
        this.data = null;
        this.error = null;
    }

    private Response(boolean success, T data, ExceptionCode exceptionCode) {
        this.success = success;
        this.data = data;
        this.error = exceptionCode == null ?
            null : new ErrorResponse(exceptionCode.name(), exceptionCode.getMessage());
    }

    private Response(boolean success, String message) {
        this.success = success;
        this.data = null;
        this.error = new ErrorResponse(null, message);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(true, data, (ExceptionCode) null);
    }

    public static Response<Void> error(ExceptionCode exceptionCode) {
        return new Response<>(false, null, exceptionCode);
    }

    public static Response<Void> error(String message) {
        return new Response<>(false, message);
    }

    private record ErrorResponse(@JsonInclude(Include.NON_NULL) String code, String message) {

    }
}
