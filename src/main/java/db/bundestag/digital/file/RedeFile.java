package db.bundestag.digital.file;
import db.bundestag.digital.data.Rede;

import java.time.LocalDate;




public class RedeFile implements Rede {
    String content;
    int speakerId;
    LocalDate date;
    String agendaItem;
    int legislativePeriod;
    String contentId;


    private RedeFile(String content, int speakerId, LocalDate date, String agendaItem, int legislativePeriod, String contentId){
        this.content = content;
        this.speakerId = speakerId;
        this.date = date;
        this.agendaItem = agendaItem;
        this.legislativePeriod = legislativePeriod;
        this.contentId = contentId;
    }

    public String getContent(){
        return this.content;
    }

    public int getSpeakerId(){
        return this.speakerId;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public String getAgendaItem(){
        return this.agendaItem;
    }

    public int getLegislativePeriod(){
        return this.legislativePeriod;
    }

    public String getContentId(){
        return this.contentId;
    }

    protected static RedeFile createRede(String content, int speakerId, LocalDate date, String agendaItem, int legislativePeriod, String contentId){
        return new RedeFile(content, speakerId, date, agendaItem, legislativePeriod, contentId);
    }
}
