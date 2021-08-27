package edit;

import connection.sender;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;

public class editProfileConnection {
    jsonGeneratorEdit jsonGeneratorEdit = new jsonGeneratorEdit();
    sender sender = new sender();
    public editProfileConnection(String type,String AuthKey,String text) {
        JSONObject jsonObject = jsonGeneratorEdit.generate(type,AuthKey,text);
        JSONObject jsonResult = sender.send(jsonObject);
        if (jsonResult.get("result").equals("0")){
            JOptionPane.showMessageDialog(null,"Error in connection");
        }else if (type.equals("get")){
            new editProfileDecoder(jsonResult);
        }

    }
}
