package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertyReader {

    public static String getValue(String filePath, String key) {
        String value = null;

        //Input stream is required while loading into properties

        try (InputStream input = Files.newInputStream(Paths.get(filePath)))
        {

            // object creation for Property class
            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            //getProperty will fetch the value according to the key
            value = prop.getProperty(key);


        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return value;
    }
}
