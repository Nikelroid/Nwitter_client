package notifications;

import connection.sender;
import loadTwittes.jsonDecoderComments;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class notifsConnection {
    jsonGeneratorNotifs jsonGenerator = new jsonGeneratorNotifs();
    sender connection = new sender();
    JSONObject outputJson = new JSONObject();
    JSONObject inputJson = new JSONObject();

    public notifsConnection() {
    }

    private static final Logger logger = LogManager.getLogger(notifsConnection.class);
        public void get(String type,String AuthKey){
            try {
                logger.info("System: user went to loadConnection");
                outputJson = jsonGenerator.generate(type,AuthKey);
                inputJson = connection.send(outputJson);
                    new jsonNotifsDecoder(inputJson);
            }catch (Exception e){
                logger.error("cant load twittes");

            }
    }
    public void get(String type,String AuthKey,String serial){
        try {
            logger.info("System: user went to loadConnection");
            outputJson = jsonGenerator.generate(type,AuthKey,serial);
            inputJson = connection.send(outputJson);
                new jsonNotifsDecoder(inputJson);
        }catch (Exception e){
            logger.error("cant load twittes");

        }
    }
}
