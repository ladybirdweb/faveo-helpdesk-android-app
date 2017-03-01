package co.helpdesk.faveo.backend.api.v1;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import co.helpdesk.faveo.Constants;
import co.helpdesk.faveo.Preference;

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
        Log.d("verifyURLAPI", companyURL + "api/v1/helpdesk/url?url=" + companyURL.substring(0, companyURL.length() - 1) + "&api_key=" + apiKey);
        return new HTTPConnection().HTTPResponseGet(companyURL + "api/v1/helpdesk/url?url=" + companyURL.substring(0, companyURL.length() - 1) + "&api_key=" + apiKey);
    }

    public String postCreateTicket(int userID, String subject, String body, int helpTopic,
                                   int sla, int priority, int dept, String fname, String lname, String phone, String email, String code) {
        Log.d("postCreateTicketAPI", Constants.URL + "helpdesk/create?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&user_id=" + userID +
                "&subject=" + subject +
                "&body=" + body +
                "&helptopic=" + helpTopic +
                "&sla=" + sla +
                "&priority=" + priority +
                "&dept=" + dept +
                "&first_name=" + fname +
                "&last_name=" + lname +
                "&phone=" + phone +
                "&code=" + code +
                "&email=" + email +
                "&token=" + token);

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
                "&first_name=" + fname +
                "&last_name=" + lname +
                "&phone=" + phone +
                "&code=" + code +
                "&email=" + email +
                "&token=" + token, null);

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
                    "&first_name=" + fname +
                    "&last_name=" + lname +
                    "&phone=" + phone +
                    "&code=" + code +
                    "&email=" + email +
                    "&token=" + token, null);
        return result;
    }

    public String postCreateInternalNote(int ticketID, int userID, String note) {
        Log.d("CreateInternalNoteAPI", Constants.URL + "helpdesk/internal-note?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&token=" + token +
                "&ticketid=" + ticketID +
                "&userid=" + userID +
                "&body=" + note);
        String result = new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/internal-note?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&token=" + token +
                "&ticketid=" + ticketID +
                "&userid=" + userID +
                "&body=" + note, null);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/internal-note?" +
                    "api_key=" + apiKey +
                    "&ip=" + IP +
                    "&token=" + token +
                    "&ticketid=" + ticketID +
                    "&userid=" + userID +
                    "&body=" + note, null);
        return result;
    }

    public String postReplyTicket(int ticketID, String cc, String replyContent) {
        Log.d("ReplyTicketAPI", Constants.URL + "helpdesk/reply?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&token=" + token +
                "&ticket_ID=" + ticketID +
                "&cc=" + cc +
                "&reply_content=" + replyContent);
        String result = new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/reply?" +
                        "api_key=" + apiKey +
                        "&ip=" + IP +
                        "&token=" + token +
                        "&ticket_ID=" + ticketID +
                        "&cc=" + cc +
                        "&reply_content=" + replyContent,
                null);
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
                                 int ticketSource, int ticketPriority, int ticketStatus) {
        Log.d("EditTicketAPI", Constants.URL + "helpdesk/edit?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&token=" + token +
                "&ticket_id=" + ticketID +
                "&subject=" + subject +
                "&sla_plan=" + slaPlan +
                "&help_topic=" + helpTopic +
                "&ticket_source=" + ticketSource +
                "&ticket_priority=" + ticketPriority +
                "&status=" + ticketStatus);
        String result = new HTTPConnection().HTTPResponsePost(Constants.URL + "helpdesk/edit?" +
                "api_key=" + apiKey +
                "&ip=" + IP +
                "&token=" + token +
                "&ticket_id=" + ticketID +
                "&subject=" + subject +
                "&sla_plan=" + slaPlan +
                "&help_topic=" + helpTopic +
                "&ticket_source=" + ticketSource +
                "&ticket_priority=" + ticketPriority +
                "&status=" + ticketStatus, null);
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
                    "&ticket_priority=" + ticketPriority +
                    "&status=" + ticketStatus, null);
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
        Log.d("UnassignedTicketAPI", Constants.URL + "helpdesk/unassigned?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/unassigned?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/unassigned?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        return result;
    }

    public String getClosedTicket() {
        Log.d("ClosedTicketAPI", Constants.URL + "helpdesk/closed?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/closed?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/closed?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        return result;
    }

    public String getTicketDetail(String ticketID) {
        Log.d("TicketDetailAPI", Constants.URL + "helpdesk/ticket?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&id=" + ticketID);
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
        Log.d("CustomersOverviewAPI", Constants.URL + "helpdesk/customers-custom?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
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
        Log.d("TicketThreadAPI", Constants.URL + "helpdesk/ticket-thread?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&id=" + ticketID);
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
        Log.d("InboxTicketAPI", Constants.URL + "helpdesk/inbox?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/inbox?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/inbox?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        return result;
    }

    public String getTrashTickets() {
        Log.d("TrashTicketsAPI", Constants.URL + "helpdesk/trash?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/trash?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/trash?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        return result;
    }

    public String nextPageURL(String URL) {
        //Log.d("URL",URL);
        // int lastSlash = URL.lastIndexOf("/");
        // URL = URL.substring(0, lastSlash) + URL.substring(lastSlash + 1, URL.length());
        Log.d("nextPageURLAPI", URL + "&api_key=" + apiKey + "&token=" + token);
        String result = new HTTPConnection().HTTPResponseGet(URL + "&api_key=" + apiKey + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(URL + "&api_key=" + apiKey + "&token=" + token);
        return result;
    }

    public String nextPageURL(String URL, String userID) {

        Log.d("nextPageURLAPI", URL + "&api_key=" + apiKey + "&token=" + token + "&user_id=" + userID);
        String result = new HTTPConnection().HTTPResponseGet(URL + "&api_key=" + apiKey + "&token=" + token + "&user_id=" + userID);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(URL + "&api_key=" + apiKey + "&token=" + token + "&user_id=" + userID);
        return result;
    }

    public String getMyTickets(String userID) {
        Log.d("MyTicketsAPI", Constants.URL + "helpdesk/my-tickets?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        return result;
    }

    public String getTicketsByAgent(String userID) {
        Log.d("TicketsByAgentAPI", Constants.URL + "helpdesk/my-tickets-agent?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets-agent?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets-agent?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        return result;
    }

    public String getTicketsByUser(String userID) {
        Log.d("TicketsByUserAPI", Constants.URL + "helpdesk/my-tickets-user?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets-user?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/my-tickets-user?api_key=" + apiKey + "&ip=" + IP + "&token=" + token + "&user_id=" + userID);
        return result;
    }

    public String getDependency() {
        Log.d("DependencyAPI", Constants.URL + "helpdesk/dependency?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        String result = new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/dependency?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        if (result != null && result.equals("tokenRefreshed"))
            return new HTTPConnection().HTTPResponseGet(Constants.URL + "helpdesk/dependency?api_key=" + apiKey + "&ip=" + IP + "&token=" + token);
        return result;
    }

}
