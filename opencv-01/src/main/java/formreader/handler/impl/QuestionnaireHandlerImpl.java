package formreader.handler.impl;

import formreader.common.FormReaderConstants;
import formreader.handler.Ocr;
import formreader.handler.QuestionnaireHandler;
import formreader.to.ProcessedAreaTO;
import formreader.to.QuestionMetadataTO;
import formreader.to.SheetMetadataTO;
import formreader.to.config.FormReaderConfig;
import formreader.to.template.TemplateMetadataConstants;
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
import java.util.*;
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

                    Mat optionsProcess = tempMatrix.clone();
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


                    List<MatOfPoint>[] arr = new List[tempMatrix.cols() + 1];
                    textContours.forEach(c -> {
                        Rect rect = Imgproc.boundingRect(c);
                        if (arr[rect.x] == null) {
                            arr[rect.x] = new ArrayList<>();
                        }
                        arr[rect.x].add(c);
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
                                tempSec.addAll(arr[i]);
                            }
                        } else if (arr[i] != null) {
                            w = 0;
                            tempSec.addAll(arr[i]);
                        }
                    }
                    secs.add(tempSec);

                    List<Rect> textProcessRects = new ArrayList<>();

                    secs.forEach(s -> {
                        Imgproc.drawContours(spaces, s, -1, new Scalar(0, 255, 0), 4);

                        AtomicInteger t = new AtomicInteger(Integer.MAX_VALUE),
                        l = new AtomicInteger(Integer.MAX_VALUE),
                        b = new AtomicInteger(Integer.MIN_VALUE),
                        r = new AtomicInteger(Integer.MIN_VALUE);
                        s.forEach(mp -> {
                            mp.toList().forEach(p -> {
                                t.set(Math.min((int)p.y, t.get()));
                                l.set(Math.min((int)p.x, l.get()));
                                b.set(Math.max((int)p.y, b.get()));
                                r.set(Math.max((int)p.x, r.get()));
                            });
                        });

                        Imgproc.rectangle(spaces,
                                new Point(l.get() - 10, t.get() - 10),
                                new Point(r.get() + 10, b.get() + 10),
                                new Scalar(255, 0, 0), 4);

                        System.out.println(l.get() + "--" + t.get() + "--" + r.get() + "--" + b.get());
                        textProcessRects.add(new Rect(new Point(l.get() - 10, t.get() - 10), new Point(r.get() + 10, b.get() + 10)));
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


                    List<Rect> optionProcessRects = new ArrayList<>();
                    edgeContours.forEach(c -> {
                        Double d = Imgproc.matchShapes(c, circle, Imgproc.CV_CONTOURS_MATCH_I1, 0);
                        if (d < 0.001) {
//                            System.out.println(String.format("%.08f", d));
                            Imgproc.drawContours(circles, Collections.singletonList(c), -1, new Scalar(0, 255, 0), 1);

                            Rect r = Imgproc.boundingRect(c);
                            optionProcessRects.add(r);
                            System.out.println("c : " + String.format("%.08f", d) + "--" + r.x + "--" + r.y + "--" + r.height + "--" + r.width);
                        }

                    });

                    Imgproc.rectangle (
                            matrix,
                            new Point(q.getQuestion().getLeft(), q.getQuestion().getTop()),
                            new Point(q.getQuestion().getLeft() + q.getQuestion().getWidth(),
                                    q.getQuestion().getTop() + q.getQuestion().getHeight()),
                            new Scalar(0, 0, 255),
                            4
                    );

                    Imgcodecs.imwrite(FormReaderUtil.getIntermediateFileName(file, "1", String.valueOf(index), "1"), circles);

                    processQuestions(file, tempMatrix, textProcessRects, optionProcessRects, q.getAnswerContentPosition());

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

    private ProcessedQuestion processQuestions(String file, Mat tempMatrix, List<Rect> textProcess, List<Rect> optionProcess, String position) {

        ProcessedQuestion question = new ProcessedQuestion();
        question.setQuestion(textProcess.get(0));

        int countCountForNotSelected = 4;
        int i = 0;

        Mat m;

        optionProcess.sort((o1, o2) -> {return o1.y - o2.y;});

        m = tempMatrix.submat(textProcess.get(0));
        try {
            Imgcodecs.imwrite(FormReaderUtil.getIntermediateFileName(file, "5", String.valueOf(++i), "2"), m);

            File temp = new File(FormReaderUtil.getIntermediateFileName(file, "5", String.valueOf(i), "2"));
            String s = ocr.read(temp);
            System.out.println("Question: -- " + s.trim());

        } catch (Exception e) {

            e.printStackTrace();
        }

        int cols = (textProcess.size() - 1) / 2;

        if (TemplateMetadataConstants.AnswerContentPosition.LEFT.equals(position)) {
            for (int a = 0; a < cols ; a++) {
                try {
                    m = tempMatrix.submat(textProcess.get((a * 2) + 1));
                    Imgcodecs.imwrite(FormReaderUtil.getIntermediateFileName(file, "5", String.valueOf(++i), "2"), m);

                    File temp = new File(FormReaderUtil.getIntermediateFileName(file, "5", String.valueOf(i), "2"));
                    String s = ocr.read(temp);
                    System.out.println("Answer: -- " + s.trim());

                } catch (Exception e) {

                    e.printStackTrace();
                }


                AtomicInteger c = new AtomicInteger(0);
                Rect tempRect = textProcess.get((a * 2) + 2);
                List<List<Rect>> scannedRects = new ArrayList<>();
                List<Rect> currRects = null;
                Rect lastRect = null;
                for (Rect r : optionProcess) {
                    if (tempRect.x < r.x && tempRect.x + tempRect.width > r.x + r.width) {
                        if (lastRect == null) {
                            lastRect = r;
                            currRects = new ArrayList<>();
                            currRects.add(r);
                            scannedRects.add(currRects);
                        } else {

                            if (lastRect.x <= r.x && lastRect.x + lastRect.width >= r.x + r.width &&
                                    lastRect.y + lastRect.height >= r.y + r.height ) {
                                currRects.add(r);
                                lastRect = r;
                            } else {
                                lastRect = r;
                                currRects = new ArrayList<>();
                                currRects.add(r);
                                scannedRects.add(currRects);
                            }

                        }
                    }
                }

                String answer = "";
                for (List<Rect> scan : scannedRects) {
                    if (scan.size() == countCountForNotSelected) {
                        System.out.println("Option : Not Selected");
                    } else {
                        System.out.println("Option : Selected");
                    }
                }
            }
        }

        return question;
    }

    class ProcessedQuestion {
        private Rect question;
        private List<ProcessedAnswer> answers;

        public Rect getQuestion() {
            return question;
        }

        public void setQuestion(Rect question) {
            this.question = question;
        }

        public List<ProcessedAnswer> getAnswers() {
            return answers;
        }

        public void setAnswers(List<ProcessedAnswer> answers) {
            this.answers = answers;
        }
    }

    class ProcessedAnswer {
        private Rect answer;
        private Rect mark;
        private String output;

        public Rect getAnswer() {
            return answer;
        }

        public void setAnswer(Rect answer) {
            this.answer = answer;
        }

        public Rect getMark() {
            return mark;
        }

        public void setMark(Rect mark) {
            this.mark = mark;
        }

        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }
    }

    public void setConfig(FormReaderConfig config) {
        this.config = config;
    }
}