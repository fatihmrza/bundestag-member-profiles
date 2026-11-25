package db.bundestag.digital.database;

import db.bundestag.digital.data.Abgeordnete;
import db.bundestag.digital.file.AbgeordneteFile;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;

public class AbgeordneteMongo implements Abgeordnete {

    private final Document document;

    private AbgeordneteMongo(Document document){
        this.document = document;
    }

    public int getId(){
        return document.getInteger("id");
    }

    public String getFirstName(){
        return document.getString("firstname");

    }
    public String getLastName(){
        return document.getString("lastname");

    }
    public String getParty(){
        return document.getString("party");
    }

    public ArrayList<Integer> getLegislativePeriods(){
        return new ArrayList<>(document.getList("legislative_periods", Integer.class));
    }

    public String getProfession(){
        return document.getString("profession");
    }

    public LocalDate getBirthdate(){
        return LocalDate.parse(document.getString("birthdate"));
    }

    public String getBirthplace(){
        return document.getString("birthplace");
    }

    public String getGender(){
        return document.getString("gender");
    }

    public LocalDate getDeathdate(){
        return LocalDate.parse(document.getString("deathdate"));
    }

    public String getAcademicTitle(){
        return document.getString("academic_title");
    }

    public String getSalutionTitle(){
        return document.getString("salution_title");
    }

    public Document toDocument(AbgeordneteFile file){
        int id = file.getId();
        String firstName = file.getFirstName();
        String lastName = file.getLastName();
        String party = file.getParty();
        String profession = file.getProfession();
        LocalDate birthdate = file.getBirthdate();
        String birthplace = file.getBirthplace();
        String gender = file.getGender();
        LocalDate deathdate = file.getDeathdate();
        String academicTitle = file.getAcademicTitle();
        String salutionTitle = file.getSalutionTitle();


        return new Document().append("id", id)
                .append("firstname", firstName)
                .append("lastname", lastName)
                .append("party", party)
                .append("profession", profession)
                .append("birthdate", birthdate)
                .append("birthplace", birthplace)
                .append("gender", gender)
                .append("deathdate", deathdate)
                .append("academic_title", academicTitle)
                .append("salution_title", salutionTitle);
    }


}