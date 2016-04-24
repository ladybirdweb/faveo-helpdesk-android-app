package co.helpdesk.faveo.frontend.activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void onDrawerItemSelected(View view, int position) {
    }

    public void setActionBarTitle(final String title){
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.title);
        mTitle.setText(title.toUpperCase());

        final View mCreateTicket = toolbarTop.findViewById(R.id.button_create_ticket);
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
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.create_ticket));
                if (fragment == null)
                    fragment = new CreateTicket();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment, title);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
