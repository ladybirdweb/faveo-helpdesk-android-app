package co.helpdesk.faveo.backend.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import co.helpdesk.faveo.model.TicketDetail;
import co.helpdesk.faveo.model.TicketOverview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sumit
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FAVEO";

    private static final String TABLE_TICKET_OVERVIEW = "ticket_overview";
    private static final String CLIENT_PICTURE = "client_picture";
    private static final String CLIENT_NAME = "client_name";
    private static final String TICKET_SUBJECT = "ticket_subject";
    private static final String TICKET_TIME = "ticket_time";
    private static final String TICKET_BUBBLE = "ticket_bubble";

    private static final String TABLE_TICKET_DETAIL = "ticket_detail";
    private static final String ID = "id";
    private static final String TICKET_NUMBER = "ticket_number";
    private static final String USER_ID = "user_id";
    private static final String DEPT_ID = "dept_id";
    private static final String TEAM_ID = "team_id";
    private static final String PRIORITY_ID = "priority_id";
    private static final String SLA = "sla";
    private static final String HELP_TOPIC_ID = "helpTopic_id";
    private static final String STATUS = "status";
    private static final String FLAGS = "flags";
    private static final String IP_ADDRESS = "ip_address";
    private static final String ASSIGNED_TO = "assigned_to";
    private static final String LOCKED_BY = "locked_by";
    private static final String LOCKED_AT = "locked_at";
    private static final String SOURCE = "source";
    private static final String IS_OVERDUE = "is_overdue";
    private static final String RE_OPENED = "re_opened";
    private static final String IS_ANSWERED = "is_answered";
    private static final String HTML = "html";
    private static final String IS_DELETED = "is_deleted";
    private static final String CLOSED = "closed";
    private static final String REOPENED_AT = "reopened_at";
    private static final String DUE_DATE = "due_date";
    private static final String CLOSED_AT = "closed_at";
    private static final String LAST_MESSAGE_AT = "last_message_at";
    private static final String LAST_RESPONSE_AT = "last_response_at";
    private static final String CREATED_AT = "created_at";
    private static final String UPDATED_AT = "updated_at";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TICKET_OVERVIEW = "CREATE TABLE " + TABLE_TICKET_OVERVIEW + "("
                + ID + " INTEGER,"
                + CLIENT_PICTURE + " TEXT,"
                + TICKET_NUMBER + " TEXT,"
                + CLIENT_NAME + " TEXT,"
                + TICKET_SUBJECT + " TEXT,"
                + TICKET_TIME + " TEXT,"
                + TICKET_BUBBLE + " TEXT"
                + ")";
        db.execSQL(CREATE_TICKET_OVERVIEW);

        String CREATE_TICKET_DETAIL_TABLE = "CREATE TABLE " + TABLE_TICKET_DETAIL + "("
                + ID + " INTEGER,"
                + TICKET_NUMBER + " TEXT,"
                + USER_ID + " INTEGER,"
                + DEPT_ID + " INTEGER,"
                + TEAM_ID + " INTEGER,"
                + PRIORITY_ID + " INTEGER,"
                + SLA + " INTEGER,"
                + HELP_TOPIC_ID + " INTEGER,"
                + STATUS + " INTEGER,"
                + FLAGS + " INTEGER,"
                + IP_ADDRESS + " INTEGER,"
                + ASSIGNED_TO + " INTEGER,"
                + LOCKED_BY + " INTEGER,"
                + LOCKED_AT + " INTEGER,"
                + SOURCE + " INTEGER,"
                + IS_OVERDUE + " INTEGER,"
                + RE_OPENED + " INTEGER,"
                + IS_ANSWERED + " INTEGER,"
                + HTML + " INTEGER,"
                + IS_DELETED + " INTEGER,"
                + CLOSED + " INTEGER,"
                + REOPENED_AT + " TEXT,"
                + DUE_DATE + " TEXT,"
                + CLOSED_AT + " TEXT,"
                + LAST_MESSAGE_AT + " TEXT,"
                + LAST_RESPONSE_AT + " TEXT,"
                + CREATED_AT + " TEXT,"
                + UPDATED_AT + " TEXT"
                + ")";
        db.execSQL(CREATE_TICKET_DETAIL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_OVERVIEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_DETAIL);
        onCreate(db);
    }

    public void addTicketOverview(TicketOverview ticketOverview) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, ticketOverview.getTicketID());
        values.put(CLIENT_PICTURE, ticketOverview.getClientPicture());
        values.put(TICKET_NUMBER, ticketOverview.getTicketNumber());
        values.put(CLIENT_NAME, ticketOverview.getClientName());
        values.put(TICKET_SUBJECT, ticketOverview.getTicketSubject());
        values.put(TICKET_TIME, ticketOverview.getTicketTime());
        values.put(TICKET_BUBBLE, ticketOverview.getTicketBubble());
        db.insert(TABLE_TICKET_OVERVIEW, null, values);
        db.close();
    }

    public void addTicketDetail(TicketDetail ticketDetail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, ticketDetail.getID());
        values.put(TICKET_NUMBER, ticketDetail.getTicketNumber());
        values.put(USER_ID, ticketDetail.getUserID());
        values.put(DEPT_ID, ticketDetail.getDeptID());
        values.put(TEAM_ID, ticketDetail.getTeamID());
        values.put(PRIORITY_ID, ticketDetail.getPriorityID());
        values.put(SLA, ticketDetail.getSLA());
        values.put(HELP_TOPIC_ID, ticketDetail.getHelpTopicID());
        values.put(STATUS, ticketDetail.getStatus());
        values.put(FLAGS, ticketDetail.getFlags());
        values.put(IP_ADDRESS, ticketDetail.getIPAddress());
        values.put(ASSIGNED_TO, ticketDetail.getAssignedTo());
        values.put(LOCKED_BY, ticketDetail.getLockedBy());
        values.put(LOCKED_AT, ticketDetail.getLockedAt());
        values.put(SOURCE, ticketDetail.getSource());
        values.put(IS_OVERDUE, ticketDetail.getIsOverdue());
        values.put(RE_OPENED, ticketDetail.getReOpened());
        values.put(IS_ANSWERED, ticketDetail.getIsAnswered());
        values.put(HTML, ticketDetail.getHtml());
        values.put(IS_DELETED, ticketDetail.getIsDeleted());
        values.put(CLOSED, ticketDetail.getClosed());
        values.put(REOPENED_AT, ticketDetail.getReopenedAt());
        values.put(DUE_DATE, ticketDetail.getDueDate());
        values.put(CLOSED_AT, ticketDetail.getClosedAt());
        values.put(LAST_MESSAGE_AT, ticketDetail.getLastMessageAt());
        values.put(LAST_RESPONSE_AT, ticketDetail.getLastResponseAt());
        values.put(CREATED_AT, ticketDetail.getCreatedAt());
        values.put(UPDATED_AT, ticketDetail.getUpdatedAt());
        db.insert(TABLE_TICKET_DETAIL, null, values);
        db.close();
    }

    public List<TicketOverview> getTicketOverview() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TICKET_OVERVIEW, null, null, null, null, null, null, null);
        List<TicketOverview> ticketOverviewList = new ArrayList<>();
        if (cursor == null)
            return null;
        else if (cursor.moveToFirst()) {
            do {
                TicketOverview ticketOverview = new TicketOverview(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6));
                ticketOverviewList.add(ticketOverview);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ticketOverviewList;
    }

    public List<TicketDetail> getTicketDetail() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TICKET_DETAIL, null, null, null, null, null, null, null);
        List<TicketDetail> ticketDetailList = new ArrayList<>();
        if (cursor == null)
            return null;
        else if (cursor.moveToFirst()) {
                do {
                    TicketDetail ticketDetail = new TicketDetail(
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            cursor.getString(8),
                            cursor.getString(9),
                            cursor.getString(10),
                            cursor.getString(11),
                            cursor.getString(12),
                            cursor.getString(13),
                            cursor.getString(14),
                            cursor.getString(15),
                            cursor.getString(16),
                            cursor.getString(17),
                            cursor.getString(18),
                            cursor.getString(19),
                            cursor.getString(20),
                            cursor.getString(21),
                            cursor.getString(22),
                            cursor.getString(23),
                            cursor.getString(24),
                            cursor.getString(25),
                            cursor.getString(26),
                            cursor.getString(27));
                    ticketDetailList.add(ticketDetail);
                } while (cursor.moveToNext());
            }
        cursor.close();
        return ticketDetailList;
    }

    public void recreateTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_OVERVIEW);
        String CREATE_TICKET_OVERVIEW = "CREATE TABLE " + TABLE_TICKET_OVERVIEW + "("
                + ID + " INTEGER,"
                + CLIENT_PICTURE + " TEXT,"
                + TICKET_NUMBER + " TEXT,"
                + CLIENT_NAME + " TEXT,"
                + TICKET_SUBJECT + " TEXT,"
                + TICKET_TIME + " TEXT,"
                + TICKET_BUBBLE + " TEXT"
                + ")";
        db.execSQL(CREATE_TICKET_OVERVIEW);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_DETAIL);
        String CREATE_TICKET_DETAIL_TABLE = "CREATE TABLE " + TABLE_TICKET_DETAIL + "("
                + ID + " INTEGER,"
                + TICKET_NUMBER + " TEXT,"
                + USER_ID + " INTEGER,"
                + DEPT_ID + " INTEGER,"
                + TEAM_ID + " INTEGER,"
                + PRIORITY_ID + " INTEGER,"
                + SLA + " INTEGER,"
                + HELP_TOPIC_ID + " INTEGER,"
                + STATUS + " INTEGER,"
                + FLAGS + " INTEGER,"
                + IP_ADDRESS + " INTEGER,"
                + ASSIGNED_TO + " INTEGER,"
                + LOCKED_BY + " INTEGER,"
                + LOCKED_AT + " INTEGER,"
                + SOURCE + " INTEGER,"
                + IS_OVERDUE + " INTEGER,"
                + RE_OPENED + " INTEGER,"
                + IS_ANSWERED + " INTEGER,"
                + HTML + " INTEGER,"
                + IS_DELETED + " INTEGER,"
                + CLOSED + " INTEGER,"
                + REOPENED_AT + " TEXT,"
                + DUE_DATE + " TEXT,"
                + CLOSED_AT + " TEXT,"
                + LAST_MESSAGE_AT + " TEXT,"
                + LAST_RESPONSE_AT + " TEXT,"
                + CREATED_AT + " TEXT,"
                + UPDATED_AT + " TEXT"
                + ")";
        db.execSQL(CREATE_TICKET_DETAIL_TABLE);
    }

}
