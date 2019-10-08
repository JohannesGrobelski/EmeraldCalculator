package com.example.titancalculator;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.util.DisplayMetrics;
import android.util.Log;
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


public class CalcActivity_small extends AppCompatActivity {
    TableLayout background;

    static final int REQUEST_CODE_CONST = 1;  // The request code
    static final int REQUEST_CODE_CONV = 1;  // The request code
    static final int REQUEST_CODE_Verlauf = 1;  // The request code

    LinkedList<String> verlauf = new LinkedList<>();
    int buttonshapeID = R.drawable.buttonshape_square;
    String buttonfüllung="voll";
    DisplayMetrics screen_density;

    Set<String> UserFctGroups = new HashSet<>();

    String current_Callback="";
    String answer="";

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


    EditText tV_eingabe;
    boolean tV_eingabe_hasFocus=true;
    EditText tV_ausgabe;

    NavigatableString I;


    String X = "";
    String Y = "";


    private Set<Button> BTN_FKT;
    private Set<Button> BTN_NUMBERS;
    private Set<Button> BTN_SAVES;
    private Set<Button> BTN_SPECIALS;
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
        setTitle("Rechner");
        SettingsApplier.setColors(CalcActivity_small.this);
        applySettings();
        setBackgrounds();
        ArrayList<View> list = new ArrayList<View>() {{addAll(BTN_ALL);add(tV_eingabe);add(tV_ausgabe);}};
        SettingsApplier.setFonts(CalcActivity_small.this,list);


        tV_ausgabe.setOnFocusChangeListener(focusListener);
        tV_eingabe.setOnFocusChangeListener(focusListener);

        mode = "BASIC";

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
        setContentView(R.layout.activity_calc_small);

        setTitle("Rechner");

        background = findViewById(R.id.s_background);




        tV_eingabe = findViewById(R.id.s_tV_Eingabe);
        tV_ausgabe = findViewById(R.id.s_tV_Ausgabe);

        tV_ausgabe.setOnFocusChangeListener(focusListener);
        tV_eingabe.setOnFocusChangeListener(focusListener);
        mode = "BASIC";


        tV_eingabe.setShowSoftInputOnFocus(false);
        tV_eingabe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(CalcActivity_small.this);
                //v.performClick();
                return false;
            }
        });

        tV_ausgabe.setShowSoftInputOnFocus(false);
        tV_ausgabe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(CalcActivity_small.this);
                //v.performClick();
                return false;
            }
        });


        background.setOnTouchListener(new OnSwipeTouchListener(CalcActivity_small.this) {
            public void onSwipeTop() {
                Toast.makeText(CalcActivity_small.this, "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(CalcActivity_small.this, "right", Toast.LENGTH_SHORT).show();
                Intent conversionIntent = new Intent(CalcActivity_small.this, MainActivity.class);
                conversionIntent.putExtra("verlauf",verlaufToString(verlauf));
                conversionIntent.putExtra("input",tV_eingabe.getText().toString());
                conversionIntent.putExtra("output",tV_ausgabe.getText().toString());
                conversionIntent.putExtra("swipeDir","right");
                conversionIntent.putExtra("layout","small");
                startActivity(conversionIntent);
                finish();
            }
            public void onSwipeLeft() {
                Toast.makeText(CalcActivity_small.this, "left", Toast.LENGTH_SHORT).show();
                Intent conversionIntent = new Intent(CalcActivity_small.this, MainActivity.class);
                conversionIntent.putExtra("verlauf",verlaufToString(verlauf));
                conversionIntent.putExtra("input",tV_eingabe.getText().toString());
                conversionIntent.putExtra("output",tV_ausgabe.getText().toString());
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

        BTN_FKT = new HashSet<>(Arrays.asList(new Button[]{btn_clear, btn_clearall}));
        BTN_NUMBERS = new HashSet<>(Arrays.asList(new Button[]{btn_com, btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9}));
        BTN_SAVES = new HashSet<>(Arrays.asList(new Button[]{btn_eq}));
        BTN_SPECIALS = new HashSet<>(Arrays.asList(new Button[]{btn_mul, btn_div, btn_sub, btn_add}));
        BTN_ALL = new ArrayList<>();
        BTN_ALL.addAll(BTN_FKT);
        BTN_ALL.addAll(BTN_NUMBERS);
        BTN_ALL.addAll(BTN_SAVES);
        BTN_ALL.addAll(BTN_SPECIALS);

        applySettings();
        SettingsApplier.setColors(CalcActivity_small.this);
        setBackgrounds();
        ArrayList<View> list = new ArrayList<View>() {{addAll(BTN_ALL);add(tV_eingabe);add(tV_ausgabe);}};
        SettingsApplier.setFonts(CalcActivity_small.this,list);

        try {
            setBackgroundImage();
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
        //Toast.makeText(CalcActivity_small.this,"Ausgabe: "+res,Toast.LENGTH_SHORT).show();
        tV_ausgabe.setText(res);
    }

    public static void setDefaultColors(){
        SettingsApplier.color_fops = 0x3498db; //blau
        SettingsApplier.color_act =  0x9b59b6; //lila
        SettingsApplier.color_fkt = 0x2ecc71; //grün
        SettingsApplier.color_specials = 0xe67e22; //orange
        SettingsApplier.color_numbers = 0xecf0f1; //hellgrau
        SettingsApplier.color_saves = 0xAFAFAF; //dunkelgrau
        SettingsApplier.color_display = 0xecf0f1; //hellgrau
        SettingsApplier.color_displaytext = 0x000000; //schwarz
        SettingsApplier.color_background = 0xFFFFFF; //weiß
    }



    void setBackground(View x){
        if(buttonshapeID==0)applySettings();
        Drawable background;
        SettingsApplier.setColors(CalcActivity_small.this);
        float factor_font = 0.5f;
        boolean stroke = true;

        //Default Case
        background = getResources().getDrawable(buttonshapeID);
        setColor(background, SettingsApplier.color_specials,buttonfüllung,stroke);
        int darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.color_specials,factor_font);
        if(x instanceof Button) ((Button) x).setTextColor(darker);

        if(x instanceof Button){
            //fix für größe dieser kleinen unicode symbole
            if(((Button) x).getText().equals(getResources().getString(R.string.CLEAR_ALL)) || ((Button) x).getText().equals(getResources().getString(R.string.CLEAR))){
                ((Button) x).setTextSize(Math.min(30,((Button)btn_0).getTextSize()*2));
            }
        }

        if(x.equals(display)){
            background = getResources().getDrawable(buttonshapeID);
            setColor(background, SettingsApplier.color_display,buttonfüllung,stroke);
            tV_ausgabe.setTextColor(SettingsApplier.color_displaytext);
            tV_eingabe.setTextColor(SettingsApplier.color_displaytext);
            x.setBackground(background);
        }

        else if(BTN_FKT.contains(x)){
            background = getResources().getDrawable(buttonshapeID);
            setColor(background, SettingsApplier.color_fkt,buttonfüllung,stroke);
            darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.color_fkt,factor_font);
            if(x instanceof Button) ((Button) x).setTextColor(darker);
        }
        else if(BTN_NUMBERS.contains(x)){
            background = getResources().getDrawable(buttonshapeID);
            setColor(background, SettingsApplier.color_numbers,buttonfüllung,stroke);
            darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.color_numbers,factor_font);
            if(x instanceof Button) ((Button) x).setTextColor(darker);
        }
        else if(BTN_SAVES.contains(x)){
            background = getResources().getDrawable(buttonshapeID);
            setColor(background, SettingsApplier.color_saves,buttonfüllung,stroke);
            darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.color_saves,factor_font);
            if(x instanceof Button) ((Button) x).setTextColor(darker);
        }

        x.setBackground(background);

    }

    void setBackgroundImage() throws Exception {
        String path = PreferenceManager.getDefaultSharedPreferences(CalcActivity_small.this).getString("backgroundimage", "");
        if(path.equals(""))return;

        if(!checkPermissionForReadExtertalStorage(this))requestPermissionForReadExtertalStorage(this);

        try{
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            View view = (ImageView) findViewById(R.id.container);
            view.setBackground(bd);
        } catch (Exception e){
            Toast t =  Toast.makeText(CalcActivity_small.this,"Could not draw Backgroundimage:"+e.getMessage(),Toast.LENGTH_LONG);
            t.show();
            Log.e("IMAGEERROR",path);
        }

    }


    void setBackgrounds(){
        display.setBackgroundColor(SettingsApplier.color_display);
        background.setBackgroundColor(SettingsApplier.color_background);

        //setBackground(spinner_shift);


        for(Button b: BTN_ALL){
            setBackground(b);
        }

        setBackground(display);
    }



    static void setColor(Drawable background, int c, String füllung, boolean stroke){
        if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            shapeDrawable.getPaint().setColor(c);
        } else if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(c);
            int rahmen_farbe = ButtonSettingsActivity.manipulateColor(c,0.7f);
            if(füllung.equals("leer"))gradientDrawable.setColor(SettingsApplier.color_background);
            if(stroke)gradientDrawable.setStroke(7, rahmen_farbe);
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(c);
        }
        else Log.e("setColor Error","");

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
        language = PreferenceManager.getDefaultSharedPreferences(CalcActivity_small.this).getString("pref_lang","english");
        if(language.equals("english") || language.equals("englisch")){
            btn_menu.setText(R.string.MENU_EN);
            btn_verlauf.setText(R.string.VERLAUFEN);
            act_options = getResources().getStringArray(R.array.act_EN);
            mode_options = FunctionGroupSettingsActivity.getGroups(CalcActivity_small.this);
        }
        else if(language.equals("german") || language.equals("deutsch")){
            btn_menu.setText(R.string.MENU_DE);
            btn_verlauf.setText(R.string.VERLAUFDE);
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
        if (PreferenceManager.getDefaultSharedPreferences(CalcActivity_small.this).contains("buttonshape")) {
            String form = PreferenceManager.getDefaultSharedPreferences(CalcActivity_small.this).getString("buttonshape","round");
            if(form != null){
                switch(form){
                    case "Round": {
                        buttonshapeID = R.drawable.buttonshape_round;
                        break;
                    }
                    case "Square": {
                        buttonshapeID = R.drawable.buttonshape_square;
                        break;
                    }
                }
            }
            else Toast.makeText(CalcActivity_small.this,"no buttonshape settings",Toast.LENGTH_SHORT).show();
        }

        //buttonfüllung
        if (PreferenceManager.getDefaultSharedPreferences(CalcActivity_small.this).contains("buttonfüllung")) {
            buttonfüllung = PreferenceManager.getDefaultSharedPreferences(CalcActivity_small.this).getString("buttonfüllung","voll");
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


