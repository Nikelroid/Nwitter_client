package lists;

import loadTwittes.jsonDecoder;
import org.json.JSONArray;
import org.json.JSONObject;
import profiles.profileSaver;

import java.util.ArrayList;

public class jsonListDecoder {

    public static ArrayList<String> username = new ArrayList<>();
    public static ArrayList<String> name = new ArrayList<>();
    public static ArrayList<String> pic = new ArrayList<>();


    public jsonListDecoder(JSONObject jsonInput) {

            JSONArray usernames = jsonInput.getJSONArray("username");
            JSONArray names = jsonInput.getJSONArray("name");
            JSONArray pics = jsonInput.getJSONArray("pic");

            username = new ArrayList<>();
            name = new ArrayList<>();
            pic = new ArrayList<>();



            for (int i = 0; i < usernames.length(); i++) {
                jsonListDecoder.username.add(usernames.get(i).toString());
                jsonListDecoder.name.add(names.get(i).toString());
                jsonListDecoder.pic.add(pics.get(i).toString());
            }
        for (int i = 0; i < pic.size(); i++) {
            if (!pics.get(i).toString().isEmpty()){
                new profileSaver(username.get(i),pic.get(i));
            }
        }
    }
}
