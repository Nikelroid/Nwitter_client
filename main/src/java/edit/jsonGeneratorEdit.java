package edit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class jsonGeneratorEdit {
    public jsonGeneratorEdit() {
    }
    private static final Logger logger = LogManager.getLogger(jsonGeneratorEdit.class);
    public JSONObject generate(String type,String AuthKey,String text){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("key","edit")
                .put("type",type)
                .put("AuthKey",AuthKey)
                .put("new",text);
        logger.info("successfully load json generated");
        return jsonOutput;
    }
}
