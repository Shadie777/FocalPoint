package com.seankeating.focalpointPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.seankeating.focalpoint.R;
import com.seankeating.focalpointModel.UserAccessToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by Sean on 04/12/2015.
 */
public class LoginManager extends Fragment{
    private TextView text;
    private CallbackManager callBackManager;
    public static  AccessToken accessToken;
   // private static final List<String> PERMISSIONS = Arrays.asList("");

   public boolean loginSuccessful;
    public LoginManager(){

    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();

            if(profile !=null){
                String accessToken= loginResult.getAccessToken().getToken();
                new UserAccessToken(accessToken);

            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(getActivity(),   "Login attempt canceled",
                    Toast.LENGTH_LONG).show();

        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(getActivity(),   "Error",
                    Toast.LENGTH_LONG).show();
        }
    };



    @Override
    public void onCreate(  @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callBackManager= CallbackManager.Factory.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // find fb login button by id and assign it to variable
        LoginButton login_button = (LoginButton) view.findViewById(R.id.login_button);
        login_button.setFragment(this); //set login button as a fragment
      // login_button.setReadPermissions(Arrays.asList("public_profile,user_events,user_actions.music,user_likes,rsvp_event"));
        login_button.registerCallback(callBackManager, callback);

        text = (TextView) view.findViewById(R.id.text_details);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callBackManager.onActivityResult(requestCode, resultCode, data);



    }



}


