package co.helpdesk.faveo.model;

/**
 * Created by Sumit
 */
public class TicketThread {

    public String clientPicture;
    public String clientName;
    public String messageTime;
    public String messageTitle;
    public String message;
    public String isReply;

    public TicketThread(String clientName, String messageTime, String message, String isReply) {
        this.messageTime = messageTime;
        this.message = message;
        this.isReply = isReply;
        this.clientName = clientName;
    }

    public TicketThread(String clientPicture, String clientName, String messageTime, String messageTitle, String message, String isReply) {
        this.clientPicture = clientPicture;
        this.clientName = clientName;
        this.messageTime = messageTime;
        this.messageTitle = messageTitle;
        this.message = message;
        this.isReply = isReply;
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

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIsReply() {
        return isReply;
    }

    public void setIsReply(String isReply) {
        this.isReply = isReply;
    }
}
