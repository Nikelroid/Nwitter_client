package launch;

import category.catrgoryCfg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

 public class Main {

     private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) throws Exception {

        new configsAddress();


        logger.info("System: user Started the app");
        new view(args);
    }

}
