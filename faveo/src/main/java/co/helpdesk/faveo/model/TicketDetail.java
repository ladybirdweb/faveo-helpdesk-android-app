package co.helpdesk.faveo.model;

/**
 * Created by Sumit
 */
public class TicketDetail {

    String ID;
    String ticketNumber;
    String userID;
    String deptID;
    String teamID;
    String priorityID;
    String SLA;
    String helpTopicID;
    String status;
    String flags;
    String IPaddress;
    String assignedTo;
    String lockedBy;
    String lockedAt;
    String source;
    String isOverdue;
    String reOpened;
    String isAnswered;
    String html;
    String isDeleted;
    String closed;
    String reopenedAt;
    String dueDate;
    String closedAt;
    String lastMessageAt;
    String lastResponseAt;
    String createdAt;
    String updatedAt;

    public TicketDetail(String ID, String ticketNumber, String userID, String deptID, String teamID, String priorityID, String SLA, String helpTopicID, String status, String flags, String IPaddress, String assignedTo, String lockedBy, String lockedAt, String source, String isOverdue, String reOpened, String isAnswered, String html, String isDeleted, String closed, String reopenedAt, String dueDate, String closedAt, String lastMessageAt, String lastResponseAt, String createdAt, String updatedAt) {
        this.ID = ID;
        this.ticketNumber = ticketNumber;
        this.userID = userID;
        this.deptID = deptID;
        this.teamID = teamID;
        this.priorityID = priorityID;
        this.SLA = SLA;
        this.helpTopicID = helpTopicID;
        this.status = status;
        this.flags = flags;
        this.IPaddress = IPaddress;
        this.assignedTo = assignedTo;
        this.lockedBy = lockedBy;
        this.lockedAt = lockedAt;
        this.source = source;
        this.isOverdue = isOverdue;
        this.reOpened = reOpened;
        this.isAnswered = isAnswered;
        this.html = html;
        this.isDeleted = isDeleted;
        this.closed = closed;
        this.reopenedAt = reopenedAt;
        this.dueDate = dueDate;
        this.closedAt = closedAt;
        this.lastMessageAt = lastMessageAt;
        this.lastResponseAt = lastResponseAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getID() {
        return ID;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getUserID() {
        return userID;
    }

    public String getDeptID() {
        return deptID;
    }

    public String getTeamID() {
        return teamID;
    }

    public String getPriorityID() {
        return priorityID;
    }

    public String getSLA() {
        return SLA;
    }

    public String getHelpTopicID() {
        return helpTopicID;
    }

    public String getStatus() {
        return status;
    }

    public String getFlags() {
        return flags;
    }

    public String getIPAddress() {
        return IPaddress;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public String getLockedAt() {
        return lockedAt;
    }

    public String getSource() {
        return source;
    }

    public String getIsOverdue() {
        return isOverdue;
    }

    public String getReOpened() {
        return reOpened;
    }

    public String getIsAnswered() {
        return isAnswered;
    }

    public String getHtml() {
        return html;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public String getClosed() {
        return closed;
    }

    public String getReopenedAt() {
        return reopenedAt;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getClosedAt() {
        return closedAt;
    }

    public String getLastMessageAt() {
        return lastMessageAt;
    }

    public String getLastResponseAt() {
        return lastResponseAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public void setPriorityID(String priorityID) {
        this.priorityID = priorityID;
    }

    public void setSLA(String SLA) {
        this.SLA = SLA;
    }

    public void setHelpTopicID(String helpTopicID) {
        this.helpTopicID = helpTopicID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public void setIPaddress(String IPaddress) {
        this.IPaddress = IPaddress;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public void setLockedAt(String lockedAt) {
        this.lockedAt = lockedAt;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setIsOverdue(String isOverdue) {
        this.isOverdue = isOverdue;
    }

    public void setReOpened(String reOpened) {
        this.reOpened = reOpened;
    }

    public void setIsAnswered(String isAnswered) {
        this.isAnswered = isAnswered;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    public void setReopenedAt(String reopenedAt) {
        this.reopenedAt = reopenedAt;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setClosedAt(String closedAt) {
        this.closedAt = closedAt;
    }

    public void setLastMessageAt(String lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public void setLastResponseAt(String lastResponseAt) {
        this.lastResponseAt = lastResponseAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
