package formreader.service.impl;

import formreader.service.TestFormReaderService;
import formreader.to.BaseResponse;
import formreader.to.analysis.AnalysisCriteriaTO;
import formreader.to.analysis.ResponseAnalysisTO;
import formreader.to.template.QuestionnaireMetadataTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test/")
public class TestFormReaderServiceImpl implements TestFormReaderService {

    public BaseResponse uploadTemplate(QuestionnaireMetadataTO questionnaire, String file) {

        BaseResponse response = new BaseResponse();
        return response;
    }

    public BaseResponse uploadAnswerSheet(String file) {

        BaseResponse response = new BaseResponse();
        return response;
    }

    public ResponseAnalysisTO getResponse(AnalysisCriteriaTO criteria) {

        ResponseAnalysisTO analysis = new ResponseAnalysisTO();
        return analysis;
    }
}