package co.helpdesk.faveo.backend.api.v1;


import co.helpdesk.faveo.Constants;
import co.helpdesk.faveo.Preference;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit
 */
public class Authenticate {

    static String apiKey;
    static String token;
    static String IP;

    public Authenticate() {
        apiKey = Constants.API_KEY;
        token = Preference.getToken();
        IP = null;
    }

    public String postAuthenticateUser(String username, String password) {
        String parameters = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("username", username);
            obj.put("password", password);
            obj.put("api_key", apiKey);
            obj.put("ip", IP);
            parameters = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new HTTPConnection().HTTPResponsePost(Constants.URL + "authenticate", parameters);
    }

    public String getAuthenticatedUser() {
        String parameters = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("api_key", apiKey);
            obj.put("ip", IP);
            obj.put("token", token);
            parameters = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new HTTPConnection().HTTPResponseGet(Constants.URL + "authenticate/user?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
    }


}
