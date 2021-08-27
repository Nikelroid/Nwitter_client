package groups;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import objects.objTwitte;
import objects.objUsers;
import userControl.userFinder;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class gpPrint {
    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();

    userFinder uf = new userFinder();
    int target =0;

    AnchorPane Group;
    Label Name, Count;
    public static ArrayList<Button> Members, Messages, Delete,Link;

    jsonTwittes Get = new jsonTwittes();

    private void difiner() throws IOException {

        Group = FXMLLoader.load(getClass().getResource("/layout/cards/group_card.fxml"));

        Name = (Label) Group.lookup("#name");
        Count = (Label) Group.lookup("#count");
        Members.add((Button) Group.lookup("#member"));
        Messages.add((Button) Group.lookup("#message"));
        Delete.add((Button) Group.lookup("#delete"));
        Link.add((Button) Group.lookup("#link"));

    }

    private void setter(String gpName,String id) {
        Name.setText(gpName);
        Count.setText("Serial: "+id);
    }
    private void adder(){
        ScrollPane scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
        VBox twitteList = (VBox) scrollPane.lookup("#twittelist");
        twitteList.getChildren().add(Group);
    }


    public gpPrint(String gpName,String id) throws IOException {

            difiner();
            setter(gpName,id);
            adder();

    }
}
