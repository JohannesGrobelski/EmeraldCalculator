package com.example.titancalculator;

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

import java.util.HashMap;
import java.util.Map;

/** View implemented by Activity, will contain a reference to the presenter.
  * The only thing that the view does is to call a method from the Presenter every time there is an interface action.
  */
public class MainActivity extends AppCompatActivity implements Presenter.View {
    private Presenter presenter;
    //auxiliary variables
    boolean eT_input_hasFocus = true;

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
    EditText eT_input; EditText eT_output;
    LinearLayout LN2; LinearLayout LN3; LinearLayout LN4;

    @Override public boolean onCreateOptionsMenu(Menu menu) {getMenuInflater().inflate(R.menu.menu_modes, menu); return true;}

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        String idName = getResources().getResourceEntryName(item.getItemId());
        presenter.setMode(idName);
        setViewsAccordingToMode(item.toString());
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        SettingsApplier.applySettings(MainActivity.this);
        setTitle("Calculator");
        SettingsApplier.setColors(MainActivity.this);
        eT_output.setOnFocusChangeListener(focusListener);
        eT_input.setOnFocusChangeListener(focusListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new Presenter(this);
        SettingsApplier.applySettings(MainActivity.this);
        setContentView(R.layout.activity_main);
        setTitle("Calculator");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        science_background = findViewById(R.id.science_background);
        eT_input = findViewById(R.id.eT_input);
        eT_output = findViewById(R.id.eT_output);
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

        eT_output.setOnFocusChangeListener(focusListener);
        eT_input.setOnFocusChangeListener(focusListener);
        disableSoftInputFromAppearing(eT_input);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.modes_EN, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        presenter.setMode("basic");

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
        setOnClickListeners();

    }

    public void setOnClickListeners(){
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
                presenter.inputButton("⌫");
            }
        });
        btn_clearall.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("⌧");
            }
        });
        //L2
        btn_11.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("11");
            }
        });
        btn_12.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("12");
            }
        });
        btn_13.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("13");
            }
        });
        btn_14.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("14");
            }
        });
        btn_15.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("15");
            }
        });
        btn_16.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("16");
            }
        });
        //L3
        btn_21.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("21");
            }
        });
        btn_22.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("22");
            }
        });
        btn_23.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("23");
            }
        });
        btn_24.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("24");
            }
        });
        btn_25.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("25");
            }
        });
        btn_26.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("26");
            }
        });
        btn_LINKS.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("L");
            }
        });
        btn_RECHTS.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("R");
            }
        });
        //G1
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("1");
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("2");
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("3");
            }
        });
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("4");
            }
        });
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("5");
            }
        });
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("6");
            }
        });
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("7");
            }
        });
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("8");
            }
        });
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("9");
            }
        });
        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("0");
            }
        });
        btn_com.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton(".");

            }
        });
        btn_sep.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton(",");
            }
        });
        btn_ans.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("ANS");
            }
        });
        //G2
        btn_open_bracket.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {presenter.inputButton("(");
            }
        });
        btn_close_bracket.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {presenter.inputButton(")");
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("+");
            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("-");
            }
        });
        btn_mul.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("*");
            }
        });
        btn_div.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("/");
            }
        });
        btn_eq.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("=");
            }
        });
        btn_eq.setLongClickable(true);
        btn_eq.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {presenter.inputButtonLongClick("=");return false;}
        });
    }

    public void setViewsAccordingToMode(String mode){
        btn_11.setText(presenter.getFunctionButtonText(11)); btn_12.setText(presenter.getFunctionButtonText(12)); btn_13.setText(presenter.getFunctionButtonText(13));
        btn_14.setText(presenter.getFunctionButtonText(14)); btn_15.setText(presenter.getFunctionButtonText(15)); btn_16.setText(presenter.getFunctionButtonText(16));
        btn_21.setText(presenter.getFunctionButtonText(21)); btn_22.setText(presenter.getFunctionButtonText(22)); btn_23.setText(presenter.getFunctionButtonText(23));
        btn_24.setText(presenter.getFunctionButtonText(24)); btn_25.setText(presenter.getFunctionButtonText(25)); btn_26.setText(presenter.getFunctionButtonText(26));
        toolbar.setTitle(mode);
    }

    @Override public void setBtnText(int index, String text){
        switch(index){
            case 11:{btn_11.setText(text); break;}
            case 12:{btn_12.setText(text); break;}
            case 13:{btn_13.setText(text); break;}
            case 14:{btn_14.setText(text); break;}
            case 15:{btn_15.setText(text); break;}
            case 16:{btn_16.setText(text); break;}
            case 21:{btn_21.setText(text); break;}
            case 22:{btn_22.setText(text); break;}
            case 23:{btn_23.setText(text); break;}
            case 24:{btn_24.setText(text); break;}
            case 25:{btn_25.setText(text); break;}
            case 26:{btn_26.setText(text); break;}
        }
    }
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

    @Override public void setSelectionInput(int selectionInput) {eT_input.setSelection(selectionInput);}
    @Override public void setSelectionInput(int selectionInputStart, int selectionInputEnd) {eT_input.setSelection(selectionInputStart, selectionInputEnd);}
    @Override public void setSelectionOutput(int selectionOutput) {eT_output.setSelection(selectionOutput);}
    @Override public void setSelectionOutput(int selectionOutputStart, int selectionOutputEnd) {eT_output.setSelection(selectionOutputStart, selectionOutputEnd);}

    @Override public void setInputText(String i) {eT_input.setText(i);}
    @Override public void setOutputText(String res) {
        eT_output.setText(res);
    }

    @Override public void clearFocusInput() {eT_input.clearFocus();}
    @Override public void clearFocusOutput() {eT_output.clearFocus();}
    @Override public boolean hasFocusInput() {return eT_input.hasFocus();}
    @Override public boolean hasFocusOutput() {return eT_output.hasFocus();}
    @Override public void requestFocusInput() {eT_input.requestFocus();}
    @Override public void requestFocusOutput() {eT_output.requestFocus();}

    @Override public String getInputText() {return eT_input.getText().toString();}
    @Override public String getOutputText() {
        return eT_input.getText().toString();
    }
    @Override public int getSelectionStartInput() {
        if(eT_input.hasFocus()){return eT_input.getSelectionStart();}
        else return -1;
    }
    @Override public int getSelectionEndInput() {
        if(eT_input.hasFocus()){return eT_input.getSelectionEnd();}
        else return -1;
    }
    @Override public int getSelectionStartOutput() {
        if(eT_output.hasFocus()){return eT_output.getSelectionStart();}
        else return -1;
    }
    @Override public int getSelectionEndOutput() {
        if(eT_output.hasFocus()){return eT_output.getSelectionEnd();}
        else return -1;
    }

    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                if (v.equals(eT_input)) {
                    eT_input_hasFocus = true;
                } else eT_input_hasFocus = false;
            } else {
                eT_input_hasFocus = false;
            }
        }
    };

    /**
     * get String of selection in et_input
     * @return
     */
    @Override public String getSelection() {
        String selection = "";
        int selStart = -1;
        int selEnd = -1;
        if (eT_input.hasFocus()) {
            selStart = eT_input.getSelectionStart();
            selEnd = eT_input.getSelectionEnd();
            if (selStart >= 0 && selEnd > 0 && selStart < selEnd) {
                selection = eT_input.getText().toString().substring(selStart, selEnd);
            } else {
                selection = eT_input.getText().toString();
            }
        } else if (eT_output.hasFocus()) {
            selStart = eT_output.getSelectionStart();
            selEnd = eT_output.getSelectionEnd();
            if (selStart >= 0 && selEnd > 0 && selStart < selEnd) {
                selection = eT_output.getText().toString().substring(selStart, selEnd);
            } else {
                selection = eT_output.getText().toString();
            }
        } else {
            selection = eT_input.getText().toString();
        }
        //selection funzt net (siehe memory)
        return selection;
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

}
