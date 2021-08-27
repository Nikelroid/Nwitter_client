package offlineSetting;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class jsonGeneratorSetting {
    public jsonGeneratorSetting() {
    }
    private static final Logger logger = LogManager.getLogger(jsonGeneratorSetting.class);
    public JSONObject generate(String type,String AuthKey){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("key","setting")
                .put("type",type)
                .put("AuthKey",AuthKey);
        logger.info("successfully load json generated");
        return jsonOutput;
    }
    public JSONObject generate(String type,String AuthKey,String value){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("key","setting")
                .put("type",type)
                .put("AuthKey",AuthKey)
                .put("value",value);
        logger.info("successfully load json generated");
        return jsonOutput;
    }
}
