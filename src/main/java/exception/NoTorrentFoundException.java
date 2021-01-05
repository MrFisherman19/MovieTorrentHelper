package exception;

public class NoTorrentFoundException extends RuntimeException {

    public NoTorrentFoundException() { }

    public NoTorrentFoundException(String message) {
        super(message);
    }
}