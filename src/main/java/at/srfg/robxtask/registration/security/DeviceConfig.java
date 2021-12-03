package at.srfg.robxtask.registration.security;

public class DeviceConfig {
    String apiKeyName;

    public String getApiKeyValue() {
        return apiKeyValue;
    }

    public void setApiKeyValue(String apiKeyValue) {
        this.apiKeyValue = apiKeyValue;
    }

    String apiKeyValue;

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
                ", apiKeyValue='" + apiKeyValue + '\'' +
                '}';
    }
}
