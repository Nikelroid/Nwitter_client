package offlineMessages;

import org.json.JSONObject;

public class getMessagesConnection {
    getMessageJsonGenerator jsonGenerator = new getMessageJsonGenerator();
    JSONObject jsonMessages ;
    public getMessagesConnection(String AuthKey) throws InterruptedException {
        jsonMessages = jsonGenerator.connection(AuthKey,"get");
        new getMessageJsonSaver(jsonMessages);

    }
}
