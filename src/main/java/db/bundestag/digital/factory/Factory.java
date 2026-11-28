package db.bundestag.digital.factory;

import com.mongodb.client.model.*;
import db.bundestag.digital.data.Abgeordnete;
import db.bundestag.digital.data.Rede;
import db.bundestag.digital.database.AbgeordneteMongo;
import db.bundestag.digital.database.MongoDBConnector;
import db.bundestag.digital.database.RedeMongo;
import db.bundestag.digital.exceptions.DataExportException;
import db.bundestag.digital.exceptions.DataImportException;
import db.bundestag.digital.file.AbgeordneteFile;
import db.bundestag.digital.file.FileReader;
import db.bundestag.digital.htmlexport.HtmlExporter;
import org.bson.Document;
import org.bson.conversions.Bson;


import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Factory {

    private final MongoDBConnector connector;
    private final HtmlExporter exporter;

    public Factory(MongoDBConnector connector, HtmlExporter exporter){
        this.connector = connector;
        this.exporter = exporter;
    };

    public void importData (String filePath) throws DataImportException {
        int totalSpeeches = 0;
        int countSpeech = 0;
        int countDeputy = 0;
        try {
            List<AbgeordneteFile> deputies = FileReader.loadDocument(filePath);

            for (AbgeordneteFile deputy : deputies) {
                Document newAbgeordnete = AbgeordneteMongo.toDocument(deputy);
                System.out.println("giris");
                boolean insertedDeputy = connector.insertIfAbsent("abgeordneten", newAbgeordnete);
                System.out.println("cikis");
                if (insertedDeputy) {
                    countDeputy += 1;
                }
                for (Rede speech : deputy.getSpeeches()) {
                    totalSpeeches += 1;
                    Document newSpeech = RedeMongo.toDocument(speech);
                    boolean insertedRede = connector.insertIfAbsent("reden", newSpeech);
                    if (insertedRede) {
                        countSpeech += 1;
                    }
                }

            }
            System.out.printf("Abgeordnete: %d read, %d added, %d already exist. \n", deputies.size(), countDeputy, (deputies.size() - countDeputy));
            System.out.printf("Rede: %d read, %d added, %d already exist. \n ", totalSpeeches, countSpeech, (totalSpeeches - countSpeech));
        }
        catch (IOException e){
            throw new DataImportException("The file with this path could not be imported", e);
        }


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


    public Map<Abgeordnete, Integer> countAllSpeechesByDeputy(){
        Map<Abgeordnete, Integer> filterResults = new LinkedHashMap<>();
        List<Bson> pipeline = List.of(Aggregates.group("$speaker_id", Accumulators.sum("numberOfSpeech", 1)), Aggregates.sort(Sorts.descending("numberOfSpeech"))); // i did it for just self learning to know how aggregate methods working.
        List<Document> results = connector.aggregate("reden", pipeline);
        for (Document result: results){
            var speaker = AbgeordneteMongo.toMongo(result);
            int numberOfSpeech = result.getInteger("numberOfSpeech");
            filterResults.put(speaker, numberOfSpeech);
        }
        return filterResults;
    }

    public void exportData(String path) throws DataExportException {
        Map<String, Map<Abgeordnete, List<Rede>>> groups = new LinkedHashMap<>();
        for (String party : getAllPartys()){
            List<Bson> pipeline = List.of(
                    Aggregates.match(Filters.eq("party", party)),
                    Aggregates.lookup("reden", "_id", "speaker_id", "speeches"),
                    Aggregates.addFields(new Field<>(
                            "speechCount", new Document("$size", "$speeches"))),
                    Aggregates.sort(Sorts.orderBy(
                            Sorts.descending("speechCount"),
                            Sorts.ascending("lastname", "_id")
                    ))
            );
            Map<Abgeordnete, List<Rede>> cards = new LinkedHashMap<>();

            for (Document deputy : connector.aggregate("abgeordneten", pipeline)){
                List<Rede> speeches = new ArrayList<>();
                for (Document speech : deputy.getList("speeches", Document.class)){
                    speeches.add(RedeMongo.toMongo(speech));

                }
                cards.put(AbgeordneteMongo.toMongo(deputy), speeches);

            }
            groups.put(party, cards);
        }
        try {
            exporter.exportHtml(groups, Path.of(path));
        } catch (IOException e) {
            throw new DataExportException("Data's in this path could not be exported", e);
        }

    }


    private List<String> getAllPartys() {
        List<String> partyLists = new ArrayList<>();
        List<Bson> pipeline = List.of(Aggregates.group("$party"));
        List<Document> partys = connector.aggregate("abgeordneten", pipeline);
        for (Document party : partys) {
            partyLists.add(party.getString("_id"));

        }
        return partyLists;

    }
}
