package com.android.faveo.frontend.fragments.ticketDetail;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.faveo.R;
import com.android.faveo.backend.api.v1.Helpdesk;
import com.android.faveo.frontend.adapters.TicketThreadAdapter;
import com.android.faveo.model.TicketOverview;
import com.android.faveo.frontend.adapters.TicketOverviewAdapter;
import com.android.faveo.frontend.activities.TicketDetailActivity;
import com.android.faveo.model.TicketThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Conversation extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;
    View rootView;

    TicketThreadAdapter ticketThreadAdapter;
    List<TicketThread> ticketThreadList = new ArrayList<>();
    ProgressDialog progressDialog;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static Conversation newInstance(String param1, String param2) {
        Conversation fragment = new Conversation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Conversation() {
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
            rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Fetching conversation");
            progressDialog.show();
            new FetchTicketThreads(getActivity()).execute();
        }

        return rootView;
    }

    public class FetchTicketThreads extends AsyncTask<String, Void, String> {
        Context context;

        public FetchTicketThreads(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            String result = new Helpdesk().getTicketThread(TicketDetailActivity.ticketID);
            if (result == null)
                return null;
            String data;
            try {
                JSONObject jsonObject = new JSONObject(result);
                try {
                    data = jsonObject.getString("data");
                } catch (JSONException e) {
                    data = jsonObject.getString("result");
                }
                JSONArray jsonArray = new JSONArray(data);
                for(int i = 0; i < jsonArray.length(); i++) {
                    TicketThread ticketThread = null;
                    try {
                        String clientName = jsonArray.getJSONObject(i).getString("poster");
                        String messageTime = jsonArray.getJSONObject(i).getString("created_at");
                        String messageTitle = jsonArray.getJSONObject(i).getString("title");
                        String message = jsonArray.getJSONObject(i).getString("body");
                        ticketThread = new TicketThread(clientName, messageTime, messageTitle, message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(ticketThread != null)
                        ticketThreadList.add(ticketThread);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result == null) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
            recyclerView.setHasFixedSize(false);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            ticketThreadAdapter = new TicketThreadAdapter(ticketThreadList);
            recyclerView.setAdapter(ticketThreadAdapter);
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
