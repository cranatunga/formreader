package formreader.handler.impl;

import formreader.handler.Ocr;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OcrTesseractImpl implements Ocr {

    Log log = LogFactory.getLog(OcrTesseractImpl.class);

    public String read(File file) {
        String text = null;
//        try {
//            Class.forName("net.sourceforge.tess4j.TessAPI");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        ITesseract instance = new Tesseract();
//        try {
//            text = instance.doOCR(file);
//            log.debug("OCR ---- text : " + text);
//        } catch (TesseractException e) {
//            log.error("OCR Failed ---- ", e);
//        }

        com.asprise.ocr.Ocr.setUp(); // one time setup
        com.asprise.ocr.Ocr ocr = new com.asprise.ocr.Ocr(); // create a new OCR engine
        ocr.startEngine("eng", com.asprise.ocr.Ocr.SPEED_FASTEST); // English
        String s = ocr.recognize(new File[] {file},
                com.asprise.ocr.Ocr.RECOGNIZE_TYPE_ALL, com.asprise.ocr.Ocr.OUTPUT_FORMAT_PLAINTEXT);
        System.out.println("Result: " + s);
// ocr more images here ...
        ocr.stopEngine();

        return s;
    }
}