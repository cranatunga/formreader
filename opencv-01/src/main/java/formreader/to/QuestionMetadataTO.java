package formreader.to;

import java.util.List;
import java.util.Map;

public class QuestionMetadataTO {

    private ProcessedAreaTO questionNumber;
    private ProcessedAreaTO question;
    private String answerContentPosition;
    private List<AnswerMetadataTO> answers;
    private Map<String, QuestionMetadataTO> subQuestions;

    public ProcessedAreaTO getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(ProcessedAreaTO questionNumber) {
        this.questionNumber = questionNumber;
    }

    public ProcessedAreaTO getQuestion() {
        return question;
    }

    public void setQuestion(ProcessedAreaTO question) {
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

    public Map<String, QuestionMetadataTO> getSubQuestions() {
        return subQuestions;
    }

    public void setSubQuestions(Map<String, QuestionMetadataTO> subQuestions) {
        this.subQuestions = subQuestions;
    }
}