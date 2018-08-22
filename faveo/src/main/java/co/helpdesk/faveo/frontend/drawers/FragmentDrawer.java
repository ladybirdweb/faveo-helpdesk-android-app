package co.helpdesk.faveo.frontend.drawers;
/**
 * Created by Sumit
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import co.helpdesk.faveo.CircleTransform;
import co.helpdesk.faveo.Constants;
import co.helpdesk.faveo.FaveoApplication;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Authenticate;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.activities.CreateTicketActivity;
import co.helpdesk.faveo.frontend.activities.LoginActivity;
import co.helpdesk.faveo.frontend.activities.MainActivity;
import co.helpdesk.faveo.frontend.adapters.DrawerItemCustomAdapter;
import co.helpdesk.faveo.frontend.fragments.About;
import co.helpdesk.faveo.frontend.fragments.ClientList;
import co.helpdesk.faveo.frontend.fragments.ConfirmationDialog;
import co.helpdesk.faveo.frontend.fragments.Settings;
import co.helpdesk.faveo.frontend.fragments.tickets.ClosedTickets;
import co.helpdesk.faveo.frontend.fragments.tickets.InboxTickets;
import co.helpdesk.faveo.frontend.fragments.tickets.MyTickets;
import co.helpdesk.faveo.frontend.fragments.tickets.TrashTickets;
import co.helpdesk.faveo.frontend.fragments.tickets.UnassignedTickets;
import co.helpdesk.faveo.model.DataModel;
import co.helpdesk.faveo.model.UIUtils;
import es.dmoral.toasty.Toasty;

public class FragmentDrawer extends Fragment implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    public View containerView;
    private static String[] titles = null;
    public FragmentDrawerListener drawerListener;
    View layout;
    Context context;
    DataModel[] drawerItem;
    DrawerItemCustomAdapter drawerItemCustomAdapter;
    ConfirmationDialog confirmationDialog;
    int count=0;
    ProgressDialog progressDialog;
    String title;
    @BindView(R.id.usernametv)
    TextView userName;
    @BindView(R.id.domaintv)
    TextView domainAddress;
    @BindView(R.id.roleTv)
    TextView userRole;
    @BindView(R.id.imageView_default_profile)
    ImageView profilePic;
    @BindView(R.id.listviewNavigation)
    ListView listView;
    @BindView(R.id.ticket_list)
    LinearLayout ticketList;

    static String token;
    int responseCodeForShow;


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
        listView= (ListView) layout.findViewById(R.id.listviewNavigation);
        layout.findViewById(R.id.create_ticket).setOnClickListener(this);
//        layout.findViewById(R.id.inbox_tickets).setOnClickListener(this);
//        layout.findViewById(R.id.my_tickets).setOnClickListener(this);
//        layout.findViewById(R.id.unassigned_tickets).setOnClickListener(this);
//        layout.findViewById(R.id.closed_tickets).setOnClickListener(this);
//        layout.findViewById(R.id.trash_tickets).setOnClickListener(this);
        layout.findViewById(R.id.client_list).setOnClickListener(this);
        layout.findViewById(R.id.settings).setOnClickListener(this);
        layout.findViewById(R.id.about).setOnClickListener(this);
        layout.findViewById(R.id.logout).setOnClickListener(this);
        drawerItem = new DataModel[5];


        ButterKnife.bind(this, layout);
        confirmationDialog=new ConfirmationDialog();

        //inbox_count.setText(Prefs.getString("inboxTickets",null));
        //closed_tickets_count.setText(Prefs.getString("closedTickets", null));
        //unassigned_tickets_count.setText(Prefs.getString("unassignedTickets",null));
        //trash_tickets_count.setText(Prefs.getString("trashTickets", null));
        //my_tickets_count.setText(Prefs.getString("myTickets", null));
        drawerItem[0] = new DataModel(R.drawable.inbox_tickets,getString(R.string.inbox), Prefs.getString("inboxTickets",null));
        drawerItem[1] = new DataModel(R.drawable.my_ticket,getString(R.string.my_tickets),Prefs.getString("myTickets", null));
        drawerItem[2] = new DataModel(R.drawable.unassigned_ticket,getString(R.string.unassigned_tickets),Prefs.getString("unassignedTickets",null));
        drawerItem[3] = new DataModel(R.drawable.closed_ticket,getString(R.string.closed_tickets),Prefs.getString("closedTickets", null));
        drawerItem[4] = new DataModel(R.drawable.trash_ticket ,getString(R.string.trash),Prefs.getString("trashTickets", null));
        drawerItemCustomAdapter=new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row,drawerItem);
        listView.setAdapter(drawerItemCustomAdapter);
        progressDialog=new ProgressDialog(getActivity());
        drawerItemCustomAdapter.notifyDataSetChanged();
        UIUtils.setListViewHeightBasedOnItems(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment=null;
                //Toast.makeText(context, "Clicked at :"+position, Toast.LENGTH_SHORT).show();
                if (position==0){
                    title = getString(R.string.inbox);
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                    if (fragment == null)
                        fragment = new InboxTickets();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        // fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity()).setActionBarTitle(title);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                }else if (position==1){
                    title = getString(R.string.my_tickets);
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                    if (fragment == null)
                        fragment = new MyTickets();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        // fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity()).setActionBarTitle(title);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                }
                else if (position==2){
                    title = getString(R.string.unassigned_tickets);
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                    if (fragment == null)
                        fragment = new UnassignedTickets();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        // fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity()).setActionBarTitle(title);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                }else if (position==3){
                    title = getString(R.string.closed_tickets);
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                    if (fragment == null)
                        fragment = new ClosedTickets();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        // fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity()).setActionBarTitle(title);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                }else if (position==4){
                    title = getString(R.string.trash);
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                    if (fragment == null)
                        fragment = new TrashTickets();
                    if (fragment != null) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_body, fragment);
                        // fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        ((MainActivity) getActivity()).setActionBarTitle(title);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    }
                }

            }
        });
        try {
            String letter = Prefs.getString("profilePicture", null);
            Log.d("profilePicture", letter);
            if (letter.contains("jpg") || letter.contains("png") || letter.contains("jpeg")) {
                //profilePic.setColorFilter(getContext().getResources().getColor(R.color.white));
                //profilePic.setColorFilter(context.getResources().getColor(R.color.faveo), PorterDuff.Mode.SRC_IN);
                Picasso.with(context).load(letter).transform(new CircleTransform()).into(profilePic);
            }
            else {
                int color= Color.parseColor("#ffffff");
                String letter1 = String.valueOf(Prefs.getString("PROFILE_NAME", "").charAt(0));
                ColorGenerator generator = ColorGenerator.MATERIAL;
                TextDrawable drawable = TextDrawable.builder()
                        .buildRound(letter1,color);
                //profilePic.setAlpha(0.2f);
                profilePic.setColorFilter(context.getResources().getColor(R.color.faveo), PorterDuff.Mode.SRC_IN);
                profilePic.setImageDrawable(drawable);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
//        IImageLoader imageLoader = new PicassoLoader();
//        imageLoader.loadImage(profilePic, Prefs.getString("PROFILE_PIC", null), Prefs.getString("USERNAME", " ").charAt(0) + "");
        userRole.setText(Prefs.getString("ROLE", ""));
        domainAddress.setText(Prefs.getString("BASE_URL", ""));
        userName.setText(Prefs.getString("PROFILE_NAME", ""));

        ticketList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                listView.setVisibility(View.VISIBLE);
            }
        });
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //Toast.makeText(getActivity(), "Drawer opened", Toast.LENGTH_SHORT).show();
                //listView.setAdapter(null);
                //progressDialog=new ProgressDialog(getActivity());

                new FetchDependency().execute();
                new SendPostRequest().execute();
                drawerItemCustomAdapter.notifyDataSetChanged();
                getActivity().invalidateOptionsMenu();



            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //listView.setAdapter(null);
                //progressDialog=new ProgressDialog(getActivity());
//
//                new FetchDependency().execute();
//                drawerItemCustomAdapter.notifyDataSetChanged();
                //Toasty.normal(getActivity(), "Getting Information", Toast.LENGTH_LONG).show();
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //listView.setAdapter(null);
                //progressDialog=new ProgressDialog(getActivity());

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
    private class FetchDependency extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog.setMessage("Loading your information");
        }

        protected String doInBackground(String... urls) {

            return new Helpdesk().getDependency();

        }

        protected void onPostExecute(String result) {
            Log.d("Depen Response : ", result + "");
            progressDialog.dismiss();

            if (result == null) {

                return;
            }

            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");

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
                if (isAdded()) {
                    drawerItem[0] = new DataModel(R.drawable.inbox_tickets, getString(R.string.inbox), Prefs.getString("inboxTickets", null));
                    drawerItem[1] = new DataModel(R.drawable.my_ticket, getString(R.string.my_tickets), Prefs.getString("myTickets", null));
                    drawerItem[2] = new DataModel(R.drawable.unassigned_ticket, getString(R.string.unassigned_tickets), Prefs.getString("unassignedTickets", null));
                    drawerItem[3] = new DataModel(R.drawable.closed_ticket, getString(R.string.closed_tickets), Prefs.getString("closedTickets", null));
                    drawerItem[4] = new DataModel(R.drawable.trash_ticket, getString(R.string.trash), Prefs.getString("trashTickets", null));
                    drawerItemCustomAdapter = new DrawerItemCustomAdapter(getActivity(), R.layout.list_view_item_row, drawerItem);
                    listView.setAdapter(drawerItemCustomAdapter);
                    drawerItemCustomAdapter.notifyDataSetChanged();
                    UIUtils.setListViewHeightBasedOnItems(listView);
//                drawerItemCustomAdapter.notifyDataSetChanged();
//

                }
            }
            catch (JSONException e) {
                try {
                    Toasty.error(getActivity(), "Parsing Error!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }catch (NullPointerException e1){
                    e1.printStackTrace();
                }
            }
//            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
//            builder.setTitle("Welcome to FAVEO");
//            //builder.setMessage("After 2 second, this dialog will be closed automatically!");
//            builder.setCancelable(true);
//
//            final AlertDialog dlg = builder.create();
//
//            dlg.show();
//
//            final Timer t = new Timer();
//            t.schedule(new TimerTask() {
//                public void run() {
//                    dlg.dismiss(); // when the task active then close the dialog
//                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
//                }
//            }, 3000);

        }
    }
    public class SendPostRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            try {

                URL url = new URL(Constants.URL + "authenticate"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", Prefs.getString("USERNAME", null));
                postDataParams.put("password", Prefs.getString("PASSWORD", null));
                Log.e("params",postDataParams.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                //MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    Log.d("ifresponseCode",""+responseCode);
                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                }
                else {
                    if (responseCode==400){
                        Log.d("cameInThisBlock","true");
                        responseCodeForShow=400;
                    }
                    else if (responseCode==405){
                        responseCodeForShow=405;
                    }
                    else if (responseCode==302){
                        responseCodeForShow=302;
                    }
                    Log.d("elseresponseCode",""+responseCode);
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }


        @Override
        protected void onPostExecute(String result) {
            Log.d("resultFromNewCall",result);
            if (isAdded()) {

                if (result == null)
                    return;
                try{
                    JSONObject jsonObject = new JSONObject(result);
                    String error=jsonObject.getString("error");
                    if (error.equals("invalid_credentials")){
                        Prefs.putString("credentialsChanged","true");
                        final Toast toast = Toasty.info(getActivity(), "your credentials have been changed,you have to log in again to use the app.", Toast.LENGTH_SHORT);
                    toast.show();
                    new CountDownTimer(10000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            toast.show();
                        }

                        public void onFinish() {
                            toast.cancel();
                        }
                    }.start();
                        String url=Prefs.getString("BASE_URL",null);
                        Prefs.clear();
                        Prefs.putString("BASE_URL",url);
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                        return;
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String token = jsonObject.getString("token");
                    Prefs.putString("TOKEN", token);
                    // Preference.setToken(token);
                    Authenticate.token = token;
                    Helpdesk.token = token;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return ;
                }
//                if (responseCodeForShow == 400) {
//                    final Toast toast = Toasty.info(getActivity(), getString(R.string.urlchange), Toast.LENGTH_SHORT);
//                    toast.show();
//                    new CountDownTimer(10000, 1000) {
//                        public void onTick(long millisUntilFinished) {
//                            toast.show();
//                        }
//
//                        public void onFinish() {
//                            toast.cancel();
//                        }
//                    }.start();
//                    Prefs.clear();
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(intent);
//                    return;
//                }
//
//
//                if (responseCodeForShow == 405) {
//                    final Toast toast = Toasty.info(getActivity(), getString(R.string.urlchange), Toast.LENGTH_SHORT);
//                    toast.show();
//                    new CountDownTimer(10000, 1000) {
//                        public void onTick(long millisUntilFinished) {
//                            toast.show();
//                        }
//
//                        public void onFinish() {
//                            toast.cancel();
//                        }
//                    }.start();
//                    Prefs.clear();
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(intent);
//                    return;
//                }
//
//
//                if (responseCodeForShow == 302) {
//                    final Toast toast = Toasty.info(getActivity(), getString(R.string.urlchange), Toast.LENGTH_SHORT);
//                    toast.show();
//                    new CountDownTimer(10000, 1000) {
//                        public void onTick(long millisUntilFinished) {
//                            toast.show();
//                        }
//
//                        public void onFinish() {
//                            toast.cancel();
//                        }
//                    }.start();
//                    Prefs.clear();
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(intent);
//                    return;
//                }
            }

            try {
                JSONObject jsonObject=new JSONObject(result);
                JSONObject jsonObject1=jsonObject.getJSONObject("data");
                token = jsonObject1.getString("token");
                JSONObject jsonObject2=jsonObject1.getJSONObject("user");
                String role=jsonObject2.getString("role");
                if (role.equals("user")){
                    final Toast toast = Toasty.info(getActivity(), getString(R.string.roleChanged),Toast.LENGTH_SHORT);
                    toast.show();
                    new CountDownTimer(10000, 1000)
                    {
                        public void onTick(long millisUntilFinished) {toast.show();}
                        public void onFinish() {toast.cancel();}
                    }.start();
                    Prefs.clear();
                    //Prefs.putString("role",role);
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    //Toasty.info(getActivity(), getString(R.string.roleChanged), Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }
                Prefs.putString("TOKEN", token);
                Log.d("TOKEN",token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        title = getString(R.string.app_name);
        switch (v.getId()) {
            case R.id.create_ticket:
//                title = getString(R.string.create_ticket);
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                if (fragment == null)
//                    fragment = new CreateTicket();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(getContext(), CreateTicketActivity.class);
                startActivity(intent);

                break;

//            case R.id.inbox_tickets:
//
//                title = getString(R.string.inbox);
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                if (fragment == null)
//                    fragment = new InboxTickets();
//                break;
//            case R.id.my_tickets:
//                title = getString(R.string.my_tickets);
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                if (fragment == null)
//                    fragment = new MyTickets();
//                break;
//            case R.id.unassigned_tickets:
//                title = getString(R.string.unassigned_tickets);
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                if (fragment == null)
//                    fragment = new UnassignedTickets();
//                break;
//            case R.id.closed_tickets:
//                title = getString(R.string.closed_tickets);
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                if (fragment == null)
//                    fragment = new ClosedTickets();
//                break;
//            case R.id.trash_tickets:
//                title = getString(R.string.trash);
//                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
//                if (fragment == null)
//                    fragment = new TrashTickets();
//                break;
            case R.id.client_list:
                title = getString(R.string.client_list);
                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                if (fragment == null)
                    fragment = new ClientList();
                break;
            case R.id.settings:
                title = getString(R.string.settings);
                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                if (fragment == null)
                    fragment = new Settings();
                break;
            case R.id.about:
                title = getString(R.string.about);
                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(title);
                if (fragment == null)
                    fragment = new About();
                break;
            case R.id.logout:
                confirmationDialog.show(getFragmentManager(),null);
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            // fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            ((MainActivity) getActivity()).setActionBarTitle(title);
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }



    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }
    @Override
    public void onStop() {
        super.onStop();
        new FetchDependency().cancel(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new FetchDependency().cancel(true);
    }
}



