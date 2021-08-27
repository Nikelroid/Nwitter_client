package category;

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
import java.util.ArrayList;
import java.util.List;

public class categoryPrint {

    jsonUsers get_users = new jsonUsers();
    List<objUsers> users = get_users.get();

    userFinder uf = new userFinder();
    int target =0;

    AnchorPane Category;
    Label Name, Count;
    public static ArrayList<Button> Members, Messages, Delete;

    jsonTwittes Get = new jsonTwittes();
    List<objTwitte> twittes = Get.get();
    jsonUsers us = new jsonUsers();

    private void difiner() throws IOException {

        Category = FXMLLoader.load(getClass().getResource("/layout/cards/category_card.fxml"));

        Name = (Label) Category.lookup("#name");
        Count = (Label) Category.lookup("#count");
        Members.add((Button) Category.lookup("#member"));
        Messages.add((Button) Category.lookup("#message"));
        Delete.add((Button) Category.lookup("#delete"));

    }

    private void setter(String name,int count) {
        Name.setText(name);
        Count.setText(count + " members");
    }
    private void adder(){
        category.Category.twitteList.getChildren().add(Category);
    }


    public categoryPrint(String AuthKey,ArrayList<String> categoryNames,
                         ArrayList<Integer> categoryCounts) throws IOException {
        for (int i = 0; i < categoryNames.size(); i++) {
            difiner();
            setter(categoryNames.get(i),categoryCounts.get(i));
            adder();
        }
    }

}
