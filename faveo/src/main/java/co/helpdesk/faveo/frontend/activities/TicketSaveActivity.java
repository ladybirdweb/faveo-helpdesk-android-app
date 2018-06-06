package co.helpdesk.faveo.frontend.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
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
    TextView tv_helpTopic, tv_dept;
    AsyncTask<String, Void, String> task;
    TextView textViewTicketNumber, textViewErrorSubject;
    int paddingTop, paddingBottom;
    EditText editTextSubject, editTextFirstName, editTextEmail,
            editTextLastMessage, editTextDueDate, editTextCreatedDate, editTextLastResponseDate;

    //ArrayAdapter<String> spinnerSlaArrayAdapter, spinnerAssignToArrayAdapter, spinnerStatusArrayAdapter;

    Spinner spinnerSLAPlans, spinnerType, spinnerStatus, spinnerSource,
            spinnerPriority, spinnerHelpTopics, spinnerAssignTo;
    ProgressDialog progressDialog;
    ArrayList<Data> helptopicItems, priorityItems, typeItems, sourceItems,slaItems;
    ArrayAdapter<Data> spinnerPriArrayAdapter, spinnerHelpArrayAdapter, spinnerTypeArrayAdapter, spinnerSourceArrayAdapter,spinnerSlaArrayAdapter;
    Button buttonSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_save);
        setUpViews();
        if (InternetReceiver.isConnected()) {
            progressDialog=new ProgressDialog(TicketSaveActivity.this);
            progressDialog.setMessage(getString(R.string.pleaseWait));
            progressDialog.show();
            task = new FetchTicketDetail(Prefs.getString("TICKETid",null));
            task.execute();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarsave);
        TextView textView = (TextView) toolbar.findViewById(R.id.titlesave);
        ImageView imageView= (ImageView) toolbar.findViewById(R.id.imageView);
        textView.setText(getString(R.string.ticketProperties));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TicketSaveActivity.this, TicketDetailActivity.class);
                startActivity(intent);
            }
        });
        setSupportActionBar(toolbar);
        editTextSubject.addTextChangedListener(passwordWatcher);
        spinnerPriority.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buttonSave.setVisibility(View.VISIBLE);
                return false;
            }
        });
        spinnerSource.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buttonSave.setVisibility(View.VISIBLE);
                return false;
            }
        });
        spinnerHelpTopics.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buttonSave.setVisibility(View.VISIBLE);
                return false;
            }
        });
        spinnerSLAPlans.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                buttonSave.setVisibility(View.VISIBLE);
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
                resetViews();

                //int helpTopic=1;
                boolean allCorrect = true;
                String subject = editTextSubject.getText().toString();
                // int SLAPlans = spinnerSLAPlans.getSelectedItemPosition();
                Data helpTopic = (Data) spinnerHelpTopics.getSelectedItem();
                Data source = (Data) spinnerSource.getSelectedItem();
                Data priority = (Data) spinnerPriority.getSelectedItem();
//                Data type = (Data) spinnerType.getSelectedItem();
                Data sla= (Data) spinnerSLAPlans.getSelectedItem();

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
                    if (InternetReceiver.isConnected()) {
                        progressDialog=new ProgressDialog(TicketSaveActivity.this);
                        progressDialog.setMessage(getString(R.string.updating_ticket));
                        progressDialog.show();
                        try {
                            new SaveTicket(TicketSaveActivity.this,
                                    Integer.parseInt(Prefs.getString("TICKETid",null)),
                                    URLEncoder.encode(subject.trim(), "utf-8"),
                                    helpTopic.ID,sla.ID,
                                    source.ID,
                                    priority.ID)
                                    .execute();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }
    final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //buttonsave.setEnabled(false);
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            editTextSubject.setCursorVisible(true);
            //buttonsave.setEnabled(true);
        }

        public void afterTextChanged(Editable s) {
//                if (s.length() == 0) {
//                    edittextsubject.setVisibility(View.GONE);
//                } else{
//                    textView.setText("You have entered : " + passwordEditText.getText());
//                }
        }
    };
     class FetchTicketDetail extends AsyncTask<String, Void, String> {
        String ticketID;

        FetchTicketDetail(String ticketID) {

            this.ticketID = ticketID;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketDetail(ticketID);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
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
                //editTextSubject.setText(jsonObject1.getString("title"));
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
                        //spinnerHelpTopics.setSelection(Integer.parseInt(jsonObject1.getString("priority_id")));
                        spinnerSLAPlans.setSelection(getIndex(spinnerSLAPlans, jsonObject1.getString("sla_name")));
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


        SaveTicket(Context context, int ticketNumber, String subject, int helpTopic,int sla, int ticketSource, int ticketPriority) {
            this.context = context;
            this.ticketNumber = ticketNumber;
            this.subject = subject;
            this.sla = sla;
            this.helpTopic = helpTopic;
            this.ticketSource = ticketSource;
            this.ticketPriority = ticketPriority;
            // this.ticketStatus = ticketStatus;

        }

        protected String doInBackground(String... urls) {
            if (subject.equals("Not available"))
                subject = "";
            return new Helpdesk().postEditTicket(ticketNumber, subject,
                    helpTopic,sla, ticketSource, ticketPriority);
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
                Data data = new Data(Integer.parseInt(jsonArraySLA.getJSONObject(i).getString("id")), jsonArraySLA.getJSONObject(i).getString("name"));
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
        editTextLastResponseDate = (EditText) findViewById(R.id.editText_last_response_date);
        spinnerAssignTo = (Spinner) findViewById(R.id.spinner_assign_to);
        buttonSave = (Button) findViewById(R.id.button_save);
        //tv_dept = (TextView) rootView.findViewById(R.id.tv_dept);
        tv_helpTopic = (TextView) findViewById(R.id.tv_helpTopic);

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
    private void resetViews() {
        editTextSubject.setBackgroundResource(R.drawable.edittext_theme_states);
        editTextSubject.setPadding(0, paddingTop, 0, paddingBottom);
        textViewErrorSubject.setText("");
    }
}
