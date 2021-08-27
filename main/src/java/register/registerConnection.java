package register;

import connection.sender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class registerConnection {
    jsonGeneratorRegister jsonGeneratorRegister = new jsonGeneratorRegister();
    connection.sender connection = new sender();
    JSONObject outputJson = new JSONObject();
    JSONObject inputJson = new JSONObject();

    public registerConnection() {
    }

    private static final Logger logger = LogManager.getLogger(registerConnection.class);
        public boolean register(String[] regInfo){
            try {
                logger.info("System: user went to registerConnection");
                outputJson = jsonGeneratorRegister.generate(regInfo);
                inputJson = connection.send(outputJson);
                return true;
            }catch (Exception e){
                logger.error("cant register");
                return false;
            }

    }
}
