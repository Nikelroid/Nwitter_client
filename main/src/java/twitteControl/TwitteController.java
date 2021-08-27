package twitteControl;


import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import launch.authKey;
import org.apache.commons.io.FileUtils;
import profiles.getImageFile;
import userControl.userProfile;
import userControl.usersPrint;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class TwitteController {



    AnchorPane Twitte,Text;
    FXMLLoader fXMLLoader = new FXMLLoader();
    Label Sender,Date;
    public static ScrollPane scrollPane;
    public static VBox twitteList;
    public static ArrayList<Button> likeList,likeAction,commentList,commentAction,retwitteList,
    retwitteAction, Share,Save,Report,Link;
    public static ArrayList<ImageView> twitteImages;
    twitteModifier modifier = new twitteModifier();

    public TwitteController() throws IOException {
        Twitte = FXMLLoader.load(getClass().getResource("/layout/cards/Notwittes.fxml"));
        adder();
    }
    public TwitteController(int k) throws IOException {
        if (k==-1) {
            Twitte = FXMLLoader.load(getClass().getResource("/layout/cards/Notwittes.fxml"));
            Label text = (Label) Twitte.lookup("#twitte_text");
            ImageView image = (ImageView) Twitte.lookup("#image");

            text.setText("User's account is private");

            image.setImage(new Image(getClass().getResourceAsStream("/img/lock.png")));
            adder();
        }
    }

    private void difiner() throws IOException {

        Twitte = FXMLLoader.load(getClass().getResource("/layout/cards/twitte.fxml"));
        Text = (AnchorPane) Twitte.lookup("#twitte_text");
        Sender = (Label) Twitte.lookup("#sender");
        Date = (Label) Twitte.lookup("#date");
        likeList.add((Button) Twitte.lookup("#like_l"));
        likeAction.add((Button) Twitte.lookup("#like_a"));
        commentList.add((Button) Twitte.lookup("#comment_l"));
        commentAction.add((Button) Twitte.lookup("#comment_a"));
        retwitteList.add((Button) Twitte.lookup("#ret_l"));
        retwitteAction.add((Button) Twitte.lookup("#ret_a"));
        Share.add((Button) Twitte.lookup("#share"));
        Save.add((Button) Twitte.lookup("#save"));
        Report.add((Button) Twitte.lookup("#report"));
        Link.add((Button) Twitte.lookup("#link"));

        twitteImages.add((ImageView)  Twitte.lookup("#image"));
    }

    private void setter(String serial,int counter ,String username,String text,String date, int likes,int comments,int retwittes,
                        boolean isLiked,boolean isRetwitted,boolean isSaved) {

        TextFlow flow = modifier.modifier(text);
        flow.setPrefWidth(Text.getPrefWidth());
        Text.getChildren().add(flow);
        Sender.setOnMouseClicked(mouseEvent -> {
            authKey.getter();
            try {
                new userProfile(authKey.getter().substring(2),username);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        Sender.setOnMouseEntered(mouseEvent -> {
            launch.view.scene.setCursor(Cursor.HAND);
            Sender.getStyleClass().remove("username");
            Sender.getStyleClass().add("focused");
        });
        Sender.setOnMouseExited(mouseEvent -> {
            launch.view.scene.setCursor(Cursor.DEFAULT);
            Sender.getStyleClass().add("username");
            Sender.getStyleClass().remove("focused");
        });

        Sender.setText(" From: "+username+" ");
        Date.setText(" Date: "+date + " ");
        likeList.get(counter-1).setText(String.valueOf(likes - 1));
        commentList.get(counter-1).setText(String.valueOf(comments - 1));
        retwitteList.get(counter-1).setText(String.valueOf(retwittes - 1));

        try {
            getImageFile getProfileFile = new getImageFile();
            File file = getProfileFile.twitte(serial);
            Image image = new Image(file.toURI().toString());
            FileUtils.readFileToByteArray(file);
            twitteImages.get(counter-1).setImage(image);
        } catch (NullPointerException| FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        Menu menu = new Menu();
        menu.buttonsStyleSetter(counter,isLiked,isSaved,isRetwitted);
    }
    private void adder(){
            twitteList.getChildren().add(Twitte);
    }


    public TwitteController(
            int counter,
            String username,
            String text,
            String date,
            String serial,
            int likes,
            int comments,
            int retwittes,
            boolean isSaved,
            boolean isLiked,
            boolean isRetwitted) throws IOException {


        difiner();
        setter(serial,counter,username,text,date,likes,comments,retwittes,isLiked,isRetwitted,isSaved);
        adder();

    }


}
