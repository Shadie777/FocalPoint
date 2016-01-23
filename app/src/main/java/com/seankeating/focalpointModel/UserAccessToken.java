package com.seankeating.focalpointModel;

import com.facebook.AccessToken;
/**
 * Created by Sean on 22/01/2016.
 */
public class UserAccessToken {
AccessToken accessToken;

    public UserAccessToken(AccessToken accessToken){
        this.accessToken = accessToken;
    }

    public AccessToken getAccessToken(){return accessToken;}
}
