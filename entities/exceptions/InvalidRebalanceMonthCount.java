package entities.exceptions;

public class InvalidRebalanceMonthCount extends Exception {
    String errorCode;
    public InvalidRebalanceMonthCount (String message) {
        super();
        this.errorCode = "101";
    }

}
