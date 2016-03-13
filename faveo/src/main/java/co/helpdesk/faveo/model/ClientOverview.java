package co.helpdesk.faveo.model;

/**
 * Created by Sumit
 */
public class ClientOverview {

    public int clientID;
    public String clientPicture;
    public String clientName;
    public String clientEmail;
    public String clientPhone;

    public ClientOverview(int clientID, String clientPicture, String clientName, String clientEmail, String clientPhone) {
        this.clientID = clientID;
        this.clientPicture = clientPicture;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.clientPhone = clientPhone;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getClientPicture() {
        return clientPicture;
    }

    public void setClientPicture(String clientPicture) {
        this.clientPicture = clientPicture;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }
}
