package com.example.titancalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.helper.InitConstMapDE;
import com.example.titancalculator.helper.InitConstMapEN;
import com.example.titancalculator.helper.MainDisplay.SettingsApplier;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ConstantsActivity extends AppCompatActivity {
    static String language="";
    static String QCat="";
    static String QConst="";
    static String QPrec="";

    String[] catAr;
    String[] nachkommastellenAr;

    HashMap<String, String> uniKAr = new HashMap<>();
    HashMap<String, String> matKAr = new HashMap<>();
    HashMap<String, String> phyKAr = new HashMap<>();
    HashMap<String, String> chKAr = new HashMap<>();
    HashMap<String, String> favKAr = new HashMap<>();
    HashMap<String, String> eignKAr = new HashMap<>();
    HashMap<String, String> currentMap = new HashMap<>();


    String result="";
    int currentAuswahlKategorie = 0;
    int currentAuswahlKonstante = 0;
    int currentNachkommastellen = 5;

    View const_background;
    TextView tV_Pfad;
    ListView LV_Auswahl;
    Button btn_back;
    EditText tV_cur_const;
    EditText tV_cur_const_val;
    Button copy;
    Button save_fav;
    Button del_fav;

    Set<View> VIEW_CONST;

    String zustand = "kategorie";
    private int precision = 10;

    @Override
    protected void onResume() {
        super.onResume();
        applySettings();
        initMaps();

        try {
            SettingsApplier.setBackgroundImage(ConstantsActivity.this,const_background);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_constants);

        const_background = findViewById(R.id.const_background);
        LV_Auswahl = (ListView) findViewById(R.id.LV_Auswahl);
        btn_back = (Button) findViewById(R.id.btn_back);

        tV_Pfad = findViewById(R.id.tV_Pfad);
        tV_cur_const = (EditText) findViewById(R.id.eT_cur_const);
        tV_cur_const_val = (EditText) findViewById(R.id.eT_cur_const_val2);

        eTSetEditable(false);

        copy = findViewById(R.id.btn_copy);
        save_fav = findViewById(R.id.btn_mark_fav);
        del_fav = findViewById(R.id.btn_del_fav);

        VIEW_CONST = new HashSet<View>(Arrays.asList(tV_Pfad,tV_cur_const,tV_cur_const_val,btn_back,copy,save_fav,del_fav));
        ArrayList<View> list = new ArrayList<View>() {{addAll(VIEW_CONST);add(tV_Pfad);add(tV_cur_const);add(tV_cur_const_val);add(LV_Auswahl);}};
        SettingsApplier.setFonts(ConstantsActivity.this,list);

        LV_Auswahl.setBackgroundColor(SettingsApplier.getColor_background(ConstantsActivity.this));
        applySettings();
        initMaps();


        //ArrayAdapter adapter_fct = new ArrayAdapter<String>(this,R.layout.lvitem_layout, catAr);

        //ArrayAdapter adpt_modeoptions = new ArrayAdapter<String>(this, R.layout.lvitem_layout, mode_options);
        SettingsApplier.setArrayAdapter(ConstantsActivity.this,LV_Auswahl,catAr,SettingsApplier.getColor_const(ConstantsActivity.this));


        tV_Pfad.setText(QCat);

        LV_Auswahl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if(zustand.equals("kategorie")){
                    if(zustand.equals("eigene Konstanten")){
                        eTSetEditable(true);
                    } else{
                        eTSetEditable(false);
                    }
                    Log.e("kataus pos: ",String.valueOf(position));
                    Log.e("kataus val: ",String.valueOf(LV_Auswahl.getItemAtPosition(position)));

                    setUpMeasure((String) LV_Auswahl.getItemAtPosition(position));
                    currentAuswahlKategorie = position;
                    zustand = "konstante";
                    tV_Pfad.setText(QConst);
                } else if(zustand.equals("konstante")){
                    currentAuswahlKonstante=position;
                    result = currentMap.get(LV_Auswahl.getItemAtPosition(position));
                    tV_cur_const.setText(LV_Auswahl.getItemAtPosition(position).toString());
                    LV_COM_anpassen(getNachkommastellen(result));
                    tV_cur_const_val.setText(result);

                    zustand = "nachkommastelle";
                    tV_Pfad.setText(QPrec);
                    currentNachkommastellen = Math.min(precision,getNachkommastellen(result));
                } else if(zustand.equals("nachkommastelle")){
                    currentNachkommastellen = Integer.parseInt(LV_Auswahl.getItemAtPosition(position).toString());

                    result = currentMap.get(currentMap.keySet().toArray()[currentAuswahlKonstante]);
                    result = getStringNumberTrimed(result,currentNachkommastellen);
                    tV_cur_const_val.setText(result);

                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(zustand.equals("kategorie")){
                    tV_Pfad.setText("Kategorie?");
                } else if(zustand.equals("konstante")){
                    SettingsApplier.setArrayAdapter(ConstantsActivity.this,LV_Auswahl,catAr,SettingsApplier.getColor_const(ConstantsActivity.this));

                    zustand = "kategorie";
                    tV_Pfad.setText(QCat);
                } else if(zustand.equals("nachkommastelle")){
                    setUpMeasure(catAr[currentAuswahlKategorie]);
                    zustand = "konstante";

                    result = currentMap.get(LV_Auswahl.getItemAtPosition(currentAuswahlKonstante).toString());
                    tV_cur_const.setText(LV_Auswahl.getItemAtPosition(currentAuswahlKonstante).toString());
                    tV_cur_const_val.setText(result);
                    //LV_Auswahl anpassen (Konstantenauswahl)
                        LV_COM_anpassen(getNachkommastellen(result));
                    String[] currentMapAr = currentMap.keySet().toArray(new String[currentMap.keySet().size()]);
                    SettingsApplier.setArrayAdapter(ConstantsActivity.this,LV_Auswahl,currentMapAr,SettingsApplier.getColor_const(ConstantsActivity.this));
                    tV_Pfad.setText(QConst);
                }
            }
        });


        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("RESULT_STRING", result);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        del_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentAuswahlKategorie == 0 || currentAuswahlKonstante == 0 || result.isEmpty())return;

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ConstantsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();


                String save = currentMap.keySet().toArray()[currentAuswahlKonstante].toString();

                Set<String> FavKonst =  new HashSet<String>(PreferenceManager.getDefaultSharedPreferences(ConstantsActivity.this).getStringSet("FAV_KONST", new HashSet<String>()));
                if(FavKonst.contains(save))FavKonst.remove(save);

                editor.putStringSet("FAV_KONST",FavKonst);

                Toast t =  Toast.makeText(ConstantsActivity.this,"Removed from Favorites: "+save,Toast.LENGTH_SHORT);
                t.show();
                editor.commit();

                initFav();
            }
        });

        save_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentAuswahlKategorie == 0 || currentAuswahlKonstante == 0 || result.isEmpty())return;

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ConstantsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                String save = currentMap.keySet().toArray()[currentAuswahlKonstante].toString();

                Set<String> FavKonst =  new HashSet<String>(PreferenceManager.getDefaultSharedPreferences(ConstantsActivity.this).getStringSet("FAV_KONST", new HashSet<String>()));
                if(!FavKonst.contains(save))FavKonst.add(save);

                editor.putStringSet("FAV_KONST",FavKonst);


                Toast t =  Toast.makeText(ConstantsActivity.this,"Saved to Favorites: "+save,Toast.LENGTH_SHORT);
                t.show();
                editor.commit();

                initFav();
            }
        });


        try {
            SettingsApplier.setBackgroundImage(ConstantsActivity.this,const_background);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpMeasure(String cat) {
        if (cat.equals("Universelle Konstanten") || cat.equals("universal constants")) {
            currentMap = uniKAr;
        } else if (cat.equals("mathematische Konstanten") || cat.equals("mathematical constants")) {
            currentMap = matKAr;
        } else if (cat.equals("Physikalische Konstanten") || cat.equals("physical constants")) {
            currentMap = phyKAr;
        } else if (cat.equals("Chemische Konstanten") || cat.equals("chemical constants")) {
            currentMap = chKAr;
        } else if (cat.equals("Favoriten Konstanten") || cat.equals("favorite constants")) {
            currentMap = favKAr;
        } else if (cat.equals("eigene Konstanten") || cat.equals("own constants")) {
            currentMap = eignKAr;
        }
        String[] currentMapAr = currentMap.keySet().toArray(new String[currentMap.keySet().size()]);
        SettingsApplier.setArrayAdapter(ConstantsActivity.this,LV_Auswahl,currentMapAr,SettingsApplier.getColor_const(ConstantsActivity.this));

    }

    private void initMaps(){
        if(language.equals(""))applySettings();
        else{
            if(language.equals("english") || language.equals("englisch")){
                InitConstMapEN.init(ConstantsActivity.this);
                uniKAr = InitConstMapEN.getUniKAr();
                matKAr = InitConstMapEN.getMatKAr();
                phyKAr = InitConstMapEN.getPhyKAr();
                chKAr = InitConstMapEN.getChKAr();
                initFav();
                favKAr = InitConstMapEN.getFavKAr();
                eignKAr = InitConstMapEN.geteignKAr();
            } else if(language.equals("deutsch") || language.equals("german")){
                InitConstMapDE.init(ConstantsActivity.this);
                uniKAr = InitConstMapDE.getUniKAr();
                matKAr = InitConstMapDE.getMatKAr();
                phyKAr = InitConstMapDE.getPhyKAr();
                chKAr = InitConstMapDE.getChKAr();
                initFav();
                favKAr = InitConstMapDE.getFavKAr();
                eignKAr = InitConstMapDE.geteignKAr();
            }
        }
      

    }

    private void initFav(){
        if(language.equals(""))applySettings();
        else{
            if(language.equals("english") || language.equals("englisch")){
                InitConstMapEN.initFavKAr();
                favKAr = InitConstMapEN.getFavKAr();
            } else if(language.equals("deutsch") || language.equals("german")){
                InitConstMapDE.initFavKAr();
                favKAr = InitConstMapDE.getFavKAr();
            }
        }
        //Toast t =  Toast.makeText(ConstantsActivity.this,"|Fav|: "+favKAr.keySet().size(),Toast.LENGTH_LONG);
        //t.show();
        //Toast t1 =  Toast.makeText(ConstantsActivity.this,"Fav(1): "+favKAr.get((favKAr.keySet().toArray()[0]).toString()),Toast.LENGTH_LONG);
        //t1.show();
    }

    private static String getStringNumberTrimed(String input, int nachkommastellen){
        Pattern p=Pattern.compile("[0-9]\\.[0-9]+");
        Matcher m=p.matcher(input);
        String zahl="";
        if(m.find()){
            zahl = m.group();
            //System.out.println("Start :"+m.start()+", End : "+m.end()+", Group "+m.group());
        } else {
            return input;
        }

        double num = Double.parseDouble(zahl);
        String repeated = new String(new char[nachkommastellen]).replace("\0", "#");
        String pattern = "#."+repeated;
        DecimalFormat df = new DecimalFormat(pattern);
        String roundednumber = df.format(num);

        input = input.replace(zahl,roundednumber);

        return input;
    }

    private static int getNachkommastellen(String input){
        Pattern p=Pattern.compile("[0-9]\\.[0-9]+");
        Matcher m=p.matcher(input);
        String zahl="";
        if(m.find()){
            zahl = m.group();
            //System.out.println("Start :"+m.start()+", End : "+m.end()+", Group "+m.group());
        } else {
            return 0;
        }

        return zahl.length()-1 - zahl.lastIndexOf(".");
    }

    public static void main(String[] a){
        System.out.println(getStringNumberTrimed("BLA BLA 2.123456789 10^265 BLA BLA",3));
        System.out.println(getNachkommastellen("BLA BLA 2.12345 10^265 BLA BLA"));

    }

    public void LV_COM_anpassen(int Nachkommastellen){
        String[] nachkommastellenAr = new String[Nachkommastellen];
        for(int i=0; i<nachkommastellenAr.length; i++){
            nachkommastellenAr[i] = String.valueOf(i+1);
        }
        SettingsApplier.setArrayAdapter(ConstantsActivity.this,LV_Auswahl,nachkommastellenAr,SettingsApplier.getColor_const(ConstantsActivity.this));
    }

    private void eTSetEditable(boolean editable){
        if(editable == false){
            tV_cur_const.setShowSoftInputOnFocus(false);
            tV_cur_const.setInputType(InputType.TYPE_NULL);
            tV_cur_const.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    CalcActivity_science.hideKeyboard(ConstantsActivity.this);
                    //v.performClick();
                    return false;
                }
            });

            tV_cur_const_val.setInputType(InputType.TYPE_NULL);
            tV_cur_const_val.setShowSoftInputOnFocus(false);
            tV_cur_const_val.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    CalcActivity_science.hideKeyboard(ConstantsActivity.this);
                    //v.performClick();
                    return false;
                }
            });
        } else {
            tV_cur_const.setShowSoftInputOnFocus(true);
            tV_cur_const.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            tV_cur_const.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });

            tV_cur_const_val.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            tV_cur_const_val.setShowSoftInputOnFocus(true);
            tV_cur_const_val.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }
    }


    private void applySettings(){
        language = PreferenceManager.getDefaultSharedPreferences(ConstantsActivity.this).getString("pref_lang","english");
        //Toast t =  Toast.makeText(SettingsActivity.this,"Lang: "+language,Toast.LENGTH_LONG);
        //t.show();
        if(language.equals("english") || language.equals("englisch")){
            catAr = new String[]{"universal constants", "mathematical constants", "physical constants", "chemical constants", "favorite constants", "own constants"};
            btn_back.setText("back");
            copy.setText("COPY");
            save_fav.setText("SAVE FAV");
            del_fav.setText("DEL FAV");
            QCat = "category?";
            QConst = "constant?";
            QPrec = "precision?";
        }
        else if(language.equals("german") || language.equals("deutsch")){
            catAr = new String[]{"Universelle Konstanten","mathematische Konstanten","Physikalische Konstanten","Chemische Konstanten","Favoriten Konstanten","eigene Konstanten"};
            btn_back.setText("zurück");
            copy.setText("KOPIEREN");
            save_fav.setText("FAV SPEICHERN");
            del_fav.setText("FAV LÖSCHEN");
            QCat = "Kategorie?";
            QConst = "Konstante?";
            QPrec = "Nachkommastellen?";
        }

        if (PreferenceManager.getDefaultSharedPreferences(ConstantsActivity.this).contains("pref_precision")) {
            String prec = PreferenceManager.getDefaultSharedPreferences(ConstantsActivity.this).getString("pref_precision","10");
            if(prec != null)precision =  Integer.valueOf(prec) + 1;
        }

        setBackgrounds();
    }



    void setBackgrounds(){
        tV_Pfad.setTextColor(SettingsApplier.getColor_const(ConstantsActivity.this));
        const_background.setBackgroundColor(SettingsApplier.getColor_background(ConstantsActivity.this));
        for(View v: VIEW_CONST){
            SettingsApplier.setViewDesign(ConstantsActivity.this,v,SettingsApplier.getColor_const(ConstantsActivity.this));
        }
    }


}
