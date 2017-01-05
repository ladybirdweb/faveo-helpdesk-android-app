package co.helpdesk.faveo.frontend.fragments.ticketDetail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import co.helpdesk.faveo.Helper;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.Utils;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.activities.SplashActivity;
import co.helpdesk.faveo.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.frontend.receivers.InternetReceiver;

public class Detail extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView tv_helpTopic, tv_dept;
    TextView textViewOpenedBy, textViewErrorSubject;
    int paddingTop, paddingBottom;
    EditText editTextSubject, editTextFirstName, editTextLastName, editTextEmail,
            editTextLastMessage, editTextDueDate, editTextCreatedDate, editTextLastResponseDate;

    Spinner spinnerSLAPlans, spinnerDepartment, spinnerStatus, spinnerSource,
            spinnerPriority, spinnerHelpTopics, spinnerAssignTo;

    ArrayAdapter<String> spinnerSlaArrayAdapter, spinnerAssignToArrayAdapter, spinnerStatusArrayAdapter,
            spinnerSourceArrayAdapter, spinnerHelpArrayAdapter, spinnerDeptArrayAdapter, spinnerPriArrayAdapter;

    ProgressDialog progressDialog;

    Button buttonSave;

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
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Fetching detail");
        progressDialog.show();
        new FetchTicketDetail(getActivity(), TicketDetailActivity.ticketID).execute();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetViews();
                boolean allCorrect = true;
                String subject = editTextSubject.getText().toString();
                if (subject.trim().length() == 0) {
                    setErrorState(editTextSubject, textViewErrorSubject, "Please fill the field");
                    allCorrect = false;
                } else if (subject.trim().length() < 5) {
                    setErrorState(editTextSubject, textViewErrorSubject, "Subject should be minimum 5 characters");
                    allCorrect = false;
                }
                if (allCorrect) {
                    if (InternetReceiver.isConnected()) {
                        progressDialog.setMessage("Updating ticket");
                        progressDialog.show();
                        try {
                            new SaveTicket(getActivity(),
                                    Integer.parseInt(TicketDetailActivity.ticketID),
                                    URLEncoder.encode(subject, "utf-8"),
                                    Integer.parseInt(Utils.removeDuplicates(SplashActivity.keySLA.split(","))[spinnerSLAPlans.getSelectedItemPosition()]),
                                    Integer.parseInt(Utils.removeDuplicates(SplashActivity.keyTopic.split(","))[spinnerHelpTopics.getSelectedItemPosition()]),
                                    Integer.parseInt(Utils.removeDuplicates(SplashActivity.keySource.split(","))[spinnerSource.getSelectedItemPosition()]),
                                    Integer.parseInt(Utils.removeDuplicates(SplashActivity.keyPriority.split(","))[spinnerPriority.getSelectedItemPosition()]),
                                    Integer.parseInt(Utils.removeDuplicates(SplashActivity.keyStatus.split(","))[spinnerStatus.getSelectedItemPosition()]))
                                    .execute();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return rootView;
    }

    private void setErrorState(EditText editText, TextView textViewError, String error) {
        editText.setBackgroundResource(R.drawable.edittext_error_state);
        editText.setPadding(0, paddingTop, 0, paddingBottom);
        textViewError.setText(error);
    }

    public class FetchTicketDetail extends AsyncTask<String, Void, String> {
        Context context;
        String ticketNumber;

        FetchTicketDetail(Context context, String ticketNumber) {
            this.context = context;
            this.ticketNumber = ticketNumber;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketDetail(ticketNumber);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result == null) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");

                try {
                    if (jsonObject1.getString("sla_name") != null) {
                        //spinnerSLAPlans.setSelection(Integer.parseInt(jsonObject1.getString("sla")) - 1);
                        // spinnerSLAPlans.setSelection(adapter.getPosition("YOUR_VALUE"));
                        spinnerSLAPlans.setSelection(spinnerSlaArrayAdapter.getPosition(jsonObject1.getString("sla_name")));
                    }
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }
                try {
                    if (jsonObject1.getString("status") != null) {
                        spinnerStatus.setSelection(Integer.parseInt(jsonObject1.getString("status")) - 1);

                    }
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }
                try {
                    if (jsonObject1.getString("priority_name") != null) {
                        // spinnerPriority.setSelection(Integer.parseInt(jsonObject1.getString("priority_id")) - 1);
                        spinnerPriority.setSelection(spinnerPriArrayAdapter.getPosition(jsonObject1.getString("priority_name")));
                    }
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }

                try {

                    // spinnerDepartment.setSelection(Integer.parseInt(jsonObject1.getString("dept_id")) - 1);
                    spinnerDepartment.setSelection(spinnerDeptArrayAdapter.getPosition(jsonObject1.getString("dept_name")));
                } catch (Exception e) {
                    tv_dept.setVisibility(View.GONE);
                    spinnerDepartment.setVisibility(View.GONE);
                    e.printStackTrace();
                }
                try {
                    spinnerHelpTopics.setSelection(Integer.parseInt(jsonObject1.getString("help_topic_id")) - 1);
                } catch (Exception e) {
                    spinnerHelpTopics.setVisibility(View.GONE);
                    tv_helpTopic.setVisibility(View.GONE);
                    e.printStackTrace();
                }
                try {
                    if (jsonObject1.getString("source_name") != null)
                        //spinnerSource.setSelection(Integer.parseInt(jsonObject1.getString("source")) - 1);
                        spinnerSource.setSelection(spinnerSourceArrayAdapter.getPosition(jsonObject1.getString("source_name")));
                } catch (JSONException | NumberFormatException e) {
                    e.printStackTrace();
                }

                if (jsonObject1.getString("first_name").equals("") || jsonObject1.getString("first_name") == null) {
                    editTextFirstName.setText("Not available");
                } else
                    editTextFirstName.setText(jsonObject1.getString("first_name"));

                if (jsonObject1.getString("last_name").equals("") || jsonObject1.getString("last_name") == null) {
                    editTextLastName.setText("Not available");
                } else
                    editTextLastName.setText(jsonObject1.getString("last_name"));

                if (jsonObject1.getString("email").equals("") || jsonObject1.getString("email") == null) {
                    editTextEmail.setText("Not available");
                } else
                    editTextEmail.setText(jsonObject1.getString("email"));

                if (jsonObject1.getString("duedate").equals("") || jsonObject1.getString("duedate") == null) {
                    editTextDueDate.setText("Not available");
                } else {
                    editTextDueDate.setText(Helper.parseDate(jsonObject1.getString("duedate")));
                }

                if (jsonObject1.getString("created_at").equals("") || jsonObject1.getString("created_at") == null) {
                    editTextCreatedDate.setText("Not available");
                } else {
                    editTextCreatedDate.setText(Helper.parseDate(jsonObject1.getString("created_at")));
                }

                if (jsonObject1.getString("updated_at").equals("") || jsonObject1.getString("updated_at") == null) {
                    editTextLastResponseDate.setText("Not available");
                } else {
                    editTextLastResponseDate.setText(Helper.parseDate(jsonObject1.getString("updated_at")));
                }


//                if (jsonObject1.getString("last_message").equals("null")) {
//                    editTextLastMessage.setText("Not available");
//                } else
//                    editTextLastMessage.setText(jsonObject1.getString("last_message"));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public class SaveTicket extends AsyncTask<String, Void, String> {
        Context context;
        int ticketNumber;
        String subject;
        int slaPlan;
        int helpTopic;
        int ticketSource;
        int ticketPriority;
        int ticketStatus;

        SaveTicket(Context context, int ticketNumber, String subject,
                   int slaPlan, int helpTopic, int ticketSource, int ticketPriority, int ticketStatus) {
            this.context = context;
            this.ticketNumber = ticketNumber;
            this.subject = subject;
            this.slaPlan = slaPlan;
            this.helpTopic = helpTopic;
            this.ticketSource = ticketSource;
            this.ticketPriority = ticketPriority;
            this.ticketStatus = ticketStatus;
        }

        protected String doInBackground(String... urls) {
            if (subject.equals("Not available"))
                subject = "";
            return new Helpdesk().postEditTicket(ticketNumber, subject, slaPlan,
                    helpTopic, ticketSource, ticketPriority, ticketStatus);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Log.d("edit ticket result", result + "");
            if (result == null) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            if (result.contains("ticket_id"))
                Toast.makeText(getActivity(), "Update successful", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getActivity(), "Failed to update ticket", Toast.LENGTH_LONG).show();
        }
    }

    private void setUpViews(View rootView) {
        textViewOpenedBy = (TextView) rootView.findViewById(R.id.textView_opened_by);
        textViewOpenedBy.setText(TicketDetailActivity.ticketOpenedBy);

        editTextSubject = (EditText) rootView.findViewById(R.id.editText_subject);
        editTextSubject.setText(TicketDetailActivity.ticketSubject);
        textViewErrorSubject = (TextView) rootView.findViewById(co.helpdesk.faveo.R.id.textView_error_subject);
        spinnerSLAPlans = (Spinner) rootView.findViewById(R.id.spinner_sla_plans);
        spinnerSlaArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueSLA.split(","))); //selected item will look like a spinner set from XML
        spinnerSlaArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSLAPlans.setAdapter(spinnerSlaArrayAdapter);

        spinnerStatus = (Spinner) rootView.findViewById(R.id.spinner_status);
        spinnerStatusArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueStatus.split(","))); //selected item will look like a spinner set from XML
        spinnerStatusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(spinnerStatusArrayAdapter);

        spinnerPriority = (Spinner) rootView.findViewById(R.id.spinner_priority);
        spinnerPriArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valuePriority.split(","))); //selected item will look like a spinner set from XML
        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(spinnerPriArrayAdapter);

        spinnerDepartment = (Spinner) rootView.findViewById(R.id.spinner_department);
        spinnerDeptArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueDepartment.split(","))); //selected item will look like a spinner set from XML
        spinnerDeptArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(spinnerDeptArrayAdapter);

        spinnerHelpTopics = (Spinner) rootView.findViewById(R.id.spinner_help_topics);
        spinnerHelpArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueTopic.split(","))); //selected item will look like a spinner set from XML
        spinnerHelpArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHelpTopics.setAdapter(spinnerHelpArrayAdapter);

        editTextFirstName = (EditText) rootView.findViewById(R.id.editText_ticketDetail_firstname);
        editTextLastName = (EditText) rootView.findViewById(R.id.editText_ticketDetail_lastname);
        editTextEmail = (EditText) rootView.findViewById(R.id.editText_email);

        spinnerSource = (Spinner) rootView.findViewById(R.id.spinner_source);
        spinnerSourceArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueSource.split(","))); //selected item will look like a spinner set from XML
        spinnerSourceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSource.setAdapter(spinnerSourceArrayAdapter);

        //editTextLastMessage = (EditText) rootView.findViewById(R.id.editText_last_message);
        editTextDueDate = (EditText) rootView.findViewById(R.id.editText_due_date);
        editTextCreatedDate = (EditText) rootView.findViewById(R.id.editText_created_date);
        editTextLastResponseDate = (EditText) rootView.findViewById(R.id.editText_last_response_date);
        spinnerAssignTo = (Spinner) rootView.findViewById(R.id.spinner_assign_to);
        buttonSave = (Button) rootView.findViewById(R.id.button_save);
        tv_dept = (TextView) rootView.findViewById(R.id.tv_dept);
        tv_helpTopic = (TextView) rootView.findViewById(R.id.tv_helpTopic);

        paddingTop = editTextEmail.getPaddingTop();
        paddingBottom = editTextEmail.getPaddingBottom();
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
