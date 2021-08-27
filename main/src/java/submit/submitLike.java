package submit;

import connection.sender;
import lists.jsonListDecoder;
import lists.likeretList;
import operation.jsonCreatorOperator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;

public class submitLike {
    sender sender = new sender();
    jsonCreatorOperator jsonCreator = new jsonCreatorOperator();
    listJson listJson = new listJson();
    public submitLike(){}
    private static final Logger logger = LogManager.getLogger(submitLike.class);
    public submitLike(String serial,String AuthKey) {
        logger.info("System: user went to submitLike");
        JSONObject likeRequest = jsonCreator.generate("like",AuthKey,serial);
        String result = sender.send(likeRequest).get("result").toString();
        if(result.equals("0")){
            JOptionPane.showMessageDialog(null,"Error in connection with server");
        }
    }
    public void list(String serial,String AuthKey) throws IOException {
        logger.info("System: Like list opened");
        JSONObject likeRequest = listJson.get("likes",serial);
        JSONObject outputJson = sender.send(likeRequest);
        String result = outputJson.get("result").toString();
        if(result.equals("0")){
            JOptionPane.showMessageDialog(null,"Error in connection with server");
        }else{

            new jsonListDecoder(outputJson);
        }

        new likeretList(AuthKey,serial,1);

    }
    }

