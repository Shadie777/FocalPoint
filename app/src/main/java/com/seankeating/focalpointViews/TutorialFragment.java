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
public class TutorialFragment extends Fragment {

    //argument key for the page num it represents
    public static final String ARG_PAGE = "page";
    private static String title;

    //this fragments page number
    private int mPageNumber;

    public static TutorialFragment create(int pageNumber){
       TutorialFragment fragment = new TutorialFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    public TutorialFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        title = getArguments().getString("someTitle");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tutorial_section_fragment, container, false);
        TextView textView = (TextView) view.findViewById(R.id.tutorialText);
        textView.setText(mPageNumber + " -- " + title);
        return view;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
}
