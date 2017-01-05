package co.helpdesk.faveo.frontend.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.helpdesk.faveo.Helper;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.activities.ClientDetailActivity;
import co.helpdesk.faveo.frontend.activities.MainActivity;
import co.helpdesk.faveo.frontend.adapters.ClientOverviewAdapter;
import co.helpdesk.faveo.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.model.ClientOverview;

public class ClientList extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    static String nextPageURL = "";

    TextView tv;
    RecyclerView recyclerView;

    ClientOverviewAdapter clientOverviewAdapter;
    List<ClientOverview> clientOverviewList = new ArrayList<>();
    View rootView;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefresh;

    private boolean loading = true;
    int pastVisibleItems, visibleItemCount, totalItemCount;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static ClientList newInstance(String param1, String param2) {
        ClientList fragment = new ClientList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ClientList() {
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
            recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
            recyclerView.setHasFixedSize(false);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Fetching clients");
            if (InternetReceiver.isConnected()) {
                progressDialog.show();
                new FetchClients(getActivity()).execute();
            } else
                Toast.makeText(getActivity(), "Oops! No internet", Toast.LENGTH_LONG).show();

            swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
            swipeRefresh.setColorSchemeResources(R.color.faveo_blue);
            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (InternetReceiver.isConnected()) {
                        new FetchClients(getActivity()).execute();
                    } else
                        Toast.makeText(getActivity(), "Oops! No internet", Toast.LENGTH_LONG).show();
                }
            });
            tv = (TextView) rootView.findViewById(R.id.empty_view);
            tv.setText("No Clients!");
        }
        ((MainActivity) getActivity()).setActionBarTitle("Client list");
        return rootView;
    }

    public class FetchClients extends AsyncTask<String, Void, String> {
        Context context;

       FetchClients(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
//            if (nextPageURL.equals("null")) {
//                return "all done";
//            }
            String result = new Helpdesk().getCustomersOverview();
            if (result == null)
                return null;
            String data;
            clientOverviewList.clear();
            try {
                JSONObject jsonObject = new JSONObject(result);
                data = jsonObject.getString("data");
                nextPageURL = jsonObject.getString("next_page_url");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    ClientOverview clientOverview = Helper.parseClientOverview(jsonArray, i);
                    if (clientOverview != null)
                        clientOverviewList.add(clientOverview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
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
            if (result.equals("all done")) {

                Toast.makeText(context, "All Done!", Toast.LENGTH_SHORT).show();
                //return;
            }
            recyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
            recyclerView.setHasFixedSize(false);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) {
                        visibleItemCount = linearLayoutManager.getChildCount();
                        totalItemCount = linearLayoutManager.getItemCount();
                        pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                        if (loading) {
                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                                loading = false;
                                new FetchNextPage(getActivity()).execute();
                                Toast.makeText(getActivity(), "Loading!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
            clientOverviewAdapter = new ClientOverviewAdapter(clientOverviewList);
            recyclerView.setAdapter(clientOverviewAdapter);
            if (clientOverviewAdapter.getItemCount() == 0) {
                tv.setVisibility(View.VISIBLE);
            } else tv.setVisibility(View.GONE);
        }
    }

    public class FetchNextPage extends AsyncTask<String, Void, String> {
        Context context;

        FetchNextPage(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            if (nextPageURL.equals("null")) {
                return "all done";
            }
            String result = new Helpdesk().nextPageURL(nextPageURL);
            if (result == null)
                return null;
            String data;
            try {
                JSONObject jsonObject = new JSONObject(result);
                data = jsonObject.getString("data");
                nextPageURL = jsonObject.getString("next_page_url");
                JSONArray jsonArray = new JSONArray(data);
                for (int i = 0; i < jsonArray.length(); i++) {
                    ClientOverview clientOverview = Helper.parseClientOverview(jsonArray, i);
                    if (clientOverview != null)
                        clientOverviewList.add(clientOverview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String result) {
            if (result == null)
                return;
            if (result.equals("all done")) {
                Toast.makeText(context, "All tickets loaded", Toast.LENGTH_SHORT).show();
                return;
            }
            clientOverviewAdapter.notifyDataSetChanged();
            loading = true;
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
        nextPageURL="";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.client4:
                Intent intent = new Intent(getActivity(), ClientDetailActivity.class);
                startActivity(intent);
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
