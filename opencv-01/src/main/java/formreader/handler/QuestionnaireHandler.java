package formreader.handler;

import formreader.to.SheetMetadataTO;
import formreader.to.template.TemplateMetadataTO;

public interface QuestionnaireHandler {

    SheetMetadataTO processTemplate(TemplateMetadataTO questionnaire, String file);
}