package formreader.bl.impl;

import formreader.to.QuestionnaireMetadataTO;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Scope(value = "singleton")
public class QuestionnaireTemplateStore {

    private Map<String, QuestionnaireMetadataTO> metadata;

    public QuestionnaireTemplateStore() {
        metadata = new HashMap<>();
    }

    public void putMetadata(QuestionnaireMetadataTO questionnaire) {
        metadata.put(questionnaire.getName(), questionnaire);
    }

    public QuestionnaireMetadataTO getMetadata(String name) {
        return metadata.get(name);
    }
}