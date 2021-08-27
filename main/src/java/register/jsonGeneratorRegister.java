package register;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class jsonGeneratorRegister {
    public jsonGeneratorRegister() {
    }
    private static final Logger logger = LogManager.getLogger(jsonGeneratorRegister.class);
    public JSONObject generate(String[] regInfo){
        JSONObject jsonOutput = new JSONObject();
        jsonOutput.put("key","register")
                .put("name",regInfo[0]+" "+regInfo[1])
                .put("username",regInfo[2])
                .put("password",regInfo[3])
                .put("birthday",regInfo[7]+"/"+regInfo[6]+"/"+regInfo[5])
                .put("email",regInfo[8])
                .put("phonenumber",regInfo[9])
                .put("bio",regInfo[10]);
        logger.info("login json generated");
        return jsonOutput;
    }
}
