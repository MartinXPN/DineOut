package com.dineoutmobile.dineout.databasehelpers;


import com.dineoutmobile.dineout.util.models.RestaurantFullInfo;

import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;


public interface DataTransferAPI {

    @GET("/users/{user}")//here is the other url part.best way is to start using /
    void getFeed(@Path("user") String user, Callback<RestaurantFullInfo> response);
    //string user is for passing values from edittext for eg: user=basil2style,google
    //response is the response from the server which is now in the POJO
}
