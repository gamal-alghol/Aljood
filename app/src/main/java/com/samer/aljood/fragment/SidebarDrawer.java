package com.samer.aljood.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.samer.aljood.R;
import com.samer.aljood.model.User;
import com.samer.aljood.utils.Constans;
import com.samer.aljood.view.AboutUs;
import com.samer.aljood.view.Branches;
import com.samer.aljood.view.Chat;
import com.samer.aljood.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.Calendar;

public class SidebarDrawer extends Fragment {
    View view;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    LinearLayout aboutUsBtn,pranchUsBtn,chatBtn,logOut;
    Intent intentAiguilleur;
    ImageButton face,instgram,whatsapp,gmail;
TextView name,account,txtTime,points;
    private Calendar time;
    private Calendar now;
    Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    public SidebarDrawer(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

         view = inflater.inflate(R.layout.fragment_sidebar_drawer, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aboutUsBtn=view.findViewById(R.id.about_us);
        pranchUsBtn=view.findViewById(R.id.pranch_us);
        face=view.findViewById(R.id.facebook);
        instgram=view.findViewById(R.id.instgram);
        whatsapp=view.findViewById(R.id.whatsapp);
        gmail=view.findViewById(R.id.gmail);
        name=view.findViewById(R.id.txt_name);
        account=view.findViewById(R.id.word_account);
        txtTime=view.findViewById(R.id.txt_account);
        chatBtn=view.findViewById(R.id.contact_us);
        points=view.findViewById(R.id.points_text);
        logOut=view.findViewById(R.id.logout);

        swipeRefreshLayout=view.findViewById(R.id.swipe_refresh);
        context=getContext();
        getUserInfo();
        socialMedia();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserInfo();

            }
        });

        aboutUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), AboutUs.class));
            }
        });
        pranchUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), Branches.class));
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getContext(), Chat.class));
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getActivity().startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

    }

    private void socialMedia() {

        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    intentAiguilleur = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + "1635763410024243"));
                } catch (Exception e) {
                    Log.d("ttt",e.getMessage());
                    intentAiguilleur =  new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + "1635763410024243"));
                }
                getActivity().startActivity(intentAiguilleur);

            }

        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String smsNumber = "972597166606";
                    Intent sendIntent = new Intent("android.intent.action.MAIN");
                    sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
                    startActivity(sendIntent);
                }catch (Exception e){
                    copy("972597166606");
                }
            }
        });
        instgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scheme = "http://instagram.com/_u/al_jood_optical";
                String nomPackageInfo ="com.instagram.android";
                try {
                    getActivity().getPackageManager().getPackageInfo(nomPackageInfo, 0);
                    intentAiguilleur = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));
                } catch (Exception e) {

                }
                getActivity().startActivity(intentAiguilleur);

            }
        });
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentAiguilleur = new Intent(Intent.ACTION_SEND);
                intentAiguilleur.setType("text/html");
                intentAiguilleur.putExtra(Intent.EXTRA_EMAIL, new String[]{"al-jood.optical@gmail.com"});
                intentAiguilleur.setPackage("com.google.android.gm");
                if (intentAiguilleur.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intentAiguilleur);
                }
            }
        });
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
//        View containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;


        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (getActivity() != null) {
                    getActivity().invalidateOptionsMenu();
                }


            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                if (getActivity() != null) {
                    getActivity().invalidateOptionsMenu();
                }


            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);

                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

//        mDrawerToggle.setHomeAsUpIndicator(R.drawable.icon_menu);
        mDrawerToggle.setDrawerIndicatorEnabled(false);

        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                }
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }
    public void openDrawer(final CoordinatorLayout drawerLayout, final View fragment, final ImageButton imageButton, final User user) {

        if (!mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.openDrawer(Gravity.RIGHT);
            mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
            mDrawerLayout.setDrawerElevation(0);
            mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onDrawerSlide(View drawer, float slideOffset) {
                    drawerLayout.setX(fragment.getWidth() * -slideOffset);
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) drawerLayout.getLayoutParams();
                    lp.height = drawer.getHeight() - (int) (drawer.getHeight() * slideOffset * 0.3f);
                    lp.topMargin = (drawer.getHeight() - lp.height) / 2;

                    drawerLayout.setLayoutParams(lp);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    imageButton.setImageResource(R.drawable.ic_baseline_dehaze);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    imageButton.setImageResource(R.drawable.icon_left_arrow);



                }
            });

        }
    }
    private void getUserInfo() {
        FirebaseDatabase.getInstance().getReference(Constans.FIREBASE_DB_USERS_TABLE)
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if (dataSnapshot!=null){

                    User  user=  dataSnapshot.getValue(User.class);
                    setUserInfo( user);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void setUserInfo(User user) {
        if (user!=null){
            name.setText(user.getName());
            account.setText(user.getAccount()+"ش  ");
            time = Calendar.getInstance();
            time.setTimeInMillis(user.getTime());
            now = Calendar.getInstance();
            points.setText(user.getPoints()+"نقطة  ");
            setTime(txtTime);
        }
    }

    private void setTime(TextView timePost) {
        if (now.getTimeInMillis() - time.getTimeInMillis() >= 59 * 1000 * 60*24*7) {
Log.d("ttt","1");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(now.getTimeInMillis() - time.getTimeInMillis());
            timePost.setText("اخر تحديث "+(calendar.getTimeInMillis() / (59 * 1000 * 60*24*7)) + " " + context.getResources().getString(R.string.weeks_ago));
        } else
        if (now.getTimeInMillis() - time.getTimeInMillis() >= 59 * 1000 * 60*24) {
            Log.d("ttt","2");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(now.getTimeInMillis() - time.getTimeInMillis());
            timePost.setText("اخر تحديث "+(calendar.getTimeInMillis() / (59 * 1000 * 60*24)) + " " + context.getResources().getString(R.string.days_ago));
        } else
        if (now.getTimeInMillis() - time.getTimeInMillis() >= 59 * 1000 * 60) {
            Log.d("ttt","3");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(now.getTimeInMillis() - time.getTimeInMillis());
            timePost.setText("اخر تحديث "+(calendar.getTimeInMillis() / (59 * 1000 * 60)) + " " + context.getResources().getString(R.string.hours_ago));
        } else if (now.getTimeInMillis() - time.getTimeInMillis() > 1000 * 60) {
            Log.d("ttt","4");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(now.getTimeInMillis() - time.getTimeInMillis());
            timePost.setText("اخر تحديث "+calendar.get(Calendar.MINUTE) + " " + context.getResources().getString(R.string.minutes_ago));
        } else if(now.getTimeInMillis() - time.getTimeInMillis() < 1000 * 60){
            Log.d("ttt","5");

            timePost.setText("اخر تحديث الان");
        }
    }
    private void copy(String text){
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText( getContext(), "تم نسخ"+text+"للتواصل", Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText(text,text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText( getContext(), "تم نسخ"+text+"للتواصل", Toast.LENGTH_SHORT).show();

        }
    }
    public void closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        }
    }

}