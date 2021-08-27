package offlineMessages;

import jsonContoller.jsonMessage;
import objects.objMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class submitMessage {
    List<objMessage> messages = new ArrayList<>();
    jsonMessage Get = new jsonMessage();
    File f = new File("Message.json");
    public submitMessage() {
    }
    private static final Logger logger = LogManager.getLogger(submitMessage.class);
    public int SubMess(int id,String text, String sender, String receiver,String time,
                       boolean seen,boolean delivered,boolean sent){
        logger.info("System: user went to submit.submitMessage");
        try {
            messages = Get.get();
            adder(id,text, sender, receiver, time, seen, delivered, sent);
            return 1;
        }catch (Exception e){
            logger.error("Error in submit messages file");
            e.printStackTrace();
            return 0;
        }
    }


    private void adder(int id,String text, String sender, String receiver,String time,
                       boolean seen,boolean delivered,boolean sent){

        if (f.exists()) {
            messages = Get.get();
            messages.add(new objMessage(id,text,sender,receiver,time,seen,delivered,sent));
        } else {
    messages = Collections.singletonList(new objMessage(id,text,sender,receiver,time,seen,delivered,sent));
        }
        new jsonMessage(messages);
    }

}
