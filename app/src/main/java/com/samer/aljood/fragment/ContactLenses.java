package com.samer.aljood.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.samer.aljood.R;
import com.samer.aljood.annotations.Color;
import com.samer.aljood.annotations.ColorString;
import com.samer.aljood.annotations.ContactLensesString;
import com.samer.aljood.model.ContactLense;
import com.samer.aljood.utils.Constans;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sha.kamel.multitogglebutton.MultiToggleButton;
import com.sha.kamel.multitogglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.Locale;


public class ContactLenses extends Fragment {
    AutoCompleteTextView spnSph,spnColor;
    TextInputLayout spnSphTextInputLayout;
    ChipGroup chipGroupCategory,chipGroupDuration,chipGroupBrin;
    Button chipGroupCategoryBtn,chipGroupDurationBtn,chipGroupsearchBrinBtn,search,searchBrin;
    String groupCategoryString,groupDurationString,groupBrinString=null;
    ArrayAdapter adapter;
    ArrayList<String> arrayListSph;
    ArrayList<String> arrayListColorCosmetic;
    ArrayList<String> arrayListColorMedcal;
    ProgressBar progressBarSearch;
    boolean isAvilable;
    MultiToggleButton multiToggleButtonSph;
    float selectedSph;
    int  selectedColor;
    String StringSelectedColor;
    View view;
    Chip chipChecked;



    public ContactLenses() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_contact_lenses, container, false);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spnSph =view.findViewById(R.id.spn_sph);
        spnColor =view.findViewById(R.id.spn_color);
        chipGroupCategoryBtn =view.findViewById(R.id.btn_category);
        chipGroupsearchBrinBtn =view.findViewById(R.id.btn_brine);
        chipGroupBrin=view.findViewById(R.id.chip_group_brine);
        chipGroupCategory = view.findViewById(R.id.chip_group_category);
        chipGroupDurationBtn =view.findViewById(R.id.btn_duration);
        chipGroupDuration = view.findViewById(R.id.chip_group_duration);
        progressBarSearch=view.findViewById(R.id.progressBar_search);
        spnSphTextInputLayout=view.findViewById(R.id.textInputLayout_sph);
        search=view.findViewById(R.id.search);
        searchBrin=view.findViewById(R.id.search_brine);

    }


    @Override
    public void onStart() {
        super.onStart();
        multiToggleButtonSph =view.findViewById(R.id.mtb_shp_contcat);
        multiToggleButtonSph.setOnItemSelectedListener(new ToggleButton.OnItemSelectedListener() {
            @Override
            public void onSelected(ToggleButton toggleButton, View item, int position, String label, boolean selected) {
                spnSph.setText(label);
            }
        });
        Listeners();
        creatSpinnerSph();
        creatSpinnerColor();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    private void Listeners() {
        spnSph.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSph= Float.parseFloat((String) parent.getItemAtPosition(position)) ;

            }
        });
        spnColor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StringSelectedColor= (String) parent.getItemAtPosition(position);
                    switch (StringSelectedColor) {
                        case ColorString.transparent_lences :
                        selectedColor = Color.transparent_lences;
                            break;
                        case ColorString.grey :
                            selectedColor = Color.grey;
                            break;
                        case ColorString.sterling_grey :
                            selectedColor = Color.sterling_grey;
                            break;
                          case ColorString.blue :
                             selectedColor = Color.blue;
                            break;
                            case ColorString.green :
                            selectedColor = Color.green;
                            break;
                            case ColorString.brouwn :
                            selectedColor = Color.brouwn;
                            break;
                        case ColorString.amethyst :
                            selectedColor = Color.amethyst;
                            break;
                        case ColorString.gemstone_green :
                            selectedColor = Color.gemstone_green;
                            break;
                        case ColorString.turquoise :
                            selectedColor = Color.turquoise;
                            break;
                        case ColorString.honey :
                            selectedColor = Color.honey;
                            break;
                        case ColorString.pure_hazel :
                            selectedColor = Color.pure_hazel;
                            break;

                    }
                Log.d("ttt",selectedColor+"");
                spnColor.setTextColor(getResources().getColor(selectedColor));


            }
        });

        chipGroupCategory.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                chipChecked = view.findViewById(checkedId);

                if(chipChecked != null){
                chipGroupCategoryBtn.setText(chipChecked.getText());
                    switch ((String) chipChecked.getText()) {
                        case ContactLensesString.medicalAr :
                            groupCategoryString = ContactLensesString.medical;
                            spnSphTextInputLayout.setVisibility(View.VISIBLE);
                           // adapter = new ArrayAdapter(getContext(), R.layout.txt_spinner, arrayListColorMedcal);
                            //spnColor.setAdapter(adapter);
                            break;
                        case ContactLensesString.cosmeticAr :
                            groupCategoryString = ContactLensesString.cosmetic;
                            spnSphTextInputLayout.setVisibility(View.INVISIBLE);
                            selectedSph=0;
                       //     adapter = new ArrayAdapter(getContext(), R.layout.txt_spinner, arrayListColorCosmetic);
                     //       spnColor.setAdapter(adapter);
                            break;
                    }
                }
                else {
                    chipGroupCategoryBtn.setText(getActivity().getResources().getString(R.string.catygory));
                    groupCategoryString = null;
                }
            }
        });
        chipGroupDuration.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                chipChecked = view.findViewById(checkedId);

                if (chipChecked != null) {
                    chipGroupDurationBtn.setText(chipChecked.getText());
                    switch ((String) chipChecked.getText()) {
                        case ContactLensesString.monthlyAr :
                            groupDurationString = ContactLensesString.monthly;
                            break;
                        case ContactLensesString.yearlyAr :
                            groupDurationString = ContactLensesString.yearly;
                            break;
                    }
                } else {
                    chipGroupDurationBtn.setText(getActivity().getResources().getString(R.string.duration));
                    groupDurationString = null;
                }
            }
        });
        chipGroupBrin.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                chipChecked = view.findViewById(checkedId);

                if (chipChecked != null) {
                    chipGroupsearchBrinBtn.setText(chipChecked.getText());
                  groupBrinString=chipChecked.getText().toString();
                } else {
                    chipGroupsearchBrinBtn.setText(getActivity().getResources().getString(R.string.brine));
                    groupBrinString = null;
                }
            }
        });
        chipGroupCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChipGroupDisapper(chipGroupCategory,chipGroupCategoryBtn);

            }
        });

        chipGroupDurationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChipGroupDisapper(chipGroupDuration,chipGroupDurationBtn);
            }
        });
        chipGroupsearchBrinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChipGroupDisapper(chipGroupBrin,chipGroupsearchBrinBtn);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarSearch.setVisibility(View.VISIBLE);
                if (groupCategoryString != null&& groupDurationString!=null){
                    getLense();

                }else {
                    progressBarSearch.setVisibility(View.INVISIBLE);

                    Toast.makeText(getContext(),"يرجى اختيار فئة و مدة العدسة",Toast.LENGTH_SHORT).show();
                }
            }
        });
        searchBrin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarSearch.setVisibility(View.VISIBLE);
                if (groupBrinString != null){
                   getBrin();

                }else {
                    progressBarSearch.setVisibility(View.INVISIBLE);

                    Toast.makeText(getContext(),"يرجى اختيار حجم العبوة",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void ChipGroupDisapper(ChipGroup chipGroup,Button Btn) {
        if(chipGroup.getVisibility()==View.GONE){
            chipGroup.setVisibility(View.VISIBLE);
            Btn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_bottom, 0, 0, 0);
        }else if(chipGroup.getVisibility()==View.VISIBLE){
            Btn.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_top, 0,  0, 0);
            chipGroup.setVisibility(View.GONE);

        }
    }
    private void getBrin() {
        FirebaseFirestore.getInstance().collection(Constans.FIREBASE_DB_contactLenses)
                .document("brine")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
      DocumentSnapshot is=  task.getResult();
      if(is.getData().get(groupBrinString).equals(true)){
          Constans.bDiloge(getActivity(), "متوفرة", "يمكنك طلب المحاليل", R.drawable.ic_baseline_check_circle_24);

      }else{
          Constans.bDiloge(getActivity(),"غير متوفرة","سيتم توفيرها قريبا",R.drawable.ic_cross);


      }



                progressBarSearch.setVisibility(View.GONE);

            }
        });
    }

    private void getLense() {

        Log.d("ttt",selectedSph+"");
        FirebaseFirestore.getInstance().collection(Constans.FIREBASE_DB_contactLenses)
        .document(groupCategoryString+"-"+groupDurationString)
        .collection("products")
        .whereEqualTo("available", true)
        .whereEqualTo("sph", selectedSph)
        .whereEqualTo("color",StringSelectedColor)
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
        @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()) {
                                ContactLense contactLense=queryDocumentSnapshot.toObject(ContactLense.class);
                                progressBarSearch.setVisibility(View.INVISIBLE);
                                isAvilable = true;
                                Constans.bDiloge(getActivity(), "متوفرة", "يمكنك طلب العدسات", R.drawable.ic_baseline_check_circle_24);
                            }
                        }else {
                            Log.d("ttt",task.getException()+"");
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
            },1200);

        } catch (Exception e) {
            Log.d("ttt",e.getMessage());
            progressBarSearch.setVisibility(View.INVISIBLE);

        }

        isAvilable = false;
    }

    private void creatSpinnerSph() {
        arrayListSph=new ArrayList<>();
        for(float i =20;i>=-20;i= (float) (i-0.25)){
            String result;
            if(i==0) {
                result = String.format(Locale.US, "%.2f", i);
            }else {
                result = String.format(Locale.US, "%+.2f", i);

            }
            arrayListSph.add(result);
        }
        adapter = new ArrayAdapter(getContext(), R.layout.txt_spinner, arrayListSph);
        spnSph.setAdapter(adapter);
    }
    private void creatSpinnerColor() {
        arrayListColorCosmetic=new ArrayList<>();
        arrayListColorCosmetic.add(ColorString.grey);
        arrayListColorCosmetic.add(ColorString.blue);
        arrayListColorCosmetic.add(ColorString.brouwn);
        arrayListColorCosmetic.add(ColorString.green);
        arrayListColorCosmetic.add(ColorString.gemstone_green);
        arrayListColorCosmetic.add(ColorString.honey);
        arrayListColorCosmetic.add(ColorString.sterling_grey);
        arrayListColorCosmetic.add(ColorString.pure_hazel);
        arrayListColorCosmetic.add(ColorString.turquoise);
        arrayListColorCosmetic.add(ColorString.amethyst);
        arrayListColorCosmetic.add(ColorString.transparent_lences);
        adapter = new ArrayAdapter(getContext(), R.layout.txt_spinner, arrayListColorCosmetic);
        spnColor.setAdapter(adapter);
       /* arrayListColorMedcal=new ArrayList<>();
        arrayListColorMedcal.add(ColorString.transparent_lences);
        arrayListColorMedcal.add(ColorString.pure_hazel);
        arrayListColorMedcal.add(ColorString.grey);
        arrayListColorMedcal.add(ColorString.green);
*/

    }
}