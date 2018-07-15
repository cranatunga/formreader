package formreader.bl.impl;

import formreader.bl.FormReaderBL;
import formreader.handler.Ocr;
import formreader.handler.Omr;
import formreader.handler.QuestionnaireHandler;
import formreader.to.BaseResponse;
import formreader.to.QuestionnaireMetadataTO;
import formreader.to.SheetMetadataTO;
import formreader.to.analysis.AnalysisCriteriaTO;
import formreader.to.analysis.ResponseAnalysisTO;
import formreader.to.config.FormReaderConfig;
import formreader.to.template.TemplateMetadataTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class FormReaderBLImpl implements FormReaderBL {

    private static Log log = LogFactory.getLog(FormReaderBLImpl.class);

    @Autowired
    private QuestionnaireTemplateStore store;

    @Autowired
    private FormReaderConfig config;

    @Autowired
    private Omr omr;

    @Autowired
    private Ocr ocr;

    @Autowired
    private QuestionnaireHandler questionnaireHandler;

    public BaseResponse uploadTemplate(TemplateMetadataTO questionnaire, String file) {

        QuestionnaireMetadataTO questionnaireMetadata = store.getMetadata(questionnaire.getQuestionnaireName());
        if (questionnaireMetadata == null) {
            questionnaireMetadata = new QuestionnaireMetadataTO();
            questionnaireMetadata.setName(questionnaire.getQuestionnaireName());
            questionnaireMetadata.setSheets(new HashMap<>());
            store.putMetadata(questionnaireMetadata);
        }

        SheetMetadataTO sheetMetadata = questionnaireHandler.processTemplate(questionnaire, file);
        sheetMetadata.setSheetNumber(questionnaire.getSheetNumber());

        questionnaireMetadata.getSheets().put(questionnaire.getSheetNumber(), sheetMetadata);

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

    public void setStore(QuestionnaireTemplateStore store) {
        this.store = store;
    }

    public void setConfig(FormReaderConfig config) {
        this.config = config;
    }

    public void setOmr(Omr omr) {
        this.omr = omr;
    }

    public void setOcr(Ocr ocr) {
        this.ocr = ocr;
    }
}