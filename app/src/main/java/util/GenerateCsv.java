package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by ASAF on 26/11/2017.
 */

public class GenerateCsv
{
    public static FileWriter generateCsvFile(File sFileName, String fileContent)
    {
        FileWriter writer = null;

        try {
            writer = new FileWriter(sFileName);
            writer.append(fileContent);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return writer;
    }
}