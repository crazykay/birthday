package io.github.scola.birthday.preferences;

import io.github.scola.birthday.utils.Util;

import java.lang.reflect.Field;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

public class DatePreference extends DialogPreference {
	private int lastMonth;
    private int lastDay;
    private DatePicker picker=null;

    public static int getHour(String date) {
        String[] pieces=date.split("-");

        return(Integer.parseInt(pieces[0]));
    }

    public static int getMinute(String date) {
        String[] pieces=date.split("-");

        return(Integer.parseInt(pieces[1]));
    }

    public DatePreference(Context ctxt, AttributeSet attrs) {
        super(ctxt, attrs);      

//        setPositiveButtonText(ctxt.getResources().getString(android.R.string.ok));
//        setNegativeButtonText(ctxt.getResources().getString(android.R.string.cancel));
    }
    
    @Override
    protected View onCreateDialogView() {
        picker=new DatePicker(getContext());
        try {
            Field f[] = picker.getClass().getDeclaredFields();
            for (Field field : f) {
            	if (field.getName().equals("mYearSpinner") ||field.getName().equals("mYearPicker")) {
                    field.setAccessible(true);
                    Object yearPicker = new Object();
                    yearPicker = field.get(picker);
                    ((View) yearPicker).setVisibility(View.GONE);
                }
            }
        } 
        catch (SecurityException e) {
            Log.d("ERROR", e.getMessage());
        } 
        catch (IllegalArgumentException e) {
            Log.d("ERROR", e.getMessage());
        } 
        catch (IllegalAccessException e) {
            Log.d("ERROR", e.getMessage());
        }
    	
        return(picker);
    }
    
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        //Date date = new Date();
        int[] month_day = Util.SplitString(getSummary().toString(), "-");
        lastMonth = month_day[0] - 1;
        lastDay = month_day[1];
        
        picker.updateDate(2020, lastMonth, lastDay);
        picker.setCalendarViewShown(false);
        //picker.setCurrentMinute(lastMinute);
        //picker.setIs24HourView(true);
    }
    

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
        	lastMonth=picker.getMonth();
        	lastDay=picker.getDayOfMonth();

            String date=String.format("%02d", lastMonth + 1) + "-" + String.format("%02d", lastDay);

//            if (callChangeListener(date)) {
//                persistString(date);
//            }
            setSummary(date);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return(a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        String time=null;

        if (restoreValue) {
            if (defaultValue==null) {
                time=getPersistedString("00:00");
            }
            else {
                time=getPersistedString(defaultValue.toString());
            }
        }
        else {
            time=defaultValue.toString();
        }

//        lastMonth=getHour(time);
//        lastDay=getMinute(time);
        int[] month_day = Util.SplitString(getSummary().toString(), "-");
        lastMonth = month_day[0] - 1;
        lastDay = month_day[1];
    }
}
