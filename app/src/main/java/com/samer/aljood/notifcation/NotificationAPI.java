package com.samer.aljood.notifcation;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.samer.aljood.utils.Constans.CONTANT_TYPE;
import static com.samer.aljood.utils.Constans.SERVER_KEY;

public interface NotificationAPI {
    @Headers({"Authorization: key="+SERVER_KEY, "Content-Type:"+CONTANT_TYPE})
    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);


}
