package db.bundestag.digital.data;

import java.time.LocalDate;
import java.util.ArrayList;

public interface Rede {

    String getContent();

    int getSpeakerId();

    LocalDate getDate();

    String getAgendaItem();

    int getLegislativePeriod();

    int getContentId();

}
