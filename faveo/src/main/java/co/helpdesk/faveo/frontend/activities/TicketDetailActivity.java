package co.helpdesk.faveo.frontend.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
//    public static String ticketNumber;
//    public static String ticketOpenedBy;
//    public static String ticketSubject;
    TextView textViewStatus, textviewAgentName, textViewTitle, textViewSubject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ticket_detail);
        setupFab();
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbarTicketDetail);
        textViewStatus = (TextView) mToolbar.findViewById(R.id.status);
        textviewAgentName = (TextView) mToolbar.findViewById(R.id.textViewagentName);
        textViewTitle = (TextView) mToolbar.findViewById(R.id.title);
        textViewSubject = (TextView) mToolbar.findViewById(R.id.subject);
        setSupportActionBar(mToolbar);
        isShowing=true;
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setHomeButtonEnabled(true);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
        Constants.URL = Prefs.getString("COMPANY_URL", "");

        ticketID = ticketID=Prefs.getString("TICKETid",null);
        //ticketNumber = getIntent().getStringExtra("ticket_number");
        // ticketOpenedBy = getIntent().getStringExtra("ticket_opened_by");
        //ticketSubject = getIntent().getStringExtra("ticket_subject");
//         TextView mTitle = (TextView) mToolbar.findViewById(R.id.title);
//        mTitle.setText(ticketNumber == null ? "Unknown" : ticketNumber);
        //getSupportActionBar().setTitle(ticketNumber == null ? "NotificationThread" : ticketNumber);
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
                onBackPressed();
            }
        });

        /*
          This button is for creating the internal note.
         */
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = editTextInternalNote.getText().toString();
                if (note.trim().length() == 0) {
                    Toasty.warning(TicketDetailActivity.this, getString(R.string.msg_must_not_be_empty), Toast.LENGTH_LONG).show();
                    return;
                }
                String userID = Prefs.getString("ID", null);
                if (userID != null && userID.length() != 0) {
                    try {
                        note = URLEncoder.encode(note, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    new CreateInternalNote(Integer.parseInt(ticketID), Integer.parseInt(userID), note).execute();
                    progressDialog.setMessage(getString(R.string.creating_note));
                    progressDialog.show();


                } else
                    Toasty.warning(TicketDetailActivity.this, getString(R.string.wrong_user_id), Toast.LENGTH_LONG).show();
            }
        });

        /*
          This button is for getting the cc from the reply option
          here we are handling multiple cc items.
         */
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String cc = editTextCC.getText().toString();
                String replyMessage = editTextReplyMessage.getText().toString();
                if (replyMessage.trim().length() == 0) {
                    Toasty.warning(TicketDetailActivity.this, getString(R.string.msg_must_not_be_empty), Toast.LENGTH_LONG).show();
                    return;
                }

//                cc = cc.replace(", ", ",");
//                if (cc.length() > 0) {
//                    String[] multipleEmails = cc.split(",");
//                    for (String email : multipleEmails) {
//                        if (email.length() > 0 && !Helper.isValidEmail(email)) {
//                            Toasty.warning(TicketDetailActivity.this, getString(R.string.invalid_cc), Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                    }
//                }

                String userID = Prefs.getString("ID", null);
                if (userID != null && userID.length() != 0) {
                    try {
                        replyMessage = URLEncoder.encode(replyMessage, "utf-8");
                        Log.d("Msg", replyMessage);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    new ReplyTicket(Integer.parseInt(ticketID), replyMessage).execute();
                    progressDialog.setMessage(getString(R.string.sending_msg));
                    progressDialog.show();

                } else
                    Toasty.warning(TicketDetailActivity.this, getString(R.string.wrong_user_id), Toast.LENGTH_LONG).show();
            }
        });

//        fabAdd = (FloatingActionButton) findViewById(R.id.fab);
//        fabAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getCreateRequest();
//            }
//        });
//
        overlay = findViewById(R.id.overlay);
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitReveal();
            }
        });

    }


    /**
     * Sets up the Floating action button.
     */
    private void setupFab() {

        fab = (Fab) findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay1 = findViewById(R.id.overlay1);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.theme_accent);

        /**
         * Create material sheet FAB.
         */
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay1, sheetColor, fabColor);

        /**
         * Set material sheet event listener.
         */
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                // Save current status bar color
                statusBarColor = getStatusBarColor();
                // Set darker status bar color to match the dim overlay
                setStatusBarColor(getResources().getColor(R.color.theme_primary_dark2));
            }

            @Override
            public void onHideSheet() {
                // Restore status bar color
                setStatusBarColor(statusBarColor);
            }
        });

        /**
         * Set material sheet item click listeners.
         */
        findViewById(R.id.fab_sheet_item_reply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialSheetFab.hideSheetThenFab();
                Intent intent=new Intent(TicketDetailActivity.this,TicketReplyActivity.class);
                startActivity(intent);
                //exitReveal();
                //enterReveal("Reply");
            }
        });
        findViewById(R.id.fab_sheet_item_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialSheetFab.hideSheetThenFab();
                Intent intent=new Intent(TicketDetailActivity.this,InternalNoteActivity.class);
                startActivity(intent);
//                exitReveal();
                //enterReveal("Internal notes");
            }
        });
//        findViewById(R.id.fab_sheet_item_photo).setOnClickListener(this);
//        findViewById(R.id.fab_sheet_item_note).setOnClickListener(this);
    }

    private int getStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getWindow().getStatusBarColor();
        }
        return 0;
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
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
//            title = getString(R.string.title_activity_ticketsave);
//            fragment = getSupportFragmentManager().findFragmentByTag(title);
//            if (fragment == null)
//                fragment = new TicketSaveFragment();
//            if (fragment != null) {
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.container_body, fragment);
//                // fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void getCreateRequest() {
        final CharSequence[] items = {"Reply", "Internal notes"};
        AlertDialog.Builder builder = new AlertDialog.Builder(TicketDetailActivity.this);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                if (items[item].equals("Reply")) {
                    cx = (int) fabAdd.getX() + dpToPx(40);
                    cy = (int) fabAdd.getY();
                    fabExpanded = true;
                    fabAdd.hide();
                    enterReveal("Reply");
                } else {
                    cx = (int) fabAdd.getX() + dpToPx(40);
                    cy = (int) fabAdd.getY();
                    fabExpanded = true;
                    fabAdd.hide();
                    enterReveal("Internal notes");
                }
            }
        });
        builder.show();
    }

    /**
     * This API is for creating the internal note.
     */
    private class CreateInternalNote extends AsyncTask<String, Void, String> {
        int ticketID;
        int userID;
        String note;

        CreateInternalNote(int ticketID, int userID, String note) {
            this.ticketID = ticketID;
            this.userID = userID;
            this.note = note;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postCreateInternalNote(ticketID, userID, note);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result == null) {
                Toasty.error(TicketDetailActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            Toasty.success(TicketDetailActivity.this, getString(R.string.internal_notes_posted), Toast.LENGTH_LONG).show();
            editTextInternalNote.getText().clear();
            exitReveal();

            Conversation conversation = (Conversation) adapter.getItem(0);
            if (conversation != null)
                conversation.refresh();
        }
    }

    /**
     * This API is for replying to the particular ticket.
     */
    private class ReplyTicket extends AsyncTask<String, Void, String> {
        int ticketID;
        //String cc;
        String replyContent;

        ReplyTicket(int ticketID, String replyContent) {
            this.ticketID = ticketID;
            //this.cc = cc;
            this.replyContent = replyContent;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postReplyTicket(ticketID, replyContent);
        }

        protected void onPostExecute(String result) {
            Log.d("reply", result + "");
            progressDialog.dismiss();
            if (result == null) {
                Toasty.error(TicketDetailActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
//            editTextCC.getText().clear();
            editTextReplyMessage.getText().clear();
            exitReveal();
            Toasty.success(TicketDetailActivity.this, getString(R.string.posted_reply), Toast.LENGTH_LONG).show();
            Conversation conversation = (Conversation) adapter.getItem(0);
            if (conversation != null)
                conversation.refresh();
            // try {
//                TicketThread ticketThread;
//                JSONObject jsonObject = new JSONObject(result);
//                JSONObject res = jsonObject.getJSONObject("result");
//                String clientPicture = "";
//                try {
//                    clientPicture = res.getString("profile_pic");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String messageTitle = "";
//                try {
//                    messageTitle = res.getString("title");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String clientName = "";
//                try {
//                    clientName = res.getString("first_name") + " " + res.getString("last_name");
//                    if (clientName.equals("") || clientName == null)
//                        clientName = res.getString("user_name");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                String messageTime = res.getString("created_at");
//                String message = res.getString("body");
//                String isReply = "true";
//                try {
//                    isReply = res.getString("is_reply");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            // ticketThread = new TicketThread(clientPicture, clientName, messageTime, messageTitle, message, isReply);
            //ticketThread = new TicketThread(clientName, messageTime, message, isReply);

//            if (fragmentConversation != null) {
//                exitReveal();
//                //fragmentConversation.addThreadAndUpdate(ticketThread);
//            }
//            } catch (JSONException e) {
//                if (fragmentConversation != null) {
//                    exitReveal();
//                }
//                e.printStackTrace();
//                // Toast.makeText(TicketDetailActivity.this, "Unexpected Error ", Toast.LENGTH_LONG).show();
//            }
        }
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

        }

        /**
         * This method is for controlling the FAB button.
         * @param position of the View pager page.
         */
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    fab.show();
                    break;

                default:
                    fab.hide();
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
     * Here we are controlling the FAB reply and internal note option.
     *
     * @param type
     */
    void enterReveal(String type) {
        fab.setVisibility(View.GONE);
        final View myView = findViewById(R.id.reveal);
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
        SupportAnimator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
        if (type.equals("Reply")) {
            myView.setVisibility(View.VISIBLE);
            myView.findViewById(R.id.section_reply).setVisibility(View.VISIBLE);
            myView.findViewById(R.id.section_internal_note).setVisibility(View.GONE);
            overlay.setVisibility(View.VISIBLE);
        } else {
            myView.setVisibility(View.VISIBLE);
            myView.findViewById(R.id.section_reply).setVisibility(View.GONE);
            myView.findViewById(R.id.section_internal_note).setVisibility(View.VISIBLE);
            overlay.setVisibility(View.VISIBLE);
        }

        anim.start();
    }

    void exitReveal() {

        View myView = findViewById(R.id.reveal);
        fab.show();
        fabExpanded = false;
        myView.setVisibility(View.GONE);
        overlay.setVisibility(View.GONE);
//        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
//        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
//        animator.setInterpolator(new AccelerateDecelerateInterpolator());
//        animator.setDuration(300);
//        animator = animator.reverse();
//        animator.addListener(new SupportAnimator.AnimatorListener() {
//
//            @Override
//            public void onAnimationStart() {
//
//            }
//
//            @Override
//            public void onAnimationEnd() {
//                fab.show();
//                fabExpanded = false;
//                myView.setVisibility(View.GONE);
//               // overlay.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationCancel() {
//
//            }
//
//            @Override
//            public void onAnimationRepeat() {
//
//            }
//
//        });
//        animator.start();

    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    /**
     * Handling the back button here.
     */
    @Override
    public void onBackPressed() {
        if (!MainActivity.isShowing) {
            Log.d("isShowing", "false");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            Log.d("isShowing", "true");
        }

        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
        } else {
            super.onBackPressed();
        }
//        if (fabExpanded)
//            exitReveal();
//        else super.onBackPressed();
    }

    /**
     * While resuming it will check if the internet
     * is available or not.
     */
    @Override
    protected void onResume() {
       // super.onResume();
        // register connection status listener
        // FaveoApplication.getInstance().setInternetListener(this);
        checkConnection();
        setupFab();
        fab.bringToFront();
        super.onResume();
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
            progressDialog.dismiss();
            if (isCancelled()) return;
//            if (progressDialog.isShowing())
//                progressDialog.dismiss();

            if (result == null) {
                //Toasty.error(TicketDetailActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                String ticketNumber=jsonObject1.getString("ticket_number");
                String statusName=jsonObject1.getString("status_name");
                String subject=jsonObject1.getString("title");
                String assigneeName;
                String first_name,last_name;
                first_name=jsonObject1.getString("assignee_first_name");
                last_name=jsonObject1.getString("assignee_last_name");

                if (!first_name.equals("null")){
                    assigneeName=jsonObject1.getString("assignee_first_name")+" "+jsonObject1.getString("assignee_last_name");
                    textviewAgentName.setText(assigneeName);
                }
                else if (first_name.equals("null")){
                    assigneeName=getString(R.string.unassigned);
                    textviewAgentName.setText(assigneeName);
                }
                else{
                    assigneeName=getString(R.string.unassigned);
                    textviewAgentName.setText(assigneeName);
                }
//                String userName =
//                Log.d("AgentName",userName);
//                if (userName.equals("")||userName.equals("null null")||!userName.contains(null)||!userName.contains("null")){
//                    textviewAgentName.setText(userName);
//                }
//                else{
//                    textviewAgentName.setText(getString(R.string.unassigned));
//                }
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


//                String assignee=jsonObject2.getString("assignee");
//                if (assignee.equals(null)||assignee.equals("null")||assignee.equals("")){
//                 textviewAgentName.setText(getString(R.string.unassigned));
//                }
//                else{
//                    JSONObject jsonObject3=jsonObject2.getJSONObject("assignee");
//                    try {
//                        if (jsonObject3.getString("first_name") != null&&jsonObject3.getString("last_name") != null) {
//                            //spinnerHelpTopics.setSelection(getIndex(spinnerHelpTopics, jsonObject1.getString("helptopic_name")));
//                            agentName=jsonObject3.getString("first_name") + " " + jsonObject3.getString("last_name");
//                            textviewAgentName.setText(agentName);
//
//                            //spinnerStaffs.setSelection(staffItems.indexOf("assignee_email"));
//                        }
//                        else{
//                            agentName=jsonObject3.getString("user_name");
//                            textviewAgentName.setText(agentName);
//                        }
//                        //spinnerHelpTopics.setSelection(Integer.parseInt(jsonObject1.getString("helptopic_id")));
//                    } catch (ArrayIndexOutOfBoundsException e){
//                        e.printStackTrace();
//                    } catch (Exception e) {
////                    spinnerHelpTopics.setVisibility(View.GONE);
////                    tv_helpTopic.setVisibility(View.GONE);
//                        e.printStackTrace();
//                    }
//                }

                //JSONObject jsonObject3=jsonObject2.getJSONObject("assignee");
//                if (jsonObject3.getString("first_name").equals("")&&jsonObject3.getString("last_name").equals("")){
//                    agentName=jsonObject3.getString("user_name");
//                }
//                else {
//                    agentName = jsonObject3.getString("first_name") + " " + jsonObject3.getString("last_name");
//                }

                Log.d("TITLE",subject);
                Log.d("TICKETNUMBER",ticketNumber);
                //String priority=jsonObject1.getString("priority_id");





            } catch (JSONException e) {
                e.printStackTrace();
            }
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
}