package userControl;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import jsonContoller.jsonUsers;
import objects.objUsers;

import java.util.List;

public class userController {
    public Label nameLabel,emailLabel,phonenumberLabel,birthdayLabel,bioLabel,
            lastSeen,Privacy,Status,followsYou,mutesYou,blocksYou;
    public Button followerCount,followingCount,blockCount,muteCount;
    public userController(String user) {

        nameLabel = (Label) launch.view.root.lookup("#name_label");
        emailLabel = (Label) launch.view.root.lookup("#email_label");
        phonenumberLabel = (Label) launch.view.root.lookup("#phonenumber_label");
        birthdayLabel = (Label) launch.view.root.lookup("#birthday_label");
        bioLabel = (Label) launch.view.root.lookup("#bio_label");

        lastSeen = (Label) launch.view.root.lookup("#last_seen");
        Privacy = (Label) launch.view.root.lookup("#privacy");
        Status = (Label) launch.view.root.lookup("#status");

        followsYou = (Label) launch.view.root.lookup("#follows_you");
        mutesYou = (Label) launch.view.root.lookup("#mutes_you");
        blocksYou = (Label) launch.view.root.lookup("#blocks_you");

        followsYou.setText(" "+user+" follows you");
        mutesYou.setText(" "+user+" mutes you");
        blocksYou.setText(" "+user+" blocks you");

        if(!userJsonDecoder.followedBy)followsYou.setVisible(false);
        if(!userJsonDecoder.mutedBy)mutesYou.setVisible(false);
        if(!userJsonDecoder.blockdBy)blocksYou.setVisible(false);

        followerCount = (Button) launch.view.root.lookup("#follower_count");
        followingCount = (Button) launch.view.root.lookup("#following_count");
        blockCount = (Button) launch.view.root.lookup("#block_count");
        muteCount = (Button) launch.view.root.lookup("#mute_count");

        if (!this.lastSeen.getText().equals("Online")) {
            lastSeen.getStyleClass().remove("accept");
            lastSeen.getStyleClass().add("username");
        }

        nameLabel.setText(userJsonDecoder.Name);

        emailLabel.setText("Email: "+userJsonDecoder.Email);
        phonenumberLabel.setText("Phone number: "+userJsonDecoder.Phonenumber);
        birthdayLabel.setText("Birthday date: "+userJsonDecoder.Birthday);
        lastSeen.setText(" Last seen: "+userJsonDecoder.lastseen+" ");

        bioLabel.setText("Bio: "+userJsonDecoder.Bio);


        if (userJsonDecoder.account){
            Privacy.getStyleClass().remove("wrong");
            Privacy.setText(" Public ");
        }else{
            Privacy.getStyleClass().remove("accept");
            Privacy.getStyleClass().add("wrong");
            Privacy.setText(" Private ");
        }
        if (userJsonDecoder.enable) {
            Status.getStyleClass().remove("wrong");
            Status.setText(" Account is Enable ");
        }else{
            Status.getStyleClass().remove("accept");
            Status.getStyleClass().add("wrong");
            Status.setText(" Account is Disable ");
        }

    }
}
