package chat;

import graphics.selectIcon;
import jsonContoller.jsonMessage;
import launch.view;
import lists.jsonListDecoder;
import lists.userSelect;
import objects.objMessage;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitMessage;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class chatContoller {
    public chatContoller() {

    }
    jsonMessage get_mess = new jsonMessage();
    submitMessage submitMessage = new submitMessage();
    List<objMessage> messages = get_mess.get();
    List<String> savedMessages = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(chatContoller.class);
    public chatContoller(String AuthKey,String me, String he){
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getSender().equals(me) && messages.get(i).getReceiver().equals(me)){
                savedMessages.add(messages.get(i).getText());
            }
        }
        for (int i = 0; i < chatPrinter.Save.size(); i++) {
            try{
            final int finalI = i;
            if (savedMessages.contains("(From "+he+") "
                    +messages.get(chatPage.mapper[finalI]).getText())) {
                chatPrinter.Save.get(i).getStyleClass().remove("share");
                chatPrinter.Save.get(i).getStyleClass().add("saved");
                chatPrinter.Save.get(i).setText("Unsave");
            }
            if (!messages.get(chatPage.mapper[finalI]).getText().isEmpty() &&
                    messages.get(chatPage.mapper[finalI]).getText().charAt(0)=='^') {
                chatPrinter.Edit.get(finalI).setVisible(false);
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

            chatPrinter.Save.get(i).setOnAction(event -> {
                LocalDateTime now = LocalDateTime.now();

                if (chatPrinter.Save.get(finalI).getText().equals("Save")) {

                    Path simple = Paths.get("simple.png");
                    Path path = Paths.get(simple.toAbsolutePath().getParent() +
                            "\\main\\src\\resources\\messages\\" +
                            messages.get(chatPage.mapper[finalI]).getId() + ".png");
                    File file = path.toFile();
                    try {
                        byte[] fileContent = new byte[0];

                        fileContent = FileUtils.readFileToByteArray(file);
                        String encodedString = Base64.getEncoder().encodeToString(fileContent);
                        String text = "(From " + he + ") " + messages.get(chatPage.mapper[finalI]).getText();
                        submitMessage.SubMess("~" + text.length() + "~" + text + encodedString, me, me, dtf.format(now));

                    } catch (Exception ignored) {
                        submitMessage.SubMess("(From " + he + ") "
                                + messages.get(chatPage.mapper[finalI]).getText(), me, me, dtf.format(now));
                    }


                    chatPrinter.Save.get(finalI).getStyleClass().remove("share");
                    chatPrinter.Save.get(finalI).getStyleClass().add("saved");
                    chatPrinter.Save.get(finalI).setText("Unsave");
                } else {
                    for (int j = 0; j < messages.size(); j++) {
                        if (messages.get(j).getText().equals("(From " + he + ") "
                                + messages.get(chatPage.mapper[finalI]).getText()))
                            messages.remove(j);
                        new jsonMessage(messages);
                    }
                    chatPrinter.Save.get(finalI).getStyleClass().add("share");
                    chatPrinter.Save.get(finalI).getStyleClass().remove("saved");
                    chatPrinter.Save.get(finalI).setText("Save");
                }

            });
            chatPrinter.Delete.get(i).setOnAction(event -> {
                if (messages.get(chatPage.mapper[finalI]).getSender().endsWith("_bot")){
                    new sendMessage("/"+chatPage.messageText.getText());
                }else {
                    int input = JOptionPane.showConfirmDialog(null, "" +
                            "Are you sure to delete this message ?");
                    if (input == 0) {
                        messages.remove(chatPage.mapper[finalI]);
                        new jsonMessage(messages);
                    }
                    if (me.equals(he)) {
                        for (int j = 0; j < messages.size(); j++) {
                            if (messages.get(j).getSender().equals(me)
                                    && messages.get(j).getReceiver().equals(me)
                                    && messages.get(j).getText().equals(
                                    "(From " + he + ") " + messages.remove(chatPage.mapper[finalI]).getText())) {
                                messages.remove(j);
                                break;
                            }
                        }
                    }
                    chatPrinter.chatList.getChildren().remove(finalI);
                }
            });

            chatPrinter.Forward.get(i).setOnAction(event -> {

                    userSelect.select = new ArrayList<>();
                    try {
                        new userSelect(AuthKey,me);
                    } catch (IOException e) {
                        logger.error("Error in loading user select page from mass messenger");
                    }
                    boolean sent = false;
                    for (int z = 0; z < userSelect.select.size(); z++) {
                        LocalDateTime now = LocalDateTime.now();

                        if (userSelect.select.get(z).isSelected()){
                            var sb = new submitMessage();
                            Path simple = Paths.get("simple.png");
                            Path path = Paths.get(simple.toAbsolutePath().getParent()+
                                    "\\main\\src\\resources\\messages\\"+
                                    messages.get(chatPage.mapper[finalI]).getId()+".png");
                            File file = path.toFile();
                            try {
                                byte[] fileContent = new byte[0];

                                fileContent = FileUtils.readFileToByteArray(file);
                                String  encodedString = Base64.getEncoder().encodeToString(fileContent);
                                String text = "Forwarded message for "
                                        + messages.get(chatPage.mapper[finalI]).getSender()+ " to "+
                                        messages.get(chatPage.mapper[finalI]).getReceiver() + " : "+
                                        messages.get(chatPage.mapper[finalI]).getText();
                                submitMessage.SubMess("^~"+text.length()+"~"+text+encodedString
                                        ,me, jsonListDecoder.username.get(z)
                                        ,dtf.format(now));

                            }catch (Exception ignored){
                                sb.SubMess("^Forwarded message for "
                                                + messages.get(chatPage.mapper[finalI]).getSender()+ " to "+
                                                messages.get(chatPage.mapper[finalI]).getReceiver() + " : "+
                                                messages.get(chatPage.mapper[finalI]).getText(),
                                        me,
                                        jsonListDecoder.username.get(z),
                                        dtf.format(now));
                            }

                            sent = true;
                        }
                    }
                    if (sent) {
                        JOptionPane.showMessageDialog(null, "Messages sent to selected followings!");
                    }

            });
            chatPrinter.Edit.get(i).setOnAction(event -> {

                chatPage.messageText.setText(messages.get(chatPage.mapper[finalI]).getText());
                chatPage.send.setText("Edit");
                chatPage.picture.setVisible(false);
                chatPage.send.setOnAction(event1 -> {
                    if (!chatPage.messageText.getText().isEmpty() &&
                            !chatPage.messageText.getText().equals(messages.get(chatPage.mapper[finalI]).getText())){
                        messages.get(chatPage.mapper[finalI]).setText(chatPage.messageText.getText());
                        new jsonMessage(messages);
                    }
                });
                view.scene.setOnKeyPressed(keyEvent -> {
                    if (!chatPage.messageText.getText().isEmpty() &&
                            !chatPage.messageText.getText().equals(messages.get(chatPage.mapper[finalI]).getText())){
                        messages.get(chatPage.mapper[finalI]).setText(chatPage.messageText.getText());
                        new jsonMessage(messages);
                    }
                });
            });

            chatPrinter.messageImage.get(i).setOnMouseClicked(mouseEvent -> {
                Path simple = Paths.get("simple.png");
                Path path = Paths.get(simple.toAbsolutePath().getParent()+
                        "\\main\\src\\resources\\messages\\"+
                        messages.get(chatPage.mapper[finalI]).getId()+".png");
                File file = path.toFile();
                try {
                    FileUtils.readFileToByteArray(file);
                    new selectIcon(file);
                }catch (Exception ignored){

                }
            });
            }catch(Exception e){

            }

        }

    }
}
