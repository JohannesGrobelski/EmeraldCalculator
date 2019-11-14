package com.example.titancalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.helper.ButtonShapeCustomAdapter;
import com.example.titancalculator.helper.ButtonShapeImageModel;
import com.example.titancalculator.helper.MainDisplay.DisplaySetupHelper;
import com.example.titancalculator.helper.MainDisplay.SettingsApplier;

public class FontSettingsActivity extends AppCompatActivity {

    private static String[] font_families = {"SANS_SERIF","SERIF","MONOSPACE"};
    private static String[] font_style = {"normal","bold","italic"};
    private static String[] fontsize = {"automatic","5","10","15"};

    ListView lv_fontfamily;
    ListView lv_fontsize;
    ListView lv_fontstyle;

    TextView tv_preview;

    String current_tf="";
    String current_style="";
    String current_fs="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_settings);
        populate_fontsizeaAr();

        lv_fontfamily = findViewById(R.id.lv_fontfamily);
        lv_fontsize = findViewById(R.id.lv_fontsize);
        lv_fontstyle = findViewById(R.id.lv_style);
        tv_preview = findViewById(R.id.tv_preview);

        //ArrayAdapter<String> ff_adt = new ArrayAdapter<>(this,R.layout.lvitem_layout, font_families);
        //lv_fontfamily.setAdapter(ff_adt);
        setAdapter(FontSettingsActivity.this,lv_fontfamily,font_families);

        //ArrayAdapter<String> s_adt = new ArrayAdapter<>(this,R.layout.lvitem_layout, font_style);
        //lv_fontstyle.setAdapter(s_adt);
        setAdapter(FontSettingsActivity.this,lv_fontstyle,font_style);

        //ArrayAdapter<String> fs_adt = new ArrayAdapter<>(this,R.layout.lvitem_layout, fontsize);
        //lv_fontsize.setAdapter(fs_adt);
        setAdapter(FontSettingsActivity.this,lv_fontsize,fontsize);


        lv_fontstyle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(FontSettingsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                String inh = lv_fontstyle.getItemAtPosition(position).toString();
                editor.putString("fontstyle", inh);
                editor.commit();
                Toast.makeText(FontSettingsActivity.this,"set font style to:"+inh,Toast.LENGTH_SHORT).show();
                current_style = inh;
                setPreviewImage();
            }
        });

        lv_fontfamily.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(FontSettingsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                String inh = lv_fontfamily.getItemAtPosition(position).toString();
                editor.putString("fontfamily", inh);
                editor.commit();
                Toast.makeText(FontSettingsActivity.this,"set font family to:"+inh,Toast.LENGTH_SHORT).show();
                current_tf = inh;
                setPreviewImage();
            }
        });

        lv_fontsize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //Einstellung speichern
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(FontSettingsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                String inh = lv_fontsize.getItemAtPosition(position).toString();
                editor.putString("fontsize", inh);
                editor.commit();
                Toast.makeText(FontSettingsActivity.this,"set font size to:"+inh,Toast.LENGTH_SHORT).show();
                current_fs = inh;
                setPreviewImage();
            }
        });
    }

    public static void setAdapter(final Context context, View view, String[] array){
        float textsize = 20f;

        if(SettingsApplier.getCurrent_fontsize().matches("[0-9]+")){
            textsize = Float.valueOf(SettingsApplier.getCurrent_fontsize());
        } final float textsizefinal = textsize;
        ArrayAdapter<String> array_adaper = new ArrayAdapter<String>(context, R.layout.spinner_shift_style, array)
        {
            float factor_font = SettingsApplier.getDarker_factor_font(context);
            int darker =SettingsApplier.manipulateColor(SettingsApplier.getColor_fops(context),factor_font);
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                //SettingsApplier.setViewDesign(context,v,color);
                //((TextView) v).setBackgroundColor(color);
                ((TextView) v).setTextSize(textsizefinal);
                ((TextView) v).setTextColor(darker);
                ((TextView) v).setTypeface(Typeface.MONOSPACE);
                ((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                SettingsApplier.setViewDesign(context,v,Color.GRAY);
                ((TextView) v).setTextSize(textsizefinal);
                ((TextView) v).setTextColor(darker);
                ((TextView) v).setTypeface(Typeface.MONOSPACE);
                ((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }
        };
        if(view instanceof Spinner) ((Spinner) view).setAdapter(array_adaper);
        if(view instanceof  ListView) ((ListView) view).setAdapter(array_adaper);
    }

    private void populate_fontsizeaAr(){
        fontsize = new String[40];
        fontsize[0] = "automatic";
        for(int i=5; i<200; i+=5){
            fontsize[(i/5)] = String.valueOf(i);
        }
    }

    private void setPreviewImage(){
        String test = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";

        String tf = "monospace";
        String style = "normal";
        if(!current_tf.isEmpty())tf = current_tf;
        if(!current_style.isEmpty())style = current_style;
        tv_preview.setTypeface(getTypeFace(current_tf,current_style),getTypeFace(current_tf,current_style).getStyle());

        Float f = 10f;
        if(!current_fs.isEmpty() && !current_fs.equals("automatic"))f = Float.valueOf(current_fs);
        if(current_fs.equals("automatic")){
            f = DisplaySetupHelper.getDefaultTextSize(this);
        }
        tv_preview.setTextSize(f);

        tv_preview.setText(test);
    }

    public static Typeface getTypeFace(String tf, String style){
        Typeface face;
        switch (tf){
            case "SANS_SERIF":{
                switch(style){
                    case "bold":{
                        face = Typeface.create(Typeface.SANS_SERIF,Typeface.BOLD);
                        break;
                    }
                    case "italic":{
                        face = Typeface.create(Typeface.SANS_SERIF,Typeface.ITALIC);
                        break;
                    }
                    default:{
                        face = Typeface.create(Typeface.SANS_SERIF,Typeface.NORMAL);
                        break;
                    }
                }
            }
            case "SERIF":{
                switch(style){
                    case "bold":{
                        face = Typeface.create(Typeface.SERIF,Typeface.BOLD);
                        break;
                    }
                    case "italic":{
                        face = Typeface.create(Typeface.SERIF,Typeface.ITALIC);
                        break;
                    }
                    default:{
                        face = Typeface.create(Typeface.SERIF,Typeface.NORMAL);
                        break;
                    }
                }
            }
            default:{
                switch(style){
                    case "bold":{
                        face = Typeface.create(Typeface.MONOSPACE,Typeface.BOLD);
                        break;
                    }
                    case "italic":{
                        face = Typeface.create(Typeface.MONOSPACE,Typeface.ITALIC);
                        break;
                    }
                    default:{
                        face = Typeface.create(Typeface.MONOSPACE,Typeface.NORMAL);
                        break;
                    }
                }
                break;
            }
        }
        return face;
    }


}
