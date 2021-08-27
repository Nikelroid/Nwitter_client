package notifications;

import org.json.JSONArray;
import org.json.JSONObject;
import profiles.profileSaver;

import java.util.ArrayList;

public class jsonNotifsDecoder {
    public jsonNotifsDecoder() {
    }

    public static ArrayList<String> user1 = new ArrayList<>();
    public static ArrayList<String> user2 = new ArrayList<>();
    public static ArrayList<Integer> type = new ArrayList<>();
    public static ArrayList<String> id = new ArrayList<>();

    public jsonNotifsDecoder(JSONObject jsonInput) {

        JSONArray User1 = jsonInput.getJSONArray("user1");
        JSONArray User2 = jsonInput.getJSONArray("user2");
        JSONArray Type = jsonInput.getJSONArray("type");
        JSONArray Id = jsonInput.getJSONArray("_id");
        JSONArray profilePics = jsonInput.getJSONArray("profile_pics");
        JSONArray profile = jsonInput.getJSONArray("profile");

        user1 = new ArrayList<>();
        user2 = new ArrayList<>();
        type = new ArrayList<>();
        id = new ArrayList<>();

        for (int i = 0; i < User1.length(); i++) {

            jsonNotifsDecoder.user1.add(User1.get(i).toString());
            jsonNotifsDecoder.user2.add(User2.get(i).toString());
            jsonNotifsDecoder.type.add(Integer.parseInt(Type.get(i).toString()));
            jsonNotifsDecoder.id.add(Id.get(i).toString());

        }
        for (int i = 0; i < profilePics.length(); i++) {
            if (!profilePics.get(i).toString().isEmpty()){
                new profileSaver(profile.get(i).toString(),
                        profilePics.get(i).toString());
            }
        }
    }
}
