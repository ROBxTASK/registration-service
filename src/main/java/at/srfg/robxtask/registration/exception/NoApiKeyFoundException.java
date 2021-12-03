package at.srfg.robxtask.registration.exception;

import javax.servlet.ServletException;

public class NoApiKeyFoundException extends ServletException {
    public NoApiKeyFoundException() {
        super();
    }

    public NoApiKeyFoundException(String message) {
        super(message);
    }
}
