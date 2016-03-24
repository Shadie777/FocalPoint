package com.seankeating.focalpointViews;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;


import com.facebook.FacebookSdk;
import com.seankeating.focalpoint.R;
import com.seankeating.focalpointPresenter.LoginManager;
import com.seankeating.focalpointPresenter.MapsActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
public class LoginActivity extends FragmentActivity {
  private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            loginManager = new LoginManager();

            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, loginManager).commit();
        } else {
            // Or set the fragment from restored state info
            loginManager = (LoginManager) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if ( resultCode == RESULT_OK && null != data) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            finish();
        }
    }
}