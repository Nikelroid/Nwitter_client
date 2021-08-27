package chat;

import javafx.scene.control.Hyperlink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import submit.submitMessage;
import twitteControl.deathPages;
import twitteHyperlink.showHyperlink;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class textModifierHyperlinkSetOnAction {
    private static final submit.submitMessage submitMessage = new submitMessage();
    private static final Logger logger = LogManager.getLogger(textModifierHyperlinkSetOnAction.class);
    static void botLink (List<Hyperlink> botLinks, int finalI, String sender, String username, String AuthKey) throws Exception {
    if (sender.endsWith("_bot")) {
        StringSelection stringSelection = new StringSelection(botLinks.get(finalI).getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        JOptionPane.showMessageDialog(null, "Robot link: " +
                botLinks.get(finalI).getText() + " Added to clipboard");
    }else{
        String link = botLinks.get(finalI).getText();
        String botId  = null;
        for (int i = 0; i <link.length() ; i++) {
            if (link.charAt(i)=='/'){
                botId = link.substring(1,i);
                link = link.substring(i+1);
                break;
            }
        }
        if (textModifier.exists.connection("username", botId) == 1){
            logger.error("Bot is not valid");
            JOptionPane.showMessageDialog(null, "Bot doesn't exists!");
        }else {
            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                submitMessage.SubMess("%"+link,username,botId,dtf.format(now));
                new deathPages();
                new chatPage(AuthKey, username, botId);
            } catch (Exception exception) {
                logger.error("Link is not valid");
                exception.printStackTrace();
            }
        }
    }

    }

    static void groupLink(List<Hyperlink> groupLinks, int finalI, String AuthKey, String username) throws Exception {
        JSONObject resultJson = textModifier.connection.getter("addMembersLink", AuthKey,
                groupLinks.get(finalI).getText().substring(1));
        switch (resultJson.getString("result")) {
            case "0" -> {
                logger.error("Error in add you to category");
                JOptionPane.showMessageDialog(null, "error in connection");
            }
            case "1" -> {
                new deathPages();
                new chatPage(AuthKey, username, "*" + resultJson.get("name").toString());
            }
            case "2" -> {
               logger.error("user exists in gp");
                JOptionPane.showMessageDialog(null, "You already joined " +
                        resultJson.get("name").toString() + " group");
            }
            case "3" -> {
                logger.error("Link is not valid");
                JOptionPane.showMessageDialog(null, "invite link expired");
            }
        }
    }

    static void chatLink(List<Hyperlink> chatLinks, int finalI, String AuthKey, String username){
        if (!chatLinks.get(finalI).getText().substring(1).startsWith("gp_")) {
            if (textModifier.exists.connection("username", chatLinks.get(finalI).getText().substring(1)) == 1){
                JOptionPane.showMessageDialog(null, "User doesn't exists!");
                logger.error("User doesn't exists!");
            }else {
                try {
                    new deathPages();
                    new chatPage(AuthKey, username, chatLinks.get(finalI).getText().substring(1));
                } catch (Exception exception) {
                    logger.error("Error in loading chat");
                    exception.printStackTrace();
                }
            }
        }else{
            int response = textModifier.exists.connection("group", chatLinks.get(finalI).getText().substring(4) ,AuthKey);
            if (response== 1){
                logger.error("group doesn't exists");
                JOptionPane.showMessageDialog(null, "Group doesn't exists!");
            }else if(response == 2) {
                JOptionPane.showMessageDialog(null, "You aren't member of this group!");
                logger.error("group is private");
            }else{
                try {
                    new deathPages();
                    new chatPage(AuthKey, username,
                            "*"+chatLinks.get(finalI).getText().substring(4));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    static void twitteLink(List<Hyperlink> twitteLinks, int finalI) throws Exception {
        new showHyperlink(twitteLinks.get(finalI).getText().substring(1));
    }
}
