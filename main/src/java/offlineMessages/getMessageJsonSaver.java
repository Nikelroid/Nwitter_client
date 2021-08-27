package offlineMessages;

import jsonContoller.jsonMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import profiles.messageSaver;
import profiles.profileSaver;

import javax.swing.*;
import java.io.*;

public class getMessageJsonSaver {
    File messagesFile = new File("Message.json");
    submitMessage submitMessage = new submitMessage();
    public getMessageJsonSaver(JSONObject jsonObject) {

        jsonMessage.messageLock.lock();
        try {
            if (jsonObject.get("result").equals("0")) JOptionPane.showMessageDialog(
                    null, "Error in connection");
            else {
                messagesFile.delete();
                JSONArray _idJson = jsonObject.getJSONArray("_id");
                JSONArray textJson = jsonObject.getJSONArray("text");
                JSONArray senderJson = jsonObject.getJSONArray("sender");
                JSONArray receiverJson = jsonObject.getJSONArray("receiver");
                JSONArray timeJson = jsonObject.getJSONArray("time");
                JSONArray seenJson = jsonObject.getJSONArray("seen");
                JSONArray deliveredJson = jsonObject.getJSONArray("delivered");
                JSONArray sentJson = jsonObject.getJSONArray("sent");
                JSONArray messagePics = jsonObject.getJSONArray("message_pics");
                JSONArray profilePics = jsonObject.getJSONArray("profile_pics");
                JSONArray profile = jsonObject.getJSONArray("profile");

                for (int i = 0; i < _idJson.length(); i++) {
                    submitMessage.SubMess(
                            Integer.parseInt(_idJson.get(i).toString()),
                            textJson.get(i).toString(),
                            senderJson.get(i).toString(),
                            receiverJson.get(i).toString(),
                            timeJson.get(i).toString(),
                            (Boolean) seenJson.get(i),
                            (Boolean) deliveredJson.get(i),
                            (Boolean) sentJson.get(i));
                }
                for (int i = 0; i < profilePics.length(); i++) {
                    if (!profilePics.get(i).toString().isEmpty()){
                        new profileSaver(profile.get(i).toString(),
                                profilePics.get(i).toString());
                    }
                }
                for (int i = 0; i < messagePics.length(); i++) {
                    if (!messagePics.get(i).toString().isEmpty()){
                        new messageSaver(Integer.parseInt(_idJson.get(i).toString()),
                                messagePics.get(i).toString());
                    }
                }
            }
        }catch (Exception ignored){
        }finally {
                jsonMessage.messageLock.unlock();
            }
    }
}
