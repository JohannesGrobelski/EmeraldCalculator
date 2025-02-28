package com.example.titancalculator.geplanteFeatures;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.MainActivity;
import com.example.titancalculator.R;
import com.example.titancalculator.geplanteFeatures.helper.MainDisplay.SettingsApplier;
import com.example.titancalculator.helper.Math_String.MathEvaluator;
import com.example.titancalculator.geplanteFeatures.Umrechnung.DatenspeicherUmrechnung;
import com.example.titancalculator.geplanteFeatures.Umrechnung.DatentransferUmrechnung;
import com.example.titancalculator.geplanteFeatures.Umrechnung.FlächenUmrechnung;
import com.example.titancalculator.geplanteFeatures.Umrechnung.GeschwindigkeitUmrechnung;
import com.example.titancalculator.geplanteFeatures.Umrechnung.KraftUmrechnung;
import com.example.titancalculator.geplanteFeatures.Umrechnung.LaengeUmrechnung;
import com.example.titancalculator.geplanteFeatures.Umrechnung.LeistungUmrechnung;
import com.example.titancalculator.geplanteFeatures.Umrechnung.MasseUmrechnung;
import com.example.titancalculator.geplanteFeatures.Umrechnung.StromstärkeUmrechnung;
import com.example.titancalculator.geplanteFeatures.Umrechnung.TemperaturUmrechnung;
import com.example.titancalculator.geplanteFeatures.Umrechnung.VolumenUmrechnung;
import com.example.titancalculator.geplanteFeatures.Umrechnung.ZeitUmrechnung;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ConversionActivity extends AppCompatActivity implements  PopupMenu.OnMenuItemClickListener {
    String[] measArDE = {"Länge","Zeit","Geschwindigkeit","Masse","Volumen","Fläche","Temperatur","Kraft","Leistung","Stromstärke","Datenspeicher","Datentransfer"};
    String[] measArEN = {"length","time","speed","mass","volume","area","temperature","force","power","current","data storage","data rate"};
    String language="";
    int precision=10;
    Float fontsize;

    private static LinkedHashSet<String> laenge = new LinkedHashSet<>();
    private static LinkedHashSet<String> zeit = new LinkedHashSet<>();
    private static LinkedHashSet<String> geschwindigkeit = new LinkedHashSet<>();

    private static LinkedHashSet<String> masse = new LinkedHashSet<>();
    private static LinkedHashSet<String> volumen = new LinkedHashSet<>();
    private static LinkedHashSet<String> fläche = new LinkedHashSet<>();

    private static LinkedHashSet<String> temperatur = new LinkedHashSet<>();
    private static LinkedHashSet<String> kraft = new LinkedHashSet<>();
    private static LinkedHashSet<String> leistung = new LinkedHashSet<>();

    private static LinkedHashSet<String> stromstärke = new LinkedHashSet<>();
    private static LinkedHashSet<String> datenspeicher = new LinkedHashSet<>();
    private static LinkedHashSet<String> datentransfer = new LinkedHashSet<>();

    LinkedHashSet<String> currentSet = new LinkedHashSet<>();
    
    String zustand = "kategorie"; String lastInput = "";


    int currentAuswahlKategorie = 0;
    int currentAuswahlKonstante = 0;

    String currentConv="";

    View conv_background;
    Button btn_auswahl;

    EditText eT_cur_const_val1,eT_cur_const_val2;
    Button btn_maßeinheit1, btn_maßeinheit2;

    Button selected;
    Button btn_save;

    Set<View> VIEW_CONV;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.einstellungen:
                Intent settingsIntent = new Intent(this,
                        SettingsActivity.class);
                startActivity(settingsIntent);
            case R.id.taschenrechner:
                Intent mainIntent = new Intent(this,
                        MainActivity.class);
                startActivity(mainIntent);
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void setUpMeasure(String cat) {
        if(cat.equals("Länge") || cat.equals("length")) {
            currentSet = laenge;
        } else if (cat.equals("Zeit")) {
            currentSet = zeit;
        } else if (cat.equals("Geschwindigkeit")) {
            currentSet = geschwindigkeit;
        } else if (cat.equals("Masse")) {
            currentSet = masse;
        } else if (cat.equals("Volumen")) {
            currentSet = volumen;
        } else if (cat.equals("Fläche")) {
            currentSet = fläche;
        } else if (cat.equals("Temperatur")) {
            currentSet = temperatur;
        }else if (cat.equals("Kraft")) {
            currentSet = kraft;
        }else if (cat.equals("Leistung")) {
            currentSet = leistung;
        }else if (cat.equals("Stromstärke")) {
            currentSet = stromstärke;
        }else if (cat.equals("Datenspeicher")) {
            currentSet = datenspeicher;
        }else if (cat.equals("Datentransfer")) {
            currentSet = datentransfer;
        }

        String[] a = (String[]) currentSet.toArray(new String[currentSet.size()]);
        //ArrayAdapter adapter_cat = new ArrayAdapter<String>(this, R.layout.lvitem_layout, a);
        SettingsApplier.setArrayAdapter(ConversionActivity.this,btn_auswahl,a,SettingsApplier.getColor_conv(ConversionActivity.this));
        btn_auswahl.setBackgroundColor(SettingsApplier.getColor_background(ConversionActivity.this));
        setBackgrounds();
    }

    void select(Button x){
        if(x.equals(selected)){
            Toast t =  Toast.makeText(ConversionActivity.this,"unclicked: "+x.getText().toString(),Toast.LENGTH_SHORT);
            t.show();
            visual_select(x);
            selected = new Button(this);
        } else if(!x.equals(selected)) {
            Toast t =  Toast.makeText(ConversionActivity.this,"clicked: "+x.getText().toString(),Toast.LENGTH_SHORT);
            t.show();
            visual_select(selected);

            selected = x;
            visual_unselect(x);
        }
    }

    void visual_select(Button x){
        if(x==null)return;
        Drawable background_fkt = getResources().getDrawable(SettingsApplier.getButtonshapeID());
        setColor(background_fkt,SettingsApplier.getColor_conv(ConversionActivity.this),SettingsApplier.getButtonfüllung(),12);

        x.setBackground(background_fkt);
    }

    void visual_unselect(Button x){
        if(x==null)return;
        Drawable background_fkt = getResources().getDrawable(SettingsApplier.getButtonshapeID());
        setColor(background_fkt,SettingsApplier.getColor_conv(ConversionActivity.this),SettingsApplier.getButtonfüllung(),12);

        x.setBackground(background_fkt);
    }

    void setBackgrounds(){
        conv_background.setBackgroundColor(SettingsApplier.getColor_background(ConversionActivity.this));
        for(View v: VIEW_CONV){
            SettingsApplier.setFonts(ConversionActivity.this,v);
            SettingsApplier.setViewDesign(ConversionActivity.this,v,SettingsApplier.getColor_conv(ConversionActivity.this));
        }
    }

    public void convert(){
        //Toast.makeText(ConversionActivity.this,lastInput,Toast.LENGTH_SHORT).show();
        EditText eTtarget;
        if(lastInput.equals("em1"))eTtarget = eT_cur_const_val2;
        else if(lastInput.equals("em2"))eTtarget = eT_cur_const_val1;
        else return;
        String me1 = btn_maßeinheit1.getText().toString();
        String me2 = btn_maßeinheit2.getText().toString();
        String e1val = eT_cur_const_val1.getText().toString();
        if(checkValid(me1,me2,e1val)>0){
            String target = MathEvaluator.evaluate(convert(new BigDecimal(e1val),me1,me2));
            eTtarget.setText(target);
            currentConv = target;
        }
        lastInput = "";
    }

    public void showMenuKategorien(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        if(language.equals("deutsch") || language.equals("german")) {
            popup.inflate(R.menu.menu_conversion_kategorien);
        } else {
            popup.inflate(R.menu.menu_conversion_categories);
        }
        popup.show();
        setBackgrounds();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Toast.makeText(this,item.getItemId(),Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.Länge:
                if(language.equals("deutsch"))btn_auswahl.setText("Länge");
                else btn_auswahl.setText("length");
                setUpMeasure("Länge");
                return true;
            case R.id.Zeit:
                if(language.equals("deutsch"))btn_auswahl.setText("Zeit");
                else btn_auswahl.setText("time");
                setUpMeasure("Zeit");
                return true;
            case R.id.Geschwindigkeit:
                if(language.equals("deutsch"))btn_auswahl.setText("Geschwindigkeit");
                else btn_auswahl.setText("speed");
                setUpMeasure("Geschwindigkeit");
                return true;
            case R.id.Masse:
                if(language.equals("deutsch"))btn_auswahl.setText("Masse");
                else btn_auswahl.setText("mass");
                setUpMeasure("Masse");
                return true;
            case R.id.Volumen:
                if(language.equals("deutsch"))btn_auswahl.setText("Volumen");
                else btn_auswahl.setText("volume");
                setUpMeasure("Volumen");
                return true;
            case R.id.Fläche:
                if(language.equals("deutsch"))btn_auswahl.setText("Fläche");
                else btn_auswahl.setText("area");
                setUpMeasure("Fläche");
                return true;
            case R.id.Temperatur:
                if(language.equals("deutsch"))btn_auswahl.setText("Temperatur");
                else btn_auswahl.setText("temperature");
                setUpMeasure("Temperatur");
                return true;
            case R.id.Kraft:
                if(language.equals("deutsch"))btn_auswahl.setText("Kraft");
                else btn_auswahl.setText("force");
                setUpMeasure("Kraft");
                return true;
            case R.id.Leistung:
                if(language.equals("deutsch"))btn_auswahl.setText("Leistung");
                else btn_auswahl.setText("performance");
                setUpMeasure("Leistung");
                return true;
            case R.id.Stromstärke:
                if(language.equals("deutsch"))btn_auswahl.setText("Stromstärke");
                else btn_auswahl.setText("current");
                setUpMeasure("Stromstärke");
                return true;
            case R.id.Datenspeicher:
                if(language.equals("deutsch"))btn_auswahl.setText("Datenspeicher");
                else btn_auswahl.setText("datamemory");
                setUpMeasure("Datenspeicher");
                return true;
            case R.id.Datentransfer:
                if(language.equals("deutsch"))btn_auswahl.setText("Datentransfer");
                else btn_auswahl.setText("datatransfer");
                setUpMeasure("Datentransfer");
                return true;
            default:
                return false;
        }
    }


    public void showMenuMeasurements(final int  id, View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        final List<String> currentSetList = new ArrayList<String>(currentSet);
        for(int i=0; i<currentSet.size(); i++){
            popup.getMenu().add(Menu.NONE,i,i,currentSetList.get(i));
        }
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if(id==1)btn_maßeinheit1.setText(currentSetList.get(i));
                else btn_maßeinheit2.setText(currentSetList.get(i));
                return false;
            }

        });
        setBackgrounds();
    }



    @Override
    protected void onResume() {
        super.onResume();
        applySettings();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);


        conv_background = findViewById(R.id.conv_background);
        btn_auswahl = findViewById(R.id.btn_Auswahl);
        btn_save = findViewById(R.id.btn_save);


        eT_cur_const_val1 = findViewById(R.id.eT_cur_const_val1);
        eT_cur_const_val2 = findViewById(R.id.eT_cur_const_val2);
        btn_maßeinheit1 = findViewById(R.id.btn_maßeinheit1);
        btn_maßeinheit2 = findViewById(R.id.btn_maßeinheit2);

        VIEW_CONV = new HashSet<View>(Arrays.asList(btn_auswahl,eT_cur_const_val1,eT_cur_const_val2,btn_maßeinheit1,btn_maßeinheit2,btn_save));


        applySettings();

        //ArrayAdapter adapter_Meas = new ArrayAdapter<String>(this,R.layout.lvitem_layout, measArDE);
        initMaps();

        btn_auswahl.setBackgroundColor(SettingsApplier.getColor_background(ConversionActivity.this));


        eT_cur_const_val1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    if(eT_cur_const_val1.getText().toString().length() > 0 && eT_cur_const_val2.getText().toString().length() > 0 ){
                        eT_cur_const_val1.setText(""); eT_cur_const_val2.setText("");
                    } else {
                        convert();
                    }
                }
            }
        });
        eT_cur_const_val1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lastInput = "em1";
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        eT_cur_const_val2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    if(eT_cur_const_val1.getText().toString().length() > 0 && eT_cur_const_val2.getText().toString().length() > 0 ){
                        eT_cur_const_val1.setText(""); eT_cur_const_val2.setText("");
                    } else {
                        convert();
                    }
                }
            }
        });
        eT_cur_const_val2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lastInput = "em2";
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });



        btn_maßeinheit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenuMeasurements(1,view);
            }
        });
        btn_maßeinheit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenuMeasurements(2,view);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("RESULT_STRING", currentConv);
                setResult(RESULT_OK, intent);
                finish();
            }
        });



        btn_auswahl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenuKategorien(view);
            }
        });
    }

    private static ArrayAdapter<String> getArrayAdapter(final Context context, final float fontsize, String[] array){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_shift_style, array){
            float factor_font = 0.5f;
            int fontcolor = SettingsApplier.manipulateColor(SettingsApplier.getColor_conv(context),factor_font);
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(fontsize);
                ((TextView) v).setBackgroundColor(SettingsApplier.getColor_background(context));
                ((TextView) v).setTypeface(FontSettingsActivity.getTypeFace(SettingsApplier.current_font_family,SettingsApplier.current_fontstlye));
                ((TextView) v).setTextColor(fontcolor);
                return v;
            }
        };
        return adapter;
    }

    private void initMaps(){
        //ins englische übersetzen
        if(language.equals("deutsch") || language.equals("german")){
            laenge = new LinkedHashSet<>(Arrays.asList(LaengeUmrechnung.getunitsDE()));
            zeit = new LinkedHashSet<>(Arrays.asList(ZeitUmrechnung.getunitsDE()));
            geschwindigkeit = new LinkedHashSet<>(Arrays.asList(GeschwindigkeitUmrechnung.getunitsDE()));
            masse = new LinkedHashSet<>(Arrays.asList(MasseUmrechnung.getunitsDE()));
            volumen = new LinkedHashSet<>(Arrays.asList(VolumenUmrechnung.getunitsDE()));
            fläche = new LinkedHashSet<>(Arrays.asList(FlächenUmrechnung.getunitsDE()));
            temperatur = new LinkedHashSet<>(Arrays.asList(TemperaturUmrechnung.getunitsDE()));
            kraft = new LinkedHashSet<>(Arrays.asList(KraftUmrechnung.getunitsDE()));
            leistung = new LinkedHashSet<>(Arrays.asList(LeistungUmrechnung.getunitsDE()));
            stromstärke = new LinkedHashSet<>(Arrays.asList(StromstärkeUmrechnung.getunitsDE()));
            datenspeicher = new LinkedHashSet<>(Arrays.asList(DatenspeicherUmrechnung.getunitsDE()));
            datentransfer = new LinkedHashSet<>(Arrays.asList(DatentransferUmrechnung.getunitsDE()));
        } else{
            laenge = new LinkedHashSet<>(Arrays.asList(LaengeUmrechnung.getunitsEN()));
            zeit = new LinkedHashSet<>(Arrays.asList(ZeitUmrechnung.getunitsEN()));
            geschwindigkeit = new LinkedHashSet<>(Arrays.asList(GeschwindigkeitUmrechnung.getunitsEN()));
            masse = new LinkedHashSet<>(Arrays.asList(MasseUmrechnung.getunitsEN()));
            volumen = new LinkedHashSet<>(Arrays.asList(VolumenUmrechnung.getunitsEN()));
            fläche = new LinkedHashSet<>(Arrays.asList(FlächenUmrechnung.getunitsEN()));
            temperatur = new LinkedHashSet<>(Arrays.asList(TemperaturUmrechnung.getunitsEN()));
            kraft = new LinkedHashSet<>(Arrays.asList(KraftUmrechnung.getunitsEN()));
            leistung = new LinkedHashSet<>(Arrays.asList(LeistungUmrechnung.getunitsEN()));
            stromstärke = new LinkedHashSet<>(Arrays.asList(StromstärkeUmrechnung.getunitsEN()));
            datenspeicher = new LinkedHashSet<>(Arrays.asList(DatenspeicherUmrechnung.getunitsEN()));
            datentransfer = new LinkedHashSet<>(Arrays.asList(DatentransferUmrechnung.getunitsEN()));
        }
    }

    private int checkValid(String me1, String me2, String e1val){
        int ERR=1;
        try {
            Double.parseDouble(e1val);
        } catch(NumberFormatException e){
            ERR = -1;
        }
        if(getSystem(me1,me2).equals(""))ERR = -2;

        if(ERR < 0){

            Toast t =  Toast.makeText(ConversionActivity.this,"unbekannter Fehler",Toast.LENGTH_LONG);
            if(ERR==-1){
                t =  Toast.makeText(ConversionActivity.this,"Eingabe nicht valide (ungültige Maßeingabe): "+e1val,Toast.LENGTH_LONG);
                eT_cur_const_val1.setText("");
                eT_cur_const_val2.setText("");
            }
            else if(ERR==-2){
                t =  Toast.makeText(ConversionActivity.this,"Eingabe nicht valide (Maßeinheiten stimmen nicht überein!)",Toast.LENGTH_LONG);
                btn_maßeinheit1.setText("");
                btn_maßeinheit2.setText("");
            }
            t.show();
        }

        return ERR;
    }

    static String getSystem(String source, String target){
        if(laenge.contains(source) && laenge.contains(target))return "laenge";
        else if(zeit.contains(source) && zeit.contains(target))return "Zeit";
        else if(geschwindigkeit.contains(source) && geschwindigkeit.contains(target))return "Geschwindigkeit";
        else if(masse.contains(source) && masse.contains(target))return "Masse";
        else if(volumen.contains(source) && volumen.contains(target))return "Volumen";
        else if(fläche.contains(source) && fläche.contains(target))return "Fläche";
        else if(temperatur.contains(source) && temperatur.contains(target))return "Temperatur";
        else if(kraft.contains(source) && kraft.contains(target))return "Kraft";
        else if(leistung.contains(source) && leistung.contains(target))return "Leistung";
        else if(stromstärke.contains(source) && stromstärke.contains(target))return "Stromstärke";
        else if(datenspeicher.contains(source) && datenspeicher.contains(target))return "Datenspeicher";
        else if(datentransfer.contains(source) && datentransfer.contains(target))return "Datentransfer";
        return "";
    }







    private static String convert(BigDecimal quantS, String sourceM, String targetM){
        if(quantS == null){
            return "";
        }

        String sys = getSystem(sourceM,targetM);
        try {
            switch (sys) {
                case "laenge": {
                    return LaengeUmrechnung.convert(quantS,sourceM,targetM).toString();
                }
                case "Zeit": {
                    return ZeitUmrechnung.convert(quantS,sourceM,targetM).toString();
                }
                case "Geschwindigkeit": {
                    return GeschwindigkeitUmrechnung.convert(quantS,sourceM,targetM).toString();
                }
                case "Masse": {
                    return MasseUmrechnung.convert(quantS,sourceM,targetM).toString();
                }
                case "Volumen": {
                    return VolumenUmrechnung.convert(quantS,sourceM,targetM).toString();
                }
                case "Fläche": {
                    return FlächenUmrechnung.convert(quantS,sourceM,targetM).toString();
                }
                case "Temperatur": {
                    return TemperaturUmrechnung.convert(quantS,sourceM,targetM).toString();
                }
                case "Kraft": {
                    return KraftUmrechnung.convert(quantS,sourceM,targetM).toString();
                }
                case "Leistung": {
                    return LeistungUmrechnung.convert(quantS,sourceM,targetM).toString();
                }
                case "Stromstärke": {
                    return StromstärkeUmrechnung.convert(quantS,sourceM,targetM).toString();
                }
                case "Datenspeicher": {
                    return DatenspeicherUmrechnung.convert(quantS,sourceM,targetM).toString();
                }
                case "Datentransfer": {
                    return DatentransferUmrechnung.convert(quantS,sourceM,targetM).toString();
                }
                default:
                    return "";
            }
        }
        catch (Exception e){
            return "";
        }
    }

    private void applySettings(){
        language = PreferenceManager.getDefaultSharedPreferences(ConversionActivity.this).getString("pref_lang","english");
        //Toast t =  Toast.makeText(SettingsActivity.this,"Lang: "+language,Toast.LENGTH_LONG);
        //t.show();
        if(language.equals("english") || language.equals("englisch")){
            btn_save.setText("SAVE");
            eT_cur_const_val1.setHint("Measure1");
            eT_cur_const_val2.setHint("Measure2");
            btn_maßeinheit1.setHint("Measure1");
            btn_maßeinheit2.setHint("Measure2");
        }
        else if(language.equals("german") || language.equals("deutsch")){
            btn_save.setText("speichern");
            eT_cur_const_val1.setHint("Maß1");
            eT_cur_const_val2.setHint("Maß2");
            btn_maßeinheit1.setHint("Maß1");
            btn_maßeinheit2.setHint("Maß2");
        }

        if (PreferenceManager.getDefaultSharedPreferences(ConversionActivity.this).contains("pref_precision")) {
            String prec = PreferenceManager.getDefaultSharedPreferences(ConversionActivity.this).getString("pref_precision","10");
            if(prec != null)precision =  Integer.valueOf(prec) + 1;
        }

        setBackgrounds();
        fontsize = SettingsApplier.getCurrentFontsize(this);
    }

    void setColor(Drawable background, int c, String füllung, int stroke){
        if (background instanceof ShapeDrawable) {
            // cast to 'ShapeDrawable'
            ShapeDrawable shapeDrawable = (ShapeDrawable) background;
            shapeDrawable.getPaint().setColor(c);
        } else if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(c);
            int rahmen_farbe = SettingsApplier.manipulateColor(c,0.7f);
            if(füllung.equals("leer"))gradientDrawable.setColor(SettingsApplier.getColor_background((ConversionActivity.this)));
            if(stroke!=0)gradientDrawable.setStroke(stroke, rahmen_farbe);
        } else if (background instanceof ColorDrawable) {
            // alpha value may need to be set again after this call
            ColorDrawable colorDrawable = (ColorDrawable) background;
            colorDrawable.setColor(c);
        }
        else Log.e("setColor Error","");

    }


    public static void main(String[] a){
        new ConversionActivity().initMaps();
        //System.out.println(convert(new BigDecimal("54"),"Meter","Meter"));


        //System.out.println(Arrays.toString(zeit.toArray()));
        //System.out.println(getSystem("Minute","Minute"));
        //System.out.println(dict_basiseinheiten.get(getSystem("Sekunde","Minute")));
        //System.out.println(baseToTarget(new BigDecimal("300"),"Minute"));
        //System.out.println(InitConvMap.getZeit().get("Minute"));


        System.out.println(convert(new BigDecimal("300"),"Sekunde","Minute"));
    }
}
