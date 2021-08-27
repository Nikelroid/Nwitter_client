package submit;

import comments.commentsPage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import loadTwittes.loadConnection;
import objects.objTwitte;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitteControl.Newtwitte;
import twitteControl.TwitteController;
import userControl.userFinder;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class submitComment {

    Newtwitte newTwitte = new Newtwitte();
    private static final Logger logger = LogManager.getLogger(submitComment.class);

    public submitComment() {
    }

    public submitComment(String serial, String AuthKey) {
        String twitteText = JOptionPane.showInputDialog("Input comment text");
        if (twitteText!=null) {
            try {
                newTwitte.newComment(twitteText, AuthKey, serial);
            } catch (Exception e) {
                logger.error("Error in creat a new comment");
            }
        }
    }


    public void list(String serial, String AuthKey) throws Exception {
        if (commentsPage.updateChecker != null) {
            commentsPage.updateChecker.stop();
        }
        new commentsPage(serial, AuthKey, false);

    }
}
