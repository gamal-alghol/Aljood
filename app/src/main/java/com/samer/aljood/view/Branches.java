package com.samer.aljood.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.samer.aljood.R;
import com.samer.aljood.adapter.BrancheAdabter;
import com.samer.aljood.model.Branche;

import java.util.ArrayList;

public class Branches extends AppCompatActivity {
RecyclerView recyclerViewBranche;
ArrayList<Branche> arrayListBranche;
BrancheAdabter brancheAdabter;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);
    recyclerViewBranche=findViewById(R.id.recyclerView_branches);
        back=findViewById(R.id.btn_back);

        arrayListBranche=new ArrayList();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
Branche branche1=new Branche("الفرع الرئيسي","غزة-مفترق ضبيط-برج الجوهرة","08-2889939 / 059-7166606");
arrayListBranche.add(branche1);
        Branche branche2=new Branche("الفرع الثاني","خانيونس-شارع السنية-بجوار كفي شوب الأماكن","059-8188870");
        arrayListBranche.add(branche2);

        recyclerViewBranche.setLayoutManager(getLinearManger(LinearLayoutManager.VERTICAL));
        brancheAdabter = new BrancheAdabter(getApplicationContext(),arrayListBranche);
        recyclerViewBranche.setAdapter(brancheAdabter);
    }
    private LinearLayoutManager getLinearManger(int orientation) {
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(orientation);
        return llm;
    }
}