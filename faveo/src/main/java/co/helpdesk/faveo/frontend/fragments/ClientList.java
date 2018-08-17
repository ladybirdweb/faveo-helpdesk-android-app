package co.helpdesk.faveo.frontend.fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.Helper;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.activities.ClientDetailActivity;
import co.helpdesk.faveo.frontend.activities.MainActivity;
import co.helpdesk.faveo.frontend.adapters.ClientOverviewAdapter;
import co.helpdesk.faveo.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.model.ClientOverview;
import es.dmoral.toasty.Toasty;

public class ClientList extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    static String nextPageURL = "";

    @BindView(R.id.cardList)
    ShimmerRecyclerView recyclerView;

    @BindView(R.id.empty_view)
    TextView empty_view;
    @BindView(R.id.noiternet_view)
    TextView noInternet_view;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    ClientOverviewAdapter clientOverviewAdapter;
    List<ClientOverview> clientOverviewList = new ArrayList<>();
    View rootView;

    @BindView(R.id.totalcount)
    TextView textViewTotalCount;
//    @BindView(R.id.totalcount)
//    TextView textView;
    int count;


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
            rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
            ButterKnife.bind(this, rootView);
            swipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
            swipeRefresh.setColorSchemeResources(R.color.faveo_blue);
            if (InternetReceiver.isConnected()) {
                noInternet_view.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(true);
                new FetchClients(getActivity()).execute();
            } else {
                noInternet_view.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                empty_view.setVisibility(View.GONE);
            }

            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (InternetReceiver.isConnected()) {
                        loading = true;
                        recyclerView.setVisibility(View.VISIBLE);
                        noInternet_view.setVisibility(View.GONE);
                        new FetchClients(getActivity()).execute();
                    } else {
                        recyclerView.setVisibility(View.INVISIBLE);
                        swipeRefresh.setRefreshing(false);
                        empty_view.setVisibility(View.GONE);
                        noInternet_view.setVisibility(View.VISIBLE);
                    }
                }
            });

            empty_view.setText(R.string.no_clients);
        }
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.client_list));
        return rootView;
    }

    private class FetchClients extends AsyncTask<String, Void, String> {
        Context context;

        FetchClients(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            String result = new Helpdesk().getCustomersOverview();
            if (result == null)
                return null;
            String data;
            clientOverviewList.clear();
            try {
                JSONObject jsonObject = new JSONObject(result);
                count=jsonObject.getInt("total");

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
            textViewTotalCount.setText("" + count + " clients");
            swipeRefresh.setRefreshing(false);
            if (swipeRefresh.isRefreshing())
                swipeRefresh.setRefreshing(false);

            if (result == null) {
                Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            if (result.equals("all done")) {

                Toasty.info(context, getString(R.string.all_clients_loaded), Toast.LENGTH_SHORT).show();
                //return;
            }
            // recyclerView = (ShimmerRecyclerView) rootView.findViewById(R.id.cardList);
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
                                //Toast.makeText(getActivity(), "Loading!", Toast.LENGTH_SHORT).show();

                                StyleableToast st = new StyleableToast(getContext(), getString(R.string.loading), Toast.LENGTH_SHORT);
                                st.setBackgroundColor(Color.parseColor("#3da6d7"));
                                st.setTextColor(Color.WHITE);
                                st.setIcon(R.drawable.ic_autorenew_black_24dp);
                                st.spinIcon();
                                st.setMaxAlpha();
                                st.show();
                            }
                        }
                    }
                }
            });
            clientOverviewAdapter = new ClientOverviewAdapter(getContext(),clientOverviewList);
            recyclerView.setAdapter(clientOverviewAdapter);
            if (clientOverviewAdapter.getItemCount() == 0) {
                empty_view.setVisibility(View.VISIBLE);
            } else empty_view.setVisibility(View.GONE);
        }
    }

    private class FetchNextPage extends AsyncTask<String, Void, String> {
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
                Toasty.info(context, getString(R.string.all_clients_loaded), Toast.LENGTH_SHORT).show();
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
    /**
     * When the fragment is going to be attached
     * this life cycle method is going to be called.
     * @param context refers to the current fragment.
     */
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
    /**
     * Once the fragment is going to be detached then
     * this method is going to be called.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        nextPageURL = "";
    }

    /**
     * Here we are handling the click event .
     * @param v is the view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.client4:
                Intent intent = new Intent(getActivity(), ClientDetailActivity.class);
                View sharedView = v.findViewById(R.id.imageView_default_profile);
                String transitionName = getString(R.string.blue_name);

                ActivityOptions transitionActivityOptions = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), sharedView, transitionName);
                    startActivity(intent, transitionActivityOptions.toBundle());
                } else startActivity(intent);

                break;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
