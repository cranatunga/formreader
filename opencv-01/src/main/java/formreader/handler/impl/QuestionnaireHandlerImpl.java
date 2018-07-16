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
import org.opencv.objdetect.Objdetect;
import org.opencv.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QuestionnaireHandlerImpl implements QuestionnaireHandler {

    private interface IntermediaryFileName {
        String[] BOUNDARIES_MARKED = {"1"};
        String[] SPLIT_QUESTION = {"2"};
        String[] OBJECT_CIRCLES = {"3", "1"};
        String[] OBJECT_SQUARES = {"3", "2"};
        String[] TEXT_QUESTION = {"4", "1"};
        String[] TEXT_OPTIONS = {"4", "2"};
    }

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

                    Mat m = tempMatrix;

                    Mat optionsProcess = new Mat(tempMatrix.rows(), tempMatrix.cols(), tempMatrix.type());
                    Mat textProcess = new Mat(tempMatrix.rows(), tempMatrix.cols(), tempMatrix.type());

                    Imgproc.cvtColor(tempMatrix, textProcess, Imgproc.COLOR_BGR2GRAY);
                    Imgproc.GaussianBlur(textProcess, textProcess, new Size(9, 9), 2, 2 );
                    Imgproc.Canny(textProcess, textProcess, 40, 50);

                    Mat space  = getSpace();

                    List<MatOfPoint> textContours = new ArrayList<>();
                    Imgproc.findContours(textProcess, textContours, new Mat(),
                            Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

                    System.out.println("---------");
                    Mat spaces = tempMatrix.clone();
//                    Mat spaces = new Mat(tempMatrix.rows(), tempMatrix.cols(), CvType.CV_8UC3, new Scalar(255, 255, 255));
                    Imgproc.drawContours(spaces, textContours, -1, new Scalar(0, 255, 0), 4);


                    List<MatOfPoint>[] arr = new List[tempMatrix.cols() + 1];
                    textContours.forEach(c -> {
                        Rect rect = Imgproc.boundingRect(c);
                        if (arr[rect.x] == null) {
                            arr[rect.x] = new ArrayList<>();
                        }
                        arr[rect.x].add(c);
                        System.out.println("--" + rect.x + "--" + rect.y + "--" + rect.width + "--" + rect.height + "--"
                                + Imgproc.contourArea(c));
                    });
                    List<List<MatOfPoint>> secs = new ArrayList<>();
                    List<MatOfPoint> tempSec = new ArrayList<>();
                    int max = 50;
                    int w = 0;
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i] == null || arr[i].isEmpty()) {
                            w++;
                        } else if (w > max && arr[i] != null && (!arr[i].isEmpty())) {
                            w = 0;
                            if (!tempSec.isEmpty()) {
                                secs.add(tempSec);
                                tempSec = new ArrayList<>();
                            }
                        } else if (arr[i] != null) {
                            w = 0;
                            tempSec.addAll(arr[i]);
                        }
                    }

                    secs.forEach(s -> {
                        Imgproc.rectangle(spaces,
                                new Point(s.get(0).toList().get(0).x, s.get(0).toList().get(0).y),
                                new Point(s.get(s.size() - 1).toList().get(0).x, s.get(s.size() - 1).toList().get(0).y),
                                new Scalar(255, 0, 0), 4);
                            });

                    Imgcodecs.imwrite(FormReaderUtil.getIntermediateFileName(file, "1", String.valueOf(index), "4"), spaces);




//                    Mat textProcess1 = new Mat(textProcess.rows(), textProcess.cols(), textProcess.type());
//                    Mat markers1 = new Mat(textProcess.size(),CvType.CV_8U, new Scalar(0));
//                    Mat markers2 =new Mat();
//                    markers1.convertTo(markers2, CvType.CV_32SC1);
//                    Imgproc.connectedComponents(textProcess, textProcess1);



//                    Imgcodecs.imwrite(FormReaderUtil.getIntermediateFileName(file, "1", String.valueOf(index), "3"), markers2);




                    Mat circles = new Mat(tempMatrix.rows(), tempMatrix.cols(), CvType.CV_8UC3, new Scalar(255, 255, 255));
                    int minRadius = 10;
                    int maxRadius = 20;
                    Imgproc.cvtColor(optionsProcess, optionsProcess, Imgproc.COLOR_BGR2GRAY);
                    Imgproc.GaussianBlur(optionsProcess, optionsProcess, new Size(9, 9), 2, 2 );
                    Imgproc.Canny(optionsProcess, optionsProcess, 40, 50);

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
                    Imgproc.findContours(optionsProcess, edgeContours, new Mat(),
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


                    try {
                        Imgcodecs.imwrite(FormReaderUtil.getIntermediateFileName(file, "1", String.valueOf(index), "2"), m);

                        File temp = new File(FormReaderUtil.getIntermediateFileName(file, "1", String.valueOf(index), "2"));
                        String s = ocr.read(temp);
                        System.out.println(s);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }


                    sheetMetadataTO.getQuestions()
                            .put(FormReaderConstants.PREFIX_AUTO_ASSIGN_ID + index, questionMetadataTO);


                });

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

    private Mat getSpace() {

        String circleFileName = config.getPath().getObjects() + "space.jpg";
        Mat matrix = Imgcodecs.imread(circleFileName);

        Imgproc.cvtColor(matrix, matrix, Imgproc.COLOR_BGR2GRAY);

        return matrix;
    }


    public void setConfig(FormReaderConfig config) {
        this.config = config;
    }
}