package db.bundestag.digital.file;
import db.bundestag.digital.data.Abgeordnete;
import org.bson.json.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.texttechnologylab.utilities.FileUtilities;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class FileReader {


    public FileReader() {
    }


    public void loadDocument(String filePath) throws IOException {
        File file = new File(filePath);
        JSONArray jsonArray = FileUtilities.readContentAsJSON(file);
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String firstName = jsonObject.getString("vorname");
            String lastName = jsonObject.getString("name");
            String party = jsonObject.getString("partei");
            ArrayList<Integer> legislativePeriods = periodsToArray(jsonObject.getJSONArray("perioden")); // using helping method to convert JSONArray into ArrayList.
            String profession = jsonObject.getString("beruf");
            LocalDate birthdate = LocalDate.parse(jsonObject.getString("geburtsdatum"));
            String birthplace = jsonObject.getString("geburtsort");
            String gender = jsonObject.getString("geschlecht");
            LocalDate deathdate = LocalDate.parse(jsonObject.getString("sterbedatum"));
            String academicTitle = jsonObject.getString("akademischer_titel");
            String salutionTitle = jsonObject.getString("anrede_titel");

            AbgeordneteFile newAbgeordnete = AbgeordneteFile.createAbgeordneteFile(id, firstName, lastName, party, legislativePeriods, profession, birthdate, birthplace, gender, deathdate, academicTitle, salutionTitle);

            JSONArray speechesJsonArray = jsonObject.getJSONArray("speeches");
            for (int k = 0; k < speechesJsonArray.length(); k++){
                JSONObject redeObject =  speechesJsonArray.getJSONObject(k);
                String content = redeObject.getString("content");
                int speakerId = id;
                LocalDate date = LocalDate.parse(redeObject.getString("datum"));
                String agendaItem = redeObject.getString("top");
                int legislativePeriod = redeObject.getInt("wp");
                for (int j = 0; j < speechesJsonArray.length(); j++) {
                    String contentId = (id + "-" + j);
                    RedeFile newRede = RedeFile.createRede(content, speakerId, date, agendaItem, legislativePeriod, contentId);

                }

            }


        }

    }



    private ArrayList<Integer> periodsToArray(JSONArray periodsJson){
        ArrayList<Integer> periodsArray = new ArrayList<>();
        for (int i =0; i< periodsJson.length(); i++){
            periodsArray.add(periodsJson.getInt(i));
        }
        return periodsArray;
    }


}
