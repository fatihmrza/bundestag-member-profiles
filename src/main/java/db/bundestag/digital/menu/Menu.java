package db.bundestag.digital.menu;

import db.bundestag.digital.data.Abgeordnete;
import db.bundestag.digital.data.Rede;
import db.bundestag.digital.database.MongoDBConnector;
import db.bundestag.digital.exceptions.DataExportException;
import db.bundestag.digital.exceptions.DataImportException;
import db.bundestag.digital.exceptions.DatabaseException;
import db.bundestag.digital.exceptions.InvalidInputException;
import db.bundestag.digital.factory.Factory;
import db.bundestag.digital.htmlexport.HtmlExport;
import db.bundestag.digital.htmlexport.HtmlExporter;

import java.awt.*;
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

    private  void findByParty(String party){
       List<Abgeordnete> deputies = this.factory.findByParty(party);
       if (deputies.isEmpty()){
           System.out.println("There is no deputy in this party");
           return;
       }
       for (Abgeordnete deputy : deputies){
           System.out.println(deputy.getFirstName() + " " + deputy.getLastName());
       }
    }

    private void findById(int id){
        List<Abgeordnete> deputies = this.factory.findById(id);
        if (deputies.isEmpty()){
            System.out.println("There is no deputy in this party");
            return;
        }
        for (Abgeordnete deputy : deputies){
            System.out.println(deputy.getFirstName() + " " + deputy.getLastName());
        }
    }

    private void findBySpeech(int speakerId){
        List<Rede> speeches = factory.findBySpeech(speakerId);
        if (speeches.isEmpty()){
            System.out.println("There is no speech for this speaker");
            return;
        }
        for (Rede speech : speeches){
            System.out.println(speech.getContent());
        }

    }

    private void findByAge(int minAge, int maxAge){
        List<Abgeordnete> deputies = factory.findByAge(minAge, maxAge);
        if (deputies.isEmpty()){
            System.out.println("There is no deputy with this id");
            return;
        }
        for (Abgeordnete deputy : deputies){
            System.out.println(deputy.getFirstName() + " " + deputy.getLastName());
        }
    }

    private void countDeputiesByParty(String party){
        System.out.println(factory.countDeputiesByParty(party));
    }

    private void countSpeechesByDeputy(int id){
        System.out.println(this.factory.countSpeechesByDeputy(id));
    }

    private void countAllSpeechesByDeputy(){
        Map<Abgeordnete, Integer> groups = factory.countAllSpeechesByDeputy();
        for (Map.Entry<Abgeordnete, Integer> group : groups.entrySet()){
            System.out.println(group.getKey().getFirstName() + " " + group.getKey().getLastName() + ": " + group.getValue().toString());
        }

    }

    public static void run() {
        try(MongoDBConnector connector = new MongoDBConnector(Path.of("mongodb.properties"))){
            HtmlExporter exporter = new HtmlExporter();
            Factory factory = new Factory(connector, exporter);
            Menu menu = new Menu(factory);

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
                            String filePath = scanner.nextLine();
                            if (filePath.isBlank()){
                                factory.importData("mdb_speeches.json");
                            }
                            factory.importData(filePath);
                            System.out.println("All datas are successfully imported.");
                            break;
                        case "2":
                            System.out.println("Please enter a filepath for exported html data: ");
                            String outputPath = scanner.nextLine();
                            if (outputPath.isBlank()){
                                factory.exportData("visitenkarten.html");
                            }
                            factory.exportData(outputPath);
                            System.out.println("All datas are successfully exported.");

                           try {
                               Desktop.getDesktop().browse(Path.of(outputPath).toUri());  // to open automatically exported data appearance.
                               break;
                           }
                           catch (IOException e) {
                               System.err.println("Html document saved but could not opened on browser. " + "Filepath: " + outputPath +e.getMessage());
                           }

                        case "3":
                            System.out.println("""
                                    1- List all Deputies by Party Name \n
                                    2- List all Deputies by Id \n
                                    3- List all Speeches by Deputies Id \n
                                    4- List all Deputies with Age Interval \n
                                    5- Number of all Deputies by Party Name \n
                                    6- Number of all Speeches of a Deputy \n
                                    7 - List of all Number of Speeches of Deputies \n
                                    0 - Return main menu \n
                                    
                                    """);
                            String choice1 = scanner.nextLine();
                            switch (choice1){
                                case "1":
                                    System.out.println("Enter a party name: ");
                                    String party = scanner.nextLine();
                                    menu.findByParty(party);
                                    break;
                                case "2":
                                    System.out.println("Enter an id: ");
                                    int id = Integer.parseInt(scanner.nextLine());
                                    menu.findById(id);
                                    break;
                                case "3":
                                    System.out.println("Enter a speaker id: ");
                                    int speakerId = Integer.parseInt(scanner.nextLine());
                                    menu.findBySpeech(speakerId);
                                    break;
                                case "4":
                                    System.out.println("Enter a minimum age: ");
                                    int minAge = Integer.parseInt(scanner.nextLine());
                                    System.out.println("Enter a max age");
                                    int maxAge = Integer.parseInt(scanner.nextLine());
                                    menu.findByAge(minAge, maxAge);
                                    break;
                                case "5":
                                    System.out.println("Enter a party name: ");
                                    String party2 = scanner.nextLine();
                                    menu.countDeputiesByParty(party2);
                                    break;
                                case "6":
                                    System.out.println("Enter an id: ");
                                    int id2 = Integer.parseInt(scanner.nextLine());
                                    menu.countSpeechesByDeputy(id2);
                                    break;
                                case "7":
                                    menu.countAllSpeechesByDeputy();
                                    break;
                                case "0":
                                    break;
                                default:
                                    throw new InvalidInputException("Invalid input.");
                            }
                            break;

                        case "0":
                            return;
                        default:
                            throw new InvalidInputException("Invalid input");
                    }
                }
                catch (InvalidInputException e){
                    System.err.println(e.getMessage());
                }
                catch (DataImportException e){
                    System.err.println(e.getMessage());
                }
                catch (DataExportException e){
                    System.err.println(e.getMessage());
                }
                catch (NumberFormatException e){
                    throw new InvalidInputException("Invalid input");
                }
            }

        } catch (DatabaseException e) {
            System.err.println(e.getMessage());

        }


        catch (InvalidInputException e){
            System.err.println(e.getMessage());
        }





    }







}
