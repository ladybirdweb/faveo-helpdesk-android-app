package com.android.faveo.backend.api.v1;


import com.android.faveo.Constants;
import com.android.faveo.Helper;
import com.android.faveo.Preference;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sumit
 */
public class Helpdesk {

    static String apiKey;
    static String token;
    static String IP;

    public Helpdesk() {
        apiKey = Constants.API_KEY;
        token = Preference.getToken();
        IP = null;
    }

    public String getBaseURL(String apiKey, String IP) {
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
        return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/url?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
    }

    public String postCreateTicket(int userID, String subject, String body, int helpTopic,
                                   int sla, int priority, int dept) {
        String parameters = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("api_key", apiKey);
            obj.put("ip", IP);
            obj.put("user_id", userID);
            obj.put("subject", subject);
            obj.put("body", body);
            obj.put("helptopic", helpTopic);
            obj.put("sla", sla);
            obj.put("priority", priority);
            obj.put("dept", dept);
            obj.put("token", token);
            parameters = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/create?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&user_id=" + userID +
                "&subject=" + subject +
                "&body=" + body +
                "&helptopic=" + helpTopic +
                "&sla=" + sla +
                "&priority=" + priority +
                "&dept=" + dept +
                "&token=" + token , null);
        if (result != null && result.equals("tokenRefreshed"))
            postCreateTicket(userID, subject, body, helpTopic, sla, priority, dept);
        return result;
    }

    public String postReplyTicket(int ticketID, String replyContent) {
        String parameters = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("api_key", apiKey);
            obj.put("ip", IP);
            obj.put("ticket_id", ticketID);
            obj.put("ReplyContent", replyContent);
            obj.put("token", token);
            parameters = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/create", parameters);
    }

    public String postEditTicket(int ticketID, String subject, int slaPlan, int helpTopic,
                                   int ticketSource, int ticketPriority) {
        String parameters = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("api_key", apiKey);
            obj.put("ip", IP);
            obj.put("ticket_id", ticketID);
            obj.put("subject", subject);
            obj.put("sla_plan", slaPlan);
            obj.put("help_topic", helpTopic);
            obj.put("ticket_source", ticketSource);
            obj.put("ticket_priority", ticketPriority);
            obj.put("token", token);
            parameters = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/edit", parameters);
    }

    public String postDeleteTicket(int ticketID) {
        String parameters = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("api_key", apiKey);
            obj.put("ip", IP);
            obj.put("ticket_ID", ticketID);
            obj.put("token", token);
            parameters = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/delete", parameters);
    }

    public String getOpenTicket() {
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
        return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/open?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
    }

    public String getUnassignedTicket() {
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
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/unassigned?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            getUnassignedTicket();
        return result;
    }

    public String getClosedTicket() {
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
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/closed?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            getClosedTicket();
        return result;
    }

    public String getTicketDetail(String ticketID) {
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
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/ticket?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&id=" + ticketID);
        if (result != null && result.equals("tokenRefreshed"))
            getClosedTicket();
        return result;
    }

    public String getAgents() {
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
        return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/agents?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
    }

    public String getTeams() {
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
        return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/teams?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
    }

    public String getCustomers(String search) {
        String parameters = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("api_key", apiKey);
            obj.put("ip", IP);
            obj.put("token", token);
            obj.put("search", search);
            parameters = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/customers?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&search=" + search);
        if (result != null && result.equals("tokenRefreshed"))
            getCustomers(search);
        return result;
    }

    public String getCustomersOverview() {
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
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/customers-custom?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            getCustomersOverview();
        return result;
    }

    public String getCustomer(int userID) {
        String parameters = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("api_key", apiKey);
            obj.put("ip", IP);
            obj.put("token", token);
            obj.put("user_id", userID);
            parameters = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/customer?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
    }

    public String getTicket(String search) {
        String parameters = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("api_key", apiKey);
            obj.put("ip", IP);
            obj.put("token", token);
            obj.put("search", search);
            parameters = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/ticket-search?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&search=" + search);
    }

    public String getTicketThread(String ticketID) {
        String parameters = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("api_key", apiKey);
            obj.put("ip", IP);
            obj.put("token", token);
            obj.put("id", ticketID);
            parameters = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/ticket-thread?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&id=" + ticketID);
        if (result != null && result.equals("tokenRefreshed"))
            getTicketThread(ticketID);
        return result;
    }

    public String postAssignTicket(int id) {
        String parameters = null;
        JSONObject obj = new JSONObject();
        try {
            obj.put("api_key", apiKey);
            obj.put("ip", IP);
            obj.put("token", token);
            obj.put("id", id);
            parameters = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/assign", parameters);
    }

    public String getInboxTicket() {
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
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/inbox?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            getInboxTicket();
        return result;
    }

    public String getTrashTickets() {
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
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/inbox?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            getTrashTickets();
        return result;
    }

    public String nextPageURL(String URL) {
        String rawURL = URL;
        int lastSlash = URL.lastIndexOf("/");
        URL = URL.substring(0, lastSlash) + URL.substring(lastSlash + 1, URL.length());
        String result = new HTTPConnection().HTTPResponseGet(URL + "&api_key=" + apiKey + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            nextPageURL(rawURL);
        return result;
    }

    public String getMyTickets(String userID) {
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
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user-id=" + userID);
        if (result != null && result.equals("tokenRefreshed"))
            getMyTickets(userID);
        return result;
    }

}
