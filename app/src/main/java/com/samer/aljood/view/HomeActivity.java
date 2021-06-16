package com.samer.aljood.view;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.samer.aljood.R;
import com.samer.aljood.adapter.PagerAdapter;
import com.samer.aljood.fragment.ContactLenses;
import com.samer.aljood.fragment.Devices;
import com.samer.aljood.fragment.Lenses;
import com.samer.aljood.fragment.SidebarDrawer;
import com.samer.aljood.model.User;
import com.samer.aljood.utils.Constans;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter adapter;
    SidebarDrawer mSidebarDrawer;
    Toolbar mCustomToolbar;
    DrawerLayout drawerLayout;
ImageButton btnDrawer;
User user;
    private static final float MIN_SCALE = 0.75f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_home);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mCustomToolbar = findViewById(R.id.toolbar_condition);
btnDrawer=findViewById(R.id.btn_drawer);
drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mSidebarDrawer = (SidebarDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        mSidebarDrawer.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mCustomToolbar);
        //getUserInfo();
            viewPager =  findViewById(R.id.pager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
 viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage( View view, float position) {

            }
        });

        btnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                mSidebarDrawer.openDrawer((CoordinatorLayout) findViewById(R.id.cl_container)
                        ,(View)findViewById(R.id.fragment_navigation_drawer),(ImageButton) findViewById(R.id.btn_drawer)
                , user);

            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        mSidebarDrawer.closeDrawer();

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new PagerAdapter(getSupportFragmentManager(), this) ;
        adapter.addFragment(new Devices(Constans.FIREBASE_DB_Devices), "الأجهزة");
        adapter.addFragment(new Devices(Constans.FIREBASE_DB_FRAMES), "الاطارات");
        adapter.addFragment(new Lenses(), "العدسات");
        adapter.addFragment(new ContactLenses(), "العدسات الاصقة");
        adapter.addFragment(new Devices(Constans.FIREBASE_DB_Accessories), "الاكسسوارات");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}