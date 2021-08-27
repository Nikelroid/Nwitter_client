package comments;

import graphics.selectIcon;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import profiles.getImageFile;
import reports.Reports;
import submit.submitComment;
import submit.submitLike;
import submit.submitRetwitte;
import submit.submitShare;
import twitteControl.TwitteController;
import twitteControl.commentsController;
import twitteControl.deathPages;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class commentButtons {
    private static final Logger logger = LogManager.getLogger(commentButtons.class);
    public void buttonCommandSetter(String serial, String AuthKey, Button Back, int counter,String[] mapper) {

        for (int i = 1; i < counter; i++) {
            final int finalI = i;
            commentsController.likeList.get(i - 1).setOnAction(event -> {
                new deathPages();
                try {
                    submitLike Like = new submitLike();
                    Like.list(mapper[finalI + 1], AuthKey);
                } catch (IndexOutOfBoundsException | IOException error) {
                    JOptionPane.showMessageDialog(null, "This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, AuthKey,true);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            commentsController.likeAction.get(i - 1).setOnAction(event -> {
                try {
                    new submitLike(mapper[finalI + 1], AuthKey);
                } catch (IndexOutOfBoundsException error) {
                    JOptionPane.showMessageDialog(null, "This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, AuthKey,true);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            commentsController.commentList.get(i - 1).setOnAction(event -> {
                new deathPages();
                try {
                    submitComment Com = new submitComment();
                    try {
                        Com.list(mapper[finalI + 1], AuthKey);
                    } catch (IOException e) {
                        logger.error("Error: in loading comment list");
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (IndexOutOfBoundsException error) {
                    JOptionPane.showMessageDialog(null, "This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, AuthKey,true);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            commentsController.commentAction.get(i - 1).setOnAction(event -> {
                new submitComment(mapper[finalI+1], AuthKey);
            });
            commentsController.retwitteList.get(i - 1).setOnAction(event -> {
                new deathPages();
                try {
                    submitRetwitte Ret = new submitRetwitte();
                    Ret.list(mapper[finalI + 1], AuthKey);
                } catch (IndexOutOfBoundsException | IOException error) {
                    JOptionPane.showMessageDialog(null, "This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, AuthKey,true);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            TwitteController.twitteImages.get(i-1).setOnMouseClicked(mouseEvent -> {
                try {
                    getImageFile getProfileFile = new getImageFile();
                    File file = getProfileFile.profile(mapper[finalI+1]);
                    if (file!=null)
                        new selectIcon(file);
                } catch (NullPointerException| FileNotFoundException ignored) {
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            commentsController.retwitteAction.get(i - 1).setOnAction(event -> {
                try {
                    new submitRetwitte(mapper[finalI + 1], AuthKey);
                } catch (IndexOutOfBoundsException error) {
                    JOptionPane.showMessageDialog(null, "This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, AuthKey,true);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            commentsController.Share.get(i - 1).setOnAction(event -> {
                try {
                    new submitShare(mapper[finalI + 1], AuthKey);
                } catch (IndexOutOfBoundsException | IOException error) {
                    JOptionPane.showMessageDialog(null, "This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, AuthKey,true);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            commentsController.Save.get(i - 1).setOnAction(event -> {
                try {
                    submitShare sS = new submitShare();
                    sS.submitSave(mapper[finalI + 1], AuthKey);
                } catch (IndexOutOfBoundsException error) {
                    JOptionPane.showMessageDialog(null, "This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, AuthKey,true);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            commentsController.Report.get(i - 1).setOnAction(event -> {
                try {
                    String reptext = JOptionPane.showInputDialog("Insert your philosophy of report");
                    if (reptext!=null)
                        new Reports(reptext, mapper[finalI + 1], AuthKey);
                } catch (IndexOutOfBoundsException error) {
                    JOptionPane.showMessageDialog(null, "This page expired. " +
                            "Do you want to reload?");
                    Back.getScene().getWindow().hide();
                    try {
                        new commentsPage(serial, AuthKey,true);
                    } catch (Exception e) {
                        logger.error("Reload comments page hanged");
                    }
                }

            });
            commentsController.Link.get(i-1).setOnMouseClicked(mouseEvent -> {
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
            commentsController.likeAction.get(counter - 1).getStyleClass().add("liked");
            commentsController.likeAction.get(counter - 1).setText("Unlike");
        }
        if (isRetwitted) {
            commentsController.retwitteAction.get(counter - 1).getStyleClass().add("retwitted");
            commentsController.retwitteAction.get(counter - 1).setText("Retwitted");
        }

        if (isSaved) {
            commentsController.Save.get(counter - 1).getStyleClass().add("saved");
            commentsController.Save.get(counter - 1).setText("Saved");
        }
    }
}
