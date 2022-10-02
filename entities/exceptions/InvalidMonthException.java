package entities.exceptions;

public class InvalidMonthException extends Exception{
    String errorCode;
    public InvalidMonthException (String message) {
        super();
        this.errorCode = "100";
    }
}
