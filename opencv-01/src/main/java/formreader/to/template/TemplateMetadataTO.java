package formreader.to.template;

import formreader.to.PersonMetadataTO;

import java.util.List;

public class TemplateMetadataTO {

    private String questionnaireName;
    private Integer sheetNumber;
    private PersonMetadataTO person;
    private List<TemplateQuestionTO> questions;

    public String getQuestionnaireName() {
        return questionnaireName;
    }

    public void setQuestionnaireName(String questionnaireName) {
        this.questionnaireName = questionnaireName;
    }

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

    public List<TemplateQuestionTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<TemplateQuestionTO> questions) {
        this.questions = questions;
    }
}