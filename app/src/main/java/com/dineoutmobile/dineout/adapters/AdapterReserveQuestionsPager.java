package com.dineoutmobile.dineout.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.dineoutmobile.dineout.R;
import com.dineoutmobile.dineout.util.CacheUtil;
import com.dineoutmobile.dineout.util.NumberPicker;
import com.dineoutmobile.dineout.util.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


public class AdapterReserveQuestionsPager extends PagerAdapter {

    private Context context;
    private static final int NUMBER_OF_QUESTION_PAGES = 2;
    private static final int MAX_NUMBER_OF_PEOPLE = 11;
    private static final int MAX_NUMBER_OF_DAYS = 4;
    private static final int MAX_NUMBER_OF_HOURS = 48;
    String[] people = new String[MAX_NUMBER_OF_PEOPLE];
    String[] date = new String[MAX_NUMBER_OF_DAYS];
    String[] time = new String[MAX_NUMBER_OF_HOURS];



    public AdapterReserveQuestionsPager(Context context) {

        this.context = context;

        /// initialize person picker values
        for( int i=1; i < MAX_NUMBER_OF_PEOPLE; i++ )
            people[i-1] = String.valueOf( i );
        people[ MAX_NUMBER_OF_PEOPLE - 1 ] = String.valueOf( MAX_NUMBER_OF_PEOPLE ) + "+";

        /// initialize date picker values
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
        for (int i = 0; i < MAX_NUMBER_OF_DAYS; i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, i);
            date[i] = sdf.format(calendar.getTime());
        }

        /// initialize time picker values
        for (int i = 0; i < MAX_NUMBER_OF_HOURS; i++)
            time[i] = String.valueOf( i / 2 ) + ( i%2 == 0 ? ":00" : ":30" );
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {

        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup layout;
        if( position == 0 ) {
            layout = (ViewGroup) inflater.inflate(R.layout.reserve_question_people_date_time_pickers, collection, false);

            /// initialize person picker
            final NumberPicker numberOfPeople = (NumberPicker) layout.findViewById( R.id.number_of_people_picker);
            assert numberOfPeople != null;
            numberOfPeople.setMinValue( 1 );
            numberOfPeople.setMaxValue( MAX_NUMBER_OF_PEOPLE );
            numberOfPeople.setWrapSelectorWheel( false );
            numberOfPeople.setDisplayedValues( people );
            numberOfPeople.setValue( CacheUtil.getCache( context, Util.Tags.SHARED_PREFS_RESERVE_PEOPLE, 2 ) );
            numberOfPeople.setOnValueChangedListener( new OnNumberPickerValueChangeListener( Util.Tags.SHARED_PREFS_RESERVE_PEOPLE ) );


            /// initialize date picker
            final NumberPicker datePicker = (NumberPicker) layout.findViewById( R.id.date_picker);
            assert datePicker != null;
            datePicker.setMinValue( 1 );
            datePicker.setMaxValue( MAX_NUMBER_OF_DAYS );
            datePicker.setWrapSelectorWheel( false );
            datePicker.setDisplayedValues( date );
            datePicker.setValue( CacheUtil.getCache( context, Util.Tags.SHARED_PREFS_RESERVE_DATE, 2 ) );
            datePicker.setOnValueChangedListener( new OnNumberPickerValueChangeListener( Util.Tags.SHARED_PREFS_RESERVE_DATE ) );

            /// initialize time picker
            final NumberPicker timePicker = (NumberPicker) layout.findViewById( R.id.time_picker);
            assert timePicker != null;
            timePicker.setMinValue( 1 );
            timePicker.setMaxValue( MAX_NUMBER_OF_HOURS );
            timePicker.setWrapSelectorWheel( false );
            timePicker.setDisplayedValues( time );
            timePicker.setValue( CacheUtil.getCache( context, Util.Tags.SHARED_PREFS_RESERVE_TIME, 40 ) );
            timePicker.setOnValueChangedListener( new OnNumberPickerValueChangeListener( Util.Tags.SHARED_PREFS_RESERVE_TIME ) );
        }
        else {
            layout = (ViewGroup) inflater.inflate(R.layout.reserve_question_phone_name, collection, false);

            final EditText userName = (EditText) layout.findViewById( R.id.user_name );
            assert userName != null;
            if( userName.getText().length() == 0 )
                userName.setText(CacheUtil.getCache( context, Util.Tags.SHARED_PREFS_USER_NAME, "" ) );
            userName.addTextChangedListener( new OnTextChangeListener( Util.Tags.SHARED_PREFS_USER_NAME ) );

            final EditText userPhoneNumber = (EditText) layout.findViewById( R.id.user_phone_number );
            assert userPhoneNumber != null;
            if( userPhoneNumber.getText().length() == 0 )
                userPhoneNumber.setText(CacheUtil.getCache( context, Util.Tags.SHARED_PREFS_USER_PHONE, "" ) );
            userPhoneNumber.addTextChangedListener( new OnTextChangeListener( Util.Tags.SHARED_PREFS_USER_PHONE ) );
        }

        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return NUMBER_OF_QUESTION_PAGES;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }



    public class OnTextChangeListener implements TextWatcher {

        private String key;
        public OnTextChangeListener( String sharedPrefKey ) {
            key = sharedPrefKey;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            CacheUtil.setCache( context, key, s.toString() );
        }
    }


    public class OnNumberPickerValueChangeListener implements android.widget.NumberPicker.OnValueChangeListener {

        private String key;
        public OnNumberPickerValueChangeListener( String SharedPrefKey ) {
            key = SharedPrefKey;
        }

        @Override
        public void onValueChange(android.widget.NumberPicker picker, int oldVal, int newVal) {
            CacheUtil.setCache( context, key, newVal );
        }
    }
}