package messengerChilds;

import connection.sender;
import org.json.JSONObject;

public class messengerConnection {
    sender sender = new sender();
    JSONObject outputJSon = new JSONObject();
    public JSONObject getter(String type,String AuthKey) {
        outputJSon.put("key","category").put("type",type).put("AuthKey",AuthKey);
        return sender.send(outputJSon);
    }
    public JSONObject getter(String type,String AuthKey,String catName) {
        outputJSon.put("key","category").put("type",type).put("AuthKey",AuthKey)
                .put("catName",catName);
        return sender.send(outputJSon);
    }
    public JSONObject getter(String type,String AuthKey,String catName,String username) {
        outputJSon.put("key","category").put("type",type).put("AuthKey",AuthKey)
                .put("catName",catName).put ("username",username);
        return sender.send(outputJSon);
    }
}
