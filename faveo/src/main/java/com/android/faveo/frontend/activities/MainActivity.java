package com.android.faveo.frontend.activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.faveo.R;
import com.android.faveo.frontend.drawers.FragmentDrawer;
import com.android.faveo.frontend.fragments.About;
import com.android.faveo.frontend.fragments.ClientList;
import com.android.faveo.frontend.fragments.CreateTicket;
import com.android.faveo.frontend.fragments.Settings;
import com.android.faveo.frontend.fragments.tickets.ClosedTickets;
import com.android.faveo.frontend.fragments.tickets.InboxTickets;
import com.android.faveo.frontend.fragments.tickets.MyTickets;
import com.android.faveo.frontend.fragments.tickets.TrashTickets;
import com.android.faveo.frontend.fragments.tickets.UnassignedTickets;


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
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        InboxTickets inboxTickets = new InboxTickets();
        inboxTickets.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_body, inboxTickets);
        fragmentTransaction.commit();
        getSupportActionBar().setTitle(R.string.inbox_tickets);

        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("FAVEO", MODE_PRIVATE).edit();
        editor.putBoolean("LOGIN_COMPLETE", true);
        editor.apply();

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
