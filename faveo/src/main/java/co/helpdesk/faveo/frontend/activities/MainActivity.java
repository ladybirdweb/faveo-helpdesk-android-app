package co.helpdesk.faveo.frontend.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import co.helpdesk.faveo.FaveoApplication;
import co.helpdesk.faveo.Preference;
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
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,
        ClosedTickets.OnFragmentInteractionListener,
        InboxTickets.OnFragmentInteractionListener,
        MyTickets.OnFragmentInteractionListener,
        TrashTickets.OnFragmentInteractionListener,
        UnassignedTickets.OnFragmentInteractionListener,
        About.OnFragmentInteractionListener,
        ClientList.OnFragmentInteractionListener,
        CreateTicket.OnFragmentInteractionListener,
        Settings.OnFragmentInteractionListener,
        InternetReceiver.InternetReceiverListener {

    protected boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Move this to where you establish a user session
        logUser();

        setContentView(R.layout.activity_main);

        String nextPageURL = getIntent().getStringExtra("nextPageURL");
        Bundle bundle = new Bundle();
        bundle.putString("nextPageURL", nextPageURL);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        InboxTickets inboxTickets = new InboxTickets();
        inboxTickets.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, inboxTickets);
        fragmentTransaction.commit();
        setActionBarTitle(getResources().getString(R.string.inbox_tickets));

        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("FAVEO", MODE_PRIVATE).edit();
        editor.putBoolean("LOGIN_COMPLETE", true);
        editor.apply();

    }

    private void logUser() {
        // TODO: Use the current user's information
        // You can call any combination of these three methods
        if (Preference.getUserID() != null) {
            Crashlytics.setUserIdentifier(Preference.getUserID());
            Log.d("user id", Preference.getUserID());
        }
//        Crashlytics.setUserEmail("user@fabric.io");
//        Crashlytics.setUserName("Test User");
    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
    }

    public void setActionBarTitle(final String title) {
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.title);
        mTitle.setText(title.toUpperCase());

        View mCreateTicket = toolbarTop.findViewById(R.id.button_create_ticket);

        switch (title) {
            case "Inbox":
            case "My tickets":
            case "Unassigned tickets":
            case "Closed tickets":
            case "Trash":
                mCreateTicket.setVisibility(View.VISIBLE);
                break;
            default:
                mCreateTicket.setVisibility(View.GONE);
                break;
        }
        mCreateTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.create_ticket));
                // if (fragment == null)
                Fragment fragment = new CreateTicket();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        FaveoApplication.getInstance().setInternetListener(this);
        checkConnection();
    }

    private void checkConnection() {
        boolean isConnected = InternetReceiver.isConnected();
        showSnackIfNoInternet(isConnected);
    }

    private void showSnackIfNoInternet(boolean isConnected) {
        if (!isConnected) {
            final Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "Sorry! Not connected to internet", Snackbar.LENGTH_INDEFINITE);

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

    private void showSnack(boolean isConnected) {

        if (isConnected) {

            Snackbar snackbar = Snackbar
                    .make(findViewById(android.R.id.content), "Connected to Internet", Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } else {
            showSnackIfNoInternet(false);
        }

    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(findViewById(android.R.id.content), "Press again to EXIT!", Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2500);
    }

//    @Override
//    public void onBackPressed() {
//        Fragment fragment = this.getSupportFragmentManager().findFragmentByTag(getString(R.string.inbox_tickets));
//        if (fragment == null) {
//            fragment = new InboxTickets();
//        }
//        FragmentManager fragmentManager = this.getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container_body, fragment);
//        // fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//        this.setActionBarTitle(getString(R.string.inbox_tickets));
//
//       // super.onBackPressed();
//    }
}
