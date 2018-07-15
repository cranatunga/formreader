package formreader;

import org.opencv.core.Core;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FormReader {

    public static void main(String[] args) {
//        System.loadLibrary("tesseract");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME );
        SpringApplication.run(FormReader.class, args);
    }

}