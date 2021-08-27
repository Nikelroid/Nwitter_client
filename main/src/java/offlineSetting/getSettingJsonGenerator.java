package offlineSetting;

import connection.sender;
import org.json.JSONObject;

public class getSettingJsonGenerator {
    connection.sender sender = new sender();
    JSONObject jsonOutput = new JSONObject();
    public JSONObject connection(String AuthKey,String type) {
        jsonOutput.put("key","setting").put("type",type).put("AuthKey",AuthKey);
        return sender.send(jsonOutput);
    }
}
