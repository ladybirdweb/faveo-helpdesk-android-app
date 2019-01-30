package co.helpdesk.faveo.model;

/**
 * Created by Sumit
 * Model class for ticket glimpse.
 */
public class TicketGlimpse {

    public int ticketID;
    public String ticketNumber;
    public String ticketSubject;
    public String ticketStatusName;
    public TicketGlimpse(int ticketID, String ticketNumber, String ticketSubject,String ticketStatusName) {
        this.ticketID = ticketID;
        this.ticketNumber = ticketNumber;
        this.ticketSubject = ticketSubject;
        this.ticketStatusName=ticketStatusName;
    }

    public String getTicketStatusName() {
        return ticketStatusName;
    }

    public void setTicketStatusName(String ticketStatusName) {
        this.ticketStatusName = ticketStatusName;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setTicketID(int ticketID) {
        this.ticketID = ticketID;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getTicketSubject() {
        return ticketSubject;
    }

    public void setTicketSubject(String ticketSubject) {
        this.ticketSubject = ticketSubject;
    }

}
