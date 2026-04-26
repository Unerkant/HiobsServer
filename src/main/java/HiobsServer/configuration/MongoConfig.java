package HiobsServer.configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

/**
 * Den 30.12.2025
 */

/**
 * ACHTUNG: für den Automatischen erstellung von Datenbank in Mongo,
 * ansonsten wird von spring boot einer 'test' Datenbank erstellt
 */
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "hiobspost"; // Hier wird der Name hart gesetzt
    }

    @Override
    public MongoClient mongoClient() {
        // Nutzt Ihre URI aus den Properties oder einen Standard
        return MongoClients.create("mongodb://localhost:27017");
    }
}

