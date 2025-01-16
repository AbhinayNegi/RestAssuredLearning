package utility;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;

public class JsonReader {

    public static String getTestData(String input) throws IOException,  ParseException {
        String testdata;
        return testdata = (String) getJsonData().get(input);//input is the key

    }

    public static JSONObject getJsonData() throws IOException, ParseException  {

        //pass the path of the testdata.json file
        File filename = new File("src//test//resources//testData//testData.json");
        //convert json file into string
        String json = FileUtils.readFileToString(filename, "UTF-8");
        //parse the string into object
        Object obj = new JSONParser().parse(json);
        //give jsonobject o that I can return it to the function everytime it get called
        JSONObject jsonObject = (JSONObject) obj;
        return jsonObject;

    }
}
