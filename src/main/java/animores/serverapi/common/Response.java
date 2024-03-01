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
    private final ErrorResponse error;

    private Response(boolean success, T data, ExceptionCode exceptionCode) {
        this.success = success;
        this.data = data;
        this.error = exceptionCode == null ?
                null : new ErrorResponse(exceptionCode.name(), exceptionCode.getMessage());
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(true, data, null);
    }

    public static Response<Void> error(ExceptionCode exceptionCode) {
        return new Response<>(false, null, exceptionCode);
    }

    private record ErrorResponse(String code, String message) {
    }
}
