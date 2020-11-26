package com.example.titancalculator.geplanteFeatures.helper.MainDisplay;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.titancalculator.helper.ArrayUtils;
import java.util.ArrayList;

public class DesignApplier {
    public static final String[] designs = {"light","dark","dawn","germany","bright"};
    public static final double contrast_thresh = 10;
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

    public static int invertColorBrightness(int color){
        if(color == 0)color = 0x00111111;
        //while(getBrightness(transToRGB(color)) < contrast_thresh){
            color = setBrightness(color,1.5);
        //}
        return color;
    }

    public static void bugfix_sameheight_ll(ArrayList<LinearLayout> input){
        int max = 0;
        for(int i=0; i<input.size(); i++){
            if(input.get(i) != null){
                if(((LinearLayout) input.get(i)).getHeight() > max){
                    max = input.get(i).getHeight();
                }
            }
        }
        for(int i=0; i<input.size(); i++){
            if(input.get(i) != null){
               input.get(i).setMinimumHeight(max);
            }
        }
    }

    public static void bugfix_sameheight_buttons(ArrayList<Button> input){
        int max = 0;
        for(int i=0; i<input.size(); i++){
            if(input.get(i) != null){
                if(((Button) input.get(i)).getHeight() > max){
                    max = input.get(i).getHeight();
                }
            }
        }
        for(int i=0; i<input.size(); i++){
            if(input.get(i) != null){
                input.get(i).setMinimumHeight(max);
            }
        }
    }

    /**
     * bei design auswahl ALLES zurücksetzen (act color, colors, schrift(art,größe,style)buttonfüllung und buttonshape )
     */
    public static void setDefaultDesign(){
        SettingsApplier.setDefaultColors();
        SettingsApplier.setDefaultFont();
        SettingsApplier.setDefaultButtonShapeFilling();
    }

    public static void apply_theme(Context c, String name){
        setDefaultDesign();
        if(ArrayUtils.array_contains(designs,name)){
            switch(name){
                case "dark":{
                    SettingsApplier.darker_factor_font = 1f;
                    SettingsApplier.setButtonshapeID("Round");
                    SettingsApplier.setButtonfüllung("leer");

                    SettingsApplier.setColor_background(0xff000000);
                    SettingsApplier.setColor_display(0xffFFFFFF);
                    SettingsApplier.setColor_displaytext(0xffFFFFFF);
                    SettingsApplier.setDarker_factor_font(0.99f);
                    break;
                }
                case "light":{
                    SettingsApplier.darker_factor_font = 0.5f;
                    SettingsApplier.setButtonshapeID("Square");
                    SettingsApplier.setButtonfüllung("voll");

                    SettingsApplier.setColor_display(SettingsApplier.getColor_numbers(c));
                    float factor_font = SettingsApplier.getDarker_factor_font(c);
                    int darkerNumberTheme = SettingsApplier.manipulateColor(SettingsApplier.getColor_numbers(c),factor_font);
                    SettingsApplier.setColor_displaytext(darkerNumberTheme);
                    SettingsApplier.setColor_background(0xffFFFFFF);
                    break;
                } case "dawn":{
                    SettingsApplier.darker_factor_font = 0.5f;
                    SettingsApplier.setButtonshapeID("Round");
                    SettingsApplier.setButtonfüllung("voll");

                    SettingsApplier.setColor_fkt(0xfffe938c);
                    SettingsApplier.setColor_fops(0xffead2ac);
                    SettingsApplier.setColor_numbers(0xff9cafb7);
                    SettingsApplier.setColor_act(0xffe6b89c);
                    SettingsApplier.setColor_specials(0xff4281a4);
                    SettingsApplier.setColor_background(0xff182f3c);
                    SettingsApplier.setColor_displaytext(0xff182f3c);

                    SettingsApplier.setColor_conv(0xfffe938c);
                    SettingsApplier.setColor_const(0xff9cafb7);
                    SettingsApplier.setColor_hist(0xff4281a4);

                    break;
                } case "germany":{
                    SettingsApplier.setButtonshapeID("Round");
                    SettingsApplier.setButtonfüllung("voll");

                    SettingsApplier.setColor_display(0xff000000);
                    SettingsApplier.setColor_displaytext(0xffFFFFFF);
                    SettingsApplier.setColor_act(0xff000000);
                    SettingsApplier.setColor_fops(0xff000000);
                    SettingsApplier.setColor_fkt(0xffFF0000);
                    SettingsApplier.setColor_numbers(0xffFFCC00);
                    SettingsApplier.setColor_specials(0xffFFCC00);
                    SettingsApplier.setColor_background(0xffFFFFFF);

                    SettingsApplier.setColor_conv(0xffFFCC00);
                    SettingsApplier.setColor_const(0xffFF0000);
                    SettingsApplier.setColor_hist(0xffFFCC00);
                    break;
                } case "bright":{
                    SettingsApplier.darker_factor_font = .5f;

                    SettingsApplier.setButtonshapeID("Round");
                    SettingsApplier.setButtonfüllung("voll");

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

    public static Drawable generateButtonDrawable(Drawable Shape, Drawable Filling){
        return new LayerDrawable(new Drawable[] {Filling,Shape});
    }



    public static int getBrightness(int[]RGB){
        if(RGB.length != 4)return -1;
        return (299*RGB[0] + 587*RGB[1] + 114*RGB[2]) / 1000;
    }

    public static int[] transToRGB(int color){
        int b = (color)&0xFF;
        int g = (color>>8)&0xFF;
        int r = (color>>16)&0xFF;
        int a = (color>>24)&0xFF;
        return new int[]{r,g,b,a};
    }

    public static int transToColor(int[] RGBA){
        if(RGBA.length != 4 || RGBA[0]<0 || RGBA[0] > 255 || RGBA[1]<0 || RGBA[1] > 255 || RGBA[2]<0 || RGBA[2] > 255 || RGBA[3]<0 || RGBA[3] > 255)return 0x00000000;
        int color = 0x00;
        String R = Integer.toHexString(RGBA[0]);
        String G = Integer.toHexString(RGBA[1]);
        String B = Integer.toHexString(RGBA[2]);
        String A = Integer.toHexString(RGBA[3]);

        String rgba = R+G+B;
        return Integer.parseInt(rgba,16);
    }

    public static int setBrightness(int color, double factor){
        int[] Color = transToRGB(color);

        //check if factor valid
        double maxFac = factor;
        for(int i=0; i<3; i++){
            if(Color[i] * factor > 255){
                maxFac = 255 / Color[i];
            }
        }

        //change brightness
        for(int i=0; i<3; i++){
            Color[i] *= maxFac;
        }

        //return color
        return transToColor(Color);
    }

    public static int getContrast(int color1, int color2){
        Log.d("brightnessC",getBrightness(transToRGB(color1)) +" "+getBrightness(transToRGB(color2)));
        if(getBrightness(transToRGB(color1)) == 0){
            return getBrightness(transToRGB(color2));
        }
        Log.d("brightnessC",String.valueOf(Math.abs(getBrightness(transToRGB(color2)) - getBrightness(transToRGB(color1)))));
        return Math.abs(getBrightness(transToRGB(color2)) - getBrightness(transToRGB(color1)));
    }

    public static void main(String[] a){
       // System.out.println(DesignApplier.getBrightness(DesignApplier.transToRGB(0xffFFCC00)));
        //System.out.println(DesignApplier.getContrast(DesignApplier.transToRGB(0xffFFCC00)));

        System.out.println(transToColor(transToRGB(0x11345435)) == 0x11345435);

    }
}

