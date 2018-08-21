package co.helpdesk.faveo.frontend.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.Constants;
import co.helpdesk.faveo.FaveoApplication;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.model.MessageEvent;
import es.dmoral.toasty.Toasty;

/**
 * This splash activity is responsible for
 * getting the metadata of our faveo application from the dependency API.
 */
public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.progressBar1)
    ProgressBar progressDialog;

    @BindView(R.id.loading)
    TextView loading;

    @BindView(R.id.tryagain)
    Button buttonTryAgain;

    @BindView(R.id.clear_cache)
    Button buttonClearCache;

    public static String
            keyDepartment = "", valueDepartment = "",
            keySLA = "", valueSLA = "",
            keyStatus = "", valueStatus = "",
            keyStaff = "", valueStaff = "",
            keyTeam = "", valueTeam = "",
            keyName="",
            keyPriority = "", valuePriority = "",
            keyTopic = "", valueTopic = "",
            keySource = "", valueSource = "",
            keyType = "", valueType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_in_from_right);
        ButterKnife.bind(this);
        Window window = SplashActivity.this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(SplashActivity.this,R.color.faveo));
        //welcomeDialog=new WelcomeDialog();

        if (InternetReceiver.isConnected()) {
            progressDialog.setVisibility(View.VISIBLE);
            new FetchDependency().execute();
            //new FetchData(this).execute();
        } else {
            progressDialog.setVisibility(View.INVISIBLE);
            loading.setText(getString(R.string.oops_no_internet));
            buttonTryAgain.setVisibility(View.VISIBLE);
        }

        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                Intent intent=new Intent(SplashActivity.this,SplashActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * This dependency is for getting the meta data about
     * our faveo application.From here we will get sla,priority
     * help topics.
     */
    private class FetchDependency extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            return new Helpdesk().getDependency();

        }

        protected void onPostExecute(String result) {
            Log.d("Depen Response : ", result + "");

            if (result == null) {
                loading.setText("Oops! Something went wrong, \nplease try again later.");
                progressDialog.setVisibility(View.INVISIBLE);
                buttonClearCache.setVisibility(View.VISIBLE);
                buttonTryAgain.setVisibility(View.VISIBLE);

                buttonClearCache.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FaveoApplication.getInstance().clearApplicationData();
                        String url=Prefs.getString("BASE_URL",null);
                        Prefs.clear();
                        Prefs.putString("BASE_URL",url);
                        SplashActivity.this.getSharedPreferences(Constants.PREFERENCE, Context.MODE_PRIVATE).edit().clear().apply();
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

                buttonTryAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        Intent intent=new Intent(SplashActivity.this,SplashActivity.class);
                        startActivity(intent);
                    }
                });
                return;
            }

            switch (result) {

                case "HTTP_UNAUTHORIZED":
                    loading.setText("The credentials has been changed, \nplease LOGIN again.");
                    return;
                case "HTTP_NOT_FOUND":
                    loading.setText("Oops! \n404-Page not found.");
                    return;
                case "HTTP_INTERNAL_ERROR":
                    loading.setText("Oops! \n500-Please try again later.");
                    return;
                case "HTTP_GATEWAY_TIMEOUT":
                    loading.setText("Oops! Seems like you have a slower net connection,\nplease try again later.");
                    return;
                case "HTTP_UNAVAILABLE":
                    loading.setText("Oops! Server busy, please try again later.  ");
                    return;
            }

            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                Prefs.putString("DEPENDENCY", jsonObject1.toString());
                // Preference.setDependencyObject(jsonObject1, "dependency");
                JSONArray jsonArrayDepartments = jsonObject1.getJSONArray("departments");
                for (int i = 0; i < jsonArrayDepartments.length(); i++) {
                    keyDepartment += jsonArrayDepartments.getJSONObject(i).getString("id") + ",";
                    valueDepartment += jsonArrayDepartments.getJSONObject(i).getString("name") + ",";
                }
                Prefs.putString("keyDept", keyDepartment);
                Prefs.putString("valueDept", valueDepartment);


                JSONArray jsonArraySla = jsonObject1.getJSONArray("sla");
                for (int i = 0; i < jsonArraySla.length(); i++) {
                    keySLA += jsonArraySla.getJSONObject(i).getString("id") + ",";
                    valueSLA += jsonArraySla.getJSONObject(i).getString("sla_duration") + ",";
                }
                Prefs.putString("keySLA", keySLA);
                Prefs.putString("valueSLA", valueSLA);

//                JSONArray jsonArrayType = jsonObject1.getJSONArray("type");
//                for (int i = 0; i < jsonArrayType.length(); i++) {
//                    keyType += jsonArrayType.getJSONObject(i).getString("id") + ",";
//                    valueType += jsonArrayType.getJSONObject(i).getString("name") + ",";
//                }
//                Prefs.putString("keyType", keyType);
//                Prefs.putString("valueType", valueType);

//                JSONArray jsonArrayStaffs = jsonObject1.getJSONArray("staffs");
//                for (int i = 0; i < jsonArrayStaffs.length(); i++) {
//                    keyName +=jsonArrayStaffs.getJSONObject(i).getString("first_name") + jsonArrayStaffs.getJSONObject(i).getString("last_name") +",";
//                    keyStaff += jsonArrayStaffs.getJSONObject(i).getString("id") + ",";
//                    valueStaff += jsonArrayStaffs.getJSONObject(i).getString("email") + ",";
//                }
//                Prefs.putString("keyName",keyName);
//                Prefs.putString("keyStaff", keyStaff);
//                Prefs.putString("valueStaff", valueStaff);

//                JSONArray jsonArrayTeams = jsonObject1.getJSONArray("teams");
//                for (int i = 0; i < jsonArrayTeams.length(); i++) {
//                    keyTeam += jsonArrayTeams.getJSONObject(i).getString("id") + ",";
//                    valueTeam += jsonArrayTeams.getJSONObject(i).getString("name") + ",";
//                }

                //Set<String> keyPri = new LinkedHashSet<>();
                // Set<String> valuePri = new LinkedHashSet<>();
                JSONArray jsonArrayPriorities = jsonObject1.getJSONArray("priorities");
                for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                    // keyPri.add(jsonArrayPriorities.getJSONObject(i).getString("priority_id"));
                    //valuePri.add(jsonArrayPriorities.getJSONObject(i).getString("priority"));
                    keyPriority += jsonArrayPriorities.getJSONObject(i).getString("priority_id") + ",";
                    valuePriority += jsonArrayPriorities.getJSONObject(i).getString("priority") + ",";
                }
                Prefs.putString("keyPri", keyPriority);
                Prefs.putString("valuePri", valuePriority);
                //Prefs.putOrderedStringSet("keyPri", keyPri);
                // Prefs.putOrderedStringSet("valuePri", valuePri);
                //Log.d("Testtttttt", Prefs.getOrderedStringSet("keyPri", keyPri) + "   " + Prefs.getOrderedStringSet("valuePri", valuePri));


                JSONArray jsonArrayHelpTopics = jsonObject1.getJSONArray("helptopics");
                for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {

                    keyTopic += jsonArrayHelpTopics.getJSONObject(i).getString("id") + ",";
                    valueTopic += jsonArrayHelpTopics.getJSONObject(i).getString("topic") + ",";
                }

                Prefs.putString("keyHelpTopic", keyTopic);
                Prefs.putString("valueHelptopic", valueTopic);

                JSONArray jsonArrayStatus = jsonObject1.getJSONArray("status");
                for (int i = 0; i < jsonArrayStatus.length(); i++) {
                    keyStatus += jsonArrayStatus.getJSONObject(i).getString("id") + ",";
                    valueStatus += jsonArrayStatus.getJSONObject(i).getString("name") + ",";
                }
                Prefs.putString("keyStatus", keyStatus);
                Prefs.putString("valueStatus", valueStatus);

                JSONArray jsonArraySources = jsonObject1.getJSONArray("sources");
                for (int i = 0; i < jsonArraySources.length(); i++) {
                    keySource += jsonArraySources.getJSONObject(i).getString("id") + ",";
                    valueSource += jsonArraySources.getJSONObject(i).getString("name") + ",";
                }

                Prefs.putString("keySource", keySource);
                Prefs.putString("valueSource", valueSource);

                int open = 0, closed = 0, trash = 0, unasigned = 0, my_tickets = 0;
                JSONArray jsonArrayTicketsCount = jsonObject1.getJSONArray("tickets_count");
                for (int i = 0; i < jsonArrayTicketsCount.length(); i++) {
                    String name = jsonArrayTicketsCount.getJSONObject(i).getString("name");
                    String count = jsonArrayTicketsCount.getJSONObject(i).getString("count");

                    switch (name) {
                        case "Open":
                            open = Integer.parseInt(count);
                            break;
                        case "Closed":
                            closed = Integer.parseInt(count);
                            break;
                        case "Deleted":
                            trash = Integer.parseInt(count);
                            break;
                        case "unassigned":
                            unasigned = Integer.parseInt(count);
                            break;
                        case "mytickets":
                            my_tickets = Integer.parseInt(count);
                            break;
                        default:
                            break;

                    }
                }


                if (open > 999)
                    Prefs.putString("inboxTickets", "999+");
                else
                    Prefs.putString("inboxTickets", open + "");

                if (closed > 999)
                    Prefs.putString("closedTickets", "999+");
                else
                    Prefs.putString("closedTickets", closed + "");

                if (my_tickets > 999)
                    Prefs.putString("myTickets", "999+");
                else
                    Prefs.putString("myTickets", my_tickets + "");

                if (trash > 999)
                    Prefs.putString("trashTickets", "999+");
                else
                    Prefs.putString("trashTickets", trash + "");

                if (unasigned > 999)
                    Prefs.putString("unassignedTickets", "999+");
                else
                    Prefs.putString("unassignedTickets", unasigned + "");

            } catch (JSONException e) {
                Toasty.error(SplashActivity.this, "Parsing Error!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                progressDialog.setVisibility(View.INVISIBLE);

            }

            loading.setText(R.string.done_loading);

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkConnection();
    }

    private void checkConnection() {
        boolean isConnected = InternetReceiver.isConnected();
        showSnackIfNoInternet(isConnected);
    }

    /**
     * Display the snackbar if network connection is not there.
     *
     * @param isConnected is a boolean value of network connection.
     */
    private void showSnackIfNoInternet(boolean isConnected) {
        if (!isConnected) {
            final Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), R.string.sry_not_connected_to_internet, Snackbar.LENGTH_INDEFINITE);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.setAction("X", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }

    }

    /**
     * Display the snackbar if network connection is there.
     *
     * @param isConnected is a boolean value of network connection.
     */

    private void showSnack(boolean isConnected) {

        if (isConnected) {

            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), R.string.connected_to_internet, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } else {
            showSnackIfNoInternet(false);
        }

    }

    /**
     * This method will be called when a MessageEvent is posted (in the UI thread for Toast).
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        showSnack(event.message);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
