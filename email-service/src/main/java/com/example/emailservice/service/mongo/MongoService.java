package com.example.emailservice.service.mongo;

import com.example.emailservice.jacoco.ExcludeFromJacocoGeneratedReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


@Service
public class MongoService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @ExcludeFromJacocoGeneratedReport
    public void clearDatabase() {
        mongoTemplate.getDb().listCollectionNames().forEach(collectionName -> {
            mongoTemplate.getDb().getCollection(collectionName).drop();
        });
    }
}
