package com.example.calcitecalculator.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.calcitecalculator.presenter.Presenter;
import com.example.calcitecalculator.helper.OnSwipeTouchListener;
import com.example.calcitecalculator.model.CalcModel;
import com.example.emeraldcalculator.R;

import javax.inject.Inject;

/** View implemented by Activity, will contain a reference to the presenter.
  * The only thing that the view does is to call a method from the Presenter every time there is an interface action.
  */
public class MainActivity extends AppCompatActivity implements Presenter.View {
    private static Context context;
    private Presenter presenter;
    private static String[] menuItems;
    private static String[] menuItemsID;
    //auxiliary variables
    boolean eT_input_hasFocus = true;

    //VIEWS
    Toolbar toolbar; TextView toolbarTitle;
    LinearLayout science_background;
    LinearLayout display;
    //Line 1
    Button btn_clear_all; public Button btn_left; public Button btn_right;
    //Line 2
    Button btn_11; Button btn_12;Button btn_13;Button btn_14;Button btn_15;Button btn_16;
    //Line 3
    Button btn_21; Button btn_22; Button btn_23; Button btn_24; Button btn_25; Button btn_26;
    //Group 1
    Button btn_1; Button btn_2; Button btn_3; Button btn_4; Button btn_5; Button btn_6; Button btn_7; Button btn_8; Button btn_9; Button btn_0;
    //Group 2
    Button btn_open_bracket; Button btn_close_bracket; Button btn_add; Button btn_sub; Button btn_mul; Button btn_div; Button btn_com; Button btn_sep; Button btn_eq_ans;
    EditText eT_input; EditText eT_output;
    Button[] allButtons;

    public static Context getContext(){return context;}

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modes, menu);
        menuItems = new String[menu.size()];
        menuItemsID = new String[menu.size()];
        for(int i=0; i<menu.size(); i++){
            menuItems[i] = menu.getItem(i).toString();
            menuItemsID[i] = getResources().getResourceEntryName(menu.getItem(i).getItemId());
        }
        CalcModel.modes = menuItems;
        setViewsAccordingToMode(CalcModel.modes[presenter.getMode()]);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        String idName = getResources().getResourceEntryName(item.getItemId());
        int indexMenuItem = 0;
        for(int i=0; i<menuItems.length; i++){
            if(menuItemsID[i].equals(idName))indexMenuItem = i;
        }

        presenter.setMode(indexMenuItem);
        setViewsAccordingToMode(CalcModel.modes[presenter.getMode()]);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle("Calculator");
        eT_output.setOnFocusChangeListener(focusListener);
        eT_input.setOnFocusChangeListener(focusListener);
        context = this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Calculator");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        science_background = findViewById(R.id.science_background);
        eT_input = findViewById(R.id.eT_input);
        eT_output = findViewById(R.id.eT_output);
        display = findViewById(R.id.display);
        science_background = findViewById(R.id.science_background);
        //L1
        btn_clear_all = findViewById(R.id.btn_clear_all);
        //L2
        btn_11 = findViewById(R.id.btn_11);  btn_12 = findViewById(R.id.btn_12);  btn_13 = findViewById(R.id.btn_13);  btn_14 = findViewById(R.id.btn_14);  btn_15 = findViewById(R.id.btn_15);  btn_16 = findViewById(R.id.btn_16);
        //L3
        btn_21 = findViewById(R.id.btn_21);  btn_22 = findViewById(R.id.btn_22);  btn_23 = findViewById(R.id.btn_23);  btn_24 = findViewById(R.id.btn_24);  btn_25 = findViewById(R.id.btn_25);  btn_26 = findViewById(R.id.btn_26);
        //G1
        btn_1 = findViewById(R.id.btn_1);  btn_2 = findViewById(R.id.btn_2);  btn_3 = findViewById(R.id.btn_3);  btn_4 = findViewById(R.id.btn_4);  btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);  btn_7 = findViewById(R.id.btn_7);  btn_8 = findViewById(R.id.btn_8);  btn_9 = findViewById(R.id.btn_9);  btn_0 = findViewById(R.id.btn_0);
        btn_com = findViewById(R.id.btn_com);  btn_sep = findViewById(R.id.btn_sep);
        //G2
        btn_open_bracket = findViewById(R.id.btn_open_bracket);  btn_close_bracket = findViewById(R.id.btn_close_bracket);
        btn_add = findViewById(R.id.btn_add);  btn_sub = findViewById(R.id.btn_sub);  btn_mul = findViewById(R.id.btn_mul);  btn_div = findViewById(R.id.btn_div);  btn_eq_ans = findViewById(R.id.btn_eq_ANS);
        btn_left = new Button(this); btn_right = new Button(this);
        allButtons = new Button[] {btn_11,btn_12,btn_13,btn_14,btn_15,btn_16,btn_21,btn_22,btn_23,btn_24,btn_25,btn_26,btn_clear_all,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_open_bracket,btn_close_bracket,btn_add,btn_sub,btn_mul,btn_div,btn_com,btn_sep,btn_eq_ans};

        eT_output.setOnFocusChangeListener(focusListener);
        eT_input.setOnFocusChangeListener(focusListener);
        disableSoftInputFromAppearing(eT_input); disableSoftInputFromAppearing(eT_output);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.modes_EN, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        setPresenter(new Presenter());
        presenter.setMode(0);

        prepareViews();
        presenter.setMode(0); setViewsAccordingToMode("basic");
        localization();
        requestFocusInput();
        setSupportActionBar(toolbar);
        context = this;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        presenter.attachView(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        presenter.detachView();
    }

    @Inject
    public void setPresenter(Presenter presenter){
        this.presenter = presenter;
        presenter.attachView(this);
    }

    /**
     * sets onClickListener (all buttons), onLongClickListener (btn_11..btn_26), OnSwipeTouchListener (toolbar),
     * disables textSuggestions (et_input,et_output), onTouchlistener (all Buttons)
     */
    public void prepareViews(){
        for(Button btn: allButtons){
            Log.d("btn_touch",getResources().getResourceEntryName(btn.getId()));
            btn.setOnTouchListener(new View.OnTouchListener() {
                @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                        ((Button)view).setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
                    }
                    else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
                        ((Button)view).setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                    }
                    return false;
                }
            });
        }

        btn_clear_all.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("⌫");
            }
        });
        btn_clear_all.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                presenter.inputButton("⌧");
                return true;
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
                presenter.inputButton("×");
            }
        });
        btn_div.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("÷");
            }
        });
        btn_eq_ans.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                presenter.inputButton("=");
            }
        });
        btn_eq_ans.setLongClickable(true);
        btn_eq_ans.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {presenter.inputButton("ANS");return false;}
        });
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {presenter.inputButton("L");
            }
        });
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {presenter.inputButton("R");
            }
        });
        //L2
        btn_11.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {
                presenter.inputButtonLong("11"); return true;
            }
        });
        btn_12.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {
                presenter.inputButtonLong("12"); return true;
            }
        });
        btn_13.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {
                presenter.inputButtonLong("13"); return true;
            }
        });
        btn_14.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {
                presenter.inputButtonLong("14"); return true;
            }
        });
        btn_15.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {
                presenter.inputButtonLong("15"); return true;
            }
        });
        btn_16.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {
                presenter.inputButtonLong("16"); return true;
            }
        });
        //L3
        btn_21.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {
                presenter.inputButtonLong("21"); return true;
            }
        });
        btn_22.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {
                presenter.inputButtonLong("22"); return true;
            }
        });
        btn_23.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {
                presenter.inputButtonLong("23"); return true;
            }
        });
        btn_24.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {
                presenter.inputButtonLong("24"); return true;
            }
        });
        btn_25.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {
                presenter.inputButtonLong("25"); return true;
            }
        });
        btn_26.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View view) {
                presenter.inputButtonLong("26"); return true;
            }
        });

        toolbar.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {presenter.previousMode(); setViewsAccordingToMode(CalcModel.modes[presenter.getMode()]);}
            public void onSwipeLeft() {presenter.nextMode(); setViewsAccordingToMode(CalcModel.modes[presenter.getMode()]);}
        });

        eT_input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        eT_output.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    public void setViewsAccordingToMode(String mode){
        btn_11.setText(presenter.getFunctionButtonText(11)); btn_12.setText(presenter.getFunctionButtonText(12)); btn_13.setText(presenter.getFunctionButtonText(13));
        btn_14.setText(presenter.getFunctionButtonText(14)); btn_15.setText(presenter.getFunctionButtonText(15)); btn_16.setText(presenter.getFunctionButtonText(16));
        btn_21.setText(presenter.getFunctionButtonText(21)); btn_22.setText(presenter.getFunctionButtonText(22)); btn_23.setText(presenter.getFunctionButtonText(23));
        btn_24.setText(presenter.getFunctionButtonText(24)); btn_25.setText(presenter.getFunctionButtonText(25)); btn_26.setText(presenter.getFunctionButtonText(26));
        toolbar.setTitle("");
        toolbarTitle.setText(mode);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * set localization of button names and functionality
     */
    public void localization(){
        Configuration conf = getResources().getConfiguration();
        conf.locale = getResources().getConfiguration().locale;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Resources resources = new Resources(getAssets(), metrics, conf);

        CalcModel.translatePrimeFactorization(resources.getString(R.string.primefactorization));
        CalcModel.translateGreatestCommonDenominator(resources.getString(R.string.greatest_common_denominator));
        CalcModel.translateLeastCommonMultiply(resources.getString(R.string.least_common_multiply));
        CalcModel.translateRandomNumber(resources.getString(R.string.random_number));
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
            default:{return "";}
        }
    }

    @Override public void setSelectionInput(int selectionInput) {try{eT_input.setSelection(selectionInput);}catch(Exception e){}}
    @Override public void setSelectionInput(int selectionInputStart, int selectionInputEnd) {try{eT_input.setSelection(selectionInputStart, selectionInputEnd);}catch(Exception e){}}
    @Override public void setSelectionOutput(int selectionOutput) {try{eT_output.setSelection(selectionOutput);}catch(Exception e){}}
    @Override public void setSelectionOutput(int selectionOutputStart, int selectionOutputEnd) {try{eT_output.setSelection(selectionOutputStart, selectionOutputEnd);}catch(Exception e){}}

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
        return eT_output.getText().toString();
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
            if (selStart >= 0 && selEnd > 0 && selStart < selEnd
                    && selEnd <= eT_input.getText().length() && selStart <= eT_input.getText().length()  ) {
                selection = eT_input.getText().toString().substring(selStart, selEnd);
            } else {
                selection = eT_input.getText().toString();
            }
        } else if (eT_output.hasFocus()) {
            selStart = eT_output.getSelectionStart();
            selEnd = eT_output.getSelectionEnd();
            if (selStart >= 0 && selEnd > 0 && selStart < selEnd
                    && selEnd <= eT_output.getText().length() && selStart <= eT_output.getText().length()  ) {
                selection = eT_output.getText().toString().substring(selStart, selEnd);
            } else {
                selection = eT_output.getText().toString();
            }
        } else {
            selection = eT_input.getText().toString();
        }
        return selection;
    }

    /**
     * Disable soft keyboard from appearing, use in conjunction with android:windowSoftInputMode="stateAlwaysHidden|adjustNothing"
     * @param editText
     */
    public static void disableSoftInputFromAppearing(EditText editText) {
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextIsSelectable(true);
        editText.setShowSoftInputOnFocus(false);
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
