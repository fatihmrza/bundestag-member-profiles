package db.bundestag.digital.file;
import db.bundestag.digital.data.Rede;

import java.time.LocalDate;




public class RedeFile implements Rede {
    private final String content;
    private final int speakerId;
    private final LocalDate date;
    private final String agendaItem;
    private final int legislativePeriod;
    private final String contentId;


    private RedeFile(String content, int speakerId, LocalDate date, String agendaItem, int legislativePeriod, String contentId){
        this.content = content;
        this.speakerId = speakerId;
        this.date = date;
        this.agendaItem = agendaItem;
        this.legislativePeriod = legislativePeriod;
        this.contentId = contentId;
    }
    @Override
    public String getContent(){
        return this.content;
    }
    @Override
    public int getSpeakerId(){
        return this.speakerId;
    }
    @Override
    public LocalDate getDate(){
        return this.date;
    }
    @Override
    public String getAgendaItem(){
        return this.agendaItem;
    }
    @Override
    public int getLegislativePeriod(){
        return this.legislativePeriod;
    }
    @Override
    public String getContentId(){
        return this.contentId;
    }
    @Override
    public String toString(){
        return ("content: " + this.content + " " +"speaker id: " + this.speakerId + " " + "date: " + this.date + " " + "agenda item: " + this.agendaItem + " " + "legislative period: " + this.legislativePeriod + " " + "contentId: " + this.contentId);
    }

    protected static RedeFile createRede(String content, int speakerId, LocalDate date, String agendaItem, int legislativePeriod, String contentId){
        return new RedeFile(content, speakerId, date, agendaItem, legislativePeriod, contentId);
    }
}
