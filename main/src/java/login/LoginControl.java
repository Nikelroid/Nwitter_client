package login;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import launch.authKey;
import mainPages.Feed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.Scanner;

public class LoginControl {
    protected static authKey authKey = new authKey();
    private static final login.loginConnection loginConnection  = new loginConnection();
    Scanner input = new Scanner(System.in);
    private final String[] log_info = new String[2];
    public LoginControl() {
    }

    private static final Logger logger = LogManager.getLogger(LoginControl.class);

    public static void loginAction(Label info, String[] log_info, TextField password, Stage stage, TextField userName,boolean remember) throws Exception {


        logger.info("System: user went to LogInfo");

        int response = loginConnection.connection(log_info[0],log_info[1]);


            if (response==1) {

                logger.info("System: User logged in");

                if(remember) authKey.setter(authKey.getter().substring(2),"1",log_info[0]);
                else authKey.setter(authKey.getter().substring(2),"0",log_info[0]);
                JOptionPane.showMessageDialog(null, "You logged in Successfully!");
                stage.close();

                new Feed(authKey.getter().substring(2));

            } else if (response==2) {

                logger.info("System: User inserted wrong password");
                info.setText("Your Password was wrong, please try again!");
                info.setTextFill(Color.RED);
                password.getStyleClass().add("wrong");
                Timeline Normalizer = null;
                Normalizer = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
                    password.getStyleClass().remove("wrong");
                    info.setText("Login or creat an account");
                    info.setTextFill(Color.web("#2196f3"));
                }));
                Normalizer.setCycleCount(0);
                Normalizer.play();

            }else if (response==3){

            logger.info("System: User inserted wrong username and password");
            info.setText("Your Username or Password was wrong, please try again!");
            info.setTextFill(Color.RED);
            password.getStyleClass().add("wrong");
            userName.getStyleClass().add("wrong");
            Timeline Normalizer = null;
            Normalizer = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
                password.getStyleClass().remove("wrong");
                userName.getStyleClass().remove("wrong");
                info.setText("Login or creat an account");
                info.setTextFill(Color.web("#2196f3"));
            }));
            Normalizer.setCycleCount(0);
            Normalizer.play();

        }else{
                logger.info("System: error in connection");
                info.setText("Error in connection to server. check it!");
                info.setTextFill(Color.RED);
                password.getStyleClass().add("wrong");
                userName.getStyleClass().add("wrong");
                Timeline Normalizer = null;
                Normalizer = new Timeline(new KeyFrame(Duration.seconds(3), ev -> {
                    password.getStyleClass().remove("wrong");
                    userName.getStyleClass().remove("wrong");
                    info.setText("Login or creat an account");
                    info.setTextFill(Color.web("#2196f3"));
                }));
                Normalizer.setCycleCount(0);
                Normalizer.play();

            }
    }
}

