package co.helpdesk.faveo.frontend.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.hbb20.CountryCodePicker;
import com.pixplicity.easyprefs.library.Prefs;
//import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
//import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.Constants;
import co.helpdesk.faveo.FaveoApplication;
import co.helpdesk.faveo.Helper;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.fragments.CreateTicket;
import co.helpdesk.faveo.frontend.receivers.InternetReceiver;
import co.helpdesk.faveo.model.Data;
import co.helpdesk.faveo.model.MessageEvent;
import es.dmoral.toasty.Toasty;

/**
 * This activity is for responsible for creating the ticket.
 * Here we are using create ticket async task which is
 * POST request.We are getting the JSON data here from the dependency API.
 */
public class CreateTicketActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_LOAD_FILE = 42;
    //CountryCodePicker ccp;
    //String imgDecodableString;
    static final String TAG = "CreateTicketActivity";
    boolean allCorrect;
    ArrayAdapter<Data> spinnerPriArrayAdapter, spinnerHelpArrayAdapter,spinnerSLAAdapter;
    ArrayAdapter<String> spinnerSlaArrayAdapter, spinnerAssignToArrayAdapter,
            spinnerDeptArrayAdapter;
    @BindView(R.id.fname_edittext)
    EditText editTextFirstName;
    @BindView(R.id.email_edittext)
    EditText editTextEmail;
    @BindView(R.id.lname_edittext)
    EditText editTextLastName;
    @BindView(R.id.phone_edittext)
    EditText editTextPhone;
//    @BindView(R.id.spinner_code)
//    SearchableSpinner phCode;
    Toolbar toolbar;
    @BindView(R.id.attachment_name)
    TextView attachmentFileName;
    @BindView(R.id.attachment_size)
    TextView attachmentFileSize;
    @BindView(R.id.attachment_layout)
    RelativeLayout attachment_layout;
    @BindView(R.id.attach_fileimgview)
    ImageView imageView;
    @BindView(R.id.sub_edittext)
    EditText subEdittext;
    @BindView(R.id.msg_edittext)
    EditText msgEdittext;
    @BindView(R.id.phone_edittext10)
    EditText editTextMobile;
    //    @BindView(R.id.spinner_dept)
//    Spinner spinnerDept;
    @BindView(R.id.spinner_pri)
    Spinner spinnerPriority;
    @BindView(R.id.spinner_help)
    Spinner spinnerHelpTopic;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.msgLinearLayout)
    LinearLayout msglinearLayout;
    @BindView(R.id.relativefname)
    RelativeLayout relativeLayoutfname;
    @BindView(R.id.relativeEmaill)
    RelativeLayout relativeLayoutemail;
    @BindView(R.id.relativeSubject)
    RelativeLayout relativeLayoutsub;

    @BindView(R.id.relativemessage)
    RelativeLayout relativeLayoutmessage;
    @BindView(R.id.relativepriority)
    RelativeLayout relativepriority;
    @BindView(R.id.relativehelp)
    RelativeLayout relativehelp;
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScrollView;
    @BindView(R.id.spinner_sla_plans)
            Spinner spinnerSLA;
    @BindView(R.id.refresh)
            Button button ;
    ProgressDialog progressDialog;
    CountryCodePicker countryCodePicker;
    ArrayList<Data> helptopicItems, priorityItems,slaItems;
    String mobile="";
    String splChrs = "-/@#$%^&_+=()" ;
    String countrycode = "";
    ImageView imageViewBack;
    Animation rotation;
    public static String
            keyDepartment = "", valueDepartment = "",
            keySLA = "", valueSLA = "",
            keyStatus = "", valueStatus = "",
            keyStaff = "", valueStaff = "",
            keyTeam = "", valueTeam = "",
            keyName="",
            keyPriority = "", valuePriority = "",
            keyTopic = "", valueTopic = "",
            keySource = "", valueSource = "",
            keyType = "", valueType = "";
    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            String blockCharacterSet = "~!@#$%^&*()_-;:<>,.[]{}|/+";
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);
        Window window = CreateTicketActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(CreateTicketActivity.this,R.color.faveo));
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        imageViewBack= (ImageView) findViewById(R.id.imageViewBack);
        countryCodePicker= (CountryCodePicker) findViewById(R.id.countrycoode);
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countrycode=countryCodePicker.getSelectedCountryCode();
            }
        });

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editTextFirstName.requestFocus();
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createButtonClick();
            }
        });
        JSONObject jsonObject;
        String json = Prefs.getString("DEPENDENCY", "");
        try {
            helptopicItems = new ArrayList<>();
            helptopicItems.add(new Data(0, "Please select help topic"));
            jsonObject = new JSONObject(json);
            JSONArray jsonArrayHelpTopics = jsonObject.getJSONArray("helptopics");
            for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {
                Data data = new Data(Integer.parseInt(jsonArrayHelpTopics.getJSONObject(i).getString("id")), jsonArrayHelpTopics.getJSONObject(i).getString("topic"));
                helptopicItems.add(data);
            }

            JSONArray jsonArrayPriorities = jsonObject.getJSONArray("priorities");
            priorityItems = new ArrayList<>();
            priorityItems.add(new Data(0, "Please select the priority"));
            for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                Data data = new Data(Integer.parseInt(jsonArrayPriorities.getJSONObject(i).getString("priority_id")), jsonArrayPriorities.getJSONObject(i).getString("priority"));
                priorityItems.add(data);
            }
            JSONArray jsonArraySLA=jsonObject.getJSONArray("sla");
            slaItems=new ArrayList<>();
            slaItems.add(new Data(0,"Please select SLA"));
            for (int i = 0; i < jsonArraySLA.length(); i++) {
                Data data = new Data(Integer.parseInt(jsonArraySLA.getJSONObject(i).getString("id")), jsonArraySLA.getJSONObject(i).getString("sla_duration"));
                slaItems.add(data);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        setUpViews();

        spinnerPriority.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(subEdittext.getWindowToken(), 0);
                return false;
            }
        }) ;
        spinnerHelpTopic.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(subEdittext.getWindowToken(), 0);
                return false;
            }
        }) ;
        spinnerSLA.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(subEdittext.getWindowToken(), 0);
                return false;
            }
        }) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                hideKeyboard(CreateTicketActivity.this);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicketActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle(getString(R.string.refreshingPage));

                // Setting Dialog Message
                alertDialog.setMessage(getString(R.string.refreshPage));

                // Setting Icon to Dialog
                alertDialog.setIcon(R.mipmap.ic_launcher);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke YES event
                        //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        if (InternetReceiver.isConnected()){
                            button.startAnimation(rotation);
//                            progressDialog=new ProgressDialog(CreateTicketActivity.this);
//                            progressDialog.setMessage(getString(R.string.refreshing));
//                            progressDialog.show();

                            new FetchDependency().execute();
                            setUpViews();
                        }
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_ticket_menu, menu);

        return true;
    }

    /**
     * Handlig the menu items here.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String ss = cursor.getString(column_index);
        cursor.close();
        return ss;
    }


    @Override
    public void onBackPressed() {
            finish();
    }

    /**
     * Here we are handling the activity result.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == RESULT_LOAD_FILE && resultCode == RESULT_OK
                    && null != data) {
                Uri selectedFile = data.getData();
                String uriString = getPath(selectedFile);
                File myFile = new File(uriString);
                imageView.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_txt));
                attachmentFileSize.setText(getFileSize(myFile.length()));
                attachmentFileName.setText(myFile.getName());
            } else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                Uri selectedImage = data.getData();
                Log.d("selectedIMG  ", selectedImage + "");
                Log.d("getPath()  ", getPath(selectedImage) + "");
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                String uriString = getPath(selectedImage);
                File myFile = new File(uriString);
                attachment_layout.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
                Log.d("size", myFile.length() + "");
                attachmentFileSize.setText(getFileSize(myFile.length()));
                attachmentFileName.setText(myFile.getName());

            } else {
                Toasty.info(this, getString(R.string.you_hvent_picked_anything),
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toasty.error(this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG)
                    .show();
        }
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * Setting up the views here.
     */
    public void setUpViews() {
        final CursorAdapter suggestionAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                null,
                new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
                new int[]{android.R.id.text1},
                0);
        final List<String> suggestions = new ArrayList<>();

        spinnerHelpArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, helptopicItems); //selected item will look like a spinner set from XML
        spinnerHelpArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHelpTopic.setAdapter(spinnerHelpArrayAdapter);

        spinnerSLAAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,slaItems); //selected item will look like a spinner set from XML
        spinnerSLAAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSLA.setAdapter(spinnerSLAAdapter);


        spinnerPriArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priorityItems); //selected item will look like a spinner set from XML
        spinnerPriArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(spinnerPriArrayAdapter);

        editTextLastName.setFilters(new InputFilter[]{filter});
        editTextFirstName.setFilters(new InputFilter[]{filter});
        subEdittext.setFilters(new InputFilter[]{filter});


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    /**
     * Handling the create button here.
     */
    public void createButtonClick() {
        String subject = subEdittext.getText().toString();
        String message = msgEdittext.getText().toString();
        String email = editTextEmail.getText().toString();
        String fname = editTextFirstName.getText().toString();
        String lname = editTextLastName.getText().toString();
        String phone = editTextPhone.getText().toString();
        mobile = editTextMobile.getText().toString();

        countrycode=countryCodePicker.getSelectedCountryCode();

        allCorrect = true;


        final Data helpTopic = (Data) spinnerHelpTopic.getSelectedItem();
        Log.d("ID of objt", "" + helpTopic.ID);
        final Data priority = (Data) spinnerPriority.getSelectedItem();
        final Data sla= (Data) spinnerSLA.getSelectedItem();

        if (fname.length()==0&&email.length()==0&&subject.length()==0&&message.length()==0&&helpTopic.ID == 0&&priority.ID == 0){
            Toasty.warning(this,getString(R.string.fill_all_the_details),Toast.LENGTH_SHORT).show();
            allCorrect=false;
        }

        else if (fname.trim().length() == 0) {
            relativeLayoutfname.requestFocus();
            nestedScrollView.scrollTo(0,0);
            //editTextFirstName.requestFocus();
            Toasty.warning(this, getString(R.string.fill_firstname), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        } else if (fname.trim().length() < 3) {
            Toasty.warning(this, getString(R.string.firstname_minimum_char), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }
        else if (fname.length()>20){
            Toasty.warning(this, getString(R.string.firstname_maximum_char), Toast.LENGTH_SHORT).show();
            allCorrect=false;
        }   else if (email.trim().length() == 0 || !Helper.isValidEmail(email)) {
            nestedScrollView.scrollTo(0,0);
            relativeLayoutemail.requestFocus();
            editTextEmail.requestFocus();
            Toasty.warning(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }



        else if (subject.trim().length() == 0) {
            relativeLayoutsub.requestFocus();
            subEdittext.requestFocus();
            Toasty.warning(this, getString(R.string.sub_must_not_be_empty), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }
        else if (subject.trim().length() < 5) {
            Toasty.warning(this, getString(R.string.sub_minimum_char), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }
        else if (subject.matches("[" + splChrs + "]+")){
            Toasty.warning(this, getString(R.string.only_special_characters_not_allowed_here), Toast.LENGTH_SHORT).show();
            allCorrect=false;
        }
        else if (subject.trim().length()>50){
            Toasty.warning(this,"Subject must not exceed 50 characters"
                    , Toast.LENGTH_SHORT).show();
            allCorrect=false;
        }
        else if (priority.ID == 0) {
            //relativepriority.requestFocus();
            //nestedScrollView.scrollTo(0,5);
            nestedScrollView.fullScroll(View.FOCUS_DOWN);
            spinnerPriority.requestFocus();
            allCorrect = false;

            Toasty.warning(CreateTicketActivity.this, getString(R.string.please_select_some_priority), Toast.LENGTH_SHORT).show();
        }
        else if (helpTopic.ID == 0) {
            nestedScrollView.fullScroll(View.FOCUS_DOWN);
            spinnerHelpTopic.requestFocus();
            allCorrect = false;
            Toasty.warning(CreateTicketActivity.this, getString(R.string.select_some_helptopic), Toast.LENGTH_SHORT).show();
        }
        else if (sla.ID==0){
            nestedScrollView.fullScroll(View.FOCUS_DOWN);
            spinnerSLA.requestFocus();
            allCorrect = false;

            Toasty.warning(CreateTicketActivity.this, getString(R.string.select_some_sla), Toast.LENGTH_SHORT).show();
        }
        else if (message.trim().length() == 0) {
            //nestedScrollView.scrollTo(0, (int) msgEdittext.getY());
            relativeLayoutmessage.requestFocus();
            msglinearLayout.requestFocus();

            msgEdittext.requestFocus();

            Toasty.warning(this, getString(R.string.msg_must_not_be_empty), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }  else if (message.trim().length() < 10) {
            Toasty.warning(this, getString(R.string.msg_minimum_char), Toast.LENGTH_SHORT).show();
            allCorrect = false;
        }

        if (allCorrect) {

            if (InternetReceiver.isConnected()) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage(getString(R.string.creating_ticket));

                try {
                    fname = URLEncoder.encode(fname.trim(), "utf-8");
                    lname = URLEncoder.encode(lname.trim(), "utf-8");
                    subject = URLEncoder.encode(subject.trim(), "utf-8");
                    message = URLEncoder.encode(message.trim(), "utf-8");
                    email = URLEncoder.encode(email.trim(), "utf-8");
                    phone = URLEncoder.encode(phone.trim(), "utf-8");
                    mobile = URLEncoder.encode(mobile.trim(), "utf-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                hideKeyboard(this);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateTicketActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle(R.string.creating);

                // Setting Dialog Message
                alertDialog.setMessage(R.string.sure);

                // Setting Icon to Dialog
                alertDialog.setIcon(R.mipmap.ic_launcher);

                // Setting Positive "Yes" Button
                final String finalSubject = subject;
                final String finalMessage = message;
                final String finalPhone = phone;
                final String finalFname = fname;
                final String finalLname = lname;
                final String finalEmail = email;
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke YES event
                        //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                        if (InternetReceiver.isConnected()){

                            progressDialog.show();
                            new CreateNewTicket(Integer.parseInt(Prefs.getString("ID", null)), finalSubject, finalMessage, helpTopic.ID,sla.ID, priority.ID, finalPhone, finalFname, finalLname, finalEmail, countrycode, mobile).execute();
//                            progressDialog=new ProgressDialog(CreateTicketActivity.this);
//                            progressDialog.setMessage(getString(R.string.refreshing));
//                            progressDialog.show();


                        }
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();


            } else
                Toasty.info(this, getString(R.string.oops_no_internet), Toast.LENGTH_SHORT, true).show();
        }
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    /**
     * Async task for creating the ticket.
     */
    private class CreateNewTicket extends AsyncTask<String, Void, String> {
        String fname, lname, email, code;
        String subject;
        String body;
        String phone;
        String mobile;
        int helpTopic;
         int SLA;
        int priority;
        //int dept;
        int userID;

        CreateNewTicket(int userID, String subject, String body,
                        int helpTopic,int SLA, int priority, String phone, String fname, String lname, String email, String code, String mobile) {

            this.subject = subject;
            this.body = body;
            this.helpTopic = helpTopic;
            this.SLA = SLA;
            this.priority = priority;
            //this.dept = dept;
            this.userID = userID;
            this.phone = phone;
            this.lname = lname;
            this.fname = fname;
            this.email = email;
            this.code = code;
            this.mobile = mobile;

        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postCreateTicket(userID, subject, body, helpTopic,SLA, priority, fname, lname, phone, email, code, mobile);
        }

        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result == null) {
                Toasty.error(CreateTicketActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
            try {

                JSONObject jsonObject=new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("error");
                String message=jsonObject1.getString("code");

                if (message.contains("The code feild is required.")){
                    Toasty.warning(CreateTicketActivity.this,getString(R.string.select_code),Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

  if (result.contains("Ticket created successfully!")) {
                Toasty.success(CreateTicketActivity.this, getString(R.string.ticket_created_success), Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(CreateTicketActivity.this, MainActivity.class));

            }


  }


        }



     /**
     * This method will be called when a MessageEvent is posted (in the UI thread for Toast).
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        showSnack(event.message);
    }

    @Override
    protected void onResume() {
        super.onResume();

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

    private class FetchDependency extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {

            return new Helpdesk().getDependency();

        }

        protected void onPostExecute(String result) {
            Log.d("Depen Response : ", result + "");
            button.clearAnimation();
            if (result == null) {
                return;
            }

            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                Prefs.putString("DEPENDENCY", jsonObject1.toString());
                // Preference.setDependencyObject(jsonObject1, "dependency");
                JSONArray jsonArrayDepartments = jsonObject1.getJSONArray("departments");
                for (int i = 0; i < jsonArrayDepartments.length(); i++) {
                    keyDepartment += jsonArrayDepartments.getJSONObject(i).getString("id") + ",";
                    valueDepartment += jsonArrayDepartments.getJSONObject(i).getString("name") + ",";
                }
                Prefs.putString("keyDept", keyDepartment);
                Prefs.putString("valueDept", valueDepartment);


                JSONArray jsonArraySla = jsonObject1.getJSONArray("sla");
                for (int i = 0; i < jsonArraySla.length(); i++) {
                    keySLA += jsonArraySla.getJSONObject(i).getString("id") + ",";
                    valueSLA += jsonArraySla.getJSONObject(i).getString("name") + ",";
                }
                Prefs.putString("keySLA", keySLA);
                Prefs.putString("valueSLA", valueSLA);

                JSONArray jsonArrayPriorities = jsonObject1.getJSONArray("priorities");
                for (int i = 0; i < jsonArrayPriorities.length(); i++) {
                    // keyPri.add(jsonArrayPriorities.getJSONObject(i).getString("priority_id"));
                    //valuePri.add(jsonArrayPriorities.getJSONObject(i).getString("priority"));
                    keyPriority += jsonArrayPriorities.getJSONObject(i).getString("priority_id") + ",";
                    valuePriority += jsonArrayPriorities.getJSONObject(i).getString("priority") + ",";
                }
                Prefs.putString("keyPri", keyPriority);
                Prefs.putString("valuePri", valuePriority);
                //Prefs.putOrderedStringSet("keyPri", keyPri);
                // Prefs.putOrderedStringSet("valuePri", valuePri);
                //Log.d("Testtttttt", Prefs.getOrderedStringSet("keyPri", keyPri) + "   " + Prefs.getOrderedStringSet("valuePri", valuePri));


                JSONArray jsonArrayHelpTopics = jsonObject1.getJSONArray("helptopics");
                for (int i = 0; i < jsonArrayHelpTopics.length(); i++) {

                    keyTopic += jsonArrayHelpTopics.getJSONObject(i).getString("id") + ",";
                    valueTopic += jsonArrayHelpTopics.getJSONObject(i).getString("topic") + ",";
                }

                Prefs.putString("keyHelpTopic", keyTopic);
                Prefs.putString("valueHelptopic", valueTopic);

                JSONArray jsonArrayStatus = jsonObject1.getJSONArray("status");
                for (int i = 0; i < jsonArrayStatus.length(); i++) {
                    keyStatus += jsonArrayStatus.getJSONObject(i).getString("id") + ",";
                    valueStatus += jsonArrayStatus.getJSONObject(i).getString("name") + ",";
                }
                Prefs.putString("keyStatus", keyStatus);
                Prefs.putString("valueStatus", valueStatus);

                JSONArray jsonArraySources = jsonObject1.getJSONArray("sources");
                for (int i = 0; i < jsonArraySources.length(); i++) {
                    keySource += jsonArraySources.getJSONObject(i).getString("id") + ",";
                    valueSource += jsonArraySources.getJSONObject(i).getString("name") + ",";
                }

                Prefs.putString("keySource", keySource);
                Prefs.putString("valueSource", valueSource);

                int open = 0, closed = 0, trash = 0, unasigned = 0, my_tickets = 0;
                JSONArray jsonArrayTicketsCount = jsonObject1.getJSONArray("tickets_count");
                for (int i = 0; i < jsonArrayTicketsCount.length(); i++) {
                    String name = jsonArrayTicketsCount.getJSONObject(i).getString("name");
                    String count = jsonArrayTicketsCount.getJSONObject(i).getString("count");

                    switch (name) {
                        case "Open":
                            open = Integer.parseInt(count);
                            break;
                        case "Closed":
                            closed = Integer.parseInt(count);
                            break;
                        case "Deleted":
                            trash = Integer.parseInt(count);
                            break;
                        case "unassigned":
                            unasigned = Integer.parseInt(count);
                            break;
                        case "mytickets":
                            my_tickets = Integer.parseInt(count);
                            break;
                        default:
                            break;

                    }
                }


                if (open > 999)
                    Prefs.putString("inboxTickets", "999+");
                else
                    Prefs.putString("inboxTickets", open + "");

                if (closed > 999)
                    Prefs.putString("closedTickets", "999+");
                else
                    Prefs.putString("closedTickets", closed + "");

                if (my_tickets > 999)
                    Prefs.putString("myTickets", "999+");
                else
                    Prefs.putString("myTickets", my_tickets + "");

                if (trash > 999)
                    Prefs.putString("trashTickets", "999+");
                else
                    Prefs.putString("trashTickets", trash + "");

                if (unasigned > 999)
                    Prefs.putString("unassignedTickets", "999+");
                else
                    Prefs.putString("unassignedTickets", unasigned + "");

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {


            }
            finish();
            Intent intent=new Intent(CreateTicketActivity.this, CreateTicketActivity.class);
            startActivity(intent);

        }
    }
}
