package com.jfund.currencyvaluesservice.testutils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
class DatabaseManager {

    private MongoTemplate mongoTemplate;

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void dropCollection(String collectionName){
        mongoTemplate.getCollection(collectionName).drop();
    }
}
