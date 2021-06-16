package com.samer.aljood.utils;

import android.app.Activity;
import android.content.Intent;

import com.samer.aljood.R;
import com.samer.aljood.view.Chat;

import cn.pedant.SweetAlert.SweetAlertDialog;

    public class Constans {
    public static final String FIREBASE_DB_Devices = "devices";
    public static final String FIREBASE_DB_Accessories = "accessories";
        public  static SweetAlertDialog  DialogAvailable;
        public static final String FIREBASE_DB_USERS_TABLE = "users";
        public static final String FIREBASE_DB_FRAMES = "frames";

        public static final String FIREBASE_DB_Lenses = "lenses";
        public static final String FIREBASE_DB_contactLenses = "contactLenses";
        public static final String ADMIN_TOKEN = "admin_token";

        public static final String SERVER_KEY = "AAAAv2R3sH4:APA91bH5hFbagn3FMa4b_Y9hXneaWLzglgSKSqP-PA6koDRB1cQw0r1iKxvjeCUXOOsMpU4eMsdg-5s-iC5ApRuQVWD4iOgeKgSxP9LeHMaNab8hgPC1Sadqu8slucxf11FCUo9nFi4P";
        public static final String CONTANT_TYPE = "application/json";





    public  static void bDiloge(final Activity activity, String title, String text , int img){


        if (img==R.drawable.ic_baseline_check_circle_24){
            DialogAvailable= new SweetAlertDialog(activity, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
            DialogAvailable. setTitleText(title);
            DialogAvailable. setContentText(text);
            DialogAvailable. setCancelable(true);
            DialogAvailable. setCustomImage(img);


            DialogAvailable.setConfirmButton("طلب و استفسار", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    activity.startActivity(new Intent(activity.getApplicationContext(), Chat.class));
DialogAvailable.dismiss();
                }
            });

            } else if(img==R.drawable.ic_cross){
            DialogAvailable= new SweetAlertDialog(activity, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
            DialogAvailable. setTitleText(title);
            DialogAvailable. setContentText(text);
            DialogAvailable. setCancelable(true);
            DialogAvailable. setCustomImage(img);

            DialogAvailable.setConfirmText("اغلاق");

        }
        if (text.equals("")){
            DialogAvailable= new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE);
        }
        DialogAvailable. show();

     }
        public  static void bDilogeStop(){
        if (DialogAvailable!=null){
            DialogAvailable.dismiss();
            DialogAvailable.cancel();
        }
        }

}
