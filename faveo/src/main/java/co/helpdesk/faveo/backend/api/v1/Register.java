package co.helpdesk.faveo.backend.api.v1;

import co.helpdesk.faveo.Constants;
import co.helpdesk.faveo.Preference;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit
 */
public class Register {

    static String apiKey;
    static String token;
    static String IP;

    public Register() {
        apiKey = Constants.API_KEY;
        token = Preference.getToken();
        IP = null;
    }

    public String postRegisterUser(String email, String password, int gender, String address,
                                    String firstName, String lastName, String mobile, String company) {
        String parameters = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("email", email);
            obj.put("password", password);
            obj.put("api_key", apiKey);
            obj.put("ip", IP);
            obj.put("gender", gender);
            obj.put("address", address);
            obj.put("first_name", firstName);
            obj.put("last_name", lastName);
            obj.put("mobile", mobile);
            obj.put("company", company);
            parameters = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = new HTTPConnection().HTTPResponsePost(Constants.URL + "register", parameters);
        if (result.equals("tokenRefreshed"))
            postRegisterUser(email, password, gender, address, firstName, lastName, mobile, company);
        return result;
    }
}
