package offlineSetting;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class settingOffline {

    public static boolean enable;
    public static boolean account;
    public static int lastseen;
    public static int birthday;
    public static int email;
    public static int phonenumber;

    private static final Logger logger = LogManager.getLogger(settingOffline.class);
    public settingOffline() {

        File configFile = new File("offlineSetting.properties");

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            enable = props.getProperty("enable").equals("true");
            account = props.getProperty("account").equals("true");
            lastseen = Integer.parseInt(props.getProperty("lastseen"));
            birthday = Integer.parseInt(props.getProperty("birthday"));
            email = Integer.parseInt(props.getProperty("email"));
            phonenumber = Integer.parseInt(props.getProperty("phonenumber"));

            logger.info("offlineSetting red successfully");
            reader.close();
        } catch (
                FileNotFoundException ex) {
            logger.error("file authKey doesnt exists");
        } catch (
                IOException ex) {
            logger.error("error in reading offlineSetting");
        }
    }
    public static void setter(String enable, String account,int lastseen,
                              int birthday,int email,int phonenumber) {
        File configFile = new File("offlineSetting.properties");

        try {
            Properties props = new Properties();
            props.setProperty("enable", enable);
            props.setProperty("account", account);
            props.setProperty("lastseen", String.valueOf(lastseen));
            props.setProperty("birthday", String.valueOf(birthday));
            props.setProperty("email", String.valueOf(email));
            props.setProperty("phonenumber", String.valueOf(phonenumber));

            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "update setting");
            logger.info("authKey wrote successfully");
            writer.close();
        } catch (
                FileNotFoundException ex) {
            logger.error("file authKey doesnt exists");
        } catch (
                IOException ex) {
            logger.error("error in reading config authKey");
        }
    }

}
