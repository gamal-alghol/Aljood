package com.samer.aljood.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.samer.aljood.R;
import com.samer.aljood.adapter.ChatAdapter;
import com.samer.aljood.model.Msg;
import com.samer.aljood.model.User;
import com.samer.aljood.notifcation.Client;
import com.samer.aljood.notifcation.MyResponse;
import com.samer.aljood.notifcation.Notification;
import com.samer.aljood.notifcation.NotificationAPI;
import com.samer.aljood.notifcation.NotificationSender;
import com.samer.aljood.utils.Constans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat extends AppCompatActivity {
ImageButton imgSend,btnBack;
RecyclerView listChat;
EditText editMsg;
    int limitMsgs = 8;
    ArrayList<Msg> msgArrayList;
    String id;
    LinearLayoutManager layoutManager;
ProgressBar progressBar;
 ArrayList<String> tokenAdmin;
    String nameUser;
    ChatAdapter messagesAdapter;
    private NotificationAPI apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        imgSend=findViewById(R.id.send);
        editMsg=findViewById(R.id.edit_text_msg);
        listChat=findViewById(R.id.list_chat);
        progressBar=findViewById(R.id.progressBar2);
        btnBack=findViewById(R.id.btn_back);
         id=FirebaseAuth.getInstance().getUid();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(NotificationAPI.class);
tokenAdmin=new ArrayList<>();
msgArrayList=new ArrayList<>();

        getMessages();
        getTokenAdmin();
   getnameUser();

        layoutManager = new LinearLayoutManager(this);
        listChat.setLayoutManager(layoutManager);
        listChat.setItemAnimator(new DefaultItemAnimator());
        messagesAdapter = new ChatAdapter(msgArrayList, getApplicationContext());
        listChat.setAdapter(messagesAdapter);

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editMsg.getText().toString()!=null&&!editMsg.getText().toString().equals("")){
                    sendMsg(id);
                }
            }
        });

btnBack.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onBackPressed();
    }
});
    }

    private void getnameUser() {
        FirebaseDatabase.getInstance()
                .getReference(Constans.FIREBASE_DB_USERS_TABLE).child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    DataSnapshot dataSnapshot= task.getResult();
                    User user=dataSnapshot.getValue(User.class);
                    nameUser=user.getName();
                    Log.d("ttt",nameUser);

                }
            }

        });
    }

    private void getTokenAdmin() {
        FirebaseDatabase.getInstance()
                .getReference(Constans.ADMIN_TOKEN).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1:snapshot.getChildren()){
                    String token=dataSnapshot1.getValue(String.class);
                  Log.d("ttt","token //->// "+token);
                    tokenAdmin.add(token);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMessages() {
        FirebaseDatabase.getInstance().getReference("chat").child(id).orderByChild("time")
            .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        limitMsgs = limitMsgs + 1;
                        Msg message = dataSnapshot.getValue(Msg.class);
                        msgArrayList.add(message);
                        messagesAdapter.notifyDataSetChanged();
//                        refreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                        listChat.scrollToPosition(msgArrayList.size() - 1);
                        listChat.setEnabled(true);
                    }
                }).run();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);

            }


        });
        progressBar.setVisibility(View.GONE);

    }

    private void sendMsg(String id) {
        imgSend.setEnabled(false);
        final Msg msg=new Msg(id,1,editMsg.getText().toString(),getCurrentDate());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("chat").child(id).push().setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    editMsg.setText("");
                    imgSend.setEnabled(true);
                    for(int i=0;i<tokenAdmin.size();i++){
sendNotifications(tokenAdmin.get(i),nameUser,msg.getMessage());
                    }
                }
            }
        });
    }
    public void sendNotifications(String usertoken, String title, String message) {

        Notification data = new Notification(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {

                    if (response.body().success != 1) {
                        Log.d("ttt","Fsend");

                    }else {
Log.d("ttt","send");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    private Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }
}