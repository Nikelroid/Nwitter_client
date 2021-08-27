package loadTwittes;

import org.json.JSONArray;
import org.json.JSONObject;
import profiles.profileSaver;

import java.util.ArrayList;

public class jsonDecoderComments {
    public jsonDecoderComments() {
    }

    public static ArrayList<String> usernames = new ArrayList<>();
    public static ArrayList<String> text = new ArrayList<>();
    public static ArrayList<String> date = new ArrayList<>();
    public static ArrayList<String> serials = new ArrayList<>();
    public static ArrayList<Integer> likes = new ArrayList<>();
    public static ArrayList<Integer> comments = new ArrayList<>();
    public static ArrayList<Integer> retwittes = new ArrayList<>();
    public static ArrayList<Boolean> isSaved = new ArrayList<>();
    public static ArrayList<Boolean> isLiked = new ArrayList<>();
    public static ArrayList<Boolean> isRetwitted = new ArrayList<>();

    public jsonDecoderComments(JSONObject jsonInput) {

        JSONArray Username = jsonInput.getJSONArray("usernames");
        JSONArray Text = jsonInput.getJSONArray("text");
        JSONArray Date = jsonInput.getJSONArray("date");
        JSONArray Serials = jsonInput.getJSONArray("serials");
        JSONArray Likes = jsonInput.getJSONArray("likes");
        JSONArray Comments = jsonInput.getJSONArray("comments");
        JSONArray Retwittes = jsonInput.getJSONArray("retwittes");
        JSONArray IsSaved = jsonInput.getJSONArray("isSaved");
        JSONArray IsLiked = jsonInput.getJSONArray("isLiked");
        JSONArray IsRetwitted = jsonInput.getJSONArray("isRetwitted");
        JSONArray pic = jsonInput.getJSONArray("pic");

        usernames = new ArrayList<>();
        text = new ArrayList<>();
        date = new ArrayList<>();
        serials = new ArrayList<>();
        likes = new ArrayList<>();
        comments = new ArrayList<>();
        retwittes = new ArrayList<>();
        isSaved = new ArrayList<>();
        isLiked = new ArrayList<>();
        isRetwitted = new ArrayList<>();

        for (int i = 0; i < Username.length(); i++) {

            jsonDecoderComments.usernames.add(Username.get(i).toString());
            jsonDecoderComments.text.add(Text.get(i).toString());
            jsonDecoderComments.date.add(Date.get(i).toString());
            jsonDecoderComments.serials.add(Serials.get(i).toString());
            jsonDecoderComments.likes.add(Integer.parseInt(Likes.get(i).toString()));
            jsonDecoderComments.comments.add(Integer.parseInt(Comments.get(i).toString()));
            jsonDecoderComments.retwittes.add(Integer.parseInt(Retwittes.get(i).toString()));
            jsonDecoderComments.isSaved.add((Boolean) IsSaved.get(i));
            jsonDecoderComments.isLiked.add((Boolean) IsLiked.get(i));
            jsonDecoderComments.isRetwitted.add((Boolean) IsRetwitted.get(i));

        }
        for (int i = 0; i < pic.length(); i++) {
            if (!pic.get(i).toString().isEmpty()){
                new profileSaver(serials.get(i),pic.get(i).toString());
            }
        }
    }
}
