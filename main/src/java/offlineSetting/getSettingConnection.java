package offlineSetting;

import org.json.JSONObject;

public class getSettingConnection {
    getSettingJsonGenerator jsonGenerator = new getSettingJsonGenerator();
    JSONObject jsonMessages ;
    public getSettingConnection(String AuthKey) throws InterruptedException {

        jsonMessages = jsonGenerator.connection(AuthKey,"get");

        new getSettingJsonSaver(jsonMessages);

    }
}
