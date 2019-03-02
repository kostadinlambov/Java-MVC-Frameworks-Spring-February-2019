package realestate.util;

import java.io.*;

public class HtmlReader {
    public String readHtml(String filePath) throws IOException {
        StringBuilder result = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(
                                new File(filePath))));

        String line;

        while((line = reader.readLine()) != null){
            result.append(line).append(System.lineSeparator());
        }

        return result.toString().trim();
    }
}
