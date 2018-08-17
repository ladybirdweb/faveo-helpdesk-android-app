package co.helpdesk.faveo.frontend.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;
import com.pixplicity.easyprefs.library.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import co.helpdesk.faveo.Constants;
import co.helpdesk.faveo.Fab;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.fragments.ticketDetail.Conversation;
import co.helpdesk.faveo.frontend.fragments.ticketDetail.Detail;
import co.helpdesk.faveo.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.model.MessageEvent;
import es.dmoral.toasty.Toasty;
import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;


public class TicketDetailActivity extends AppCompatActivity implements
        Conversation.OnFragmentInteractionListener,
        Detail.OnFragmentInteractionListener {

    ViewPager viewPager;
    ViewPagerAdapter adapter;
    Conversation fragmentConversation;
    Detail fragmentDetail;
    Boolean fabExpanded = false;
    FloatingActionButton fabAdd;
    int cx, cy;
    Fab fab;
    private MaterialSheetFab materialSheetFab;
    View overlay;
    EditText editTextInternalNote, editTextCC, editTextReplyMessage;
    Button buttonCreate, buttonSend;
    ProgressDialog progressDialog;
    private int statusBarColor;
    ImageView imgaeviewBack;
    public static String ticketID;
    public static boolean isShowing = false;
    FabSpeedDial fabSpeedDial;
    private boolean isFabOpen;
    View view;
    View viewpriority,viewCollapsePriority;
//    public static String ticketNumber;
//    public static String ticketOpenedBy;
//    public static String ticketSubject;
    LoaderTextView textViewStatus, textviewAgentName, textViewTitle, textViewSubject,textViewDepartment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        overridePendingTransition(R.anim.slide_in_from_right,R.anim.slide_in_from_right);
        Window window = TicketDetailActivity.this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(TicketDetailActivity.this,R.color.faveo));
        //setupFab();
        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbarTicketDetail);
        textViewStatus = (LoaderTextView) mAppBarLayout.findViewById(R.id.status);
        textviewAgentName = (LoaderTextView) mAppBarLayout.findViewById(R.id.textViewagentName);
        textViewTitle = (LoaderTextView) mAppBarLayout.findViewById(R.id.title);
        textViewSubject = (LoaderTextView) mToolbar.findViewById(R.id.subject);
        viewpriority=mToolbar.findViewById(R.id.viewPriority);
        textViewDepartment= (LoaderTextView) mAppBarLayout.findViewById(R.id.department);
        viewCollapsePriority=mAppBarLayout.findViewById(R.id.viewPriority1);
        fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);
        setSupportActionBar(mToolbar);
        isShowing=true;
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
        Constants.URL = Prefs.getString("COMPANY_URL", "");

        ticketID =Prefs.getString("TICKETid",null);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(onTabSelectedListener);
        imgaeviewBack= (ImageView) findViewById(R.id.imageViewBackTicketDetail);
        progressDialog = new ProgressDialog(this);
        editTextInternalNote = (EditText) findViewById(R.id.editText_internal_note);
        //editTextCC = (EditText) findViewById(R.id.editText_cc);
        editTextReplyMessage = (EditText) findViewById(R.id.editText_reply_message);
        buttonCreate = (Button) findViewById(R.id.button_create);
        buttonSend = (Button) findViewById(R.id.button_send);
        new Thread(new Runnable() {
            public void run(){
                Log.d("threadisrunning","true");
                new FetchTicketDetail(Prefs.getString("TICKETid",null)).execute();
            }
        }).start();

        imgaeviewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id=menuItem.getItemId();
                if (id==R.id.fab_reply){
                    Intent intent=new Intent(TicketDetailActivity.this,TicketReplyActivity.class);
                    intent.putExtra("ticket_id", ticketID);
                    startActivity(intent);
                }
                else if (id==R.id.fab_internalnote){
                    Intent intent=new Intent(TicketDetailActivity.this,InternalNoteActivity.class);
                    intent.putExtra("ticket_id", ticketID);
                    startActivity(intent);
                }
                //TODO: Start some activity
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        isShowing = false;
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_new, menu);
        return true;
    }

    /**
     * Handling the back button.
     *
     * @param item refers to the menu item .
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }
        if (item.getItemId()==R.id.buttonsave){
            Intent intent=new Intent(TicketDetailActivity.this,TicketSaveActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Here we are initializing the view pager
     * for the conversation and detail fragment.
     */
    private void setupViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentConversation = new Conversation();
        fragmentDetail = new Detail();
        adapter.addFragment(fragmentConversation, getString(R.string.conversation));
        adapter.addFragment(fragmentDetail, getString(R.string.detail));
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
            fabSpeedDial.setRotation(positionOffset * 180.0f);
        }

        /**
         * This method is for controlling the FAB button.
         * @param position of the View pager page.
         */
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    fabSpeedDial.show();
                    break;

                default:
                    fabSpeedDial.hide();
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
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

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    /**
     * Handling the back button here.
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * While resuming it will check if the internet
     * is available or not.
     */
    @Override
    protected void onResume() {
        super.onResume();
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

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

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


    private class FetchTicketDetail extends AsyncTask<String, Void, String> {
        String ticketID;
        String agentName;
        String title;
        FetchTicketDetail(String ticketID) {

            this.ticketID = ticketID;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().getTicketDetail(ticketID);
        }

        protected void onPostExecute(String result) {
            if (isCancelled()) return;

            if (result == null) {
                return;
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                String ticketNumber=jsonObject1.getString("ticket_number");
                String statusName=jsonObject1.getString("status_name");
                String subject=jsonObject1.getString("title");
                String department=jsonObject1.getString("dept_name");
                String assigneeName=jsonObject1.getString("assignee");
                String first_name,last_name;
                String prioritycolor=jsonObject1.getString("priority_color");

                if (!prioritycolor.equals("")||!prioritycolor.equals("null")){
                    viewpriority.setBackgroundColor(Color.parseColor(prioritycolor));
                    viewCollapsePriority.setBackgroundColor(Color.parseColor(prioritycolor));
                }
                else{
                    viewpriority.setVisibility(View.GONE);
                    viewCollapsePriority.setVisibility(View.GONE);
                }
                //first_name=jsonObject1.getString("assignee_first_name");
                //last_name=jsonObject1.getString("assignee_last_name");

                if (!assigneeName.equals("null")){
                    textviewAgentName.setText(assigneeName);
                }
                else if (assigneeName.equals("null")||assigneeName.equals(null)||assigneeName.equals("")){
                    assigneeName=getString(R.string.unassigned);
                    textviewAgentName.setText(assigneeName);
                }
                else{
                    assigneeName=getString(R.string.unassigned);
                    textviewAgentName.setText(assigneeName);
                }
                if (!statusName.equals("null")||!statusName.equals("")){
                    textViewStatus.setText(statusName);
                }
                else{
                    textViewStatus.setVisibility(View.GONE);
                }
                textViewTitle.setText(ticketNumber);
                if (subject.startsWith("=?")){
                    title=subject.replaceAll("=?UTF-8?Q?","");
                    String newTitle=title.replaceAll("=E2=80=99","");
                    String finalTitle=newTitle.replace("=??Q?","");
                    String newTitle1=finalTitle.replace("?=","");
                    String newTitle2=newTitle1.replace("_"," ");
                    Log.d("new name",newTitle2);
                    textViewSubject.setText(newTitle2);
                }
                else if (!subject.equals("null")){
                    textViewSubject.setText(subject);
                }
                else if (subject.equals("null")){
                    textViewSubject.setText("");
                }

                if (!department.equals("")||!department.equals("null")){
                    textViewDepartment.setText(department);
                }
                else{
                    textViewDepartment.setVisibility(View.GONE);
                }



                Log.d("TITLE",subject);
                Log.d("TICKETNUMBER",ticketNumber);
                } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}