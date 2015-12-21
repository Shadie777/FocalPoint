package com.seankeating.focalpointViews;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seankeating.focalpoint.R;


public class SecondTutorialFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_second__tutorial_, container, false);

        TextView tv = (TextView) v.findViewById(R.id.tutorialText2);
        tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static SecondTutorialFragment create(String text) {

        SecondTutorialFragment f = new SecondTutorialFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

}
