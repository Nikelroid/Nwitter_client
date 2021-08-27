package register;

import jsonContoller.jsonUsers;
import objects.objUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class registerController {


    public registerController() {
    }

    private final boolean register_status = false;
    private static final Logger logger = LogManager.getLogger(registerController.class);
    public void Registeruser() throws Exception {
        logger.info("System: user went to Register");
        new registerView();


    }




}

