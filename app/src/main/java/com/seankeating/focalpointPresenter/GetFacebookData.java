package com.seankeating.focalpointPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONObject;

/**
 * Created by Sean on 05/01/2016.
 */
public class GetFacebookData {
String get_name;
String url = "/search?type=place&q=*&center={coordinate}&distance={distance}";

public static AccessToken accessToken;


    GetFacebookData(){
    }


    public static void storeAccessToken(AccessToken accessToken1){
        accessToken = accessToken1;
    }

    public void getUserInformation() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/{user-id}/events",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                    }
                }
        ).executeAsync();
    }

    public static void getInformation(double lat, double lon) {

    }
}
//GraphRequest request = GraphRequest.newMeRequest(
//        accessToken,
//        new GraphRequest.GraphJSONObjectCallback() {
//@Override
//public void onCompleted(
//        JSONObject object,
//        GraphResponse response) {
//        // Application code
//        Log.e("GraphResponse", "-------------" + response.toString());
//        }
//        });
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,link");