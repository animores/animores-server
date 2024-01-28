package animores.serverapi.to_do.exception;

public class ToDoException extends RuntimeException {

        private final ToDoExceptionCode code;

        public ToDoException(ToDoExceptionCode code) {
            super(code.getMessage());
            this.code = code;
        }

        public ToDoExceptionCode getCode() {
            return code;
        }
}
