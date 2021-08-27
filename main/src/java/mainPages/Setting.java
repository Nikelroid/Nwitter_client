package mainPages;

import category.Category;
import connection.sender;
import edit.editProfile;
import offlineSetting.settingOffline;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import launch.authKey;
import login.loginConnection;
import login.loginView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import submit.submitDelet;
import submit.submitPrivacy;
import twitteControl.Menu;
import twitteControl.deathPages;
import userControl.userFinder;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Setting {
    private static double vValueFeed;
    private static ArrayList<Object> tempPrivacy;




    protected static boolean isEnable;
    protected static boolean isPrivate;

    public static List<Integer> privacy = new ArrayList<>();

    sender sender = new sender();
    int target=0;
    private static final Logger logger = LogManager.getLogger(Setting.class);
    public static Button togglePrivate, toggleLastseen, toggleEmail, togglePhonenumber, toggleBirthday,
            Catrgories, Edit, Logout, toggleEnable,Delete;

    public static Timeline updateChecker;

    public Setting(String AuthKey) throws Exception {

        new settingOffline();

        launch.view.root = FXMLLoader.load(getClass().getResource("/layout/page/setting_page.fxml"));
        launch.view.scene = new Scene(launch.view.root);
        launch.view.stage.setScene(launch.view.scene);
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



            new settingOffline();
            isEnable = settingOffline.enable;
            isPrivate = settingOffline.account;
            Setting.privacy = new ArrayList<>();

        Setting.privacy.add(settingOffline.lastseen);
        Setting.privacy.add(settingOffline.birthday);
        Setting.privacy.add(settingOffline.email);
        Setting.privacy.add(settingOffline.phonenumber);




        updateChecker = new Timeline(
                new KeyFrame(Duration.millis(300),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                new settingOffline();
                                Setting.tempPrivacy = new ArrayList<>();
                                Setting.tempPrivacy.add(settingOffline.lastseen);
                                Setting.tempPrivacy.add(settingOffline.birthday);
                                Setting.tempPrivacy.add(settingOffline.email);
                                Setting.tempPrivacy.add(settingOffline.phonenumber);
                                if (isEnable != settingOffline.enable
                                || isPrivate != settingOffline.account
                                        || !privacy.equals(tempPrivacy)){
                                    try {
                                        new deathPages();
                                        new Setting(AuthKey);
                                    } catch (Exception exception) {
                                        exception.printStackTrace();
                                    }
                                }
                            }
                        }));
        updateChecker.setCycleCount(Timeline.INDEFINITE);
        updateChecker.play();



        togglePrivate = (Button) launch.view.root.lookup("#toggle_private");
        toggleLastseen = (Button) launch.view.root.lookup("#toggle_lastseen");
        toggleBirthday = (Button) launch.view.root.lookup("#toggle_birthday");
        toggleEmail = (Button) launch.view.root.lookup("#toggle_email");
        togglePhonenumber = (Button) launch.view.root.lookup("#toggle_phonenumber");
        Catrgories = (Button) launch.view.root.lookup("#categories");
        Edit = (Button) launch.view.root.lookup("#edit");
        Logout = (Button) launch.view.root.lookup("#logout");
        toggleEnable = (Button) launch.view.root.lookup("#toggle_enable");
        Delete = (Button) launch.view.root.lookup("#delete");

        logger.info("System: user went to mainPages.Setting");

        var menu = new Menu();
        menu.Menu_command(AuthKey);
        var uf = new userFinder();
        target = uf.UserFinder(AuthKey);


        Delete.setOnAction(event -> {
            int input = JOptionPane.showConfirmDialog(null, "Are you sure?");
            if (input==0)
                new submitDelet(AuthKey);

        });

        if(isPrivate){
            togglePrivate.getStyleClass().remove("toggle_wrong");
            togglePrivate.setText("Account: Public");
                }else{
            togglePrivate.getStyleClass().add("toggle_wrong");
            togglePrivate.setText("Account: Private");
                }

        togglePrivate.setOnAction(event -> {
            boolean target = true;
            if (isPrivate) target = false;
            settingOffline.setter(
                    String.valueOf(settingOffline.enable),
                    String.valueOf(target),
                    settingOffline.lastseen,
                    settingOffline.birthday,
                    settingOffline.email,
                    settingOffline.phonenumber);
        });


        if(isEnable){
            toggleEnable.getStyleClass().remove("toggle_wrong");
            toggleEnable.setText("Account is Enable");
        }else{
            toggleEnable.getStyleClass().add("toggle_wrong");
            toggleEnable.setText("Account is Disable");
        }

        toggleEnable.setOnAction(event -> {
            boolean target = true;
            if (isEnable) target = false;
            settingOffline.setter(
                    String.valueOf(target),
                    String.valueOf(settingOffline.account),
                    settingOffline.lastseen,
                    settingOffline.birthday,
                    settingOffline.email,
                    settingOffline.phonenumber);
        });

        Edit.setOnAction(event -> {
            try {
                new editProfile(AuthKey);
            } catch (Exception e) {
                logger.error("error in loading edit info page");
            }
        });

        Catrgories.setOnAction(event -> {
            try {
                new Category(AuthKey);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        Logout.setOnAction(event -> {
            try {
                new deathPages();
                loginConnection loginConnection = new loginConnection();
                loginConnection.connection(AuthKey,"-");
                authKey.setter(authKey.getter().substring(2),"0",authKey.username);
                new loginView(true);
            } catch (Exception e) {
                logger.error("Error in loading login page");
            }
        });

                new submitPrivacy(AuthKey);

    }
}
