package chat;

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
import profiles.messageSaver;

import java.util.ArrayList;
import java.util.List;

public class textModifier {
    private static final Logger logger = LogManager.getLogger(textModifier.class);

    static checkExists exists = new checkExists();
    static groupsConnection connection = new groupsConnection();

    jsonMessage get_mess = new jsonMessage();
    List<objMessage> messages = get_mess.get();

    public TextFlow modifier(int target) {

        String myText = messages.get(target).getText();



        if(!messages.get(target).getText().isEmpty() && messages.get(target).getText().charAt(0)=='^') {
            myText = messages.get(target).getText().substring(1);
        }
        int last = 1;
        if (!myText.isEmpty() && (myText.charAt(0)=='~')) {
            while (myText.charAt(last)!='~'){
                last++;
            }
            int myChar = Integer.parseInt(myText.substring(1,last));
            myText = myText.substring(last+1,last+myChar+1);

        }


        List<Text> texts = new ArrayList<>();
        List<Hyperlink> chatLinks = new ArrayList<>();
        List<Hyperlink> twitteLinks = new ArrayList<>();
        List<Hyperlink> groupLinks = new ArrayList<>();
        List<Hyperlink> botLinks = new ArrayList<>();
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
            if(i==myText.length())break;
            if (myText.charAt(i)=='%') {
                String part = myText.substring(pointer, i);
                texts.add(new Text(part));
                order.add(0);
                pointer=i;

                while ( i<myText.length() && myText.charAt(i) != ' ') {
                    i++;
                }
                botLinks.add(new Hyperlink(myText.substring(pointer,i)));
                order.add(4);
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
                textModifierHyperlinkSetOnAction.chatLink(chatLinks,finalI,AuthKey,username);
            });
        }
        for (int i = 0; i < twitteLinks.size(); i++) {
            final int finalI = i;
            twitteLinks.get(i).setOnAction(event -> {
                try {
                    textModifierHyperlinkSetOnAction.twitteLink(twitteLinks,finalI);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        }
        for (int i = 0; i < groupLinks.size(); i++) {
            final int finalI = i;
            groupLinks.get(i).setOnAction(event -> {
                try {
                    textModifierHyperlinkSetOnAction.groupLink(groupLinks,finalI,AuthKey,username);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        }
        for (int i = 0; i < botLinks.size(); i++) {
            final int finalI = i;
            botLinks.get(i).setOnAction(event -> {
                try {
                    textModifierHyperlinkSetOnAction.botLink(botLinks,finalI,messages.get(target).getSender(),username,AuthKey);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        }
        int textCount = 0;
        int chatLinkCount = 0;
        int twitteLinkCount = 0;
        int groupLinkCount = 0;
        int botLinkCount = 0;
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
                case 4 -> {
                    botLinks.get(botLinkCount).setFont(Font.font("Bodoni MT Condensed",15));
                    flow.getChildren().add(botLinks.get(botLinkCount));
                    botLinkCount++;
                }
            }
        }
       return flow;
    }

}
