package com.android.faveo.frontend.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.faveo.Constants;
import com.android.faveo.Preference;
import com.android.faveo.R;
import com.android.faveo.backend.api.v1.Helpdesk;
import com.android.faveo.backend.database.DatabaseHandler;
import com.android.faveo.model.TicketOverview;
import com.android.faveo.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SplashActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        new FetchData(this).execute();

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
