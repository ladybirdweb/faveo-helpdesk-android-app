package com.android.faveo.frontend.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.faveo.Constants;
import com.android.faveo.Preference;
import com.android.faveo.R;
import com.android.faveo.backend.api.v1.Authenticate;
import com.android.faveo.backend.api.v1.Helpdesk;
import com.android.faveo.frontend.activities.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateTicket extends Fragment {

    EditText editTextEmail, editTextName, editTextPhone, editTextSubject, editTextMessage;
    TextView textViewErrorEmail,textViewErrorName, textViewErrorPhone, textViewErrorSubject, textViewErrorMessage;
    Spinner spinnerHelpTopic, spinnerSLAPlans, spinnerAssignTo, spinnerPriority;
    Button buttonSubmit;
    ProgressDialog progressDialog;

    int paddingTop, paddingBottom;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static CreateTicket newInstance(String param1, String param2) {
        CreateTicket fragment = new CreateTicket();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateTicket() {
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
        View rootView = inflater.inflate(R.layout.fragment_create_ticket, container, false);
        setUpViews(rootView);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetViews();

                String email = editTextEmail.getText().toString();
                String name = editTextName.getText().toString();
                String phone = editTextPhone.getText().toString();
                String subject = editTextSubject.getText().toString();
                String message = editTextEmail.getText().toString();
                int helpTopic = spinnerHelpTopic.getSelectedItemPosition() + 1;
                int SLAPlans = spinnerSLAPlans.getSelectedItemPosition() + 1;
                int assignTo = spinnerAssignTo.getSelectedItemPosition() + 1;
                int priority = spinnerPriority.getSelectedItemPosition() + 1;

                boolean allCorrect = true;

                if (email.trim().length() == 0) {
                    setErrorState(editTextEmail, textViewErrorEmail, "Invalid email");
                    allCorrect = false;
                }

                if (name.trim().length() == 0) {
                    setErrorState(editTextName, textViewErrorName, "Wrong name");
                    allCorrect = false;
                }

                if (phone.trim().length() == 0) {
                    setErrorState(editTextPhone, textViewErrorPhone, "Invalid phone");
                    allCorrect = false;
                }

                if (subject.trim().length() == 0) {
                    setErrorState(editTextSubject, textViewErrorSubject, "Invalid subject");
                    allCorrect = false;
                }

                if (message.trim().length() == 0) {
                    setErrorState(editTextMessage, textViewErrorMessage, "Invalid message");
                    allCorrect = false;
                }

                if (allCorrect) {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Creating ticket");
                    progressDialog.show();
                    new CreateNewTicket(Integer.parseInt(Preference.getUserID()), subject, message, helpTopic, SLAPlans, priority, assignTo).execute();
                }

            }
        });

        return rootView;
    }

    public class CreateNewTicket extends AsyncTask<String, Void, String> {
        int userID;
        String subject;
        String body;
        int helpTopic;
        int SLA;
        int priority;
        int dept;

        public CreateNewTicket(int userID, String subject, String body,
                               int helpTopic, int SLA, int priority, int dept) {
            this.userID = userID;
            this.subject = subject;
            this.body = body;
            this.helpTopic = helpTopic;
            this.SLA = SLA;
            this.priority = priority;
            this.dept = dept;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postCreateTicket(userID, subject, body, helpTopic, SLA, priority, dept);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result == null) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
        }
    }

    private void resetViews() {
        editTextEmail.setBackgroundResource(R.drawable.edittext_theme_states);
        editTextEmail.setPadding(0, paddingTop, 0, paddingBottom);
        editTextName.setBackgroundResource(R.drawable.edittext_theme_states);
        editTextName.setPadding(0, paddingTop, 0, paddingBottom);
        editTextPhone.setBackgroundResource(R.drawable.edittext_theme_states);
        editTextPhone.setPadding(0, paddingTop, 0, paddingBottom);
        editTextSubject.setBackgroundResource(R.drawable.edittext_theme_states);
        editTextSubject.setPadding(0, paddingTop, 0, paddingBottom);
        editTextMessage.setBackgroundResource(R.drawable.edittext_theme_states);
        editTextMessage.setPadding(0, paddingTop, 0, paddingBottom);
        textViewErrorEmail.setText("");
        textViewErrorName.setText("");
        textViewErrorPhone.setText("");
        textViewErrorSubject.setText("");
        textViewErrorMessage.setText("");
    }

    private void setErrorState(EditText editText, TextView textViewError, String error) {
        editText.setBackgroundResource(R.drawable.edittext_error_state);
        editText.setPadding(0, paddingTop, 0, paddingBottom);
        textViewError.setText(error);
    }

    private void setUpViews(View rootView) {
        editTextEmail = (EditText) rootView.findViewById(R.id.editText_email);
        editTextName = (EditText) rootView.findViewById(R.id.editText_name);
        editTextPhone = (EditText) rootView.findViewById(R.id.editText_phone);
        editTextSubject = (EditText) rootView.findViewById(R.id.editText_subject);
        editTextMessage = (EditText) rootView.findViewById(R.id.editText_message);
        textViewErrorEmail = (TextView) rootView.findViewById(R.id.textView_error_email);
        textViewErrorName = (TextView) rootView.findViewById(R.id.textView_error_name);
        textViewErrorPhone = (TextView) rootView.findViewById(R.id.textView_error_phone);
        textViewErrorSubject = (TextView) rootView.findViewById(R.id.textView_error_subject);
        textViewErrorMessage = (TextView) rootView.findViewById(R.id.textView_error_message);
        spinnerHelpTopic = (Spinner) rootView.findViewById(R.id.spinner_help_topics);
        spinnerSLAPlans = (Spinner) rootView.findViewById(R.id.spinner_sla_plans);
        spinnerAssignTo = (Spinner) rootView.findViewById(R.id.spinner_assign_to);
        spinnerPriority = (Spinner) rootView.findViewById(R.id.spinner_priority);
        buttonSubmit = (Button) rootView.findViewById(R.id.button_submit);
        paddingTop = editTextEmail.getPaddingTop();
        paddingBottom = editTextEmail.getPaddingBottom();
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
