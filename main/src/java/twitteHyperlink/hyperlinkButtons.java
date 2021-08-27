package twitteHyperlink;

import graphics.selectIcon;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextFlow;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import profiles.getImageFile;
import reports.Reports;
import submit.submitComment;
import submit.submitLike;
import submit.submitRetwitte;
import submit.submitShare;
import twitteControl.deathPages;
import twitteControl.twitteModifier;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class hyperlinkButtons {


    submitLike Like = new submitLike();

    private static final Logger logger = LogManager.getLogger(hyperlinkButtons.class);

    public hyperlinkButtons(String finalSerial, String AuthKey, String username, String text
            , boolean isLiked, boolean isRetwitted, boolean isSaved) {
        showHyperlink.likeList.setOnAction(event -> {
            new deathPages();
            try {
                Like.list(finalSerial, AuthKey);
            } catch (IOException e) {
                logger.error("error in main list of likes");
            }
        });
        showHyperlink.likeAction.setOnAction(event -> {
            new submitLike(finalSerial, AuthKey);
        });
        showHyperlink.commentList.setOnAction(event -> {
            new deathPages();
            submitComment Com = new submitComment();
            try {
                Com.list(finalSerial, AuthKey);
            } catch (IOException e) {
                logger.error("Error: in loading comment list");
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        showHyperlink.commentAction.setOnAction(event -> {
            new submitComment(finalSerial, AuthKey);
        });
        showHyperlink.retwitteList.setOnAction(event -> {
            new deathPages();
            submitRetwitte Ret = new submitRetwitte();
            try {
                Ret.list(finalSerial, AuthKey);
            } catch (IOException e) {
                logger.error("error in main list of rets");
            }
        });
        showHyperlink.retwitteAction.setOnAction(event -> {
            new submitRetwitte(finalSerial, AuthKey);
        });
        showHyperlink.Share.setOnAction(event -> {

            try {
                new submitShare("^( Shared twiite from " +
                        username + " ): " +
                        text, AuthKey);
            } catch (IOException e) {
                logger.error("Error in sharing twitte");
            }
        });
        showHyperlink.Save.setOnAction(event -> {
            submitShare sS = new submitShare();
            sS.submitSave(finalSerial, AuthKey);
        });
        showHyperlink.Report.setOnAction(event -> {
            String reptext = JOptionPane.showInputDialog("Insert your philosophy of report");
            if (reptext!=null)

                new Reports(reptext, finalSerial, AuthKey);
        });
        showHyperlink.twitteImages.setOnMouseClicked(mouseEvent -> {
            try {
                getImageFile getProfileFile = new getImageFile();
                File file = getProfileFile.twitte(finalSerial);
                FileUtils.readFileToByteArray(file);
                new selectIcon(file);
            } catch (NullPointerException | FileNotFoundException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        showHyperlink.Link.setOnMouseClicked(mouseEvent -> {
            StringSelection stringSelection = new StringSelection("#" + finalSerial);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            JOptionPane.showMessageDialog(null, "Link: #" + finalSerial +
                    " Added to clipboard");
        });
        if (isLiked) {

            showHyperlink.likeAction.getStyleClass().add("liked");
            showHyperlink.likeAction.setText("Unlike");
        }
        if (isRetwitted) {
            showHyperlink.retwitteAction.getStyleClass().add("retwitted");
            showHyperlink.retwitteAction.setText("Retwitted");
        }

        if (isSaved) {
            showHyperlink.Save.getStyleClass().add("saved");
            showHyperlink.Save.setText("Saved");
        }
    }

    public static void setter(javafx.scene.control.Label date1, javafx.scene.control.Label sender, AnchorPane text1, String serial, String username, String text, String date,
                              int likes, int comments, int retwittes) {

        twitteModifier modifier = new twitteModifier();
        TextFlow flow = modifier.modifier(text);
        flow.setPrefWidth(text1.getPrefWidth());
        text1.getChildren().add(flow);

        sender.setText(" From: " + username + " ");
        date1.setText(" Date: " + date + " ");
        showHyperlink.likeList.setText(String.valueOf(likes - 1));
        showHyperlink.commentList.setText(String.valueOf(comments - 1));
        showHyperlink.retwitteList.setText(String.valueOf(retwittes - 1));

        try {
            getImageFile getProfileFile = new getImageFile();
            File file = getProfileFile.twitte(serial);
            javafx.scene.image.Image image = new Image(file.toURI().toString());
            FileUtils.readFileToByteArray(file);
            showHyperlink.twitteImages.setImage(image);
        } catch (NullPointerException | FileNotFoundException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
