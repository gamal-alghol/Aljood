package com.samer.aljood.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.samer.aljood.R;
import com.samer.aljood.utils.Constans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sha.kamel.multitogglebutton.MultiToggleButton;
import com.sha.kamel.multitogglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.Locale;


public class Lenses extends Fragment {
    View view;
    ChipGroup chipGroupCategory;
    Button chipGroupCategoryBtn,search;
    AutoCompleteTextView spnSph,spnCyl;
    Chip chip;
    ArrayAdapter adapter;
    ArrayList<String> arrayListSph,arrayListSph_pos,arrayListSph_niv;
    ArrayList<String> arrayListCyl;
    float selectedCyl,selectedSph;
    TextView isAvilableTxtView;
    String groupCategoryString=null;
    ProgressBar progressBarCategory,progressBarSearch;
    boolean isAvilable;
    MultiToggleButton multiToggleButtonSph,multiToggleButtonCyl;
    public Lenses() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        super.onCreateView(inflater, container, savedInstanceState);
         view= inflater.inflate(R.layout.fragment_lenses, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chipGroupCategoryBtn =view.findViewById(R.id.btn_category);
        chipGroupCategory = view.findViewById(R.id.chip_group_category);
        spnSph =view.findViewById(R.id.spn_sph);
        spnCyl =view.findViewById(R.id.spn_cyl);
        progressBarSearch=view.findViewById(R.id.progressBar_search);
        progressBarCategory=view.findViewById(R.id.progressBar_Catygory);
        isAvilableTxtView =view.findViewById(R.id.txt_is_available);
        search=view.findViewById(R.id.search);
        multiToggleButtonSph =view.findViewById(R.id.mtb_shp);
        multiToggleButtonCyl=view.findViewById(R.id.mtb_cyl);
        multiToggleButtonSph.setOnItemSelectedListener(new ToggleButton.OnItemSelectedListener() {
            @Override
            public void onSelected(ToggleButton toggleButton, View item, int position, String label, boolean selected) {
spnSph.setText(label);
            }
        });
        multiToggleButtonCyl.setOnItemSelectedListener(new ToggleButton.OnItemSelectedListener() {
            @Override
            public void onSelected(ToggleButton toggleButton, View item, int position, String label, boolean selected) {
                spnCyl.setText(label);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getCategory();
        creatSpinnerSph();
        creatSpinnerCyl();
        Listeners();

    }



    private void creatSpinnerCyl() {
        arrayListCyl=new ArrayList<>();
        for(float i = (float) 0; i>=-6; i= (float) (i-0.25)){
            String result;
            if(i==0) {
                result = String.format(Locale.US, "%.2f", i);
            }else {
                result = String.format(Locale.US, "%+.2f", i);

            }
            arrayListCyl.add(result);
        }
         adapter = new ArrayAdapter(getContext(), R.layout.txt_spinner, arrayListCyl);
        spnCyl.setAdapter(adapter);
    }

    private void creatSpinnerSph() {
        arrayListSph=new ArrayList<>();
        arrayListSph_niv=new ArrayList<>();
        arrayListSph_pos=new ArrayList<>();


        for(float i =20;i>=-20;i= (float) (i-0.25)){
            String result;
            if(i==0) {
                result = String.format(Locale.US, "%.2f", i);
            }else {
                result = String.format(Locale.US, "%+.2f", i);

            }

            arrayListSph.add(result);
            if(i>=0){
                Log.d("ttt","i>0>>>"+i);
                arrayListSph_pos.add(result);
            }else if(i<0){
                arrayListSph_niv.add(result);

            }
        }
   //      adapter = new ArrayAdapter(getContext(), R.layout.txt_spinner, arrayListSph);
     //   spnSph.setAdapter(adapter);
    }

    private void getCategory() {
        FirebaseFirestore.getInstance().collection(Constans.FIREBASE_DB_Lenses).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for( QueryDocumentSnapshot queryDocumentSnapshots1 :task.getResult()){
                        String id = queryDocumentSnapshots1.getId();
                        creatChip(id);
                    }
                    progressBarCategory.setVisibility(View.GONE);
                }
            }

        });
    }

    private void Listeners() {
        chipGroupCategory.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip=group.findViewById(checkedId);
                if (chip!=null) {
                    Log.d("ttt",chip.getText()+"");

                    if(chip.getText().equals("CR-39 1.49 -/-")||chip.getText().equals("CR-39 1.61 -/-")){
                        Log.d("ttt",+arrayListSph_niv.size()+"");

                        adapter = new ArrayAdapter(getContext(), R.layout.txt_spinner, arrayListSph_niv);
                        spnSph.setAdapter(adapter);
                        chipGroupCategoryBtn.setText(chip.getText());
                        if (chip.getText().toString().contains("1.49")){
                            groupCategoryString="CR-39 1.49";
                        }else{
                            groupCategoryString="CR-39 1.61";
                        }

              }else if(chip.getText().equals("CR-39 1.56 +/-")){
                        Log.d("ttt",+arrayListSph_pos.size()+"");

                        adapter = new ArrayAdapter(getContext(), R.layout.txt_spinner, arrayListSph_pos);
                        spnSph.setAdapter(adapter);
                        chipGroupCategoryBtn.setText(chip.getText());
                        groupCategoryString="CR-39 1.56";

                    }else{
                        Log.d("ttt",+arrayListSph.size()+"");

                        adapter = new ArrayAdapter(getContext(), R.layout.txt_spinner, arrayListSph);
                        spnSph.setAdapter(adapter);
                        chipGroupCategoryBtn.setText(chip.getText());
                        groupCategoryString= (String) chip.getText();

                    }

                }else{
                    chipGroupCategoryBtn.setText(getActivity().getResources().getString(R.string.catygory));
                    groupCategoryString=null;
                }
            }
        });

        chipGroupCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chipGroupCategory.getVisibility()==View.GONE){
                    chipGroupCategory.setVisibility(View.VISIBLE);
                    chipGroupCategoryBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_bottom, 0, 0, 0);
                }else if(chipGroupCategory.getVisibility()==View.VISIBLE){
                chipGroupCategoryBtn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_top, 0,  0, 0);
                    chipGroupCategory.setVisibility(View.GONE);

                }
            }
        });
        spnSph.setSoundEffectsEnabled(true);

spnSph.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedSph= Float.parseFloat((String) parent.getItemAtPosition(position)) ;
        // here is your selected item
    }
});
        spnCyl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCyl= Float.parseFloat((String) parent.getItemAtPosition(position)) ;


            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarSearch.setVisibility(View.VISIBLE);
                if (groupCategoryString != null){
getLense();

            }else {
                    progressBarSearch.setVisibility(View.INVISIBLE);

                    Toast.makeText(getContext(),"يرجى اختيار فئة العدسة",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getLense() {
        FirebaseFirestore.getInstance().collection(Constans.FIREBASE_DB_Lenses)
                .document(groupCategoryString)
                .collection("type")
                .whereEqualTo("available", true).whereEqualTo("cyl", selectedCyl)
                .whereEqualTo("sph", selectedSph)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()) {
                                progressBarSearch.setVisibility(View.INVISIBLE);
selectedSph= (float) 0.0;

                                selectedCyl= (float) 0.0;

                                isAvilable = true;
                                Constans.bDiloge(getActivity(), "متوفرة", "يمكنك طلب العدسات", R.drawable.ic_baseline_check_circle_24);
                            }

                        }
                    }
                });
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isAvilable == false) {
                        progressBarSearch.setVisibility(View.INVISIBLE);
                        Constans.bDiloge(getActivity(),"غير متوفرة","سيتم توفيرها قريبا",R.drawable.ic_cross);
                        isAvilable = false;
                    }
                }
            },1500);

        } catch (Exception e) {
            Log.d("ttt",e.getMessage());
            progressBarSearch.setVisibility(View.INVISIBLE);

        }

        isAvilable = false;
    }
 //   CR-39 1.49
   //      CR-39 1.56
        //CR-39 1.61

    private void creatChip(String id) {
        chip = new Chip(getActivity());
        Log.d("ttt",id);
        if(id.equalsIgnoreCase("CR-39 1.49")||id.equalsIgnoreCase("CR-39 1.61")){
            chip.setText(id+" -/-");
        }else if(id.equalsIgnoreCase("CR-39 1.56")){
            chip.setText(id+" +/-");

        }else{
            chip.setText(id);

        }
        chip.setId(ViewCompat.generateViewId());
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setTextSize(14);
        chip.setCheckable(true);
        chip.setClickable(true);
        chip.setChipStrokeWidth(4);
        chip.setChipCornerRadius((float) 10);
        chip.setCheckedIconVisible(false);
        chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected);
        chip.setChipStrokeColorResource(R.color.bg_chip_state_list);
        chipGroupCategory.addView(chip);
    }

}