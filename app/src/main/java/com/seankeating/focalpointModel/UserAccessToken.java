package com.seankeating.focalpointModel;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

/**
 * Created by Sean on 22/01/2016.
 */
public class UserAccessToken {
static String accessToken;

    public UserAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    public static String getAccessToken(){

        return accessToken;}
}
