package formreader.util;

import formreader.common.FormReaderConstants;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FormReaderUtil {

    public static String generateTempFileName() {
        return UUID.randomUUID().toString();
    }

    public static void writeFile(String path, String fileName, byte[] content) {

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(path + fileName))) {
            dos.write(content);
        } catch (IOException e) {
            e.printStackTrace(); // TODO -- handle this
        }
    }

    public static String getIntermediateFileName(String fileName, String ... postfixes) {

        int fileNameLength = fileName.lastIndexOf('.');

        String newFileName = fileName.substring(0, fileNameLength);
        for (String postfix : postfixes) {
            newFileName += FormReaderConstants.DELIMITER_FILE_NAME_SECTIONS + postfix;
        }

        newFileName += fileName.substring(fileNameLength);

        return newFileName;
    }

}