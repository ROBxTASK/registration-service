package at.srfg.robxtask.registration.exception;

public class InvalidApiKeyException extends RuntimeException {
    public InvalidApiKeyException(String id) {
        super("Device not found: " + id);
    }
}
