package com.sergio.socialnetwork.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Collections;

@Configuration
@EnableMongoRepositories(basePackages = "com.sergio.socialnetwork.repositories")
public class MongoClientConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "socialnetworkdb";
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        builder.credential(MongoCredential.createCredential("sergio", "socialnetworkdb", "ser".toCharArray()))
                .applyToClusterSettings(settings -> settings.hosts(Collections.singletonList(new ServerAddress("localhost", 27017))));
    }

}
