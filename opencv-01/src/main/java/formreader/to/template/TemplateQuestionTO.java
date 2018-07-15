package formreader.to.template;

import formreader.to.AreaTO;

import java.util.List;

public class TemplateQuestionTO {

    private AreaTO questionNumber;
    private AreaTO question;
    private String answerContentPosition;
    private List<TemplateQuestionTO> subQuestions;

    public AreaTO getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(AreaTO questionNumber) {
        this.questionNumber = questionNumber;
    }

    public AreaTO getQuestion() {
        return question;
    }

    public void setQuestion(AreaTO question) {
        this.question = question;
    }

    public String getAnswerContentPosition() {
        return answerContentPosition;
    }

    public void setAnswerContentPosition(String answerContentPosition) {
        this.answerContentPosition = answerContentPosition;
    }

    public List<TemplateQuestionTO> getSubQuestions() {
        return subQuestions;
    }

    public void setSubQuestions(List<TemplateQuestionTO> subQuestions) {
        this.subQuestions = subQuestions;
    }
}