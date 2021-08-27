package graphics;

import chat.chatPage;
import connection.sender;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import launch.authKey;
import mainPages.Info;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import profiles.getImageFile;
import profiles.imageConnection;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Base64;


public class selectIcon {
    Image image1;
    File file = null;
    imageConnection profileConnection = new imageConnection();
    sender sender = new sender();
    public selectIcon(String username,int i) throws IOException {

        String AuthKey = authKey.getter().substring(2);

        Parent cRoot = FXMLLoader.load(getClass().getResource("/layout/mini_page/select_image.fxml"));
        Scene cScene = new Scene(cRoot);
        Stage cStage = new Stage();
        cStage.setScene(cScene);

        cStage.setTitle("Choose image");

        file = null;

        final FileChooser fileChooser = new FileChooser();
        ImageView pic = (ImageView) cScene.lookup("#pic");
        final Button openButton = (Button) cScene.lookup("#select");
        Button Back = (Button) cScene.lookup("#back");
        openButton.setOnAction((final ActionEvent e) -> {
            file = fileChooser.showOpenDialog(cStage);



                if (file != null) {
                    authKey.getter();
                    try {
                        Path simple = Paths.get("simple.png");
                        Path path = Paths.get(simple.toAbsolutePath().getParent() +
                                "\\main\\src\\resources\\profiles\\" +username + ".png");
                        Files.copy(file.toPath(), path, StandardCopyOption.REPLACE_EXISTING);
                        File file1 = new File(String.valueOf(path));
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    Image image1 = new Image(file.toURI().toString());
                    pic.setImage(image1);
                    pic.setVisible(true);

                    Back.setText("Accept");
                    Back.getStyleClass().add("accept");
                }

        });

        authKey.getter();
        getImageFile getProfileFile = new getImageFile();
        File file2 = getProfileFile.profile(authKey.username);
        Image image = new Image(file2.toURI().toString());

        try {
            pic.setImage(image);
        } catch (NullPointerException ignored) {
            pic.setVisible(false);
        }
        if (i == 2) {
            openButton.setVisible(false);
        }


        Back.setOnAction(event -> {
            if (file != null) {

                byte[] fileContent = new byte[0];
                String encodedString = null;
                try {
                    fileContent = FileUtils.readFileToByteArray(file);
                    encodedString = Base64.getEncoder().encodeToString(fileContent);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                JSONObject outputJson = profileConnection.jsonCreator("profile",AuthKey,encodedString);
                sender.send(outputJson);
                Image image1 = new Image(file.toURI().toString());
                Info.Icon.setImage(image1);
                pic.setImage(image1);
                pic.setVisible(true);
            }
            Back.getScene().getWindow().hide();
        });
        cStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/LOGO_main.png")));
        cStage.show();
    }

    public selectIcon(File file) throws IOException {

        Parent cRoot = FXMLLoader.load(getClass().getResource("/layout/mini_page/select_image.fxml"));
        Scene cScene = new Scene(cRoot);
        Stage cStage = new Stage();
        cStage.setScene(cScene);

        cStage.setTitle("Show image");

        ImageView pic = (ImageView) cScene.lookup("#pic");
        final Button openButton = (Button) cScene.lookup("#select");

            if (file != null) {
                Image image1 = new Image(file.toURI().toString());
                pic.setImage(image1);
                pic.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(null,"An error ourcud, try again");
            }


        openButton.setVisible(false);



        Button Back = (Button) cScene.lookup("#back");
        Back.setOnAction(event -> {
            Back.getScene().getWindow().hide();
        });
        cStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/LOGO_main.png")));
        cStage.show();
    }

    public static Image ImageIm;
    public static File fileIm;
    public void SelectImage() throws IOException {

        Parent cRoot = FXMLLoader.load(getClass().getResource("/layout/mini_page/select_image.fxml"));
        Scene cScene = new Scene(cRoot);
        Stage cStage = new Stage();
        cStage.setScene(cScene);

        cStage.setTitle("Slecting image");
        Button Back = (Button) cScene.lookup("#back");

        final FileChooser fileChooser = new FileChooser();
        ImageView pic = (ImageView) cScene.lookup("#pic");
        final Button openButton = (Button) cScene.lookup("#select");
        openButton.setOnAction((final ActionEvent e) -> {
            selectIcon.fileIm = fileChooser.showOpenDialog(cStage);
            if(selectIcon.fileIm!=null) {
            selectIcon.ImageIm = new Image(selectIcon.fileIm.toURI().toString());
            try {
                pic.setImage(ImageIm);
            }catch (NullPointerException ignored){
                pic.setVisible(false);
            }
                Back.setText("Confirm");
                Back.getStyleClass().remove("login");
                Back.getStyleClass().add("toggle_accept");
            }
        });

        try {
            pic.setImage(ImageIm);
        }catch (NullPointerException ignored){
            pic.setVisible(false);
        }


        Back.setOnAction(event -> {
           // if(selectIcon.fileIm!=null) {
             //   chatPage.picture.setText("Deselect");
             //   chatPage.picture.getStyleClass().add("accept");
           // }
            Back.getScene().getWindow().hide();
        });
        cStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/LOGO_main.png")));

        cStage.show();

    }

    public selectIcon() {

    }

}
