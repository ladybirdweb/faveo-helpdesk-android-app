package co.helpdesk.faveo.frontend.fragments.ticketDetail;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.frontend.adapters.TicketThreadAdapter;
import co.helpdesk.faveo.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.model.TicketThread;
import es.dmoral.toasty.Toasty;

/**
 *This is the Fragment for showing the conversation details.
 */
public class Conversation extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    AsyncTask<String, Void, String> task;
    @BindView(R.id.cardList)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.empty_view)
    TextView empty_view;

    @BindView(R.id.noiternet_view)
    TextView noInternet_view;
//    @BindView(R.id.totalcount)
//    TextView textView;
    @BindView(R.id.totalcount)
        TextView textViewTotalCount;
    View rootView;

    TicketThreadAdapter ticketThreadAdapter;
    List<TicketThread> ticketThreadList = new ArrayList<>();

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
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
            ButterKnife.bind(this, rootView);
            swipeRefresh.setColorSchemeResources(R.color.faveo_blue);
            swipeRefresh.setRefreshing( false );
            swipeRefresh.setEnabled( false );
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.pleaseWait));

            textViewTotalCount.setVisibility(View.GONE);
            if (InternetReceiver.isConnected()) {
                noInternet_view.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(true);
                task = new FetchTicketThreads(getActivity());
                task.execute();
            } else {
                noInternet_view.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                empty_view.setVisibility(View.GONE);
            }

//            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    if (InternetReceiver.isConnected()) {
////                        loading = true;
//                        recyclerView.setVisibility(View.VISIBLE);
//                        noInternet_view.setVisibility(View.GONE);
//                        if (ticketThreadList.size() != 0) {
//                            ticketThreadList.clear();
//                            ticketThreadAdapter.notifyDataSetChanged();
//                            task = new FetchTicketThreads(getActivity());
//                            task.execute();
//                        }
//                    } else {
//                        recyclerView.setVisibility(View.INVISIBLE);
//                        swipeRefresh.setRefreshing(false);
//                        empty_view.setVisibility(View.GONE);
//                        noInternet_view.setVisibility(View.VISIBLE);
//                    }
//
//                }
//            });
        }

        return rootView;
    }

    private class FetchTicketThreads extends AsyncTask<String, Void, String> {
        Context context;

        FetchTicketThreads(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketThread(Prefs.getString("TICKETid",null));
        }

        protected void onPostExecute(String result) {
            swipeRefresh.setRefreshing(false);
            if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);
            if (result == null) {
                Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
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
                        String firstName = jsonArray.getJSONObject(i).getString("first_name");
                        String userName=jsonArray.getJSONObject(i).getString("user_name");
                        String lastName = jsonArray.getJSONObject(i).getString("last_name");
                        String clientName = firstName + " " + lastName;
                        String f = "", l = "";
                        if (firstName.trim().length() != 0) {
                            f = firstName.substring(0, 1);
                        }
                        if (lastName.trim().length() != 0) {
                            l = lastName.substring(0, 1);
                        }
//                        if ((clientName.equals("null null") || clientName.equals(""))&&userName.equals("")){
//                            clientName="system";
//                        }else
                        if (firstName.equals("null")&&lastName.equals("null")&&userName.equals("null")){
                            clientName="System Generated";
                        }
                        else if (clientName.equals("")&&userName.equals("null")&&userName.equals("null")){
                            clientName="System Generated";
                        }
                        else if ((firstName.equals("null"))&&(lastName.equals("null"))&&(userName!=null)){
                            clientName=userName;
                        }
                        else if (firstName.equals("")&&(lastName.equals(""))&&(userName!=null)){
                            clientName=userName;
                        }
                        else if (firstName!=null||lastName!=null) {
                            clientName = firstName+" "+lastName;
                        }




                        String messageTime = jsonArray.getJSONObject(i).getString("created_at");
                        String messageTitle = jsonArray.getJSONObject(i).getString("title");
                        String message = jsonArray.getJSONObject(i).getString("body");
                        Log.d("body:", message);
                        String isReply = jsonArray.getJSONObject(i).getString("is_internal").equals("0") ? "false" : "true";
                        ticketThread = new TicketThread(clientPicture, clientName, messageTime, messageTitle, message, isReply, f + l);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (ticketThread != null)
                        ticketThreadList.add(ticketThread);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            recyclerView.setHasFixedSize(false);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            //Collections.reverse(ticketThreadList);
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


//    public void addThreadAndUpdate(TicketThread thread) {
//        ticketThreadList.add(0, thread);
//        ticketThreadAdapter.notifyDataSetChanged();
//    }

    public void refresh() {

        if (InternetReceiver.isConnected()) {
//                        loading = true;
            recyclerView.setVisibility(View.VISIBLE);
            noInternet_view.setVisibility(View.GONE);
            if (ticketThreadList.size() != 0) {
                ticketThreadList.clear();
                ticketThreadAdapter.notifyDataSetChanged();
                new FetchTicketThreads(getActivity()).execute();
                //task.execute();
            }
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            swipeRefresh.setRefreshing(false);
            empty_view.setVisibility(View.GONE);
            noInternet_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
//        progressDialog.show();
//        if (InternetReceiver.isConnected()){
//            new FetchTicketThreads(getActivity()).execute();
//        }
        super.onResume();

    }

    @Override
    public void onPause() {
        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);
            Log.d("Async Detail", "Cancelled");
        }
        super.onPause();
    }
}
