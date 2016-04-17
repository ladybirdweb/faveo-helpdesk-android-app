package co.helpdesk.faveo.frontend.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import co.helpdesk.faveo.Constants;
import co.helpdesk.faveo.Preference;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Authenticate;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    TextView textViewFieldError, textViewForgotPassword, textViewSignUp;
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
        if(loginComplete) {
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
                    Toast.makeText(getApplicationContext(), "Please enter a valid url", Toast.LENGTH_LONG).show();
                    return;
                }
                progressDialogVerifyURL.show();
                new VerifyURL(LoginActivity.this, companyURL).execute();
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
                progressDialogSignIn.show();
                new SignIn(LoginActivity.this, username, password).execute();
            }
        });

        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
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
            if (result == null) {
                Toast.makeText(context, "Invalid URL", Toast.LENGTH_LONG).show();
                return;
            }

            if (result.contains("success")) {
                Preference.setCompanyURL(companyURL + "api/v1/");
                Constants.URL = Preference.getCompanyURL();
                viewflipper.showNext();
            }
            else
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
            progressDialogSignIn.dismiss();
            if(result == null) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                return;
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
        progressDialogSignIn = new ProgressDialog(this);
        progressDialogSignIn.setMessage("Signing in");
        editTextCompanyURL = (EditText) findViewById(R.id.editText_company_url);
        viewflipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        buttonVerifyURL = (ImageButton) findViewById(R.id.imageButton_verify_url);
        textViewFieldError = (TextView) findViewById(R.id.textView_field_error);
        textViewForgotPassword = (TextView) findViewById(R.id.forgot_password);
        textViewSignUp = (TextView) findViewById(R.id.textView_sign_up);
        editTextUsername = (EditText) findViewById(R.id.editText_username);
        editTextPassword = (EditText) findViewById(R.id.editText_password);
        buttonSignIn = (Button) findViewById(R.id.button_sign_in);
        paddingTop = editTextUsername.getPaddingTop();
        paddingBottom = editTextUsername.getPaddingBottom();
    }

}
