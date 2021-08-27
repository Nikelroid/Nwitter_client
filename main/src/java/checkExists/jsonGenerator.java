package checkExists;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class jsonGenerator {
    private static final Logger logger = LogManager.getLogger(jsonGenerator.class);
    public JSONObject generator(String field, String input) {
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("key","exists").put("field",field).put("input",input);
        logger.info("exists json generated");
        return jsonOutput;
    }
    public JSONObject generator(String field, String input,String AuthKey) {
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("key","exists").put("field",field).put("input",input).put("AuthKey",AuthKey);
        logger.info("exists json generated");
        return jsonOutput;
    }
}
