package db.bundestag.digital.exceptions;

public class DataImportException extends Exception{
    public DataImportException(String message, Exception cause){
        super(message, cause);
    }
}
