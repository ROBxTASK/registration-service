package at.srfg.robxtask.registration.persistence;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
@Configuration
public class MongoConnector {

    @Bean
    @ConfigurationProperties(prefix = "robxtask.registration-service.mongodb")
    MongoConfig getMongoConfig() {
        return new MongoConfig();
    }

    @Bean
    public MongoClient getClient() {
        return MongoClients.create(getMongoConfig().getUrl());
    }

    @Bean
    public MongoDatabase getDatabase() {
        return getClient().getDatabase(getMongoConfig().getName());
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }

}
