package db.bundestag.digital.file;
import db.bundestag.digital.data.Abgeordnete;
import db.bundestag.digital.data.Rede;
import org.bson.json.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.texttechnologylab.utilities.FileUtilities;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileReader {


    public FileReader() {
    }


    public static List<AbgeordneteFile> loadDocument(String filePath) throws IOException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        File file = new File(filePath);
        JSONArray jsonArray = FileUtilities.readContentAsJSON(file);
        List<AbgeordneteFile> deputies = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String firstName = jsonObject.getString("vorname");
            String lastName = jsonObject.getString("name");
            String party = jsonObject.getString("partei");
            ArrayList<Integer> legislativePeriods = periodsToArray(jsonObject.getJSONArray("perioden")); // using helping method to convert JSONArray into ArrayList.
            String profession = jsonObject.getString("beruf");

            LocalDate birthdate = LocalDate.parse(jsonObject.getString("geburtsdatum"), dateFormat);
            String birthplace = jsonObject.getString("geburtsort");
            String gender = jsonObject.getString("geschlecht");
            String deathdateText = jsonObject.optString("sterbedatum", "");
            LocalDate deathdate = deathdateText.isBlank()
                    ? null
                    : LocalDate.parse(deathdateText, dateFormat);
            String academicTitle = jsonObject.getString("akademischer_titel");
            String salutionTitle = jsonObject.getString("anrede_titel");


            JSONArray speechesJsonArray = jsonObject.getJSONArray("speeches");

            List<Rede> speeches = new ArrayList<>();
            for (int k = 0; k < speechesJsonArray.length(); k++){
                JSONObject redeObject =  speechesJsonArray.getJSONObject(k);
                String content = redeObject.getString("content");
                int speakerId = id;
                LocalDate date = LocalDate.parse(redeObject.getString("datum"), dateFormat);
                String agendaItem = redeObject.getString("top");
                int legislativePeriod = redeObject.getInt("wp");
                String contentId = (id + "-" + k);
                RedeFile newRede = RedeFile.createRede(content, speakerId, date, agendaItem, legislativePeriod, contentId);
                speeches.add(newRede);

            }
            AbgeordneteFile newAbgeordnete = AbgeordneteFile.createAbgeordneteFile(id, firstName, lastName, party, legislativePeriods, profession, birthdate, birthplace, gender, deathdate, academicTitle, salutionTitle, speeches);
            deputies.add(newAbgeordnete);


        }
        return deputies;

    }






    private static ArrayList<Integer> periodsToArray(JSONArray periodsJson){
        ArrayList<Integer> periodsArray = new ArrayList<>();
        for (int i =0; i< periodsJson.length(); i++){
            periodsArray.add(periodsJson.getInt(i));
        }
        return periodsArray;
    }




}
