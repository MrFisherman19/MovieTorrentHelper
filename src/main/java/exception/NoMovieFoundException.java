package exception;

public class NoMovieFoundException extends RuntimeException {

    public NoMovieFoundException() {
    }

    public NoMovieFoundException(String message) {
        super(message);
    }
}
