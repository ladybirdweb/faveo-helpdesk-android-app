//package co.helpdesk.faveo.frontend.activities;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.design.widget.Snackbar;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
//import com.muddzdev.styleabletoastlibrary.StyleableToast;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import co.helpdesk.faveo.Helper;
//import co.helpdesk.faveo.R;
//import co.helpdesk.faveo.backend.api.v1.Helpdesk;
//import co.helpdesk.faveo.frontend.adapters.NotificationAdapter;
//import co.helpdesk.faveo.frontend.receivers.InternetReceiver;
//import co.helpdesk.faveo.model.MessageEvent;
//import co.helpdesk.faveo.model.NotificationThread;
//import es.dmoral.toasty.Toasty;
//
///**
// * This activity is for getting the notification.We have used recycler view for showing the
// * notification to the user.We have used swipe refresh layout here,so when ever we are going to scroll down
// * we will make call to fetch first async task.
// */
//public class NotificationActivity extends AppCompatActivity {
//
//    @BindView(R.id.swipeRefresh)
//    SwipeRefreshLayout swipeRefresh;
//
//    @BindView(R.id.recycler_view)
//    ShimmerRecyclerView recyclerView;
//
//    @BindView(R.id.empty_view)
//    TextView empty_view;
//
//    @BindView(R.id.noiternet_view)
//    TextView noInternet_view;
//    @BindView(R.id.totalcount)
//    TextView textView;
//
//    static String nextPageURL = "";
//    ProgressDialog progressDialog;
//
//    NotificationAdapter notificationAdapter;
//    List<NotificationThread> notiThreadList = new ArrayList<>();
//
//    private boolean loading = true;
//    int total;
//    int pastVisibleItems, visibleItemCount, totalItemCount;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_notification);
//        ButterKnife.bind(this);
//        swipeRefresh.setColorSchemeResources(R.color.faveo_blue);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        progressDialog=new ProgressDialog(NotificationActivity.this);
//        progressDialog.setMessage("Please wait");
//        if (InternetReceiver.isConnected()) {
//            noInternet_view.setVisibility(View.GONE);
//            // swipeRefresh.setRefreshing(true);
//            progressDialog.show();
//            new FetchFirst(this).execute();
//        } else {
//            noInternet_view.setVisibility(View.VISIBLE);
//            recyclerView.setVisibility(View.INVISIBLE);
//            empty_view.setVisibility(View.GONE);
//        }
//
//        /*
//         * Handling the refresh layout listener here.
//         */
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (InternetReceiver.isConnected()) {
//                    loading = true;
//                    recyclerView.setVisibility(View.VISIBLE);
//                    noInternet_view.setVisibility(View.GONE);
//                    new FetchFirst(NotificationActivity.this).execute();
//                } else {
//                    recyclerView.setVisibility(View.INVISIBLE);
//                    swipeRefresh.setRefreshing(false);
//                    empty_view.setVisibility(View.GONE);
//                    noInternet_view.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//    }
//
////    private void setupRecycler() {
////        // use this setting to improve performance if you know that changes
////        // in content do not change the layout size of the RecyclerView
////        recycler.setHasFixedSize(true);
////
////        // use a linear layout manager since the cards are vertically scrollable
////        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
////        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
////        recycler.setLayoutManager(layoutManager);
////
////        // create an empty adapter and add it to the recycler view
////        adapter = new NotificationAdapter(this);
////        recycler.setAdapter(adapter);
////    }
//
//    /**
//     * This async task is for getting the notification details .
//     */
//    private class FetchFirst extends AsyncTask<String, Void, String> {
//        Context context;
//
//        FetchFirst(Context context) {
//            this.context = context;
//        }
//
//        protected String doInBackground(String... urls) {
////            if (nextPageURL.equals("null")) {
////                return "all done";
////            }
//            String result = new Helpdesk().getNotifications();
//            if (result == null)
//                return null;
//            String data;
//            notiThreadList.clear();
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                total=jsonObject.getInt("total");
//
//                try {
//                    data = jsonObject.getString("data");
//                    nextPageURL = jsonObject.getString("next_page_url");
//                } catch (JSONException e) {
//                    data = jsonObject.getString("result");
//                }
//                JSONArray jsonArray = new JSONArray(data);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    NotificationThread notificationThread = Helper.parseNotifications(jsonArray, i);
//                    if (notificationThread != null)
//                        notiThreadList.add(notificationThread);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            return "success";
//        }
//
//        protected void onPostExecute(String result) {
//            progressDialog.dismiss();
//            //textView.setVisibility(View.GONE);
//            textView.setText(""+total+" notifications");
//            if (swipeRefresh.isRefreshing())
//                swipeRefresh.setRefreshing(false);
//
//            if (result == null) {
//                Toasty.error(NotificationActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                return;
//            }
//
//            if (result.equals("all done")) {
//
//                Toasty.info(context, getString(R.string.all_caught_up), Toast.LENGTH_SHORT).show();
//                //return;
//            }
//            recyclerView.setHasFixedSize(false);
//            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NotificationActivity.this);
//            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//            recyclerView.setLayoutManager(linearLayoutManager);
//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    if (dy > 0) {
//                        visibleItemCount = linearLayoutManager.getChildCount();
//                        totalItemCount = linearLayoutManager.getItemCount();
//                        pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
//                        if (loading) {
//                            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
//                                loading = false;
//                                new FetchNextPage(NotificationActivity.this).execute();
//                                StyleableToast st = new StyleableToast(NotificationActivity.this, getString(R.string.loading), Toast.LENGTH_SHORT);
//                                st.setBackgroundColor(Color.parseColor("#3da6d7"));
//                                st.setTextColor(Color.WHITE);
//                                st.setIcon(R.drawable.ic_autorenew_black_24dp);
//                                st.spinIcon();
//                                st.setMaxAlpha();
//                                st.show();
//                                //Toast.makeText(getActivity(), "Loading!", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                }
//            });
//
//            notificationAdapter = new NotificationAdapter(notiThreadList);
//            recyclerView.setAdapter(notificationAdapter);
//            if (notificationAdapter.getItemCount() == 0) {
//                empty_view.setVisibility(View.VISIBLE);
//            } else empty_view.setVisibility(View.GONE);
//        }
//    }
//
//    /**
//     * This async task is for getting the next page url.
//     */
//    private class FetchNextPage extends AsyncTask<String, Void, String> {
//        Context context;
//
//        FetchNextPage(Context context) {
//            this.context = context;
//        }
//
//        protected String doInBackground(String... urls) {
//            if (nextPageURL.equals("null")) {
//                return "all done";
//            }
//            String result = new Helpdesk().nextPageURL(nextPageURL);
//            if (result == null)
//                return null;
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                nextPageURL = jsonObject.getString("next_page_url");
//                String data = jsonObject.getString("data");
//                JSONArray jsonArray = new JSONArray(data);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    NotificationThread notificationThread = Helper.parseNotifications(jsonArray, i);
//                    if (notificationThread != null)
//                        notiThreadList.add(notificationThread);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return "success";
//        }
//
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                Toasty.error(NotificationActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
//                return;
//            }
//            if (result.equals("all done")) {
//                Toasty.info(context, getString(R.string.all_caught_up), Toast.LENGTH_SHORT).show();
//                return;
//            }
//            notificationAdapter.notifyDataSetChanged();
//            loading = true;
//        }
//    }
////    public void setRealmAdapter(RealmResults<NotificationThread> tickets) {
////
////        RealmTicketAdapter realmAdapter = new RealmTicketAdapter(this.getApplicationContext(), tickets, true);
////        // Set the data and tell the RecyclerView to draw
////        adapter.setRealmAdapter(realmAdapter);
////        adapter.notifyDataSetChanged();
////    }
//
//    /**
//     * Handling the menu items here.
//     * @param item
//     * @return
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                //Write your logic here
//                finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    /**
//     * While resuming it will check if the internet
//     * is available or not.
//     */
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // register connection status listener
//        //FaveoApplication.getInstance().setInternetListener(this);
//        checkConnection();
//    }
//
//    private void checkConnection() {
//        boolean isConnected = InternetReceiver.isConnected();
//        showSnackIfNoInternet(isConnected);
//    }
//
//    /**
//     * Display the snackbar if network connection is not there.
//     *
//     * @param isConnected is a boolean value of network connection.
//     */
//    private void showSnackIfNoInternet(boolean isConnected) {
//        if (!isConnected) {
//            final Snackbar snackbar = Snackbar
//                    .make(findViewById(android.R.id.content), R.string.sry_not_connected_to_internet, Snackbar.LENGTH_INDEFINITE);
//
//            View sbView = snackbar.getView();
//            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//            textView.setTextColor(Color.RED);
//            snackbar.setAction("X", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    snackbar.dismiss();
//                }
//            });
//            snackbar.show();
//        }
//
//    }
//
//    /**
//     * Display the snackbar if network connection is there.
//     *
//     * @param isConnected is a boolean value of network connection.
//     */
//    private void showSnack(boolean isConnected) {
//
//        if (isConnected) {
//            Snackbar snackbar = Snackbar
//                    .make(findViewById(android.R.id.content), R.string.connected_to_internet, Snackbar.LENGTH_LONG);
//
//            View sbView = snackbar.getView();
//            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//            textView.setTextColor(Color.WHITE);
//            snackbar.show();
//        } else {
//            showSnackIfNoInternet(false);
//        }
//
//    }
//
//    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageEvent event) {
//        //Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
////        Snackbar.make(findViewById(android.R.id.content), event.message, Snackbar.LENGTH_LONG).show();
//        showSnack(event.message);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // mListener = null;
//        progressDialog.dismiss();
//        nextPageURL = "";
//    }
//    @Override
//    public void onBackPressed() {
//        // your code.
//        progressDialog.setMessage("Please wait");
//        progressDialog.show();
//        Intent intent=new Intent(NotificationActivity.this,MainActivity.class);
////        progressDialog.dismiss();
//        startActivity(intent);
//    }
//
//
//}
