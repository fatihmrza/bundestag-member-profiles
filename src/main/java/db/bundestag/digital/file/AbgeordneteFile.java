package db.bundestag.digital.file;

import db.bundestag.digital.data.Abgeordnete;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;

public class AbgeordneteFile implements Abgeordnete {

    int id;
    String firstName;
    String lastName;
    String party;
    ArrayList<Integer> legislativePeriods = new ArrayList<>();
    String profession;
    LocalDate birthdate;
    String birthplace;
    String gender;
    LocalDate deathdate;
    String academicTitle;
    String salutionTitle;

    private AbgeordneteFile(int id, String firstName, String lastName, String party, ArrayList<Integer> legislativePeriods, String profession, LocalDate birthdate, String birthplace, String gender, LocalDate deathdate, String academicTitle, String salutionTitle){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.party = party;
        this.legislativePeriods = legislativePeriods;
        this.profession = profession;
        this.birthdate = birthdate;
        this.birthplace = birthplace;
        this.gender = gender;
        this.deathdate = deathdate;
        this.academicTitle = academicTitle;
        this.salutionTitle = salutionTitle;
    }

    @Override
    public int getId(){
        return this.id;
        }

    @Override
    public String getFirstName(){
        return this.firstName;
    }

    @Override
    public String getLastName(){
        return this.lastName;
    }

    @Override
    public String getParty(){
        return this.lastName;
    }

    @Override
    public ArrayList<Integer> getLegislativePeriods(){
        return this.legislativePeriods;
    }

    @Override
    public String getProfession(){
        return this.profession;
    }
    @Override
    public LocalDate getBirthdate(){
        return this.birthdate;
    }
    @Override
    public String getBirthplace(){
        return this.birthplace;
    }
    @Override
    public String getGender(){
        return this.gender;
    }
    @Override
    public LocalDate getDeathdate(){
        return this.deathdate;
    }

    @Override
    public String getAcademicTitle(){
        return this.academicTitle;
    }

    @Override
    public String getSalutionTitle() {
        return salutionTitle;
    }

    public AbgeordneteFile createAbgeordneteFile(int id, String firstName, String lastName, String party, ArrayList<Integer> legislativePeriods, String profession,
                                                 LocalDate birthdate, String birthplace, String gender, LocalDate deathdate, String academicTitle, String salutionTitle){
        return new AbgeordneteFile(id,firstName, lastName, party, legislativePeriods, profession,birthdate, birthplace, gender,deathdate, academicTitle, salutionTitle);
    }
}