package twitteControl;


import chat.textModifier;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import org.apache.commons.io.FileUtils;
import profiles.getImageFile;
import submit.submitComment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class commentsController {


    public static ScrollPane scrollPane;
    public static VBox twitteList;
    AnchorPane Twitte,Text;
    Label Sender,Date;
    public static ArrayList<Button> likeList,likeAction,commentList,commentAction,retwitteList,
    retwitteAction, Share,Save,Report,Link;

    twitteModifier twitteModifier = new twitteModifier();
    public static ArrayList<ImageView> twitteImages;


    public commentsController() throws IOException {
        Twitte = FXMLLoader.load(getClass().getResource("/layout/cards/Notwittes.fxml"));
        adder();
    }
    public commentsController(int k) throws IOException {
        if (k==-1) {
            Twitte = FXMLLoader.load(getClass().getResource("/layout/cards/Notwittes.fxml"));
            AnchorPane text = (AnchorPane) Twitte.lookup("#twitte_text");
            ImageView image = (ImageView) Twitte.lookup("#image");

            TextFlow flow = twitteModifier.modifier("User's account is private");
            flow.setPrefWidth(text.getPrefWidth());
            text.getChildren().add(flow);

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

    private void setter(String serial,int counter ,String username,String myText,String date, int likes,int comments,int retwittes,
                        boolean isLiked,boolean isRetwitted,boolean isSaved) {

        TextFlow flow = twitteModifier.modifier(myText);
        flow.setPrefWidth(Text.getPrefWidth());
        Text.getChildren().add(flow);

        Sender.setText(" From: "+username+" ");
        Date.setText(" Date: "+date + " ");
        likeList.get(counter-1).setText(String.valueOf(likes - 1));
        commentList.get(counter-1).setText(String.valueOf(comments - 1));
        retwitteList.get(counter-1).setText(String.valueOf(retwittes - 1));

        try {
            getImageFile getProfileFile = new getImageFile();
            File file = getProfileFile.profile(serial);
            Image image = new Image(file.toURI().toString());
            FileUtils.readFileToByteArray(file);
            twitteImages.get(counter-1).setImage(image);
        } catch (NullPointerException| FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void adder(){
        commentsController.twitteList.getChildren().add(Twitte);
    }


    public commentsController(
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
