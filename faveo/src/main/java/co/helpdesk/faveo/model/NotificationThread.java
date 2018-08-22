package co.helpdesk.faveo.model;

/**
 * Created by narendra on 09/12/16.
 * Model class for notification.
 */
public class NotificationThread {

    public String profiel_pic;
    public String noti_time;
    public String noti_seen;
    public int ticket_id;
    public String ticket_subject;
    public String placeHolder;
    public String noti_scenario;
    public int client_id;
    public int noti_id;
    public String requesterName;

    public NotificationThread(String profiel_pic, String noti_time, int ticket_id, String ticket_subject, String placeHolder, String noti_scenario, int client_id, int noti_id, String noti_seen, String requesterName) {
        this.profiel_pic = profiel_pic;
        this.noti_time = noti_time;
        this.ticket_id = ticket_id;
        this.ticket_subject = ticket_subject;
        this.placeHolder = placeHolder;
        this.noti_scenario = noti_scenario;
        this.client_id = client_id;
        this.noti_id = noti_id;
        this.noti_seen = noti_seen;
        this.requesterName = requesterName;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getNoti_seen() {
        return noti_seen;
    }

    public void setNoti_seen(String noti_seen) {
        this.noti_seen = noti_seen;
    }

    public int getNoti_id() {
        return noti_id;
    }

    public void setNoti_id(int noti_id) {
        this.noti_id = noti_id;
    }

    public int getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(int ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getNoti_scenario() {
        return noti_scenario;
    }

    public void setNoti_scenario(String noti_scenario) {
        this.noti_scenario = noti_scenario;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    public String getTicket_subject() {
        return ticket_subject;
    }

    public void setTicket_subject(String ticket_subject) {
        this.ticket_subject = ticket_subject;
    }


    public String getProfiel_pic() {
        return profiel_pic;
    }

    public void setProfiel_pic(String profiel_pic) {
        this.profiel_pic = profiel_pic;
    }

    public String getNoti_time() {
        return noti_time;
    }

    public void setNoti_time(String noti_time) {
        this.noti_time = noti_time;
    }

}
