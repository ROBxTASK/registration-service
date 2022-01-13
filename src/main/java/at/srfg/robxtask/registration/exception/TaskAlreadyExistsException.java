package at.srfg.robxtask.registration.exception;

public class TaskAlreadyExistsException extends RuntimeException {
    public TaskAlreadyExistsException(String taskID) {
        super("Task already exists: " + taskID);
    }
}
