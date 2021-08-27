package login;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
public class loginJsonCreator {
    public loginJsonCreator() {
    }
    private static final Logger logger = LogManager.getLogger(loginJsonCreator.class);
    public JSONObject jsonCreator(String username,String password) {
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("key","login").put("username",username).put("password",password);
        logger.info("login json generated");
        return jsonOutput;
    }
}
