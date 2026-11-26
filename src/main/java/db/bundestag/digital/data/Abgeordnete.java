package db.bundestag.digital.data;

import java.time.LocalDate;
import java.util.List;

public interface Abgeordnete {


    int getId();

    String getFirstName();

    String getLastName();

    String getParty();

    List<Integer> getLegislativePeriods();

    String getProfession();

    LocalDate getBirthdate();

    String getBirthplace();

    String getGender();

    LocalDate getDeathdate();

    String getAcademicTitle();

    String getSalutionTitle();

}
