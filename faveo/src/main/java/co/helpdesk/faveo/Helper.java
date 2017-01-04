package co.helpdesk.faveo;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import co.helpdesk.faveo.model.ClientOverview;
import co.helpdesk.faveo.model.TicketOverview;

/**
 * Created by sumit on 1/26/2016.
 */
public class Helper {

    public static TicketOverview parseTicketOverview(JSONArray jsonArray, int i) {
        try {
            Date updated_at = null;
            String firstName = jsonArray.getJSONObject(i).getString("first_name");
            String lastName = jsonArray.getJSONObject(i).getString("last_name");
            String username = jsonArray.getJSONObject(i).getString("user_name");
            String email = jsonArray.getJSONObject(i).getString("email");
            String profilePic = jsonArray.getJSONObject(i).getString("profile_pic");
            String ticketNumber = jsonArray.getJSONObject(i).getString("ticket_number");
            String ID = jsonArray.getJSONObject(i).getString("id");
            String title = jsonArray.getJSONObject(i).getString("title");
            String createdAt = jsonArray.getJSONObject(i).getString("created_at");
            String departmentName = jsonArray.getJSONObject(i).getString("department_name");
            String priorityName = jsonArray.getJSONObject(i).getString("priotity_name");
            String slaPlanName = jsonArray.getJSONObject(i).getString("sla_plan_name");
            String helpTopicName = jsonArray.getJSONObject(i).getString("help_topic_name");
            String ticketStatusName = jsonArray.getJSONObject(i).getString("ticket_status_name");
            String updatedAt = jsonArray.getJSONObject(i).getString("updated_at");

            return new TicketOverview(Integer.parseInt(ID), profilePic,
                    ticketNumber, firstName + " " + lastName, title, updatedAt, i + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ClientOverview parseClientOverview(JSONArray jsonArray, int i) {
        try {
            String clientID = jsonArray.getJSONObject(i).getString("id");
            String clientPicture = jsonArray.getJSONObject(i).getString("profile_pic");
            String userName = jsonArray.getJSONObject(i).getString("user_name");
            String firstName = jsonArray.getJSONObject(i).getString("first_name");
            String lastName = jsonArray.getJSONObject(i).getString("last_name");
            String clientEmail = jsonArray.getJSONObject(i).getString("email");
            String clientPhone = jsonArray.getJSONObject(i).getString("phone_number");
            String clientCompany = jsonArray.getJSONObject(i).getString("company");
            String clientActive = jsonArray.getJSONObject(i).getString("active");

            return new ClientOverview(Integer.parseInt(clientID), clientPicture,
                    firstName + " " + lastName, clientEmail, clientPhone, clientCompany, clientActive);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ClientOverview parseClientTickets(JSONArray jsonArray, int i) {
        try {
            String clientID = jsonArray.getJSONObject(i).getString("id");
            String clientPicture = jsonArray.getJSONObject(i).getString("profile_pic");
            String firstName = jsonArray.getJSONObject(i).getString("first_name");
            String lastName = jsonArray.getJSONObject(i).getString("last_name");
            String clientEmail = jsonArray.getJSONObject(i).getString("email");
            String clientPhone = jsonArray.getJSONObject(i).getString("phone_number");
            String clientCompany = jsonArray.getJSONObject(i).getString("company");
            String clientActive = jsonArray.getJSONObject(i).getString("active");

            return new ClientOverview(Integer.parseInt(clientID), clientPicture,
                    firstName + " " + lastName, clientEmail, clientPhone, clientCompany, clientActive);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long relativeTime(String dateToParse) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date d = null;
        try {
            d = sdf.parse(dateToParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat output = new SimpleDateFormat("d MMM yyyy  HH:mm");
        output.setTimeZone(TimeZone.getDefault());

        String formattedTime = output.format(d);
        Date gg = null;
        try {
            gg = output.parse(formattedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // SimpleDateFormat day = new SimpleDateFormat("dd");
//            String formattedDay = day.format(d) + Helper.getDayOfMonthSuffix(Integer.parseInt(day.format(d)));
//            formattedTime = formattedTime.replaceFirst(formattedTime.substring(0, formattedTime.indexOf(" ")), formattedDay);
//            sdf.parse(dateToParse);
        return gg != null ? gg.getTime() : 0;
    }

    public static String parseDate(String dateToParse) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date dAte = sdf.parse(dateToParse);

            SimpleDateFormat output = new SimpleDateFormat("d MMM yyyy  HH:mm");
            output.setTimeZone(TimeZone.getDefault());

            String formattedTime = output.format(dAte);
            SimpleDateFormat day = new SimpleDateFormat("dd");
            String formattedDay = day.format(dAte) + Helper.getDayOfMonthSuffix(Integer.parseInt(day.format(dAte)));
            formattedTime = formattedTime.replaceFirst(formattedTime.substring(0, formattedTime.indexOf(" ")), formattedDay);
            sdf.parse(dateToParse);
            return formattedTime;
        } catch (ParseException e) {
            e.printStackTrace();
            return dateToParse;
        }
    }

    public static String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
