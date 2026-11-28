package db.bundestag.digital.database;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import db.bundestag.digital.exceptions.DatabaseException;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MongoDBConnector implements AutoCloseable{
    private final MongoClient client;
    private final MongoDatabase database;

    public MongoDBConnector(Path configFile) throws DatabaseException {
        Properties config = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(configFile)){
            config.load(reader);
        }
        catch (IOException e){
            throw new DatabaseException("Configuration file could not be read.", e);
        }
        this.client = MongoClients.create(config.getProperty("mongo.uri"));
        this.database = this.client.getDatabase(config.getProperty("mongo.database"));
    }

    public boolean insertIfAbsent(String collectionName, Document document){
        try{
            database.getCollection(collectionName).insertOne(document);
            return true;
        }
        catch (MongoWriteException e){
            if (e.getError().getCategory() == ErrorCategory.DUPLICATE_KEY){ // to checking if error caused by double entered id.
                return false;
            }
            throw e;
        }
    }

    public List<Document> find(String collectionName, Bson filter){
        List<Document> result = new ArrayList<>();
         var filterResults = database.getCollection(collectionName).find(filter);
         for (Document filterResult : filterResults){
            result.add(filterResult);

         }
         return result;

    }

    public long countDocuments(String collectionName, Bson filter){
        long size = database.getCollection(collectionName).countDocuments(filter);
        return size;

    }

    public List<Document> aggregate(String collectionName, List<Bson> pipeline){
        List<Document> result = new ArrayList<>();
        var filterResults = database.getCollection(collectionName).aggregate(pipeline);
        for (Document filterResult : filterResults){
            result.add(filterResult);
        }
        return result;
    }

    public Document ping(){
        return database.runCommand(new Document("ping",1));
    }



    @Override
    public void close(){
        client.close();
    }
}
