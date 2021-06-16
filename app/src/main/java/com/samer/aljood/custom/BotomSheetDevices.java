package com.samer.aljood.custom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.samer.aljood.R;

import com.samer.aljood.model.Device;
import com.samer.aljood.view.Chat;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rd.PageIndicatorView;

import java.util.List;

public class BotomSheetDevices extends BottomSheetDialogFragment {

    Device device;
TextView name,isAvilable,discrib;
    ViewPager vp_images;
    PageIndicatorView mPageIndicatorView;
Button button;
    public  BotomSheetDevices(Device device){
        this.device=device;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return   inflater.inflate(R.layout.bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
name=view.findViewById(R.id.txt_name);
        isAvilable=view.findViewById(R.id.txt_is_available);
        name=view.findViewById(R.id.txt_name);
        discrib=view.findViewById(R.id.txt_discribe);
        vp_images=view.findViewById(R.id.vp_images);
        mPageIndicatorView=view.findViewById(R.id.mPageIndicatorView);
        button=view.findViewById(R.id.btn_continue);
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        getActivity().startActivity(new Intent(getContext(), Chat.class));
    }
});
        vp_images.setPageTransformer(true, new CubeInRotationTransformation());
        name.setText(device.getName());

        if(device.isAvailable){
            isAvilable.setText("متوفر");
            isAvilable.setTextColor(getContext().getResources().getColor(R.color.green));
        }else{
            isAvilable.setText("غير متوفر");
            isAvilable.setTextColor(getContext().getResources().getColor(R.color.red));
        }

        discrib.setText(device.describe);


        DeviceImagesAdapter playgroundImagesAdapter = new DeviceImagesAdapter(getContext(), device.images);
        vp_images.setAdapter(playgroundImagesAdapter);

        mPageIndicatorView.setViewPager(vp_images);

    }


    public class CubeInRotationTransformation implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {

            page.setCameraDistance(20000);


            if (position < -1) {     // [-Infinity,-1)
                // This page is way off-screen to the left.
                page.setAlpha(0);

            } else if (position <= 0) {    // [-1,0]
                page.setAlpha(1);
                page.setPivotX(page.getWidth());
                page.setRotationY(90 * Math.abs(position));

            } else if (position <= 1) {    // (0,1]
                page.setAlpha(1);
                page.setPivotX(0);
                page.setRotationY(-90 * Math.abs(position));

            } else {    // (1,+Infinity]
                // This page is way off-screen to the right.
                page.setAlpha(0);

            }
        }
    }

    private class DeviceImagesAdapter extends PagerAdapter {

        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private List<String> mImagesUrls;


        DeviceImagesAdapter(Context context, List<String> urls) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mImagesUrls = urls;
        }


        public void clear() {
            mImagesUrls.clear();
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return mImagesUrls.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.devices_image, container, false);

            final ImageView img_View = itemView.findViewById(R.id.img_image);

            Glide.with(mContext).load(mImagesUrls.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .error(R.drawable.logo_jood)
                    .into(img_View);



            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }
}
