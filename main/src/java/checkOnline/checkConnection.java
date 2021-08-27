package checkOnline;

import connection.sender;
import org.json.JSONObject;

import javax.swing.*;

public class checkConnection {
    JSONObject outputJson = new JSONObject();
    sender sender = new sender();
    public boolean isConnected() {
        outputJson.put("key","connection");
        try {
            sender.send(outputJson);
            return true;
        }catch (Exception e){
            return false;
        }


    }
}
