package co.helpdesk.faveo.model;

/**
 * Created by Sumit
 */
public class TicketOverview {

    public int ticketID;
    public String clientPicture;
    public String ticketNumber;
    public String clientName;
    public String ticketSubject;
    public String ticketTime;
    public String ticketBubble;

    public TicketOverview(int ticketID, String clientPicture, String ticketNumber, String clientName, String ticketSubject, String ticketTime, String ticketBubble) {
        this.ticketID = ticketID;
        this.clientPicture = clientPicture;
        this.ticketNumber = ticketNumber;
        this.clientName = clientName;
        this.ticketSubject = ticketSubject;
        this.ticketTime = ticketTime;
        this.ticketBubble = ticketBubble;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getClientPicture() {
        return clientPicture;
    }

    public void setClientPicture(String clientPicture) {
        this.clientPicture = clientPicture;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTicketSubject() {
        return ticketSubject;
    }

    public void setTicketSubject(String ticketSubject) {
        this.ticketSubject = ticketSubject;
    }

    public String getTicketTime() {
        return ticketTime;
    }

    public void setTicketTime(String ticketTime) {
        this.ticketTime = ticketTime;
    }

    public String getTicketBubble() {
        return ticketBubble;
    }

    public void setTicketBubble(String ticketBubble) {
        this.ticketBubble = ticketBubble;
    }
}
