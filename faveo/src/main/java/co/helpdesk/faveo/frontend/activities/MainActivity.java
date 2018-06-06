package co.helpdesk.faveo.frontend.activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.ButterKnife;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.frontend.drawers.FragmentDrawer;
import co.helpdesk.faveo.frontend.fragments.About;
import co.helpdesk.faveo.frontend.fragments.ClientList;
import co.helpdesk.faveo.frontend.fragments.CreateTicket;
import co.helpdesk.faveo.frontend.fragments.Settings;
import co.helpdesk.faveo.frontend.fragments.tickets.ClosedTickets;
import co.helpdesk.faveo.frontend.fragments.tickets.InboxTickets;
import co.helpdesk.faveo.frontend.fragments.tickets.MyTickets;
import co.helpdesk.faveo.frontend.fragments.tickets.TrashTickets;
import co.helpdesk.faveo.frontend.fragments.tickets.UnassignedTickets;
import co.helpdesk.faveo.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.model.MessageEvent;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,
        ClosedTickets.OnFragmentInteractionListener,
        InboxTickets.OnFragmentInteractionListener,
        MyTickets.OnFragmentInteractionListener,
        TrashTickets.OnFragmentInteractionListener,
        UnassignedTickets.OnFragmentInteractionListener,
        About.OnFragmentInteractionListener,
        ClientList.OnFragmentInteractionListener,
        CreateTicket.OnFragmentInteractionListener,
        Settings.OnFragmentInteractionListener {

    // The BroadcastReceiver that tracks network connectivity changes.
    private InternetReceiver receiver = new InternetReceiver();

    protected boolean doubleBackToExitPressedOnce = false;
    public static boolean isShowing = false;
    private ArrayList<String> mList = new ArrayList<>();
//    @BindView(R.id.sort_view)
//    RelativeLayout sortView;
//    @BindView(R.id.sorting_type_textview)
//    TextView sortTextview;
//    @BindView(R.id.arrow_imgView)
//    ImageView arrowDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isShowing = true;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        // TODO: Move this to where you establish a user session
        //logUser();
//        if (BuildConfig.DEBUG) {
//            DebugDB.getAddressLog();
//            Log.d("Refreshed token: ", "" + FirebaseInstanceId.getInstance().getToken());
//        }

//        String nextPageURL = getIntent().getStringExtra("nextPageURL");
//        Bundle bundle = new Bundle();
//        bundle.putString("nextPageURL", nextPageURL);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



//        getSupportActionBar().setTitle("Inbox");

        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);


        /*
          Loading the inbox fragment here.
         */
        InboxTickets inboxTickets = new InboxTickets();
        //inboxTickets.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, inboxTickets);
        fragmentTransaction.commit();
        setActionBarTitle(getResources().getString(R.string.inbox));



//        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
//        builder1.setTitle("Faveo Pro");
//        builder1.setMessage("Welcome to FAVEO!");
//        builder1.setIcon(R.mipmap.ic_launcher);
//        builder1.setCancelable(true);
//
//        final AlertDialog dlg = builder1.create();
//
//        dlg.show();
//
//        final Timer t = new Timer();
//        t.schedule(new TimerTask() {
//            public void run() {
//                dlg.dismiss(); // when the task active then close the dialog
//                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
//            }
//        }, 2000);

    }



//    private void logUser() {
//        // TODO: Use the current user's information
//        // You can call any combination of these three methods
//        Crashlytics.setUserIdentifier(Preference.getUserID());
//        Crashlytics.setUserEmail(Constants.URL);
//        Crashlytics.setUserName(Preference.getUsername());
//    }

    @Override
    protected void onDestroy() {
        isShowing = false;
        super.onDestroy();

    }

//    @OnClick(R.id.sort_view)
//    public void onClickSort() {
//        arrowDown.animate().rotation(180).start();
//
//        new BottomSheet.Builder(this).title("Sort by").sheet(R.menu.sort_menu).listener(new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case R.id.action_date:
//                        sortTextview.setText("Due by date");
//                        break;
//                    case R.id.action_time:
//                        sortTextview.setText("Due by time");
//                        break;
//                    case R.id.action_status:
//                        sortTextview.setText("Status");
//                        break;
//                    case R.id.action_priority:
//                        sortTextview.setText("Priority");
//                        break;
//                }
//            }
//        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                arrowDown.animate().rotation(0).start();
//            }
//        }).show();
//
//    }

    /**
     * This will handle the drawer item.
     * @param view
     * @param position
     */
    @Override
    public void onDrawerItemSelected(View view, int position) {
    }

    public void setActionBarTitle(final String title) {
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.title);
        mTitle.setText(title.toUpperCase());

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     * @param item items refer to the menu items.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        if (id == R.id.action_search) {
//            startActivity(new Intent(MainActivity.this, SearchActivity.class));
//            return true;
//        }

//        if (id == R.id.action_noti) {
//
//            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
//            startActivity(intent);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * While resuming it will check if the internet
     * is available or not.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        //FaveoApplication.getInstance().setInternetListener(this);
        checkConnection();
    }

    private void checkConnection() {
        boolean isConnected = InternetReceiver.isConnected();
        showSnackIfNoInternet(isConnected);
    }

    /**
     * Display the snackbar if network connection is not there.
     *
     * @param isConnected is a boolean value of network connection.
     */
    private void showSnackIfNoInternet(boolean isConnected) {
        if (!isConnected) {
            final Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), R.string.sry_not_connected_to_internet, Snackbar.LENGTH_INDEFINITE);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.setAction("X", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }

    }

    /**
     * Display the snackbar if network connection is there.
     *
     * @param isConnected is a boolean value of network connection.
     */
    private void showSnack(boolean isConnected) {

        if (isConnected) {
            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), R.string.connected_to_internet, Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } else {
            showSnackIfNoInternet(false);
        }

    }

//    /**
//     * Callback will be triggered when there is change in
//     * network connection
//     */
//    @Override
//    public void onNetworkConnectionChanged(boolean isConnected) {
//        showSnack(isConnected);
//    }

    /**
     * Handling the back button here.
     * As if we clicking twice then it will
     * ask press one more time to exit,we are handling
     * the double back button pressing here.
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(findViewById(android.R.id.content), R.string.press_again_exit, Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2500);
    }

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        //Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
//        Snackbar.make(findViewById(android.R.id.content), event.message, Snackbar.LENGTH_LONG).show();
        showSnack(event.message);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
