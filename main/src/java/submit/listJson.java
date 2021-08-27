package submit;

import org.json.JSONObject;

public class listJson {
    public listJson() {
    }

    public JSONObject get(String type , String code) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key","list")
                .put("list",type)
                .put("code",code);
        return jsonObject;
    }
}
