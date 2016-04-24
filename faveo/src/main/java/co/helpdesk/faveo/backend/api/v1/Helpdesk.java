package co.helpdesk.faveo.backend.api.v1;


import co.helpdesk.faveo.Constants;
import co.helpdesk.faveo.Preference;

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

    public String getBaseURL(String companyURL) {
        return new HTTPConnection().HTTPResponseGet(companyURL + "api/v1/helpdesk/url?url=" + companyURL.substring(0, companyURL.length() - 1) + "&api_key=" + apiKey);
    }

    public String postCreateTicket(int userID, String subject, String body, int helpTopic,
                                   int sla, int priority, int dept) {
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
            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/create?" +
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
        return result;
    }

    public String postCreateInternalNote(int ticketID, int userID, String note) {
        String result = new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/internal-note?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&token=" + token +
                "&ticketID=" + ticketID +
                "&userID=" + userID +
                "&body=" + note, null);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/internal-note?" +
                    "api_key=" + apiKey +
                    "&ip=" + IP +
                    "&token=" + token +
                    "&ticketID=" + ticketID +
                    "&userID=" + userID +
                    "&body=" + note, null);
        return result;
    }

    public String postReplyTicket(int ticketID, String cc, String replyContent) {
        String result = new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/reply?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&token=" + token +
                "&ticket_ID=" + ticketID +
                "&cc=" + cc +
                "&reply_content=" + replyContent, null);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/reply?" +
                    "api_key=" + apiKey +
                    "&ip=" + IP +
                    "&token=" + token +
                    "&ticket_ID=" + ticketID +
                    "&cc=" + cc +
                    "&reply_content=" + replyContent, null);
        return result;
    }

    public String postEditTicket(int ticketID, String subject, int slaPlan, int helpTopic,
                                   int ticketSource, int ticketPriority) {
        String result = new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/edit?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&token=" + token +
                "&ticket_id=" + ticketID +
                "&subject=" + subject +
                "&sla_plan=" + slaPlan +
                "&help_topic=" + helpTopic +
                "&ticket_source=" + ticketSource +
                "&ticket_priority=" + ticketPriority, null);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/edit?" +
                    "api_key=" + apiKey +
                    "&ip=" + IP +
                    "&token=" + token +
                    "&ticket_id=" + ticketID +
                    "&subject=" + subject +
                    "&sla_plan=" + slaPlan +
                    "&help_topic=" + helpTopic +
                    "&ticket_source=" + ticketSource +
                    "&ticket_priority=" + ticketPriority, null);
        return result;
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
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/unassigned?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/unassigned?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        return result;
    }

    public String getClosedTicket() {
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/closed?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/closed?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        return result;
    }

    public String getTicketDetail(String ticketID) {
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/ticket?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&id=" + ticketID);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/ticket?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&id=" + ticketID);
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
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/customers?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&search=" + search);
        return result;
    }

    public String getCustomersOverview() {
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/customers-custom?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/customers-custom?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
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
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/ticket-thread?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&id=" + ticketID);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/ticket-thread?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&id=" + ticketID);
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
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/inbox?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/inbox?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        return result;
    }

    public String getTrashTickets() {
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/inbox?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/inbox?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        return result;
    }

    public String nextPageURL(String URL) {
        int lastSlash = URL.lastIndexOf("/");
        URL = URL.substring(0, lastSlash) + URL.substring(lastSlash + 1, URL.length());
        String result = new HTTPConnection().HTTPResponseGet(URL + "&api_key=" + apiKey + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(URL + "&api_key=" + apiKey + "&token=" + token);
        return result;
    }

    public String getMyTickets(String userID) {
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        return result;
    }

    public String getTicketsByAgent(String userID) {
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets-agent?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets-agent?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        return result;
    }

    public String getTicketsByUser(String userID) {
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets-user?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets-user?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        return result;
    }

    public String getDependency() {
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/dependency?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/dependency?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        return result;
    }

}
