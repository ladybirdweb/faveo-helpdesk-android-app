package co.helpdesk.faveo.model;

/**
 * Created by Sumit
 * Model class for ticket overview.
 */
public class TicketOverview {

    public int ticketID;
    public String clientPicture;
    public String ticketNumber;
    public String clientName;
    public String ticketSubject;
    public String ticketTime;
    public String ticketBubble;
    public String ticketStatus;
    public String ticketPriorityColor;
    public String ticketAttachments;
    public String dueDate;
    public String placeholder;

    public TicketOverview(int ticketID, String clientPicture, String ticketNumber, String clientName, String ticketSubject, String ticketTime, String ticketPriorityColor, String ticketStatus, String ticketBubble, String ticketAttachments, String dueDate, String placeholder) {
        this.ticketID = ticketID;
        this.clientPicture = clientPicture;
        this.ticketNumber = ticketNumber;
        this.clientName = clientName;
        this.ticketSubject = ticketSubject;
        this.ticketTime = ticketTime;
        this.ticketBubble = ticketBubble;
        this.ticketPriorityColor = ticketPriorityColor;
        this.ticketStatus = ticketStatus;
        this.ticketAttachments = ticketAttachments;
        this.dueDate = dueDate;
        this.placeholder = placeholder;

    }


    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getTicketAttachments() {
        return ticketAttachments;
    }

    public void setTicketAttachments(String ticketAttachments) {
        this.ticketAttachments = ticketAttachments;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getTicketPriority() {
        return ticketPriorityColor;
    }

    public void setTicketPriority(String ticketPriority) {
        this.ticketPriorityColor = ticketPriority;
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
