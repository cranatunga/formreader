package formreader.to;

import java.util.Map;

public class SheetMetadataTO {

    private Integer sheetNumber;
    private PersonMetadataTO person;
    private Map<String, QuestionMetadataTO> questions;

    public Integer getSheetNumber() {
        return sheetNumber;
    }

    public void setSheetNumber(Integer sheetNumber) {
        this.sheetNumber = sheetNumber;
    }

    public PersonMetadataTO getPerson() {
        return person;
    }

    public void setPerson(PersonMetadataTO person) {
        this.person = person;
    }

    public Map<String, QuestionMetadataTO> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, QuestionMetadataTO> questions) {
        this.questions = questions;
    }
}