package formreader.to.template;

import java.util.List;

public class SheetMetadataTO {

    private Integer sheetNumber;
    private PersonMetadataTO person;
    private List<QuestionMetadataTO> questions;

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

    public List<QuestionMetadataTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionMetadataTO> questions) {
        this.questions = questions;
    }
}