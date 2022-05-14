package com.example.thedigitaldinerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.thedigitaldinerapp.admin.AdminMainPanel;
import com.example.thedigitaldinerapp.models.User;
import com.example.thedigitaldinerapp.utils.SharedPrefManager;

public class SplashScreen extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread th=new Thread(){
            @Override
            public void run(){
                try {
                    sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                        user = SharedPrefManager.getInstance(SplashScreen.this).getUser();
                        Intent intent;
                        if (user.getUserType().equals("ROLE_ADMIN")) {
                            intent = new Intent(SplashScreen.this, AdminMainPanel.class);
                        }
                        else {
                            intent = new Intent(SplashScreen.this, MainActivity.class);
                        }
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    finish();
                }
            }
        };
        th.start();

    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}