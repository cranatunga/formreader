package formreader.service;

import formreader.to.BaseResponse;
import formreader.to.analysis.AnalysisCriteriaTO;
import formreader.to.analysis.ResponseAnalysisTO;
import formreader.to.template.QuestionnaireMetadataTO;

public interface TestFormReaderService {

    String FILE_LOCATION = "/home/chirantha/code/rnd/java/01/java-02/01/";

    BaseResponse uploadTemplate(QuestionnaireMetadataTO questionnaire, String file);

    BaseResponse uploadAnswerSheet(String file);

    ResponseAnalysisTO getResponse(AnalysisCriteriaTO criteria);

}