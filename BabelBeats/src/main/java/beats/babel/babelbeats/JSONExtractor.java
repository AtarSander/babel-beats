package beats.babel.babelbeats;

import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;

public class JSONExtractor {
    public String extract(String json, String key){
        JSONObject parser = new JSONObject(json);
        return parser.getString(key);
    }

    public String[] readFromFile(String fileName, String[] keys) {
        try {
            String[] output = new String[keys.length];
//            JSONObject parser = new JSONObject(fr.read());
//            System.out.println(Paths.get("").toAbsolutePath());
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            reader.close();

            String content = stringBuilder.toString();
            JSONObject parser = new JSONObject(content);

            for (int i = 0; i < keys.length; i++){
                output[i] = parser.getString(keys[i]);
            }
            return output;
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
