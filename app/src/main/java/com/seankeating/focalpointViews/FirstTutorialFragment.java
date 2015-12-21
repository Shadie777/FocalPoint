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


    public static FirstTutorialFragment create(String text){
       FirstTutorialFragment fragment = new FirstTutorialFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        fragment.setArguments(b);

        return fragment;
    }

    public FirstTutorialFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_tutorial_fragment, container, false);

        //TextView textView = (TextView) view.findViewById(R.id.tutorialText);
        //textView.setText("this is the first frag");
        return view;
    }
}
