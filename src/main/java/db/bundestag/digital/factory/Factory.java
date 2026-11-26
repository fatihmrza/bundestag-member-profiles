package db.bundestag.digital.factory;

import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import db.bundestag.digital.data.Abgeordnete;
import db.bundestag.digital.data.Rede;
import db.bundestag.digital.database.AbgeordneteMongo;
import db.bundestag.digital.database.MongoDBConnector;
import db.bundestag.digital.database.RedeMongo;
import db.bundestag.digital.file.AbgeordneteFile;
import db.bundestag.digital.file.FileReader;
import org.bson.Document;
import org.bson.conversions.Bson;


import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Factory {

    private final MongoDBConnector connector;

    public Factory(MongoDBConnector connector){
        this.connector = connector;
    };

    public void importData (String filePath) throws IOException {
        int totalSpeeches = 0;
        int countSpeech = 0;
        int countDeputy = 0;
        List<AbgeordneteFile> deputies = FileReader.loadDocument(filePath);
        for (AbgeordneteFile deputy : deputies){
            Document newAbgeordnete = AbgeordneteMongo.toDocument(deputy);
            boolean insertedDeputy = connector.insertIfAbsent("abgeordneten", newAbgeordnete);
            if (insertedDeputy){
                countDeputy += 1;
            }
            for (Rede speech : deputy.getSpeeches()){
                totalSpeeches += 1;
                Document newSpeech = RedeMongo.toDocument(speech);
                boolean insertedRede = connector.insertIfAbsent("reden", newSpeech);
                if (insertedRede){
                    countSpeech += 1;
                }
            }

        }
        System.out.printf("Abgeordnete: %d read, %d added, %d already exist. \n", deputies.size(), countDeputy, (deputies.size()-countDeputy));
        System.out.printf("Rede: %d read, %d added, %d already exist. \n ", totalSpeeches, countSpeech, (totalSpeeches - countSpeech));


    }

    public List<Abgeordnete> findById(int id ){
        List<Abgeordnete> filterResults = new ArrayList<>();
        Bson filter = Filters.eq("_id", id);
        List<Document> results = connector.find("abgeordneten", filter);
        for (Document result: results){
            filterResults.add(AbgeordneteMongo.toMongo(result));
        }
        return filterResults;
    }

    public List<Abgeordnete> findByParty(String party){
        List<Abgeordnete> filterResults = new ArrayList<>();
        Bson filter = Filters.eq("party",party);
        List<Document> results = connector.find("abgeordneten",filter);
        for (Document result : results){
            filterResults.add(AbgeordneteMongo.toMongo(result));
        }
        return filterResults;
    }

    public List<Rede> findBySpeech(int speakerId){
        List<Rede> filterResults = new ArrayList<>();
        Bson filter = Filters.eq("speaker_id", speakerId);
        List<Document> results = connector.find("reden", filter);
        for (Document result : results){
            filterResults.add(RedeMongo.toMongo(result));
        }
        return filterResults;

    }

    public List<Abgeordnete> findByAge(int minAge, int maxAge){
        List<Abgeordnete> filterResults = new ArrayList<>();
        Bson olderThan = Filters.gt("age", minAge);
        Bson youngerThan = Filters.lt("age", maxAge);
        Bson filter = Filters.and(olderThan, youngerThan);
        List<Document> results = connector.find("abgeordnete", filter);
        for (Document result : results){
            filterResults.add(AbgeordneteMongo.toMongo(result));
        }
        return filterResults;
    }


    public long countDeputiesByParty(String party){
        Bson filter = Filters.eq("party", party);
        return connector.countDocuments("abgeordneten", filter);

    }

    public long countSpeechesByDeputy(int id){
        Bson filter = Filters.eq("speake_id", id);
        return connector.countDocuments("reden", filter);
    }


    public Map<Integer, Integer> countAllSpeechesByDeputy(){
        Map<Integer, Integer> filterResults = new LinkedHashMap<>();
        List<Bson> pipeline = List.of(Aggregates.group("$speaker_id", Accumulators.sum("numberOfSpeech", 1)), Aggregates.sort(Sorts.descending("numberOfSpeech"))); // i did it for just self learning to know how aggregate methods working.
        List<Document> results = connector.aggregate("reden", pipeline);
        for (Document result: results){
            int speakerId = result.getInteger("_id");
            int numberOfSpeech = result.getInteger("numberOfSpeech");
            filterResults.put(speakerId, numberOfSpeech);
        }
        return filterResults;
    }

    public void exportData(String Path){
        Map<Abgeordnete, List<Rede>> abgeordneteRedeMap = new LinkedHashMap<>();
        List<Document> allDeputies = connector.find("abgeordneten", null);
        for (Document deputy : allDeputies){
            Abgeordnete abgeordnete = AbgeordneteMongo.toMongo(deputy);
            List<Rede> listOfSpeeches = findBySpeech(deputy.getInteger("_id"));
            abgeordneteRedeMap.put(abgeordnete, listOfSpeeches);

        }





    }


    private List<String> getAllPartys(){
        List<String> partyLists = new ArrayList<>();
        List<Bson> pipeline = List.of(Aggregates.group("$party"));
        List<Document> partys = connector.aggregate("abgeordneten", pipeline);
        for (Document party : partys){
            partyLists.add(party.getString("_id"));

        return partyLists;
        }

    }
}
