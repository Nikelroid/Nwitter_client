package offlineMessages;

import connection.sender;
import org.json.JSONObject;

import javax.swing.*;

public class getFollowings {
    JSONObject outputJson = new JSONObject();
    sender sender = new sender();
    public getFollowings(String AuthKey, String username) {
        outputJson.put("key","messages").put("type","deleteChat").put("AuthKey",AuthKey)
                .put("username",username);
        if (sender.send(outputJson).get("result").toString().equals("0"))
            JOptionPane.showMessageDialog(null,"error in connection");

    }
}
