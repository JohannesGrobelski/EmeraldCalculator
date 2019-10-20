package com.example.titancalculator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.helper.ArrayUtils;
import com.example.titancalculator.helper.MainDisplay.DesignApplier;
import com.example.titancalculator.helper.MainDisplay.OnSwipeTouchListener;
import com.example.titancalculator.helper.MainDisplay.SettingsApplier;
import com.example.titancalculator.helper.Math_String.MathEvaluator;
import com.example.titancalculator.helper.Math_String.NavigatableString;
import com.example.titancalculator.helper.Math_String.NumberString;
import com.example.titancalculator.helper.ShakeListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class CalcActivity_small extends AppCompatActivity {
    TableLayout small_background;

    static final int REQUEST_CODE_CONST = 1;  // The request code
    static final int REQUEST_CODE_CONV = 1;  // The request code
    static final int REQUEST_CODE_Verlauf = 1;  // The request code

    ArrayList<String> verlauf = new ArrayList<>();
    int buttonshapeID = R.drawable.buttonshape_square;
    String buttonfüllung="voll";
    DisplayMetrics screen_density;

    Set<String> UserFctGroups = new HashSet<>();

    String current_Callback="";
    String answer="";
    boolean solve_inst_pref=false;


    String language = "";
    String[] act_options;
    String[] mode_options;


    TableRow display;
    //L1
    Button btn_menu;
    Button btn_verlauf;

    Button btn_clear;
    Button btn_clearall;


    //G1
    Button btn_1;
    Button btn_2;
    Button btn_3;
    Button btn_4;
    Button btn_5;
    Button btn_6;
    Button btn_7;
    Button btn_8;
    Button btn_9;
    Button btn_0;
    Button btn_com;

    //G2
    Button btn_add;
    Button btn_sub;
    Button btn_mul;
    Button btn_div;
    Button btn_eq;


    EditText eT_eingabe;
    boolean eT_eingabe_hasFocus=true;
    EditText eT_ausgabe;

    NavigatableString I;


    String X = "";
    String Y = "";


    private Set<Button> BTN_ACT,BTN_FKT,BTN_NUMBERS,BTN_SAVES,BTN_SPECIALS;
    private ArrayList<Button> BTN_ALL;

    String mode = "BASIC";

    Animation buttonClick = new AlphaAnimation(1.0f, 0.6f);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        SettingsApplier.applySettings(CalcActivity_small.this);

        String mode = PreferenceManager.getDefaultSharedPreferences(this).getString("layout","");
        if(!mode.isEmpty()  && MainActivity.modes.contains(mode) && !mode.equals("small")){
            Intent conversionIntent=null;
            if(mode.equals("normal")){
                conversionIntent = new Intent(CalcActivity_small.this, CalcActivity_normal.class);
            }
            else if(mode.equals("science")){
                Log.e("transfer ","science");
                conversionIntent = new Intent(CalcActivity_small.this, CalcActivity_science.class);
            }
            conversionIntent.putExtra("verlauf",ArrayUtils.listToString(new ArrayList<String>(verlauf)));
            conversionIntent.putExtra("input",eT_eingabe.getText().toString());
            conversionIntent.putExtra("output",eT_ausgabe.getText().toString());
            conversionIntent.putExtra("swipeDir","");
            conversionIntent.putExtra("layout",mode);
            startActivity(conversionIntent);
            finish();
        }

        setTitle("Rechner");
        SettingsApplier.setColors(CalcActivity_small.this);
        applySettings();
        setBackgrounds();
        ArrayList<View> list = new ArrayList<View>() {{addAll(BTN_ACT);addAll(BTN_ALL);}};
        SettingsApplier.setFonts(CalcActivity_small.this,list);


        eT_ausgabe.setOnFocusChangeListener(focusListener);
        eT_eingabe.setOnFocusChangeListener(focusListener);

        mode = "BASIC";

        try {
            CalcActivity_science.setBackgroundImage(CalcActivity_small.this,small_background);
        } catch (Exception e) {
            e.printStackTrace();
        }
        applySettings();

        eingabeAddText(current_Callback);

        //TODO
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_small);
        Toast.makeText(CalcActivity_small.this,"mode: small",Toast.LENGTH_SHORT).show();
        mode = "";
        SettingsApplier.initSettings(CalcActivity_small.this);

        setTitle("Rechner");

        small_background = findViewById(R.id.small_background);




        eT_eingabe = findViewById(R.id.s_eT_Eingabe);
        eT_ausgabe = findViewById(R.id.s_eT_Ausgabe);

        eT_ausgabe.setOnFocusChangeListener(focusListener);
        eT_eingabe.setOnFocusChangeListener(focusListener);
        mode = "BASIC";


        eT_eingabe.setShowSoftInputOnFocus(false);
        eT_eingabe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(CalcActivity_small.this);
                //v.performClick();
                return false;
            }
        });

        eT_ausgabe.setShowSoftInputOnFocus(false);
        eT_ausgabe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(CalcActivity_small.this);
                //v.performClick();
                return false;
            }
        });


        small_background.setOnTouchListener(new OnSwipeTouchListener(CalcActivity_small.this) {
            public void onSwipeTop() {
                Toast.makeText(CalcActivity_small.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(CalcActivity_small.this, "right", Toast.LENGTH_SHORT).show();
                Intent conversionIntent = new Intent(CalcActivity_small.this, MainActivity.class);
                conversionIntent.putExtra("verlauf",ArrayUtils.listToString(new ArrayList<String>(verlauf)));
                conversionIntent.putExtra("input",eT_eingabe.getText().toString());
                conversionIntent.putExtra("output",eT_ausgabe.getText().toString());
                conversionIntent.putExtra("swipeDir","right");
                conversionIntent.putExtra("layout","small");
                startActivity(conversionIntent);
                finish();
            }
            public void onSwipeLeft() {
                Toast.makeText(CalcActivity_small.this, "left", Toast.LENGTH_SHORT).show();
                Intent conversionIntent = new Intent(CalcActivity_small.this, MainActivity.class);
                conversionIntent.putExtra("verlauf",ArrayUtils.listToString(new ArrayList<String>(verlauf)));
                conversionIntent.putExtra("input",eT_eingabe.getText().toString());
                conversionIntent.putExtra("output",eT_ausgabe.getText().toString());
                conversionIntent.putExtra("swipeDir","left");
                conversionIntent.putExtra("layout","small");
                startActivity(conversionIntent);
                finish();
            }
            public void onSwipeBottom() {
                Toast.makeText(CalcActivity_small.this, "bottom", Toast.LENGTH_SHORT).show();
            }
        });
        //setzt hintergrundbild


        I = new NavigatableString("content");

        eingabeSetText("");


        //find
        display = findViewById(R.id.s_display);
        //L1
        btn_menu = findViewById(R.id.s_btn_menu);
        btn_verlauf = findViewById(R.id.s_btn_verlauf);

        btn_clear = findViewById(R.id.s_btn_clear);
        btn_clearall = findViewById(R.id.s_btn_clearall);


        //G1
        btn_1 = findViewById(R.id.s_btn_1);
        btn_2 = findViewById(R.id.s_btn_2);
        btn_3 = findViewById(R.id.s_btn_3);
        btn_4 = findViewById(R.id.s_btn_4);
        btn_5 = findViewById(R.id.s_btn_5);
        btn_6 = findViewById(R.id.s_btn_6);
        btn_7 = findViewById(R.id.s_btn_7);
        btn_8 = findViewById(R.id.s_btn_8);
        btn_9 = findViewById(R.id.s_btn_9);
        btn_0 = findViewById(R.id.s_btn_0);
        btn_com = findViewById(R.id.s_btn_com);

        //G2
        btn_add = findViewById(R.id.s_btn_add);
        btn_sub = findViewById(R.id.s_btn_sub);
        btn_mul = findViewById(R.id.s_btn_mul);
        btn_div = findViewById(R.id.s_btn_div);
        btn_eq = findViewById(R.id.s_btn_eq);

        BTN_ACT = new HashSet<>(Arrays.asList(new Button[]{btn_menu, btn_verlauf}));
        BTN_FKT = new HashSet<>(Arrays.asList(new Button[]{btn_clear, btn_clearall}));
        BTN_NUMBERS = new HashSet<>(Arrays.asList(new Button[]{btn_com, btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9}));
        BTN_SAVES = new HashSet<>(Arrays.asList(new Button[]{}));
        BTN_SPECIALS = new HashSet<>(Arrays.asList(new Button[]{btn_mul, btn_div, btn_sub, btn_add, btn_eq}));
        BTN_ALL = new ArrayList<>();
        BTN_ALL.addAll(BTN_ACT);
        BTN_ALL.addAll(BTN_FKT);
        BTN_ALL.addAll(BTN_NUMBERS);
        BTN_ALL.addAll(BTN_SAVES);
        BTN_ALL.addAll(BTN_SPECIALS);

        applySettings();
        SettingsApplier.setColors(CalcActivity_small.this);
        setBackgrounds();
        ArrayList<View> list = new ArrayList<View>() {{addAll(BTN_ALL);}};
        SettingsApplier.setFonts(CalcActivity_small.this,list);

        try {
            CalcActivity_science.setBackgroundImage(CalcActivity_small.this,small_background);
        } catch (Exception e) {
            e.printStackTrace();
        }



        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                current_Callback = "";
                Intent conversionIntent = new Intent(CalcActivity_small.this, SettingsActivity.class);
                startActivity(conversionIntent);
            }
        });


        btn_verlauf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                current_Callback = "";
                Intent verlaufIntent = new Intent(CalcActivity_small.this, HistoryActivity.class);
                String[] verlaufarray = verlauf.toArray(new String[verlauf.size()]);
                verlaufIntent.putExtra("verlauf", verlaufarray);
                String[] arrayVerlauf = verlaufIntent.getStringArrayExtra("verlauf");
                //Toast.makeText(CalcActivity_small.this,Arrays.toString(arrayVerlauf),Toast.LENGTH_LONG).show();

                startActivityForResult(verlaufIntent, REQUEST_CODE_Verlauf);
            }
        });


        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                //TODO: TAN( etc. vollständig löschen
                eingabeClear();
                ausgabe_setText("");
                setBackground(btn_clear);
                if(!CalcActivity_science.noImmidiateOps.contains(I.getDisplayableString().trim())){
                    if(solve_inst_pref){
                        answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                        if(!answer.equals("Math Error"))ausgabe_setText(answer);
                    }
                }
            }
        });
        btn_clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                eingabeSetText("");
                ausgabe_setText("");
                setBackground(btn_clearall);
            }
        });



        //G1
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("1");
                setBackground(btn_1);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }

            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("2");
                setBackground(btn_2);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("3");
                setBackground(btn_3);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }
            }
        });
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("4");
                setBackground(btn_4);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }
            }
        });
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("5");
                setBackground(btn_5);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }
            }
        });
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("6");
                setBackground(btn_6);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }
            }
        });
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("7");
                setBackground(btn_7);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }
            }
        });
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("8");
                setBackground(btn_8);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }
            }
        });
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("9");
                setBackground(btn_9);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }
            }
        });
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("0");
                setBackground(btn_0);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }
            }
        });
        btn_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText(".");
                setBackground(btn_com);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("+");
                setBackground(btn_add);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }
            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("-");
                setBackground(btn_sub);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }
            }
        });
        btn_mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("*");
                setBackground(btn_mul);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }
            }
        });
        btn_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("/");
                setBackground(btn_div);
                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }
            }
        });
        btn_eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                if (verlauf == null) verlauf = new ArrayList<>();
                verlauf.add(I.getDisplayableString());

                answer = I.getResult(CalcActivity_science.getBase(CalcActivity_small.this));
                ausgabe_setText(answer);
                setBackground(btn_eq);
            }
        });

        setBackground(btn_clear);
        setBackground(btn_clearall);

        Intent v = getIntent();
        eT_eingabe.setText( v.getStringExtra("input"));
        eT_ausgabe.setText( v.getStringExtra("output"));
        verlauf = ArrayUtils.stringToList(v.getStringExtra("verlauf"));
    }



    private void ausgabe_setText(String res) {
        //Toast.makeText(CalcActivity_small.this,"Ausgabe: "+res,Toast.LENGTH_SHORT).show();
        eT_ausgabe.setText(res);
    }

    public static void setDefaultColors(){
        SettingsApplier.setDefaultColors();
    }



    void setBackground(View x){
        if(buttonshapeID==0)applySettings();
        Drawable background;
        SettingsApplier.setColors(CalcActivity_small.this);
        float factor_font = SettingsApplier.getDarker_factor_font(CalcActivity_small.this);


        boolean stroke = true;

        //Default Case
        background = getResources().getDrawable(buttonshapeID);
        SettingsApplier.setColor(CalcActivity_small.this,background, SettingsApplier.getColor_specials(CalcActivity_small.this),buttonfüllung,stroke);
        int darker = SettingsApplier.manipulateColor(SettingsApplier.getColor_specials(CalcActivity_small.this),factor_font);
        SettingsApplier.setTextColor(x,darker);

        if(x instanceof Button){
            //fix für größe dieser kleinen unicode symbole
            if(((Button) x).getText().equals(getResources().getString(R.string.CLEAR_ALL)) || ((Button) x).getText().equals(getResources().getString(R.string.CLEAR))){
                ((Button) x).setTextSize(Math.min(30,((Button)btn_0).getTextSize()*2));
            }
        }

        if(x.equals(display)){
            background = getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor(CalcActivity_small.this,background, SettingsApplier.getColor_display(CalcActivity_small.this),buttonfüllung,stroke);
            eT_ausgabe.setTextColor(SettingsApplier.getColor_displaytext(CalcActivity_small.this));
            eT_eingabe.setTextColor(SettingsApplier.getColor_displaytext(CalcActivity_small.this));
            x.setBackground(background);
        }

        if(x.equals(btn_verlauf)){

            darker = SettingsApplier.manipulateColor(SettingsApplier.getColor_act(CalcActivity_small.this),factor_font);
            if(DesignApplier.getBrightness(DesignApplier.transToRGB(darker)) < 20){
                darker = 0xffFFFFFF;
            }
            Drawable vector =  getResources().getDrawable(R.drawable.ic_verlauf);
            vector.setColorFilter(darker, PorterDuff.Mode.SRC_ATOP);

            background = getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor((CalcActivity_small.this),background, SettingsApplier.getColor_act(CalcActivity_small.this),buttonfüllung,stroke);
            darker = SettingsApplier.manipulateColor(SettingsApplier.getColor_act(CalcActivity_small.this),factor_font);
            SettingsApplier.setTextColor(x,darker);

            x.setBackground( SettingsApplier.combineVectorBackground(vector,background));
            ((Button) x).setText("");
            return;
        }

        else if(BTN_ACT.contains(x)){
            background = getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor(CalcActivity_small.this,background, SettingsApplier.getColor_act(CalcActivity_small.this),buttonfüllung,stroke);
            darker = SettingsApplier.manipulateColor(SettingsApplier.getColor_act(CalcActivity_small.this),factor_font);
            SettingsApplier.setTextColor(x,darker);
        }
        else if(BTN_FKT.contains(x)){
            background = getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor(CalcActivity_small.this,background, SettingsApplier.getColor_fkt(CalcActivity_small.this),buttonfüllung,stroke);
            darker = SettingsApplier.manipulateColor(SettingsApplier.getColor_fkt(CalcActivity_small.this),factor_font);
            SettingsApplier.setTextColor(x,darker);
        }
        else if(BTN_NUMBERS.contains(x)){
            background = getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor(CalcActivity_small.this,background, SettingsApplier.getColor_numbers(CalcActivity_small.this),buttonfüllung,stroke);
            darker = SettingsApplier.manipulateColor(SettingsApplier.getColor_numbers(CalcActivity_small.this),factor_font);
            SettingsApplier.setTextColor(x,darker);
        }
        else if(BTN_SAVES.contains(x)){
            background = getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor(CalcActivity_small.this,background, SettingsApplier.getColor_saves(CalcActivity_small.this),buttonfüllung,stroke);
            darker = SettingsApplier.manipulateColor(SettingsApplier.getColor_saves(CalcActivity_small.this),factor_font);
            SettingsApplier.setTextColor(x,darker);
        }

        x.setBackground(background);

    }




    void setBackgrounds(){
        display.setBackgroundColor(SettingsApplier.getColor_display(CalcActivity_small.this));
        small_background.setBackgroundColor(SettingsApplier.getColor_background(CalcActivity_small.this));

        //setBackground(spinner_shift);


        for(Button b: BTN_ALL){
            setBackground(b);
        }

        setBackground(display);
    }




    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_CODE_CONST) {
            if (resultCode == RESULT_OK) {
                //Use Data to get string
                current_Callback = data.getStringExtra("RESULT_STRING");
            }
        }
        if (requestCode == REQUEST_CODE_CONV) {
            if (resultCode == RESULT_OK) {
                //Use Data to get string
                current_Callback = data.getStringExtra("RESULT_STRING");
            }
        }
        if (requestCode == REQUEST_CODE_Verlauf) {
            if (resultCode == RESULT_OK) {
                //Use Data to get string
                current_Callback = data.getStringExtra("RESULT_STRING");
                if(!current_Callback.isEmpty()){
                    //eingabeSetText(current_Callback);
                    //Toast.makeText(CalcActivity_small.this, "SET: "+current_Callback, Toast.LENGTH_LONG).show();

                    //ausgabe_setText("");
                }
            }
        }
    }


    void transBtnFct(String fct){
        if(fct.startsWith("btn"))return;

        //"PI","E","NCR","NPR","%","!N","^","A/B","x\u207B\u00B9","+/-","√","\u00B3√","LOG","LN","LB","SIN","COS","TAN","ASIN","ATAN","ASINH","ACOSH","ATANH","SINH","COSH","TANH"};
        if(fct.equals("%")){
            ausgabe_setText(I.getPercent(CalcActivity_science.getBase(CalcActivity_small.this)));
            return;
        }
        else if(fct.equals("A/B")){
            ausgabe_setText(I.getBruch(CalcActivity_science.getBase(CalcActivity_small.this)));
            return;
        }
        else if(fct.equals("x\u207B\u00B9")){
            ausgabe_setText(I.getReciproke(CalcActivity_science.getBase(CalcActivity_small.this)));
            return;
        }
        else if(fct.equals("+/-")){
            ausgabe_setText(I.getInvert(CalcActivity_science.getBase(CalcActivity_small.this)));
            return;
        }

        String A = fct;


        A = A.replace("NCR","C");
        A = A.replace("NCR","C");
        A = A.replace("!N","!");

        A = A.replace("x\u207B\u00B9","C");


        eingabeAddText(A);

    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void eingabeAddText(String i){
        if(eT_eingabe_hasFocus){
            eT_eingabe.clearFocus();

            eT_eingabe.getText().insert(eT_eingabe.getSelectionStart(), i);
            I.setText(eT_eingabe.getText().toString());
        }
    }

    public void eingabeClear(){
        if(eT_eingabe_hasFocus) {
            int pos = eT_eingabe.getSelectionStart();
            I.clear(eT_eingabe.getSelectionStart());
            eingabeSetText(I.getDisplayableString());
            eT_eingabe.setSelection(Math.max(0,pos-1));
        }
    }

    public void eingabeSetText(String i){
        if(eT_eingabe_hasFocus) {
            eT_eingabe.setText(i);
            I.setText(eT_eingabe.getText().toString());
        }
    }



    private void applySettings(){
        //language
        language = PreferenceManager.getDefaultSharedPreferences(CalcActivity_small.this).getString("pref_lang","english");
        if(language.equals("english") || language.equals("englisch")){
            btn_menu.setText(R.string.MENU_EN);
            //btn_verlauf.setText(R.string.VERLAUFEN);
            act_options = getResources().getStringArray(R.array.act_EN);
            mode_options = FunctionGroupSettingsActivity.getGroups(CalcActivity_small.this);
        }
        else if(language.equals("german") || language.equals("deutsch")){
            btn_menu.setText(R.string.MENU_DE);
            //btn_verlauf.setText(R.string.VERLAUFDE);
            act_options = getResources().getStringArray(R.array.act_DE);
            mode_options = FunctionGroupSettingsActivity.translateGroup(FunctionGroupSettingsActivity.getGroups(CalcActivity_small.this),"german");
        }

        //UserFctGroups.addAll(mode_options); UserFctGroups
        //Toast.makeText(CalcActivity_small.this,"Modes: "+Arrays.toString(mode_options),Toast.LENGTH_SHORT).show();
        UserFctGroups = new HashSet<>(Arrays.asList(FunctionGroupSettingsActivity.getUserGroups(CalcActivity_small.this)));

        //numbers
        if (PreferenceManager.getDefaultSharedPreferences(CalcActivity_small.this).contains("pref_precision")) {
            String prec = PreferenceManager.getDefaultSharedPreferences(CalcActivity_small.this).getString("pref_precision","10");
            if(prec != null)NumberString.precision =  Integer.valueOf(prec) + 1;
        }

        //buttonshape
        buttonshapeID = SettingsApplier.getButtonshapeID();


        //buttonfüllung
        if (PreferenceManager.getDefaultSharedPreferences(CalcActivity_small.this).contains("buttonfüllung")) {
            buttonfüllung = PreferenceManager.getDefaultSharedPreferences(CalcActivity_small.this).getString("buttonfüllung","voll");
        }

        //Fonts
        //Typeface font = Typeface.createFromAsset(getAssets(), "Crashed Scoreboard.ttf");
        Typeface monospace = Typeface.create("MONOSPACE",Typeface.NORMAL);
        Typeface sansSerif = Typeface.create("SANS_SERIF",Typeface.NORMAL);
        Typeface serif = Typeface.create("SERIF",Typeface.NORMAL);

        //Math Settings
        solve_inst_pref = PreferenceManager.getDefaultSharedPreferences(CalcActivity_small.this).getBoolean("solve_inst_pref",false);
        MathEvaluator.applySettings(CalcActivity_small.this);

    }

    public boolean checkPermissionForReadExtertalStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExtertalStorage(Context context) throws Exception {
        try {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Integer.parseInt(Manifest.permission.READ_EXTERNAL_STORAGE));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                if(v.equals(eT_eingabe)){
                    eT_eingabe_hasFocus = true;
                }
                else eT_eingabe_hasFocus = false;
            } else {
                eT_eingabe_hasFocus = false;
            }
        }
    };

}


