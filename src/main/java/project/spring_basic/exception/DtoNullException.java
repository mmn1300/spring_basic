package project.spring_basic.exception;

public class DtoNullException extends RuntimeException {
    public DtoNullException(String message) {
        super(message);
    }
}
