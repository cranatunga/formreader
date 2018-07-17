package formreader.service;

import formreader.to.BaseResponse;
import formreader.to.analysis.AnalysisCriteriaTO;
import formreader.to.analysis.ResponseAnalysisTO;
import formreader.to.template.TemplateMetadataTO;

public interface TestFormReaderService {

    String FILE_LOCATION = "C:\\Work\\Code\\github\\formreader\\01\\";

    BaseResponse uploadTemplate(TemplateMetadataTO questionnaire, String file);

    BaseResponse uploadAnswerSheet(String file);

    ResponseAnalysisTO getResponse(AnalysisCriteriaTO criteria);

}