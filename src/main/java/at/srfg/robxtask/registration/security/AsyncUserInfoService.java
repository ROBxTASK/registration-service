package at.srfg.robxtask.registration.security;

public interface AsyncUserInfoService {
    UserInfo resolve() throws InterruptedException;
}
