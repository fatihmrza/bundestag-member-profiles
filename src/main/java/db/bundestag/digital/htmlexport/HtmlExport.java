package db.bundestag.digital.htmlexport;

import db.bundestag.digital.data.Abgeordnete;
import db.bundestag.digital.data.Rede;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface HtmlExport {

    void exportHtml (Map<String, Map<Abgeordnete, List<Rede>>> abgeordneteRedeMap, Path outputhPath) throws IOException;
}
