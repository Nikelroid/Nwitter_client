package reports;

import connection.sender;
import operation.jsonCreatorOperator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.swing.*;

public class Reports {

    connection.sender sender = new sender();
    jsonCreatorOperator jsonCreator = new jsonCreatorOperator();


    private static final Logger logger = LogManager.getLogger(Reports.class);

    public Reports(String repText,String serial,String AuthKey) {

        logger.info("System: user went to reports.Reports");
        JSONObject repRequest = jsonCreator.generate("report",AuthKey,serial,repText);
        String result = sender.send(repRequest).get("result").toString();

        if(result.equals("0")){
            JOptionPane.showMessageDialog(null,"Error in connection with server");
        }
                logger.info("System: Twitte reported");
                JOptionPane.showMessageDialog(null,"Twitte reported !");




    }
}
