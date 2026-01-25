package currency.app.Exceptions;

public class NotValidDataException extends RuntimeException {
    public NotValidDataException(String message) {
        super(message);
    }
}
