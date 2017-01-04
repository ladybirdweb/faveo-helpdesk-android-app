package co.helpdesk.faveo.frontend.fragments.ticketDetail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.frontend.adapters.TicketThreadAdapter;
import co.helpdesk.faveo.model.TicketThread;

public class Conversation extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;
    View rootView;

    TicketThreadAdapter ticketThreadAdapter;
    List<TicketThread> ticketThreadList = new ArrayList<>();
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefresh;

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
            swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (ticketThreadList.size() != 0) {
                        ticketThreadList.clear();
                        ticketThreadAdapter.notifyDataSetChanged();
                        new FetchTicketThreads(getActivity()).execute();
                    }
                }
            });
        }

        return rootView;
    }

    public class FetchTicketThreads extends AsyncTask<String, Void, String> {
        Context context;

        FetchTicketThreads(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketThread(TicketDetailActivity.ticketID);
        }

        protected void onPostExecute(String result) {
            if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (result == null) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    TicketThread ticketThread = null;
                    try {
                        String clientPicture = null;
                        try {
                            clientPicture = jsonArray.getJSONObject(i).getString("profile_pic");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
/*                        String clientName = jsonArray.getJSONObject(i).getString("poster");
                        if (clientName.equals("null") || clientName.equals(""))
                            clientName = "NOTE";*/
                        String clientName = jsonArray.getJSONObject(i).getString("first_name") + " " + jsonArray.getJSONObject(i).getString("last_name");
                        if (clientName.equals("null") || clientName.equals(""))
                            clientName = jsonArray.getJSONObject(i).getString("user_name");
                        String messageTime = jsonArray.getJSONObject(i).getString("created_at");
                        String messageTitle = jsonArray.getJSONObject(i).getString("title");
                        String message = jsonArray.getJSONObject(i).getString("body");
                        Log.d("body:", message);
                        String isReply = jsonArray.getJSONObject(i).getString("is_internal").equals("0") ? "false" : "true";
                        ticketThread = new TicketThread(clientPicture, clientName, messageTime, messageTitle, message, isReply);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (ticketThread != null)
                        ticketThreadList.add(ticketThread);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
            recyclerView.setHasFixedSize(false);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            Collections.reverse(ticketThreadList);
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

    public void addThreadAndUpdate(TicketThread thread) {
        ticketThreadList.add(0, thread);
        ticketThreadAdapter.notifyDataSetChanged();
    }

}
