package at.srfg.robxtask.registration.exception;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(String id) {
        super("Device not found : " + id);
    }
}
