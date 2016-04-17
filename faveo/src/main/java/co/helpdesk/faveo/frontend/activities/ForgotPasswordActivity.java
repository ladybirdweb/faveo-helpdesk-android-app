package co.helpdesk.faveo.frontend.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import co.helpdesk.faveo.Constants;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Authenticate;


public class ForgotPasswordActivity extends AppCompatActivity {

    TextView textViewSignUp, textViewErrorEmail;
    EditText editTextEmail;
    Button buttonResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setUpViews();
        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setUpViews() {
        textViewSignUp = (TextView) findViewById(R.id.textView_sign_up);
        textViewErrorEmail = (TextView) findViewById(R.id.textView_error_email);
        editTextEmail = (EditText) findViewById(R.id.editText_email);
        buttonResetPassword = (Button) findViewById(R.id.button_reset_password);
    }

}
