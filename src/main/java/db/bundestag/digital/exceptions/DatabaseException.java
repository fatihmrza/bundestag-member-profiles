package db.bundestag.digital.exceptions;

public class DatabaseException extends Exception {
    public DatabaseException(String message, Exception cause){
        super(message, cause);
    }
}
