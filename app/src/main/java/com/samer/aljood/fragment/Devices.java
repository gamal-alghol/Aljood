package com.samer.aljood.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.samer.aljood.R;
import com.samer.aljood.adapter.DeviceAdapter;
import com.samer.aljood.model.Device;
import com.samer.aljood.viewModel.DevicesViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class Devices extends Fragment {

    RecyclerView devicesRecycler;
    DevicesViewModel devicesViewModel;
    DeviceAdapter deviceAdapter;
    ShimmerFrameLayout shimmerFrameLayout;

    String category;
    private String id;

    public static Devices newInstance(String id) {
        Devices f = new Devices ();

        Bundle args = new Bundle();
        if(id!= null){
            args.putString("id", id);
        }
        f.setArguments(args);

        return f;
    }

    public Devices(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            id= savedInstanceState.getString("id");
        }
    }

    public Devices(String category){
this.category=category;
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_devices, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (deviceAdapter !=null)
            deviceAdapter.startListening();
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        devicesViewModel = ViewModelProviders.of(this).get(DevicesViewModel.class);
        createRecyclerViewMajors();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        devicesRecycler =view.findViewById(R.id.devices);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.setVisibility(View.VISIBLE);


    }

    @Override
    public void onPause() {
        super.onPause();
        devicesViewModel.getNewsLiveData(category).removeObservers(this);
    }
    private void createRecyclerViewMajors() {

        devicesRecycler.setLayoutManager(getLinearManger(LinearLayoutManager.VERTICAL));
        devicesViewModel.getNewsLiveData(category).observe(getActivity(), new Observer<FirestoreRecyclerOptions<Device>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Device> deviceFirestoreRecyclerOptions) {
                deviceAdapter = new DeviceAdapter(deviceFirestoreRecyclerOptions,getContext(),getActivity().getSupportFragmentManager());
                devicesRecycler.setAdapter(deviceAdapter);
                deviceAdapter.startListening();
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                devicesRecycler.setVisibility(View.VISIBLE);
            }
        });

    }
    private LinearLayoutManager getLinearManger(int orientation) {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(orientation);
        return llm;
    }
}