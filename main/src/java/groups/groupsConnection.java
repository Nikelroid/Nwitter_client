package groups;

import connection.sender;
import org.json.JSONObject;

public class groupsConnection {
    sender sender = new sender();
    JSONObject outputJSon = new JSONObject();
    public JSONObject getter(String type,String AuthKey) {
        outputJSon.put("key","groups").put("type",type).put("AuthKey",AuthKey);
        return sender.send(outputJSon);
    }
    public JSONObject getter(String type,String AuthKey,String gpName) {
        outputJSon.put("key","groups").put("type",type).put("AuthKey",AuthKey)
                .put("gpName",gpName);
        return sender.send(outputJSon);
    }
    public JSONObject getter(String type,String AuthKey,String gpName,String username) {
        outputJSon.put("key","groups").put("type",type).put("AuthKey",AuthKey)
                .put("gpName",gpName).put ("username",username);
        return sender.send(outputJSon);
    }
}
