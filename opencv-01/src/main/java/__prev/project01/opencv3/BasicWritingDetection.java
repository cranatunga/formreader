package __prev.project01.opencv3;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.LineSegmentDetector;

import java.util.List;

public class BasicWritingDetection implements WritingDetection {

    public BasicWritingDetection() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private void detectWriting () {
        Mat src = Imgcodecs.imread("/home/chirantha/code/rnd/java/01/java-02/opencv-01/input/c-01.jpg");

        Mat dst = new Mat();


        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY);

        LineSegmentDetector detector = Imgproc.createLineSegmentDetector();
        detector.detect(dst, dst, new Mat(10), new Mat(2), new Mat(1));

        Mat out1 = new Mat(src.rows(), src.cols(), src.type());
        detector.drawSegments(out1, dst);
//        src.copyTo(out1);
//        dst.copyTo(out1);

//        Imgproc.HoughLines();1917|


        Imgproc.circle(out1, new Point(0, 0), 10, new Scalar(2));

        List l = null;
//        l.forEach(a -> {});

        String filename = "/home/chirantha/code/rnd/java/01/java-02/opencv-01/output/c-01.jpg";
        Imgcodecs.imwrite(filename, out1);
    }


    public static void _main(String[] args) {

        BasicWritingDetection detection = new BasicWritingDetection();
        detection.detectWriting();

    }

}