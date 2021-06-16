package com.samer.aljood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samer.aljood.R;
import com.samer.aljood.custom.BotomSheetDevices;
import com.samer.aljood.model.Device;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.flyco.labelview.LabelView;

import java.util.List;

public class DeviceAdapter  extends FirestoreRecyclerAdapter<Device, DeviceAdapter.ViewHolder> {
    private AdapterView.OnItemClickListener onItemClickListener;
    private Context context;
    FragmentManager fragmentManager;
    public DeviceAdapter(FirestoreRecyclerOptions<Device> deviceFirestoreRecyclerOptions, Context context, FragmentManager fragmentManager) {
        super(deviceFirestoreRecyclerOptions);
        this.context=context;
        this.fragmentManager=fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_devices, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Device model) {
        final String idNews=this.getSnapshots().getSnapshot(position).getId();
        holder.bind(model);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageDevice;
        TextView nameDevice;
        LabelView lbl_promotion;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageDevice = itemView.findViewById(R.id.img_device);
            nameDevice = itemView.findViewById(R.id.txt_name);
            lbl_promotion = itemView.findViewById(R.id.lbl_promotion);


        }
        void  bind(final Device device){
            String imgUrl="";
nameDevice.setText(device.getName());
            if (device.isPromotion) {
                lbl_promotion.setVisibility(View.VISIBLE);

            }else {
                lbl_promotion.setVisibility(View.GONE);
            }


            if (!isEmpty(device.images)) {
                imgUrl = device.images.get(0);
            }
            Glide.with(context).load(imgUrl)
                    .placeholder(R.drawable.logo_jood)
                    .error(R.drawable.logo_jood)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(imageDevice);

            imageDevice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BotomSheetDevices addPhotoBottomDialogFragment =
                          new BotomSheetDevices(device);
                    addPhotoBottomDialogFragment.show(fragmentManager,
                           " BotomSheetDevices.TAG");
                }
            });
        }
        public  <T> boolean isEmpty(List<T> list) {
            return list == null || list.size() == 0;
        }
    }


}
