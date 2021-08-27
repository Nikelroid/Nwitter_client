package lists;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jsonContoller.jsonUsers;
import objects.objUsers;
import profiles.getImageFile;
import userControl.userFinder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class listPrinter {

    AnchorPane card;
    FXMLLoader fXMLLoader = new FXMLLoader();
    Label Username ,Name;
    public static ScrollPane scrollPane;
    public static VBox twitteList;
    ImageView Icon;
    public static ArrayList<Button> followButton;


    jsonUsers us = new jsonUsers();
    List<objUsers> users = us.get();

    public listPrinter() {
    }

    protected void difiner() throws IOException {
        card = FXMLLoader.load(getClass().getResource("/layout/cards/user_card.fxml"));
        followButton.add((Button) card.lookup("#follow"));
        Username= (Label) card.lookup("#username");
        Name= (Label) card.lookup("#name");
        Icon = (ImageView) card.lookup("#icon");

    }

    private void setter(String username,String name,String pic) {
        try {
            if (!pic.isEmpty()) {
                getImageFile getProfileFile = new getImageFile();
                File file = getProfileFile.profile(username);
                Image image = new Image(file.toURI().toString());
                Icon.setImage(image);
            }
        } catch (NullPointerException ignored) {
        }
       Username.setText(username);
       Name.setText(name);
    }
    private void adder(){

        twitteList.getChildren().add(card);
    }



    public listPrinter(String username,String name,String pic) throws IOException {
        userFinder uf = new userFinder();
        difiner();
        setter(username,name,pic);
        adder();
    }
}
