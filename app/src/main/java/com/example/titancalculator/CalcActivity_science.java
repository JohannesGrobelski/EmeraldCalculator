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
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.helper.MainDisplay.OnSwipeTouchListener;
import com.example.titancalculator.helper.MainDisplay.SettingsApplier;
import com.example.titancalculator.helper.Math_String.NavigatableString;
import com.example.titancalculator.helper.Math_String.NumberString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class CalcActivity_science extends AppCompatActivity {
    TableLayout science_background;

    static final int REQUEST_CODE_CONST = 1;  // The request code
    static final int REQUEST_CODE_CONV = 1;  // The request code
    static final int REQUEST_CODE_Verlauf = 1;  // The request code

    LinkedList<String> verlauf = new LinkedList<>();
    int buttonshapeID = R.drawable.buttonshape_square;
    String buttonfüllung="voll";

    Set<String> UserFctGroups = new HashSet<>();

    String current_Callback="";
    String answer="";

    String language = "";
    String[] act_options;
    String[] mode_options;


    TableRow display;
    //L1
    Button btn_CONV;
    Button btn_CONST;
    Button btn_verlauf;
    Button btn_menu;
    Spinner spinner_shift;

    Button btn_clear;
    Button btn_clearall;

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


    //L5
    //Button btn_X;
    //Button btn_Y;
    //Button btn_FKT1;
    //Button btn_FKT2;
    Button btn_LINKS;
    Button btn_RECHTS;

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
    Button btn_ans;


    //G2
    Button btn_open_bracket;
    Button btn_close_bracket;
    Button btn_add;
    Button btn_sub;
    Button btn_mul;
    Button btn_div;
    Button btn_eq;



    EditText tV_eingabe;
    boolean tV_eingabe_hasFocus=true;
    EditText tV_ausgabe;

    NavigatableString I;


    String X = "";
    String Y = "";

    TableRow LN2;
    TableRow LN3;
    TableRow LN4;


    private Set<Button> VIEW_ACT;
    private Set<Button> VIEW_FKT;
    private Set<Button> VIEW_FOPS;
    private Set<Button> VIEW_NUMBERS;
    private Set<Button> VIEW_SAVES;
    private Set<Button> VIEW_SPECIALS;
    private ArrayList<Button> VIEW_ALL;

    String mode = "BASIC";

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
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Intent conversionIntent=null;
            conversionIntent = new Intent(CalcActivity_science.this, CalcActivity_normal.class);

            conversionIntent.putExtra("onPotraitReturn",true);
            conversionIntent.putExtra("verlauf",verlaufToString(verlauf));
            conversionIntent.putExtra("input",tV_eingabe.getText().toString());
            conversionIntent.putExtra("output",tV_ausgabe.getText().toString());
            conversionIntent.putExtra("swipeDir","");
            conversionIntent.putExtra("layout","normal");
            startActivity(conversionIntent);
            finish();
            return;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.einstellungen:
                Intent settingsIntent = new Intent(this,
                        SettingsActivity.class);
                startActivity(settingsIntent);
            case R.id.taschenrechner:
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String mode = PreferenceManager.getDefaultSharedPreferences(this).getString("layout","");
        if(!mode.isEmpty() && MainActivity.modes.contains(mode) && !mode.equals("science")){
            Intent conversionIntent=null;
            if(mode.equals("normal")){
                conversionIntent = new Intent(CalcActivity_science.this, CalcActivity_normal.class);
            }
            else if(mode.equals("small")){
                conversionIntent = new Intent(CalcActivity_science.this, CalcActivity_small.class);
            }
            else Log.e("mode  ",mode);
            conversionIntent.putExtra("verlauf",verlaufToString(verlauf));
            conversionIntent.putExtra("input",tV_eingabe.getText().toString());
            conversionIntent.putExtra("output",tV_ausgabe.getText().toString());
            conversionIntent.putExtra("swipeDir","");
            conversionIntent.putExtra("layout",mode);
            startActivity(conversionIntent);
            finish();
        }


        setTitle("Rechner");
        SettingsApplier.setColors(CalcActivity_science.this);
        applySettings();
        setBackgrounds();
        ArrayList<View> list = new ArrayList<View>() {{addAll(VIEW_ALL);}};
        SettingsApplier.setFonts(CalcActivity_science.this,list);


        tV_ausgabe.setOnFocusChangeListener(focusListener);
        tV_eingabe.setOnFocusChangeListener(focusListener);

        spinner_shift.setSelection(0);
        mode = "BASIC";


        //ArrayAdapter adpt_modeoptions = new ArrayAdapter<String>(this, R.layout.lvitem_layout, mode_options);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(CalcActivity_science.this, R.layout.spinner_shift_style, mode_options)
                {

                    float factor_font = SettingsApplier.getDarker_factor_font();
                    int darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.getColor_fops(CalcActivity_science.this),factor_font);

                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);

                        ((TextView) v).setTextSize(16);
                        ((TextView) v).setTypeface(FontSettingsActivity.getTypeFace(SettingsApplier.current_font_family,SettingsApplier.current_fontstlye));
                        ((TextView) v).setTextColor(darker);
                        SettingsApplier.setTextColor(v,darker);

                        return v;
                    }

                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        View v = super.getDropDownView(position, convertView, parent);
                        v.setBackgroundResource(R.drawable.buttonshape_square);

                        SettingsApplier.setTextColor(v,darker);

                        ((TextView) v).setTypeface(FontSettingsActivity.getTypeFace(SettingsApplier.current_font_family,SettingsApplier.current_fontstlye));
                        ((TextView) v).setGravity(Gravity.CENTER);

                        return v;
                    }
        };

        spinner_shift.setAdapter(adapter);

        try {
            setBackgroundImage();
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

        setContentView(R.layout.activity_calc_science);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // code for portrait mode
            //Toast.makeText(this,"landscape mode",Toast.LENGTH_LONG).show();
            Intent conversionIntent = new Intent(CalcActivity_science.this, CalcActivity_normal.class);
            conversionIntent.putExtra("onPotraitReturn",true);
            startActivity(conversionIntent);
            finish();
            return;
        }

        setTitle("Rechner");

        science_background = findViewById(R.id.science_background);

        tV_eingabe = findViewById(R.id.tV_Eingabe);
        tV_ausgabe = findViewById(R.id.tV_Ausgabe);

        tV_ausgabe.setOnFocusChangeListener(focusListener);
        tV_eingabe.setOnFocusChangeListener(focusListener);
        mode = "BASIC";


        tV_eingabe.setShowSoftInputOnFocus(false);
        tV_eingabe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(CalcActivity_science.this);
                //v.performClick();
                return false;
            }
        });

        tV_ausgabe.setShowSoftInputOnFocus(false);
        tV_ausgabe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(CalcActivity_science.this);
                //v.performClick();
                return false;
            }
        });

        science_background.setOnTouchListener(new OnSwipeTouchListener(CalcActivity_science.this) {
            public void onSwipeTop() {
                Toast.makeText(CalcActivity_science.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(CalcActivity_science.this, "right", Toast.LENGTH_SHORT).show();
                Intent conversionIntent = new Intent(CalcActivity_science.this, MainActivity.class);
                conversionIntent.putExtra("verlauf",verlaufToString(verlauf));
                conversionIntent.putExtra("input",tV_eingabe.getText().toString());
                conversionIntent.putExtra("output",tV_ausgabe.getText().toString());
                conversionIntent.putExtra("swipeDir","right");
                conversionIntent.putExtra("layout","science");
                startActivity(conversionIntent);
                finish();
            }
            public void onSwipeLeft() {
                Toast.makeText(CalcActivity_science.this, "left", Toast.LENGTH_SHORT).show();
                Intent conversionIntent = new Intent(CalcActivity_science.this, MainActivity.class);
                conversionIntent.putExtra("verlauf",verlaufToString(verlauf));
                conversionIntent.putExtra("input",tV_eingabe.getText().toString());
                conversionIntent.putExtra("output",tV_ausgabe.getText().toString());
                conversionIntent.putExtra("swipeDir","left");
                conversionIntent.putExtra("layout","science");
                startActivity(conversionIntent);
                finish();
            }
            public void onSwipeBottom() {
                Toast.makeText(CalcActivity_science.this, "bottom", Toast.LENGTH_SHORT).show();
            }
        });

        //setzt hintergrundbild


        I = new NavigatableString("content");

        eingabeSetText("");


        //find
        display = findViewById(R.id.display);
        science_background = findViewById(R.id.science_background);
        //L1
        spinner_shift = findViewById(R.id.spinner_SHIFT);

        btn_CONV = findViewById(R.id.btn_CONV);
        btn_verlauf = findViewById(R.id.btn_Verlauf);
        btn_CONST = findViewById(R.id.btn_CONST);
        btn_menu = findViewById(R.id.btn_menu);
        btn_clear = findViewById(R.id.btn_clear);
        btn_clearall = findViewById(R.id.btn_clearall);

        //L2
        //normal: PI,E,->DEC,->BIN,->OCT
        //TRIGO: SIN,COS,TAN,ASIN,ACOS,ATAN

        btn_11 = findViewById(R.id.btn_11);
        btn_12 = findViewById(R.id.btn_12);
        btn_13 = findViewById(R.id.btn_13);
        btn_14 = findViewById(R.id.btn_14);
        btn_15 = findViewById(R.id.btn_15);
        btn_16 = findViewById(R.id.btn_16);

        //L3
        //normal: %,!,^,a/b,x^-1,+/-
        //TRIGO: ASINH,ACOSH,ATANH,SINH,COSH,TANH
        btn_21 = findViewById(R.id.btn_21);
        btn_22 = findViewById(R.id.btn_22);
        btn_23 = findViewById(R.id.btn_23);
        btn_24 = findViewById(R.id.btn_24);
        btn_25 = findViewById(R.id.btn_25);
        btn_26 = findViewById(R.id.btn_26);


        //L5
        //btn_X = findViewById(R.id.btn_X);
        //btn_Y = findViewById(R.id.btn_Y);
        //btn_FKT1 = findViewById(R.id.btn_FKT1);
        //btn_FKT2 = findViewById(R.id.btn_FKT2);
        btn_LINKS = findViewById(R.id.btn_LINKS);
        btn_RECHTS = findViewById(R.id.btn_RECHTS);


        //G1
        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);
        btn_7 = findViewById(R.id.btn_7);
        btn_8 = findViewById(R.id.btn_8);
        btn_9 = findViewById(R.id.btn_9);
        btn_0 = findViewById(R.id.btn_0);
        btn_com = findViewById(R.id.btn_com);
        btn_ans = findViewById(R.id.btn_ANS);

        //G2
        btn_open_bracket = findViewById(R.id.btn_open_bracket);
        btn_close_bracket = findViewById(R.id.btn_close_bracket);
        btn_add = findViewById(R.id.btn_add);
        btn_sub = findViewById(R.id.btn_sub);
        btn_mul = findViewById(R.id.btn_mul);
        btn_div = findViewById(R.id.btn_div);
        btn_eq = findViewById(R.id.btn_eq);

        LN2 = findViewById(R.id.LN2);
        LN3 = findViewById(R.id.LN3);
        LN4 = findViewById(R.id.LN4);

        VIEW_ACT = new HashSet<>(Arrays.asList(new Button[]{btn_CONST, btn_CONV, btn_verlauf, btn_menu}));
        VIEW_FKT = new HashSet<>(Arrays.asList(new Button[]{btn_clear, btn_clearall, btn_LINKS, btn_RECHTS}));
        VIEW_FOPS = new HashSet<>(Arrays.asList(new Button[]{btn_11, btn_12, btn_13, btn_14, btn_15, btn_16, btn_21, btn_22, btn_23, btn_24, btn_25, btn_26}));
        VIEW_NUMBERS = new HashSet<>(Arrays.asList(new Button[]{btn_com, btn_ans, btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9}));
        VIEW_SAVES = new HashSet<>(Arrays.asList(new Button[]{}));
        VIEW_SPECIALS = new HashSet<>(Arrays.asList(new Button[]{btn_open_bracket, btn_close_bracket, btn_mul, btn_div, btn_sub, btn_add, btn_eq}));
        VIEW_ALL = new ArrayList<>();
        VIEW_ALL.addAll(VIEW_ACT);
        VIEW_ALL.addAll(VIEW_FKT);
        VIEW_ALL.addAll(VIEW_FOPS);
        VIEW_ALL.addAll(VIEW_NUMBERS);
        VIEW_ALL.addAll(VIEW_SAVES);
        VIEW_ALL.addAll(VIEW_SPECIALS);

        applySettings();
        SettingsApplier.setColors(CalcActivity_science.this);
        setBackgrounds();
        ArrayList<View> list = new ArrayList<View>() {{addAll(VIEW_ALL);}};
        SettingsApplier.setFonts(CalcActivity_science.this,list);

        try {
            setBackgroundImage();
        } catch (Exception e) {
            e.printStackTrace();
        }



        //L1
        ArrayAdapter adpt_modeoptions = new ArrayAdapter<String>(this, R.layout.lvitem_layout, mode_options);
        adpt_modeoptions = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, mode_options) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(SettingsApplier.getColor_fkt(CalcActivity_science.this));
                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(SettingsApplier.getColor_fkt(CalcActivity_science.this));
                return view;
            }
        };

        spinner_shift.setAdapter(adpt_modeoptions);
        spinner_shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(SettingsApplier.getColor_fkt(CalcActivity_science.this));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextColor(SettingsApplier.getColor_fkt(CalcActivity_science.this));
            }
        });

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                current_Callback = "";
                Intent conversionIntent = new Intent(CalcActivity_science.this, SettingsActivity.class);
                startActivity(conversionIntent);
            }
        });

        spinner_shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                view.startAnimation(buttonClick);

                mode = spinner_shift.getSelectedItem().toString();

                assignModeFct();
                setBackground(spinner_shift);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        /*
        btn_MENU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                current_Callback = "";
                Intent conversionIntent = new Intent(CalcActivity_science.this,SettingsActivity.class);
                startActivity(conversionIntent);


                //TODO
                //setBackground(btn_MENU);
            }
        });

         */
        btn_CONV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                current_Callback = "";
                Intent conversionIntent = new Intent(CalcActivity_science.this, ConversionActivity.class);
                startActivityForResult(conversionIntent, REQUEST_CODE_CONV);


                //TODO
                setBackground(btn_CONV);
            }
        });
        btn_CONST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                current_Callback = "";
                Intent constIntent = new Intent(CalcActivity_science.this, ConstantsActivity.class);
                startActivityForResult(constIntent, REQUEST_CODE_CONST);

                setBackground(btn_CONST);
            }
        });
        btn_verlauf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                current_Callback = "";
                Intent verlaufIntent = new Intent(CalcActivity_science.this, HistoryActivity.class);
                String[] verlaufarray = verlauf.toArray(new String[verlauf.size()]);
                verlaufIntent.putExtra("verlauf", verlaufarray);
                String[] arrayVerlauf = verlaufIntent.getStringArrayExtra("verlauf");
                //Toast.makeText(CalcActivity_science.this,Arrays.toString(arrayVerlauf),Toast.LENGTH_LONG).show();

                startActivityForResult(verlaufIntent, REQUEST_CODE_Verlauf);
                setBackground(btn_CONST);
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


        //L2
        //normal: PI,E,CONST,CONV
        btn_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.startAnimation(buttonClick);

                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("PI");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText("R");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("SIN");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_11");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("Zn()");
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("SINH");
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    eingabeAddText("AND(;)");
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_11.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
                setBackground(btn_11);
            }
        });
        btn_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("E");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    ausgabe_setText(I.getPFZ());
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("COS");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_12");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("Zb(;)");
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("COSH");
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    eingabeAddText("OR(;)");
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_12.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
                setBackground(btn_12);
            }
        });
        btn_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("√");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText("ggt(;)");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("TAN");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_13");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("C ");
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("TANH");
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    eingabeAddText("XOR(;)");
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_13.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
                setBackground(btn_13);
            }
        });
        btn_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("LOG ");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText("kgv(;)");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("ASIN");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_14");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("P ");
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("ASINH");
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    eingabeAddText("NOT()");
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_14.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
                setBackground(btn_14);
            }
        });
        btn_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("LN ");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText(getResources().getString(R.string.SUME) + "(;)");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("ACOS");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_15");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("ACOSH(");
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    ausgabe_setText(I.getBIN());
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_15.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
                setBackground(btn_15);
            }
        });
        btn_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("LB ");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText(getResources().getString(R.string.MULP) + "(;)");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("ATAN");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_16");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("ATANH");
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    ausgabe_setText(I.getOCT());
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_16.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
                setBackground(btn_16);
            }
        });


        //L3
        //normal: %,!,^,a/b,x^-1,+/-
        //TRIGO: ASINH,ACOSH,ATANH,SINH,COSH,TANH
        btn_21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    ausgabe_setText(I.getPercent());
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {

                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    ausgabe_setText(I.getDEG());
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_21");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {

                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    ausgabe_setText(I.getDEC());
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_21.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
                setBackground(btn_21);
            }
        });
        btn_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("!");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {

                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    ausgabe_setText(I.getRAD());
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_22");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {

                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    ausgabe_setText(I.getHEX());
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_22.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
                setBackground(btn_22);
            }
        });
        btn_23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("^");

                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {

                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("toPolar(;)");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_23");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {

                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_23.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
                setBackground(btn_23);
            }
        });
        btn_24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    ausgabe_setText(I.getBruch());
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {

                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("toCart(;)");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_24");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {

                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_24.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
                setBackground(btn_24);
            }
        });
        btn_25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    ausgabe_setText(I.getReciproke());
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {

                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {

                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_25");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {

                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_25.getText().toString());
                }else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
                setBackground(btn_25);
            }
        });
        btn_26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    ausgabe_setText(I.getInvert());
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {

                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {

                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_26");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {

                } else if (mode.equals("HYPER")) {

                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_26.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
                setBackground(btn_26);
            }
        });


        btn_LINKS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                int pos = tV_eingabe.getSelectionStart();
                tV_eingabe.setSelection(Math.max(0, pos - 1));

                setBackground(btn_LINKS);
            }
        });

        btn_RECHTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                int pos = tV_eingabe.getSelectionStart();
                tV_eingabe.setSelection(Math.min(tV_eingabe.length(), pos + 1));
                setBackground(btn_RECHTS);

            }
        });


        //G1
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("1");
                setBackground(btn_1);

            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("2");
                setBackground(btn_2);

            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("3");
                setBackground(btn_3);

            }
        });
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("4");
                setBackground(btn_4);

            }
        });
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("5");
                setBackground(btn_5);

            }
        });
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("6");
                setBackground(btn_6);

            }
        });
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("7");
                setBackground(btn_7);

            }
        });
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("8");
                setBackground(btn_8);

            }
        });
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("9");
                setBackground(btn_9);

            }
        });
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("0");
                setBackground(btn_0);

            }
        });
        btn_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText(".");
                setBackground(btn_com);

            }
        });
        btn_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("ANS");
                setBackground(btn_ans);

            }
        });

        //G2
        btn_open_bracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("(");
                setBackground(btn_open_bracket);

            }
        });
        btn_close_bracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText(")");
                setBackground(btn_close_bracket);

            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("+");
                setBackground(btn_add);

            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("-");
                setBackground(btn_sub);

            }
        });
        btn_mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("*");
                setBackground(btn_mul);

            }
        });
        btn_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);

                eingabeAddText("/");
                setBackground(btn_div);

            }
        });
        btn_eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                if (verlauf == null) verlauf = new LinkedList<>();
                verlauf.add(I.getDisplayableString());

                answer = I.getResult();
                ausgabe_setText(answer);
                setBackground(btn_eq);
            }
        });

        setBackground(btn_clear);
        setBackground(btn_clearall);

        Intent v = getIntent();
        tV_eingabe.setText( v.getStringExtra("input"));
        tV_ausgabe.setText( v.getStringExtra("output"));
        stringToVerlauf(v.getStringExtra("verlauf"));
    }



    private void ausgabe_setText(String res) {
        //Toast.makeText(CalcActivity_science.this,"Ausgabe: "+res,Toast.LENGTH_SHORT).show();
        tV_ausgabe.setText(res);
    }


    public void setUpButtons(String group){
        //L1
        setUpButton(btn_11,group+"_btn11");
        setUpButton(btn_12,group+"_btn12");
        setUpButton(btn_13,group+"_btn13");
        setUpButton(btn_14,group+"_btn14");
        setUpButton(btn_15,group+"_btn15");
        setUpButton(btn_16,group+"_btn16");

        //L2
        setUpButton(btn_21,group+"_btn21");
        setUpButton(btn_22,group+"_btn22");
        setUpButton(btn_23,group+"_btn23");
        setUpButton(btn_24,group+"_btn24");
        setUpButton(btn_25,group+"_btn25");
        setUpButton(btn_26,group+"_btn26");
    }

    public void setUpButton(Button x, String name){
        x.setText(PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).getString(name, name));
    }

    void assignModeFct(){
        if(mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))){
            //L1 normal: SIN,COS,TAN,ASIN,ACOS,ATAN
            btn_11.setText("SIN");
            btn_12.setText("COS");
            btn_13.setText("TAN");
            btn_14.setText("ASIN");
            btn_15.setText("ACOS");
            btn_16.setText("ATAN");

            //L3 normal: >DEG/>RAD/>Polar/>Cart
            btn_21.setText(">DEG");
            btn_22.setText(">RAD");
            btn_23.setText(">Polar");
            btn_24.setText(">Cart");
            btn_25.setText("");
            btn_26.setText("");
        }if(mode.equals("HYPER")){
            //L1 normal: SIN,COS,TAN,ASIN,ACOS,ATAN
            btn_11.setText("SINH");
            btn_12.setText("COSH");
            btn_13.setText("TANH");
            btn_14.setText("ASINH");
            btn_15.setText("ASINH");
            btn_16.setText("ASINH");

            //L3 normal: ASINH,ACOSH,ATANH,SINH,COSH,TANH
            btn_21.setText("");
            btn_22.setText("");
            btn_23.setText("");
            btn_24.setText("");
            btn_25.setText("");
            btn_26.setText("");
        }
        else if(mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))){
            //L1 normal: SIN,COS,TAN,ASIN,ACOS,ATAN
            btn_11.setText("ZN(N)");
            btn_12.setText("ZB(X;Y)");
            btn_13.setText("NCR");
            btn_14.setText("NPR");
            btn_15.setText("");
            btn_16.setText("");

            //L3 normal: ASINH,ACOSH,ATANH,SINH,COSH,TANH
            btn_21.setText("");
            btn_22.setText("");
            btn_23.setText("");
            btn_24.setText("");
            btn_25.setText("");
            btn_26.setText("");
        } else if(mode.equals("LOGIC") || mode.equals("LOGISCH")){
            //L1
            btn_11.setText("AND");
            btn_12.setText("OR");
            btn_13.setText("XOR");
            btn_14.setText("NOT");
            btn_15.setText(">BIN");
            btn_16.setText(">OCT");

            //L2
            btn_21.setText(">DEC");
            btn_22.setText(">HEX");
            btn_23.setText("");
            btn_24.setText("");
            btn_25.setText("");
            btn_26.setText("");
        }
        else if(mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))){
            //L1 normal: PI,E,->DEC,->BIN,->OCT
            btn_11.setText(R.string.PI);
            btn_12.setText(R.string.E);
            btn_13.setText("√");
            btn_14.setText("LOG");
            btn_15.setText("LN");
            btn_16.setText("LB");

            //L3 normal: %,!,^,a/b,x^-1,+/-
            btn_21.setText("%");
            btn_22.setText("!N");
            btn_23.setText("^");
            btn_24.setText(R.string.bruch);

            btn_25.setText(getResources().getString(R.string.x_h_one));
            btn_26.setText("+/-");
        }else if(mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))){
            //L1 normal: PI,E,->DEC,->BIN,->OCT
            btn_11.setText("R");
            btn_12.setText("PFZ");
            btn_13.setText("ggT");
            btn_14.setText("kgV");
            btn_15.setText(getResources().getString(R.string.SUME));
            btn_16.setText(getResources().getString(R.string.MULP));

            //L3 normal: %,!,^,a/b,x^-1,+/-
            btn_21.setText("");
            btn_22.setText("");
            btn_23.setText("");
            btn_24.setText("");
            btn_25.setText("");
            btn_26.setText("");
        } else {
            if(UserFctGroups.contains(mode)){
                //NutzerFct
                setUpButtons(mode);
            }

        }
    }



    public static void setDefaultColors(Context c){
        SettingsApplier.setDefaultColors();
    }



    void setBackground(View x){
        if(buttonshapeID==0)applySettings();
        Drawable background;
        SettingsApplier.setColors(CalcActivity_science.this);
        float factor_font = SettingsApplier.getDarker_factor_font();
        boolean stroke = true;

        //Default Case
        background = getResources().getDrawable(buttonshapeID);
        SettingsApplier.setColor((CalcActivity_science.this),background, SettingsApplier.getColor_specials(CalcActivity_science.this),buttonfüllung,stroke);
        int darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.getColor_specials(CalcActivity_science.this),factor_font);
        SettingsApplier.setTextColor(x,darker);

        if(x instanceof Button){
            //fix für größe dieser kleinen unicode symbole
            if(((Button) x).getText().equals(getResources().getString(R.string.CLEAR_ALL)) || ((Button) x).getText().equals(getResources().getString(R.string.CLEAR))){
                ((Button) x).setTextSize(Math.min(30,((Button) btn_LINKS).getTextSize()*2));
            }
        }

        if(x.equals(spinner_shift) || x instanceof Spinner){
            background = getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor((CalcActivity_science.this),background, SettingsApplier.getColor_fops(CalcActivity_science.this),buttonfüllung,stroke);
            x.setBackground(background);

            ///((Spinner) x).setPopupBackgroundDrawable(background);
            x.setBackgroundColor(SettingsApplier.getColor_fops(CalcActivity_science.this));
            x.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            //((Spinner) x).setGravity(Gravity.CENTER_HORIZONTAL);
        }
        if(x.equals(display)){
            background = getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor((CalcActivity_science.this),background, SettingsApplier.getColor_display(CalcActivity_science.this),buttonfüllung,stroke);
            tV_ausgabe.setTextColor(SettingsApplier.getColor_displaytext(CalcActivity_science.this));
            tV_eingabe.setTextColor(SettingsApplier.getColor_displaytext(CalcActivity_science.this));
            x.setBackground(background);
        }

        if(VIEW_ACT.contains(x)){
            background = getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor((CalcActivity_science.this),background, SettingsApplier.getColor_act(CalcActivity_science.this),buttonfüllung,stroke);
            darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.getColor_act(CalcActivity_science.this),factor_font);
            SettingsApplier.setTextColor(x,darker);
            
        }
        else if(VIEW_FKT.contains(x)){
            background = getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor((CalcActivity_science.this),background, SettingsApplier.getColor_fkt(CalcActivity_science.this),buttonfüllung,stroke);
            darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.getColor_fkt(CalcActivity_science.this),factor_font);
            SettingsApplier.setTextColor(x,darker);
        }
        else if(VIEW_FOPS.contains(x)){
            background = getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor((CalcActivity_science.this),background, SettingsApplier.getColor_fops(CalcActivity_science.this),buttonfüllung,stroke);
            darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.getColor_fops(CalcActivity_science.this),factor_font);
            SettingsApplier.setTextColor(x,darker);
        }
        else if(VIEW_NUMBERS.contains(x)){
            background = getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor((CalcActivity_science.this),background, SettingsApplier.getColor_numbers(CalcActivity_science.this),buttonfüllung,stroke);
            darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.getColor_numbers(CalcActivity_science.this),factor_font);
            SettingsApplier.setTextColor(x,darker);
        }
        else if(VIEW_SAVES.contains(x)){
            background = getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor((CalcActivity_science.this),background, SettingsApplier.getColor_saves(CalcActivity_science.this),buttonfüllung,stroke);
            darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.getColor_saves(CalcActivity_science.this),factor_font);
            SettingsApplier.setTextColor(x,darker);
        }

        x.setBackground(background);

    }

    void setBackgroundImage() throws Exception {
        String path = PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).getString("backgroundimage", "");
        if(path.equals(""))return;

        if(!checkPermissionForReadExtertalStorage(this))requestPermissionForReadExtertalStorage(this);

        try{
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            View view = (ImageView) findViewById(R.id.container);
            view.setBackground(bd);
        } catch (Exception e){
            Toast t =  Toast.makeText(CalcActivity_science.this,"Could not draw Backgroundimage:"+e.getMessage(),Toast.LENGTH_LONG);
            t.show();
            Log.e("IMAGEERROR",path);
        }

    }


    void setBackgrounds(){
        display.setBackgroundColor(SettingsApplier.getColor_display(CalcActivity_science.this));
        science_background.setBackgroundColor(SettingsApplier.getColor_background(CalcActivity_science.this));
        //setBackground(spinner_shift);


        for(Button b: VIEW_ALL){
            setBackground(b);
        }

        setBackground(spinner_shift);
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
                    //Toast.makeText(CalcActivity_science.this, "SET: "+current_Callback, Toast.LENGTH_LONG).show();

                    //ausgabe_setText("");
                }
            }
        }
    }


    void transBtnFct(String fct){
        if(fct.startsWith("btn"))return;

        //"PI","E","NCR","NPR","%","!N","^","A/B","x\u207B\u00B9","+/-","√","\u00B3√","LOG","LN","LB","SIN","COS","TAN","ASIN","ATAN","ASINH","ACOSH","ATANH","SINH","COSH","TANH"};
        if(fct.equals("%")){
            ausgabe_setText(I.getPercent());
            return;
        }
        else if(fct.equals("A/B")){
            ausgabe_setText(I.getBruch());
            return;
        }
        else if(fct.equals("x\u207B\u00B9")){
            ausgabe_setText(I.getReciproke());
            return;
        }
        else if(fct.equals("+/-")){
            ausgabe_setText(I.getInvert());
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
        if(tV_eingabe_hasFocus){
            tV_eingabe.clearFocus();

            tV_eingabe.getText().insert(tV_eingabe.getSelectionStart(), i);
            I.setText(tV_eingabe.getText().toString());
        }
    }

    public void eingabeClear(){
        if(tV_eingabe_hasFocus) {
            int pos = tV_eingabe.getSelectionStart();
            I.clear(tV_eingabe.getSelectionStart());
            eingabeSetText(I.getDisplayableString());
            tV_eingabe.setSelection(Math.max(0,pos-1));
        }
    }

    public void eingabeSetText(String i){
        if(tV_eingabe_hasFocus) {
            tV_eingabe.setText(i);
            I.setText(tV_eingabe.getText().toString());
        }
    }

    private void applySettings(){
        //language
        language = PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).getString("pref_lang","english");
        if(language.equals("english") || language.equals("englisch")){
            btn_CONV.setText(R.string.CONVEN);
            btn_CONST.setText(R.string.CONSTEN);
            btn_menu.setText(R.string.MENU_EN);
            btn_verlauf.setText(R.string.VERLAUFEN);
            act_options = getResources().getStringArray(R.array.act_EN);
            mode_options = FunctionGroupSettingsActivity.getGroups(CalcActivity_science.this);
        }
        else if(language.equals("german") || language.equals("deutsch")){
            btn_CONV.setText(R.string.CONVDE);
            btn_CONST.setText(R.string.CONSTDE);
            btn_menu.setText(R.string.MENU_DE);
            btn_verlauf.setText(R.string.VERLAUFDE);
            act_options = getResources().getStringArray(R.array.act_DE);
            mode_options = FunctionGroupSettingsActivity.translateGroup(FunctionGroupSettingsActivity.getGroups(CalcActivity_science.this),"german");
        }

        //UserFctGroups.addAll(mode_options); UserFctGroups
        //Toast.makeText(CalcActivity_science.this,"Modes: "+Arrays.toString(mode_options),Toast.LENGTH_SHORT).show();
        UserFctGroups = new HashSet<>(Arrays.asList(FunctionGroupSettingsActivity.getUserGroups(CalcActivity_science.this)));

        //numbers
        if (PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).contains("pref_precision")) {
            String prec = PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).getString("pref_precision","10");
            if(prec != null)NumberString.precision =  Integer.valueOf(prec) + 1;
        }

        //buttonshape
        buttonshapeID = SettingsApplier.getButtonshapeID();

        //buttonfüllung
        if (PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).contains("buttonfüllung")) {
            buttonfüllung = PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).getString("buttonfüllung","voll");
        }

        //Fonts
        //Typeface font = Typeface.createFromAsset(getAssets(), "Crashed Scoreboard.ttf");
        Typeface monospace = Typeface.create("MONOSPACE",Typeface.NORMAL);
        Typeface sansSerif = Typeface.create("SANS_SERIF",Typeface.NORMAL);
        Typeface serif = Typeface.create("SERIF",Typeface.NORMAL);
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
                if(v.equals(tV_eingabe)){
                    tV_eingabe_hasFocus = true;
                }
                else tV_eingabe_hasFocus = false;
            } else {
                tV_eingabe_hasFocus = false;
            }
        }
    };


    public String verlaufToString(List<String> verlauf){
        if(verlauf.isEmpty())return"";
        String output="";
        for(int i=0; i<verlauf.size()-1; i++)output+=verlauf.get(i)+"_";
        output+=verlauf.get(verlauf.size()-1);
        return output;
    }

    private void stringToVerlauf(String verlauf_string){
        if(verlauf_string==null)return;
        verlauf = new LinkedList<>();
        verlauf.addAll(new LinkedList<String>(Arrays.asList(verlauf_string.split("_"))));
    }
}

