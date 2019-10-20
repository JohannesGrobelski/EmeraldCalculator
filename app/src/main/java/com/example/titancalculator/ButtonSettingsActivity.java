package com.example.titancalculator;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.helper.MainDisplay.SettingsApplier;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class ButtonWrapper{
    String Group="";
    String ID="";
    Button view;



    public ButtonWrapper(View b, String ID){
        view = (Button) b;
        this.ID = ID;
    }
}

public class ButtonSettingsActivity extends AppCompatActivity {
    String language="";
    String[] validFct = {"PI","E","NCR","NPR","%","!N","^","A/B","x\u207B\u00B9","+/-","√","\u00B3√","LOG","LN","LB","SIN","COS","TAN","ASIN","ATAN","ASINH","ACOSH","ATANH","SINH","COSH","TANH"};
    private final Set<String> validFCT = new HashSet<>(Arrays.asList(validFct));

    ButtonWrapper clicked;
    String current_group="";
    ListView lv_group;


    //L2
    ButtonWrapper btn_11;
    ButtonWrapper btn_12;
    ButtonWrapper btn_13;
    ButtonWrapper btn_14;
    ButtonWrapper btn_15;
    ButtonWrapper btn_16;

    //L3
    ButtonWrapper btn_21;
    ButtonWrapper btn_22;
    ButtonWrapper btn_23;
    ButtonWrapper btn_24;
    ButtonWrapper btn_25;
    ButtonWrapper btn_26;


    Button save_settings;
    ListView LV_fct_sel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("ButtonWrapper Settings");
        setContentView(R.layout.activity_button_settings);

        String[] ug = FunctionGroupSettingsActivity.getUserGroups(ButtonSettingsActivity.this);

        if(ug.length > 0){
            //L2
            btn_11 = new ButtonWrapper(findViewById(R.id.btn_11),"btn11");
            btn_12 = new ButtonWrapper(findViewById(R.id.btn_12),"btn12");
            btn_13 = new ButtonWrapper(findViewById(R.id.btn_13),"btn13");
            btn_14 = new ButtonWrapper(findViewById(R.id.btn_14),"btn14");
            btn_15 = new ButtonWrapper(findViewById(R.id.btn_15),"btn15");
            btn_16 = new ButtonWrapper(findViewById(R.id.btn_16),"btn16");

            //L3
            btn_21 = new ButtonWrapper(findViewById(R.id.btn_21),"btn21");
            btn_22 = new ButtonWrapper(findViewById(R.id.btn_22),"btn22");
            btn_23 = new ButtonWrapper(findViewById(R.id.btn_23),"btn23");
            btn_24 = new ButtonWrapper(findViewById(R.id.btn_24),"btn24");
            btn_25 = new ButtonWrapper(findViewById(R.id.btn_25),"btn25");
            btn_26 = new ButtonWrapper(findViewById(R.id.btn_26),"btn26");

            setUpButtons("");

            save_settings = findViewById(R.id.save_settings);
            applyLanguage();
            ArrayAdapter adapter_fct = new ArrayAdapter<String>(this,R.layout.lvitem_layout, validFct);
            LV_fct_sel = (ListView) findViewById(R.id.LV_fct_sel);
            LV_fct_sel.setAdapter(adapter_fct);

            //L2
            //normal: PI,E,CONST,CONV
            btn_11.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_11);
                }
            });
            btn_12.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_12);

                }
            });
            btn_13.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_13);
                }
            });
            btn_14.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_14);
                }
            });
            btn_15.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_15);
                }
            });
            btn_16.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_16);
                }
            });

            //L3
            //normal: %,!,^,a/b,x^-1,+/-
            //TRIGO: ASINH,ACOSH,ATANH,SINH,COSH,TANH
            btn_21.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_21);

                }
            });
            btn_22.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_22);

                }
            });
            btn_23.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_23);

                }
            });
            btn_24.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_24);

                }
            });
            btn_25.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_25);

                }
            });
            btn_26.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click(btn_26);

                }
            });


            lv_group = findViewById(R.id.lv_group);
            current_group = ug[0];
            setUpButtons(current_group);

            Toast.makeText(ButtonSettingsActivity.this,"Modes: "+Arrays.toString(ug),Toast.LENGTH_SHORT).show();
            ArrayAdapter adapter_groups = new ArrayAdapter<String>(this,R.layout.lvitem_layout, ug);
            lv_group.setAdapter(adapter_groups);

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
                        clicked.view.setText(inh);

                        //Einstellung speichern
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ButtonSettingsActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        String key = current_group+"_"+clicked.ID;
                        String value = clicked.view.getText().toString().trim();
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
        }
        else{
            Toast.makeText(ButtonSettingsActivity.this,"no groups defined!",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    void click(ButtonWrapper x){
        if(x.equals(clicked)){
            Toast t =  Toast.makeText(ButtonSettingsActivity.this,"unclicked: "+x.view.getText().toString(),Toast.LENGTH_SHORT);
            t.show();
            brighter(x.view);
            clicked.view = new Button(this);
        } else if(!x.equals(clicked)) {
            Toast t =  Toast.makeText(ButtonSettingsActivity.this,"clicked: "+x.view.getText().toString(),Toast.LENGTH_SHORT);
            t.show();
            if(clicked != null){
                brighter(clicked.view);
            }

            clicked = x;
            darker(x.view);
        }
    }



    void brighter(Button x){
        if(x==null)return;
        Drawable background_fkt = getResources().getDrawable(R.drawable.buttonshape_square);
        int color_act = PreferenceManager.getDefaultSharedPreferences(ButtonSettingsActivity.this).getInt("actColor", 0xffff0000);
        setColor(background_fkt, color_act);
        x.setBackground(background_fkt);
    }

    void darker(Button x){
        Drawable background_fkt = getResources().getDrawable(R.drawable.buttonshape_square);
        int color_act = PreferenceManager.getDefaultSharedPreferences(ButtonSettingsActivity.this).getInt("actColor", 0xffff0000);
        setColor(background_fkt, SettingsApplier.manipulateColor(color_act,0.8f));
        x.setBackground(background_fkt);
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
            int darker = SettingsApplier.manipulateColor(c,0.8f);
            gradientDrawable.setStroke(5, darker);
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(c);
        }

    }



    public void setUpButtons(String group){
        //L1
        setUpButton(btn_11.view,group+"_btn11");
        setUpButton(btn_12.view,group+"_btn12");
        setUpButton(btn_13.view,group+"_btn13");
        setUpButton(btn_14.view,group+"_btn14");
        setUpButton(btn_15.view,group+"_btn15");
        setUpButton(btn_16.view,group+"_btn16");

        //L2
        setUpButton(btn_21.view,group+"_btn21");
        setUpButton(btn_22.view,group+"_btn22");
        setUpButton(btn_23.view,group+"_btn23");
        setUpButton(btn_24.view,group+"_btn24");
        setUpButton(btn_25.view,group+"_btn25");
        setUpButton(btn_26.view,group+"_btn26");
    }

    public void setUpButton(Button x, String name){
        x.setText(PreferenceManager.getDefaultSharedPreferences(ButtonSettingsActivity.this).getString(name, name.split("_")[1]));
        brighter(x);
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
