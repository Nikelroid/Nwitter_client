package userControl;

import org.json.JSONObject;

public class jsonGeneratorUser {
    JSONObject jsonOutput = new JSONObject();
    public JSONObject generate(String myAuthKey , String userAuthKey) {
        jsonOutput.put("key","user").put("myAuthKey",myAuthKey).put("userAuthKey",userAuthKey);
        return jsonOutput;
    }
}
