package chat;

import graphics.selectIcon;
import launch.authKey;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitMessage;

import javax.swing.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

public class sendMessage {
    submitMessage sb = new submitMessage();
    private static final Logger logger = LogManager.getLogger(sendMessage.class);

    public sendMessage(String messageText) {
        String time = getTime();
        if (time != null) {
            int ID = 0;
            if (selectIcon.ImageIm == null) {
                if (!messageText.isEmpty()) {
                    try {
                        sb.SubMess(messageText, authKey.username, chatPage.userUsername, time);
                    } catch (Exception e) {
                        logger.error("Error in creat a new nwitte");
                    }
                }

            } else {

                byte[] fileContent = new byte[0];
                String encodedString = null;
                try {
                    fileContent = FileUtils.readFileToByteArray(selectIcon.fileIm);
                    encodedString = Base64.getEncoder().encodeToString(fileContent);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                sb.SubMess("~" + messageText.length() + "~" +
                                messageText + encodedString,
                        authKey.username,
                        chatPage.userUsername, time);

                selectIcon.fileIm = null;
                selectIcon.ImageIm = null;
            }
        }
    }

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private String getTime() {
        String TIME = null;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        if (!chatPage.sendNow.isSelected()) {

            String Hour = chatPage.hour.getText();
            String Minute = chatPage.minute.getText();
            String Second = chatPage.second.getText();
            if (Hour.isEmpty() || Integer.parseInt(Hour) < 0 || Integer.parseInt(Hour) > 23
                    || Minute.isEmpty() || Integer.parseInt(Minute) < 0 || Integer.parseInt(Minute) > 59
                    || Second.isEmpty() || Integer.parseInt(Second) < 0 || Integer.parseInt(Second) > 59
                    || chatPage.datePicker.getValue() == null) {
                JOptionPane.showMessageDialog(null, "please enter valid " + "value for time");
                return null;
            } else {
                now = LocalDateTime.now();
                String messageTimeString = chatPage.datePicker.getValue().format(dateFormat) +
                        " " + Hour + ":" + Minute + ":" + Second;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date messageTime = null;
                Date currentTime = null;
                try {
                    messageTime = sdf.parse(messageTimeString);
                    currentTime = sdf.parse(dtf.format(now));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                assert messageTime != null;
                assert currentTime != null;
                if (messageTime.getTime() - 5000 < currentTime.getTime()) {
                    JOptionPane.showMessageDialog(null,
                            "please enter newer time");
                    return null;
                } else {
                    return messageTimeString;
                }

            }
        } else {
            return dtf.format(now);
        }
    }
}
