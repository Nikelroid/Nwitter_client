package operation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class jsonCreatorOperator {
    public jsonCreatorOperator() {
    }
    private static final Logger logger = LogManager.getLogger(jsonCreatorOperator.class);
    public JSONObject generate(String operator,String AuthKey,String serial){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("key","operate")
                .put("operator",operator)
                .put("AuthKey",AuthKey)
                .put("serial",serial);

        logger.info("operate json generated");
        return jsonOutput;
    }
    public JSONObject generate(String operator, String AuthKey, String serial, String text){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("key","operate")
                .put("operator",operator)
                .put("AuthKey",AuthKey)
                .put("serial",serial)
                .put("text",text);

        logger.info("operate json generated");
        return jsonOutput;
    }
}
