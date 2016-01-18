package com.example.bhavya.ScrapBook;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


//In this class , we have login check , which checks user is registered , if yes if the credentials are valid
//Register link, which takes the user to registration page.
public class Login extends ActionBarActivity implements View.OnClickListener{
    Button login;
    EditText etPassword,etUserName;
    TextView registerLink;
    DataBaseHelper helper = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_login);
        else
        setContentView(R.layout.content_login_land);
    try {
            etUserName = (EditText) findViewById(R.id.etUserName);
            etPassword = (EditText) findViewById(R.id.etPassword);
            registerLink = (TextView) findViewById(R.id.tvRegisterLink);
            login = (Button) findViewById(R.id.bLogin);
            login.setOnClickListener(this);
            registerLink.setOnClickListener(this);
        }catch(Exception e){

        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
       try {
           View v = getCurrentFocus();
           String s = etUserName.getText().toString();
           String s1 = etPassword.getText().toString();
           if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
               if (getSupportFragmentManager().findFragmentById(R.id.fragment) != null) {
                   getSupportFragmentManager().beginTransaction().
                           remove(getSupportFragmentManager().findFragmentById(R.id.fragment)).commit();
               }

               setContentView(R.layout.content_login_land);

           } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
               if (getSupportFragmentManager().findFragmentById(R.id.fragment1) != null) {
                   getSupportFragmentManager().beginTransaction().
                           remove(getSupportFragmentManager().findFragmentById(R.id.fragment1)).commit();
               }

               setContentView(R.layout.content_login);
           }


           etUserName = (EditText) findViewById(R.id.etUserName);
           etPassword = (EditText) findViewById(R.id.etPassword);
           etUserName.setText(s);
           etPassword.setText(s1);
           registerLink = (TextView) findViewById(R.id.tvRegisterLink);
           login = (Button) findViewById(R.id.bLogin);
           login.setOnClickListener(this);
           registerLink.setOnClickListener(this);
       }catch(Exception e){

       }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.bLogin:
                    String userName = etUserName.getText().toString();
                    String password = etPassword.getText().toString();
                    String pass = helper.searchPassword(userName);
                    if (pass != null) {
                        if (pass.equals(password)) {
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                            Toast temp = Toast.makeText(Login.this, "INVALID_CREDENTIALS", Toast.LENGTH_SHORT);
                            temp.show();
                        }
                    } else {
                        Toast temp = Toast.makeText(Login.this, "NOT_REGISTERED", Toast.LENGTH_SHORT);
                        temp.show();
                    }
                    break;
                case R.id.tvRegisterLink:
                    startActivity(new Intent(getApplicationContext(), Register.class));
                    break;
            }

        } catch (Exception e) {


        }
    }
    public boolean onKeyDown(int keycode, KeyEvent event) {
        try {
            if (keycode == KeyEvent.KEYCODE_BACK) {
                moveTaskToBack(true);
            }


        } catch (Exception e) {
        }
        return super.onKeyDown(keycode, event);
    }
}
