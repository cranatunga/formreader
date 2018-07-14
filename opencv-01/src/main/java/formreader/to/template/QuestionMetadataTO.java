package formreader.to.template;

import formreader.to.AreaTO;

import java.util.List;

public class QuestionMetadataTO {

    private AreaTO questionNumber;
    private AreaTO question;
    private String answerContentPosition;
    private List<AnswerMetadataTO> answers;
    private List<QuestionMetadataTO> subQuestions;

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

    public List<AnswerMetadataTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerMetadataTO> answers) {
        this.answers = answers;
    }

    public List<QuestionMetadataTO> getSubQuestions() {
        return subQuestions;
    }

    public void setSubQuestions(List<QuestionMetadataTO> subQuestions) {
        this.subQuestions = subQuestions;
    }
}