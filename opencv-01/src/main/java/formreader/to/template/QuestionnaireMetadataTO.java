package formreader.to.template;

import java.util.List;

public class QuestionnaireMetadataTO {

    private String name;
    private Integer sheetsCount;
    private List<SheetMetadataTO> sheets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSheetsCount() {
        return sheetsCount;
    }

    public void setSheetsCount(Integer sheetsCount) {
        this.sheetsCount = sheetsCount;
    }

    public List<SheetMetadataTO> getSheets() {
        return sheets;
    }

    public void setSheets(List<SheetMetadataTO> sheets) {
        this.sheets = sheets;
    }
}