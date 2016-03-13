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

    EditText editTextSubject, editTextSLA, editTextStatus, editTextPriority, editTextDepartment,
            editTextHelpTopic, editTextName, editTextEmail, editTextSource, editTextLastMessage,
            editTextDueDate, editTextCreatedDate, editTextLastResponseDate;

    Spinner spinnerAssignTo, spinnerChangeStatus;
    ProgressDialog progressDialog;

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
                switch (jsonObject.getString("sla")) {
                    case "1":
                        editTextSLA.setText("6 hours");
                        break;
                    case "2":
                        editTextSLA.setText("12 hours");
                        break;
                    case "3":
                        editTextSLA.setText("18 hours");
                        break;
                    default:
                        editTextSLA.setText("Not available");
                }
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
                switch (jsonObject.getString("priority_id")) {
                    case "1":
                        editTextPriority.setText("Low");
                        break;
                    case "2":
                        editTextPriority.setText("Medium");
                        break;
                    case "3":
                        editTextPriority.setText("High");
                        break;
                    case "4":
                        editTextPriority.setText("Emergency");
                        break;
                    default:
                        editTextPriority.setText("Not available");
                }
                editTextDepartment.setText(jsonObject.getString("dept_id"));
                switch (jsonObject.getString("help_topic_id")) {
                    case "1":
                        editTextStatus.setText("Sales query");
                        break;
                    case "2":
                        editTextStatus.setText("Support query");
                        break;
                    default:
                        editTextStatus.setText("Not available");
                }
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

    private void setUpViews(View rootView) {
        editTextSubject = (EditText) rootView.findViewById(R.id.editText_subject);
        editTextSLA = (EditText) rootView.findViewById(R.id.editText_sla);
        editTextStatus = (EditText) rootView.findViewById(R.id.editText_status);
        editTextPriority = (EditText) rootView.findViewById(R.id.editText_priority);
        editTextDepartment = (EditText) rootView.findViewById(R.id.editText_department);
        editTextHelpTopic = (EditText) rootView.findViewById(R.id.editText_help_topic);
        editTextName = (EditText) rootView.findViewById(R.id.editText_name);
        editTextEmail = (EditText) rootView.findViewById(R.id.editText_email);
        editTextSource = (EditText) rootView.findViewById(R.id.editText_source);
        editTextLastMessage = (EditText) rootView.findViewById(R.id.editText_last_message);
        editTextDueDate = (EditText) rootView.findViewById(R.id.editText_due_date);
        editTextCreatedDate = (EditText) rootView.findViewById(R.id.editText_created_date);
        editTextLastResponseDate = (EditText) rootView.findViewById(R.id.editText_last_response_date);
        spinnerAssignTo = (Spinner) rootView.findViewById(R.id.spinner_assign_to);
        spinnerChangeStatus = (Spinner) rootView.findViewById(R.id.spinner_change_status);
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
