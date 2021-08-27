package notifications;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import jsonContoller.jsonNotifs;
import jsonContoller.jsonUsers;
import launch.authKey;
import login.loginConnection;
import mainPages.Info;
import objects.objNotifs;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitRequest;
import twitteControl.deathPages;
import userControl.userFinder;

import javax.swing.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class oRequests {
    private static double vValueFeed;
    int counter = 0;
    int[] mapper = new int[10000];
    notifsConnection connection = new notifsConnection();
    Button Notifs,oReqs,uReqs,Back,Clear;


    public static Timeline updateChecker;
    ArrayList<String> user1 = new ArrayList<>();
    ArrayList<String> user2 = new ArrayList<>();
    ArrayList<Integer> notifType = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();

    public static ScrollPane scrollPane;
    public static VBox twitteList;

    private static final Logger logger = LogManager.getLogger(Notifications.class);
    public oRequests(String AuthKey) throws Exception {

        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/notification_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.setTitle("Nwitter");
        launch.view.stage.show();

        userFinder uf = new userFinder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        ImageView Exit = (ImageView) launch.view.root.lookup("#exit") ;
        Exit.setCursor(Cursor.HAND);
        Exit.setOnMouseClicked(event -> {
            int response = JOptionPane.showConfirmDialog(null,
                    "Do you want to exit the app and stay online?");
            if (response==0){
          new deathPages();
                System.exit(1);
            }else if (response==1) {
                loginConnection loginConnection = new loginConnection();
                loginConnection.connection(AuthKey,"-");
                System.exit(1);
            }
        });

        Notifs = (Button) launch.view.scene.lookup("#notifs") ;
        uReqs = (Button) launch.view.scene.lookup("#u_reqs") ;
        oReqs = (Button) launch.view.scene.lookup("#o_reqs") ;
        Back = (Button) launch.view.scene.lookup("#back") ;
        Clear = (Button) launch.view.scene.lookup("#clear") ;

        uReqs.setOnAction(event -> {
            new deathPages();
            try {
                new Notifications(AuthKey,2);
            } catch (Exception e) {
                logger.error("Error in go to u-reqs notification page ");
            }
        });
        oReqs.setOnAction(event -> {
            new deathPages();
            try {
                new oRequests(AuthKey);
            } catch (Exception e) {
                logger.error("Error in go to o-reqs page ");
            }
        });
        Notifs.setOnAction(event -> {
            new deathPages();
            try {
                new Notifications(AuthKey,1);
            } catch (Exception e) {
                logger.error("Error in refreshing o-req page ");
            }
        });
        Back.setOnAction(event -> {
            new deathPages();
            try {
                new Info(AuthKey);
            } catch (Exception e) {
                logger.error("Error in Back to home from o-req page");
            }
        });
        Label Header = (Label) launch.view.scene.lookup("#header");
        Header.setText("Requests");



        connection.get("get", AuthKey);

        user1 = jsonNotifsDecoder.user1;
        user2 = jsonNotifsDecoder.user2;
        notifType = jsonNotifsDecoder.type;
        id = jsonNotifsDecoder.id;

        updateChecker = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                connection.get("get", AuthKey);
                                if (!id.equals(jsonNotifsDecoder.id)) {
                                    try {
                                        oRequests.updateChecker.stop();
                                        oRequests.vValueFeed = oRequests.scrollPane.getVvalue();
                                        new oRequests(AuthKey);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                }
                            }
                        }));
        updateChecker.setCycleCount(Timeline.INDEFINITE);
        updateChecker.play();

        scrollPane = (ScrollPane) launch.view.scene.lookup("#scobar");
        twitteList = (VBox) scrollPane.lookup("#twittelist");
        twitteList.getChildren().removeAll();

        oReqPrint.Reject=new ArrayList<>();
        oReqPrint.Accept1=new ArrayList<>();
        oReqPrint.Accept2=new ArrayList<>();
        authKey.getter();
        String username = authKey.username;
        for (int i = user1.size() - 1; i >= 0; i--)
            if (notifType.get(i) == 8 && user2.get(i).equals(username)) {
                new oReqPrint(user1.get(i));
                mapper[counter] = i;;
                counter++;
            }
        if(counter!=0) {
            for (int i = 0; i < oReqPrint.Accept1.size(); i++) {
                final int finalI = i;
                oReqPrint.Accept1.get(i).setOnAction(event -> {
                    connection.get("accept1",AuthKey,id.get(mapper[finalI]));
                });
                oReqPrint.Accept2.get(i).setOnAction(event -> {

                    connection.get("accept2",AuthKey,id.get(mapper[finalI]));
                });
                oReqPrint.Reject.get(i).setOnAction(event -> {

                    connection.get("reject",AuthKey,id.get(mapper[finalI]));
                });
            }
            Clear.setOnAction(event -> {
                connection.get("clear",AuthKey);
            });
        }
        Platform.runLater(()->{
            oRequests.scrollPane.setVvalue(oRequests.vValueFeed);
        });
    }


}
