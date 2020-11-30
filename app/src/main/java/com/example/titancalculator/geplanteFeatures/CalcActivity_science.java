package com.example.titancalculator.geplanteFeatures;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.R;
import com.example.titancalculator.geplanteFeatures.notUsedHelper.ArrayUtils;
import com.example.titancalculator.geplanteFeatures.helper.MainDisplay.DesignApplier;
import com.example.titancalculator.geplanteFeatures.helper.MainDisplay.OnSwipeTouchListener;
import com.example.titancalculator.geplanteFeatures.helper.MainDisplay.SettingsApplier;
import com.example.titancalculator.helper.Math_String.MathEvaluator;
import com.example.titancalculator.helper.Math_String.NavigatableString;
import com.example.titancalculator.helper.Math_String.NumberString;
import com.example.titancalculator.geplanteFeatures.notUsedHelper.ShakeListener;
import com.example.titancalculator.helper.Math_String.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CalcActivity_science extends AppCompatActivity {
    int btn_digit_group_cnt = 0;
    private static int base = 0;
    String state_spinner_shift = "number_selection"; //number_selection, base_selection
    String btn_1_text = "";
    String btn_2_text = "";
    String btn_3_text = "";
    String btn_4_text = "";
    String btn_5_text = "";
    String btn_6_text = "";
    String btn_7_text = "";
    String btn_8_text = "";
    String btn_9_text = "";

    private static String[] MEMORY = new String[6];

    boolean shake = false;
    private ShakeListener mShaker;

    private Vibrator myVib;

    LinearLayout science_background;
    static final int REQUEST_CODE_CONST = 1;  // The request code
    static final int REQUEST_CODE_CONV = 1;  // The request code
    static final int REQUEST_CODE_history = 1;  // The request code
    Set<String> UserFctGroups = new HashSet<>();
    String current_Callback = "";
    String answer = "";
    boolean solve_inst_pref = false;
    public static Set<String> noImmidiateOps = new HashSet<>(Arrays.asList("³√", "ROOT", "√", "LOG", "P", "C", "%"));
    String language = "";
    String[] act_options;
    String[] mode_options;
    LinearLayout display;
    //L1
    Button btn_CONV;
    Button btn_CONST;
    Button btn_history;
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
    Spinner spinner_Base;
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
    //G2
    Button btn_open_bracket;
    Button btn_close_bracket;
    Button btn_add;
    Button btn_sub;
    Button btn_mul;
    Button btn_div;
    Button btn_com;
    Button btn_sep;
    Button btn_ans;
    Button btn_eq;
    boolean scientificNotation = false;
    boolean eT_eingabe_hasFocus = true;
    EditText eT_eingabe;
    EditText eT_ausgabe;
    NavigatableString I;
    LinearLayout LN2;
    LinearLayout LN3;
    LinearLayout LN4;
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
            Intent conversionIntent = null;
            conversionIntent = new Intent(CalcActivity_science.this, CalcActivity_normal.class);
            conversionIntent.putExtra("onPotraitReturnScience", true);
            conversionIntent.putExtra("input", eT_eingabe.getText().toString());
            conversionIntent.putExtra("output", eT_ausgabe.getText().toString());
            conversionIntent.putExtra("swipeDir", "");
            conversionIntent.putExtra("layout", "normal");
            startActivity(conversionIntent);
            finish();
            return;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        SettingsApplier.applySettings(CalcActivity_science.this);
        String mode = PreferenceManager.getDefaultSharedPreferences(this).getString("layout", "");
        if (!mode.isEmpty() && ChooserActivity.modes.contains(mode) && !mode.equals("science")) {
            Intent conversionIntent = null;
            if (mode.equals("normal")) {
                conversionIntent = new Intent(CalcActivity_science.this, CalcActivity_normal.class);
            } else if (mode.equals("small")) {
                conversionIntent = new Intent(CalcActivity_science.this, CalcActivity_small.class);
            } else Log.e("mode  ", mode);
            conversionIntent.putExtra("input", eT_eingabe.getText().toString());
            conversionIntent.putExtra("output", eT_ausgabe.getText().toString());
            conversionIntent.putExtra("swipeDir", "");
            conversionIntent.putExtra("layout", mode);
            startActivity(conversionIntent);
            finish();
        }
        setTitle("Rechner");
        SettingsApplier.setColors(CalcActivity_science.this);
        applySettings();
        setBackgrounds();

        eT_ausgabe.setOnFocusChangeListener(focusListener);
        eT_eingabe.setOnFocusChangeListener(focusListener);

        //spinner_shift = findViewById(R.id.spinner_SHIFT);
        //spinner_shift.setSelection(0);
        mode = "BASIC";
        //ArrayAdapter adpt_modeoptions = new ArrayAdapter<String>(this, R.layout.lvitem_layout, mode_options);

        //setUp_spinner_base();

        try {
            SettingsApplier.setBackgroundImage(CalcActivity_science.this, science_background);
        } catch (Exception e) {
            e.printStackTrace();
        }
        applySettings();
        eingabeAddText(current_Callback);
        //TODO
    }

    private void ausgabeSetText(String res) {
        //Toast.makeText(CalcActivity_science.this,"Ausgabe: "+res,Toast.LENGTH_SHORT).show();
        eT_ausgabe.setText(res);
    }

    private void toogleScientificNotation(){
        scientificNotation = !scientificNotation;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsApplier.applySettings(CalcActivity_science.this);

        setContentView(R.layout.activity_calc_science);
        if (shake) init_shake(CalcActivity_science.this, mShaker);

        Toast.makeText(CalcActivity_science.this, "mode: science", Toast.LENGTH_SHORT).show();
        mode = "";


        SettingsApplier.initSettings(CalcActivity_science.this);
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // code for portrait mode
            //Toast.makeText(this,"landscape mode",Toast.LENGTH_LONG).show();
            Intent conversionIntent = new Intent(CalcActivity_science.this, CalcActivity_normal.class);
            conversionIntent.putExtra("onPotraitReturnScience", true);
            startActivity(conversionIntent);
            finish();
            return;
        }
        setTitle("TITAN CALC");
        MEMORY = new String[6];
        MEMORY = loadMemory(CalcActivity_science.this);
        science_background = findViewById(R.id.science_background);
        eT_eingabe = findViewById(R.id.eT_eingabe);
        eT_ausgabe = findViewById(R.id.eT_ausgabe);
        eT_ausgabe.setOnFocusChangeListener(focusListener);
        eT_eingabe.setOnFocusChangeListener(focusListener);
        mode = "BASIC";
        eT_eingabe.setShowSoftInputOnFocus(false);
        eT_eingabe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(CalcActivity_science.this);
                //v.performClick();
                Toast.makeText(getBaseContext(),"eingabeChanged",Toast.LENGTH_SHORT);

                return false;
            }
        });
        eT_ausgabe.setShowSoftInputOnFocus(false);
        eT_ausgabe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(CalcActivity_science.this);
                //v.performClick();
                return false;
            }
        });
        science_background.setOnTouchListener(new OnSwipeTouchListener(CalcActivity_science.this) {
            public void onSwipeTop() {
                int new_btn_digit_group_cnt = ((btn_digit_group_cnt - 1) % (MathEvaluator.digit_alphabet.length / 9));
                if (new_btn_digit_group_cnt < base) {
                    btn_digit_group_cnt = new_btn_digit_group_cnt;
                    assignModeFct();
                }
                Toast.makeText(CalcActivity_science.this, "top: " + new_btn_digit_group_cnt + " < " + base + " = " + (new_btn_digit_group_cnt < base), Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                Toast.makeText(CalcActivity_science.this, "right", Toast.LENGTH_SHORT).show();
                Intent conversionIntent = new Intent(CalcActivity_science.this, ChooserActivity.class);
                conversionIntent.putExtra("input", eT_eingabe.getText().toString());
                conversionIntent.putExtra("output", eT_ausgabe.getText().toString());
                conversionIntent.putExtra("swipeDir", "right");
                conversionIntent.putExtra("layout", "science");
                startActivity(conversionIntent);
                finish();
            }

            public void onSwipeLeft() {
                Toast.makeText(CalcActivity_science.this, "left", Toast.LENGTH_SHORT).show();
                Intent conversionIntent = new Intent(CalcActivity_science.this, ChooserActivity.class);
                conversionIntent.putExtra("input", eT_eingabe.getText().toString());
                conversionIntent.putExtra("output", eT_ausgabe.getText().toString());
                conversionIntent.putExtra("swipeDir", "left");
                conversionIntent.putExtra("layout", "science");
                startActivity(conversionIntent);
                finish();
            }

            public void onSwipeBottom() {
                int new_btn_digit_group_cnt = ((btn_digit_group_cnt + 1) % (MathEvaluator.digit_alphabet.length / 9));
                if (new_btn_digit_group_cnt < base) {
                    btn_digit_group_cnt = new_btn_digit_group_cnt;
                    assignModeFct();
                }
                Toast.makeText(CalcActivity_science.this, "top: " + new_btn_digit_group_cnt, Toast.LENGTH_SHORT).show();
            }
        });
        //setzt hintergrundbild
        I = new NavigatableString();

        eingabeSetText("");
        //find
        display = findViewById(R.id.display);
        science_background = findViewById(R.id.science_background);
        //L1
        spinner_shift = findViewById(R.id.spinner_SHIFT);
        btn_CONV = findViewById(R.id.btn_CONV);
        btn_history = findViewById(R.id.btn_Verlauf);
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
        spinner_Base = findViewById(R.id.spinner_Base);
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
        btn_sep = findViewById(R.id.btn_sep);
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
        VIEW_ACT = new HashSet<>();
        VIEW_FKT = new HashSet<>(Arrays.asList(new Button[]{btn_clear, btn_clearall, btn_LINKS, btn_RECHTS}));
        VIEW_FOPS = new HashSet<>(Arrays.asList(new Button[]{btn_11, btn_12, btn_13, btn_14, btn_15, btn_16, btn_21, btn_22, btn_23, btn_24, btn_25, btn_26}));
        VIEW_NUMBERS = new HashSet<>(Arrays.asList(new Button[]{btn_com, btn_sep, btn_ans, btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9}));
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


        try {
            SettingsApplier.setBackgroundImage(CalcActivity_science.this, science_background);
        } catch (Exception e) {
            e.printStackTrace();
        }

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                current_Callback = "";
                Intent conversionIntent = new Intent(CalcActivity_science.this, SettingsActivity.class);
                startActivity(conversionIntent);
            }
        });
        spinner_shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                mode = spinner_shift.getSelectedItem().toString();
                assignModeFct();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /*
        btn_MENU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SettingsApplier.vibrate_on)myVib.vibrate(SettingsApplier.vibrate_length);
				view.startAnimation(buttonClick);
                current_Callback = "";
                Intent conversionIntent = new Intent(CalcActivity_science.this,SettingsActivity.class);
                startActivity(conversionIntent);
                //TODO
                //
            }
        });
         */
        btn_CONV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                current_Callback = "";
                Intent conversionIntent = new Intent(CalcActivity_science.this, ConversionActivity.class);
                startActivityForResult(conversionIntent, REQUEST_CODE_CONV);
                //TODO

            }
        });
        btn_CONST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                current_Callback = "";
                Intent constIntent = new Intent(CalcActivity_science.this, ConstantsActivity.class);
                startActivityForResult(constIntent, REQUEST_CODE_CONST);
            }
        });
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                current_Callback = "";
                Intent historyIntent = new Intent(CalcActivity_science.this, HistoryActivity.class);

                String[] historyarray = HistoryActivity.getHistory().toArray(new String[HistoryActivity.getHistory().size()]);
                //Toast.makeText(CalcActivity_science.this,"hist: "+Arrays.toString(historyarray),Toast.LENGTH_LONG).show();
                historyIntent.putExtra("history", historyarray);

                //Toast.makeText(CalcActivity_science.this,Arrays.toString(arrayhistory),Toast.LENGTH_LONG).show();
                startActivityForResult(historyIntent, REQUEST_CODE_history);
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                //TODO: TAN( etc. vollständig löschen
                eingabeClear();
                ausgabeSetText("");
                {
                    if (!noImmidiateOps.contains(I.getDisplayableString().trim())) {
                        if (solve_inst_pref) {
                            answer = I.getResult();
                            if (!answer.equals("Math Error")) ausgabeSetText(answer);
                        }
                    }
                }

            }
        });
        btn_clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                eT_eingabe.setText("");
                eT_ausgabe.setText("");
                I.setText(eT_eingabe.getText().toString());
            }
        });
        //L2
        //normal: PI,E,CONST,CONV
        btn_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("π");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    ausgabeSetText(I.getPFZ());
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("SIN");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_11");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("Zn()");
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("SINH");
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    eingabeAddText("AND(,)");
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    MEMORY[0] = getSelection();
                    saveMemory(CalcActivity_science.this, MEMORY);
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_11.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
            }
        });
        btn_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("e");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    if (language.equals("german") || language.equals("deutsch")) {
                        eingabeAddText("GGT(,)");
                    } else {
                        eingabeAddText("GCD(,)");
                    }
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("COS");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_12");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("Zb(,)");
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("COSH");
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    eingabeAddText("OR(,)");
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    MEMORY[1] = getSelection();
                    saveMemory(CalcActivity_science.this, MEMORY);
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_12.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
            }
        });
        btn_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("^");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    if (language.equals("german") || language.equals("deutsch")) {
                        eingabeAddText("KGV(,)");
                    } else {
                        eingabeAddText("LCM(,)");
                    }
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("TAN");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_13");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("C");
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("TANH");
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    eingabeAddText("XOR(,)");
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    MEMORY[2] = getSelection();
                    saveMemory(CalcActivity_science.this, MEMORY);
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_13.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
            }
        });
        btn_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("LOG");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText(getResources().getString(R.string.SUME) + "(,)");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("COT");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_14");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("P");
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("ASINH");
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    eingabeAddText("NOT()");
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    MEMORY[3] = getSelection();
                    saveMemory(CalcActivity_science.this, MEMORY);
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_14.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
            }
        });
        btn_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("LN");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText(getResources().getString(R.string.MULP) + "(,)");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("ASIN");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_15");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("MEAN()");
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("ACOSH");
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    //TODO:  ausgabeSetText(I.getBIN());
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    MEMORY[4] = getSelection();
                    saveMemory(CalcActivity_science.this, MEMORY);
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_15.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
            }
        });
        btn_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("LB");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("ACOS");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_16");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("VAR()");
                } else if (mode.equals("HYPER")) {
                    eingabeAddText("ATANH");
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    //TODO: ausgabeSetText(I.getOCT());
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    MEMORY[5] = getSelection();
                    saveMemory(CalcActivity_science.this, MEMORY);
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_16.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
            }
        });
        //L3
        //normal: %,!,^,a/b,x^-1,+/-
        //TRIGO: ASINH,ACOSH,ATANH,SINH,COSH,TANH
        btn_21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("³√");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    ausgabeSetText(I.getPercent());
                    answer = I.getPercent();
                    ausgabeSetText(answer);
                    HistoryActivity.saveHistory(CalcActivity_science.this);

                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("ATAN");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_21");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("E()");
                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    //TODO: ausgabeSetText(I.getDEC());
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    replaceSelection(MEMORY[0]);
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_21.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
            }
        });
        btn_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("√");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    ausgabeSetText(I.getBruch());
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("ACOT");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_22");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                    eingabeAddText("2√(VAR())");
                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                    //TODO: ausgabeSetText(I.getHEX());
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    replaceSelection(MEMORY[1]);
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_22.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
            }
        });
        btn_23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("³");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    ausgabeSetText(I.getReciproke());
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    ausgabeSetText(I.getDEG());
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_23");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    replaceSelection(MEMORY[2]);
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_23.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
            }
        });
        btn_24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("²");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    ausgabeSetText(I.getInvert());
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    ausgabeSetText(I.getRAD());
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_24");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    replaceSelection(MEMORY[3]);
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_24.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
            }
        });
        btn_25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("10^");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText("MIN()");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("toPolar(,)");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_25");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    replaceSelection(MEMORY[4]);
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_25.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
            }
        });
        btn_26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
                    eingabeAddText("!");
                } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
                    eingabeAddText("MAX()");
                } else if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
                    eingabeAddText("toCart(,)");
                } else if (mode.equals(getResources().getString(R.string.USER_DE)) || mode.equals(getResources().getString(R.string.USER_EN))) {
                    transBtnFct("btn_26");
                } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
                } else if (mode.equals("HYPER")) {
                } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
                    replaceSelection(MEMORY[5]);
                } else if (UserFctGroups.contains(mode)) {
                    transBtnFct(btn_26.getText().toString());
                } else {
                    String display = "Unknown Mode: " + mode;
                    Toast unknownMode = Toast.makeText(CalcActivity_science.this, display, Toast.LENGTH_LONG);
                    unknownMode.show();
                }
            }
        });
        ////setUp_spinner_base();
        btn_LINKS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                int pos = eT_eingabe.getSelectionStart();
                eT_eingabe.setSelection(Math.max(0, pos - 1));

            }
        });
        btn_RECHTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                int pos = eT_eingabe.getSelectionStart();
                eT_eingabe.setSelection(Math.min(eT_eingabe.length(), pos + 1));
            }
        });
        //G1
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (state_spinner_shift.equals("base_selection") && !btn_1_text.equals("1")) {
                    base = Integer.valueOf(btn_1_text);
                    state_spinner_shift = "number_selection";
                    //setUp_spinner_base();
                } else {
                    eingabeAddText(btn_1_text);
                }
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (state_spinner_shift.equals("base_selection")) {
                    base = Integer.valueOf(btn_2_text);
                    state_spinner_shift = "number_selection";
                    //setUp_spinner_base();
                } else {
                    eingabeAddText(btn_2_text);
                }
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (state_spinner_shift.equals("base_selection")) {
                    base = Integer.valueOf(btn_3_text);
                    state_spinner_shift = "number_selection";
                    //setUp_spinner_base();
                } else {
                    eingabeAddText(btn_3_text);
                }
            }
        });
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (state_spinner_shift.equals("base_selection")) {
                    base = Integer.valueOf(btn_4_text);
                    state_spinner_shift = "number_selection";
                    //setUp_spinner_base();
                } else {
                    eingabeAddText(btn_4_text);
                }
            }
        });
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (state_spinner_shift.equals("base_selection")) {
                    base = Integer.valueOf(btn_5_text);
                    state_spinner_shift = "number_selection";
                    //setUp_spinner_base();
                } else {
                    eingabeAddText(btn_5_text);
                }
            }
        });
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (state_spinner_shift.equals("base_selection")) {
                    base = Integer.valueOf(btn_6_text);
                    state_spinner_shift = "number_selection";
                    //setUp_spinner_base();
                } else {
                    eingabeAddText(btn_6_text);
                }
            }
        });
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (state_spinner_shift.equals("base_selection")) {
                    base = Integer.valueOf(btn_7_text);
                    state_spinner_shift = "number_selection";
                    //setUp_spinner_base();
                } else {
                    eingabeAddText(btn_7_text);
                }
            }
        });
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (state_spinner_shift.equals("base_selection")) {
                    base = Integer.valueOf(btn_8_text);
                    state_spinner_shift = "number_selection";
                    //setUp_spinner_base();
                } else {
                    eingabeAddText(btn_8_text);
                }
            }
        });
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                if (state_spinner_shift.equals("base_selection")) {
                    base = Integer.valueOf(btn_9_text);
                    state_spinner_shift = "number_selection";
                    //setUp_spinner_base();
                } else {
                    eingabeAddText(btn_9_text);
                }
            }
        });
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                eingabeAddText("0");
            }
        });
        btn_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                eingabeAddText(".");

            }
        });
        btn_sep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                eingabeAddText(",");
            }
        });
        btn_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                eingabeAddText("ANS");
            }
        });
        //G2
        btn_open_bracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                eingabeAddText("(");
            }
        });
        btn_close_bracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                eingabeAddText(")");
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                eingabeAddText("+");
            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                eingabeAddText("-");
            }
        });
        btn_mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                eingabeAddText("*");
            }
        });
        btn_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                eingabeAddText("/");
            }
        });
        btn_eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!eT_eingabe.getText().toString().equals(I.getDisplayableString())) {
                    I.setText(eT_eingabe.getText().toString());
                }
                if (SettingsApplier.vibrate_on) myVib.vibrate(SettingsApplier.vibrate_length);
                view.startAnimation(buttonClick);
                answer = I.getResult();

                if(scientificNotation){
                    answer = I.normalToScientific();
                } else {
                    answer = I.scientificToNormal();
                }

                ausgabeSetText(answer);
                HistoryActivity.addHistory(CalcActivity_science.this, eT_eingabe.getText().toString(), answer);
            }
        });
        btn_eq.setLongClickable(true);
        btn_eq.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                toogleScientificNotation();
                return false;
            }
        });


        Intent v = getIntent();
        eT_eingabe.setText(v.getStringExtra("input"));
        eT_ausgabe.setText(v.getStringExtra("output"));

        try {
            SettingsApplier.setBackgroundImage(CalcActivity_science.this, science_background);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //onCreate end
        setBackgrounds();
        SettingsApplier.setETTextSize(CalcActivity_science.this, eT_eingabe, 3);
        SettingsApplier.setETTextSize(CalcActivity_science.this, eT_ausgabe, 3);

    }


    public void setUpButtons(String group) {
        //L1
        setUpButton(btn_11, group + "_btn11");
        setUpButton(btn_12, group + "_btn12");
        setUpButton(btn_13, group + "_btn13");
        setUpButton(btn_14, group + "_btn14");
        setUpButton(btn_15, group + "_btn15");
        setUpButton(btn_16, group + "_btn16");
        //L2
        setUpButton(btn_21, group + "_btn21");
        setUpButton(btn_22, group + "_btn22");
        setUpButton(btn_23, group + "_btn23");
        setUpButton(btn_24, group + "_btn24");
        setUpButton(btn_25, group + "_btn25");
        setUpButton(btn_26, group + "_btn26");
    }

    public void setUpButton(Button x, String name) {
        x.setText(PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).getString(name, name));
    }

    String assignBtnNumberText(int i) {
        btn_digit_group_cnt = 0; //TODO: für basesel entfernen
        if (state_spinner_shift.equals("base_selection")) {
            return String.valueOf(btn_digit_group_cnt * 9 + i);
        } else if (state_spinner_shift.equals("number_selection")) {
            return String.valueOf(MathEvaluator.digit_alphabet[btn_digit_group_cnt * 9 + i]);
        }
        return "";
    }

    void assignModeFct() {
        btn_1_text = assignBtnNumberText(1);
        btn_2_text = assignBtnNumberText(2);
        btn_3_text = assignBtnNumberText(3);
        btn_4_text = assignBtnNumberText(4);
        btn_5_text = assignBtnNumberText(5);
        btn_6_text = assignBtnNumberText(6);
        btn_7_text = assignBtnNumberText(7);
        btn_8_text = assignBtnNumberText(8);
        btn_9_text = assignBtnNumberText(9);

        btn_1.setText(btn_1_text);
        btn_2.setText(btn_2_text);
        btn_3.setText(btn_3_text);
        btn_4.setText(btn_4_text);
        btn_5.setText(btn_5_text);
        btn_6.setText(btn_6_text);
        btn_7.setText(btn_7_text);
        btn_8.setText(btn_8_text);
        btn_9.setText(btn_9_text);

        if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
            //L1 normal: SIN,COS,TAN,ASIN,ACOS,ATAN
            btn_11.setText("SIN");
            btn_12.setText("COS");
            btn_13.setText("TAN");
            btn_14.setText("COT");
            btn_15.setText("ASIN");
            btn_16.setText("ACOS");
            //L3 normal: >DEG/>RAD/toPolar/toCart
            btn_21.setText("ATAN");
            btn_22.setText("ACOT");
            btn_23.setText(">DEG");
            btn_24.setText(">RAD");
            btn_25.setText("toPolar");
            btn_26.setText("toCart");
        }
        if (mode.equals("HYPER")) {
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
        } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
            //L1 normal: SIN,COS,TAN,ASIN,ACOS,ATAN
            btn_11.setText("ZN(N)");
            btn_12.setText("ZB(X;Y)");
            btn_13.setText("NCR");
            btn_14.setText("NPR");
            btn_15.setText("MEAN");
            btn_16.setText("VAR");
            //L3 normal: ASINH,ACOSH,ATANH,SINH,COSH,TANH
            btn_21.setText("E");
            btn_22.setText("S");
            btn_23.setText("");
            btn_24.setText("");
            btn_25.setText("");
            btn_26.setText("");
        } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
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
        } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
            //L1
            btn_11.setText("M1");
            btn_12.setText("M2");
            btn_13.setText("M3");
            btn_14.setText("M4");
            btn_15.setText("M5");
            btn_16.setText("M6");
            //L2
            btn_21.setText(">M1");
            btn_22.setText(">M2");
            btn_23.setText(">M3");
            btn_24.setText(">M4");
            btn_25.setText(">M5");
            btn_26.setText(">M6");
        } else if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
            //L1 normal: PI,E,->DEC,->BIN,->OCT
            btn_11.setText(R.string.PI);
            btn_12.setText(R.string.E);
            btn_13.setText("^");
            btn_14.setText("LOG");
            btn_15.setText("LN");
            btn_16.setText("LB");
            //L3 normal: %,!,^,a/b,x^-1,+/-
            btn_21.setText("³√");
            btn_22.setText("√");
            btn_23.setText("x³");
            btn_24.setText("x²");
            btn_25.setText("10^x");
            btn_26.setText("!");
        } else if (mode.equals(getResources().getString(R.string.BASIC2_DE)) || mode.equals(getResources().getString(R.string.BASIC2_EN))) {
            //L1 normal: PI,E,->DEC,->BIN,->OCT
            btn_11.setText("PFZ");
            if (language.equals("german") || language.equals("deutsch")) {
                btn_12.setText("GGT");
            } else {
                btn_12.setText("GCD");
            }
            if (language.equals("german") || language.equals("deutsch")) {
                btn_13.setText("KGV");
            } else {
                btn_13.setText("LCM");
            }
            btn_14.setText(getResources().getString(R.string.SUME));
            btn_15.setText(getResources().getString(R.string.MULP));
            btn_16.setText("");
            //L3 normal: %,!,^,a/b,x^-1,+/-
            btn_21.setText(">%");
            btn_22.setText("A/B");
            btn_23.setText(R.string.x_h_one);
            btn_24.setText("+/-");
            btn_25.setText("MIN");
            btn_26.setText("MAX");
        }
        if (mode.equals("PROGRAM") || mode.equals("PROGRAMM")) {
            //L1 normal: SIN,COS,TAN,ASIN,ACOS,ATAN
            btn_11.setText("P1");
            btn_12.setText("P2");
            btn_13.setText("P3");
            btn_14.setText("P4");
            btn_15.setText("P5");
            btn_16.setText("P6");
            //L3 normal: >DEG/>RAD/toPolar/toCart
            btn_21.setText(">P1");
            btn_22.setText(">P1");
            btn_23.setText(">P3");
            btn_24.setText(">P4");
            btn_25.setText(">P5");
            btn_26.setText(">P6");
        } else {
            if (UserFctGroups.contains(mode)) {
                //NutzerFct
                setUpButtons(mode);
            }
        }
    }

    public static void setDefaultColors(Context c) {
        SettingsApplier.setDefaultColors();
    }


    void setBackgrounds() {
        science_background.setBackgroundColor(SettingsApplier.getColor_background(CalcActivity_science.this));
        //SettingsApplier.drawVectorImage(CalcActivity_science.this,btn_clear,R.drawable.ic_clear,SettingsApplier.getColor_fkt(CalcActivity_science.this));
        //SettingsApplier.drawVectorImage(CalcActivity_science.this,btn_clearall,R.drawable.ic_clear_all,SettingsApplier.getColor_fkt(CalcActivity_science.this));

        SettingsApplier.drawVectorImage(CalcActivity_science.this, btn_CONST, R.drawable.ic_konstanten1, SettingsApplier.getColor_act(CalcActivity_science.this));
        SettingsApplier.drawVectorImage(CalcActivity_science.this, btn_CONV, R.drawable.ic_lineal, SettingsApplier.getColor_act(CalcActivity_science.this));
        SettingsApplier.drawVectorImage(CalcActivity_science.this, btn_history, R.drawable.ic_verlauf, SettingsApplier.getColor_act(CalcActivity_science.this));
        SettingsApplier.drawVectorImage(CalcActivity_science.this, btn_menu, R.drawable.ic_menu_black_24dp, SettingsApplier.getColor_act(CalcActivity_science.this));

        for (Button b : VIEW_ALL) {
            if (!VIEW_FOPS.contains(b)) {
                SettingsApplier.setFonts(CalcActivity_science.this, b);
            }
            if (VIEW_ACT.contains(b))
                SettingsApplier.setViewDesign(CalcActivity_science.this, b, SettingsApplier.getColor_act(CalcActivity_science.this));
            if (VIEW_FKT.contains(b))
                SettingsApplier.setViewDesign(CalcActivity_science.this, b, SettingsApplier.getColor_fkt(CalcActivity_science.this));
            if (VIEW_FOPS.contains(b))
                SettingsApplier.setViewDesign(CalcActivity_science.this, b, SettingsApplier.getColor_fops(CalcActivity_science.this));
            if (VIEW_NUMBERS.contains(b))
                SettingsApplier.setViewDesign(CalcActivity_science.this, b, SettingsApplier.getColor_numbers(CalcActivity_science.this));
            if (VIEW_SAVES.contains(b))
                SettingsApplier.setViewDesign(CalcActivity_science.this, b, SettingsApplier.getColor_saves(CalcActivity_science.this));
            if (VIEW_SPECIALS.contains(b))
                SettingsApplier.setViewDesign(CalcActivity_science.this, b, SettingsApplier.getColor_specials(CalcActivity_science.this));
        }
        SettingsApplier.setViewDesign(CalcActivity_science.this, spinner_Base, SettingsApplier.getColor_numbers(CalcActivity_science.this));
        SettingsApplier.setArrayAdapter(CalcActivity_science.this, spinner_shift, mode_options, SettingsApplier.getColor_fops(CalcActivity_science.this));
        SettingsApplier.setViewDesign(CalcActivity_science.this, spinner_shift, SettingsApplier.getColor_fops(CalcActivity_science.this));

        SettingsApplier.setViewDesign(CalcActivity_science.this, display, SettingsApplier.getColor_display(CalcActivity_science.this));
        SettingsApplier.setETDesign(CalcActivity_science.this, eT_eingabe, 4);
        SettingsApplier.setETDesign(CalcActivity_science.this, eT_ausgabe, 4);

        //fires if layout drawn
        btn_open_bracket.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                SettingsApplier.centerTextButton(btn_open_bracket, 7);
                SettingsApplier.centerTextButton(btn_close_bracket, 7);
                SettingsApplier.centerTextButton(btn_mul, 7);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        if (requestCode == REQUEST_CODE_history) {
            if (resultCode == RESULT_OK) {
                //Use Data to get string
                current_Callback = data.getStringExtra("RESULT_STRING");
                if (!current_Callback.isEmpty()) {
                    //eingabeSetText(current_Callback);
                    //Toast.makeText(CalcActivity_science.this, "SET: "+current_Callback, Toast.LENGTH_LONG).show();
                    //ausgabeSetText("");
                }
            }
        }
    }

    void transBtnFct(String fct) {
        if (fct.startsWith("btn")) return;
        //"PI","E","NCR","NPR","%","!N","^","A/B","x\u207B\u00B9","+/-","√","\u00B3√","LOG","LN","LB","SIN","COS","TAN","ASIN","ATAN","ASINH","ACOSH","ATANH","SINH","COSH","TANH"};
        if (fct.equals(">%")) {
            ausgabeSetText(I.getPercent());
            return;
        } else if (fct.equals("A/B")) {
            ausgabeSetText(I.getBruch());
            return;
        } else if (fct.equals("x\u207B\u00B9")) {
            ausgabeSetText(I.getReciproke());
            return;
        } else if (fct.equals("+/-")) {
            ausgabeSetText(I.getInvert());
            return;
        }
        String A = fct;
        A = A.replace("NCR", "C");
        A = A.replace("NCR", "C");
        A = A.replace("!N", "!");
        A = A.replace("x\u207B\u00B9", "C");
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


    public void replaceSelection(String input) {
        if (input == null || input.isEmpty()) return;
        int selStart = -1;
        int selEnd = -1;
        if (eT_eingabe.hasFocus()) {
            selStart = eT_eingabe.getSelectionStart();
            selEnd = eT_eingabe.getSelectionEnd();
            if (selStart >= 0 && selEnd >= 0 && selStart <= selEnd && selStart <= eT_eingabe.length() && selEnd <= eT_eingabe.length()) {
                String etE_text = eT_eingabe.getText().toString();
                etE_text = StringUtils.replace(etE_text, input, selStart, selEnd);

                Toast.makeText(CalcActivity_science.this, "selection: " + etE_text.toString(), Toast.LENGTH_LONG).show();
                eT_eingabe.setText(etE_text);
            } else {
                eT_eingabe.setText(input);
            }
            eT_eingabe.setSelection(selEnd);
            I.setText(eT_eingabe.getText().toString());
        }
    }


    public String getSelection() {
        String selection = "";
        int selStart = -1;
        int selEnd = -1;
        if (eT_eingabe.hasFocus()) {
            selStart = eT_eingabe.getSelectionStart();
            selEnd = eT_eingabe.getSelectionEnd();
            if (selStart > 0 && selEnd > 0 && selStart < selEnd) {
                return eT_eingabe.getText().toString().substring(selStart, selEnd);
            } else {
                return eT_eingabe.getText().toString();
            }
        } else if (eT_ausgabe.hasFocus()) {
            selStart = eT_ausgabe.getSelectionStart();
            selEnd = eT_ausgabe.getSelectionEnd();
            if (selStart > 0 && selEnd > 0 && selStart < selEnd) {
                return eT_ausgabe.getText().toString().substring(selStart, selEnd);
            } else {
                return eT_ausgabe.getText().toString();
            }
        }
        return selection;
    }

    public void eingabeAddText(String i) {
        if (eT_eingabe_hasFocus) {
            eT_eingabe.clearFocus();
        }
        eT_eingabe.getText().insert(eT_eingabe.getSelectionStart(), i);
        I.setText(eT_eingabe.getText().toString());
        if (solve_inst_pref) {
            if (!noImmidiateOps.contains(i.trim())) {
                answer = I.getResult();
                if (!answer.equals("Math Error")) ausgabeSetText(answer);
            }
        }
    }

    public void eingabeClear() {
        if (!eT_eingabe.getText().toString().equals(I.getDisplayableString())) {
            I.setText(eT_eingabe.getText().toString());
        }
        if (eT_eingabe_hasFocus) {
            int pos = eT_eingabe.getSelectionStart();
            I.clear(eT_eingabe.getSelectionStart());
            eingabeSetText(I.getDisplayableString());
            eT_eingabe.setSelection(Math.max(0, pos - 1));
        }
    }

    public void eingabeSetText(String i) {
        if (eT_eingabe_hasFocus) {
            eT_eingabe.setText(i);
            I.setText(eT_eingabe.getText().toString());
        }
    }

    public static void setBase(Context context, int Base) {
        if (Base <= 1) {
            return;
        } else {
            base = Base;
        }
    }

    public static int getBase(Context context) {
        if (base == 0) {
            String baseString = PreferenceManager.getDefaultSharedPreferences(context).getString("base", "10");
            if (baseString == null) {
                setBase(context, 0);
            } else base = Integer.parseInt(baseString);
        }
        return base;
    }

    private void applySettings() {
        //language
        language = PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).getString("pref_lang", "english");
        if (language.equals("english") || language.equals("englisch")) {
            //btn_CONV.setText(R.string.CONVEN);
            //btn_CONST.setText(R.string.CONSTEN);
            btn_menu.setText("");
            //btn_history.setText(R.string.historyEN);
            act_options = getResources().getStringArray(R.array.act_EN);
            mode_options = FunctionGroupSettingsActivity.getGroups(CalcActivity_science.this);
        } else if (language.equals("german") || language.equals("deutsch")) {
            //btn_CONV.setText(R.string.CONVDE);
            // btn_CONST.setText(R.string.CONSTDE);
            btn_menu.setText("");
            //btn_history.setText(R.string.historyDE);
            act_options = getResources().getStringArray(R.array.act_DE);
            mode_options = FunctionGroupSettingsActivity.translateGroup(FunctionGroupSettingsActivity.getGroups(CalcActivity_science.this), "german");
        }
        //UserFctGroups.addAll(mode_options); UserFctGroups
        //Toast.makeText(CalcActivity_science.this,"Modes: "+Arrays.toString(mode_options),Toast.LENGTH_SHORT).show();
        UserFctGroups = new HashSet<>(Arrays.asList(FunctionGroupSettingsActivity.getUserGroups(CalcActivity_science.this)));
        //numbers
        if (PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).contains("pre_decimal_places_pref")) {
            String prec = PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).getString("pre_decimal_places_pref", "10");
            if (prec != null) NumberString.predec_places = Integer.valueOf(prec) + 1;
        }
        if (PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).contains("decimal_places_pref")) {
            String prec = PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).getString("decimal_places_pref", "10");
            if (prec != null) NumberString.dec_places = Integer.valueOf(prec) + 1;
        }

        //Fonts
        //Typeface font = Typeface.createFromAsset(getAssets(), "Crashed Scoreboard.ttf");
        Typeface monospace = Typeface.create("MONOSPACE", Typeface.NORMAL);
        Typeface sansSerif = Typeface.create("SANS_SERIF", Typeface.NORMAL);
        Typeface serif = Typeface.create("SERIF", Typeface.NORMAL);
        //Math Settings
        solve_inst_pref = PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).getBoolean("solve_inst_pref", false);

        String mean_mode = PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).getString("mean_mode", "AriMit");
        String var_mode = PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).getString("var_mode", "AriVar");
        I.setMeanMode(mean_mode);
        I.setVarMode(var_mode);

        String base_set = PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this).getString("base", "10");
        if (base_set.matches("[0-9]*")) {
            base = Integer.parseInt(base_set);
        }


    }

    public static boolean checkPermissionForReadExtertalStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public static void requestPermissionForReadExtertalStorage(Context context) throws Exception {
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
            if (hasFocus) {
                if (v.equals(eT_eingabe)) {
                    eT_eingabe_hasFocus = true;
                } else eT_eingabe_hasFocus = false;
            } else {
                eT_eingabe_hasFocus = false;
            }
        }
    };


    public static ArrayList<String> loadHistory(Context c) {
        String HIST = PreferenceManager.getDefaultSharedPreferences(c).getString("HISTORY", "");
        ArrayList<String> histList = ArrayUtils.stringToList(HIST);
        Log.e("array mem", Arrays.toString(histList.toArray()));
        return histList;
    }


    public static void saveMemory(Context c, String[] Memory) {
        String MEMS = ArrayUtils.arrayToString(Memory);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferences.edit();
        Toast.makeText(c, "save: " + MEMS, Toast.LENGTH_SHORT).show();
        editor.putString("MEMORY", MEMS);
        editor.commit();
    }

    public static String[] loadMemory(Context c) {
        String MEMS = PreferenceManager.getDefaultSharedPreferences(c).getString("MEMORY", "");
        String[] memarray = ArrayUtils.stringToArray(MEMS);
        Log.e("array mem", Arrays.toString(memarray));
        String[] res = new String[6];
        for (int i = 0; i < 6; i++) {
            if (i < memarray.length) res[i] = memarray[i];
            else res[i] = "";
        }
        return res;
    }

    public static void init_shake(final Context c, ShakeListener mShaker) {
        mShaker = new ShakeListener(c);
        mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {
            public void onShake() {
                String random_design = DesignApplier.designs[(int) (Math.random() * DesignApplier.designs.length)];
                Toast.makeText(c, "Design: " + random_design, Toast.LENGTH_SHORT).show();
                DesignApplier.apply_theme(c, random_design);
            }
        });
    }

    public void setUp_spinner_base() {
        if (state_spinner_shift.equals("number_selection")) {
            //save base
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CalcActivity_science.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("base", String.valueOf(base));
            editor.commit();

            //set up spinner
            int d = (int) Math.ceil(((double) (base) / 10) - 1.0);
            int beg = 0, end = Math.max(d, 0);
            String[] subarray = new String[end - beg + 2];
            System.arraycopy(MathEvaluator.digit_alphabet_groups, beg, subarray, 0, subarray.length);
            subarray[subarray.length - 1] = "set base";

            SettingsApplier.setArrayAdapter(CalcActivity_science.this, spinner_Base, subarray, SettingsApplier.getColor_fkt(CalcActivity_science.this));

            spinner_Base.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //Log.e("spinner: ",adapterView.getItemAtPosition(i).toString());
                    //Toast.makeText(CalcActivity_science.this, "spinner: "+adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                    if (adapterView.getItemAtPosition(i).equals("set base")) {
                        state_spinner_shift = "base_selection";
                        //setUp_spinner_base();
                    } else {
                        int new_btn_digit_group_cnt = ((i % (MathEvaluator.digit_alphabet.length / 9)));
                        if (new_btn_digit_group_cnt * 9 < base) {
                            btn_digit_group_cnt = new_btn_digit_group_cnt;
                        } else {
                            btn_digit_group_cnt = 0;
                        }
                        assignModeFct();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        } else if (state_spinner_shift.equals("base_selection")) {
            SettingsApplier.setArrayAdapter(CalcActivity_science.this, spinner_Base, MathEvaluator.int_digit_alphabet_groups, SettingsApplier.getColor_fops(CalcActivity_science.this));

            spinner_Base.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    int new_btn_digit_group_cnt = ((i % (MathEvaluator.digit_alphabet.length / 9)));
                    btn_digit_group_cnt = new_btn_digit_group_cnt;

                    assignModeFct();
                    Toast.makeText(CalcActivity_science.this, "bs new base sel: " + btn_digit_group_cnt, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }


    }
}
