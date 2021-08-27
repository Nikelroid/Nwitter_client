package connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class connectionCfg {

    protected static String address = "";
    protected static int port = 0;
    File configFile = new File("connectionConfig.properties");
    private static final Logger logger = LogManager.getLogger(connectionCfg.class);
    public connectionCfg() {
        try {
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

             address = props.getProperty("address");
             port = Integer.parseInt(props.getProperty("port"));

            reader.close();
        } catch (FileNotFoundException ex) {
            logger.error("file does not exist");
        } catch (IOException ex) {
            logger.error("I/O error");
        }
    }
}

