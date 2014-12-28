package io.github.scola.birthday;

import java.util.UUID;

import io.github.scola.birthday.R;
import io.github.scola.birthday.preferences.SummaryEditTextPerference;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class BirthdayFragment extends PreferenceFragment 
				implements OnSharedPreferenceChangeListener{
	
	private static final String TAG = "BirthdayFragment";
	
    Birthday mBirthday;
    private static final String KEY_NAME_PREFERENCE = "name";
    private static final String KEY_LUNAR_PREFERENCE = "lunar";
    private static final String KEY_METHOD_PREFERENCE = "method";
    private static final String KEY_REPEAT_PREFERENCE = "repeat";
    private static final String KEY_DATE_PREFERENCE = "date";
    private static final String KEY_TIME_PREFERENCE = "time";
    private static final String KEY_EARLY_PREFERENCE = "early";
    
    //private static final String[] PERFERENCE_KEYS = {"name", "lunar", "method", "repeat", "date", "time", "early"};
    
    public static final String EXTRA_BIRTHDAY_ID = "birthday.BIRTHDAY_ID";
    
    public static BirthdayFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_BIRTHDAY_ID, crimeId);

        BirthdayFragment fragment = new BirthdayFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mBirthday = new Birthday();
        addPreferencesFromResource(R.xml.preferences);
        UUID birthdayId = (UUID)getArguments().getSerializable(EXTRA_BIRTHDAY_ID);
        mBirthday = BirthdayLab.get(getActivity()).getBirthday(birthdayId);
        SummaryEditTextPerference preference = (SummaryEditTextPerference)findPreference(KEY_NAME_PREFERENCE);
        preference.setSummary(mBirthday.getName());
        //preference.getDialog()
        //preference.getEditText().setText("fuck");
    }
    
    @Override
    public void onResume(){
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
            .registerOnSharedPreferenceChangeListener(this);
        //updatePreference(KEY_EDIT_TEXT_PREFERENCE);
    }
 
    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
            .unregisterOnSharedPreferenceChangeListener(this);        

    	Preference preference = findPreference(KEY_NAME_PREFERENCE);
        String summary = (preference).getSummary().toString().trim();
        mBirthday.setName(summary);

    }
	
	//@Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        updatePreference(key);
    }
	
	private void updatePreference(String key){
		Log.d(TAG, "Preference changed");
        Preference preference = findPreference(key);
        if (preference instanceof CheckBoxPreference){
        	CheckBoxPreference checkBoxPreference =  (CheckBoxPreference)preference;
            Boolean isChecked = checkBoxPreference.isChecked();
            if (key.equals(KEY_LUNAR_PREFERENCE)){
            	mBirthday.setIsLunar(isChecked);
            }else{
            	mBirthday.setIsEarly(isChecked);
            }
        } else {
        	String summary = (preference).getSummary().toString().trim();        	
        	if(summary.length() == 0) return;
    		if (key.equals(KEY_NAME_PREFERENCE)) {
    			Log.d(TAG, "setName when input name: " + summary);
    			mBirthday.setName(summary);
    		}    		
    		if (key.equals(KEY_DATE_PREFERENCE)) {
    			mBirthday.setDate(summary);
    		}
    		if (key.equals(KEY_TIME_PREFERENCE)) {
    			mBirthday.setTime(summary);
    		}
    		if (key.equals(KEY_REPEAT_PREFERENCE)) {
    			mBirthday.setRepeat(Integer.parseInt(summary));
    		}
    		if (key.equals(KEY_METHOD_PREFERENCE)) {
    			mBirthday.setMethod(summary);
    		}
        }

    }
    
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.xml.preferences, parent, false);
        
        return v; 
    }
*/    
}