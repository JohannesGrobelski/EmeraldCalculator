package com.example.titancalculator.helper.MainDisplay;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.example.titancalculator.ButtonSettingsActivity;
import com.example.titancalculator.CalcActivity_science;
import com.example.titancalculator.FontSettingsActivity;
import com.example.titancalculator.FunctionGroupSettingsActivity;
import com.example.titancalculator.MainActivity;
import com.example.titancalculator.R;
import com.example.titancalculator.helper.Math_String.NumberString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SettingsApplier {

    private static String language="german";
    private static int buttonshapeID= R.drawable.buttonshape_square;
    private static String buttonfüllung="voll";

    public static String current_font_family;
    public static String current_fontsize;
    public static String current_fontstlye;
    public static int color_act,color_fkt,color_fops,color_numbers,color_saves,color_specials,color_display,color_displaytext,color_background;



    private static void applySettings(Context c){
        //language
        language = PreferenceManager.getDefaultSharedPreferences(c).getString("pref_lang","english");

        //buttonshape
        if (PreferenceManager.getDefaultSharedPreferences(c).contains("buttonshape")) {
            String form = PreferenceManager.getDefaultSharedPreferences(c).getString("buttonshape","round");
            if(form != null){
                switch(form){
                    case "Round": {
                        buttonshapeID = R.drawable.buttonshape_round;
                        break;
                    }
                    case "Square": {
                        buttonshapeID = R.drawable.buttonshape_square;
                        break;
                    }
                }
            }
            else Toast.makeText(c,"no buttonshape settings",Toast.LENGTH_SHORT).show();
        }

        //buttonfüllung
        if (PreferenceManager.getDefaultSharedPreferences(c).contains("buttonfüllung")) {
            buttonfüllung = PreferenceManager.getDefaultSharedPreferences(c).getString("buttonfüllung","voll");
        }

    }

    public static void setBackgrounds(Context c, ArrayList<ArrayList<View>> ALL, ArrayList<Integer> colors){
        applySettings(c);
        Drawable background=null;
        SettingsApplier.setColors(c);
        float factor_font = 0.5f;
        int darker;
        boolean stroke = true;

        for(ArrayList<View> set: ALL){
            if(set == null)continue;
            else {
                for(View x: set){
                    int color = colors.get(ALL.indexOf(set));
                    if(x instanceof Button){
                        //fix für größe dieser kleinen unicode symbole
                        background = c.getResources().getDrawable(buttonshapeID);
                        setColor(background, SettingsApplier.color_act,buttonfüllung,stroke);
                        darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.color_act,factor_font);
                        ((Button) x).setTextColor(darker);
                    }
                    if(x instanceof Spinner){
                        background = c.getResources().getDrawable(buttonshapeID);
                        setColor(background,color,buttonfüllung,stroke);
                        x.setBackground(background);

                        ///((Spinner) x).setPopupBackgroundDrawable(background);
                        x.setBackgroundColor(color);
                        x.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        //((Spinner) x).setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                    if(x instanceof TextView){
                        background = c.getResources().getDrawable(buttonshapeID);
                        setColor(background, SettingsApplier.color_display,buttonfüllung,stroke);
                        //setTextColor(SettingsApplier.color_displaytext);
                        x.setBackground(background);
                    }
                    if(background!=null)x.setBackground(background);
                }

            }


        }

    }

    static void setColor(Drawable background, int c, String füllung, boolean stroke){
        if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            shapeDrawable.getPaint().setColor(c);
        } else if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(c);
            int rahmen_farbe = ButtonSettingsActivity.manipulateColor(c,0.7f);
            if(füllung.equals("leer"))gradientDrawable.setColor(SettingsApplier.color_background);
            if(stroke)gradientDrawable.setStroke(7, rahmen_farbe);
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(c);
        }
        else Log.e("setColor Error","");

    }

    public static Float getCurrentFontsize(Context c){
        String  current_fontstlye = PreferenceManager.getDefaultSharedPreferences(c).getString("fontstyle", "normal");
        Float f = 20f;
        if(!current_fontsize.isEmpty() && !current_fontsize.equals("automatic"))f = Float.valueOf(current_fontsize);
        return f;
    }

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
                if(!current_fontsize.equals("automatic"))((Button)v).setTextSize(f);
            }
            if(v instanceof EditText){
                ((EditText)v).setTextSize(TypedValue.COMPLEX_UNIT_SP, DisplaySetupHelper.getDefaultTextSize(c));
                ((EditText)v).setTypeface(FontSettingsActivity.getTypeFace(current_font_family,current_fontstlye));
                if(!current_fontsize.equals("automatic"))((EditText)v).setTextSize(f);
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
