package com.example.titancalculator.helper.MainDisplay;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

import com.example.titancalculator.FontSettingsActivity;
import com.example.titancalculator.MainActivity;

import java.util.ArrayList;

public class SettingsApplier {

    public static String current_font_family;
    public static String current_fontsize;
    public static String current_fontstlye;
    public static int color_act,color_fkt,color_fops,color_numbers,color_saves,color_specials,color_display,color_displaytext,color_background;

    public static void setFonts(Context c, ArrayList<View> BTN_ALL){
        current_font_family = PreferenceManager.getDefaultSharedPreferences(c).getString("fontfamily", "monospace");
        current_fontsize = PreferenceManager.getDefaultSharedPreferences(c).getString("fontsize", "20");
        current_fontstlye = PreferenceManager.getDefaultSharedPreferences(c).getString("fontstyle", "normal");

        if(current_fontstlye == null || current_fontstlye.equals("") || current_font_family == null || current_font_family.equals("") || current_fontsize == null || current_fontsize.equals(""))return;

        for(View v: BTN_ALL){
            Float f = 10f;
            if(!current_fontsize.isEmpty() && !current_fontsize.equals("automatic"))f = Float.valueOf(current_fontsize);
            if(current_fontsize.equals("automatic")){
                f = DisplaySetupHelper.getDefaultTextSize(c);
            }
            if(v instanceof Button){
                ((Button)v).setTextSize(TypedValue.COMPLEX_UNIT_SP, DisplaySetupHelper.getDefaultTextSize(c));
                ((Button)v).setTypeface(FontSettingsActivity.getTypeFace(current_font_family,current_fontstlye));
                if(!current_fontsize.equals("automatic"))((TextView)v).setTextSize(f);
            }
            if(v instanceof EditText){
                ((EditText)v).setTextSize(TypedValue.COMPLEX_UNIT_SP, DisplaySetupHelper.getDefaultTextSize(c));
                ((EditText)v).setTypeface(FontSettingsActivity.getTypeFace(current_font_family,current_fontstlye));
                if(!current_fontsize.equals("automatic"))((TextView)v).setTextSize(f);
            }
            if(v instanceof TextView){
                ((TextView)v).setTextSize(TypedValue.COMPLEX_UNIT_SP, DisplaySetupHelper.getDefaultTextSize(c));
                ((TextView)v).setTypeface(FontSettingsActivity.getTypeFace(current_font_family,current_fontstlye));
                if(!current_fontsize.equals("automatic"))((TextView)v).setTextSize(f);
            }

        }
    }

    public static void setColors(Context c){
        color_act = PreferenceManager.getDefaultSharedPreferences(c).getInt("ActColor", 0xffff0000);
        color_fkt = PreferenceManager.getDefaultSharedPreferences(c).getInt("FktColor", 0xffff0000);
        color_fops = PreferenceManager.getDefaultSharedPreferences(c).getInt("FopsColor", 0xffff0000);
        color_numbers = PreferenceManager.getDefaultSharedPreferences(c).getInt("NumbersColor", 0xffff0000);
        color_saves = PreferenceManager.getDefaultSharedPreferences(c).getInt("SaveColor", 0xffff0000);
        color_specials = PreferenceManager.getDefaultSharedPreferences(c).getInt("SpecialColor", 0xffff0000);
        color_display = PreferenceManager.getDefaultSharedPreferences(c).getInt("DisplayColor", 0xffff0000);
        color_displaytext = PreferenceManager.getDefaultSharedPreferences(c).getInt("DisplayTextColor", 0x000000);
        color_background = PreferenceManager.getDefaultSharedPreferences(c).getInt("BackgroundColor", 0xffff0000);
    }

}
