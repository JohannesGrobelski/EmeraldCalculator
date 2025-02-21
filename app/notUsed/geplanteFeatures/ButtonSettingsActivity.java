package com.example.titancalculator.geplanteFeatures;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.R;
import com.example.titancalculator.geplanteFeatures.helper.MainDisplay.SettingsApplier;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class ButtonSettingsActivity extends AppCompatActivity {
    String language="";
    String[] validFct = {"PI","E","NCR","NPR","%","!N","^","A/B","x\u207B\u00B9","+/-","√","\u00B3√","LOG","LN","LB","SIN","COS","TAN","ASIN","ATAN","ASINH","ACOSH","ATANH","SINH","COSH","TANH"};
    private final Set<String> validFCT = new HashSet<>(Arrays.asList(validFct));

    Button clicked; String Button_selected = "";
    String current_group="";
    ListView lv_group;


    //L2
    Button btn_11;
    Button btn_12;
    Button btn_13;
    Button btn_14;
    Button btn_15;
    Button btn_16;

    //L3
    Button btn_21;
    Button btn_22;
    Button btn_23;
    Button btn_24;
    Button btn_25;
    Button btn_26;


    Button save_settings;
    ListView LV_fct_sel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Button Settings");
        setContentView(R.layout.activity_button_settings);

        String[] ug = FunctionGroupSettingsActivity.getUserGroups(ButtonSettingsActivity.this);

        if(ug.length > 0){
            //L2
            btn_11 = findViewById(R.id.btn_11);
            btn_12 = findViewById(R.id.btn_12);
            btn_13 = findViewById(R.id.btn_13);
            btn_14 = findViewById(R.id.btn_14);
            btn_15 = findViewById(R.id.btn_15);
            btn_16 = findViewById(R.id.btn_16);

            //L3
            btn_21 = findViewById(R.id.btn_21);
            btn_22 = findViewById(R.id.btn_22);
            btn_23 = findViewById(R.id.btn_23);
            btn_24 = findViewById(R.id.btn_24);
            btn_25 = findViewById(R.id.btn_25);
            btn_26 = findViewById(R.id.btn_26);

            save_settings = findViewById(R.id.save_settings);
            applyLanguage();
            LV_fct_sel = (ListView) findViewById(R.id.LV_fct_sel);
            FontSettingsActivity.setAdapter(ButtonSettingsActivity.this,LV_fct_sel,validFct);

            //L2
            //normal: PI,E,CONST,CONV
            btn_11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_11);
                }
            });
            btn_12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_12);
                }
            });
            btn_13.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_13);
                }
            });
            btn_14.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_14);
                }
            });
            btn_15.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_15);
                }
            });
            btn_16.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_16);
                }
            });

            //L3
            //normal: %,!,^,a/b,x^-1,+/-
            //TRIGO: ASINH,ACOSH,ATANH,SINH,COSH,TANH
            btn_21.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_21);
                }
            });
            btn_22.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_22);
                }
            });
            btn_23.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_23);
                }
            });
            btn_24.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_24);
                }
            });
            btn_25.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_25);
                }
            });
            btn_26.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_26);
                }
            });


            lv_group = findViewById(R.id.lv_group);
            current_group = ug[0];
            setUpButtons(current_group);

            /*
            Toast.makeText(ButtonSettingsActivity.this,"Modes: "+Arrays.toString(ug),Toast.LENGTH_SHORT).show();
            FontSettingsActivity.setAdapter(ButtonSettingsActivity.this,lv_group,ug);

            save_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            LV_fct_sel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    if(clicked != null){
                        //Button text setzen
                        String inh = LV_fct_sel.getItemAtPosition(position).toString();
                        clicked.setText(inh);

                        //Einstellung speichern
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ButtonSettingsActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        String key = current_group+"_"+Button_selected;
                        String value = clicked.getText().toString().trim();
                        editor.putString(key,value);
                        Toast.makeText(ButtonSettingsActivity.this,"NEW FCT: k="+key+" v="+value,Toast.LENGTH_SHORT).show();
                        editor.commit();
                    }
                }
            });

            lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    current_group = lv_group.getItemAtPosition(position).toString();
                    setUpButtons(current_group);
                }
            });

            */
            setBackgrounds();

        }
        else{
            Toast.makeText(ButtonSettingsActivity.this,"no groups defined!",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    void click(Button x){
        if(clicked == null){
            clicked = x; Button_selected = x.getTag().toString();
            Toast.makeText(ButtonSettingsActivity.this,"clicked: "+x.getText().toString()+" "+Button_selected,Toast.LENGTH_LONG).show();
            select(x);
            return;
        }
        if(x.equals(clicked)){
            unselect(x);
            clicked = null;
            Toast.makeText(ButtonSettingsActivity.this,"unclicked: "+x.getText().toString()+" "+Button_selected,Toast.LENGTH_LONG).show();
        } else if(!x.equals(clicked)) {
            unselect(clicked);
            clicked = x;  Button_selected = x.getTag().toString();
            select(x);
            Toast.makeText(ButtonSettingsActivity.this,"clicked: "+x.getText().toString()+" "+Button_selected,Toast.LENGTH_LONG).show();
        }
    }



    void select(Button x){
        if(x==null)return;
        Drawable background_fkt = getResources().getDrawable(R.drawable.buttonshape_square);
        SettingsApplier.setViewDesign(ButtonSettingsActivity.this,x,Color.RED);
    }

    void unselect(Button x){
        Drawable background_fkt = getResources().getDrawable(R.drawable.buttonshape_square);
        SettingsApplier.setViewDesign(ButtonSettingsActivity.this,x,Color.GRAY);
    }

    public void setBackgrounds(){
        Button[]Buttons= {btn_11,btn_12,btn_13,btn_14,btn_15,btn_16,
                          btn_21,btn_22,btn_23,btn_24,btn_25,btn_26,
                          save_settings};
        for(Button b: Buttons){
            SettingsApplier.setViewDesign(ButtonSettingsActivity.this,b,Color.GRAY);
        }
    }

    public static void setColor(Drawable background, int c){
        if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            shapeDrawable.getPaint().setColor(c);

        } else if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(c);
            int unselect = SettingsApplier.manipulateColor(c,0.8f);
            gradientDrawable.setStroke(5, unselect);
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(c);
        }

    }



    public void setUpButtons(String group){
        //L1
        setUpButton(btn_11,group+"_btn_11");
        setUpButton(btn_12,group+"_btn_12");
        setUpButton(btn_13,group+"_btn_13");
        setUpButton(btn_14,group+"_btn_14");
        setUpButton(btn_15,group+"_btn_15");
        setUpButton(btn_16,group+"_btn_16");

        //L2
        setUpButton(btn_21,group+"_btn_21");
        setUpButton(btn_22,group+"_btn_22");
        setUpButton(btn_23,group+"_btn_23");
        setUpButton(btn_24,group+"_btn_24");
        setUpButton(btn_25,group+"_btn_25");
        setUpButton(btn_26,group+"_btn_26");
    }

    public void setUpButton(Button x, String name){
        x.setText(PreferenceManager.getDefaultSharedPreferences(ButtonSettingsActivity.this).getString(name, name.split("_")[1]));
        select(x);
    }

    private void applyLanguage(){
        language = PreferenceManager.getDefaultSharedPreferences(ButtonSettingsActivity.this).getString("pref_lang_en","english");
        //Toast t =  Toast.makeText(ButtonSettingsActivity.this,"Lang: "+language,Toast.LENGTH_LONG);
        //t.show();
        if(language.equals("english") || language.equals("englisch")){
            save_settings.setText(R.string.save_settings_EN);
        }
        else if(language.equals("german") || language.equals("deutsch")){
            save_settings.setText(R.string.save_settings_DE);
        }
    }
}