package com.example.titancalculator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.LinearLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.titancalculator.helper.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class CalcActivity_normal extends AppCompatActivity {
    private boolean onPotraitReturnScience = false;
    int VIEW_digit_group_cnt = 0;
    String state_spinner_shift = "number_selection"; //number_selection, base_selection
    String VIEW_1_text="";String VIEW_2_text="";String VIEW_3_text="";String VIEW_4_text="";String VIEW_5_text="";String VIEW_6_text="";String VIEW_7_text="";String VIEW_8_text="";String VIEW_9_text="";
    LinearLayout normal_background;

    static final int REQUEST_CODE_CONST = 1;  // The request code
    static final int REQUEST_CODE_CONV = 1;  // The request code
    static final int REQUEST_CODE_Verlauf = 1;  // The request code

    ArrayList<String> verlauf = new ArrayList<>();
    DisplayMetrics screen_density;

    Set<String> UserFctGroups = new HashSet<>();

    String current_Callback="";
    String answer="";
    boolean solve_inst_pref=false;

    private static String[] MEMORY = new String[6];

    boolean shake = false;
    private ShakeListener mShaker;

    String language = "";
    String[] act_options;
    String[] mode_options;

    LinearLayout background;
    LinearLayout display;
    //L1
    Button VIEW_CONV;
    Button VIEW_CONST;
    Button VIEW_verlauf;
    Button VIEW_menu;
    Spinner spinner_shift;
    Spinner spinner_base;

    Button VIEW_clear;
    Button VIEW_clearall;

    //L2
    Button VIEW_11;
    Button VIEW_12;
    Button VIEW_13;
    Button VIEW_14;
    Button VIEW_15;
    Button VIEW_16;

    //L3
    Button VIEW_21;
    Button VIEW_22;
    Button VIEW_23;
    Button VIEW_24;
    Button VIEW_25;
    Button VIEW_26;

    //L5
    //Button VIEW_X;
    //Button VIEW_Y;
    //Button VIEW_FKT1;
    //Button VIEW_FKT2;
    Button VIEW_LINKS;
    Button VIEW_RECHTS;



    //G2
    Button VIEW_open_bracket;
    Button VIEW_close_bracket;
    Button VIEW_add;
    Button VIEW_sub;
    Button VIEW_mul;
    Button VIEW_div;
    Button VIEW_ans;
    Button VIEW_eq;


    EditText eT_eingabe;
    boolean eT_eingabe_hasFocus=true;
    EditText eT_ausgabe;

    NavigatableString I;

    String X = "";
    String Y = "";

    LinearLayout LN2;
    LinearLayout LN3;
    LinearLayout LN4;
    LinearLayout LN5;
    LinearLayout LN6;


    private Set<Button> VIEW_ACT;
    private Set<Button> VIEW_FKT;
    private Set<Button> VIEW_FOPS;
    private Set<Button> VIEW_SAVES;
    private Set<Button> VIEW_SPECIALS;
    private ArrayList<Button> VIEW_ALL;

    String mode = "NUMBER";

    Animation buttonClick = new AlphaAnimation(1.0f, 0.6f);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == ORIENTATION_LANDSCAPE) {

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            if(onPotraitReturnScience){
                Intent conversionIntent= new Intent(CalcActivity_normal.this, CalcActivity_science.class);

                conversionIntent.putExtra("verlauf",ArrayUtils.listToString(new ArrayList<String>(verlauf)));
                conversionIntent.putExtra("input",eT_eingabe.getText().toString());
                conversionIntent.putExtra("output",eT_ausgabe.getText().toString());
                conversionIntent.putExtra("swipeDir","");
                conversionIntent.putExtra("layout","science");
                startActivity(conversionIntent);
                finish();
                return;
            }

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SettingsApplier.applySettings(CalcActivity_normal.this);

        String mode = PreferenceManager.getDefaultSharedPreferences(this).getString("layout","");
        if(!mode.isEmpty()  && MainActivity.modes.contains(mode) && !mode.equals("normal")){
            Intent conversionIntent=null;
            if(mode.equals("small")){
                conversionIntent = new Intent(CalcActivity_normal.this, CalcActivity_small.class);
            }
            else if(mode.equals("science")){
                if(getIntent().hasExtra("onPotraitReturn")){
                    if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                        //conversionIntent = new Intent(CalcActivity_normal.this, CalcActivity_normal.class);
                        return;
                    }
                    else return;
                }
                else return;
            }
            conversionIntent.putExtra("verlauf",ArrayUtils.listToString(new ArrayList<String>(verlauf)));
            conversionIntent.putExtra("input",eT_eingabe.getText().toString());
            conversionIntent.putExtra("output",eT_ausgabe.getText().toString());
            conversionIntent.putExtra("swipeDir","");
            conversionIntent.putExtra("layout",mode);
            startActivity(conversionIntent);
            finish();
            return;
        }

        setTitle("Rechner");
        SettingsApplier.setColors(CalcActivity_normal.this);
        applySettings();
        setBackgrounds();
        ArrayList<View> list = new ArrayList<View>() {{addAll(VIEW_ALL);}};
        SettingsApplier.setFonts(CalcActivity_normal.this,list);


        eT_ausgabe.setOnFocusChangeListener(focusListener);
        eT_eingabe.setOnFocusChangeListener(focusListener);

        spinner_base = findViewById(R.id.m_spinner_base);
        //spinner_shift = findViewById(R.id.spinner_SHIFT);
        //spinner_shift.setSelection(0);
        mode = "BASIC";

        //ArrayAdapter adpt_modeoptions = new ArrayAdapter<String>(this, R.layout.lvitem_layout, mode_options);
        SettingsApplier.setArrayAdapter(CalcActivity_normal.this,spinner_shift,mode_options,SettingsApplier.getColor_fops(CalcActivity_normal.this));

        setUp_spinner_base();

        try {
            SettingsApplier.setBackgroundImage(CalcActivity_normal.this,normal_background);
        } catch (Exception e) {
            e.printStackTrace();
        }
        applySettings();

        eingabeAddText(current_Callback);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_normal);
        SettingsApplier.initSettings(CalcActivity_normal.this);

        if(getIntent().hasExtra("onPotraitReturnScience")){
            Toast.makeText(CalcActivity_normal.this,"onPotraitReturnScience",Toast.LENGTH_SHORT).show();
            onPotraitReturnScience = true;
        }
        mode = "";

        SettingsApplier.setButtonshapeID("round");

        Log.v("Act ","normal");
        setTitle("Rechner");
        normal_background = findViewById(R.id.science_background);

        LN4 = findViewById(R.id.LN4);
        LN5 = findViewById(R.id.LN5);
        LN6 = findViewById(R.id.LN6);

        eT_eingabe = findViewById(R.id.m_eT_Eingabe);
        eT_ausgabe = findViewById(R.id.m_eT_Ausgabe);

        eT_ausgabe.setOnFocusChangeListener(focusListener);
        eT_eingabe.setOnFocusChangeListener(focusListener);
        mode = "BASIC";

        MEMORY = new String[6];
        MEMORY = CalcActivity_science.loadMemory(CalcActivity_normal.this);

        spinner_base = findViewById(R.id.m_spinner_base);

        eT_eingabe.setShowSoftInputOnFocus(false);
        eT_eingabe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(CalcActivity_normal.this);
                //v.performClick();
                return false;
            }
        });

        eT_ausgabe.setShowSoftInputOnFocus(false);
        eT_ausgabe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(CalcActivity_normal.this);
                //v.performClick();
                return false;
            }
        });

        normal_background.setOnTouchListener(new OnSwipeTouchListener(CalcActivity_normal.this) {
            public void onSwipeTop() {
                Toast.makeText(CalcActivity_normal.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(CalcActivity_normal.this, "right", Toast.LENGTH_SHORT).show();
                Intent conversionIntent = new Intent(CalcActivity_normal.this, MainActivity.class);
                conversionIntent.putExtra("verlauf",ArrayUtils.listToString(new ArrayList<String>(verlauf)));
                conversionIntent.putExtra("input",eT_eingabe.getText().toString());
                conversionIntent.putExtra("output",eT_ausgabe.getText().toString());
                conversionIntent.putExtra("swipeDir","right");
                conversionIntent.putExtra("layout","normal");
                startActivity(conversionIntent);
                finish();
                return;
            }
            public void onSwipeLeft() {
                Toast.makeText(CalcActivity_normal.this, "left", Toast.LENGTH_SHORT).show();
                Intent conversionIntent = new Intent(CalcActivity_normal.this, MainActivity.class);
                conversionIntent.putExtra("verlauf",ArrayUtils.listToString( new ArrayList<String>(verlauf)));
                conversionIntent.putExtra("input",eT_eingabe.getText().toString());
                conversionIntent.putExtra("output",eT_ausgabe.getText().toString());
                conversionIntent.putExtra("swipeDir","left");
                conversionIntent.putExtra("layout","normal");
                startActivity(conversionIntent);
                finish();
                return;
            }
            public void onSwipeBottom() {
                Toast.makeText(CalcActivity_normal.this, "bottom", Toast.LENGTH_SHORT).show();
            }
        });

        //setzt hintergrundbild

        I = new NavigatableString("content");

        eingabeSetText("");

        //find
        display = findViewById(R.id.m_display);
        background = findViewById(R.id.science_background);
        //L1
        spinner_shift = findViewById(R.id.m_spinner_SHIFT);
        VIEW_CONV = findViewById(R.id.m_btn_CONV);
        VIEW_verlauf = findViewById(R.id.m_btn_Verlauf);
        VIEW_CONST = findViewById(R.id.m_btn_CONST);
        VIEW_menu = findViewById(R.id.m_btn_menu);
        VIEW_clear = findViewById(R.id.m_btn_clear);
        VIEW_clearall = findViewById(R.id.m_btn_clearall);

        //L2
        //normal: PI,E,->DEC,->BIN,->OCT
        //TRIGO: SIN,COS,TAN,ASIN,ACOS,ATAN

        VIEW_11 = findViewById(R.id.m_btn_11);
        VIEW_12 = findViewById(R.id.m_btn_12);
        VIEW_13 = findViewById(R.id.m_btn_13);
        VIEW_14 = findViewById(R.id.m_btn_14);
        VIEW_15 = findViewById(R.id.m_btn_15);
        VIEW_16 = findViewById(R.id.m_btn_16);

        //L3
        //normal: %,!,^,a/b,x^-1,+/-
        //TRIGO: ASINH,ACOSH,ATANH,SINH,COSH,TANH
        VIEW_21 = findViewById(R.id.m_btn_21);
        VIEW_22 = findViewById(R.id.m_btn_22);
        VIEW_23 = findViewById(R.id.m_btn_23);
        VIEW_24 = findViewById(R.id.m_btn_24);
        VIEW_25 = findViewById(R.id.m_btn_25);
        VIEW_26 = findViewById(R.id.m_btn_26);

        //L5
        //VIEW_X = findViewById(R.id.m_btn_X);
        //VIEW_Y = findViewById(R.id.m_btn_Y);
        //VIEW_FKT1 = findViewById(R.id.m_btn_FKT1);
        //VIEW_FKT2 = findViewById(R.id.m_btn_FKT2);
        VIEW_LINKS = findViewById(R.id.m_btn_LINKS);
        VIEW_RECHTS = findViewById(R.id.m_btn_RECHTS);



        //G2
        VIEW_open_bracket = findViewById(R.id.m_btn_open_bracket);
        VIEW_close_bracket = findViewById(R.id.m_btn_close_bracket);
        VIEW_add = findViewById(R.id.m_btn_add);
        VIEW_sub = findViewById(R.id.m_btn_sub);
        VIEW_mul = findViewById(R.id.m_btn_mul);
        VIEW_div = findViewById(R.id.m_btn_div);
        VIEW_ans = findViewById(R.id.m_btn_ans);
        VIEW_eq = findViewById(R.id.m_btn_eq);

        VIEW_ACT = new HashSet<>();
        VIEW_FKT = new HashSet<>(Arrays.asList(new Button[]{VIEW_LINKS, VIEW_RECHTS}));
        VIEW_FOPS = new HashSet<>(Arrays.asList(new Button[]{VIEW_11, VIEW_12, VIEW_13, VIEW_14, VIEW_15, VIEW_16, VIEW_21, VIEW_22, VIEW_23, VIEW_24, VIEW_25, VIEW_26}));
        VIEW_SAVES = new HashSet<>(Arrays.asList(new Button[]{}));
        VIEW_SPECIALS = new HashSet<>(Arrays.asList(new Button[]{VIEW_open_bracket, VIEW_close_bracket, VIEW_mul, VIEW_div, VIEW_sub, VIEW_add, VIEW_ans, VIEW_eq}));
        VIEW_ALL = new ArrayList<>();
        VIEW_ALL.addAll(VIEW_ACT);
        VIEW_ALL.addAll(VIEW_FKT);
        VIEW_ALL.addAll(VIEW_FOPS);
        VIEW_ALL.addAll(VIEW_SAVES);
        VIEW_ALL.addAll(VIEW_SPECIALS);

        applySettings();
        setBackgrounds();
        SettingsApplier.setColors(CalcActivity_normal.this);

        ArrayList<View> list = new ArrayList<View>() {{addAll(VIEW_ALL);}};
        SettingsApplier.setFonts(CalcActivity_normal.this,list);

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

        //L1
        SettingsApplier.setArrayAdapter(CalcActivity_normal.this,spinner_shift,mode_options,SettingsApplier.getColor_fops(CalcActivity_normal.this));

        spinner_shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(SettingsApplier.getColor_fkt(CalcActivity_normal.this));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextColor(SettingsApplier.getColor_fkt(CalcActivity_normal.this));
            }
        });

        VIEW_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                current_Callback = "";
                Intent conversionIntent = new Intent(CalcActivity_normal.this, SettingsActivity.class);
                startActivity(conversionIntent);
            }
        });

        spinner_shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mode = spinner_shift.getSelectedItem().toString();

                assignModeFct();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        /*
        VIEW_MENU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                current_Callback = "";
                Intent conversionIntent = new Intent(CalcActivity_normal.this,SettingsActivity.class);
                startActivity(conversionIntent);

                //TODO

            }
        });

         */
        VIEW_CONV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                current_Callback = "";
                Intent conversionIntent = new Intent(CalcActivity_normal.this, ConversionActivity.class);
                startActivityForResult(conversionIntent, REQUEST_CODE_CONV);

                //TODO

            }
        });
        VIEW_CONST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                current_Callback = "";
                Intent constIntent = new Intent(CalcActivity_normal.this, ConstantsActivity.class);
                startActivityForResult(constIntent, REQUEST_CODE_CONST);

            }
        });
        VIEW_verlauf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                current_Callback = "";
                Intent verlaufIntent = new Intent(CalcActivity_normal.this, HistoryActivity.class);
                String[] verlaufarray = verlauf.toArray(new String[verlauf.size()]);
                verlaufIntent.putExtra("verlauf", verlaufarray);
                String[] arrayVerlauf = verlaufIntent.getStringArrayExtra("verlauf");
                //Toast.makeText(CalcActivity_normal.this,Arrays.toString(arrayVerlauf),Toast.LENGTH_LONG).show();

                startActivityForResult(verlaufIntent, REQUEST_CODE_Verlauf);

            }
        });

        VIEW_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                //TODO: TAN( etc. vollständig löschen
                eingabeClear();
                ausgabe_setText("");

                if(!CalcActivity_science.noImmidiateOps.contains(I.getDisplayableString().trim())){
                    if(solve_inst_pref){
                        answer = I.getResult(CalcActivity_science.getBase(CalcActivity_normal.this));
                        if(!answer.equals("Math Error"))ausgabe_setText(answer);
                    }
                }
            }
        });
        VIEW_clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                eT_eingabe.setText("");
                eT_ausgabe.setText("");
                I.setText(eT_eingabe.getText().toString());
            }
        });

        //L2
        //normal: PI,E,CONST,CONV
        VIEW_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.startAnimation(buttonClick);

                if (mode.equals("NUMBER") || mode.equals("ZAHLEN")) {
                    if(state_spinner_shift.equals("base_selection") && !VIEW_7_text.equals("7")){
                        CalcActivity_science.setBase(CalcActivity_normal.this,Integer.valueOf(VIEW_7_text));
                        state_spinner_shift = "number_selection";
                        setUp_spinner_base();
                    } else {
                        eingabeAddText(VIEW_7_text);
                    }
                } else if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("π");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    ausgabe_setText(I.getPFZ(CalcActivity_science.getBase(CalcActivity_normal.this)));
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("SIN");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("VIEW_11");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("Zn()");
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("SINH");
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    MEMORY[0] = getSelection();
                    CalcActivity_science.saveMemory(CalcActivity_normal.this,MEMORY);
                }else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    eingabeAddText("AND(,)");
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(VIEW_11.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_normal.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }

            }
        });
        VIEW_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals("NUMBER") || mode.equals("ZAHLEN")) {
                    if(state_spinner_shift.equals("base_selection") && !VIEW_8_text.equals("8")){
                        CalcActivity_science.setBase(CalcActivity_normal.this,Integer.valueOf(VIEW_8_text));
                        state_spinner_shift = "number_selection";
                        setUp_spinner_base();
                    } else {
                        eingabeAddText(VIEW_8_text);
                    }
                } else if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("e");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText("GGT(,)");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("COS");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("VIEW_12");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("Zb(,)");
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("COSH");
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    MEMORY[1] = getSelection();
                    CalcActivity_science.saveMemory(CalcActivity_normal.this,MEMORY);
                }else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    eingabeAddText("OR(,)");
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(VIEW_12.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_normal.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }

            }
        });
        VIEW_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals("NUMBER") || mode.equals("ZAHLEN")) {
                    if(state_spinner_shift.equals("base_selection") && !VIEW_9_text.equals("9")){
                        CalcActivity_science.setBase(CalcActivity_normal.this,Integer.valueOf(VIEW_9_text));
                        state_spinner_shift = "number_selection";
                        setUp_spinner_base();
                    } else {
                        eingabeAddText(VIEW_9_text);
                    }
                } else if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("^");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText("KGV(,)");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("TAN");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("VIEW_13");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("C ");
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("TANH");
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    MEMORY[2] = getSelection();
                    CalcActivity_science.saveMemory(CalcActivity_normal.this,MEMORY);
                }else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    eingabeAddText("XOR(,)");
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(VIEW_13.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_normal.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }

            }
        });
        VIEW_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals("NUMBER") || mode.equals("ZAHLEN")) {
                    if(state_spinner_shift.equals("base_selection") && !VIEW_4_text.equals("4")){
                        CalcActivity_science.setBase(CalcActivity_normal.this, Integer.valueOf(VIEW_4_text));
                        state_spinner_shift = "number_selection";
                        setUp_spinner_base();
                    } else {
                        eingabeAddText(VIEW_4_text);
                    }
                } else if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("LOG");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText(getResources().getString(R.string.SUME) + "(,)");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("COT");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("VIEW_14");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("P ");
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("ASINH");
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    MEMORY[3] = getSelection();
                    CalcActivity_science.saveMemory(CalcActivity_normal.this,MEMORY);
                }else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    eingabeAddText("NOT()");
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(VIEW_14.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_normal.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }

            }
        });
        VIEW_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals("NUMBER") || mode.equals("ZAHLEN")) {
                    if(state_spinner_shift.equals("base_selection") && !VIEW_5_text.equals("5")){
                        CalcActivity_science.setBase(CalcActivity_normal.this, Integer.valueOf(VIEW_5_text));
                        state_spinner_shift = "number_selection";
                        setUp_spinner_base();
                    } else {
                        eingabeAddText(VIEW_5_text);
                    }
                } else if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("LN");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText(getResources().getString(R.string.MULP) + "(,)");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("ASIN");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("VIEW_15");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("ACOSH(");
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    MEMORY[4] = getSelection();
                    CalcActivity_science.saveMemory(CalcActivity_normal.this,MEMORY);
                }else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    ausgabe_setText(I.getBIN(CalcActivity_science.getBase(CalcActivity_normal.this)));
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(VIEW_15.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_normal.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }

            }
        });
        VIEW_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals("NUMBER") || mode.equals("ZAHLEN")) {
                    if(state_spinner_shift.equals("base_selection") && !VIEW_6_text.equals("6")){
                        CalcActivity_science.setBase(CalcActivity_normal.this, Integer.valueOf(VIEW_6_text));
                        state_spinner_shift = "number_selection";
                        setUp_spinner_base();
                    } else {
                        eingabeAddText(VIEW_6_text);
                    }
                } else if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("LB");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {

                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("ACOS");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("VIEW_16");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("ATANH");
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    MEMORY[5] = getSelection();
                    CalcActivity_science.saveMemory(CalcActivity_normal.this,MEMORY);
                }else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    ausgabe_setText(I.getOCT(CalcActivity_science.getBase(CalcActivity_normal.this)));
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(VIEW_16.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_normal.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }

            }
        });

        //L3
        //normal: %,!,^,a/b,x^-1,+/-
        //TRIGO: ASINH,ACOSH,ATANH,SINH,COSH,TANH
        VIEW_21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals("NUMBER") || mode.equals("ZAHLEN")) {
                    eingabeAddText("1");
                    if(state_spinner_shift.equals("base_selection") && !VIEW_1_text.equals("1")){
                        CalcActivity_science.setBase(CalcActivity_normal.this,Integer.valueOf(VIEW_1_text));
                        state_spinner_shift = "number_selection";
                        setUp_spinner_base();
                    } else {
                        eingabeAddText(VIEW_1_text);
                    }
                } else if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("³√");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    ausgabe_setText(I.getPercent(CalcActivity_science.getBase(CalcActivity_normal.this)));
                    if (verlauf == null) verlauf = new ArrayList<>();

                    answer = I.getPercent(CalcActivity_science.getBase(CalcActivity_normal.this));
                    ausgabe_setText(answer);
                    verlauf.add(answer);
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("ATAN");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("VIEW_21");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {

                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    ausgabe_setText(I.getDEC(CalcActivity_science.getBase(CalcActivity_normal.this)));
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    replaceSelection(MEMORY[0]);
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(VIEW_21.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_normal.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }

            }
        });
        VIEW_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals("NUMBER") || mode.equals("ZAHLEN")) {
                    if(state_spinner_shift.equals("base_selection") && !VIEW_2_text.equals("2")){
                        CalcActivity_science.setBase(CalcActivity_normal.this,Integer.valueOf(VIEW_2_text));
                        state_spinner_shift = "number_selection";
                        setUp_spinner_base();
                    } else {
                        eingabeAddText(VIEW_2_text);
                    }
                } else if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("√");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    ausgabe_setText(I.getBruch(CalcActivity_science.getBase(CalcActivity_normal.this)));
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("ACOT");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("VIEW_22");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {

                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    replaceSelection(MEMORY[1]);
                }else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    ausgabe_setText(I.getHEX(CalcActivity_science.getBase(CalcActivity_normal.this)));
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(VIEW_22.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_normal.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }

            }
        });
        VIEW_23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals("NUMBER") || mode.equals("ZAHLEN")) {
                    if(state_spinner_shift.equals("base_selection") && !VIEW_3_text.equals("3")){
                        CalcActivity_science.setBase(CalcActivity_normal.this, Integer.valueOf(VIEW_3_text));
                        state_spinner_shift = "number_selection";
                        setUp_spinner_base();
                    } else {
                        eingabeAddText(VIEW_3_text);
                    }
                } else  if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("³");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    ausgabe_setText(I.getReciproke(CalcActivity_science.getBase(CalcActivity_normal.this)));
                }  else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    ausgabe_setText(I.getDEG(CalcActivity_science.getBase(CalcActivity_normal.this)));
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("VIEW_23");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {

                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    replaceSelection(MEMORY[2]);
                }else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(VIEW_23.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_normal.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }

            }
        });
        VIEW_24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals("NUMBER") || mode.equals("ZAHLEN")) {
                    eingabeAddText("0");
                } else if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("²");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    ausgabe_setText(I.getInvert(CalcActivity_science.getBase(CalcActivity_normal.this)));
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    ausgabe_setText(I.getRAD(CalcActivity_science.getBase(CalcActivity_normal.this)));
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("VIEW_24");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {

                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    replaceSelection(MEMORY[3]);
                }else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(VIEW_24.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_normal.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }

            }
        });
        VIEW_25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals("NUMBER") || mode.equals("ZAHLEN")) {
                    eingabeAddText(".");
                } else if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("10^");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText("MIN()");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("toPolar(,)");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("VIEW_25");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {

                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    replaceSelection(MEMORY[4]);
                }else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(VIEW_25.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_normal.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }

            }
        });
        VIEW_26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals("NUMBER") || mode.equals("ZAHLEN")) {
                    eingabeAddText(",");
                } if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("!");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText("MAX()");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("toCart(,)");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("VIEW_26");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {

                } else if (mode.equals("HYPER")) {

                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    replaceSelection(MEMORY[5]);
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(VIEW_26.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_normal.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }

            }
        });

        if(VIEW_LINKS!=null){
            VIEW_LINKS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(buttonClick);
                    int pos = eT_eingabe.getSelectionStart();
                    eT_eingabe.setSelection(Math.max(0, pos - 1));

                }
            });
        }

        if(VIEW_RECHTS!=null) {
            VIEW_RECHTS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(buttonClick);
                    int pos = eT_eingabe.getSelectionStart();
                    eT_eingabe.setSelection(Math.min(eT_eingabe.length(), pos + 1));

                }
            });
        }



        //G2
        VIEW_open_bracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("(");

            }
        });
        VIEW_close_bracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText(")");

            }
        });
        VIEW_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("+");

                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_normal.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }

            }
        });
        VIEW_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("-");

                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_normal.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }

            }
        });
        VIEW_mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("*");

                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_normal.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }

            }
        });
        VIEW_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("/");

                if(solve_inst_pref){
                    answer = I.getResult(CalcActivity_science.getBase(CalcActivity_normal.this));
                    if(!answer.equals("Math Error"))ausgabe_setText(answer);
                }

            }
        });
        if(VIEW_ans != null){
            VIEW_ans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.startAnimation(buttonClick);

                    eingabeAddText("ANS");

                }
            });
        }
        VIEW_eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                if(!eT_eingabe.getText().toString().equals(I.getDisplayableString())){
                    I.setText(eT_eingabe.getText().toString());
                }
                if (verlauf == null) verlauf = new ArrayList<>();
                verlauf.add(I.getDisplayableString());

                answer = I.getResult(CalcActivity_science.getBase(CalcActivity_normal.this));
                ausgabe_setText(answer);
                HistoryActivity.addHistory(CalcActivity_normal.this,eT_eingabe.getText().toString(),answer);
            }
        });



        Intent v = getIntent();
        eT_eingabe.setText( v.getStringExtra("input"));
        eT_ausgabe.setText( v.getStringExtra("output"));
        verlauf = ArrayUtils.stringToList(v.getStringExtra("verlauf"));

        try {
            SettingsApplier.setBackgroundImage(CalcActivity_normal.this,normal_background);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //End onCreate
        SettingsApplier.setETTextSize(CalcActivity_normal.this,eT_eingabe,3);
        SettingsApplier.setETTextSize(CalcActivity_normal.this,eT_ausgabe,3);
    }


    private void ausgabe_setText(String res) {
        //Toast.makeText(CalcActivity_normal.this,"Ausgabe: "+res,Toast.LENGTH_SHORT).show();
        eT_ausgabe.setText(res);
    }

    public void setUpButtons(String group){
        //L1
        setUpButton(VIEW_11,group+"_btn11");
        setUpButton(VIEW_12,group+"_btn12");
        setUpButton(VIEW_13,group+"_btn13");
        setUpButton(VIEW_14,group+"_btn14");
        setUpButton(VIEW_15,group+"_btn15");
        setUpButton(VIEW_16,group+"_btn16");

        //L2
        setUpButton(VIEW_21,group+"_btn21");
        setUpButton(VIEW_22,group+"_btn22");
        setUpButton(VIEW_23,group+"_btn23");
        setUpButton(VIEW_24,group+"_btn24");
        setUpButton(VIEW_25,group+"_btn25");
        setUpButton(VIEW_26,group+"_btn26");
    }

    public void setUpButton(Button x, String name){
        x.setText(PreferenceManager.getDefaultSharedPreferences(CalcActivity_normal.this).getString(name, name));
    }

    void assignModeFct(){
        if(mode.equals("NUMBER") || mode.equals("ZAHLEN")){
            VIEW_1_text = assignBtnNumberText(1);
            VIEW_2_text = assignBtnNumberText(2);
            VIEW_3_text = assignBtnNumberText(3);
            VIEW_4_text = assignBtnNumberText(4);
            VIEW_5_text = assignBtnNumberText(5);
            VIEW_6_text = assignBtnNumberText(6);
            VIEW_7_text = assignBtnNumberText(7);
            VIEW_8_text = assignBtnNumberText(8);
            VIEW_9_text = assignBtnNumberText(9);

            //L1 normal: SIN,COS,TAN,ASIN,ACOS,ATAN
            VIEW_11.setText(VIEW_7_text);
            VIEW_12.setText(VIEW_8_text);
            VIEW_13.setText(VIEW_9_text);
            VIEW_14.setText(VIEW_4_text);
            VIEW_15.setText(VIEW_5_text);
            VIEW_16.setText(VIEW_6_text);

            //L3 normal: >DEG/>RAD/>Polar/>Cart
            VIEW_21.setText(VIEW_1_text);
            VIEW_22.setText(VIEW_2_text);
            VIEW_23.setText(VIEW_3_text);
            VIEW_24.setText("0");
            VIEW_25.setText(".");
            VIEW_26.setText(",");
        } else if(mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))){
            //L1 normal: SIN,COS,TAN,ASIN,ACOS,ATAN
            VIEW_11.setText("SIN");
            VIEW_12.setText("COS");
            VIEW_13.setText("TAN");
            VIEW_14.setText("COT");
            VIEW_15.setText("ASIN");
            VIEW_16.setText("ACOS");

            //L3 normal: >DEG/>RAD/>Polar/>Cart
            VIEW_21.setText("ATAN");
            VIEW_22.setText("ACOT");
            VIEW_23.setText(">DEG");
            VIEW_24.setText(">RAD");
            VIEW_25.setText(">Polar");
            VIEW_26.setText(">Cart");
        }if(mode.equals("HYPER")){
            //L1 normal: SIN,COS,TAN,ASIN,ACOS,ATAN
            VIEW_11.setText("SINH");
            VIEW_12.setText("COSH");
            VIEW_13.setText("TANH");
            VIEW_14.setText("ASINH");
            VIEW_15.setText("ASINH");
            VIEW_16.setText("ASINH");

            //L3 normal: ASINH,ACOSH,ATANH,SINH,COSH,TANH
            VIEW_21.setText("");
            VIEW_22.setText("");
            VIEW_23.setText("");
            VIEW_24.setText("");
            VIEW_25.setText("");
            VIEW_26.setText("");
        }
        else if(mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))){
            //L1 normal: SIN,COS,TAN,ASIN,ACOS,ATAN
            VIEW_11.setText("ZN(N)");
            VIEW_12.setText("ZB(X;Y)");
            VIEW_13.setText("NCR");
            VIEW_14.setText("NPR");
            VIEW_15.setText("");
            VIEW_16.setText("");

            //L3 normal: ASINH,ACOSH,ATANH,SINH,COSH,TANH
            VIEW_21.setText("");
            VIEW_22.setText("");
            VIEW_23.setText("");
            VIEW_24.setText("");
            VIEW_25.setText("");
            VIEW_26.setText("");
        } else if(mode.equals("LOGIC") || mode.equals("LOGISCH")){
            //L1
            VIEW_11.setText("AND");
            VIEW_12.setText("OR");
            VIEW_13.setText("XOR");
            VIEW_14.setText("NOT");
            VIEW_15.setText(">BIN");
            VIEW_16.setText(">OCT");

            //L2
            VIEW_21.setText(">DEC");
            VIEW_22.setText(">HEX");
            VIEW_23.setText("");
            VIEW_24.setText("");
            VIEW_25.setText("");
            VIEW_26.setText("");
        }
        else if(mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))){
            //L1 normal: PI,E,->DEC,->BIN,->OCT
            VIEW_11.setText(R.string.PI);
            VIEW_12.setText(R.string.E);
            VIEW_13.setText("^");
            VIEW_14.setText("LOG");
            VIEW_15.setText("LN");
            VIEW_16.setText("LB");

            //L3 normal: %,!,^,a/b,x^-1,+/-
            VIEW_21.setText("³√");
            VIEW_22.setText("√");
            VIEW_23.setText("x³");
            VIEW_24.setText("x²");

            VIEW_25.setText("10^x");
            VIEW_26.setText("!");
        }else if(mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))){
            //L1 normal: PI,E,->DEC,->BIN,->OCT
            VIEW_11.setText("PFZ");
            VIEW_12.setText("GGT");
            VIEW_13.setText("KGV");
            VIEW_14.setText(getResources().getString(R.string.SUME));
            VIEW_15.setText(getResources().getString(R.string.MULP));
            VIEW_16.setText("");

            //L3 normal: %,!,^,a/b,x^-1,+/-
            VIEW_21.setText(">%");
            VIEW_22.setText("A/B");
            VIEW_23.setText(R.string.x_h_one);
            VIEW_24.setText("+/-");
            VIEW_25.setText("MIN");
            VIEW_26.setText("MAX");
        } else {
            if(UserFctGroups.contains(mode)){
                //NutzerFct
                setUpButtons(mode);
            }

        }
    }


    public static void setDefaultColors(){
        SettingsApplier.setDefaultColors();
    }

    void setBackgrounds(){
        normal_background.setBackgroundColor(SettingsApplier.getColor_background(CalcActivity_normal.this));
        //SettingsApplier.drawVectorImage(CalcActivity_normal.this,VIEW_clear,R.drawable.ic_clear,SettingsApplier.getColor_fkt(CalcActivity_normal.this));
        //SettingsApplier.drawVectorImage(CalcActivity_normal.this,VIEW_clearall,R.drawable.ic_clear_all,SettingsApplier.getColor_fkt(CalcActivity_normal.this));

        SettingsApplier.drawVectorImage(CalcActivity_normal.this,VIEW_menu,R.drawable.ic_menu_black_24dp,SettingsApplier.getColor_act(CalcActivity_normal.this));
        SettingsApplier.drawVectorImage(CalcActivity_normal.this,VIEW_CONST,R.drawable.ic_konstanten1,SettingsApplier.getColor_act(CalcActivity_normal.this));
        SettingsApplier.drawVectorImage(CalcActivity_normal.this,VIEW_CONV,R.drawable.ic_lineal,SettingsApplier.getColor_act(CalcActivity_normal.this));
        SettingsApplier.drawVectorImage(CalcActivity_normal.this,VIEW_verlauf,R.drawable.ic_verlauf,SettingsApplier.getColor_act(CalcActivity_normal.this));

        int height = VIEW_eq.getHeight();
        for(Button b: VIEW_ALL){
            //TODO: quickfix für: science -> land
                if(b == null)continue;
            b.setHeight(height);
            if(VIEW_ACT.contains(b))SettingsApplier.setViewDesign(CalcActivity_normal.this,b,SettingsApplier.getColor_act(CalcActivity_normal.this));
            if(VIEW_FKT.contains(b))SettingsApplier.setViewDesign(CalcActivity_normal.this,b,SettingsApplier.getColor_fkt(CalcActivity_normal.this));
            if(VIEW_FOPS.contains(b))SettingsApplier.setViewDesign(CalcActivity_normal.this,b,SettingsApplier.getColor_fops(CalcActivity_normal.this));
            if(VIEW_SAVES.contains(b))SettingsApplier.setViewDesign(CalcActivity_normal.this,b,SettingsApplier.getColor_saves(CalcActivity_normal.this));
            if(VIEW_SPECIALS.contains(b))SettingsApplier.setViewDesign(CalcActivity_normal.this,b,SettingsApplier.getColor_specials(CalcActivity_normal.this));
        }
        SettingsApplier.setViewDesign(CalcActivity_normal.this,spinner_base,SettingsApplier.getColor_fops(CalcActivity_normal.this));
        SettingsApplier.setViewDesign(CalcActivity_normal.this,spinner_shift,SettingsApplier.getColor_fops(CalcActivity_normal.this));
        SettingsApplier.setViewDesign(CalcActivity_normal.this,display,SettingsApplier.getColor_numbers(CalcActivity_normal.this));

        //VIEW_clear.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
        if(this.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE){
            SettingsApplier.setETC_ADesign(CalcActivity_normal.this,VIEW_clear,SettingsApplier.getColor_fkt(CalcActivity_normal.this));
            SettingsApplier.setETC_ADesign(CalcActivity_normal.this,VIEW_clearall,SettingsApplier.getColor_fkt(CalcActivity_normal.this));
        } else {
            SettingsApplier.setViewDesign(CalcActivity_normal.this,VIEW_clear,SettingsApplier.getColor_fkt(CalcActivity_normal.this));
            SettingsApplier.setViewDesign(CalcActivity_normal.this,VIEW_clearall,SettingsApplier.getColor_fkt(CalcActivity_normal.this));
        }

        SettingsApplier.setViewDesign(CalcActivity_normal.this,display,SettingsApplier.getColor_display(CalcActivity_normal.this));


        //fires if layout drawn
        VIEW_close_bracket.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                SettingsApplier.centerTextButton(VIEW_open_bracket,7);
                SettingsApplier.centerTextButton(VIEW_close_bracket,7);
                SettingsApplier.centerTextButton(VIEW_mul,7);
            }
        });

        SettingsApplier.setETDesign(CalcActivity_normal.this,eT_eingabe,4);
        SettingsApplier.setETDesign(CalcActivity_normal.this,eT_ausgabe,4);
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
                    //Toast.makeText(CalcActivity_normal.this, "SET: "+current_Callback, Toast.LENGTH_LONG).show();

                    //ausgabe_setText("");
                }
            }
        }
    }

    void transBtnFct(String fct){
        if(fct.startsWith("btn"))return;

        //"PI","E","NCR","NPR","%","!N","^","A/B","x\u207B\u00B9","+/-","√","\u00B3√","LOG","LN","LB","SIN","COS","TAN","ASIN","ATAN","ASINH","ACOSH","ATANH","SINH","COSH","TANH"};
        if(fct.equals(">%")){
            ausgabe_setText(I.getPercent(CalcActivity_science.getBase(CalcActivity_normal.this)));
            return;
        }
        else if(fct.equals("A/B")){
            ausgabe_setText(I.getBruch(CalcActivity_science.getBase(CalcActivity_normal.this)));
            return;
        }
        else if(fct.equals("x\u207B\u00B9")){
            ausgabe_setText(I.getReciproke(CalcActivity_science.getBase(CalcActivity_normal.this)));
            return;
        }
        else if(fct.equals("+/-")){
            ausgabe_setText(I.getInvert(CalcActivity_science.getBase(CalcActivity_normal.this)));
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
        if(solve_inst_pref){
            if(!CalcActivity_science.noImmidiateOps.contains(i.trim())){
                answer = I.getResult(CalcActivity_science.getBase(CalcActivity_normal.this));
                if(!answer.equals("Math Error"))ausgabe_setText(answer);
            }
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

    public String getSelection(){
        String selection = "";
        int selStart = -1;
        int selEnd = -1;
        if(eT_eingabe.hasFocus()){
            selStart = eT_eingabe.getSelectionStart();
            selEnd = eT_eingabe.getSelectionEnd();
            if(selStart > 0 && selEnd > 0 && selStart < selEnd){
                return eT_eingabe.getText().toString().substring(selStart,selEnd);
            } else {
                return eT_eingabe.getText().toString();
            }
        } else if(eT_ausgabe.hasFocus()){
            selStart = eT_ausgabe.getSelectionStart();
            selEnd = eT_ausgabe.getSelectionEnd();
            if(selStart > 0 && selEnd > 0 && selStart < selEnd){
                return eT_ausgabe.getText().toString().substring(selStart,selEnd);
            } else {
                return eT_ausgabe.getText().toString();
            }
        }
        return selection;
    }


    private void applySettings(){
        //language
        language = PreferenceManager.getDefaultSharedPreferences(CalcActivity_normal.this).getString("pref_lang","english");
        if(language.equals("english") || language.equals("englisch")){
            //VIEW_CONV.setText(R.string.CONVEN);
            //VIEW_CONST.setText(R.string.CONSTEN);
            VIEW_menu.setText("");
            //VIEW_verlauf.setText(R.string.VERLAUFEN);
            act_options = getResources().getStringArray(R.array.act_EN);
            mode_options = FunctionGroupSettingsActivity.getGroups(CalcActivity_normal.this);
            if(language.equals("german") || language.equals("deutsch")){
                mode_options = StringUtils.concatenate(new String[]{"ZAHLEN"},mode_options);
            }else {
                mode_options = StringUtils.concatenate(new String[]{"NUMBER"},mode_options);
            }
        }
        else if(language.equals("german") || language.equals("deutsch")){
            //VIEW_CONV.setText(R.string.CONVDE);
            //VIEW_CONST.setText(R.string.CONSTDE);
            VIEW_menu.setText("");
            //VIEW_verlauf.setText(R.string.VERLAUFDE);
            act_options = getResources().getStringArray(R.array.act_DE);
            mode_options = FunctionGroupSettingsActivity.translateGroup(FunctionGroupSettingsActivity.getGroups(CalcActivity_normal.this),"german");
            if(language.equals("german") || language.equals("deutsch")){
                mode_options = StringUtils.concatenate(new String[]{"ZAHLEN"},mode_options);
            }else {
                mode_options = StringUtils.concatenate(new String[]{"NUMBER"},mode_options);
            }
        }

        //UserFctGroups.addAll(mode_options); UserFctGroups
        //Toast.makeText(CalcActivity_normal.this,"Modes: "+Arrays.toString(mode_options),Toast.LENGTH_SHORT).show();
        UserFctGroups = new HashSet<>(Arrays.asList(FunctionGroupSettingsActivity.getUserGroups(CalcActivity_normal.this)));

        //numbers
        //numbers
        if (PreferenceManager.getDefaultSharedPreferences(CalcActivity_normal.this).contains("pre_decimal_places_pref")) {
            String prec = PreferenceManager.getDefaultSharedPreferences(CalcActivity_normal.this).getString("pre_decimal_places_pref","10");
            if(prec != null)NumberString.predec_places =  Integer.valueOf(prec) + 1;
        }
        if (PreferenceManager.getDefaultSharedPreferences(CalcActivity_normal.this).contains("decimal_places_pref")) {
            String prec = PreferenceManager.getDefaultSharedPreferences(CalcActivity_normal.this).getString("decimal_places_pref","10");
            if(prec != null)NumberString.dec_places =  Integer.valueOf(prec) + 1;
        }

        //Fonts
        //Typeface font = Typeface.createFromAsset(getAssets(), "Crashed Scoreboard.ttf");
        Typeface monospace = Typeface.create("MONOSPACE",Typeface.NORMAL);
        Typeface sansSerif = Typeface.create("SANS_SERIF",Typeface.NORMAL);
        Typeface serif = Typeface.create("SERIF",Typeface.NORMAL);

        //Math Settings
        solve_inst_pref = PreferenceManager.getDefaultSharedPreferences(CalcActivity_normal.this).getBoolean("solve_inst_pref",false);
        MathEvaluator.applySettings(CalcActivity_normal.this);

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

    String assignBtnNumberText(int i){
        VIEW_digit_group_cnt = 0; //TODO: für basesel entfernen
        if(state_spinner_shift.equals("base_selection")){
            return String.valueOf(VIEW_digit_group_cnt*9 + i);
        }
        else if(state_spinner_shift.equals("number_selection")){
            return String.valueOf(MathEvaluator.digit_alphabet[VIEW_digit_group_cnt*9 + i]);
        }
        return "";
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

    public void replaceSelection(String input){
        if(input==null || input.isEmpty())return;
        int selStart = -1;
        int selEnd = -1;
        if(eT_eingabe.hasFocus()){
            selStart = eT_eingabe.getSelectionStart();
            selEnd = eT_eingabe.getSelectionEnd();
            if(selStart >= 0 && selEnd >= 0 && selStart <= selEnd && selStart <= eT_eingabe.length() && selEnd <= eT_eingabe.length()){
                String etE_text = eT_eingabe.getText().toString();
                etE_text = StringUtils.replace(etE_text,input,selStart,selEnd);
                Toast.makeText(CalcActivity_normal.this,"selection: "+etE_text.toString(),Toast.LENGTH_LONG).show();
                eT_eingabe.setText(etE_text);
            } else {
                eT_eingabe.setText(input);
            }
            eT_eingabe.setSelection(selEnd);
            I.setText(eT_eingabe.getText().toString());
        }
    }

    public void setUp_spinner_base(){
        if(spinner_base == null)spinner_base = findViewById(R.id.m_spinner_base);
        if(state_spinner_shift.equals("number_selection")){
            //save base
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CalcActivity_normal.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("base", String.valueOf(CalcActivity_science.getBase(CalcActivity_normal.this)));
            editor.commit();

            //set up spinner
            int d = (int) Math.ceil(((double) (CalcActivity_science.getBase(CalcActivity_normal.this)) / 10) - 1.0);
            int beg = 0, end = Math.max(d,0);
            String[] subarray = new String[end - beg + 2];
            System.arraycopy(MathEvaluator.digit_alphabet_groups, beg, subarray, 0, subarray.length);
            subarray[subarray.length-1] = "set base";

            SettingsApplier.setArrayAdapter(CalcActivity_normal.this,spinner_base,subarray,SettingsApplier.getColor_fops(CalcActivity_normal.this));

            spinner_base.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(adapterView.getItemAtPosition(i).equals("set base")){
                        state_spinner_shift = "base_selection";
                        setUp_spinner_base();
                    } else {
                        int new_VIEW_digit_group_cnt = ((i  % (MathEvaluator.digit_alphabet.length / 9)));
                        if(new_VIEW_digit_group_cnt*9 < CalcActivity_science.getBase(CalcActivity_normal.this)){
                            VIEW_digit_group_cnt = new_VIEW_digit_group_cnt;
                        } else {
                            VIEW_digit_group_cnt = 0;
                        }
                        assignModeFct();
                    }
                }

                @Override public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        } else if(state_spinner_shift.equals("base_selection")){
            SettingsApplier.setArrayAdapter(CalcActivity_normal.this,spinner_base,MathEvaluator.int_digit_alphabet_groups,SettingsApplier.getColor_fops(CalcActivity_normal.this));

            spinner_base.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int new_VIEW_digit_group_cnt = ((i  % (MathEvaluator.digit_alphabet.length / 9)));
                    VIEW_digit_group_cnt = new_VIEW_digit_group_cnt;

                    assignModeFct();
                    Toast.makeText(CalcActivity_normal.this, "bs new base sel: "+VIEW_digit_group_cnt, Toast.LENGTH_SHORT).show();
                }

                @Override public void onNothingSelected(AdapterView<?> adapterView) {}
            });
        }

    }
}

