package animores.serverapi.common.exception;

public class CustomException extends RuntimeException {

    private final ExceptionCode code;

    public CustomException(ExceptionCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public ExceptionCode getCode() {
        return code;
    }
}
