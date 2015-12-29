package com.seankeating.focalpointPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.seankeating.focalpoint.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sean on 04/12/2015.
 */
public class LoginManager extends Fragment{
    private TextView text;
    private CallbackManager callBackManager;
   // private static final List<String> PERMISSIONS = Arrays.asList("");


    public LoginManager(){

    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();

            if(profile !=null){

                text.setText("Welcome" + profile.getName());
            }
        }

        @Override
        public void onCancel() {
         text.setText("Login attempt canceled");
        }

        @Override
        public void onError(FacebookException error) {
            text.setText("Login Error");
        }
    };

  @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
       callBackManager= CallbackManager.Factory.create();
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_login, container, false);
//        return view;
//    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // find fb login button by id and assign it to variable
        LoginButton login_button = (LoginButton) view.findViewById(R.id.login_button);
        login_button.setFragment(this); //set login button as a fragment
        login_button.setReadPermissions("user_friends");
        login_button.registerCallback(callBackManager, callback);

        text = (TextView) view.findViewById(R.id.text_details);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callBackManager.onActivityResult(requestCode, resultCode, data);
    }

}


