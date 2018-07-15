package formreader.handler.impl;

import formreader.common.FormReaderConstants;
import formreader.handler.Ocr;
import formreader.handler.QuestionnaireHandler;
import formreader.to.ProcessedAreaTO;
import formreader.to.QuestionMetadataTO;
import formreader.to.SheetMetadataTO;
import formreader.to.config.FormReaderConfig;
import formreader.to.template.TemplateMetadataTO;
import formreader.util.FormReaderUtil;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QuestionnaireHandlerImpl implements QuestionnaireHandler {

    @Autowired
    private FormReaderConfig config;

    @Autowired
    private Ocr ocr;

    @Override
    public SheetMetadataTO processTemplate(TemplateMetadataTO questionnaire, String file) {
        Mat matrix = Imgcodecs.imread(file);

        SheetMetadataTO sheetMetadataTO = new SheetMetadataTO();
        sheetMetadataTO.setQuestions(new HashMap<>());
        AtomicInteger counter = new AtomicInteger();
        questionnaire.getQuestions()
                .forEach(q -> {
                    ProcessedAreaTO processedAreaTO;
                    int index = counter.incrementAndGet();
                    QuestionMetadataTO questionMetadataTO = new QuestionMetadataTO();
                    processedAreaTO = ProcessedAreaTO.of(q.getQuestion());

                    questionMetadataTO.setQuestion(processedAreaTO);

                    Mat tempMatrix = matrix.submat(q.getQuestion().getTop(),
                            q.getQuestion().getTop() + q.getQuestion().getHeight(),
                            q.getQuestion().getLeft(),
                            q.getQuestion().getLeft() + q.getQuestion().getWidth());

                    Mat circles = new Mat(tempMatrix.rows(), tempMatrix.cols(), CvType.CV_8UC3, new Scalar(255, 255, 255));
                    int minRadius = 10;
                    int maxRadius = 20;
                    Imgproc.cvtColor(tempMatrix, tempMatrix, Imgproc.COLOR_BGR2GRAY);
                    Imgproc.GaussianBlur( tempMatrix, tempMatrix, new Size(9, 9), 2, 2 );
                    Imgproc.Canny(tempMatrix, tempMatrix, 40, 50);

//                    Mat cannyEdges = new Mat();
//                    Imgproc.Canny(tempMatrix, cannyEdges, 10, 100);
//                    Imgcodecs.imwrite(FormReaderUtil.getIntermediateFileName(file, "1", String.valueOf(index), "canny"), cannyEdges);
//                    Imgproc.HoughCircles(cannyEdges, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 100, 100, 10, minRadius, maxRadius);
//                    Imgproc.HoughCircles(cannyEdges, circles, Imgproc.CV_HOUGH_GRADIENT, 1, maxRadius);
//                    Imgcodecs.imwrite(FormReaderUtil.getIntermediateFileName(file, "1", String.valueOf(index), "2"), circles);


                    /*
                    for (int i = 0; i < circles.cols(); i++) {
                        double[] parameters = circles.get(0, i);
                        double x, y;
                        int r;

                        x = parameters[0];
                        y = parameters[1];
                        r = (int) parameters[2];

                        Point center = new Point(x, y);

//                        Imgproc.circle(tempMatrix, center, r, new Scalar(0, 0, 0), 4);
                    }
                    */

                    List<MatOfPoint> edgeContours = new ArrayList<>();
                    Imgproc.findContours(tempMatrix, edgeContours, new Mat(),
                            Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);


                    MatOfPoint circle  = getCircle();


                    edgeContours.forEach(c -> {
                        Double d = Imgproc.matchShapes(c, circle, Imgproc.CV_CONTOURS_MATCH_I1, 0);
                        if (d < 0.001) {
                            System.out.println(String.format("%.08f", d));
                            Imgproc.drawContours(circles, Collections.singletonList(c), -1, new Scalar(0, 255, 0), 1);
                        }

                    });


//                    Imgproc.mat

                    Imgproc.rectangle (
                            matrix,
                            new Point(q.getQuestion().getLeft(), q.getQuestion().getTop()),
                            new Point(q.getQuestion().getLeft() + q.getQuestion().getWidth(),
                                    q.getQuestion().getTop() + q.getQuestion().getHeight()),
                            new Scalar(0, 0, 255),
                            4
                    );

                    Imgcodecs.imwrite(FormReaderUtil.getIntermediateFileName(file, "1", String.valueOf(index), "1"), circles);

                    sheetMetadataTO.getQuestions()
                            .put(FormReaderConstants.PREFIX_AUTO_ASSIGN_ID + index, questionMetadataTO);


                });


        String ocrFile = "/home/chirantha/code/rnd/java/01/java-02/01/02.jpg";
        Mat m = Imgcodecs.imread(ocrFile);
        byte[] return_buff = new byte[(int) (m.total() *
                m.channels())];
        m.get(0, 0, return_buff);

        File f = new File("/home/chirantha/code/rnd/java/01/java-02/01/02.jpg");
        String s = ocr.read(f);
        System.out.println(s);

        Imgcodecs.imwrite(FormReaderUtil.getIntermediateFileName(file, "1"), matrix);
        return sheetMetadataTO;
    }

    private MatOfPoint getCircle() {
        MatOfPoint circle = null;

        String circleFileName = config.getPath().getObjects() + "circle.jpg";
        Mat matrix = Imgcodecs.imread(circleFileName);

        Mat tempMatrix = new Mat();
        Imgproc.cvtColor(matrix, tempMatrix, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur( tempMatrix, tempMatrix, new Size(9, 9), 2, 2 );
        Imgproc.Canny(tempMatrix, tempMatrix, 40, 50);

        List<MatOfPoint> edgeContours = new ArrayList<>();
        Imgproc.findContours(tempMatrix, edgeContours, new Mat(),
                Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        Imgproc.drawContours(matrix, edgeContours, 3, new Scalar(255, 0, 0), 4);
        circle = edgeContours.get(3);

        Imgcodecs.imwrite(FormReaderUtil.getIntermediateFileName(circleFileName, "1"), matrix);

        return circle;
    }

    public void setConfig(FormReaderConfig config) {
        this.config = config;
    }
}