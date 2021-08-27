package login;

import connection.sender;
import launch.authKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
public class loginConnection {
    private static final Logger logger = LogManager.getLogger(loginConnection.class);

    sender connection = new sender();
    JSONObject outputJson = new JSONObject();
    JSONObject inputJson = new JSONObject();
    loginJsonCreator jsonCreator = new loginJsonCreator();
    public loginConnection() {
    }
    public int connection(String username,String password) {
        outputJson = jsonCreator.jsonCreator(username,password);
        inputJson = connection.send(outputJson);
        if (password.equals("-")) return 1;
        else
        if(inputJson==null){
            logger.warn("input was null");
            return 0;
        } else {
            try {
            if(inputJson.get("response").toString().equals("Y")){
                authKey authKey = new authKey();
                authKey.setter(inputJson.get("result").toString(),"0","");
                return 1;
            }else {
                return Integer.parseInt(inputJson.get("result").toString());
            }
            }catch (Exception e){

                return 0;
            }

        }
    }
}
