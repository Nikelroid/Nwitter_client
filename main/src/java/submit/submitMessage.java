package submit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jsonContoller.jsonMessage;
import objects.objMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class submitMessage {
    private int serial;
    List<objMessage> messages;
    jsonMessage Get = new jsonMessage();
    File f = new File("Message.json");
    File id = new File("serialMess.txt");
    Gson twitteObj = new GsonBuilder().setPrettyPrinting().create();
    public submitMessage() {
    }
    private static final Logger logger = LogManager.getLogger(submitMessage.class);
    public int SubMess(String text, String sender, String reciver,String time){
        logger.info("System: user went to submit.submitMessage");
        try {
            if (id.createNewFile()) {
                serial=5000000;
                FileWriter myWriter = new FileWriter("serialMess.txt");
                myWriter.write(serial+"");
                myWriter.close();
            } else {
                Scanner myReader = new Scanner(id);
                String data = myReader.nextLine();
                serial = Integer.parseInt(data);
                serial++;
                FileWriter myWriter = new FileWriter("serialMess.txt");
                myWriter.write(serial+"");
                myWriter.close();
            }
        } catch (IOException ignored) {
        }

        messages = Get.get();
        adder(text,sender,reciver,time);
        return serial;
    }


    private void adder(String text, String sender,String receiver,String time) {

        boolean seenAndDelivered =sender.equals(receiver);

        if (f.exists()) {
            messages = Get.get();
            messages.add(
                    new objMessage(serial,text, sender, receiver,
                            time,seenAndDelivered,seenAndDelivered,false));


        } else {
            messages = Collections.singletonList(
                    new objMessage(serial,text, sender, receiver,
                            time,seenAndDelivered,seenAndDelivered,false));
        }
        new jsonMessage(messages);
    }

}
