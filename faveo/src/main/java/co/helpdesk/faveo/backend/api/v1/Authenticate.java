package co.helpdesk.faveo.backend.api.v1;


import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import co.helpdesk.faveo.Constants;
//import co.helpdesk.faveo.Preference;

/**
 * Created by Sumit
 */
public class Authenticate {

    public static String apiKey;
   public static String token;
    public static String IP;

    public Authenticate() {
        apiKey = Constants.API_KEY;
        token = Prefs.getString("TOKEN", "");
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
        Log.d("Authenticate-URL :", Constants.URL + "authenticate" + parameters);
        return new HTTPConnection().HTTPResponsePost(Constants.URL + "authenticate", parameters);
    }

}
