package submit;

import connection.sender;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import launch.authKey;
import lists.jsonListDecoder;
import lists.userSelect;
import mainPages.Messenger;
import objects.objTwitte;
import objects.objUsers;
import operation.jsonCreatorOperator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import twitteControl.TwitteController;
import twitteControl.deathPages;
import userControl.userFinder;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class submitShare {
    jsonTwittes Get = new jsonTwittes();
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();
    List<objTwitte> twittes = Get.get();
    int[] userMapper = new int[users.size()];
    int target = 0,userCounter = 1;
    submitMessage sb = new submitMessage();
    listJson listJson = new listJson();

    private static final Logger logger = LogManager.getLogger(submitShare.class);

    public submitShare() {}
    connection.sender sender = new sender();
    jsonCreatorOperator jsonCreator = new jsonCreatorOperator();
        public submitShare(String Text,String AuthKey) throws IOException {

            authKey.getter();
            String username = authKey.username;

            ArrayList<String> usernames = new ArrayList<String>();

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
            JSONObject likeRequest = listJson.get("followings",username);
            JSONObject outputJson = sender.send(likeRequest);
            new jsonListDecoder(outputJson);

            usernames = jsonListDecoder.username;

            if (usernames.size()>1) {
                userSelect.select = new ArrayList<>();
                try {
                    new userSelect(AuthKey, username);
                } catch (IOException e) {
                    logger.error("Error in loading user select page from mass messenger");
                }
                boolean sent = false;
                for (int i = 0; i < userSelect.select.size(); i++) {
                    if (userSelect.select.get(i).isSelected()) {
                        sb.SubMess(Text, username, usernames.get(i),dtf.format(now));
                        sent = true;
                    }
                }
                if (sent)
                    JOptionPane.showMessageDialog(null, "Forwarded to selected followings!");

            }
    }

    public void submitSave(String serial,String AuthKey){
        logger.info("System: user went to submitSave");
        JSONObject likeRequest = jsonCreator.generate("save_twitte",AuthKey,serial);
        String result = sender.send(likeRequest).get("result").toString();
        if(result.equals("0")){
            JOptionPane.showMessageDialog(null,"Error in connection with server");
        }
    }
}
