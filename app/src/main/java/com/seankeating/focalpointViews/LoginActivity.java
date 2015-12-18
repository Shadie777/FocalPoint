package com.seankeating.focalpointViews;

import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.seankeating.focalpoint.R;
import com.seankeating.focalpointPresenter.LoginFragment;

public class LoginActivity extends FragmentActivity {
  private LoginFragment login_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            login_fragment = new LoginFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, login_fragment).commit();
        } else {
            // Or set the fragment from restored state info
            login_fragment = (LoginFragment) getSupportFragmentManager()
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