package core;

public class DataException extends Exception {
    private String message;

    public DataException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
