package co.helpdesk.faveo.frontend.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONException;
import org.json.JSONObject;

import co.helpdesk.faveo.Constants;
import co.helpdesk.faveo.FaveoApplication;
import co.helpdesk.faveo.Preference;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Authenticate;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.receivers.InternetReceiver;

public class LoginActivity extends AppCompatActivity implements InternetReceiver.InternetReceiverListener {

    TextView textViewFieldError, textViewForgotPassword;
    EditText editTextCompanyURL, editTextUsername, editTextPassword;
    ViewFlipper viewflipper;
    ImageButton buttonVerifyURL;
    Button buttonSignIn;
    int paddingTop, paddingBottom;
    ProgressDialog progressDialogVerifyURL;
    ProgressDialog progressDialogSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new Preference(getApplicationContext());

        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCE, 0);
        Boolean loginComplete = prefs.getBoolean("LOGIN_COMPLETE", false);
        if (loginComplete) {
            Constants.URL = Preference.getCompanyURL();
            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        setUpViews();

        buttonVerifyURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyURL = editTextCompanyURL.getText().toString();
                if (companyURL.trim().length() == 0 || !Patterns.WEB_URL.matcher(companyURL).matches()) {
                    Toast.makeText(v.getContext(), "Please enter a valid url", Toast.LENGTH_LONG).show();
                    return;
                }
                if (InternetReceiver.isConnected()) {
                    progressDialogVerifyURL.show();
                    new VerifyURL(LoginActivity.this, companyURL).execute();
                } else
                    Toast.makeText(v.getContext(), "Oops! No internet", Toast.LENGTH_LONG).show();
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNormalStates();
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                if (username.trim().length() == 0 || password.trim().length() == 0) {
                    if (username.trim().length() == 0)
                        setUsernameErrorStates();
                    else
                        setPasswordErrorStates();
                    return;
                }
                if (InternetReceiver.isConnected()) {
                    progressDialogSignIn.show();
                    new SignIn(LoginActivity.this, username, password).execute();
                } else
                    Toast.makeText(v.getContext(), "Oops! No internet", Toast.LENGTH_LONG).show();
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    public class VerifyURL extends AsyncTask<String, Void, String> {
        Context context;
        String companyURL;

        public VerifyURL(Context context, String companyURL) {
            this.context = context;
            this.companyURL = companyURL;
        }

        protected String doInBackground(String... urls) {
            if (!companyURL.endsWith("/"))
                companyURL = companyURL.concat("/");
            return new Helpdesk().getBaseURL(companyURL);
        }

        protected void onPostExecute(String result) {
            progressDialogVerifyURL.dismiss();

            Log.d("result veryfurl", result + "");
            if (result == null) {

                Toast.makeText(context, "Invalid URL", Toast.LENGTH_LONG).show();
                return;
            }

            if (result.contains("success")) {
                Preference.setCompanyURL(companyURL + "api/v1/");
                Constants.URL = Preference.getCompanyURL();
                viewflipper.showNext();
            } else
                Toast.makeText(context, "Error verifying URL", Toast.LENGTH_LONG).show();
        }
    }

    public class SignIn extends AsyncTask<String, Void, String> {
        Context context;
        String username;
        String password;

        public SignIn(Context context, String username, String password) {
            this.context = context;
            this.username = username;
            this.password = password;
        }

        protected String doInBackground(String... urls) {
            return new Authenticate().postAuthenticateUser(username, password);
        }

        protected void onPostExecute(String result) {
            Log.d("Response login", result);
            progressDialogSignIn.dismiss();
            if (result == null) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                return;
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String error = jsonObject.getString("status_code");
                    if (error.equals("401")) {
                        Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                JSONObject jsonObject = new JSONObject(result);
                String token = jsonObject.getString("token");
                String userID = jsonObject.getString("user_id");
                SharedPreferences.Editor authenticationEditor = getApplicationContext().getSharedPreferences(Constants.PREFERENCE, MODE_PRIVATE).edit();
                authenticationEditor.putString("ID", userID);
                authenticationEditor.putString("TOKEN", token);
                authenticationEditor.putString("USERNAME", username);
                authenticationEditor.putString("PASSWORD", password);
                authenticationEditor.putBoolean("LOGIN_COMPLETE", true);
                authenticationEditor.apply();
                Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }

    private void setNormalStates() {
        textViewFieldError.setVisibility(View.INVISIBLE);
        editTextUsername.setBackgroundResource(R.drawable.edittext_modified_states);
        editTextPassword.setBackgroundResource(R.drawable.edittext_modified_states);
        editTextUsername.setPadding(0, paddingTop, 0, paddingBottom);
        editTextPassword.setPadding(0, paddingTop, 0, paddingBottom);
    }

    private void setUsernameErrorStates() {
        textViewFieldError.setText("Please insert username");
        textViewFieldError.setVisibility(View.VISIBLE);
        editTextUsername.setBackgroundResource(R.drawable.edittext_error_state);
        editTextUsername.setPadding(0, paddingTop, 0, paddingBottom);
    }

    private void setPasswordErrorStates() {
        textViewFieldError.setText("Please insert password");
        textViewFieldError.setVisibility(View.VISIBLE);
        editTextPassword.setBackgroundResource(R.drawable.edittext_error_state);
        editTextPassword.setPadding(0, paddingTop, 0, paddingBottom);
    }

    private void setUpViews() {
        progressDialogVerifyURL = new ProgressDialog(this);
        progressDialogVerifyURL.setMessage("Verifying URL");
        progressDialogVerifyURL.setCancelable(false);

        progressDialogSignIn = new ProgressDialog(this);
        progressDialogSignIn.setMessage("Signing in");
        progressDialogSignIn.setCancelable(false);

        editTextCompanyURL = (EditText) findViewById(R.id.editText_company_url);
        if (editTextCompanyURL != null) {
            editTextCompanyURL.setText("http://");
        }
        viewflipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        buttonVerifyURL = (ImageButton) findViewById(R.id.imageButton_verify_url);
        textViewFieldError = (TextView) findViewById(R.id.textView_field_error);
        textViewForgotPassword = (TextView) findViewById(R.id.forgot_password);
        editTextUsername = (EditText) findViewById(R.id.editText_username);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        buttonSignIn = (Button) findViewById(R.id.button_sign_in);
        paddingTop = editTextUsername.getPaddingTop();
        paddingBottom = editTextUsername.getPaddingBottom();

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

}
