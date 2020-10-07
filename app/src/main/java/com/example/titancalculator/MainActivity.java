package com.example.titancalculator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.helper.ArrayUtils;
import com.example.titancalculator.helper.MainDisplay.SettingsApplier;
import com.example.titancalculator.helper.Math_String.NavigatableString;
import com.example.titancalculator.helper.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    //auxiliary variables
    int btn_digit_group_cnt = 0;
    private static int base = 0;
    String state_spinner_shift = "number_selection"; //number_selection, base_selection
    private static String[] MEMORY = new String[6];
    boolean eT_eingabe_hasFocus = true;
    String mode = "BASIC"; String englishMode = "BASIC";

    //setting variables
    String current_Callback = "";
    String answer = "";
    boolean solve_inst_pref = false;
    public static Set<String> noImmidiateOps = new HashSet<>(Arrays.asList("³√", "ROOT", "√", "LOG", "P", "C", "%"));
    String language = "";
    boolean scientificNotation = false;

    Toolbar toolbar;
    //VIEWS
    LinearLayout science_background;
    LinearLayout display;
    //L1
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
    EditText eT_eingabe;
    EditText eT_ausgabe;
    NavigatableString I;
    LinearLayout LN2;
    LinearLayout LN3;
    LinearLayout LN4;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modes, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
        mode = item.toString();
        englishMode = returnEnglishMode(mode);
        assignModeFct();
        toolbar.setTitle(mode);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SettingsApplier.applySettings(MainActivity.this);
        setTitle("Calculator");
        SettingsApplier.setColors(MainActivity.this);
        eT_ausgabe.setOnFocusChangeListener(focusListener);
        eT_eingabe.setOnFocusChangeListener(focusListener);
        try {
            SettingsApplier.setBackgroundImage(MainActivity.this, science_background);
        } catch (Exception e) {
            e.printStackTrace();
        }
        eingabeAddText(current_Callback);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SettingsApplier.applySettings(MainActivity.this);
        setContentView(R.layout.activity_main);
        setTitle("Calculator");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mode = "BASIC";
        MEMORY = new String[6];
        MEMORY = loadMemory(MainActivity.this);
        //setzt hintergrundbild
        I = new NavigatableString("content");


        science_background = findViewById(R.id.science_background);
        eT_eingabe = findViewById(R.id.eT_eingabe);
        eT_ausgabe = findViewById(R.id.eT_ausgabe);
        display = findViewById(R.id.display);
        science_background = findViewById(R.id.science_background);
        //L1
        btn_clear = findViewById(R.id.btn_clear);
        btn_clearall = findViewById(R.id.btn_clearall);
        //L2 normal: PI,E,->DEC,->BIN,->OCT
        //TRIGO: SIN,COS,TAN,ASIN,ACOS,ATAN
        btn_11 = findViewById(R.id.btn_11);
        btn_12 = findViewById(R.id.btn_12);
        btn_13 = findViewById(R.id.btn_13);
        btn_14 = findViewById(R.id.btn_14);
        btn_15 = findViewById(R.id.btn_15);
        btn_16 = findViewById(R.id.btn_16);
        //L3 normal: %,!,^,a/b,x^-1,+/-
        //TRIGO: ASINH,ACOSH,ATANH,SINH,COSH,TANH
        btn_21 = findViewById(R.id.btn_21);
        btn_22 = findViewById(R.id.btn_22);
        btn_23 = findViewById(R.id.btn_23);
        btn_24 = findViewById(R.id.btn_24);
        btn_25 = findViewById(R.id.btn_25);
        btn_26 = findViewById(R.id.btn_26);
        //L5
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

        eingabeSetText("");
        eT_ausgabe.setOnFocusChangeListener(focusListener);
        eT_eingabe.setOnFocusChangeListener(focusListener);
        disableSoftInputFromAppearing(eT_eingabe);
        disableSoftInputFromAppearing(eT_ausgabe);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.modes_DE, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeClear();
                ausgabeSetText("");
                if (!noImmidiateOps.contains(I.getDisplayableString().trim())) {
                    if (solve_inst_pref) {
                        answer = I.getResult(getBase(MainActivity.this));
                        if (!answer.equals("Math Error")) ausgabeSetText(answer);
                    }
                }
            }
        });
        btn_clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eT_eingabe.setText("");
                eT_ausgabe.setText("");
                I.setText(eT_eingabe.getText().toString());
            }
        });
        //L2
        btn_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(englishMode){
                    case "BASIC": {eingabeAddText("π"); break;}
                    case "BASIC2": {ausgabeSetText(I.getPFZ(getBase(MainActivity.this))); break;}
                    case "TRIGO":  {eingabeAddText("SIN"); break;}
                    case "USER":  {transBtnFct("btn_11"); break;}
                    case "STATISTIC":  {eingabeAddText("Zn()"); break;}
                    case "HYPER":  {eingabeAddText("SINH"); break;}
                    case "LOGIC":  {eingabeAddText("AND(,)"); break;}
                    case "MEMORY":  {MEMORY[0] = getSelection(); saveMemory(MainActivity.this, MEMORY); break;}
                    default:  {Toast.makeText(MainActivity.this, "Unknown Mode: ", Toast.LENGTH_LONG).show(); break;}
                }
            }
        });
        btn_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(englishMode){
                    case "BASIC": {eingabeAddText("e"); break;}
                    case "BASIC2": {
                        if (language.equals("german") || language.equals("deutsch")) {
                            eingabeAddText("GGT(,)");
                        } else {
                            eingabeAddText("GCD(,)");
                        }   break;}
                    case "TRIGO":  {eingabeAddText("COS"); break;}
                    case "USER":  {transBtnFct("btn_12"); break;}
                    case "STATISTIC":  {eingabeAddText("Zb(,)"); break;}
                    case "HYPER":  {eingabeAddText("COSH"); break;}
                    case "LOGIC":  {eingabeAddText("OR(,)"); break;}
                    case "MEMORY":  {MEMORY[1] = getSelection(); saveMemory(MainActivity.this, MEMORY);break;}
                    default:  {Toast.makeText(MainActivity.this, "Unknown Mode: ", Toast.LENGTH_LONG).show(); break;}
                }
            }
        });
        btn_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(englishMode){
                    case "BASIC": {eingabeAddText("^");break;}
                    case "BASIC2": {if (language.equals("german") || language.equals("deutsch")) {
                                        eingabeAddText("KGV(,)");
                                    } else {
                                        eingabeAddText("LCM(,)");
                                    } break;}
                    case "TRIGO":  {eingabeAddText("TAN"); break;}
                    case "USER":  {transBtnFct("btn_13"); break;}
                    case "STATISTIC":  {eingabeAddText("C"); break;}
                    case "HYPER":  {eingabeAddText("TANH"); break;}
                    case "LOGIC":  {eingabeAddText("XOR(,)"); break;}
                    case "MEMORY":  {MEMORY[2] = getSelection(); saveMemory(MainActivity.this, MEMORY);break;}
                    default:  {Toast.makeText(MainActivity.this, "Unknown Mode: ", Toast.LENGTH_LONG).show(); break;}
                }
            }
        });
        btn_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(englishMode){
                    case "BASIC": {eingabeAddText("LOG"); break;}
                    case "BASIC2": {eingabeAddText(getResources().getString(R.string.SUME) + "(,)"); break;}
                    case "TRIGO":  {eingabeAddText("COT"); break;}
                    case "USER":  {transBtnFct("btn_14"); break;}
                    case "STATISTIC":  {eingabeAddText("P"); break;}
                    case "HYPER":  {eingabeAddText("ASINH"); break;}
                    case "LOGIC":  {eingabeAddText("NOT()"); break;}
                    case "MEMORY":  {MEMORY[3] = getSelection(); saveMemory(MainActivity.this, MEMORY);break;}
                    default:  {Toast.makeText(MainActivity.this, "Unknown Mode: ", Toast.LENGTH_LONG).show(); break;}
                }
            }
        });
        btn_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(englishMode){
                    case "BASIC": {eingabeAddText("LN"); break;}
                    case "BASIC2": {eingabeAddText(getResources().getString(R.string.MULP) + "(,)"); break;}
                    case "TRIGO":  {eingabeAddText("ASIN"); break;}
                    case "USER":  {transBtnFct("btn_15"); break;}
                    case "STATISTIC":  {eingabeAddText("MEAN()"); break;}
                    case "HYPER":  {eingabeAddText("ACOSH"); break;}
                    case "LOGIC":  {ausgabeSetText(I.getBIN(getBase(MainActivity.this))); break;}
                    case "MEMORY":  {MEMORY[4] = getSelection(); saveMemory(MainActivity.this, MEMORY);break;}
                    default:  {Toast.makeText(MainActivity.this, "Unknown Mode: ", Toast.LENGTH_LONG).show(); break;}
                }
            }
        });
        btn_16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(englishMode){
                    case "BASIC": {eingabeAddText("LB"); break;}
                    case "BASIC2": {break;}
                    case "TRIGO":  {eingabeAddText("ACOS"); break;}
                    case "USER":  {transBtnFct("btn_16"); break;}
                    case "STATISTIC":  {eingabeAddText("VAR()"); break;}
                    case "HYPER":  {eingabeAddText("ATANH"); break;}
                    case "LOGIC":  {ausgabeSetText(I.getOCT(getBase(MainActivity.this))); break;}
                    case "MEMORY":  {MEMORY[5] = getSelection(); saveMemory(MainActivity.this, MEMORY);break;}
                    default:  {Toast.makeText(MainActivity.this, "Unknown Mode: ", Toast.LENGTH_LONG).show(); break;}
                }
            }
        });
        //L3
        btn_21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(englishMode){
                    case "BASIC": {eingabeAddText("³√"); break;}
                    case "BASIC2": {ausgabeSetText(I.getPercent(getBase(MainActivity.this)));
                                    answer = I.getPercent(getBase(MainActivity.this));
                                    ausgabeSetText(answer); break;}
                    case "TRIGO":  {eingabeAddText("ATAN"); break;}
                    case "USER":  {transBtnFct("btn_21"); break;}
                    case "STATISTIC":  {eingabeAddText("E()"); break;}
                    case "HYPER":  {break;}
                    case "LOGIC":  {ausgabeSetText(I.getDEC(getBase(MainActivity.this))); break;}
                    case "MEMORY":  {replaceSelection(MEMORY[0]); break;}
                    default:  {Toast.makeText(MainActivity.this, "Unknown Mode: ", Toast.LENGTH_LONG).show(); break;}
                }
            }
        });
        btn_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(englishMode){
                    case "BASIC": {eingabeAddText("√"); break;}
                    case "BASIC2": {ausgabeSetText(I.getBruch(getBase(MainActivity.this))); break;}
                    case "TRIGO":  {eingabeAddText("ACOT"); break;}
                    case "USER":  {transBtnFct("btn_22"); break;}
                    case "STATISTIC":  {eingabeAddText("2√(VAR())"); break;}
                    case "HYPER":  {break;}
                    case "LOGIC":  {ausgabeSetText(I.getHEX(getBase(MainActivity.this))); break;}
                    case "MEMORY":  {replaceSelection(MEMORY[1]); break;}
                    default:  {Toast.makeText(MainActivity.this, "Unknown Mode: ", Toast.LENGTH_LONG).show(); break;}
                }
            }
        });
        btn_23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(englishMode){
                    case "BASIC": {eingabeAddText("³"); break;}
                    case "BASIC2": {ausgabeSetText(I.getReciproke(getBase(MainActivity.this))); break;}
                    case "TRIGO":  {ausgabeSetText(I.getDEG(getBase(MainActivity.this))); break;}
                    case "USER":  {transBtnFct("btn_23"); break;}
                    case "STATISTIC":  { break;}
                    case "HYPER":  {break;}
                    case "LOGIC":  {break;}
                    case "MEMORY":  {replaceSelection(MEMORY[2]); break;}
                    default:  {Toast.makeText(MainActivity.this, "Unknown Mode: ", Toast.LENGTH_LONG).show(); break;}
                }
            }
        });
        btn_24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(englishMode){
                    case "BASIC": {eingabeAddText("²"); break;}
                    case "BASIC2": {ausgabeSetText(I.getInvert(getBase(MainActivity.this))); break;}
                    case "TRIGO":  {ausgabeSetText(I.getRAD(getBase(MainActivity.this))); break;}
                    case "USER":  {transBtnFct("btn_24"); break;}
                    case "STATISTIC":  { break;}
                    case "HYPER":  {break;}
                    case "LOGIC":  {break;}
                    case "MEMORY":  {replaceSelection(MEMORY[3]); break;}
                    default:  {Toast.makeText(MainActivity.this, "Unknown Mode: ", Toast.LENGTH_LONG).show(); break;}
                }
            }
        });
        btn_25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(englishMode){
                    case "BASIC": {eingabeAddText("10^"); break;}
                    case "BASIC2": {eingabeAddText("MIN()"); break;}
                    case "TRIGO":  {eingabeAddText("toPolar(,)"); break;}
                    case "USER":  {transBtnFct("btn_25"); break;}
                    case "STATISTIC":  { break;}
                    case "HYPER":  {break;}
                    case "LOGIC":  {break;}
                    case "MEMORY":  {replaceSelection(MEMORY[4]); break;}
                    default:  {Toast.makeText(MainActivity.this, "Unknown Mode: ", Toast.LENGTH_LONG).show(); break;}
                }
            }
        });
        btn_26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(englishMode){
                    case "BASIC": {eingabeAddText("!"); break;}
                    case "BASIC2": {eingabeAddText("MAX()"); break;}
                    case "TRIGO":  {eingabeAddText("toCart(,)"); break;}
                    case "USER":  {transBtnFct("btn_26"); break;}
                    case "STATISTIC":  { break;}
                    case "HYPER":  {break;}
                    case "LOGIC":  {break;}
                    case "MEMORY":  {replaceSelection(MEMORY[5]); break;}
                    default:  {Toast.makeText(MainActivity.this, "Unknown Mode: ", Toast.LENGTH_LONG).show(); break;}
                }
            }
        });
        btn_LINKS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eT_eingabe.setSelection(Math.max(0, eT_eingabe.getSelectionStart() - 1));
            }
        });
        btn_RECHTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eT_eingabe.setSelection(Math.min(eT_eingabe.length(), eT_eingabe.getSelectionStart() + 1));
            }
        });
        //G1
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("1");
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("2");
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("3");
            }
        });
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("4");
            }
        });
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("5");
            }
        });
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("6");
            }
        });
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("7");
            }
        });
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("8");
            }
        });
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("9");
            }
        });
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("0");
            }
        });
        btn_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText(".");

            }
        });
        btn_sep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText(",");
            }
        });
        btn_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,answer,Toast.LENGTH_SHORT).show();
                eingabeAddText("ANS");
            }
        });
        //G2
        btn_open_bracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("(");
            }
        });
        btn_close_bracket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText(")");
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("+");
            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("-");
            }
        });
        btn_mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("*");
            }
        });
        btn_div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eingabeAddText("/");
            }
        });
        btn_eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!eT_eingabe.getText().toString().equals(I.getDisplayableString())) {
                    I.setText(eT_eingabe.getText().toString());
                }
                answer = I.getResult(getBase(MainActivity.this));
                if(scientificNotation){
                    answer = I.normalToScientific(getBase(MainActivity.this));
                } else {
                    answer = I.scientificToNormal(getBase(MainActivity.this));
                }
                ausgabeSetText(answer);
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
    }

    private void ausgabeSetText(String res) {
        eT_ausgabe.setText(res);
    }

    private void toogleScientificNotation(){
        scientificNotation = !scientificNotation;
    }

    private void assignModeFct() {
        if (mode.equals(getResources().getString(R.string.TRIGO_DE)) || mode.equals(getResources().getString(R.string.TRIGO_EN))) {
            btn_11.setText("SIN"); btn_12.setText("COS"); btn_13.setText("TAN"); btn_14.setText("COT"); btn_15.setText("ASIN"); btn_16.setText("ACOS");
            btn_21.setText("ATAN"); btn_22.setText("ACOT"); btn_23.setText(">DEG"); btn_24.setText(">RAD"); btn_25.setText(">Polar"); btn_26.setText(">Cart");
        }
        if (mode.equals("HYPER")) {
            btn_11.setText("SINH"); btn_12.setText("COSH"); btn_13.setText("TANH"); btn_14.setText("ASINH"); btn_15.setText("ASINH"); btn_16.setText("ASINH");
            btn_21.setText("");  btn_22.setText("");  btn_23.setText("");  btn_24.setText("");  btn_25.setText("");  btn_26.setText("");
        } else if (mode.equals(getResources().getString(R.string.STATS_EN)) || mode.equals(getResources().getString(R.string.STATS_DE))) {
            btn_11.setText("ZN(N)"); btn_12.setText("ZB(X;Y)"); btn_13.setText("NCR"); btn_14.setText("NPR"); btn_15.setText("MEAN"); btn_16.setText("VAR");
            btn_21.setText("E"); btn_22.setText("S"); btn_23.setText(""); btn_24.setText(""); btn_25.setText(""); btn_26.setText("");
        } else if (mode.equals("LOGIC") || mode.equals("LOGISCH")) {
            btn_11.setText("AND"); btn_12.setText("OR"); btn_13.setText("XOR"); btn_14.setText("NOT"); btn_15.setText(">BIN"); btn_16.setText(">OCT");
            btn_21.setText(">DEC"); btn_22.setText(">HEX"); btn_23.setText(""); btn_24.setText(""); btn_25.setText(""); btn_26.setText("");
        } else if (mode.equals("MEMORY") || mode.equals("SPEICHER")) {
            btn_11.setText("M1"); btn_12.setText("M2"); btn_13.setText("M3"); btn_14.setText("M4"); btn_15.setText("M5"); btn_16.setText("M6");
            btn_21.setText(">M1"); btn_22.setText(">M2"); btn_23.setText(">M3"); btn_24.setText(">M4"); btn_25.setText(">M5"); btn_26.setText(">M6");
        } else if (mode.equals(getResources().getString(R.string.BASIC_DE)) || mode.equals(getResources().getString(R.string.BASIC_EN))) {
            btn_11.setText(R.string.PI); btn_12.setText(R.string.E); btn_13.setText("^"); btn_14.setText("LOG"); btn_15.setText("LN"); btn_16.setText("LB");
            btn_21.setText("³√"); btn_22.setText("√"); btn_23.setText("x³"); btn_24.setText("x²"); btn_25.setText("10^x"); btn_26.setText("!");
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
            btn_14.setText(getResources().getString(R.string.SUME)); btn_15.setText(getResources().getString(R.string.MULP)); btn_16.setText("");
            //L3 normal: %,!,^,a/b,x^-1,+/-
            btn_21.setText(">%"); btn_22.setText("A/B"); btn_23.setText(R.string.x_h_one); btn_24.setText("+/-"); btn_25.setText("MIN"); btn_26.setText("MAX");
        }
        if (mode.equals("PROGRAM") || mode.equals("PROGRAMM")) {
            btn_11.setText("P1"); btn_12.setText("P2"); btn_13.setText("P3"); btn_14.setText("P4"); btn_15.setText("P5"); btn_16.setText("P6");
            btn_21.setText(">P1"); btn_22.setText(">P1"); btn_23.setText(">P3"); btn_24.setText(">P4"); btn_25.setText(">P5"); btn_26.setText(">P6");
        }
    }

    private void transBtnFct(String fct) {
        if (fct.startsWith("btn")) return;
        //"PI","E","NCR","NPR","%","!N","^","A/B","x\u207B\u00B9","+/-","√","\u00B3√","LOG","LN","LB","SIN","COS","TAN","ASIN","ATAN","ASINH","ACOSH","ATANH","SINH","COSH","TANH"};
        if (fct.equals(">%")) {
            ausgabeSetText(I.getPercent(getBase(MainActivity.this)));
            return;
        } else if (fct.equals("A/B")) {
            ausgabeSetText(I.getBruch(getBase(MainActivity.this)));
            return;
        } else if (fct.equals("x\u207B\u00B9")) {
            ausgabeSetText(I.getReciproke(getBase(MainActivity.this)));
            return;
        } else if (fct.equals("+/-")) {
            ausgabeSetText(I.getInvert(getBase(MainActivity.this)));
            return;
        }
        String A = fct;
        A = A.replace("NCR", "C");
        A = A.replace("NCR", "C");
        A = A.replace("!N", "!");
        A = A.replace("x\u207B\u00B9", "C");
        eingabeAddText(A);
    }

    private void replaceSelection(String input) {
        if (input == null || input.isEmpty()) return;
        int selStart = -1;
        int selEnd = -1;
        if (eT_eingabe.hasFocus()) {
            selStart = eT_eingabe.getSelectionStart();
            selEnd = eT_eingabe.getSelectionEnd();
            if (selStart >= 0 && selEnd >= 0 && selStart <= selEnd && selStart <= eT_eingabe.length() && selEnd <= eT_eingabe.length()) {
                String etE_text = eT_eingabe.getText().toString();
                etE_text = StringUtils.replace(etE_text, input, selStart, selEnd);

                Toast.makeText(MainActivity.this, "selection: " + etE_text.toString(), Toast.LENGTH_LONG).show();
                eT_eingabe.setText(etE_text);
            } else {
                eT_eingabe.setText(input);
            }
            eT_eingabe.setSelection(selEnd);
            I.setText(eT_eingabe.getText().toString());
        }
    }


    private String getSelection() {
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

    private void eingabeAddText(String i) {
        if (eT_eingabe_hasFocus) {
            eT_eingabe.clearFocus();
        }
        eT_eingabe.getText().insert(eT_eingabe.getSelectionStart(), i);
        I.setText(eT_eingabe.getText().toString());
        if (solve_inst_pref) {
            if (!noImmidiateOps.contains(i.trim())) {
                answer = I.getResult(getBase(MainActivity.this));
                if (!answer.equals("Math Error")) ausgabeSetText(answer);
            }
        }
    }

    private void eingabeClear() {
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

    private void eingabeSetText(String i) {
        if (eT_eingabe_hasFocus) {
            eT_eingabe.setText(i);
            I.setText(eT_eingabe.getText().toString());
        }
    }

    private static void setBase(Context context, int Base) {
        if (Base <= 1) {
            return;
        } else {
            base = Base;
        }
    }

    private static int getBase(Context context) {
        if (base == 0) {
            String baseString = PreferenceManager.getDefaultSharedPreferences(context).getString("base", "10");
            if (baseString == null) {
                setBase(context, 0);
            } else base = Integer.parseInt(baseString);
        }
        return base;
    }

    private static boolean checkPermissionForReadExtertalStorage(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    private static void requestPermissionForReadExtertalStorage(Context context) throws Exception {
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

    private static void saveMemory(Context c, String[] Memory) {
        String MEMS = ArrayUtils.arrayToString(Memory);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferences.edit();
        Toast.makeText(c, "save: " + MEMS, Toast.LENGTH_SHORT).show();
        editor.putString("MEMORY", MEMS);
        editor.commit();
    }

    private static String[] loadMemory(Context c) {
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

    private static String returnEnglishMode(String mode){
        switch(mode){
            case "STANDART": {return "BASIC";}
            case "STANDART2": {return "BASIC2";}
            case "STATISTIK": {return "STATISTIC";}
            case "NUTZER": {return "USER";}
            case "LOGISCH": {return "LOGIC";}
            case "SPEICHER": {return "MEMORY";}
            default: return mode;
        }
    }

    /**
     * Disable soft keyboard from appearing, use in conjunction with android:windowSoftInputMode="stateAlwaysHidden|adjustNothing"
     * @param editText
     */
    public static void disableSoftInputFromAppearing(EditText editText) {
        if (Build.VERSION.SDK_INT >= 11) {
            editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTextIsSelectable(true);
            editText.setShowSoftInputOnFocus(false);
        } else {
            editText.setRawInputType(InputType.TYPE_NULL);
            editText.setFocusable(true);
        }
    }
}
