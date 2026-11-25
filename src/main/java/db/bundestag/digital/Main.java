package db.bundestag.digital;

import db.bundestag.digital.database.MongoDBConnector;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args){
        try{
            MongoDBConnector connector = new MongoDBConnector(Path.of("mongodb.properties"));
            System.out.println(connector.ping().toJson());


        }
        catch (IOException e){
            System.err.println("MongoDb okunmadi");
        }

    }
}
