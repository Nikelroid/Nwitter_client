package loadTwittes;

import connection.sender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import register.jsonGeneratorRegister;

public class loadConnection {
    jsonGeneratorLoad jsonGenerator = new jsonGeneratorLoad();
    sender connection = new sender();
    JSONObject outputJson = new JSONObject();
    JSONObject inputJson = new JSONObject();

    public loadConnection() {
    }

    private static final Logger logger = LogManager.getLogger(loadConnection.class);
    public void get(String type,String AuthKey){
        try {
            logger.info("System: user went to loadConnection");
            outputJson = jsonGenerator.generate(type,AuthKey);
            inputJson = connection.send(outputJson);
            new jsonDecoder(inputJson);
        }catch (Exception e){
            logger.error("cant load twittes");

        }
    }
    public int get(String type,String AuthKey,String serial,boolean comment){
        try {
            logger.info("System: user went to loadConnection");
            outputJson = jsonGenerator.generate(type,AuthKey,serial);
            inputJson = connection.send(outputJson);
            if (Integer.parseInt(inputJson.get("result").toString())==0)
                return 0;
            if (comment)
                new jsonDecoderComments(inputJson);
            else
                new jsonDecoder(inputJson);
        }catch (Exception e){
            logger.error("cant load twittes");

        }
        return 1;
    }
}
