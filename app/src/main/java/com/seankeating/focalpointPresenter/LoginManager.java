package com.seankeating.focalpointPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.seankeating.focalpoint.R;

/**
 * Created by Sean on 04/12/2015.
 */
public class LoginManager extends Fragment {

    private CallbackManager callBackManager;

    public LoginManager() {

    }

    //handles callback from facebook api
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {

        //what happens if login is successful
        @Override
        public void onSuccess(LoginResult loginResult) {

        }

        //what happens if login is cancelled
        @Override
        public void onCancel() {
            Toast.makeText(getActivity(), "Login attempt canceled",
                    Toast.LENGTH_LONG).show();

        }

        //what happens if login has an error
        @Override
        public void onError(FacebookException error) {
            Toast.makeText(getActivity(), "Error",
                    Toast.LENGTH_LONG).show();
        }
    };


    //initialises facebook api and allows for callback to be created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callBackManager = CallbackManager.Factory.create();
    }


    //determines which view this fragment is connected to
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        return view;
    }

    //what happens when the view is created
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // find fb login button by id and assign it to variable
        LoginButton login_button = (LoginButton) view.findViewById(R.id.login_button);
        login_button.setFragment(this); //set login button as a fragment

        login_button.registerCallback(callBackManager, callback); // register callback to button

    }


    //results of login
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callBackManager.onActivityResult(requestCode, resultCode, data);
    }


}


