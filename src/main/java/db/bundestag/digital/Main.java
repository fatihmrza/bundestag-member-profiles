package db.bundestag.digital;

import db.bundestag.digital.data.Abgeordnete;
import db.bundestag.digital.data.Rede;
import db.bundestag.digital.database.AbgeordneteMongo;
import db.bundestag.digital.database.MongoDBConnector;
import db.bundestag.digital.database.RedeMongo;
import db.bundestag.digital.factory.Factory;
import db.bundestag.digital.file.AbgeordneteFile;
import db.bundestag.digital.file.RedeFile;
import db.bundestag.digital.htmlexport.HtmlExport;
import db.bundestag.digital.htmlexport.HtmlExporter;
import org.bson.Document;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        ArrayList<Integer> leg = new ArrayList<>();
        List<Rede> rede = new ArrayList<>();
        leg.add(2);
        leg.add(3);
        LocalDate date = LocalDate.of(2026, 10, 12);
        Rede redeFile = RedeFile.createRede("Selamlar nasilsin", 10, date, "tamam", 3, "3-0");
        rede.add(redeFile);
        Abgeordnete abgeordnete = AbgeordneteFile.createAbgeordneteFile(10, "fatih", "mirza", "cdu", leg, "student", date, "istanbul", "male", null, "", "Herr", rede);
        Document document = AbgeordneteMongo.toDocument(abgeordnete);
        Document document1 = RedeMongo.toDocument(redeFile);

        try {
            MongoDBConnector connector = new MongoDBConnector(Path.of("mongodb.properties"));
            HtmlExporter exporter = new HtmlExporter();
            Factory factory = new Factory(connector, exporter);
            connector.insertIfAbsent("abgeordneten", document);
            factory.exportData("visitenkartes.html");
            connector.insertIfAbsent("reden", document1);
        } catch (IOException e) {
            System.err.println("MongoDb okunmadi");

        }
    }
}
