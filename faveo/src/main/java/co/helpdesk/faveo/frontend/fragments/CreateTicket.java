package co.helpdesk.faveo.frontend.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import co.helpdesk.faveo.Helper;
import co.helpdesk.faveo.Preference;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.activities.MainActivity;
import co.helpdesk.faveo.frontend.activities.SplashActivity;
import co.helpdesk.faveo.frontend.receivers.InternetReceiver;

public class CreateTicket extends Fragment {

    EditText editTextEmail, editTextName, editTextPhone, editTextSubject, editTextMessage;
    TextView textViewErrorEmail, textViewErrorName, textViewErrorPhone, textViewErrorSubject, textViewErrorMessage;
    Spinner spinnerHelpTopic, spinnerSLAPlans, spinnerAssignTo, spinnerPriority;
    Button buttonSubmit;
    ProgressDialog progressDialog;

    int paddingTop, paddingBottom;
    View rootView;

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
        if (rootView == null) {
            rootView = inflater.inflate(co.helpdesk.faveo.R.layout.fragment_create_ticket, container, false);
            setUpViews(rootView);
            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetViews();
                    String email = editTextEmail.getText().toString();
                    String name = editTextName.getText().toString();
                    String phone = editTextPhone.getText().toString();
                    String subject = editTextSubject.getText().toString();
                    String message = editTextMessage.getText().toString();
                    int helpTopic = spinnerHelpTopic.getSelectedItemPosition() + 1;
                    int SLAPlans = spinnerSLAPlans.getSelectedItemPosition() + 1;
                    int assignTo = spinnerAssignTo.getSelectedItemPosition() + 1;
                    int priority = spinnerPriority.getSelectedItemPosition() + 1;
                    boolean allCorrect = true;

                    if (email.trim().length() == 0 || !Helper.isValidEmail(email)) {
                        setErrorState(editTextEmail, textViewErrorEmail, "Invalid email");
                        allCorrect = false;
                    }

                    if (name.trim().length() == 0) {
                        setErrorState(editTextName, textViewErrorName, "Wrong name");
                        allCorrect = false;
                    }

                    if (subject.trim().length() == 0) {
                        setErrorState(editTextSubject, textViewErrorSubject, "Please fill the field");
                        allCorrect = false;
                    } else if (subject.trim().length() < 5) {
                        setErrorState(editTextSubject, textViewErrorSubject, "Subject should be minimum 5 characters");
                        allCorrect = false;
                    }

                    if (message.trim().length() == 0) {
                        setErrorState(editTextMessage, textViewErrorMessage, "Please fill the field");
                        allCorrect = false;
                    } else if (message.trim().length() < 5) {
                        setErrorState(editTextMessage, textViewErrorMessage, "Message should be minimum 5 characters");
                        allCorrect = false;
                    }

                    if (spinnerAssignTo.getSelectedItemPosition() == 1) {
                        Toast.makeText(getActivity(), "Invalid assignment", Toast.LENGTH_LONG).show();
                        setErrorState(editTextMessage, textViewErrorMessage, "Invalid assign");
                        allCorrect = false;
                    }

                    if (allCorrect) {
                        if (InternetReceiver.isConnected()) {
                            progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setMessage("Creating ticket");
                            progressDialog.show();
                            try {
                                name = URLEncoder.encode(name, "utf-8");
                                subject = URLEncoder.encode(subject, "utf-8");
                                message = URLEncoder.encode(message, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            new CreateNewTicket(Integer.parseInt(Preference.getUserID()), subject, message, helpTopic, SLAPlans, priority, assignTo).execute();

                        } else
                            Toast.makeText(v.getContext(), "Oops! No internet", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
        ((MainActivity) getActivity()).setActionBarTitle("Create ticket");
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
            if (result.contains("ticket_id")) {
                Toast.makeText(getActivity(), "Ticket created", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void resetViews() {
        editTextEmail.setBackgroundResource(co.helpdesk.faveo.R.drawable.edittext_theme_states);
        editTextEmail.setPadding(0, paddingTop, 0, paddingBottom);
        editTextName.setBackgroundResource(co.helpdesk.faveo.R.drawable.edittext_theme_states);
        editTextName.setPadding(0, paddingTop, 0, paddingBottom);
        editTextPhone.setBackgroundResource(co.helpdesk.faveo.R.drawable.edittext_theme_states);
        editTextPhone.setPadding(0, paddingTop, 0, paddingBottom);
        editTextSubject.setBackgroundResource(co.helpdesk.faveo.R.drawable.edittext_theme_states);
        editTextSubject.setPadding(0, paddingTop, 0, paddingBottom);
        editTextMessage.setBackgroundResource(co.helpdesk.faveo.R.drawable.edittext_theme_states);
        editTextMessage.setPadding(0, paddingTop, 0, paddingBottom);
        textViewErrorEmail.setText("");
        textViewErrorName.setText("");
        textViewErrorPhone.setText("");
        textViewErrorSubject.setText("");
        textViewErrorMessage.setText("");
    }

    private void setErrorState(EditText editText, TextView textViewError, String error) {
        editText.setBackgroundResource(co.helpdesk.faveo.R.drawable.edittext_error_state);
        editText.setPadding(0, paddingTop, 0, paddingBottom);
        textViewError.setText(error);
    }

    private void setUpViews(View rootView) {
        editTextEmail = (EditText) rootView.findViewById(co.helpdesk.faveo.R.id.editText_email);
        editTextName = (EditText) rootView.findViewById(co.helpdesk.faveo.R.id.editText_name);
        editTextPhone = (EditText) rootView.findViewById(co.helpdesk.faveo.R.id.editText_phone);
        editTextSubject = (EditText) rootView.findViewById(co.helpdesk.faveo.R.id.editText_subject);
        editTextMessage = (EditText) rootView.findViewById(co.helpdesk.faveo.R.id.editText_message);
        textViewErrorEmail = (TextView) rootView.findViewById(co.helpdesk.faveo.R.id.textView_error_email);
        textViewErrorName = (TextView) rootView.findViewById(co.helpdesk.faveo.R.id.textView_error_name);
        textViewErrorPhone = (TextView) rootView.findViewById(co.helpdesk.faveo.R.id.textView_error_phone);
        textViewErrorSubject = (TextView) rootView.findViewById(co.helpdesk.faveo.R.id.textView_error_subject);
        textViewErrorMessage = (TextView) rootView.findViewById(co.helpdesk.faveo.R.id.textView_error_message);

        spinnerHelpTopic = (Spinner) rootView.findViewById(co.helpdesk.faveo.R.id.spinner_help_topics);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, SplashActivity.valueTopic.split(",")); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHelpTopic.setAdapter(spinnerArrayAdapter);

        spinnerSLAPlans = (Spinner) rootView.findViewById(co.helpdesk.faveo.R.id.spinner_sla_plans);
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, SplashActivity.valueSLA.split(",")); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSLAPlans.setAdapter(spinnerArrayAdapter);

        spinnerAssignTo = (Spinner) rootView.findViewById(co.helpdesk.faveo.R.id.spinner_assign_to);
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, SplashActivity.valueDepartment.split(",")); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssignTo.setAdapter(spinnerArrayAdapter);

        spinnerPriority = (Spinner) rootView.findViewById(co.helpdesk.faveo.R.id.spinner_priority);
        spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, SplashActivity.valuePriority.split(",")); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(spinnerArrayAdapter);

        buttonSubmit = (Button) rootView.findViewById(co.helpdesk.faveo.R.id.button_submit);
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
