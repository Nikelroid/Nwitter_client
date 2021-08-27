package checkExists;

import connection.sender;
import launch.authKey;
import login.loginConnection;
import login.loginJsonCreator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class checkExists {
    private static final Logger logger = LogManager.getLogger(loginConnection.class);

    sender connection = new sender();
    JSONObject outputJson = new JSONObject();
    JSONObject inputJson = new JSONObject();
    jsonGenerator jsonGenerator = new jsonGenerator();

    public checkExists() {
    }

    public int connection(String field, String input) {
        outputJson = jsonGenerator.generator(field, input);
        return sendExist();
    }
    public int connection(String field, String input,String AuthKey) {
        outputJson = jsonGenerator.generator(field, input,AuthKey);
        return sendExist();
    }

    private int sendExist() {
        inputJson = connection.send(outputJson);
        if (inputJson == null) {
            logger.warn("input was null");
            return 2;
        } else {
            try {
                return Integer.parseInt(inputJson.get("result").toString());
            } catch (Exception e) {
                return 2;
            }

        }
    }
}
