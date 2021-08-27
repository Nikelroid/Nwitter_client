package twitteControl;

import connection.sender;
import operation.jsonCreatorOperator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.swing.*;

public class Newtwitte {
    sender sender = new sender();
    public Newtwitte() {
    }
    private static final Logger logger = LogManager.getLogger(Newtwitte.class);
    public void newComment(String text,String AuthKey,String serial) {
        logger.info("System: user went to twitteControl.NewComment");
        jsonCreatorOperator creatorOperator = new jsonCreatorOperator();
        String result = sender.send(creatorOperator.generate("submit_comment",
                AuthKey,serial, text)).get("result").toString();
        if (Integer.parseInt(result)==0) {
            JOptionPane.showMessageDialog(null, "An error occurred in submit comment, try again");
        }
//        System.out.println(result);
    }


    public int sendTwitte(String text, String AuthKey) throws Exception {
        logger.info("System: user went to twitteControl.Newtwitte");
        jsonCreatorOperator creatorOperator = new jsonCreatorOperator();
        JSONObject result = sender.send(creatorOperator.generate("submit_twitte",
                AuthKey,"pic_STRING", text));
        if (Integer.parseInt(result.get("result").toString())==0) {
            JOptionPane.showMessageDialog(null, "An error occurred in submit twitte, try again");
        }else{
            return Integer.parseInt(result.get("result").toString());
        }
        return 0;
    }




}
