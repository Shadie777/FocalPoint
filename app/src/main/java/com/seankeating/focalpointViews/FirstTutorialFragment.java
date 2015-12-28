package com.seankeating.focalpointViews;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seankeating.focalpoint.R;


/**
 * Created by Sean on 08/12/2015.
 */
public class FirstTutorialFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.first_tutorial_fragment, container, false);

        return rootView;
    }

}
