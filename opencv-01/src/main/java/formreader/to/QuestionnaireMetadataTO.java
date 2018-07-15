package formreader.to;

import java.util.Map;

public class QuestionnaireMetadataTO {

    private String name;
    private Map<Integer, SheetMetadataTO> sheets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, SheetMetadataTO> getSheets() {
        return sheets;
    }

    public void setSheets(Map<Integer, SheetMetadataTO> sheets) {
        this.sheets = sheets;
    }
}