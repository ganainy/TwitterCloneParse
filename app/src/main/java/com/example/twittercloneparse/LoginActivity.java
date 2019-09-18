package com.example.twittercloneparse;

import android.content.Intent;
import android.os.Bundle;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    Button login,signup;
    EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //don't init parse again if user loggeed out
        if(!getIntent().hasExtra("source"))
            initParse();


        redirect();
        initViews();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });


    }

    private void redirect() {
        /** move user to home activity if he already logged in before*/
        if(ParseUser.getCurrentUser()!=null)
        {
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        finish();
        }
    }

    private void login() {
        if (username.getText().toString().equals("")||password.getText().toString().equals(""))
        {
            Toast.makeText(LoginActivity.this, "Username and password can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e==null)
                {
                    Log.i(TAG, "log in successful");
                    Toast.makeText(LoginActivity.this, "logged in successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    finish();
                }else
                {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, e.getMessage());
                }
            }
        });

    }

    private void signup() {
        if (username.getText().toString().equals("")||password.getText().toString().equals(""))
        {
            Toast.makeText(LoginActivity.this, "Username and password can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        ParseUser user=new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    Toast.makeText(LoginActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "signUpInBackground done");
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    finish();
                }else
                {
                    Toast.makeText(LoginActivity.this,  e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i(TAG, e.getMessage());
                }
            }
        });

    }

    private void initViews() {
        login=findViewById(R.id.loginButton);
        signup=findViewById(R.id.signupButton);

        password=findViewById(R.id.passwordEditText);
        username=findViewById(R.id.userNameEditText);
    }

    private void initParse() {
        Log.i("LoginActivity", " StarterApplication onCreate: ");
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("078d8c7b50f52c9fe4da8c8a843db434e50dfdbf")
                .clientKey("25d43481cdc9c7fc329110132c98412a79d3179d")
                .server("http://3.17.36.28:80/parse/")
                .build()
        );



/**add some info in user table in server if the app don't have signin and signup*/
        /* ParseUser.enableAutomaticUser();*/

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
