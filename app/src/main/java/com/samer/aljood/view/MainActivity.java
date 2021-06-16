package com.samer.aljood.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.samer.aljood.R;
import com.samer.aljood.utils.Constans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.sentry.Sentry;

public class MainActivity extends AppCompatActivity {
EditText email,password;
Button LogIn;
    SweetAlertDialog pDialog;
String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sentry.captureMessage("testing SDK setup");
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        hideStaustBar();
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(getApplicationContext()) ;
        email=findViewById(R.id.mail_edt);
        password=findViewById(R.id.password_edt);
        LogIn=findViewById(R.id.signIn_btn);
        getRegistrationToken();
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputValid(email) && inputValid(password)) {
                    if (isEmailValid(email.getText())) {
                        final DatabaseReference updateData = FirebaseDatabase.getInstance()
                                .getReference(Constans.FIREBASE_DB_USERS_TABLE);

                 Constans.bDiloge(MainActivity.this,getResources().getString(R.string.loading),"",0);
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),
                                password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    updateData.child(FirebaseAuth.getInstance().getUid()).child("tokenId").setValue(token);
                                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                    finish();
                                    Constans.bDilogeStop();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Constans.bDilogeStop();
                                error("خطأ في البريد الالكتروني او كلمه المرور");
                            }
                        });
                    }else{
                        error("الرجاء كتابة البريد الالكتروني بشكل صحيح");
                    }
                }
            }
        });
    }

    private void error(String text) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(text)
                .setConfirmText("تعديل")
                .show();
    }
    private boolean inputValid(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "يرجى اضافة " + editText.getHint(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void    hideStaustBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        if (getActionBar() != null) {
            getActionBar().hide();

        }}
    private void getRegistrationToken () {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            token = task.getResult().getToken();
                        }else{
                            Log.d("ttt",task.getException().getMessage());
                        }
                    }
                });


    }
}