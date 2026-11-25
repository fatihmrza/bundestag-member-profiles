package db.bundestag.digital.database;

import db.bundestag.digital.data.Abgeordnete;
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
}
