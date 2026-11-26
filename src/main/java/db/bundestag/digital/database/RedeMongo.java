package db.bundestag.digital.database;
import db.bundestag.digital.data.Rede;
import db.bundestag.digital.file.RedeFile;
import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneOffset;

public class RedeMongo implements Rede{

    private final Document document;


    private RedeMongo(Document document){
        this.document = document;
    }

    @Override
    public String getContent(){
        return this.document.getString("ccontent");
    }

    @Override
    public int getSpeakerId(){
        return document.getInteger("speaker_id");
    }
    @Override
    public LocalDate getDate() {
        return LocalDate.ofInstant(
                document.getDate("date").toInstant(),
                ZoneOffset.UTC
        );
    }
    @Override
    public String getAgendaItem(){
        return this.document.getString("agenda_item");
    }

    @Override
    public int getLegislativePeriod(){
        return this.document.getInteger("legislative_period");
    }

    @Override
    public String getContentId(){
        return this.document.getString("_id");
    }


    public static Document toDocument(Rede file){
        String content = file.getContent();
        int speakerId = file.getSpeakerId();
        String agendaItem = file.getAgendaItem();
        LocalDate date = file.getDate();
        int legislativePeriod = file.getLegislativePeriod();
        String contentId = file.getContentId();

        return new Document("_id",contentId)
                    .append("content", content)
                .append("speaker_id", speakerId)
                .append("agenda_item", agendaItem)
                .append("date", date)
                .append("legislative_period", legislativePeriod);
    }

    public static RedeMongo toMongo(Document document){
        return new RedeMongo(document);
    }
}
