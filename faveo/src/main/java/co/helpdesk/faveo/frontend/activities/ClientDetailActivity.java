package co.helpdesk.faveo.frontend.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.fragments.client.ClosedTickets;
import co.helpdesk.faveo.frontend.fragments.client.OpenTickets;
import co.helpdesk.faveo.model.TicketGlimpse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ClientDetailActivity extends AppCompatActivity implements
        OpenTickets.OnFragmentInteractionListener,
        ClosedTickets.OnFragmentInteractionListener {

    ImageView imageViewClientPicture;
    TextView textViewClientName, textViewClientEmail, textViewClientPhone, textViewClientStatus;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    OpenTickets fragmentOpenTickets;
    ClosedTickets fragmentClosedTickets;
    String clientID;
    List<TicketGlimpse> listTicketGlimpse;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.title);
        mTitle.setText("PROFILE");

        setUpViews();
        Intent intent = getIntent();
        clientID = intent.getStringExtra("CLIENT_ID");
        textViewClientName.setText(intent.getStringExtra("CLIENT_NAME"));
        textViewClientEmail.setText(intent.getStringExtra("CLIENT_EMAIL"));
        textViewClientPhone.setText(intent.getStringExtra("CLIENT_PHONE"));
        String clientPictureUrl = intent.getStringExtra("CLIENT_PICTURE");
        textViewClientStatus.setText("ACTIVE");

        if (clientPictureUrl != null && clientPictureUrl.trim().length() != 0)
            Picasso.with(this)
                    .load(clientPictureUrl)
                    .placeholder(R.drawable.default_pic)
                    .error(R.drawable.default_pic)
                    .into(imageViewClientPicture);

        progressDialog.show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new FetchClientTickets(ClientDetailActivity.this).execute();
            }
        }, 3000);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(onTabSelectedListener);

    }

    public class FetchClientTickets extends AsyncTask<String, Void, String> {
        Context context;
        List<TicketGlimpse> listOpenTicketGlimpse = new ArrayList<>();
        List<TicketGlimpse> listClosedTicketGlimpse = new ArrayList<>();

        public FetchClientTickets(Context context) {
            this.context = context;
        }

        protected String doInBackground(String... urls) {
            listTicketGlimpse = new ArrayList<>();
            String result = new Helpdesk().getMyTickets(clientID);
            if (result == null)
                return null;
            try {
                JSONObject jsonObject = new JSONObject(result);
                String data = jsonObject.getString("result");
                JSONArray jsonArray = new JSONArray(data);
                for(int i = 0; i < 10; i++) {
                    int ticketID = Integer.parseInt(jsonArray.getJSONObject(i).getString("id"));
                    boolean isOpen = true;
                    String ticketSubject = jsonArray.getJSONObject(i).getString("title");
                    try {
                        isOpen = jsonArray.getJSONObject(i).getString("ticket_status_name").equals("Open");
                        if (isOpen)
                            listOpenTicketGlimpse.add(new TicketGlimpse(ticketID, ticketSubject, true));
                        else
                            listClosedTicketGlimpse.add(new TicketGlimpse(ticketID, ticketSubject, false));
                    } catch (Exception e) {
                        listOpenTicketGlimpse.add(new TicketGlimpse(ticketID, ticketSubject, true));
                    }
                    listTicketGlimpse.add(new TicketGlimpse(ticketID, ticketSubject, isOpen));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "success";
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result == null) {
                Toast.makeText(ClientDetailActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                return;
            }
            fragmentOpenTickets.populateData(listOpenTicketGlimpse);
            fragmentClosedTickets.populateData(listClosedTicketGlimpse);
        }
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentOpenTickets = new OpenTickets();
        fragmentClosedTickets = new ClosedTickets();
        adapter.addFragment(fragmentOpenTickets, "OPEN TICKET");
        adapter.addFragment(fragmentClosedTickets, "CLOSED TICKET");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition(), true);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void setUpViews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching details");
        imageViewClientPicture = (ImageView) findViewById(R.id.imageView_default_profile);
        textViewClientName = (TextView) findViewById(R.id.textView_client_name);
        textViewClientEmail = (TextView) findViewById(R.id.textView_client_email);
        textViewClientPhone = (TextView) findViewById(R.id.textView_client_phone);
        textViewClientStatus = (TextView) findViewById(R.id.textView_client_status);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
    }

}
