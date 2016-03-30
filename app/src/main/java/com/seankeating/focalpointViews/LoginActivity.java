package com.seankeating.focalpointViews;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.seankeating.focalpoint.R;
import com.seankeating.focalpointPresenter.LoginManager;
import com.seankeating.focalpointPresenter.MapsActivity;


public class LoginActivity extends FragmentActivity {

    //stores class loginManager, which handles the login
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if there is no available instance
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

    public void onClickBtn(View v)
    {
        Intent intent = new Intent(LoginActivity.this, ScreenSliderActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the log in is valid then go on to map
        if (resultCode == RESULT_OK && null != data) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            finish();
        }
    }
}