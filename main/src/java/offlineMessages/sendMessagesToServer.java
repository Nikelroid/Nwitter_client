package offlineMessages;

import connection.sender;
import jsonContoller.jsonMessage;
import launch.authKey;
import objects.objMessage;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class sendMessagesToServer {
    sender senderClass = new sender();
    List<Integer> localId = new ArrayList<>();
    List<String> text = new ArrayList<>();
    List<String> sender = new ArrayList<>();
    List<String> receiver = new ArrayList<>();
    List<String> time = new ArrayList<>();
    List<Boolean> isSent = new ArrayList<>();
    List<Boolean> isDelivered = new ArrayList<>();
    List<Boolean> isSeen = new ArrayList<>();
    String username ;
    String AuthKey;
    jsonMessage jsonMessage = new jsonMessage();
    List<objMessage> messages;
    JSONObject outputJson = new JSONObject();
    authKey authKey = new authKey();
    public sendMessagesToServer() throws InterruptedException {

        launch.authKey.getter();
        username = launch.authKey.username;
        AuthKey= launch.authKey.getter().substring(2);
        messages = jsonMessage.get();
        if(messages==null){
            new getMessagesConnection(AuthKey);
            messages = jsonMessage.get();
        }
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).getSender().equals(username)
            || (messages.get(i).getReceiver().equals(username)
            && messages.get(i).isSeen()) || (!messages.get(i).getReceiver().equals(username) &&
                    !messages.get(i).getSender().equals(username) && messages.get(i).isSeen())){
                localId.add(messages.get(i).getId());
                text.add(messages.get(i).getText());
                sender.add(messages.get(i).getSender());
                receiver.add(messages.get(i).getReceiver());
                time.add(messages.get(i).getTime());
                isSent.add(messages.get(i).isSent());
                isDelivered.add(messages.get(i).isDelivered());
                isSeen.add(messages.get(i).isSeen());
            }
        }

        outputJson.put("key","messages")
                .put("type","set")
                .put("AuthKey",AuthKey)
                .put("localId",localId)
                .put("text",text)
                .put("sender",sender)
                .put("receiver",receiver)
                .put("time",time)
                .put("isSent",isSent)
                .put("isDelivered",isDelivered)
                .put("isSeen",isSeen);

        JSONObject inputJson = senderClass.send(outputJson);

        new getMessageJsonSaver(inputJson);
    }
}
