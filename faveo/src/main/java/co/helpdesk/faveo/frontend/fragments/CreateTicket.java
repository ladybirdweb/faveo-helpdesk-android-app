package co.helpdesk.faveo.frontend.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import co.helpdesk.faveo.Helper;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.Utils;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.activities.MainActivity;
import co.helpdesk.faveo.frontend.activities.SplashActivity;
import co.helpdesk.faveo.frontend.receivers.InternetReceiver;

public class CreateTicket extends Fragment {

    EditText editTextEmail, editTextLastName, editTextFirstName, editTextPhone, editTextSubject, editTextMessage;
    TextView textViewErrorEmail, textViewErrorLastName, textViewErrorFirstName, textViewErrorPhone, textViewErrorSubject, textViewErrorMessage;
    Spinner spinnerHelpTopic, spinnerSLAPlans, spinnerAssignTo, spinnerPriority, spinnerCountryCode;
    Button buttonSubmit;
    ProgressDialog progressDialog;
    int paddingTop, paddingBottom;
    View rootView;

    ArrayAdapter<String> spinnerSlaArrayAdapter, spinnerAssignToArrayAdapter,
            spinnerHelpArrayAdapter, spinnerDeptArrayAdapter, spinnerPriArrayAdapter;

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
    /**
     *
     * @param savedInstanceState under special circumstances, to restore themselves to a previous
     * state using the data stored in this bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public int getCountryZipCode() {
        String CountryID = "";
        String CountryZipCode = "";
        int code = 0;

        TelephonyManager manager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = this.getResources().getStringArray(R.array.spinnerCountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                code = i;
                break;
            }
        }
        return code;
    }
    /**
     *
     * @param inflater for loading the fragment.
     * @param container where the fragment is going to be load.
     * @param savedInstanceState
     * @return after initializing returning the rootview
     * which is having the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_create_ticket, container, false);
            setUpViews(rootView);
            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetViews();
                    String email = editTextEmail.getText().toString();
                    String fname = editTextFirstName.getText().toString();
                    String lname = editTextLastName.getText().toString();
                    String phone = editTextPhone.getText().toString();
                    String subject = editTextSubject.getText().toString();
                    String message = editTextMessage.getText().toString();
                    int helpTopic = spinnerHelpTopic.getSelectedItemPosition() + 1;
                    int SLAPlans = spinnerSLAPlans.getSelectedItemPosition() + 1;
                    int assignTo = spinnerAssignTo.getSelectedItemPosition() + 1;
                    int priority = spinnerPriority.getSelectedItemPosition() + 1;
                    String countrycode = spinnerCountryCode.getSelectedItem().toString();
                    String[] cc = countrycode.split(",");
                    countrycode = cc[0];
                    boolean allCorrect = true;

                    if (email.trim().length() == 0 || !Helper.isValidEmail(email)) {
                        setErrorState(editTextEmail, textViewErrorEmail, getString(R.string.invalid_email));
                        allCorrect = false;
                    }

                    if (fname.trim().length() == 0) {
                        setErrorState(editTextFirstName, textViewErrorFirstName, getString(R.string.fill_firstname));
                        allCorrect = false;
                    } else if (fname.trim().length() < 3) {
                        setErrorState(editTextFirstName, textViewErrorFirstName, getString(R.string.firstname_minimum_char));
                        allCorrect = false;
                    }
                    if (lname.trim().length() == 0) {
                        setErrorState(editTextLastName, textViewErrorLastName, getString(R.string.fill_lastname));
                        allCorrect = false;
                    }

                    if (subject.trim().length() == 0) {
                        setErrorState(editTextSubject, textViewErrorSubject, getString(R.string.please_fill_field));
                        allCorrect = false;
                    } else if (subject.trim().length() < 5) {
                        setErrorState(editTextSubject, textViewErrorSubject, getString(R.string.sub_minimum_char));
                        allCorrect = false;
                    }

                    if (message.trim().length() == 0) {
                        setErrorState(editTextMessage, textViewErrorMessage, getString(R.string.please_fill_field));
                        allCorrect = false;
                    } else if (message.trim().length() < 10) {
                        setErrorState(editTextMessage, textViewErrorMessage, getString(R.string.msg_minimum_char));
                        allCorrect = false;
                    }

//                    if (spinnerAssignTo.getSelectedItemPosition() == 1) {
//                        Toast.makeText(getActivity(), "Invalid assignment", Toast.LENGTH_LONG).show();
//                        setErrorState(editTextMessage, textViewErrorMessage, "Invalid assign");
//                        allCorrect = false;
//                    }

                    if (allCorrect) {
                        if (InternetReceiver.isConnected()) {
                            progressDialog = new ProgressDialog(getActivity());
                            progressDialog.setMessage(getString(R.string.creating_ticket));
                            progressDialog.show();
                            try {
                                fname = URLEncoder.encode(fname, "utf-8");
                                lname = URLEncoder.encode(lname, "utf-8");
                                subject = URLEncoder.encode(subject, "utf-8");
                                message = URLEncoder.encode(message, "utf-8");
                                email = URLEncoder.encode(email, "utf-8");
                                phone = URLEncoder.encode(phone, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            new CreateNewTicket(Integer.parseInt(Prefs.getString("ID", null)), subject, message, helpTopic, SLAPlans, priority, assignTo, phone, fname, lname, email, countrycode).execute();
                        } else
                            Toast.makeText(v.getContext(), R.string.oops_no_internet, Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.create_ticket));
        return rootView;
    }

    private class CreateNewTicket extends AsyncTask<String, Void, String> {
        int userID;
        String phone;
        String subject;
        String body;
        String fname, lname, email;
        int helpTopic;
        int SLA;
        int priority;
        int dept;
        String code;

        CreateNewTicket(int userID, String subject, String body,
                        int helpTopic, int SLA, int priority, int dept, String phone, String fname, String lname, String email, String code) {
            this.userID = userID;
            this.subject = subject;
            this.body = body;
            this.helpTopic = helpTopic;
            this.SLA = SLA;
            this.priority = priority;
            this.dept = dept;
            this.phone = phone;
            this.lname = lname;
            this.fname = fname;
            this.email = email;
            this.code = code;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postCreateTicket(userID, subject, body, helpTopic,SLA, priority, fname, lname, phone, email, code,"");
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result == null) {
                Toast.makeText(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            if (result.contains("NotificationThread created successfully!")) {
                Toast.makeText(getActivity(), getString(R.string.ticket_created_success), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void resetViews() {

        editTextEmail.setBackgroundResource(R.drawable.edittext_theme_states);
        editTextEmail.setPadding(0, paddingTop, 0, paddingBottom);
        editTextFirstName.setBackgroundResource(co.helpdesk.faveo.R.drawable.edittext_theme_states);
        editTextFirstName.setPadding(0, paddingTop, 0, paddingBottom);
        editTextLastName.setBackgroundResource(co.helpdesk.faveo.R.drawable.edittext_theme_states);
        editTextLastName.setPadding(0, paddingTop, 0, paddingBottom);
        editTextPhone.setBackgroundResource(R.drawable.edittext_theme_states);
        editTextPhone.setPadding(0, paddingTop, 0, paddingBottom);
        editTextSubject.setBackgroundResource(R.drawable.edittext_theme_states);
        editTextSubject.setPadding(0, paddingTop, 0, paddingBottom);
        editTextMessage.setBackgroundResource(R.drawable.edittext_theme_states);
        editTextMessage.setPadding(0, paddingTop, 0, paddingBottom);
        textViewErrorEmail.setText("");
        textViewErrorFirstName.setText("");
        textViewErrorLastName.setText("");
        textViewErrorPhone.setText("");
        textViewErrorSubject.setText("");
        textViewErrorMessage.setText("");
    }

    private void setErrorState(EditText editText, TextView textViewError, String error) {
        editText.setBackgroundResource(R.drawable.edittext_error_state);
        editText.setPadding(0, paddingTop, 0, paddingBottom);
        textViewError.setText(error);
    }

    /**
     * For initializing all the views used
     * in create ticket fragment.
     * @param rootView
     */
    private void setUpViews(View rootView) {
        editTextEmail = (EditText) rootView.findViewById(co.helpdesk.faveo.R.id.editText_email);
        editTextFirstName = (EditText) rootView.findViewById(co.helpdesk.faveo.R.id.editText_firstname);
        editTextLastName = (EditText) rootView.findViewById(co.helpdesk.faveo.R.id.editText_lastname);
        editTextPhone = (EditText) rootView.findViewById(co.helpdesk.faveo.R.id.editText_phone);

        editTextSubject = (EditText) rootView.findViewById(co.helpdesk.faveo.R.id.editText_subject);
        editTextMessage = (EditText) rootView.findViewById(co.helpdesk.faveo.R.id.editText_message);
        textViewErrorEmail = (TextView) rootView.findViewById(co.helpdesk.faveo.R.id.textView_error_email);
        textViewErrorFirstName = (TextView) rootView.findViewById(co.helpdesk.faveo.R.id.textView_error_firstname);
        textViewErrorLastName = (TextView) rootView.findViewById(co.helpdesk.faveo.R.id.textView_error_lastname);
        textViewErrorPhone = (TextView) rootView.findViewById(co.helpdesk.faveo.R.id.textView_error_phone);
        textViewErrorSubject = (TextView) rootView.findViewById(co.helpdesk.faveo.R.id.textView_error_subject);
        textViewErrorMessage = (TextView) rootView.findViewById(co.helpdesk.faveo.R.id.textView_error_message);

        spinnerHelpTopic = (Spinner) rootView.findViewById(co.helpdesk.faveo.R.id.spinner_help_topics);
        spinnerHelpArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueTopic.split(","))); //selected item will look like a spinner set from XML
        spinnerHelpArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHelpTopic.setAdapter(spinnerHelpArrayAdapter);

        spinnerSLAPlans = (Spinner) rootView.findViewById(co.helpdesk.faveo.R.id.spinner_sla_plans);
        spinnerSlaArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueSLA.split(","))); //selected item will look like a spinner set from XML
        spinnerSlaArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSLAPlans.setAdapter(spinnerSlaArrayAdapter);

        spinnerAssignTo = (Spinner) rootView.findViewById(co.helpdesk.faveo.R.id.spinner_assign_to);
        spinnerAssignToArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valueDepartment.split(","))); //selected item will look like a spinner set from XML
        spinnerAssignToArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAssignTo.setAdapter(spinnerAssignToArrayAdapter);

        spinnerPriority = (Spinner) rootView.findViewById(co.helpdesk.faveo.R.id.spinner_priority);
        spinnerPriArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, Utils.removeDuplicates(SplashActivity.valuePriority.split(","))); //selected item will look like a spinner set from XML
        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(spinnerPriArrayAdapter);

        spinnerCountryCode = (Spinner) rootView.findViewById(R.id.spinner_code);
        spinnerCountryCode.setSelection(getCountryZipCode());
        buttonSubmit = (Button) rootView.findViewById(co.helpdesk.faveo.R.id.button_submit);
        paddingTop = editTextEmail.getPaddingTop();
        paddingBottom = editTextEmail.getPaddingBottom();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    /**
     * When the fragment is going to be attached
     * this life cycle method is going to be called.
     * @param context refers to the current fragment.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Settings.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    /**
     * Once the fragment is going to be detached then
     * this method is going to be called.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


}
