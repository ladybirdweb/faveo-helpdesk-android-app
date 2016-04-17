package co.helpdesk.faveo.frontend.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import co.helpdesk.faveo.Constants;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.backend.database.DatabaseHandler;
import co.helpdesk.faveo.model.TicketOverview;
import co.helpdesk.faveo.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SplashActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    public static String
            keyDepartment = "", valueDepartment = "",
            keySLA = "", valueSLA = "",
            keyStatus = "", valueStatus = "",
            keyStaff = "", valueStaff = "",
            keyTeam = "", valueTeam = "",
            keyPriority = "", valuePriority = "",
            keyTopic = "", valueTopic = "",
            keySource = "", valueSource = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        new FetchDependency(this).execute();

    }

    public class FetchDependency extends AsyncTask<String, Void, String> {
        Context context;

        public FetchDependency(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            progressDialog.dismiss();
            return new Helpdesk().getDependency();
        }

        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(SplashActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                JSONArray jsonArrayDepartments = jsonObject1.getJSONArray("departments");
                for (int i = 0; i < jsonArrayDepartments.length(); i++) {
                    keyDepartment += jsonArrayDepartments.getJSONObject(i).getString("id") + ",";
                    valueDepartment += jsonArrayDepartments.getJSONObject(i).getString("name") + ",";
                }

                JSONArray jsonArraySla = jsonObject1.getJSONArray("sla");
                for (int i = 0; i < jsonArraySla.length(); i++) {
                    keySLA += jsonArraySla.getJSONObject(i).getString("id") + ",";
                    valueSLA += jsonArraySla.getJSONObject(i).getString("name") + ",";
                }

                JSONArray jsonArrayStaffs = jsonObject1.getJSONArray("staffs");
                for (int i = 0; i < jsonArrayStaffs.length(); i++) {
                    keyStaff += jsonArrayStaffs.getJSONObject(i).getString("id") + ",";
                    valueStaff += jsonArrayStaffs.getJSONObject(i).getString("email") + ",";
                }

                JSONArray jsonArrayTeams = jsonObject1.getJSONArray("teams");
                for (int i = 0; i < jsonArrayTeams.length(); i++) {
                    keyTeam += jsonArrayTeams.getJSONObject(i).getString("id") + ",";
                    valueTeam += jsonArrayTeams.getJSONObject(i).getString("name") + ",";
                }

                JSONArray jsonArrayPriorities = jsonObject1.getJSONArray("priorities");
                for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                    keyPriority += jsonArrayPriorities.getJSONObject(i).getString("priority_id") + ",";
                    valuePriority += jsonArrayPriorities.getJSONObject(i).getString("priority") + ",";
                }

                JSONArray jsonArrayHelpTopics = jsonObject1.getJSONArray("helptopics");
                for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {
                    keyTopic += jsonArrayHelpTopics.getJSONObject(i).getString("id") + ",";
                    valueTopic += jsonArrayHelpTopics.getJSONObject(i).getString("topic") + ",";
                }

                JSONArray jsonArrayStatus = jsonObject1.getJSONArray("status");
                for (int i = 0; i < jsonArrayStatus.length(); i++) {
                    keyStatus += jsonArrayStatus.getJSONObject(i).getString("id") + ",";
                    valueStatus += jsonArrayStatus.getJSONObject(i).getString("name") + ",";
                }

                JSONArray jsonArraySources = jsonObject1.getJSONArray("sources");
                for (int i = 0; i < jsonArraySources.length(); i++) {
                    keySource += jsonArraySources.getJSONObject(i).getString("id") + ",";
                    valueSource += jsonArraySources.getJSONObject(i).getString("name") + ",";
                }
                new FetchData(context).execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class FetchData extends AsyncTask<String, Void, String> {
        Context context;
        String nextPageURL;

        public FetchData(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            progressDialog.dismiss();
            String result = new Helpdesk().getInboxTicket();
            if (result == null)
                return null;
            DatabaseHandler databaseHandler = new DatabaseHandler(context);
            databaseHandler.recreateTable();
            try {
                JSONObject jsonObject = new JSONObject(result);
                nextPageURL = jsonObject.getString("next_page_url");
                String data = jsonObject.getString("data");
                JSONArray jsonArray = new JSONArray(data);
                for(int i = 0; i < jsonArray.length(); i++) {
                    TicketOverview ticketOverview = Helper.parseTicketOverview(jsonArray, i);
                    if(ticketOverview != null)
                        databaseHandler.addTicketOverview(ticketOverview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            databaseHandler.close();
            return "success";

        }

        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(SplashActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("nextPageURL", nextPageURL);
            startActivity(intent);
        }
    }

}
