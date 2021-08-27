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
import loadTwittes.jsonDecoder;
import loadTwittes.loadConnection;
import login.loginConnection;
import mainPages.Feed;
import mainPages.Info;
import objects.objNotifs;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitRequest;
import twitteControl.TwitteController;
import twitteControl.deathPages;
import userControl.userFinder;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Notifications {
    public static ScrollPane scrollPane;
    public static VBox twitteList;
    private static double vValueFeed;
    int counter = 0;
    String username;
    int[] mapper1 = new int[10000];
    int[] mapper2 = new int[10000];

    int counter1 = 0;
    int counter2 = 0;

    Button Notifs,oReqs,uReqs,Back,Clear;
    public static Timeline updateChecker;
    ArrayList<String> user1 = new ArrayList<>();
    ArrayList<String> user2 = new ArrayList<>();
    ArrayList<Integer> notifType = new ArrayList<>();
    ArrayList<String> id = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger(Notifications.class);
    public Notifications(String AuthKey,int type) throws Exception {

        authKey authKey = new authKey();
        username = launch.authKey.username;

        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/notification_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
        launch.view.stage.setTitle("Nwitter");
        launch.view.stage.show();

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

        Notifs.setOnAction(event -> {
            new deathPages();
            try {
                new Notifications(AuthKey,1);
            } catch (Exception e) {
                logger.error("Error in refreshing notification page ");
            }
        });
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
                e.printStackTrace();
                logger.error("Error in go to o-reqs page ");
            }
        });
        Back.setOnAction(event -> {
            new deathPages();
            try {
                new Info(AuthKey);
            } catch (Exception e) {
                logger.error("Error in Back to home from notifs page");
            }
        });

        Label Header = (Label) launch.view.scene.lookup("#header");
        if (type==1)
        Header.setText("Notifications");
        else
            Header.setText("Your requests");
        Notifs(AuthKey,type);
    }

    public void Notifs(String AuthKey,int type) throws Exception {



        notifsConnection connection = new notifsConnection();

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
                                        Notifications.updateChecker.stop();
                                        Notifications.vValueFeed = Notifications.scrollPane.getVvalue();
                                        new Notifications(AuthKey,type);
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


        notifsPrint.Dismiss = new ArrayList<>();

        counter = 0;
        for (int i = user1.size() - 1; i >= 0; i--) {
            announcer(i, username, type);
        }

        if (counter1 != 0 || counter2 != 0){
            Clear.setOnAction(event -> {
                connection.get("clear",AuthKey);
            });
        for (int i = 0; i < notifsPrint.Dismiss.size(); i++) {
            final int finalI = i;
            notifsPrint.Dismiss.get(i).setOnAction(event -> {
                if (type==1) {
//                    System.out.println(user1.get(mapper1[finalI]) + " - "+user2.get(mapper1[finalI]));
                    connection.get("delete", AuthKey, id.get(mapper1[finalI]));
                }else {
//                    System.out.println(user1.get(mapper2[finalI]) + " - "+user2.get(mapper2[finalI]));
                    connection.get("delete", AuthKey, id.get(mapper2[finalI]));
                }

            });
        }
    }
        Platform.runLater(()->{
            Notifications.scrollPane.setVvalue(Notifications.vValueFeed);
        });

    }

    public void announcer(int i,String username,int type) throws IOException {
        if (user2.get(i).equals(username)
        && notifType.get(i)!=8 && notifType.get(i)!=9 && notifType.get(i)!=10 && type==1) {
            new notifsPrint(notifType.get(i), user1.get(i),1);
            mapper1[counter1]=i;
            counter1++;
        }else if (user1.get(i).equals(username)
                && notifType.get(i)==8  && type ==2){
            new notifsPrint(notifType.get(i), user2.get(i),2);
                mapper2[counter2] = i;
                counter2++;
        }else if(user2.get(i).equals(username)
                &&( notifType.get(i)==9||notifType.get(i)==10) && type ==2){
            new notifsPrint(notifType.get(i), user1.get(i),2);
            mapper2[counter2] = i;
            counter2++;
        }
    }

}
