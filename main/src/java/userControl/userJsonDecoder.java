package userControl;

import org.json.JSONObject;
import profiles.profileSaver;

import javax.swing.*;

public class userJsonDecoder {

    public static String Username;
    public static String Name;
    public static String Email;
    public static String Phonenumber;
    public static String Birthday;
    public static String Bio;
    public static boolean account;
    public static boolean enable;
    public static String lastseen;
    public static boolean isFollowing;
    public static boolean isMuted;
    public static boolean isblockd;
    public static boolean followedBy;
    public static boolean mutedBy;
    public static boolean blockdBy;
    public static int followers;
    public static int followings;
    public static int blocks;
    public static int mutes;
    public static boolean isRequested;
    public static String AuthKey;
    public static String pic;

    public userJsonDecoder(JSONObject inputJson) {

        try {

            if (!inputJson.get("username").toString().equals("-")) {

                Username = inputJson.get("username").toString();
                Name = inputJson.get("name").toString();
                Email = inputJson.get("email").toString();
                Phonenumber = inputJson.get("phonenumber").toString();
                Birthday = inputJson.get("birthday").toString();
                Bio = inputJson.get("bio").toString();
                account = (Boolean) inputJson.get("account");
                enable = (Boolean) inputJson.get("enable");
                lastseen = inputJson.get("lastseen").toString();
                isFollowing = (Boolean) inputJson.get("isFollowing");
                isMuted = (Boolean) inputJson.get("isMuted");
                isblockd = (Boolean) inputJson.get("isBlocked");
                followedBy = (Boolean) inputJson.get("followedBy");
                mutedBy = (Boolean) inputJson.get("mutedBy");
                blockdBy = (Boolean) inputJson.get("blockedBy");
                followings = Integer.parseInt(inputJson.get("followings").toString());
                blocks = Integer.parseInt(inputJson.get("blocks").toString());
                mutes = Integer.parseInt(inputJson.get("mutes").toString());
                followers = Integer.parseInt(inputJson.get("followers").toString());
                isRequested = (Boolean) inputJson.get("requested");
                AuthKey = inputJson.get("AuthKey").toString();
                pic = inputJson.get("pic").toString();

                if(!pic.isEmpty())
                new profileSaver(Username,pic);

            } else {
                Username = inputJson.get("username").toString();
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,"Check connection");
        }
    }

}
