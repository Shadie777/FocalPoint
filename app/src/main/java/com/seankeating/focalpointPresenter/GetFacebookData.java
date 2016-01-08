package com.seankeating.focalpointPresenter;

import android.os.Bundle;
import android.util.Log;

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
    GetFacebookData(){
    }

    public static void getUserInformation(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log.e("GraphResponse", "-------------" + response.toString());
                    }
                });
        Bundle parameters = new Bundle();
       parameters.putString("fields", "id,name,link");
        request.setParameters(parameters);
     request.executeAsync();
    }


}
