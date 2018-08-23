package co.helpdesk.faveo.frontend.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import co.helpdesk.faveo.Helper;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.fragments.ticketDetail.Detail;
import co.helpdesk.faveo.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.model.Data;
import es.dmoral.toasty.Toasty;

public class TicketSaveActivity extends AppCompatActivity {
    AsyncTask<String, Void, String> task;
    TextView textViewTicketNumber, textViewErrorSubject;
    EditText editTextSubject, editTextFirstName, editTextEmail,
            editTextDueDate, editTextCreatedDate;

    Spinner spinnerSLAPlans, spinnerSource,
            spinnerPriority, spinnerHelpTopics, spinnerAssignTo,spinnerStatus;
    ProgressDialog progressDialog;
    ArrayList<Data> helptopicItems, priorityItems, typeItems, sourceItems,slaItems,statusItems;
    ArrayAdapter<Data> spinnerPriArrayAdapter, spinnerHelpArrayAdapter, spinnerTypeArrayAdapter,
            spinnerSourceArrayAdapter,spinnerSlaArrayAdapter,spinnerStatusAdapter;
    Button buttonSave,refresh;
    Animation rotation;
    String status;
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
        setContentView(R.layout.activity_ticket_save);
        overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_in_from_right);
        Window window = TicketSaveActivity.this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(TicketSaveActivity.this,R.color.faveo));
        rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        refresh= (Button) findViewById(R.id.refresh);
        setUpViews();
        if (InternetReceiver.isConnected()) {
            refresh.startAnimation(rotation);
            task = new FetchTicketDetail(Prefs.getString("TICKETid",null));
            task.execute();
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(TicketSaveActivity.this);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TicketSaveActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle(getString(R.string.refreshingPage));

                // Setting Dialog Message
                alertDialog.setMessage(getString(R.string.refreshPage));

                // Setting Icon to Dialog
                alertDialog.setIcon(R.mipmap.ic_launcher);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke YES event
                        //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        if (InternetReceiver.isConnected()){
                            refresh.startAnimation(rotation);
                            new FetchDependency().execute();
                            setUpViews();
                            task = new FetchTicketDetail(Prefs.getString("TICKETid",null));
                            task.execute();
                        }
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarsave);
        TextView textView = (TextView) toolbar.findViewById(R.id.titlesave);
        ImageView imageView= (ImageView) toolbar.findViewById(R.id.imageView);
        textView.setText(getString(R.string.ticketProperties));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        editTextSubject.addTextChangedListener(passwordWatcher);




        spinnerPriority.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buttonSave.setVisibility(View.VISIBLE);
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextSubject.getWindowToken(), 0);
                return false;
            }
        });
        spinnerSource.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buttonSave.setVisibility(View.VISIBLE);
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextSubject.getWindowToken(), 0);
                return false;

            }
        });
        spinnerHelpTopics.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buttonSave.setVisibility(View.VISIBLE);
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextSubject.getWindowToken(), 0);
                return false;
            }
        });
        spinnerSLAPlans.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buttonSave.setVisibility(View.VISIBLE);
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextSubject.getWindowToken(), 0);
                return false;
            }
        });
        spinnerStatus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buttonSave.setVisibility(View.VISIBLE);
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextSubject.getWindowToken(), 0);
                return false;
            }
        });
        editTextSubject.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                buttonSave.setVisibility(View.VISIBLE);
                return false;
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //resetViews();

                //int helpTopic=1;
                boolean allCorrect = true;
                final String subject = editTextSubject.getText().toString();
                // int SLAPlans = spinnerSLAPlans.getSelectedItemPosition();
                final Data helpTopic = (Data) spinnerHelpTopics.getSelectedItem();
                final Data source = (Data) spinnerSource.getSelectedItem();
                final Data priority = (Data) spinnerPriority.getSelectedItem();
//                Data type = (Data) spinnerType.getSelectedItem();
                final Data sla= (Data) spinnerSLAPlans.getSelectedItem();
                final Data statusId= (Data) spinnerStatus.getSelectedItem();
                status= String.valueOf(statusId.ID);
                Log.d("helptopicId", String.valueOf(helpTopic.ID));
//                spinnerHelpTopics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    }
//                });
                //int status = Integer.parseInt(Utils.removeDuplicates(SplashActivity.keyStatus.split(","))[spinnerStatus.getSelectedItemPosition()]);

//                if (SLAPlans == 0) {
//                    allCorrect = false;
//                    Toasty.warning(getContext(), "Please select some SLA plan", Toast.LENGTH_SHORT).show();
//                } else

                if (subject.trim().length() == 0) {
                    Toasty.warning(TicketSaveActivity.this, getString(R.string.sub_must_not_be_empty), Toast.LENGTH_SHORT).show();
                    allCorrect = false;
                } else if (subject.trim().length() < 5) {
                    Toasty.warning(TicketSaveActivity.this, getString(R.string.sub_minimum_char), Toast.LENGTH_SHORT).show();
                    allCorrect = false;
                } else if (helpTopic.ID == 0) {
                    allCorrect = false;
                    Toasty.warning(TicketSaveActivity.this, getString(R.string.select_some_helptopic), Toast.LENGTH_SHORT).show();
                }
                else if (sla.ID==0){
                    allCorrect=false;
                    Toasty.warning(TicketSaveActivity.this,getString(R.string.select_some_sla),Toast.LENGTH_SHORT).show();
                }
                else if (priority.ID == 0) {
                    allCorrect = false;
                    Toasty.warning(TicketSaveActivity.this, getString(R.string.please_select_some_priority), Toast.LENGTH_SHORT).show();
                } else if (source.ID == 0) {
                    allCorrect = false;
                    Toasty.warning(TicketSaveActivity.this, getString(R.string.select_source), Toast.LENGTH_SHORT).show();
                }

                if (allCorrect) {
                    hideKeyboard(TicketSaveActivity.this);
                    if (InternetReceiver.isConnected()) {
                        progressDialog=new ProgressDialog(TicketSaveActivity.this);
                        progressDialog.setMessage(getString(R.string.updating_ticket));

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(TicketSaveActivity.this);

                        // Setting Dialog Title
                        alertDialog.setTitle(R.string.editing);

                        // Setting Dialog Message
                        alertDialog.setMessage(R.string.sureUpdatre);

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.mipmap.ic_launcher);

                        // Setting Positive "Yes" Button
                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke YES event
                                //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                if (InternetReceiver.isConnected()){
                                    progressDialog.show();
                                    try {
                                        new SaveTicket(TicketSaveActivity.this,
                                                Integer.parseInt(Prefs.getString("TICKETid",null)),
                                                URLEncoder.encode(subject.trim(), "utf-8"),
                                                helpTopic.ID,sla.ID,
                                                source.ID,
                                                priority.ID,Integer.parseInt(status))
                                                .execute();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }
                        });

                        // Setting Negative "NO" Button
                        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to invoke NO event
                                //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });

                        // Showing Alert Message
                        alertDialog.show();

                    }
                }
            }
        });

    }
    final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editTextSubject.setCursorVisible(true);
        }

        public void afterTextChanged(Editable s) {
        }
    };

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
     class FetchTicketDetail extends AsyncTask<String, Void, String> {
        String ticketID;

        FetchTicketDetail(String ticketID) {

            this.ticketID = ticketID;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketDetail(ticketID);
        }

        protected void onPostExecute(String result) {
           refresh.clearAnimation();
            if (isCancelled()) return;
//            if (progressDialog.isShowing())
//                progressDialog.dismiss();

            if (result == null) {
                Toasty.error(TicketSaveActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                editTextSubject.setText(jsonObject1.getString("title"));
                status=jsonObject1.getString("status");
                Log.d("statusFromTicket",status);
                // textViewTicketNumber.setText(ticketNumber);
//                try {
//                    if (jsonObject1.getString("sla_name") != null) {
//                        //spinnerSLAPlans.setSelection(Integer.parseInt(jsonObject1.getString("sla")) - 1);
//                        spinnerSLAPlans.setSelection(spinnerSlaArrayAdapter.getPosition(jsonObject1.getString("sla_name")));
//                    }
//                } catch (JSONException | NumberFormatException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    if (jsonObject1.getString("status") != null) {
//                        // spinnerStatus.setSelection(Integer.parseInt(jsonObject1.getString("status")) - 1);
//
//                    }
//                } catch (JSONException | NumberFormatException e) {
//                    e.printStackTrace();
//                }
                try {
                    if (jsonObject1.getString("priority_name") != null) {
                        // spinnerPriority.setSelection(Integer.parseInt(jsonObject1.getString("priority_id")) - 1);
                        spinnerPriority.setSelection(Integer.parseInt(jsonObject1.getString("priority_id")));
                        //spinnerPriority.setSelection(getIndex(spinnerPriority, jsonObject1.getString("priority_name")));
                    }
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }



                try{
                   if (jsonObject1.getString("status_name")!=null) {
                     spinnerStatus.setSelection(Integer.parseInt(jsonObject1.getString("status")));
                    }
                }catch (JSONException | NumberFormatException e){
                    e.printStackTrace();
                }


//                try {
//                    if (jsonObject1.getString("type_name") != null) {
//                        // spinnerDepartment.setSelection(Integer.parseInt(jsonObject1.getString("dept_id")) - 1);
//                        spinnerType.setSelection(getIndex(spinnerType, jsonObject1.getString("type_name")));
//                    }
//                } catch (JSONException | NumberFormatException e) {
//                    e.printStackTrace();
//                }

                try {
                    if (jsonObject1.getString("helptopic_name") != null) {
                        Log.d("helptopic_id",jsonObject1.getString("helptopic_id"));
                        //spinnerHelpTopics.setSelection(Integer.parseInt(jsonObject1.getString("priority_id")));
                        spinnerHelpTopics.setSelection(getIndex(spinnerHelpTopics, jsonObject1.getString("helptopic_name")));
                    }
                } catch (Exception e) {
//                    spinnerHelpTopics.setVisibility(View.GONE);
//                    tv_helpTopic.setVisibility(View.GONE);
                    e.printStackTrace();
                }
                try {
                    if (jsonObject1.getString("sla_name") != null) {
                        for (int j=0;j<spinnerSLAPlans.getCount();j++){
                            Log.d("inforloop","true");
                            if (spinnerSLAPlans.getItemIdAtPosition(j)==Integer.parseInt(jsonObject1.getString("sla"))) {
                                spinnerSLAPlans.setSelection(j);
                                Log.d("aftermatch","true");
                            }

                        }
                        //spinnerHelpTopics.setSelection(Integer.parseInt(jsonObject1.getString("priority_id")));
                        //spinnerSLAPlans.setSelection(getIndex(spinnerSLAPlans, jsonObject1.getString("sla_name")));
                    }
                } catch (Exception e) {
//                    spinnerHelpTopics.setVisibility(View.GONE);
//                    tv_helpTopic.setVisibility(View.GONE);
                    e.printStackTrace();
                }

                try {
                    if (jsonObject1.getString("source_name") != null)
                        //spinnerSource.setSelection(Integer.parseInt(jsonObject1.getString("source")) - 1);

                        spinnerSource.setSelection(getIndex(spinnerSource, jsonObject1.getString("source_name")));
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }


            } catch (JSONException | IllegalStateException e) {
                e.printStackTrace();
            }
        }

    }

    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            Log.d("item ", spinner.getItemAtPosition(i).toString());
            if (spinner.getItemAtPosition(i).toString().equals(myString.trim())) {
                index = i;
                break;
            }
        }
        return index;
    }


    private class SaveTicket extends AsyncTask<String, Void, String> {
        Context context;
        int ticketNumber;
        String subject;
        int sla;
        int helpTopic;
        int ticketSource;
        int ticketPriority;
        int ticketStatus;


        SaveTicket(Context context, int ticketNumber, String subject, int helpTopic,int sla, int ticketSource, int ticketPriority,int ticketStatus) {
            this.context = context;
            this.ticketNumber = ticketNumber;
            this.subject = subject;
            this.sla = sla;
            this.helpTopic = helpTopic;
            this.ticketSource = ticketSource;
            this.ticketPriority = ticketPriority;
            this.ticketStatus = ticketStatus;

        }

        protected String doInBackground(String... urls) {
            if (subject.equals("Not available"))
                subject = "";
            return new Helpdesk().postEditTicket(ticketNumber, subject,sla,
                    helpTopic, ticketSource, ticketPriority, Integer.parseInt(status));
        }

        protected void onPostExecute(String result) {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (result == null) {
                Toasty.error(TicketSaveActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }

            if (result.contains("Edited successfully")) {
                Toasty.success(TicketSaveActivity.this, getString(R.string.update_success), Toast.LENGTH_LONG).show();
                finish();
                Intent intent=new Intent(TicketSaveActivity.this, MainActivity.class);
                startActivity(intent);
            } else
                Toasty.error(TicketSaveActivity.this, getString(R.string.failed_to_update_ticket), Toast.LENGTH_LONG).show();
        }
    }
    private void setUpViews() {

        JSONObject jsonObject;
        String json = Prefs.getString("DEPENDENCY", "");
        try {
            helptopicItems = new ArrayList<>();
            helptopicItems.add(new Data(0, "Please select helptopic"));
            jsonObject = new JSONObject(json);
            JSONArray jsonArrayHelpTopics = jsonObject.getJSONArray("helptopics");
            for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {
                Data data = new Data(Integer.parseInt(jsonArrayHelpTopics.getJSONObject(i).getString("id")), jsonArrayHelpTopics.getJSONObject(i).getString("topic"));
                helptopicItems.add(data);
            }

            JSONArray jsonArrayPriorities = jsonObject.getJSONArray("priorities");
            priorityItems = new ArrayList<>();
            priorityItems.add(new Data(0, "--"));
            for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                Data data = new Data(Integer.parseInt(jsonArrayPriorities.getJSONObject(i).getString("priority_id")), jsonArrayPriorities.getJSONObject(i).getString("priority"));
                priorityItems.add(data);
            }

            JSONArray jsonArrayStatus=jsonObject.getJSONArray("status");
            statusItems=new ArrayList<>();
            statusItems.add(new Data(0,"--"));
            for (int i=0;i<jsonArrayStatus.length();i++){
                Data data=new Data(Integer.parseInt(jsonArrayStatus.getJSONObject(i).getString("id")),jsonArrayStatus.getJSONObject(i).getString("name"));
                statusItems.add(data);
            }

//            JSONArray jsonArrayType = jsonObject.getJSONArray("type");
//            typeItems = new ArrayList<>();
//            typeItems.add(new Data(0, "--"));
//            for (int i = 1; i < jsonArrayType.length(); i++) {
//                Data data = new Data(Integer.parseInt(jsonArrayType.getJSONObject(i).getString("id")), jsonArrayType.getJSONObject(i).getString("name"));
//                typeItems.add(data);
//
//            }
            JSONArray jsonArraySLA=jsonObject.getJSONArray("sla");
            slaItems=new ArrayList<>();
            slaItems.add(new Data(0,"--"));
            for (int i = 0; i < jsonArraySLA.length(); i++) {
                Data data = new Data(Integer.parseInt(jsonArraySLA.getJSONObject(i).getString("id")), jsonArraySLA.getJSONObject(i).getString("sla_duration"));
                slaItems.add(data);
            }

            JSONArray jsonArraySources = jsonObject.getJSONArray("sources");
            sourceItems = new ArrayList<>();
            sourceItems.add(new Data(0, "--"));
            for (int i = 0; i < jsonArraySources.length(); i++) {
                Data data = new Data(Integer.parseInt(jsonArraySources.getJSONObject(i).getString("id")), jsonArraySources.getJSONObject(i).getString("name"));
                sourceItems.add(data);
            }

        } catch (JSONException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }


        // textViewTicketNumber = (TextView) rootView.findViewById(R.id.textView_ticket_number);
        //textViewOpenedBy.setText(TicketDetailActivity.ticketOpenedBy);

        editTextSubject = (EditText) findViewById(R.id.editText_subject);
        //editTextSubject.setText(TicketDetailActivity.ticketSubject);
        textViewErrorSubject = (TextView) findViewById(co.helpdesk.faveo.R.id.textView_error_subject);

        spinnerSLAPlans = (Spinner) findViewById(R.id.spinner_sla_plans);
        spinnerSlaArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_spinner_item,slaItems); //selected item will look like a spinner set from XML
        spinnerSlaArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSLAPlans.setAdapter(spinnerSlaArrayAdapter);

        spinnerStatus= (Spinner) findViewById(R.id.spinner_status);
        spinnerStatusAdapter=new ArrayAdapter<>(TicketSaveActivity.this,android.R.layout.simple_spinner_item,statusItems);
        spinnerStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerStatusAdapter);


//        spinnerStatus = (Spinner) rootView.findViewById(R.id.spinner_status);
//        spinnerStatusArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueStatus.split(","))); //selected item will look like a spinner set from XML
//        spinnerStatusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerStatus.setAdapter(spinnerStatusArrayAdapter);

        spinnerPriority = (Spinner) findViewById(R.id.spinner_priority);
        spinnerPriArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_spinner_item, priorityItems); //selected item will look like a spinner set from XML
        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(spinnerPriArrayAdapter);

//        spinnerType = (Spinner) rootView.findViewById(R.id.spinner_type);
//        spinnerTypeArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, typeItems); //selected item will look like a spinner set from XML
//        spinnerTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerType.setAdapter(spinnerTypeArrayAdapter);

        spinnerHelpTopics = (Spinner) findViewById(R.id.spinner_help_topics);
        spinnerHelpArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_spinner_item, helptopicItems); //selected item will look like a spinner set from XML
        spinnerHelpArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHelpTopics.setAdapter(spinnerHelpArrayAdapter);


        spinnerSource = (Spinner) findViewById(R.id.spinner_source);
        spinnerSourceArrayAdapter = new ArrayAdapter<>(TicketSaveActivity.this, android.R.layout.simple_spinner_item, sourceItems); //selected item will look like a spinner set from XML
        spinnerSourceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(spinnerSourceArrayAdapter);
        editTextFirstName = (EditText) findViewById(R.id.editText_ticketDetail_firstname);
        //editTextLastName = (EditText) rootView.findViewById(R.id.editText_ticketDetail_lastname);
        editTextEmail = (EditText) findViewById(R.id.editText_email);


//
//        spinnerSource = (Spinner) rootView.findViewById(R.id.spinner_source);
//        spinnerSourceArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sourceItems); //selected item will look like a spinner set from XML
//        spinnerSourceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerSource.setAdapter(spinnerSourceArrayAdapter);

        //editTextLastMessage = (EditText) rootView.findViewById(R.id.editText_last_message);
        editTextDueDate = (EditText)findViewById(R.id.editText_due_date);
        editTextCreatedDate = (EditText) findViewById(R.id.editText_created_date);
        //editTextLastResponseDate = (EditText) findViewById(R.id.editText_last_response_date);
        spinnerAssignTo = (Spinner) findViewById(R.id.spinner_assign_to);
        buttonSave = (Button) findViewById(R.id.button_save);
        //tv_dept = (TextView) rootView.findViewById(R.id.tv_dept);
        //tv_helpTopic = (TextView) findViewById(R.id.tv_helpTopic);

//        paddingTop = editTextEmail.getPaddingTop();
//        paddingBottom = editTextEmail.getPaddingBottom();
        editTextSubject.setFilters(new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if (src.equals("")) { // for backspace
                            return src;
                        }
                        if (src.toString().matches("[\\x00-\\x7F]+")) {
                            return src;
                        }
                        return "";
                    }
                }
        });
    }
//    private void resetViews() {
//        editTextSubject.setBackgroundResource(R.drawable.edittext_theme_states);
//        editTextSubject.setPadding(0, paddingTop, 0, paddingBottom);
//        textViewErrorSubject.setText("");
//    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private class FetchDependency extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            return new Helpdesk().getDependency();

        }

        protected void onPostExecute(String result) {
            Log.d("Depen Response : ", result + "");
            refresh.clearAnimation();
            if (result == null) {
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
                e.printStackTrace();
            } finally {


            }
            Intent intent=new Intent(TicketSaveActivity.this, TicketSaveActivity.class);
            startActivity(intent);

        }
    }
}
