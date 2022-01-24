package at.srfg.robxtask.registration.security;

public class DeviceConfig {
    String apiKeyName;

    public String getApiKeySecret() {
        return apiKeySecret;
    }

    public void setApiKeySecret(String apiKeySecret) {
        this.apiKeySecret = apiKeySecret;
    }

    String apiKeySecret;

    public String getApiKeyName() {
        return apiKeyName;
    }

    public void setApiKeyName(String apiKeyName) {
        this.apiKeyName = apiKeyName;
    }

    @Override
    public String toString() {
        return "DeviceConfig{" +
                "apiKeyName='" + apiKeyName + '\'' +
                ", apiKeySecret='" + apiKeySecret + '\'' +
                '}';
    }
}
