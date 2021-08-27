package profiles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
public class imageConnection {
    public imageConnection() {
    }
    private static final Logger logger = LogManager.getLogger(imageConnection.class);
    public JSONObject jsonCreator(String type,String AuthKey,String picture) {
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("key","image").put("type",type)
                .put("AuthKey",AuthKey).put("picture",picture);
        logger.info("login json generated");
        return jsonOutput;
    }
}
