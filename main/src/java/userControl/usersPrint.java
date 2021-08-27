package userControl;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jsonContoller.jsonTwittes;
import org.apache.commons.io.FileUtils;
import profiles.getImageFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class usersPrint {

        public static ArrayList<AnchorPane> user;
        int target =0;
        ImageView Icon;
        Label Username,nonSeen;
        public static ArrayList<Button> Delete;

        jsonTwittes Get = new jsonTwittes();

        private void difiner(int counter) throws IOException {

            user.add(FXMLLoader.load(getClass().getResource("/layout/cards/user_messanger_card.fxml")));
            Username = (Label) user.get(counter).lookup("#username");
            nonSeen = (Label) user.get(counter).lookup("#nonseen");
            Delete.add((Button) user.get(counter).lookup("#delete"));
            Icon= (ImageView) user.get(counter).lookup("#icon");
        }

        private void setter(String username,int nonseen) {
            if (username.startsWith("Group ")){
                Icon.setImage(new Image(usersPrint.class.getResourceAsStream("/img/group.png")));
        }else {
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
            }
            Username.setText(username);
            nonSeen.setText(" "+nonseen+" ");
        }
        private void adder(int counter){
            ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
            VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
            twitteList.getChildren().add(user.get(counter));
        }


        public usersPrint(String username,int nsm,int counter) throws IOException {

                difiner(counter);
                setter(username,nsm);
                adder(counter);
        }

    }

