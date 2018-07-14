package __prev.project01.opencv3;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.Core.inRange;

public class IconSelectDetection implements OptionSelectDetection {


    private void detectSquare() {

    }

    private void detectCircle() {
        Mat lower_red_hue_range = Imgcodecs.imread("/home/chirantha/code/rnd/java/01/java-02/opencv-01/input/c-01.jpg");
        Mat blurredImage = new Mat();
        Mat hsv_image = new Mat();
        Mat mask = new Mat();
        Mat morphOutput = new Mat();

// remove some noise
        Imgproc.blur(lower_red_hue_range, blurredImage, new Size(7, 7));

// convert the frame to HSV
        Imgproc.cvtColor(blurredImage, hsv_image, Imgproc.COLOR_BGR2HSV);
// Convert input image to HSV

//        Imgproc.cvtColor(bgr_image, hsv_image, Imgproc.COLOR_BGR2HSV);
        Mat upper_red_hue_range = Imgcodecs.imread("/home/chirantha/code/rnd/java/01/java-02/opencv-01/input/c-01.jpg");
        inRange(hsv_image, new Scalar(0, 100, 100), new Scalar(10, 255, 255), lower_red_hue_range);
        inRange(hsv_image, new Scalar(160, 100, 100), new Scalar(179, 255, 255), upper_red_hue_range);
    }

}