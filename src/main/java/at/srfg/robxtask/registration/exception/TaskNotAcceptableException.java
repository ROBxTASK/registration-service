package at.srfg.robxtask.registration.exception;

public class TaskNotAcceptableException extends RuntimeException {
    public TaskNotAcceptableException(String e) {
        super("task not in acceptable form");
    }
}
