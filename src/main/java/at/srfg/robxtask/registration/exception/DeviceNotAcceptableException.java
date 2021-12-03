package at.srfg.robxtask.registration.exception;

public class DeviceNotAcceptableException extends RuntimeException {
    public DeviceNotAcceptableException(String e) {
        super("device not in acceptable form");
    }
}
