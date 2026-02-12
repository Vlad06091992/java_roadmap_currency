package currency.app.Exceptions;

public class DatabaseException extends RuntimeException {
    public DatabaseException() {
        super("Database request error");
    }
}
