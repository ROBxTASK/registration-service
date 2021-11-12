package at.srfg.robxtask.persistence;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
@ConfigurationProperties(prefix = "robxtask.registration-service.mongodb")
public class MongoConfig {

    private String url;
    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

