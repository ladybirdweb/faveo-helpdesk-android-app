package co.helpdesk.faveo.frontend.fragments.ticketDetail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import co.helpdesk.faveo.Helper;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.activities.TicketDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Detail extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText editTextSubject, editTextStatus, editTextDepartment, editTextName, editTextEmail,
            editTextSource, editTextLastMessage, editTextDueDate, editTextCreatedDate, editTextLastResponseDate;

    Spinner spinnerSLAPlans, spinnerPriority, spinnerHelpTopics, spinnerAssignTo, spinnerChangeStatus;
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
                progressDialog.setMessage("Updating ticket");
                progressDialog.show();
                new SaveTicket(getActivity(),
                        Integer.parseInt(TicketDetailActivity.ticketID),
                        editTextSubject.getText().toString(),
                        spinnerSLAPlans.getSelectedItemPosition(),
                        spinnerHelpTopics.getSelectedItemPosition(),
                        Integer.parseInt(editTextSource.getText().toString()),
                        spinnerPriority.getSelectedItemPosition()).execute();
            }
        });
        return rootView;
    }

    public class FetchTicketDetail extends AsyncTask<String, Void, String> {
        Context context;
        String ticketNumber;

        public FetchTicketDetail(Context context, String ticketNumber) {
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

            JSONObject jsonObject;
            try {
                JSONObject jsonObjectResult = new JSONObject(result);
                jsonObject = jsonObjectResult.getJSONObject("result");
                try {
                    editTextSubject.setText(jsonObject.getString("subject"));
                } catch (JSONException e) {
                    editTextSubject.setText("Not available");
                }
                try {
                    spinnerSLAPlans.setSelection(Integer.parseInt(jsonObject.getString("sla")));
                } catch(Exception e) { }

                switch (jsonObject.getString("status")) {
                    case "1":
                        editTextStatus.setText("Open");
                        break;
                    case "2":
                        editTextStatus.setText("Closed");
                        break;
                    case "3":
                        editTextStatus.setText("Resolved");
                        break;
                    case "4":
                        editTextStatus.setText("Deleted");
                        break;
                    default:
                        editTextStatus.setText("Not available");
                }

                try {
                    spinnerPriority.setSelection(Integer.parseInt(jsonObject.getString("priority_id")));
                } catch(Exception e) { }

                editTextDepartment.setText(jsonObject.getString("dept_id"));

                try {
                    spinnerHelpTopics.setSelection(Integer.parseInt(jsonObject.getString("help_topic_id")));
                } catch(Exception e) { }

                editTextName.setText(jsonObject.getString("assigned_to"));
                try {
                    editTextEmail.setText(jsonObject.getString("email"));
                } catch (JSONException e) {
                    editTextEmail.setText("Not available");
                }
                editTextSource.setText(jsonObject.getString("source"));
                try {
                    editTextLastMessage.setText(jsonObject.getString("last_message"));
                } catch (JSONException e) {
                    editTextLastMessage.setText("Not available");
                }
                editTextDueDate.setText(Helper.parseDate(jsonObject.getString("duedate")));
                editTextCreatedDate.setText(Helper.parseDate(jsonObject.getString("created_at")));
                editTextLastResponseDate.setText(Helper.parseDate(jsonObject.getString("updated_at")));
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

        public SaveTicket(Context context, int ticketNumber, String subject,
                          int slaPlan, int helpTopic, int ticketSource, int ticketPriority) {
            this.context = context;
            this.ticketNumber = ticketNumber;
            this.subject = subject;
            this.slaPlan = slaPlan;
            this.helpTopic = helpTopic;
            this.ticketSource = ticketSource;
            this.ticketPriority = ticketPriority;
        }

        protected String doInBackground(String... urls) {
            if (subject.equals("Not available"))
                subject = "";
            return new Helpdesk().postEditTicket(ticketNumber, subject, slaPlan,
                    helpTopic, ticketSource, ticketPriority);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
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
        editTextSubject = (EditText) rootView.findViewById(R.id.editText_subject);
        spinnerSLAPlans = (Spinner) rootView.findViewById(R.id.spinner_sla_plans);
        editTextStatus = (EditText) rootView.findViewById(R.id.editText_status);
        spinnerPriority = (Spinner) rootView.findViewById(R.id.spinner_priority);
        editTextDepartment = (EditText) rootView.findViewById(R.id.editText_department);
        spinnerHelpTopics = (Spinner) rootView.findViewById(R.id.spinner_help_topics);
        editTextName = (EditText) rootView.findViewById(R.id.editText_name);
        editTextEmail = (EditText) rootView.findViewById(R.id.editText_email);
        editTextSource = (EditText) rootView.findViewById(R.id.editText_source);
        editTextLastMessage = (EditText) rootView.findViewById(R.id.editText_last_message);
        editTextDueDate = (EditText) rootView.findViewById(R.id.editText_due_date);
        editTextCreatedDate = (EditText) rootView.findViewById(R.id.editText_created_date);
        editTextLastResponseDate = (EditText) rootView.findViewById(R.id.editText_last_response_date);
        spinnerAssignTo = (Spinner) rootView.findViewById(R.id.spinner_assign_to);
        spinnerChangeStatus = (Spinner) rootView.findViewById(R.id.spinner_change_status);
        buttonSave = (Button) rootView.findViewById(R.id.button_save);
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
