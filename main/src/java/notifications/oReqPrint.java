package notifications;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import jsonContoller.jsonNotifs;
import jsonContoller.jsonUsers;
import objects.objNotifs;
import org.apache.commons.io.FileUtils;
import profiles.getImageFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class oReqPrint {

        public static AnchorPane user;
        jsonNotifs Not = new jsonNotifs();
        List<objNotifs> notifs = Not.get();
        jsonUsers get_users = new jsonUsers();
        ImageView Icon;
        Label Text, Username;
        public static ArrayList<Button> Reject,Accept1,Accept2;

        private void difiner() throws IOException {

            user = FXMLLoader.load(getClass().getResource("/layout/cards/reqs_card.fxml"));
            Username = (Label) user.lookup("#username");
            Reject.add((Button) user.lookup("#reject"));
            Accept1.add((Button) user.lookup("#accept1"));
            Accept2.add((Button) user.lookup("#accept2"));
            Icon = (ImageView)  user.lookup("#icon");

        }

        private void setter(String username) {
            try {
                getImageFile getProfileFile = new getImageFile();
                File file = getProfileFile.profile(username);
                Image image = new Image(file.toURI().toString());
                FileUtils.readFileToByteArray(file);
                Icon.setImage(image);
            } catch (NullPointerException | FileNotFoundException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }
            Username.setText(username);
            }


        private void adder(){
            oRequests.twitteList.getChildren().add(user);
        }


        public oReqPrint(String username) throws IOException {
            difiner();
            setter(username);
            adder();
        }

}
