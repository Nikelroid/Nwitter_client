package offlineSetting;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;

public class getSettingJsonSaver {

    public getSettingJsonSaver(JSONObject jsonObject) {

        try {
            if (jsonObject.get("result").equals("0")) JOptionPane.showMessageDialog(
                    null, "Error in connection");
            else {
                String enable = String.valueOf(jsonObject.getBoolean("enable"));
                String account = String.valueOf(jsonObject.getBoolean("account"));
                JSONArray privacy = jsonObject.getJSONArray("privacy");
                settingOffline.setter(enable, account,
                        Integer.parseInt(privacy.get(0).toString()),
                        Integer.parseInt(privacy.get(1).toString()),
                        Integer.parseInt(privacy.get(2).toString()),
                        Integer.parseInt(privacy.get(3).toString()));
            }
        }catch (Exception ignored){

        }
    }
}
