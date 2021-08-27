package loadTwittes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class jsonGeneratorLoad {
    public jsonGeneratorLoad() {
    }
    private static final Logger logger = LogManager.getLogger(jsonGeneratorLoad.class);
    public JSONObject generate(String type,String AuthKey){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("key","load")
                .put("type",type)
                .put("AuthKey",AuthKey);
        logger.info("successfully load json generated");
        return jsonOutput;
    }
    public JSONObject generate(String type,String AuthKey,String serial){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("key","load")
                .put("type",type)
                .put("AuthKey",AuthKey)
                .put("serial",serial);
        logger.info("successfully load json generated");
        return jsonOutput;
    }
}
