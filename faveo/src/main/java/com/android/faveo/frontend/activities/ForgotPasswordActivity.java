package com.android.faveo.frontend.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.faveo.R;


public class ForgotPasswordActivity extends AppCompatActivity {

    TextView textViewSignUp, textViewErrorEmail;
    EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setUpViews();
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setUpViews() {
        textViewSignUp = (TextView) findViewById(R.id.textView_sign_up);
        textViewErrorEmail = (TextView) findViewById(R.id.textView_error_email);
        editTextEmail = (EditText) findViewById(R.id.editText_email);
    }

}
