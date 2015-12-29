package com.seankeating.focalpointPresenter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;


import com.seankeating.focalpointPresenter.LoginManager;

public class LoginActivity extends FragmentActivity {
  private LoginManager login_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            login_fragment = new LoginManager();

            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, login_fragment).commit();
        } else {
            // Or set the fragment from restored state info
            login_fragment = (LoginManager) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //loginfragment.onActivityResult(requestCode, resultCode, data);
    }
}