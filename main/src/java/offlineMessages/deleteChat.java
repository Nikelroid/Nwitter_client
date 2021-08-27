package offlineMessages;

import connection.sender;
import org.json.JSONObject;

import javax.swing.*;

public class deleteChat {
    JSONObject outputJson = new JSONObject();
    sender sender = new sender();
    public deleteChat(String AuthKey,String username) {
        outputJson.put("key","messages").put("type","deleteChat").put("AuthKey",AuthKey)
                .put("username",username);
        try {
            if (sender.send(outputJson).get("result").toString().equals("0"))
                JOptionPane.showMessageDialog(null,"error in connection");
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"You should be online.");

        }


    }
}
