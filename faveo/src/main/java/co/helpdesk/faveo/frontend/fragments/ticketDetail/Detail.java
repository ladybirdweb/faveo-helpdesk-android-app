package co.helpdesk.faveo.frontend.fragments.ticketDetail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import co.helpdesk.faveo.frontend.activities.MainActivity;
import co.helpdesk.faveo.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.frontend.activities.TicketSaveActivity;
import co.helpdesk.faveo.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.model.Data;
import es.dmoral.toasty.Toasty;

public class Detail extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    AsyncTask<String, Void, String> task;
    TextView textViewTicketNumber, textViewErrorSubject;
    int paddingTop, paddingBottom;
    EditText editTextSubject, editTextFirstName, editTextEmail,
            editTextLastMessage, editTextDueDate, editTextCreatedDate;

    //ArrayAdapter<String> spinnerSlaArrayAdapter, spinnerAssignToArrayAdapter, spinnerStatusArrayAdapter;

    Spinner spinnerSLAPlans, spinnerType, spinnerStatus, spinnerSource,
            spinnerPriority, spinnerHelpTopics, spinnerAssignTo;
    ProgressDialog progressDialog;
    ArrayList<Data> helptopicItems, priorityItems, typeItems, sourceItems,slaItems,statusItems;
    ArrayAdapter<Data> spinnerPriArrayAdapter, spinnerHelpArrayAdapter, spinnerStatusAdapter, spinnerSourceArrayAdapter,spinnerSlaArrayAdapter;
    Button buttonSave;
    Animation animation;
    String ticketID;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static Detail newInstance(String param1, String param2) {
        Detail fragment = new Detail();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Detail() {
    }

    @Override
    public void onPause() {
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);
            Log.d("Async Detail", "Cancelled");
        }
        super.onPause();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        setUpViews(rootView);
        animation= AnimationUtils.loadAnimation(getActivity(),R.anim.shake_error);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.fetching_detail));
        // progressDialog.show();
        if (InternetReceiver.isConnected()) {
            task = new FetchTicketDetail(Prefs.getString("TICKETid",null));
            task.execute();
        }
        return rootView;
    }

    private void setErrorState(EditText editText, TextView textViewError, String error) {
        editText.setBackgroundResource(R.drawable.edittext_error_state);
        editText.setPadding(0, paddingTop, 0, paddingBottom);
        textViewError.setText(error);
    }

    private class FetchTicketDetail extends AsyncTask<String, Void, String> {
        String ticketID;

        FetchTicketDetail(String ticketID) {

            this.ticketID = ticketID;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketDetail(ticketID);
        }

        protected void onPostExecute(String result) {
            if (isCancelled()) return;
//            if (progressDialog.isShowing())
//                progressDialog.dismiss();

            if (result == null) {
                Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                String title=jsonObject1.getString("title");
                editTextSubject.setText(title);
                String ticketNumber = jsonObject1.getString("ticket_number");
                // textViewTicketNumber.setText(ticketNumber);
//                ActionBar actionBar = ((TicketDetailActivity) getActivity()).getSupportActionBar();
//                if (actionBar != null) {
//                    actionBar.setTitle(ticketNumber == null ? "TicketDetail" : ticketNumber);
//
//                }

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
                        spinnerPriority.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                return true;
                            }
                        });
                        //spinnerPriority.setSelection(getIndex(spinnerPriority, jsonObject1.getString("priority_name")));
                    }
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }
                try{
                    if (jsonObject1.getString("status_name")!=null) {
                        spinnerStatus.setSelection(Integer.parseInt(jsonObject1.getString("status")));
                        spinnerStatus.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                return true;
                            }
                        });
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
                        //spinnerHelpTopics.setSelection(Integer.parseInt(jsonObject1.getString("priority_id")));
                        spinnerHelpTopics.setSelection(getIndex(spinnerHelpTopics, jsonObject1.getString("helptopic_name")));
                        spinnerHelpTopics.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                return true;
                            }
                        });
                    }
                } catch (Exception e) {
//                    spinnerHelpTopics.setVisibility(View.GONE);
//                    tv_helpTopic.setVisibility(View.GONE);
                    e.printStackTrace();
                }
                try {
                    if (jsonObject1.getString("sla_name") != null) {
                        //spinnerHelpTopics.setSelection(Integer.parseInt(jsonObject1.getString("priority_id")));

                        for (int j=0;j<spinnerSLAPlans.getCount();j++){
                            Log.d("inforloop","true");
                            if (spinnerSLAPlans.getItemIdAtPosition(j)==Integer.parseInt(jsonObject1.getString("sla"))) {
                                spinnerSLAPlans.setSelection(j);
                                Log.d("aftermatch","true");
                            }

                        }
                        //spinnerSLAPlans.setSelection(getIndex(spinnerSLAPlans, jsonObject1.getString("sla_name")));
                        spinnerSLAPlans.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                return true;
                            }
                        });
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
                    spinnerSource.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            return true;
                        }
                    });
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }

                if (jsonObject1.getString("first_name").equals("") || jsonObject1.getString("first_name") == null) {
                    editTextFirstName.setText(getString(R.string.not_available));
                } else
                    editTextFirstName.setText(jsonObject1.getString("first_name"));

//                if (jsonObject1.getString("last_name").equals("") || jsonObject1.getString("last_name") == null) {
//                    editTextLastName.setText(getString(R.string.not_available));
//                } else
//                    editTextLastName.setText(jsonObject1.getString("last_name"));

                if (jsonObject1.getString("email").equals("") || jsonObject1.getString("email") == null) {
                    editTextEmail.setText(getString(R.string.not_available));
                } else
                    editTextEmail.setText(jsonObject1.getString("email"));

                if (jsonObject1.getString("duedate").equals("") || jsonObject1.getString("duedate") == null) {
                    editTextDueDate.setText(getString(R.string.not_available));
                } else {
                    editTextDueDate.setText(Helper.parseDate(jsonObject1.getString("duedate")));
                }

                if (jsonObject1.getString("created_at").equals("") || jsonObject1.getString("created_at") == null) {
                    editTextCreatedDate.setText(getString(R.string.not_available));
                } else {
                    editTextCreatedDate.setText(Helper.parseDate(jsonObject1.getString("created_at")));
                }

//                if (jsonObject1.getString("updated_at").equals("") || jsonObject1.getString("updated_at") == null) {
//                    //editTextLastResponseDate.setText(getString(R.string.not_available));
//                } else {
//                    //editTextLastResponseDate.setText(Helper.parseDate(jsonObject1.getString("updated_at")));
//                }


//                if (jsonObject1.getString("last_message").equals("null")) {
//                    editTextLastMessage.setText("Not available");
//                } else
//                    editTextLastMessage.setText(jsonObject1.getString("last_message"));


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

    private void setUpViews(View rootView) {

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
            priorityItems.add(new Data(0, "Please select priority"));
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
            slaItems.add(new Data(0,"Please select SLA"));
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
            JSONArray jsonArrayStatus=jsonObject.getJSONArray("status");
            statusItems=new ArrayList<>();
            statusItems.add(new Data(0,"--"));
            for (int i=0;i<jsonArrayStatus.length();i++){
                Data data=new Data(Integer.parseInt(jsonArrayStatus.getJSONObject(i).getString("id")),jsonArrayStatus.getJSONObject(i).getString("name"));
                statusItems.add(data);
            }

        } catch (JSONException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }




        // textViewTicketNumber = (TextView) rootView.findViewById(R.id.textView_ticket_number);
        //textViewOpenedBy.setText(TicketDetailActivity.ticketOpenedBy);

        editTextSubject = (EditText) rootView.findViewById(R.id.editText_subject);
        //editTextSubject.setText(TicketDetailActivity.ticketSubject);
        textViewErrorSubject = (TextView) rootView.findViewById(co.helpdesk.faveo.R.id.textView_error_subject);

        spinnerSLAPlans = (Spinner) rootView.findViewById(R.id.spinner_sla_plans);
        spinnerSlaArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,slaItems); //selected item will look like a spinner set from XML
        spinnerSlaArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSLAPlans.setAdapter(spinnerSlaArrayAdapter);

        spinnerStatus= (Spinner) rootView.findViewById(R.id.spinner_status);
        spinnerStatusAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item,statusItems);
        spinnerStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerStatusAdapter);

        spinnerPriority = (Spinner) rootView.findViewById(R.id.spinner_priority);
        spinnerPriArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, priorityItems); //selected item will look like a spinner set from XML
        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(spinnerPriArrayAdapter);

//        spinnerType = (Spinner) rootView.findViewById(R.id.spinner_type);
//        spinnerTypeArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, typeItems); //selected item will look like a spinner set from XML
//        spinnerTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerType.setAdapter(spinnerTypeArrayAdapter);

        spinnerHelpTopics = (Spinner) rootView.findViewById(R.id.spinner_help_topics);
        spinnerHelpArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, helptopicItems); //selected item will look like a spinner set from XML
        spinnerHelpArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHelpTopics.setAdapter(spinnerHelpArrayAdapter);


        spinnerSource = (Spinner) rootView.findViewById(R.id.spinner_source);
        spinnerSourceArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sourceItems); //selected item will look like a spinner set from XML
        spinnerSourceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(spinnerSourceArrayAdapter);
        editTextFirstName = (EditText) rootView.findViewById(R.id.editText_ticketDetail_firstname);
        //editTextLastName = (EditText) rootView.findViewById(R.id.editText_ticketDetail_lastname);
        editTextEmail = (EditText) rootView.findViewById(R.id.editText_email);
//
//        spinnerSource = (Spinner) rootView.findViewById(R.id.spinner_source);
//        spinnerSourceArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sourceItems); //selected item will look like a spinner set from XML
//        spinnerSourceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerSource.setAdapter(spinnerSourceArrayAdapter);

        //editTextLastMessage = (EditText) rootView.findViewById(R.id.editText_last_message);
        editTextDueDate = (EditText) rootView.findViewById(R.id.editText_due_date);
        editTextCreatedDate = (EditText) rootView.findViewById(R.id.editText_created_date);
        //editTextLastResponseDate = (EditText) rootView.findViewById(R.id.editText_last_response_date);
        spinnerAssignTo = (Spinner) rootView.findViewById(R.id.spinner_assign_to);
        buttonSave = (Button) rootView.findViewById(R.id.button_save);
        //tv_dept = (TextView) rootView.findViewById(R.id.tv_dept);

        paddingTop = editTextEmail.getPaddingTop();
        paddingBottom = editTextEmail.getPaddingBottom();
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
