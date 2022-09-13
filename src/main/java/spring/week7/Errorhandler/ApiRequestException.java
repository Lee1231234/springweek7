package spring.week7.Errorhandler;


public class ApiRequestException extends IllegalArgumentException{
    public ApiRequestException(String message) {
        super(message);
    }

}