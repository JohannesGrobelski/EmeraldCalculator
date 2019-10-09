package com.example.titancalculator.helper.MainDisplay;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.titancalculator.SettingsActivity;
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

    private static int color_conv=0;
    private static int color_const=0;
    private static int color_hist=0;

    public static Float darker_factor_font=0.7f;

    private static int color_act=0;
    private static int color_fkt=0;
    private static int color_fops=0;
    private static int color_numbers=0;
    private static int color_saves=0;
    private static int color_specials=0;
    private static int color_display=0;
    private static int color_displaytext=0;
    private static int color_background=0;


    public static void saveSettings(Context c){
        //write into Preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("buttonshape", SettingsApplier.getButtonshapeID());
        editor.putString("buttonfüllung", SettingsApplier.getButtonfüllung());

        editor.putString("current_font_family", SettingsApplier.getCurrent_font_family());
        editor.putString("current_fontsize", SettingsApplier.getCurrent_fontsize());
        editor.putString("current_fontstlye", SettingsApplier.getCurrent_fontstlye());

        editor.putInt("ConvColor", SettingsApplier.getColor_conv(c));
        editor.putInt("ConstColor", SettingsApplier.getColor_const(c));
        editor.putInt("HistColor", SettingsApplier.getColor_hist(c));

        editor.putInt("ActColor", SettingsApplier.getColor_act(c));
        editor.putInt("FktColor", SettingsApplier.getColor_fkt(c));
        editor.putInt("FopsColor", SettingsApplier.getColor_fops(c));
        editor.putInt("NumbersColor", SettingsApplier.getColor_numbers(c));
        editor.putInt("SaveColor", SettingsApplier.getColor_saves(c));
        editor.putInt("SpecialColor", SettingsApplier.getColor_specials(c));
        editor.putInt("DisplayColor", SettingsApplier.getColor_display(c));
        editor.putInt("DisplayTextColor", SettingsApplier.getColor_displaytext(c));
        editor.putInt("BackgroundColor", SettingsApplier.getColor_background(c));

        editor.commit();
    }

    public static void setDefaultColors(){
        setColor_conv(0xffc60aff); //lila
        setColor_const(0xff00B0FF); //blau
        setColor_hist(0xff00E676); //grün

        setColor_fops(0xff3498db); //blau
        setColor_act(0xff9b59b6); //lila
        setColor_act(0xff9b59b6);
        setColor_fkt(0xff2ecc71); //grün
        setColor_specials(0xffe67e22); //orange
        setColor_numbers(0xffecf0f1); //hellgrau
        setColor_saves(0xffAFAFAF); //dunkelgrau
        setColor_display(0xffecf0f1); //hellgrau
        setColor_displaytext(0xff000000); //schwarz
        setColor_background(0xffFFFFFF); //weiß
    }







    private static void applySettings(Context c){
        //language
        language = PreferenceManager.getDefaultSharedPreferences(c).getString("pref_lang","english");

        //buttonshape
        if (PreferenceManager.getDefaultSharedPreferences(c).contains("buttonshape")) {
            String form = PreferenceManager.getDefaultSharedPreferences(c).getString("buttonshape","round");
            if(form != null){
                switch(form){
                    case "Circle": {
                        buttonshapeID = R.drawable.buttonshape_circel;
                        break;
                    }
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
                        setColor(c, background, SettingsApplier.getColor_act(c),buttonfüllung,stroke);
                        darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.getColor_act(c),factor_font);
                        ((Button) x).setTextColor(darker);
                    }
                    if(x instanceof Spinner){
                        background = c.getResources().getDrawable(buttonshapeID);
                        setColor(c,background,color,buttonfüllung,stroke);
                        x.setBackground(background);

                        ///((Spinner) x).setPopupBackgroundDrawable(background);
                        x.setBackgroundColor(color);
                        x.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        //((Spinner) x).setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                    if(x instanceof TextView){
                        background = c.getResources().getDrawable(buttonshapeID);
                        setColor(c, background, SettingsApplier.getColor_display(c),buttonfüllung,stroke);
                        //setTextColor(SettingsApplier.getColor_displaytext);
                        x.setBackground(background);
                    }


                    if(background!=null)x.setBackground(background);
                }

            }


        }

    }


    public static void setTextColor(View v, int color) {
        if(DesignApplier.getBrightness(DesignApplier.transToRGB(color)) < 20){
            color = 0xffFFFFFF; //weiß
        }

        if(v instanceof Button){
            ((Button) v).setTextColor(color);
        } else if (v instanceof TextView){
            ((TextView) v).setTextColor(color);
        } else if (v instanceof EditText){
            ((EditText) v).setTextColor(color);
        }
    }


    public static void setColor(Context context, Drawable background, int color, String füllung, boolean stroke){
        if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            shapeDrawable.getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;

            int farbe = 0; int rahmen_farbe = 0;
            //wenn farbe zu dunkel
            if(DesignApplier.getBrightness(DesignApplier.transToRGB(color)) < 20){
                rahmen_farbe = color;
                farbe = ButtonSettingsActivity.manipulateColor(color, 1 / (3*SettingsApplier.darker_factor_font));;
            } else{
                rahmen_farbe = ButtonSettingsActivity.manipulateColor(color,SettingsApplier.darker_factor_font);
                farbe = color;
            }

            gradientDrawable.setColor(farbe);
            if(füllung.equals("leer"))gradientDrawable.setColor(SettingsApplier.getColor_background(context));
            if(stroke)gradientDrawable.setStroke(7, rahmen_farbe);

            //fix für circle
            if(buttonshapeID == R.drawable.buttonshape_circel){
                int mw = Math.max(background.getMinimumHeight(), background.getMinimumWidth());
                gradientDrawable.setCornerRadius(mw);
            }
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(color);
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
            Float shrink_factor=1f;
            Float f = 10f;
            if(!current_fontsize.isEmpty() && !current_fontsize.equals("automatic"))f = Float.valueOf(current_fontsize);
            if(current_fontsize.equals("automatic")){
                f = DisplaySetupHelper.getDefaultTextSize(c);
            }
            if(v instanceof Button){
                //shrink_factor = (float) (Math.ceil(((Button)v).getText().length() / 4) + 1);
                //Log.e("SHRINK",c.getResources().getResourceEntryName(v.getId())+" "+shrink_factor);
                ((Button)v).setTextSize(TypedValue.COMPLEX_UNIT_SP, f / shrink_factor);
                ((Button)v).setTypeface(FontSettingsActivity.getTypeFace(current_font_family,current_fontstlye));
                if(!current_fontsize.equals("automatic"))((Button)v).setTextSize(f / shrink_factor);
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
        color_conv = PreferenceManager.getDefaultSharedPreferences(c).getInt("ConvColor", 0xffff0000);
        color_const = PreferenceManager.getDefaultSharedPreferences(c).getInt("ConstColor", 0xffff0000);
        color_hist = PreferenceManager.getDefaultSharedPreferences(c).getInt("HistColor", 0xffff0000);

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


    //Getter und Setter

    public static int getButtonshapeID() {
        return buttonshapeID;
    }

    public static void setButtonshapeID(String buttonshape) {
        if(buttonshape != null){
            switch(buttonshape){
                case "Circle": {
                    buttonshapeID = R.drawable.buttonshape_circel;
                    break;
                }
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
    }

    public static String getButtonfüllung() {
        return buttonfüllung;
    }

    public static void setButtonfüllung(String buttonfüllung) {
        SettingsApplier.buttonfüllung = buttonfüllung;
    }

    public static String getCurrent_font_family() {
        return current_font_family;
    }

    public static void setCurrent_font_family(String current_font_family) {
        SettingsApplier.current_font_family = current_font_family;
    }

    public static String getCurrent_fontsize() {
        return current_fontsize;
    }

    public static void setCurrent_fontsize(String current_fontsize) {
        SettingsApplier.current_fontsize = current_fontsize;
    }

    public static String getCurrent_fontstlye() {
        return current_fontstlye;
    }

    public static void setCurrent_fontstlye(String current_fontstlye) {
        SettingsApplier.current_fontstlye = current_fontstlye;
    }

    public static int getColor_conv() {
        return color_conv;
    }

    public static void setColor_conv(int color_conv) {
        SettingsApplier.color_conv = color_conv;
    }

    public static int getColor_const() {
        return color_const;
    }

    public static void setColor_const(int color_const) {
        SettingsApplier.color_const = color_const;
    }

    public static int getColor_hist() {
        return color_hist;
    }

    public static void setColor_hist(int color_hist) {
        SettingsApplier.color_hist = color_hist;
    }

    public static void setColor_act(int color_act) {
        SettingsApplier.color_act = color_act;
    }

    public static void setColor_fkt(int color_fkt) {
        SettingsApplier.color_fkt = color_fkt;
    }

    public static void setColor_fops(int color_fops) {
        SettingsApplier.color_fops = color_fops;
    }

    public static void setColor_numbers(int color_numbers) {
        SettingsApplier.color_numbers = color_numbers;
    }

    public static void setColor_saves(int color_saves) {
        SettingsApplier.color_saves = color_saves;
    }

    public static void setColor_specials(int color_specials) {
        SettingsApplier.color_specials = color_specials;
    }

    public static void setColor_display(int color_display) {
        SettingsApplier.color_display = color_display;
    }

    public static void setColor_displaytext(int color_displaytext) {
        SettingsApplier.color_displaytext = color_displaytext;
    }

    public static void setColor_background(int color_background) {
        SettingsApplier.color_background = color_background;
    }

    public static int getColor_conv(Context c) {
        if(color_conv == 0){
            color_conv = PreferenceManager.getDefaultSharedPreferences(c).getInt("ConvColor",0xffc60aff);
        }
        return color_conv;
    }

    public static int getColor_const(Context c) {
        if(color_const == 0){
            color_const = PreferenceManager.getDefaultSharedPreferences(c).getInt("ConstColor",0x00B0FF);
        }
        return color_const;
    }

    public static int getColor_hist(Context c) {
        if(color_hist == 0){
            color_hist = PreferenceManager.getDefaultSharedPreferences(c).getInt("HistColor",0x00E676);
        }
        return color_hist;
    }

    public static int getColor_act(Context c) {
        if(color_const == 0){
            color_const = PreferenceManager.getDefaultSharedPreferences(c).getInt("ConstColor",0x9b59b6);
        }
        return color_act;
    }

    public static int getColor_fkt(Context c) {
        if(color_fkt == 0){
            color_fkt = PreferenceManager.getDefaultSharedPreferences(c).getInt("FktColor",0x2ecc71);
        }
        return color_fkt;
    }

    public static int getColor_fops(Context c) {
        if(color_fops == 0){
            color_fops = PreferenceManager.getDefaultSharedPreferences(c).getInt("FopsColor",0x3498db);
        }
        return color_fops;
    }

    public static int getColor_numbers(Context c) {
        if(color_numbers == 0){
            color_numbers = PreferenceManager.getDefaultSharedPreferences(c).getInt("NumbersColor",0xecf0f1);
        }
        return color_numbers;
    }

    public static int getColor_saves(Context c) {
        if(color_saves == 0){
            color_saves = PreferenceManager.getDefaultSharedPreferences(c).getInt("SaveColor",0xAFAFAF);
        }
        return color_saves;
    }

    public static int getColor_specials(Context c) {
        if(color_specials == 0){
            color_specials = PreferenceManager.getDefaultSharedPreferences(c).getInt("SpecialColor",0xe67e22);
        }
        return color_specials;
    }

    public static int getColor_display(Context c) {
        if(color_display == 0){
            color_display = PreferenceManager.getDefaultSharedPreferences(c).getInt("DisplayColor",0xecf0f1);
        }
        return color_display;
    }

    public static int getColor_displaytext(Context c) {
        if(color_displaytext == 0){
            color_displaytext = PreferenceManager.getDefaultSharedPreferences(c).getInt("DisplayTextColor",0x000000);
        }
        return color_displaytext;
    }

    public static int getColor_background(Context c) {
        if(color_background == 0){
            color_background = PreferenceManager.getDefaultSharedPreferences(c).getInt("BackgroundColor",0xFFFFFF);
        }
        return color_background;
    }

    public static void setDarker_factor_font(Float darker_factor_font) {
        SettingsApplier.darker_factor_font = darker_factor_font;
    }

    public static Float getDarker_factor_font() {
        return darker_factor_font;
    }

}
