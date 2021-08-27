package submit;

import connection.sender;
import offlineSetting.settingOffline;
import javafx.scene.control.Button;
import mainPages.Setting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class submitPrivacy {

    int target;
    private static final Logger logger = LogManager.getLogger(submitPrivacy.class);
    String username;
    Button toggleLastseen,toggleEmail,togglePhonenumber,toggleBirthday;
    JSONObject jsonOutput = new JSONObject();
    JSONObject jsonResult = new JSONObject();

    sender sender = new sender();
    public submitPrivacy(String AuthKey) throws Exception {
        this.username=AuthKey;

        toggleLastseen = (Button) launch.view.root.lookup("#toggle_lastseen");
        toggleEmail = (Button) launch.view.root.lookup("#toggle_email");
        togglePhonenumber = (Button) launch.view.root.lookup("#toggle_phonenumber");
        toggleBirthday = (Button) launch.view.root.lookup("#toggle_birthday");


        logger.info("System: user went to submit.submitPrivacy");



        buttonStyleSetter();
        toggleLastseen.setOnAction(event -> {
            int target;
            if (settingOffline.lastseen==3)target=1;
            else target=settingOffline.lastseen+1;
            settingOffline.setter(
                    String.valueOf(settingOffline.enable),
                    String.valueOf(settingOffline.account),
                    target,
                    settingOffline.birthday,
                    settingOffline.email,
                    settingOffline.phonenumber);
        });
        toggleBirthday.setOnAction(event -> {
            int target;
            if (settingOffline.birthday==3)target=1;
            else target=settingOffline.birthday+1;
            settingOffline.setter(
                    String.valueOf(settingOffline.enable),
                    String.valueOf(settingOffline.account),
                    settingOffline.lastseen,
                    target,
                    settingOffline.email,
                    settingOffline.phonenumber);
        });
        toggleEmail.setOnAction(event -> {
            int target;
            if (settingOffline.email==3)target=1;
            else target=settingOffline.email+1;
            settingOffline.setter(
                    String.valueOf(settingOffline.enable),
                    String.valueOf(settingOffline.account),
                    settingOffline.lastseen,
                    settingOffline.birthday,
                    target,
                    settingOffline.phonenumber);
        });
        togglePhonenumber.setOnAction(event -> {
            int target;
            if (settingOffline.phonenumber==3)target=1;
            else target=settingOffline.phonenumber+1;
            settingOffline.setter(
                    String.valueOf(settingOffline.enable),
                    String.valueOf(settingOffline.account),
                    settingOffline.lastseen,
                    settingOffline.birthday,
                    settingOffline.email,
                    target);
        });

    }

    public void buttonStyleSetter(){
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0 -> style(toggleLastseen, Setting.privacy.get(0));
                case 1 -> style(toggleBirthday, Setting.privacy.get(1));
                case 2 -> style(toggleEmail, Setting.privacy.get(2));
                case 3 -> style(togglePhonenumber, Setting.privacy.get(3));
            }

        }
    }
    public void style(Button B,int type){
        switch (type) {
            case 1 -> {
                B.getStyleClass().remove("login");
                B.getStyleClass().remove("toggle_wrong");
                B.getStyleClass().add("toggle_accept");
                B.setText("Everyone");
            }
            case 2 -> {
                B.getStyleClass().remove("toggle_accept");
                B.getStyleClass().remove("toggle_wrong");
                B.getStyleClass().add("toggle_regular");
                B.setText("Your followings");
            }
            case 3 -> {
                B.getStyleClass().remove("login");
                B.getStyleClass().remove("toggle_accept");
                B.getStyleClass().add("toggle_wrong");
                B.setText("Nobody");
            }
        }

    }
}
