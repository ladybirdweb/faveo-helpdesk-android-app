package com.android.faveo.frontend.drawers;
/**
 * Created by Sumit
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.faveo.R;
import com.android.faveo.frontend.activities.LoginActivity;
import com.android.faveo.frontend.activities.MainActivity;
import com.android.faveo.frontend.fragments.About;
import com.android.faveo.frontend.fragments.ClientList;
import com.android.faveo.frontend.fragments.CreateTicket;
import com.android.faveo.frontend.fragments.Settings;
import com.android.faveo.frontend.fragments.tickets.ClosedTickets;
import com.android.faveo.frontend.fragments.tickets.InboxTickets;
import com.android.faveo.frontend.fragments.tickets.MyTickets;
import com.android.faveo.frontend.fragments.tickets.TrashTickets;
import com.android.faveo.frontend.fragments.tickets.UnassignedTickets;

import java.util.ArrayList;
import java.util.List;

public class FragmentDrawer extends Fragment implements View.OnClickListener  {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    View layout;
    Context context;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();

        for (String title : titles) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(title);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titles = (new String[]{"Item1", "Item2", "Item3", "Item4"});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        layout.findViewById(R.id.create_ticket).setOnClickListener(this);
        layout.findViewById(R.id.inbox_tickets).setOnClickListener(this);
        layout.findViewById(R.id.my_tickets).setOnClickListener(this);
        layout.findViewById(R.id.unassigned_tickets).setOnClickListener(this);
        layout.findViewById(R.id.closed_tickets).setOnClickListener(this);
        layout.findViewById(R.id.trash_tickets).setOnClickListener(this);
        layout.findViewById(R.id.client_list).setOnClickListener(this);
        layout.findViewById(R.id.settings).setOnClickListener(this);
        layout.findViewById(R.id.about).setOnClickListener(this);
        layout.findViewById(R.id.logout).setOnClickListener(this);
        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (v.getId()) {
            case R.id.create_ticket:
                fragment = new CreateTicket();
                title = getString(R.string.create_ticket);
                break;
            case R.id.inbox_tickets:
                fragment = new InboxTickets();
                title = getString(R.string.inbox_tickets);
                break;
            case R.id.my_tickets:
                fragment = new MyTickets();
                title = getString(R.string.my_tickets);
                break;
            case R.id.unassigned_tickets:
                fragment = new UnassignedTickets();
                title = getString(R.string.unassigned_tickets);
                break;
            case R.id.closed_tickets:
                fragment = new ClosedTickets();
                title = getString(R.string.closed_tickets);
                break;
            case R.id.trash_tickets:
                fragment = new TrashTickets();
                title = getString(R.string.trash_tickets);
                break;
            case R.id.client_list:
                fragment = new ClientList();
                title = getString(R.string.client_list);
                break;
            case R.id.settings:
                fragment = new Settings();
                title = getString(R.string.settings);
                break;
            case R.id.about:
                fragment = new About();
                title = getString(R.string.about);
                break;
            case R.id.logout:
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("FAVEO", getActivity().MODE_PRIVATE).edit();
                editor.putBoolean("LOGIN_COMPLETE", false);
                editor.apply();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            ((MainActivity) getActivity()).setActionBarTitle(title);
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

}

