package com.seankeating.focalpointViews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seankeating.focalpoint.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginButtonFragment extends Fragment {

    public LoginButtonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.login_section_fragment, container, false);
        return view;
    }

}
