package chat;

import checkExists.checkExists;
import groups.groupsConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import jsonContoller.jsonMessage;
import objects.objMessage;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import profiles.getImageFile;
import userControl.userFinder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class chatPrinter {
    public static ScrollPane scrollPane ;
    public static VBox chatList ;
    public static ArrayList<ImageView> messageImage;
    jsonMessage get_mess = new jsonMessage();
    List<objMessage> messages = get_mess.get();
    public static AnchorPane Message,Text;
    ImageView Image;
    Label Name,Date;
    public static Label Seen;
    static checkExists exists = new checkExists();
    static groupsConnection connection = new groupsConnection();
    public static ArrayList<Button> Forward, Save, Delete,commentAction, Edit, Share;
    private static final Logger logger = LogManager.getLogger(chatPrinter.class);

    private void difinerSender(int target) throws IOException {


        Message = FXMLLoader.load(getClass().getResource("/layout/chat/chat_sender.fxml"));
        Text = (AnchorPane) Message.lookup("#text");
        Name = (Label) Message.lookup("#name");
        Date = (Label) Message.lookup("#date");
        Seen = (Label) Message.lookup("#seen");
        Image = (ImageView) Message.lookup("#image");
        iconDefiner(Image,target);
        Edit.add((Button) Message.lookup("#edit"));
    }

    private void iconDefiner(ImageView Image,int target) {
        ImageView Icon = (ImageView) Message.lookup("#icon");
        try {
            getImageFile getProfileFile = new getImageFile();
            File file = getProfileFile.profile(messages.get(target).getSender());
            Image image = new Image(file.toURI().toString());
            FileUtils.readFileToByteArray(file);
            Icon.setImage(image);
        } catch (NullPointerException| FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageImage.add(Image);
        Forward.add((Button) Message.lookup("#forward"));
        Save.add((Button) Message.lookup("#save"));
        Delete.add((Button) Message.lookup("#delete"));
    }

    private void difinerReciever(int target) throws IOException {

        if (messages.get(target).getSender().endsWith("_bot"))
            Message = FXMLLoader.load(getClass().getResource("/layout/chat/chat_reciver_bot.fxml"));
        else
            Message = FXMLLoader.load(getClass().getResource("/layout/chat/chat_reciver.fxml"));

        Text = (AnchorPane) Message.lookup("#text");
        Name = (Label) Message.lookup("#name");
        Date = (Label) Message.lookup("#date");
        Image = (ImageView) Message.lookup("#image");
        iconDefiner(Image,target);
        Edit.add(new Button());
    }

    private void setterSender(int target) {
        checkForward(target);
        Seen.setText("...");
        if(messages.get(target).isSent()) {
            Seen.setText("+");
            Seen.getStyleClass().remove("wrong");
            Seen.getStyleClass().add("accept");
        }
        if(messages.get(target).isDelivered()) {
            Seen.setText("++");
        }
        if(messages.get(target).isSeen()) {
            Seen.setText("+++");
        }
    }
    private void setterReciever(int target) {

        checkForward(target);
    }

    private void checkForward(int target) {
        forward(target, messages, Text, Date, Name,Image);
    }

    public static void forward(int target, List<objMessage> messages, AnchorPane text, Label date, Label name,ImageView imageView) {

        textModifier textModifier = new textModifier();
        TextFlow flow = textModifier.modifier(target);
        flow.setPrefWidth(text.getPrefWidth());
        text.getChildren().add(flow);
        date.setText(" Date: "+ messages.get(target).getTime());
        name.setText(messages.get(target).getSender());

        try {
            getImageFile getProfileFile = new getImageFile();
            File file = getProfileFile.message(messages.get(target).getId()+"");
            Image image = new Image(file.toURI().toString());
            FileUtils.readFileToByteArray(file);
            imageView.setImage(image);
        } catch (NullPointerException| FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adder(){
        chatList.getChildren().add(Message);
    }
    public chatPrinter(int serial,int type) throws IOException {
        var uf = new userFinder();
        int target = uf.messageFinder(serial);
            if (type ==1 ) {
                difinerSender(target);
                setterSender(target);
            }else {
                difinerReciever(target);
                setterReciever(target);
            }
            adder();
    }

}
