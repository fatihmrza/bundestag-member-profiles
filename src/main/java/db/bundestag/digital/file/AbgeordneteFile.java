package db.bundestag.digital.file;

import db.bundestag.digital.data.Abgeordnete;
import db.bundestag.digital.data.Rede;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AbgeordneteFile implements Abgeordnete {

    private final int id;
    private final String firstName;
    private final String lastName;
    private final String party;
    private final ArrayList<Integer> legislativePeriods;
    private final String profession;
    private final LocalDate birthdate;
    private final String birthplace;
    private final String gender;
    private final LocalDate deathdate;
    private final String academicTitle;
    private final String salutionTitle;
    private final List<Rede> speeches;

    private AbgeordneteFile(int id, String firstName, String lastName, String party, ArrayList<Integer> legislativePeriods, String profession, LocalDate birthdate, String birthplace, String gender, LocalDate deathdate, String academicTitle, String salutionTitle, List<Rede> speeches){
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
        this.speeches = speeches;
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
        return this.party;
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

    public List<Rede> getSpeeches() {
        return speeches;
    }

    @Override
    public String toString(){
        return ("id: " + this.id + " " + "name: " + this.firstName + " " + this.lastName + "party: "+ this.party + " " + "legislative periods: " + this.legislativePeriods.toString() + " " +
                "profession: " + this.profession + " " + "birthdate: " + this.birthdate + " " + "birthplace: " + this.birthplace + " " + "gender: " + this.gender + " " + "deathdate: " + this.deathdate + " " + "academic title: " + this.academicTitle + " " + "salution title: " + this.salutionTitle);
    }

    protected static AbgeordneteFile createAbgeordneteFile(int id, String firstName, String lastName, String party, ArrayList<Integer> legislativePeriods, String profession,
                                                           LocalDate birthdate, String birthplace, String gender, LocalDate deathdate, String academicTitle, String salutionTitle, List<Rede> speeches){
        return new AbgeordneteFile(id,firstName, lastName, party, legislativePeriods, profession,birthdate, birthplace, gender,deathdate, academicTitle, salutionTitle, speeches);
    }
}