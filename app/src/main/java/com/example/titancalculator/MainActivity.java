package com.example.titancalculator;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DrawFilter;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.titancalculator.geplanteFeatures.helper.MainDisplay.SettingsApplier;
import com.example.titancalculator.helper.Math_String.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.*;

/** View implemented by Activity, will contain a reference to the presenter.
  * The only thing that the view will do is to call a method from the Presenter every time there is an interface action.
  */
public class MainActivity extends AppCompatActivity implements Presenter.View {
    Presenter presenter;
    //auxiliary variables
    boolean eT_eingabe_hasFocus = true;

    //VIEWS
    Toolbar toolbar;
    LinearLayout science_background;
    LinearLayout display;
    //Line 1
    Button btn_FUN; Button btn_clear; Button btn_clearall;
    //Line 2
    Button btn_11; Button btn_12;Button btn_13;Button btn_14;Button btn_15;Button btn_16;
    //Line 3
    Button btn_21; Button btn_22; Button btn_23; Button btn_24; Button btn_25; Button btn_26;
    //Line 5
    Button btn_LINKS; Button btn_RECHTS; Spinner spinner_Base;
    //Group 1
    Button btn_1; Button btn_2; Button btn_3; Button btn_4; Button btn_5; Button btn_6; Button btn_7; Button btn_8; Button btn_9; Button btn_0;
    //Group 2
    Button btn_open_bracket; Button btn_close_bracket; Button btn_add; Button btn_sub; Button btn_mul; Button btn_div; Button btn_com; Button btn_sep; Button btn_ans; Button btn_eq;
    EditText eT_eingabe; EditText eT_ausgabe;
    LinearLayout LN2; LinearLayout LN3; LinearLayout LN4;

    @Override public boolean onCreateOptionsMenu(Menu menu) {getMenuInflater().inflate(R.menu.menu_modes, menu); return true;}

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        String idName = getResources().getResourceEntryName(item.getItemId());
        presenter.setMode(idName);
        presenter.assignModeFct();
        toolbar.setTitle(item.toString());
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
        //presenter.eingabeAddText(current_Callback); TODO
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new Presenter(this,this);
        SettingsApplier.applySettings(MainActivity.this);
        setContentView(R.layout.activity_main);
        setTitle("Calculator");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter.setMode("basic");
        //setzt hintergrundbild

        science_background = findViewById(R.id.science_background);
        eT_eingabe = findViewById(R.id.eT_eingabe);
        eT_ausgabe = findViewById(R.id.eT_ausgabe);
        display = findViewById(R.id.display);
        science_background = findViewById(R.id.science_background);
        //L1
        btn_FUN = findViewById(R.id.btn_FUN); btn_clear = findViewById(R.id.btn_clear); btn_clearall = findViewById(R.id.btn_clearall);
        //L2
        btn_11 = findViewById(R.id.btn_11);  btn_12 = findViewById(R.id.btn_12);  btn_13 = findViewById(R.id.btn_13);  btn_14 = findViewById(R.id.btn_14);  btn_15 = findViewById(R.id.btn_15);  btn_16 = findViewById(R.id.btn_16);
        //L3
        btn_21 = findViewById(R.id.btn_21);  btn_22 = findViewById(R.id.btn_22);  btn_23 = findViewById(R.id.btn_23);  btn_24 = findViewById(R.id.btn_24);  btn_25 = findViewById(R.id.btn_25);  btn_26 = findViewById(R.id.btn_26);
        //L5
        btn_LINKS = findViewById(R.id.btn_LINKS);  btn_RECHTS = findViewById(R.id.btn_RECHTS);  spinner_Base = findViewById(R.id.spinner_Base);
        //G1
        btn_1 = findViewById(R.id.btn_1);  btn_2 = findViewById(R.id.btn_2);  btn_3 = findViewById(R.id.btn_3);  btn_4 = findViewById(R.id.btn_4);  btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);  btn_7 = findViewById(R.id.btn_7);  btn_8 = findViewById(R.id.btn_8);  btn_9 = findViewById(R.id.btn_9);  btn_0 = findViewById(R.id.btn_0);
        btn_com = findViewById(R.id.btn_com);  btn_sep = findViewById(R.id.btn_sep);  btn_ans = findViewById(R.id.btn_ANS);
        //G2
        btn_open_bracket = findViewById(R.id.btn_open_bracket);  btn_close_bracket = findViewById(R.id.btn_close_bracket);
        btn_add = findViewById(R.id.btn_add);  btn_sub = findViewById(R.id.btn_sub);  btn_mul = findViewById(R.id.btn_mul);  btn_div = findViewById(R.id.btn_div);  btn_eq = findViewById(R.id.btn_eq);
        LN2 = findViewById(R.id.LN2);  LN3 = findViewById(R.id.LN3);  LN4 = findViewById(R.id.LN4);

        eT_ausgabe.setOnFocusChangeListener(focusListener);
        eT_eingabe.setOnFocusChangeListener(focusListener);
        disableSoftInputFromAppearing(eT_eingabe);
        disableSoftInputFromAppearing(eT_ausgabe);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.modes_EN, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Button[] allButtons = {btn_11,btn_12,btn_13,btn_14,btn_15,btn_16,btn_21,btn_22,btn_23,btn_24,btn_25,btn_26,btn_FUN,btn_clear,btn_clearall,btn_LINKS,btn_RECHTS,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_open_bracket,btn_close_bracket,btn_add,btn_sub,btn_mul,btn_div,btn_com,btn_sep,btn_ans,btn_eq};
        final Map<Button, Drawable> backgrounds = new HashMap<>();
        for(Button btn: allButtons){
           // Log.d("",getResources().getResourceEntryName(btn.getId()));
            btn.setOnTouchListener(new View.OnTouchListener() {
                @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        ((Button)view).setTypeface(null, Typeface.BOLD);
                    }
                    else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
                        ((Button)view).setTypeface(null, Typeface.NORMAL);
                    }
                    return false;
                }
            });
        }

        btn_FUN.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if(LN2.getVisibility() == View.VISIBLE){
                    LN2.setVisibility(View.GONE);
                    LN3.setVisibility(View.GONE);
                } else {
                    LN2.setVisibility(View.VISIBLE);
                    LN3.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputClearOne();
            }
        });
        btn_clearall.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
               presenter.inputClearAll();
            }
        });
        //L2
        btn_11.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("11");
            }
        });
        btn_12.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
               presenter.inputBtn("12");
            }
        });
        btn_13.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("13");
            }
        });
        btn_14.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("14");
            }
        });
        btn_15.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("15");
            }
        });
        btn_16.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("16");
            }
        });
        //L3
        btn_21.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("21");
            }
        });
        btn_22.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("22");
            }
        });
        btn_23.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("23");
            }
        });
        btn_24.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("24");
            }
        });
        btn_25.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("25");
            }
        });
        btn_26.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("26");
            }
        });
        btn_LINKS.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                eT_eingabe.setSelection(Math.max(0, eT_eingabe.getSelectionStart() - 1));
            }
        });
        btn_RECHTS.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                eT_eingabe.setSelection(Math.min(eT_eingabe.length(), eT_eingabe.getSelectionStart() + 1));
            }
        });
        //G1
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("1");
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("2");
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("3");
            }
        });
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("4");
            }
        });
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("5");
            }
        });
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("6");
            }
        });
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("7");
            }
        });
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("8");
            }
        });
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("9");
            }
        });
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("0");
            }
        });
        btn_com.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn(".");

            }
        });
        btn_sep.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn(",");
            }
        });
        btn_ans.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("ANS");
            }
        });
        //G2
        btn_open_bracket.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("(");
            }
        });
        btn_close_bracket.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {presenter.inputBtn(")");
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("+");
            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("-");
            }
        });
        btn_mul.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("*");
            }
        });
        btn_div.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputBtn("/");
            }
        });
        btn_eq.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputEqual();
            }
        });
        btn_eq.setLongClickable(true);
        btn_eq.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {
                presenter.toogleScientificNotation();
                return false;
            }
        });
    }

    /* fullscreen
    @Override public void onWindowFocusChanged(boolean hasFocus) {
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
     */



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

    @Override public void setBtn11Text(String text) { btn_11.setText(text);}
    @Override public void setBtn12Text(String text) { btn_12.setText(text);}
    @Override public void setBtn13Text(String text) { btn_13.setText(text);}
    @Override public void setBtn14Text(String text) { btn_14.setText(text);}
    @Override public void setBtn15Text(String text) { btn_15.setText(text);}
    @Override public void setBtn16Text(String text) { btn_16.setText(text);}
    @Override public void setBtn21Text(String text) { btn_21.setText(text);}
    @Override public void setBtn22Text(String text) { btn_22.setText(text);}
    @Override public void setBtn23Text(String text) { btn_23.setText(text);}
    @Override public void setBtn24Text(String text) { btn_24.setText(text);}
    @Override public void setBtn25Text(String text) { btn_25.setText(text);}
    @Override public void setBtn26Text(String text) { btn_26.setText(text);}
    @Override public String getBtnText(int i) {
        switch (i) {
            case 11:{return btn_11.getText().toString();}
            case 12:{return btn_12.getText().toString();}
            case 13:{return btn_13.getText().toString();}
            case 14:{return btn_14.getText().toString();}
            case 15:{return btn_15.getText().toString();}
            case 16:{return btn_16.getText().toString();}
            case 21:{return btn_21.getText().toString();}
            case 22:{return btn_22.getText().toString();}
            case 23:{return btn_23.getText().toString();}
            case 24:{return btn_24.getText().toString();}
            case 25:{return btn_25.getText().toString();}
            case 26:{return btn_26.getText().toString();}
            default:{System.out.println("wrong i:"+i);assert(false);return "";}
        }
    }



    @Override public void setSelectionEingabe(int selectionEingabe) {eT_eingabe.setSelection(selectionEingabe);}
    @Override public void setSelectionEingabe(int selectionEingabeStart, int selectionEingabeEnde) {eT_eingabe.setSelection(selectionEingabeStart, selectionEingabeEnde);}
    @Override public void setSelectionAusgabe(int selectionAusgabe) {eT_ausgabe.setSelection(selectionAusgabe);}
    @Override public void setSelectionAusgabe(int selectionAusgabeStart, int selectionAusgabeEnde) {eT_ausgabe.setSelection(selectionAusgabeStart, selectionAusgabeEnde);}
    @Override public void eingabeAddText(String i) {eT_eingabe.getText().insert(eT_eingabe.getSelectionStart(), i);}
    @Override public void eingabeSetText(String i) {eT_eingabe.setText(i);}
    @Override public String eingabeGetText() {
        return eT_eingabe.getText().toString();
    }
    @Override public void ausgabeSetText(String res) {
        eT_ausgabe.setText(res);
    }

    @Override public int getSelectionStartEingabe() {
        if(eT_eingabe.hasFocus()){return eT_eingabe.getSelectionStart();}
        else return -1;
    }

    @Override public int getSelectionEndEingabe() {
        if(eT_eingabe.hasFocus()){return eT_eingabe.getSelectionEnd();}
        else return -1;
    }

    @Override public int getSelectionStartAusgabe() {
        if(eT_ausgabe.hasFocus()){return eT_ausgabe.getSelectionStart();}
        else return -1;
    }

    @Override public int getSelectionEndAusgabe() {
        if(eT_ausgabe.hasFocus()){return eT_ausgabe.getSelectionEnd();}
        else return -1;
    }

    public void eingabeClearOne() {
        int pos = eT_eingabe.getSelectionStart();
        //altered
            if(pos != -1 && pos > 0) eT_eingabe.setText(eT_eingabe.getText().toString().substring(0,pos-1));
            else {eT_eingabe.setText(eT_eingabe.getText().toString().substring(0,Math.max(0,eT_eingabe.getText().toString().length()-2)));}
            eT_eingabe.clearFocus();
        //original//TODO: test and delete
            //eingabeSetText("");
            //eT_eingabe.setSelection(Math.max(0, pos - 1));
    }

    public void eingabeClearAll() {
        eT_eingabe.setText(""); eT_eingabe.clearFocus();
        eT_ausgabe.setText(""); eT_ausgabe.clearFocus();
    }


    @Override public void replaceSelection(String input) {
        if (input == null || input.isEmpty()) return;
        int selStart = eT_eingabe.getSelectionStart();
        int selEnd = eT_eingabe.getSelectionEnd();
        if (eT_eingabe.hasFocus()) {
            if (selStart >= 0 && selEnd >= 0 && selStart <= selEnd && selStart <= eT_eingabe.length() && selEnd <= eT_eingabe.length()) {
                String etE_text = eT_eingabe.getText().toString();
                etE_text = StringUtils.replace(etE_text, input, selStart, selEnd);
                eT_eingabe.setText(etE_text);
            } else {
                eT_eingabe.setText(input);
            }
            eT_eingabe.setSelection(selEnd);
            presenter.setNavI(eT_eingabe.getText().toString());
        } else {
            eingabeAddText(input);
        }
    }

    @Override public String getSelection() {
        String selection = "";
        int selStart = -1;
        int selEnd = -1;
        if (eT_eingabe.hasFocus()) {
            selStart = eT_eingabe.getSelectionStart();
            selEnd = eT_eingabe.getSelectionEnd();
            if (selStart >= 0 && selEnd > 0 && selStart < selEnd) {
                selection = eT_eingabe.getText().toString().substring(selStart, selEnd);
            } else {
                selection = eT_eingabe.getText().toString();
            }
        } else if (eT_ausgabe.hasFocus()) {
            selStart = eT_ausgabe.getSelectionStart();
            selEnd = eT_ausgabe.getSelectionEnd();
            if (selStart >= 0 && selEnd > 0 && selStart < selEnd) {
                selection = eT_ausgabe.getText().toString().substring(selStart, selEnd);
            } else {
                selection = eT_ausgabe.getText().toString();
            }
        } else {
            selection = eT_eingabe.getText().toString();
        }
        //selection funzt net (siehe memory)
        return selection;
    }


}
