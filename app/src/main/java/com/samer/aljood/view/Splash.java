package com.samer.aljood.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.samer.aljood.R;
import com.google.firebase.auth.FirebaseAuth;

public class Splash extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 750;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStaustBar();
     setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                }
                else{

                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    finish();
                }


            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    private void hideStaustBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if (getActionBar() != null) {
            getActionBar().hide();

        }}

}