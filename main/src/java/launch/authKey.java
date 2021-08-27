package launch;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class authKey {
    public static String username="";
    public authKey() {
    }

    private static final Logger logger = LogManager.getLogger(authKey.class);
    public static String getter() {
        int authKey=0;
        int remember=0;


        File configFile = new File("authKey.properties");

        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);
            username = props.getProperty("username");
            authKey = Integer.parseInt(props.getProperty("authKey"));
            remember = Integer.parseInt(props.getProperty("stay_logged_in"));
            logger.info("authKey red successfully");
            reader.close();
        } catch (
                FileNotFoundException ex) {
            logger.error("file authKey doesnt exists");
        } catch (
                IOException ex) {
            logger.error("error in reading config authKey");
        }
        return remember+"-"+authKey;
    }
    public static void setter(String authKey, String remember,String username) {
        File configFile = new File("authKey.properties");

        try {
            Properties props = new Properties();
            props.setProperty("username", username);
            props.setProperty("authKey", authKey);
            props.setProperty("stay_logged_in", remember);
            FileWriter writer = new FileWriter(configFile);
            props.store(writer, "update Key");
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
