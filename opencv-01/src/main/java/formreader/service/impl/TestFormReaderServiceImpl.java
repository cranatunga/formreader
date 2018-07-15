package formreader.service.impl;

import formreader.bl.FormReaderBL;
import formreader.service.TestFormReaderService;
import formreader.to.BaseResponse;
import formreader.to.analysis.AnalysisCriteriaTO;
import formreader.to.analysis.ResponseAnalysisTO;
import formreader.to.template.TemplateMetadataTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/test/formreader")
public class TestFormReaderServiceImpl implements TestFormReaderService {

    private static Log log = LogFactory.getLog(TestFormReaderServiceImpl.class);

    @Autowired
    private FormReaderBL formReaderBL;

    @RequestMapping(path = "/template", method = RequestMethod.POST)
    public BaseResponse uploadTemplate(@RequestBody TemplateMetadataTO questionnaire, @RequestParam("file") String file) {

        long startTime = System.currentTimeMillis();
        BaseResponse response = formReaderBL.uploadTemplate(questionnaire, TestFormReaderService.FILE_LOCATION + file);
        long endTime = System.currentTimeMillis();
        System.out.println("Process Time : " + (endTime - startTime));

        return response;
    }

    @RequestMapping(path = "/answer", method = RequestMethod.POST)
    public BaseResponse uploadAnswerSheet(@RequestParam("file") String file) {
        return formReaderBL.uploadAnswerSheet(TestFormReaderService.FILE_LOCATION + file);
    }

    @RequestMapping(path = "/analysis", method = RequestMethod.POST)
    public ResponseAnalysisTO getResponse(@RequestBody AnalysisCriteriaTO criteria) {
        return formReaderBL.getResponse(criteria);
    }

    public void setFormReaderBL(FormReaderBL formReaderBL) {
        this.formReaderBL = formReaderBL;
    }
}