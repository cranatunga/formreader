package formreader.bl;

import formreader.to.BaseResponse;
import formreader.to.analysis.AnalysisCriteriaTO;
import formreader.to.analysis.ResponseAnalysisTO;
import formreader.to.template.TemplateMetadataTO;

public interface FormReaderBL {

    BaseResponse uploadTemplate(TemplateMetadataTO questionnaire, String file);

    BaseResponse uploadAnswerSheet(String file);

    ResponseAnalysisTO getResponse(AnalysisCriteriaTO criteria);
}