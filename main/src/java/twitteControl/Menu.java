package twitteControl;

import graphics.selectIcon;
import javafx.scene.control.Button;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import loadTwittes.jsonDecoder;
import mainPages.*;
import objects.objTwitte;
import objects.objUsers;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import profiles.getImageFile;
import reports.Reports;
import submit.submitComment;
import submit.submitLike;
import submit.submitRetwitte;
import submit.submitShare;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Menu {


    public Menu() {
    }

    jsonUsers Users_get = new jsonUsers();
    List<objUsers> users = Users_get.get();
    jsonTwittes Get = new jsonTwittes();
    List<objTwitte> twittes = Get.get();
    submitLike Like = new submitLike();
    private static final Logger logger = LogManager.getLogger(Menu.class);
    public void Menu_command(String AuthKey) {
        Button Feed = (Button) launch.view.scene.lookup("#feed");
        Button Explorer = (Button) launch.view.scene.lookup("#expelorer");
        Button Home = (Button) launch.view.scene.lookup("#home");
        Button Messenger = (Button) launch.view.scene.lookup("#messenger");
        Button Setting = (Button) launch.view.scene.lookup("#setting");



        Feed.setOnAction(event -> {

            try {
                new deathPages();
                new Feed(AuthKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Explorer.setOnAction(event -> {

            try {
                new deathPages();
                new Expelorer(AuthKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Home.setOnAction(event -> {
            try {
                new deathPages();
                new Info(AuthKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Messenger.setOnAction(event -> {

                try {
                    new deathPages();
                    new Messenger(AuthKey);
                } catch (Exception e) {
                    e.printStackTrace();
            }

        });
        Setting.setOnAction(event -> {
            try {
                new deathPages();
                new Setting(AuthKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void twitteButtons(String AuthKey, String[] mapper, int counter) throws Exception {
            for (int i = 1; i < counter; i++) {
                final int finalI = i;
                TwitteController.likeList.get(i - 1).setOnAction(event -> {
                    new deathPages();
                    try {
                        Like.list(mapper[finalI+1], AuthKey);
                    } catch (IOException e) {
                        logger.error("error in main list of likes");
                    }
                });
                TwitteController.likeAction.get(i - 1).setOnAction(event -> {
                    new submitLike(mapper[finalI+1], AuthKey);
                });
                TwitteController.commentList.get(i - 1).setOnAction(event -> {
                    new deathPages();
                    submitComment Com = new submitComment();
                    try {
                        Com.list(mapper[finalI+1], AuthKey);
                    } catch (IOException e) {
                        logger.error("Error: in loading comment list");
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                TwitteController.commentAction.get(i - 1).setOnAction(event -> {
                    new submitComment(mapper[finalI+1], AuthKey);
                });
                TwitteController.retwitteList.get(i - 1).setOnAction(event -> {
                    new deathPages();
                    submitRetwitte Ret = new submitRetwitte();
                    try {
                        Ret.list(mapper[finalI+1], AuthKey);
                    } catch (IOException e) {
                        logger.error("error in main list of rets");
                    }
                });
                TwitteController.retwitteAction.get(i - 1).setOnAction(event -> {
                    new submitRetwitte(mapper[finalI+1], AuthKey);
                });
                TwitteController.Share.get(i - 1).setOnAction(event -> {

                    try {
                        new submitShare("^( Shared twiite from "+
                                jsonDecoder.usernames.get(finalI-1)+ " ): "+
                                jsonDecoder.text.get(finalI-1), AuthKey);
                    } catch (IOException e) {
                        logger.error("Error in sharing twitte");
                    }
                });
                TwitteController.Save.get(i - 1).setOnAction(event -> {
                    submitShare sS = new submitShare();
                    sS.submitSave(mapper[finalI+1], AuthKey);
                });
                TwitteController.Report.get(i - 1).setOnAction(event -> {
                    String reptext = JOptionPane.showInputDialog("Insert your philosophy of report");
                    if (reptext!=null)
                    new Reports(reptext, mapper[finalI+1], AuthKey);
                });
                TwitteController.twitteImages.get(i-1).setOnMouseClicked(mouseEvent -> {
                    try {
                        getImageFile getProfileFile = new getImageFile();
                        File file = getProfileFile.twitte(mapper[finalI+1]);
                        FileUtils.readFileToByteArray(file);
                          new selectIcon(file);
                    } catch (NullPointerException| FileNotFoundException ignored) {
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
                TwitteController.Link.get(i-1).setOnMouseClicked(mouseEvent -> {
                    StringSelection stringSelection = new StringSelection("#"+mapper[finalI+1]);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                    JOptionPane.showMessageDialog(null,"Link: #"+mapper[finalI+1]+
                            " Added to clipboard");
                });
            }

        }

        public void buttonsStyleSetter ( int counter,boolean isLiked,
                                         boolean isSaved,boolean isRetwitted){
            if (isLiked) {

                TwitteController.likeAction.get(counter - 1).getStyleClass().add("liked");
                TwitteController.likeAction.get(counter - 1).setText("Unlike");
            }
            if (isRetwitted) {
                TwitteController.retwitteAction.get(counter - 1).getStyleClass().add("retwitted");
                TwitteController.retwitteAction.get(counter - 1).setText("Retwitted");
            }

            if (isSaved) {
                TwitteController.Save.get(counter - 1).getStyleClass().add("saved");
                TwitteController.Save.get(counter - 1).setText("Saved");
            }
        }

}
