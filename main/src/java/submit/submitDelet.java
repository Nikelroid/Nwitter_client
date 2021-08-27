package submit;

import connection.sender;
import offlineSetting.jsonGeneratorSetting;
import jsonContoller.jsonNotifs;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import launch.authKey;
import login.loginView;
import objects.objNotifs;
import objects.objTwitte;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import twitteControl.deathPages;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class submitDelet {
    jsonUsers get_j = new jsonUsers();
    jsonNotifs get_n = new jsonNotifs();
    List<objUsers> users = get_j.get();
    List<objTwitte> twittes;
    List<objNotifs> notifs = get_n.get();
    jsonTwittes Get = new jsonTwittes();
    File f = new File("Twittes.json");
    File n = new File("Notifs.json");
    offlineSetting.jsonGeneratorSetting jsonGeneratorSetting = new jsonGeneratorSetting();
    sender sender = new sender();
    private static final Logger logger = LogManager.getLogger(submitDelet.class);
    public submitDelet(String AuthKey) {

        JSONObject jsonOutput = jsonGeneratorSetting.generate("delete",AuthKey);
        JSONObject jsonResults = sender.send(jsonOutput);
        if (jsonResults.get("result").equals("0")) JOptionPane.showMessageDialog(null,
                "Error in deleting account");
        else{
            logger.info("System: " + AuthKey + "Account deleted");
            try {
                new deathPages();
                authKey.setter("0","0","");
                new loginView(true);
            } catch (Exception e) {
                logger.error("System: error in reload login page");
            }
        }
    }
}
