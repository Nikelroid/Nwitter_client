package offlineMessages;

import connection.sender;
import org.json.JSONObject;

public class getMessageJsonGenerator {
    connection.sender sender = new sender();
    JSONObject jsonOutput = new JSONObject();
    public JSONObject connection(String AuthKey,String type) {
        jsonOutput.put("key","messages").put("type",type).put("AuthKey",AuthKey);
        return sender.send(jsonOutput);
    }
}
