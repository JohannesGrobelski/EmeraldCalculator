package com.example.titancalculator.helper.MainDisplay;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.audiofx.Equalizer;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.example.titancalculator.CalcActivity_science;
import com.example.titancalculator.FontSettingsActivity;
import com.example.titancalculator.R;

public class DesignApplier {
    public static final String[] designs = {"light","dark","dawn","germany","bright"};

    /* Design applies to:
     buttonshapeID
     buttonfüllung
    
     current_font_family
     current_fontsize
     current_fontstlye
    
     color_conv
     color_const
     color_hist

     color_act
     color_fkt
     color_fops
     color_numbers
     color_saves
     color_specials
     color_display
     color_displaytext
     color_background
     */

    public static void apply_theme(Context c, String name){
        Toast.makeText(c,"Set Design to:"+name,Toast.LENGTH_SHORT).show();
        if(array_contains(designs,name)){
            switch(name){
                case "dark":{
                    SettingsApplier.darker_factor_font = 1f;
                    SettingsApplier.setButtonshapeID("Round");
                    SettingsApplier.setButtonfüllung("leer");

                    SettingsApplier.setCurrent_font_family("monospace");
                    SettingsApplier.setCurrent_fontsize("30");
                    SettingsApplier.setCurrent_fontsize("normal");

                    SettingsApplier.setDefaultColors();
                    SettingsApplier.setColor_background(0xff000000);
                    SettingsApplier.setColor_displaytext(0xffFFFFFF);
                    break;
                }
                case "light":{
                    SettingsApplier.darker_factor_font = 0.5f;
                    SettingsApplier.setButtonshapeID("Square");
                    SettingsApplier.setButtonfüllung("voll");

                    SettingsApplier.setCurrent_font_family("monospace");
                    SettingsApplier.setCurrent_fontsize("30");
                    SettingsApplier.setCurrent_fontsize("normal");

                    SettingsApplier.setDefaultColors();
                    SettingsApplier.setColor_background(0xffFFFFFF);
                    break;
                } case "dawn":{
                    SettingsApplier.darker_factor_font = 0.5f;
                    SettingsApplier.setButtonshapeID("Round");
                    SettingsApplier.setButtonfüllung("voll");

                    SettingsApplier.setCurrent_font_family("monospace");
                    SettingsApplier.setCurrent_fontsize("30");
                    SettingsApplier.setCurrent_fontsize("normal");

                    SettingsApplier.setDefaultColors();
                    SettingsApplier.setColor_fkt(0xfffe938c);
                    SettingsApplier.setColor_fops(0xffead2ac);
                    SettingsApplier.setColor_numbers(0xff9cafb7);
                    SettingsApplier.setColor_act(0xffe6b89c);
                    SettingsApplier.setColor_specials(0xff4281a4);
                    SettingsApplier.setColor_background(0xff182f3c);
                    break;
                } case "germany":{
                    SettingsApplier.setButtonshapeID("Round");
                    SettingsApplier.setButtonfüllung("voll");

                    SettingsApplier.setCurrent_font_family("monospace");
                    SettingsApplier.setCurrent_fontsize("30");
                    SettingsApplier.setCurrent_fontsize("normal");

                    SettingsApplier.setDefaultColors();
                    SettingsApplier.setColor_display(0xff000000);
                    SettingsApplier.setColor_displaytext(0xffFFFFFF);
                    SettingsApplier.setColor_act(0xff000000);
                    SettingsApplier.setColor_fops(0xff000000);
                    SettingsApplier.setColor_fkt(0xffFF0000);
                    SettingsApplier.setColor_numbers(0xffFFCC00);
                    SettingsApplier.setColor_specials(0xffFFCC00);
                    SettingsApplier.setColor_background(0xffFFFFFF);
                    break;
                } case "bright":{
                    SettingsApplier.darker_factor_font = .5f;

                    SettingsApplier.setButtonshapeID("Round");
                    SettingsApplier.setButtonfüllung("voll");

                    SettingsApplier.setCurrent_font_family("monospace");
                    SettingsApplier.setCurrent_fontsize("30");
                    SettingsApplier.setCurrent_fontsize("normal");

                    SettingsApplier.setDefaultColors();
                    SettingsApplier.setColor_display(0xffa8d8ea);
                    SettingsApplier.setColor_displaytext(0xffFFFFFF);
                    SettingsApplier.setColor_act(0xffaa96da);
                    SettingsApplier.setColor_fops(0xffa8d8ea);
                    SettingsApplier.setColor_fkt(0xfffcbad3);
                    SettingsApplier.setColor_numbers(0xffffffd2);
                    SettingsApplier.setColor_specials(0xffffffd2);
                    SettingsApplier.setColor_background(0xffFFFFFF);
                    break;
                }


            }
            SettingsApplier.saveSettings(c);
            Toast.makeText(c,"Set Design to:"+name,Toast.LENGTH_SHORT).show();

        }
        return;
    }


    private static boolean array_contains(String AR[], String S){
        for(String s: AR){
            if(s.equals(S))return true;
        }
        return false;
    }

    public static int getBrightness(int[]RGB){
        if(RGB.length != 3)return -1;
        return (299*RGB[0] + 587*RGB[1] + 114*RGB[2]) / 1000;
    }

    public static int[] transToRGB(int color){
        int b = (color)&0xFF;
        int g = (color>>8)&0xFF;
        int r = (color>>16)&0xFF;
        int a = (color>>24)&0xFF;
        return new int[]{r,g,b};
    }

    public static int getContrast(int color1, int color2){
        return getBrightness(transToRGB(color1)) / getBrightness(transToRGB(color2));
    }

    public static void main(String[] a){
        System.out.println(DesignApplier.getBrightness(DesignApplier.transToRGB(0xffFFCC00)));
    }
}

