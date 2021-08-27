package edit;

import javafx.scene.control.PasswordField;
import org.json.JSONObject;

public class editProfileDecoder {

    public static String Username;
    public static String Name;
    public static String Email;
    public static String Phonenumber;
    public static String Birthday;
    public static String Bio;
    public static String Password;


    public editProfileDecoder(JSONObject inputJson) {

            Username = inputJson.get("username").toString();
            Name = inputJson.get("name").toString();
            Email = inputJson.get("email").toString();
            Phonenumber = inputJson.get("phonenumber").toString();
            Birthday = inputJson.get("birthday").toString();
            Bio = inputJson.get("bio").toString();
            Password = inputJson.get("password").toString();

        }

}
