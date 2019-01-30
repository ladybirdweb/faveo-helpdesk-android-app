package co.helpdesk.faveo.frontend.fragments.client;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.adapters.TicketGlimpseAdapter;
import co.helpdesk.faveo.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.model.TicketGlimpse;
import es.dmoral.toasty.Toasty;

public class Profile extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    View rootView;
    EditText userName, firstName, email, phoneEditText, mobileEdittext;
    public String clientID;
    private OnFragmentInteractionListener mListener;

    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Profile() {
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
            clientID = getArguments().getString( "userId", null);
            rootView = inflater.inflate(R.layout.userprofile, container, false);
            userName= (EditText) rootView.findViewById(R.id.username);
            firstName= (EditText) rootView.findViewById(R.id.firstname);
            email= (EditText) rootView.findViewById(R.id.email);
            phoneEditText= (EditText) rootView.findViewById(R.id.phone);
            mobileEdittext= (EditText) rootView.findViewById(R.id.mobile);
            phoneEditText= (EditText) rootView.findViewById(R.id.phone);

            if (InternetReceiver.isConnected()){
                new FetchClientTickets(getActivity()).execute();
            }
        }

        return rootView;
    }


    private class FetchClientTickets extends AsyncTask<String, Void, String> {
        Context context;
        FetchClientTickets(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketsByUser(clientID);
        }

        protected void onPostExecute(String result) {
            if (isCancelled()) return;

            if (result == null) return;
            try{
                JSONObject jsonObject = new JSONObject(result);
                String error=jsonObject.getString("error");
                if (error.equals("This is not a client")){
                    Toasty.info(getActivity(), "This is not a client", Toast.LENGTH_LONG).show();
                    return;
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject requester = jsonObject.getJSONObject("requester");
                Prefs.putString("clientProfile",requester.toString());
                String firstname = requester.getString("first_name");
                String lastName = requester.getString("last_name");
                String username = requester.getString("user_name");
                String clientname;

                if (!username.equals("")){
                    userName.setText(username);
                }
                else{
                    userName.setText("");
                }
                if (firstname == null || firstname.equals(""))
                    clientname = username;
                else
                    clientname = firstname + " " + lastName;
                if (!clientname.equals("")){
                    email.setVisibility(View.VISIBLE);
                    firstName.setText(clientname);
                    email.setText(requester.getString("email"));
                }
                String phone = "";
                String mobile="";
                phone = requester.getString("phone_number");
                mobile=requester.getString("mobile");


                if (phone.equals("null")||phone.equals("")||phone.equals("Not available")||phone.equals("")){
                    phoneEditText.setVisibility(View.VISIBLE);
                }else {
                    phoneEditText.setVisibility(View.VISIBLE);
                    phoneEditText.setText(phone);
                }
                if (mobile.equals("null")||mobile.equals("")||mobile.equals("Not available")){
                    mobileEdittext.setVisibility(View.VISIBLE);
                }
                else {
                    mobileEdittext.setVisibility(View.VISIBLE);
                    mobileEdittext.setText(mobile);
                }


            } catch (JSONException e) {
                Toasty.error(getActivity(), getString(R.string.unexpected_error), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
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
