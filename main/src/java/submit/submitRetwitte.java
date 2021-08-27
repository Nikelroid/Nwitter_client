package submit;

import connection.sender;
import jsonContoller.jsonTwittes;
import jsonContoller.jsonUsers;
import lists.jsonListDecoder;
import lists.likeretList;
import objects.objTwitte;
import objects.objUsers;
import operation.jsonCreatorOperator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import twitteControl.TwitteController;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class submitRetwitte {
    connection.sender sender = new sender();
    jsonCreatorOperator jsonCreator = new jsonCreatorOperator();
    listJson listJson = new listJson();
    public submitRetwitte(){}
    private static final Logger logger = LogManager.getLogger(submitRetwitte.class);
    public submitRetwitte(String serial,String AuthKey) {
        logger.info("System: user went to submit.submitRetwitte");
        JSONObject likeRequest = jsonCreator.generate("retwitte",AuthKey,serial);
        String result = sender.send(likeRequest).get("result").toString();
        if(result.equals("0")){
            JOptionPane.showMessageDialog(null,"Error in connection with server");
        }
        }


    public void list(String serial,String AuthKey) throws IOException {
        logger.info("System: Like list opened");
        JSONObject likeRequest = listJson.get("retwittes",serial);
        JSONObject outputJson = sender.send(likeRequest);
        String result = outputJson.get("result").toString();
        if(result.equals("0")){
            JOptionPane.showMessageDialog(null,"Error in connection with server");
        }else{
            new jsonListDecoder(outputJson);
        }

        new likeretList(AuthKey,serial,2);


    }
}
