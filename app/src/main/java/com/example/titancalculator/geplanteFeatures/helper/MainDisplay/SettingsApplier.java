package com.example.titancalculator.geplanteFeatures.helper.MainDisplay;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

import com.example.titancalculator.geplanteFeatures.FontSettingsActivity;
import com.example.titancalculator.R;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class SettingsApplier {
    public static int haptic_feedback_duration=0;

    private static String language="german";
    private static int buttonshapeID= R.drawable.buttonshape_square;
    private static String buttonfüllung="voll";

    public static String current_font_family;
    public static String current_fontsize;
    public static String current_fontstlye;

    private static int color_conv=0;
    private static int color_const=0;
    private static int color_hist=0;

    public static Float darker_factor_font=-1f;

    private static int color_act=0;
    private static int color_fkt=0;
    private static int color_fops=0;
    private static int color_numbers=0;
    private static int color_saves=0;
    private static int color_specials=0;
    private static String füllung_display="voll";
    private static int color_display=0;
    private static int color_displaytext=0;
    private static int color_background=0;
    public static int vibrate_length=0;
    public static boolean vibrate_on=false;



    /**
     * @param b
     * @param paddingfactor  determines padding: int p = b.getMeasuredHeight() / paddingfactor
     */
    public static void centerTextButton(Button b, int paddingfactor){
        int p = b.getMeasuredHeight() / paddingfactor;
        if(b.getText().equals("(") || b.getText().equals(")")){
            b.setPadding(0,0,0,p);
        } else if(b.getText().equals("*")){
            b.setPadding(0,p,0,0);
        }
    }

    public static void setArrayAdapter(final Context context, View view, String[] array, final int color){
        float textsize = 20f;
        if(SettingsApplier.getCurrent_fontsize().matches("[0-9]+")){
            textsize = Float.valueOf(SettingsApplier.getCurrent_fontsize());
        } final float textsizefinal = textsize;

        int darker;
        if(!buttonfüllung.equals("leer")) {
            darker = SettingsApplier.manipulateColor(color,getDarker_factor_font(context));
        } else {
            darker = color;
        }

        if(DesignApplier.getBrightness(DesignApplier.transToRGB(darker)) == 0){darker = Color.WHITE;}

        final int finalDarker = darker;
        ArrayAdapter<String> array_adaper = new ArrayAdapter<String>(context, R.layout.spinner_shift_style, array){
                    float factor_font = SettingsApplier.getDarker_factor_font(context);
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        //SettingsApplier.setViewDesign(context,v,color);
                        //((TextView) v).setBackgroundColor(color);
                        ((TextView) v).setTextSize(textsizefinal);
                        ((TextView) v).setTextColor(finalDarker);
                        ((TextView) v).setTypeface(FontSettingsActivity.getTypeFace(current_font_family,current_fontstlye));
                        ((TextView) v).setGravity(Gravity.CENTER);
                        return v;
                    }
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View v = super.getDropDownView(position, convertView, parent);
                        SettingsApplier.setArrayElementDesign(context,(TextView) v,color);
                        ((TextView) v).setTextSize(textsizefinal);
                        ((TextView) v).setTextColor(finalDarker);
                        ((TextView) v).setTypeface(FontSettingsActivity.getTypeFace(current_font_family,current_fontstlye));
                        ((TextView) v).setGravity(Gravity.CENTER);
                        return v;
                    }
                };
        if(view instanceof  Spinner) ((Spinner) view).setAdapter(array_adaper);
        if(view instanceof  ListView) ((ListView) view).setAdapter(array_adaper);
    }

    /**
     * sets up views: font(family,style,size), background
     * @param context
     * @param view
     * @param invTextSizeFactor view.setTextSize(view.getHeight() / invTextSizeFactor);
     */
    public static void setETDesign(Context context, final EditText view, final int invTextSizeFactor) {
        if (current_font_family == null || current_fontstlye == null) applySettings(context);
        Typeface tp_current = FontSettingsActivity.getTypeFace(current_font_family, current_fontstlye);

        view.setTextColor(SettingsApplier.getColor_displaytext(context));
        view.setTypeface(tp_current);
        view.setLines(1);
        view.setMaxLines(1);
        view.setSingleLine(true);
        view.setHorizontallyScrolling(true);
        view.setGravity(Gravity.CENTER_VERTICAL);
    }

    /**
     * sets up views: font(family,style,size), background
     * @param context
     * @param view
     * @param invTextSizeFactor view.setTextSize(view.getHeight() / invTextSizeFactor);
     */
    public static void setETTextSize(Context context, final EditText view, final int invTextSizeFactor) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                view.setTextSize(view.getHeight() / invTextSizeFactor);
            }
        });
    }



    /**
     * sets up views: font(family,style,size), background
     * @param context
     * @param view
     * @param color
     */
    public static View setETC_ADesign(Context context, View view, int color) {
        if (current_font_family == null || current_fontstlye == null) applySettings(context);
        Typeface tp_current = FontSettingsActivity.getTypeFace(current_font_family, current_fontstlye);
        float textsize = 20f;
        if (SettingsApplier.getCurrent_fontsize().matches("[0-9]+")) {
            textsize = Float.valueOf(SettingsApplier.getCurrent_fontsize());
        }
        float factor_font = SettingsApplier.getDarker_factor_font(context);
        int darker = SettingsApplier.manipulateColor(color, factor_font);
        boolean stroke = true;

        //TODO: Quickfix
        if (DesignApplier.getBrightness(DesignApplier.transToRGB(darker)) == 0) {
            darker = Color.WHITE;
        }

        //Toast.makeText(context,"SA aS buttonshape ("+view.getClass().getSimpleName()+"): "+context.getResources().getResourceEntryName(buttonshapeID),Toast.LENGTH_SHORT).show();
        String simplename = view.getClass().getSimpleName();

        if(view instanceof Button){
            Drawable background = context.getResources().getDrawable(buttonshapeID);
            //Toast.makeText(context,"SA aS buttonshape ("+view.getClass().getSimpleName()+"): "+context.getResources().getResourceEntryName(buttonshapeID),Toast.LENGTH_SHORT).show();
            SettingsApplier.setColor((context),background, color,buttonfüllung,stroke);
            SettingsApplier.setTextColor(view,darker);
            ((Button) view).setTypeface(tp_current);
            if(((Button) view).getText().equals("⌧") || ((Button) view).getText().equals("⌫")){
                ((Button) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) (textsize));
            }
            else {
                ((Button) view).setTextSize(textsize);
            }
            view.setBackground(background);
        }
        return view;
    }

    /**
     * sets up views: font(family,style,size), background
     * @param context
     * @param view
     * @param color
     */
    public static View setArrayElementDesign(Context context, TextView view, int color) {
        if (current_font_family == null || current_fontstlye == null) applySettings(context);
        Typeface tp_current = FontSettingsActivity.getTypeFace(current_font_family, current_fontstlye);
        float textsize = 20f;
        if (SettingsApplier.getCurrent_fontsize().matches("[0-9]+")) {
            textsize = Float.valueOf(SettingsApplier.getCurrent_fontsize());
        }
        float factor_font = SettingsApplier.getDarker_factor_font(context);
        int darker = SettingsApplier.manipulateColor(color, factor_font);
        boolean stroke = true;

        //TODO: Quickfix
        if (DesignApplier.getBrightness(DesignApplier.transToRGB(darker)) == 0) {
            darker = Color.WHITE;
        }

        //Toast.makeText(context,"SA aS buttonshape ("+view.getClass().getSimpleName()+"): "+context.getResources().getResourceEntryName(buttonshapeID),Toast.LENGTH_SHORT).show();
        String simplename = view.getClass().getSimpleName();

        if (view instanceof TextView) {
            // Drawable background = DesignApplier.generateButtonDrawable(context.getDrawable(buttonshapeID),buttonfüllung);
            Drawable background = context.getDrawable(R.drawable.buttonshape_square);
            SettingsApplier.setColor((context), background, color, buttonfüllung, stroke);
            SettingsApplier.setTextColor(view, darker);
            ((TextView) view).setHintTextColor(SettingsApplier.manipulateColor(darker, darker_factor_font / 2));

            ((TextView) view).setTypeface(tp_current);
            ((TextView) view).setTextSize(textsize);
            view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            view.setBackground(background);
        }
        return view;
    }

    public static void rescaleText(View v){
        if(v instanceof TextView){
            ((TextView) v).setTextSize(v.getHeight() / 3);
        } else if(v instanceof EditText){
            ((EditText) v).setTextSize(v.getHeight() / 3);
        }
    }

    /**
     * sets up views: font(family,style,size), background
     * @param context
     * @param view
     * @param color
     */
    public static View setViewDesign(Context context, View view, int color){
        if(current_font_family==null || current_fontstlye==null)applySettings(context);
        Typeface tp_current = FontSettingsActivity.getTypeFace(current_font_family,current_fontstlye);
        //float textsize = 15f;
        if(SettingsApplier.getCurrent_fontsize().matches("[0-9]+")){
            //textsize = Float.valueOf(SettingsApplier.getCurrent_fontsize());
        }
        float factor_font = SettingsApplier.getDarker_factor_font(context);
        int darker;
        if(!buttonfüllung.equals("leer")) {
            darker = SettingsApplier.manipulateColor(color, factor_font);
        } else {
            darker = color;
        }

        boolean stroke = true;

        //TODO: Quickfix
        if(DesignApplier.getBrightness(DesignApplier.transToRGB(darker)) == 0){darker = Color.WHITE;}

        //Toast.makeText(context,"SA aS buttonshape ("+view.getClass().getSimpleName()+"): "+context.getResources().getResourceEntryName(buttonshapeID),Toast.LENGTH_SHORT).show();
        String simplename = view.getClass().getSimpleName();

        if(view instanceof TextView){
           // Drawable background = DesignApplier.generateButtonDrawable(context.getDrawable(buttonshapeID),buttonfüllung);
            Drawable background = context.getDrawable(buttonshapeID);
            SettingsApplier.setColor((context),background, color,buttonfüllung,stroke);
            SettingsApplier.setTextColor(view,darker);
            ((TextView) view).setHintTextColor(darker);

            ((TextView) view).setTypeface(tp_current);
            //((TextView) view).setTextSize(textsize);
            view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            view.setBackground(background);
        }
        if(view instanceof EditText){
            Drawable background = context.getDrawable(buttonshapeID);
            SettingsApplier.setColor((context),background, color,buttonfüllung,stroke);
            SettingsApplier.setTextColor(view,darker);
            ((EditText) view).setHintTextColor(darker);

            ((EditText) view).setTypeface(tp_current);
            view.setBackground(background);
        }
        if(view instanceof Button){
            Drawable background = context.getResources().getDrawable(buttonshapeID);
            //Toast.makeText(context,"SA aS buttonshape ("+view.getClass().getSimpleName()+"): "+context.getResources().getResourceEntryName(buttonshapeID),Toast.LENGTH_SHORT).show();
            SettingsApplier.setColor((context),background, color,buttonfüllung,stroke);
            SettingsApplier.setTextColor(view,darker);
            ((Button) view).setTypeface(tp_current);
            /*
            if(((Button) view).getText().equals("⌧") || ((Button) view).getText().equals("⌫")){
                ((Button) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, (float) (textsize*1.7));
            }

            else {

                if(color == SettingsApplier.getColor_fops(context)){
                    textsize = Math.min(15,textsize);
                }

                //((Button) view).setTextSize(textsize);
            }

             */
            view.setBackground(background);
        }
        if(view instanceof LinearLayout){
            Drawable background = context.getResources().getDrawable(buttonshapeID);
            //Toast.makeText(context,"SA aS buttonshape ("+view.getClass().getSimpleName()+"): "+context.getResources().getResourceEntryName(buttonshapeID),Toast.LENGTH_SHORT).show();
            SettingsApplier.setColor((context),background, color,buttonfüllung,stroke);
            SettingsApplier.setTextColor(view,darker);
            view.setBackground(background);
        }
        if(view instanceof Spinner){
            Drawable background = context.getDrawable(buttonshapeID);
            SettingsApplier.setColor((context),background, color,buttonfüllung,stroke);
            SettingsApplier.setTextColor(view,darker);

            view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            view.setBackground(background);
        }
        return view;
    }

    public static void saveSettings(Context c){
        //write into Preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("darker_factor_font", darker_factor_font);

        editor.putInt("buttonshape", SettingsApplier.getButtonshapeID());
        editor.putString("buttonfüllung", SettingsApplier.getButtonfüllung());


        editor.putString("fontfamily", SettingsApplier.getCurrent_font_family());
        editor.putString("fontsize", SettingsApplier.getCurrent_fontsize());
        editor.putString("fontstyle", SettingsApplier.getCurrent_fontstlye());

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

    public static void setDefaultFont(){
        setCurrent_font_family("monospace");
        setCurrent_fontsize("30");
        setCurrent_fontstlye("normal");
    }

    public static void setDefaultButtonShapeFilling(){
        setButtonshapeID(R.drawable.buttonshape_square);
        setButtonfüllung("voll");
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

    public static void applySettings(Context c){
        //language
        language = PreferenceManager.getDefaultSharedPreferences(c).getString("pref_lang","english");

        //buttonshapeö
        SettingsApplier.getButtonshapeID();

        //buttonfüllung
        if (PreferenceManager.getDefaultSharedPreferences(c).contains("buttonfüllung")) {
            buttonfüllung = PreferenceManager.getDefaultSharedPreferences(c).getString("buttonfüllung","voll");
        }

        darker_factor_font = PreferenceManager.getDefaultSharedPreferences(c).getFloat("darker_factor_font",3f);

        if(PreferenceManager.getDefaultSharedPreferences(c).contains("vibration_on")){
            Boolean vb = PreferenceManager.getDefaultSharedPreferences(c).getBoolean("vibration_on",false);
            vibrate_on = Boolean.valueOf(vb);
        }

        String vl = PreferenceManager.getDefaultSharedPreferences(c).getString("vibrate_length","0");
        if(Pattern.matches("[0-9]?[0-9]?[0-9]?",vl)){
                vibrate_length = Integer.valueOf(vl);
        }

        //font
        current_font_family = PreferenceManager.getDefaultSharedPreferences(c).getString("fontfamily", "monospace");
        setCurrent_fontsize(PreferenceManager.getDefaultSharedPreferences(c).getString("fontsize", "20"));
        current_fontstlye = PreferenceManager.getDefaultSharedPreferences(c).getString("fontstyle", "normal");
    }

    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(Math.round(color*factor));
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r,255),
                Math.min(g,255),
                Math.min(b,255));
    }

    public static void setBackgrounds(Context c, ArrayList<ArrayList<View>> ALL, ArrayList<Integer> colors){
        applySettings(c);
        Drawable background=null;
        SettingsApplier.setColors(c);
        float factor_font = getDarker_factor_font(c);
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
                        darker = manipulateColor(SettingsApplier.getColor_act(c),factor_font);
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

    public static Drawable combineVectorBackground(Drawable vector, Drawable background){
            LayerDrawable finalDrawable = new LayerDrawable(new Drawable[] {background, vector});
            return finalDrawable;
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
                rahmen_farbe = manipulateColor(color, 1 / (3*SettingsApplier.darker_factor_font));;
                farbe = color;
            } else{
                rahmen_farbe = manipulateColor(color,SettingsApplier.darker_factor_font);
                farbe = color;
            }
            if(buttonfüllung.equals("leer")){
                rahmen_farbe = color;
                farbe = color;
            }

            gradientDrawable.setColor(farbe);
            if(füllung.equals("leer"))gradientDrawable.setColor(SettingsApplier.getColor_background(context));
            if(stroke)gradientDrawable.setStroke(7, rahmen_farbe);
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(color);
        }
        else Log.e("setColor Error","");
    }

    public static Float getCurrentFontsize(Context c){
        String current_fontstlye = PreferenceManager.getDefaultSharedPreferences(c).getString("fontstyle", "normal");
        Float f = 20f;
        if(!current_fontsize.isEmpty() && !current_fontsize.equals("automatic"))f = Float.valueOf(current_fontsize);
        return f;
    }

    public static void setFonts(Context c, View view){
        current_font_family = PreferenceManager.getDefaultSharedPreferences(c).getString("fontfamily", "monospace");
        current_fontsize = PreferenceManager.getDefaultSharedPreferences(c).getString("fontsize", "20");
        current_fontstlye = PreferenceManager.getDefaultSharedPreferences(c).getString("fontstyle", "normal");

        if(current_fontstlye == null || current_fontstlye.equals("") || current_font_family == null || current_font_family.equals("") || current_fontsize == null || current_fontsize.equals(""))return;


        Float shrink_factor=1f;
        Float f = 10f;
        if(!current_fontsize.isEmpty() && !current_fontsize.equals("automatic"))f = Float.valueOf(current_fontsize);
        if(current_fontsize.equals("automatic")){
            f = DisplaySetupHelper.getDefaultTextSize(c);
        }
        if(view instanceof Button){
            //shrink_factor = (float) (Math.ceil(((Button)v).getText().length() / 4) + 1);
            //Log.e("SHRINK",c.getResources().getResourceEntryName(v.getId())+" "+shrink_factor);
            ((Button)view).setTextSize(TypedValue.COMPLEX_UNIT_SP, f / shrink_factor);
            ((Button)view).setTypeface(FontSettingsActivity.getTypeFace(current_font_family,current_fontstlye));
            if(!current_fontsize.equals("automatic"))((Button)view).setTextSize(f / shrink_factor);
        }
        if(view instanceof EditText){
            ((EditText)view).setTextSize(TypedValue.COMPLEX_UNIT_SP, DisplaySetupHelper.getDefaultTextSize(c));
            ((EditText)view).setTypeface(FontSettingsActivity.getTypeFace(current_font_family,current_fontstlye));
            if(!current_fontsize.equals("automatic"))((EditText)view).setTextSize(f);
        }
        if(view instanceof TextView){
            ((TextView)view).setTextSize(TypedValue.COMPLEX_UNIT_SP, DisplaySetupHelper.getDefaultTextSize(c));
            ((TextView)view).setTypeface(FontSettingsActivity.getTypeFace(current_font_family,current_fontstlye));
            if(!current_fontsize.equals("automatic"))((TextView)view).setTextSize(f);
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

    public static void setButtonshapeID(int ID) {
        buttonshapeID = ID;
    }

    public static String getButtonfüllung() {
        return SettingsApplier.buttonfüllung;
    }

    public static void setButtonfüllung(String buttonfüllung) {
        SettingsApplier.buttonfüllung = buttonfüllung;
    }

    public static String getCurrent_font_family() {
        return SettingsApplier.current_font_family;
    }

    public static void setCurrent_font_family(String current_font_family) {
        SettingsApplier.current_font_family = current_font_family;
    }

    public static String getCurrent_fontsize() {
        return SettingsApplier.current_fontsize;
    }

    public static void setCurrent_fontsize(String current_fontsize) {
        SettingsApplier.current_fontsize = current_fontsize;
    }

    public static String getCurrent_fontstlye() {
        return SettingsApplier.current_fontstlye;
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
            color_displaytext = PreferenceManager.getDefaultSharedPreferences(c).getInt("DisplayTextColor",0xff000000);
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

    public static Float getDarker_factor_font(Context c) {
        if(getButtonfüllung().equals("voll")){
            if(darker_factor_font < .2f || darker_factor_font >= .8f){
                darker_factor_font = 0.5f;
            }
        }
        return darker_factor_font;
    }

    public static void drawVectorImage(Context context, View v,  int vectorID, int color){
            int darker;
            if(!buttonfüllung.equals("leer")) {
                darker = SettingsApplier.manipulateColor(color,getDarker_factor_font(context));
            } else {
                darker = color;
            }

            //TODO: Quickfix
            //Log.d("bright",""+DesignApplier.getBrightness(DesignApplier.transToRGB(darker)));
            if(DesignApplier.getBrightness(DesignApplier.transToRGB(darker)) == 0){darker = Color.WHITE;}
            if(SettingsApplier.getButtonfüllung().equals("leer")){
                /*
                if(DesignApplier.getContrast(getColor_background(context),darker) < DesignApplier.contrast_thresh){
                }
                */
                //TODOdarker = DesignApplier.invertColorBrightness(darker);
            } else {
                /*
                if(DesignApplier.getContrast(color,darker) < DesignApplier.contrast_thresh){
                    darker = DesignApplier.invertColorBrightness(darker);
                }
                */
            }

            /*
            int width = 100;
            int height = 100;
            if(v.getWidth() != 0 && v.getHeight() != 0){
                width = v.getWidth() / 3;
                height = v.getHeight() / 3;
            }
             */

            Drawable vector =  context.getResources().getDrawable(vectorID);
            //vector = ImageUtils.resizeImage(context,vectorID,width,height);
            vector.setColorFilter(darker, PorterDuff.Mode.SRC_ATOP);

            Drawable background = context.getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor((context),background, color,buttonfüllung,true);
            SettingsApplier.setTextColor(v,darker);
            ((Button) v).setText("");
            v.setBackground( SettingsApplier.combineVectorBackground(vector,background));

            //vector.invalidateSelf();
            //v.setBackground(vector);
    }

    public static Bitmap getBitmap(Drawable drawable){
        try {
            Bitmap bitmap;

            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            // Handle the error
            return null;
        }
    }

    //inits settings if user starts calc for first time
    public static void initSettings(Context c){
        if(!PreferenceManager.getDefaultSharedPreferences(c).contains("buttonfüllung")){ //signal
            DesignApplier.apply_theme(c, "bright");
        }
    }

}
