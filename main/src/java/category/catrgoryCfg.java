package category;

import launch.configsAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class catrgoryCfg {

    public static String fxml_general;
    public static String header;
    public static String fxml_main_card;
    public static String date_format;
    public static String fxml_main_card_member;
    public static String minor_page;
    public static String button_add;
    public static String plus_image;
    public static String username_text;
    public static String remove_text;

    public static String header_groups;

    public static String chat_main;

    public static String my_childs;



    private static final Logger logger = LogManager.getLogger(catrgoryCfg.class);
    public catrgoryCfg() {

        String config_dir = configsAddress.category;
        Path simple = Paths.get("simple.png");
        Path path = Paths.get(simple.toAbsolutePath().getParent() +config_dir);
        File configFile = new File(String.valueOf(path));
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            fxml_general = props.getProperty("fxml_general");
            header = props.getProperty("header");
            fxml_main_card = props.getProperty("fxml_main_card");
            date_format = props.getProperty("date_format");
            fxml_main_card_member = props.getProperty("fxml_main_card_member");
            minor_page = props.getProperty("minor_page");
            remove_text = props.getProperty("remove_text");
            header_groups = props.getProperty("header_groups");
            chat_main = props.getProperty("chat_main");
            my_childs = props.getProperty("my_childs");


            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file does not exist");
        } catch (IOException ex) {
            logger.error("I/O error");
        }
    }
}

