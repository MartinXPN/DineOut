package com.dineoutmobile.dineout.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.adapters.AdapterReserveQuestionsPager;
import com.dineoutmobile.dineout.util.DynamicHeightViewPager;
import com.viewpagerindicator.CirclePageIndicator;

public class FragmentReserveQuestions extends Fragment {

    AdapterReserveQuestionsPager adapterReserveQuestionsPager;
    OnRestaurantReservedListener onRestaurantReservedListener;

    public interface OnRestaurantReservedListener {
        void onRestaurantReserved();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRestaurantReservedListener = (OnRestaurantReservedListener) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        adapterReserveQuestionsPager = new AdapterReserveQuestionsPager( getActivity() );

        final View rootView = inflater.inflate( R.layout.fragment_reserve_questions, container, false );
        final DynamicHeightViewPager questions = (DynamicHeightViewPager) rootView.findViewById( R.id.questions_pager );
        questions.setAdapter( adapterReserveQuestionsPager );

        /// initialize restaurant photo page indicator
        final CirclePageIndicator pageIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        assert pageIndicator != null;
        pageIndicator.setViewPager( questions );


        final Button reserveButton = (Button) rootView.findViewById( R.id.reserve_restaurant_button );
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( questions.getCurrentItem() == 0 ) {
                    questions.setCurrentItem( 1 );
                }
                else {

                    rootView.setVisibility( View.GONE );
                    onRestaurantReservedListener.onRestaurantReserved();
                }
            }
        });


        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return rootView;
    }
}
