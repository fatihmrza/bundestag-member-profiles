package db.bundestag.digital.menu;

import db.bundestag.digital.data.Abgeordnete;
import db.bundestag.digital.data.Rede;
import db.bundestag.digital.database.MongoDBConnector;
import db.bundestag.digital.exceptions.DataExportException;
import db.bundestag.digital.exceptions.InvalidInputException;
import db.bundestag.digital.factory.Factory;
import db.bundestag.digital.htmlexport.HtmlExport;
import db.bundestag.digital.htmlexport.HtmlExporter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private final Factory factory;

    private Menu(Factory factory){
        this.factory = factory;
    }

    private  List<Abgeordnete> findByParty(String party){
        return this.factory.findByParty(party);
    }

    private  List<Abgeordnete> findById(int id){
        return this.factory.findById(id);
    }

    private List<Rede> findBySpeech(int speakerId){
        return factory.findBySpeech(speakerId);
    }

    private List<Abgeordnete> findByAge(int minAge, int maxAge){
        return factory.findByAge(minAge, maxAge);
    }

    private long countDeptuiesByParty(String party){
        return factory.countDeputiesByParty(party);
    }

    private Map<Abgeordnete, Integer> countAllSpeechesByDeputy(int id){
        return factory.countAllSpeechesByDeputy();
    }

    public static void run() {
        try(MongoDBConnector connector = new MongoDBConnector(Path.of("mongodb.properties"))){
            HtmlExporter exporter = new HtmlExporter();
            Factory factory = new Factory(connector, exporter);

        } catch (IOException e) {
            System.err.println("Configuration file could not be read." + e.getMessage());
        }



        Scanner scanner = new Scanner(System.in);
        System.out.println(" **** Parliament Digital Member Management System **** ");
        while (true) {
            System.out.println("""
                    Please choose an option: \n
                    1- Import Data \n
                    2- Export Data \n
                    3- Other operations on imported Data \n
                    0 - quit
              
                    
                    """);
            String choice = scanner.nextLine();
            try{
                switch(choice){
                    case "1":
                        System.out.println("Please enter the pathfile: ");
                        String path = scanner.nextLine();
                        factory.importData();

                }
            }
        }
    }







}
