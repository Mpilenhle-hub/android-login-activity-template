package com.projects.fbgrecojr.logintemplate.Parser;

import com.projects.fbgrecojr.logintemplate.Structures.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fbgrecojr on 11/5/15.
 */
public class JSONParser {

    /**
     * Turns a string of JSON formatted content and fills a typed User Object
     * @param content the JSON string to parse
     * @return a User objects
     */
    public static User parseUserFeed(String content){

        User user;

        try {
            user = new User();
            JSONArray arr = new JSONArray(content);

            for (int i = 0; i < arr.length(); ++i) {
                JSONObject obj = arr.getJSONObject(i);

                user.setUsername(obj.getString("username"));
                user.setPassword(obj.getString("password"));
                user.setFirst(obj.getString("first_name"));
                user.setLast(obj.getString("last_name"));
                user.setEmail(obj.getString("email"));
                user.setPhone(obj.getString("phone"));
                user.setCovered(obj.getBoolean("covered"));
                user.setHandicap(obj.getBoolean("handicap"));
                user.setElectric(obj.getBoolean("electric"));
                user.setDistORprice(obj.getInt("dist_price"));
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return user;
    }
}
