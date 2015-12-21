package com.seankeating.focalpointViews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seankeating.focalpoint.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Second_Tutorial_Fragment extends Fragment {


    public Second_Tutorial_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second__tutorial_, container, false);
    }

}
