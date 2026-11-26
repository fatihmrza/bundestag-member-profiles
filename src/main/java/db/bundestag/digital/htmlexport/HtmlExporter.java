package db.bundestag.digital.htmlexport;

import db.bundestag.digital.data.Abgeordnete;
import db.bundestag.digital.data.Rede;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class HtmlExporter implements HtmlExport{

    @Override
    public void exportHtml(Map<String, Map<Abgeordnete, List<Rede>>> abgeordneteRedeMap, Path outputPath) throws IOException {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.uuuu");
        StringBuilder html = new StringBuilder("""
                <!doctype html>
                <html lang="en">
                <head>
                <meta charset = "UTF-8">
                <title>Visitenkarte</title>
                </head>
                <body>
                """);
        for (Map.Entry<String, Map<Abgeordnete,List<Rede>>> items: abgeordneteRedeMap.entrySet()){     // i stored all party name, deputy and his related speeches as Map to avoiding redundant parameters or giving as factory instance as attribute.
            html.append("<h2>");
            html.append(items.getKey()); // Printing fraction name as title.
            html.append("</h2>");
            for (Map.Entry<Abgeordnete, List<Rede>> item : items.getValue().entrySet()){
                html.append("<h3>");
                html.append(item.getKey().getFirstName() + " " + item.getKey().getLastName() + " \n" + item.getKey().getBirthdate().format(dateFormat)); // adding abgeordnete firstname, lastname, and his birthday.
                html.append("</h3>");
                html.append("<details>");
                html.append("<summary>Show speeches</summary>");
                html.append("<ul>");
                for (Rede speech: item.getValue()){
                   html.append("<li>")
                       .append(speech.getContent())
                       .append("<br>")
                       .append(speech.getLegislativePeriod())
                       .append("<br>")
                       .append(speech.getDate().format(dateFormat))
                       .append("<br>")
                       .append("</li>");
                }
                html.append("</ul>");
                html.append("</details>");
            }
        }
        html.append("</body>");
        html.append("</html>");
        Files.writeString(outputPath, html.toString());

    }


}
