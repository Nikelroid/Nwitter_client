package groups;

import chat.chatPrinter;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import jsonContoller.jsonMessage;
import jsonContoller.jsonUsers;
import objects.objMessage;
import objects.objUsers;
import userControl.userFinder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class gpPrinter {

    public static ScrollPane scrollPane ;
    public static VBox chatList ;
    public static ArrayList<ImageView> messageImage;
    jsonMessage get_mess = new jsonMessage();
    List<objMessage> messages = get_mess.get();
    AnchorPane Message,Text;
    Label  Name,Date;
    public static Label Seen;
    ImageView Icon,Image;

    jsonUsers getUsers = new jsonUsers();
    List<objUsers> users = getUsers.get();

    public static ArrayList<Button> Forward, Save, Delete,commentAction, Edit, Share;

    private void difinerSender() throws IOException {

        Message = FXMLLoader.load(getClass().getResource("/layout/chat/chat_sender.fxml"));
        Text = (AnchorPane) Message.lookup("#text");
        Name = (Label) Message.lookup("#name");
        Date = (Label) Message.lookup("#date");
        Seen = (Label) Message.lookup("#seen");
        Forward.add((Button) Message.lookup("#forward"));
        Save.add((Button) Message.lookup("#save"));
        Delete.add((Button) Message.lookup("#delete"));
        Edit.add((Button) Message.lookup("#edit"));
        Icon = (ImageView) Message.lookup("#icon");
        Image = (ImageView) Message.lookup("#image");
    }
    private void difinerReciever() throws IOException {

        Message = FXMLLoader.load(getClass().getResource("/layout/chat/chat_reciver.fxml"));
        Text = (AnchorPane) Message.lookup("#text");
        Name = (Label) Message.lookup("#name");
        Date = (Label) Message.lookup("#date");
        Icon = (ImageView) Message.lookup("#icon");

        Forward.add((Button) Message.lookup("#forward"));
        Save.add((Button) Message.lookup("#save"));
        Delete.add((Button) Message.lookup("#delete"));
        Edit.add(new Button());
    }

    private void setterSender(int target,ImageView Image) {
        try {
            Icon.setImage(new Image(gpPrinter.class.getResourceAsStream("/profiles/"+
                    messages.get(target).getSender()+".png")));
        }catch (NullPointerException ignored){
        }
        messageImage.add(Image);
        try {
            messageImage.get(messageImage.size()-1).setImage(new Image(chatPrinter.class.getResourceAsStream("/messages/"+
                    messages.get(target).getId()+".png")));
        }catch (NullPointerException ignored){
            messageImage.get(messageImage.size()-1).setVisible(false);
        }
        chatPrinter.forward(target, messages, Text, Date, Name,Image);
        if(messages.get(target).isSeen()) {
            Seen.setText("Seen");
            Seen.getStyleClass().remove("wrong");
            Seen.getStyleClass().add("accept");
        }
    }
    private void setterReciever(int target) {
        try {
            Icon.setImage(new Image(gpPrinter.class.getResourceAsStream("/profiles/"+
                    messages.get(target).getSender()+".png")));
        }catch (NullPointerException ignored){
        }
        messageImage.add((ImageView) Message.lookup("#image"));
        try {
            messageImage.get(messageImage.size()-1).setImage(new Image(chatPrinter.class.getResourceAsStream("/messages/"+
                    messages.get(target).getId()+".png")));
        }catch (NullPointerException ignored){
            messageImage.get(messageImage.size()-1).setVisible(false);
        }
        chatPrinter.forward(target, messages, Text, Date, Name,Image);
        Edit.get(Edit.size()-1).setVisible(false);
        Delete.get(Delete.size()-1).setVisible(false);
    }
    private void adder(){
        chatList.getChildren().add(Message);
    }

    public gpPrinter(int serial,int type) throws IOException {
        var uf = new userFinder();
        int target = uf.messageFinder(serial);

        if (type ==1 ) {
            difinerSender();
            setterSender(target,Image);
        }else {
            difinerReciever();
            setterReciever(target);
        }
        adder();

    }

}
