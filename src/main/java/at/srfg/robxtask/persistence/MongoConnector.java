package at.srfg.robxtask.persistence;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class MongoConnector {

    @Autowired
    private MongoConfig config;

    @Autowired
    private MongoClient client;

    @Autowired
    public MongoDatabase db;

    @Bean
    public MongoClient getClient() {
        return MongoClients.create(config.getUrl());
    }

    @Bean
    public MongoDatabase getDatabase() {
        return client.getDatabase(config.getName());
    }

    public MongoCollection<Document> getCollection(String collectionName) {
        MongoCollection<Document> documentCollection = db.getCollection(collectionName);
        return documentCollection;
    }

}
