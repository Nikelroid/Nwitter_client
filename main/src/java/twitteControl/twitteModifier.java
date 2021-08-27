package twitteControl;

import chat.chatPage;
import checkExists.checkExists;
import groups.groupsConnection;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jsonContoller.jsonMessage;
import launch.authKey;
import objects.objMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import twitteHyperlink.showHyperlink;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class twitteModifier {
    private static final Logger logger = LogManager.getLogger(twitteModifier.class);
    static checkExists exists = new checkExists();
    static groupsConnection connection = new groupsConnection();
    jsonMessage get_mess = new jsonMessage();
    public TextFlow modifier(String myText) {



        List<Text> texts = new ArrayList<>();
        List<Hyperlink> chatLinks = new ArrayList<>();
        List<Hyperlink> twitteLinks = new ArrayList<>();
        List<Hyperlink> groupLinks = new ArrayList<>();
        List<Integer> order = new ArrayList<>();
        TextFlow flow = new TextFlow();

        int pointer = 0;
        for (int i = 0; i < myText.length(); i++) {
            if (myText.charAt(i)=='@') {
                String part = myText.substring(pointer, i);
                texts.add(new Text(part));
                order.add(0);
                pointer=i;

                while ( i<myText.length() && myText.charAt(i) != ' ') {
                    i++;
                }
                chatLinks.add(new Hyperlink(myText.substring(pointer,i)));
                order.add(1);
                pointer=i;
            }
            if(i==myText.length())break;
            if (myText.charAt(i)=='#') {
                String part = myText.substring(pointer, i);
                texts.add(new Text(part));
                order.add(0);
                pointer=i;

                while ( i<myText.length() && myText.charAt(i) != ' ') {
                    i++;
                }
                twitteLinks.add(new Hyperlink(myText.substring(pointer,i)));
                order.add(2);
                pointer=i;
            }
            if(i==myText.length())break;
            if (myText.charAt(i)=='$') {
                String part = myText.substring(pointer, i);
                texts.add(new Text(part));
                order.add(0);
                pointer=i;

                while ( i<myText.length() && myText.charAt(i) != ' ') {
                    i++;
                }
                groupLinks.add(new Hyperlink(myText.substring(pointer,i)));
                order.add(3);
                pointer=i;
            }
        }
        texts.add(new Text(myText.substring(pointer)));
        order.add(0);

        String AuthKey = authKey.getter().substring(2);
        String username = authKey.username;

        for (int i = 0; i < chatLinks.size(); i++) {
            final int finalI = i;
            chatLinks.get(i).setOnAction(event -> {
                chatLink(chatLinks,finalI,AuthKey,username);
            });
        }
        for (int i = 0; i < twitteLinks.size(); i++) {
            final int finalI = i;
            twitteLinks.get(i).setOnAction(event -> {
                try {
                    twitteLink(twitteLinks,finalI);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        }
        for (int i = 0; i < groupLinks.size(); i++) {
            final int finalI = i;
            groupLinks.get(i).setOnAction(event -> {
                try {
                    groupLink(groupLinks,finalI,AuthKey,username);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        }
        int textCount = 0;
        int chatLinkCount = 0;
        int twitteLinkCount = 0;
        int groupLinkCount = 0;
        for (int i = 0; i < order.size(); i++) {
            switch (order.get(i)){
                case 0 -> {
                    texts.get(textCount).setFont(Font.font("Bodoni MT Condensed",16));
                    flow.getChildren().add(texts.get(textCount));
                    textCount++;
                }
                case 1-> {
                    chatLinks.get(chatLinkCount).setFont(Font.font("Bodoni MT Condensed",15));
                    flow.getChildren().add(chatLinks.get(chatLinkCount));
                    chatLinkCount++;
                }
                case 2 -> {
                    twitteLinks.get(twitteLinkCount).setFont(Font.font("Bodoni MT Condensed",15));
                    flow.getChildren().add(twitteLinks.get(twitteLinkCount));
                    twitteLinkCount++;
                }
                case 3 -> {
                    groupLinks.get(groupLinkCount).setFont(Font.font("Bodoni MT Condensed",15));
                    flow.getChildren().add(groupLinks.get(groupLinkCount));
                    groupLinkCount++;
                }
            }
        }
       return flow;
    }
    private static void chatLink(List<Hyperlink> chatLinks, int finalI, String AuthKey, String username){
        if (!chatLinks.get(finalI).getText().substring(1).startsWith("gp_")) {
            if (exists.connection("username", chatLinks.get(finalI).getText().substring(1)) == 1){
                JOptionPane.showMessageDialog(null, "User doesn't exists!");
            }else {
                try {
                    new deathPages();
                    new chatPage(AuthKey, username, chatLinks.get(finalI).getText().substring(1));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }else{
            int response = exists.connection("group", chatLinks.get(finalI).getText().substring(4) ,AuthKey);
            if (response== 1){
                JOptionPane.showMessageDialog(null, "Group doesn't exists!");
            }else if(response == 2) {
                JOptionPane.showMessageDialog(null, "You aren't member of this group!");
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
    private static void twitteLink (List<Hyperlink> twitteLinks, int finalI) throws Exception {
        new showHyperlink(twitteLinks.get(finalI).getText().substring(1));
    }
    private static void groupLink (List<Hyperlink> groupLinks, int finalI,String AuthKey,String username) throws Exception {
        JSONObject resultJson = connection.getter("addMembersLink",AuthKey,
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
}
