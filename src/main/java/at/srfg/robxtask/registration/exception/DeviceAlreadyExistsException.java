package at.srfg.robxtask.registration.exception;

public class DeviceAlreadyExistsException extends RuntimeException {
    public DeviceAlreadyExistsException(String deviceID) {
        super("Device already exists: " + deviceID);
    }
}
