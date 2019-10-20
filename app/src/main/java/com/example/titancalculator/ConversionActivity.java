package com.example.titancalculator;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.helper.MainDisplay.SettingsApplier;
import com.example.titancalculator.helper.Umrechnung.DatenspeicherUmrechnung;
import com.example.titancalculator.helper.Umrechnung.DatentransferUmrechnung;
import com.example.titancalculator.helper.Umrechnung.FlächenUmrechnung;
import com.example.titancalculator.helper.Umrechnung.GeschwindigkeitUmrechnung;
import com.example.titancalculator.helper.Umrechnung.KraftUmrechnung;
import com.example.titancalculator.helper.Umrechnung.LaengeUmrechnung;
import com.example.titancalculator.helper.Umrechnung.LeistungUmrechnung;
import com.example.titancalculator.helper.Umrechnung.MasseUmrechnung;
import com.example.titancalculator.helper.Umrechnung.StromstärkeUmrechnung;
import com.example.titancalculator.helper.Umrechnung.TemperaturUmrechnung;
import com.example.titancalculator.helper.Umrechnung.VolumenUmrechnung;
import com.example.titancalculator.helper.Umrechnung.ZeitUmrechnung;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class ConversionActivity extends AppCompatActivity {
    String[] measAr = {"laenge","Zeit","Geschwindigkeit","Masse","Volumen","Fläche","Temperatur","Kraft","Leistung","Stromstärke","Datenspeicher","Datentransfer"};
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
    
    String zustand = "kategorie";


    int currentAuswahlKategorie = 0;
    int currentAuswahlKonstante = 0;

    String currentConv="";

    View conv_background;
    ListView LV_Auswahl;
    Button btn_back;

    EditText eT_cur_const_val1,eT_cur_const_val2;
    Button btn_maßeinheit1, btn_me1_me2;
    Button btn_maßeinheit2, btn_me2_me1;

    Button selected;
    Button btn_save;

    private int buttonshapeID = R.drawable.buttonshape_square;
    private String buttonfüllung = "voll";
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

        if (cat.equals("laenge")) {
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
            currentSet = volumen;
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
        ArrayAdapter<String> adapter_cat = new ArrayAdapter<String>(this, R.layout.spinner_shift_style, a){
            int color_font = SettingsApplier.manipulateColor(SettingsApplier.getColor_conv(ConversionActivity.this),SettingsApplier.darker_factor_font);
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(fontsize);
                ((TextView) v).setBackgroundColor(SettingsApplier.getColor_background(ConversionActivity.this));
                ((TextView) v).setTypeface(FontSettingsActivity.getTypeFace(SettingsApplier.current_font_family,SettingsApplier.current_fontstlye));
                ((TextView) v).setTextColor(color_font);
                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundResource(buttonshapeID);
                int darker = Math.round(SettingsApplier.getColor_conv(ConversionActivity.this) * SettingsApplier.darker_factor_font);
                SettingsApplier.setTextColor(v,darker);
                ((TextView) v).setBackgroundColor(SettingsApplier.getColor_background(ConversionActivity.this));
                ((TextView) v).setTypeface(FontSettingsActivity.getTypeFace(SettingsApplier.current_font_family,SettingsApplier.current_fontstlye));
                ((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }
        };
        LV_Auswahl.setBackgroundColor(SettingsApplier.getColor_background(ConversionActivity.this));
        LV_Auswahl.setAdapter(adapter_cat);
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
        Drawable background_fkt = getResources().getDrawable(buttonshapeID);
        setBackground(x);
        setColor(background_fkt,SettingsApplier.getColor_conv(ConversionActivity.this),buttonfüllung,7);

        x.setBackground(background_fkt);
    }

    void visual_unselect(Button x){
        if(x==null)return;
        Drawable background_fkt = getResources().getDrawable(buttonshapeID);
        setBackground(x);
        setColor(background_fkt,SettingsApplier.getColor_conv(ConversionActivity.this),buttonfüllung,12);

        x.setBackground(background_fkt);
    }

    void setBackground(View x){

        Drawable background;
        SettingsApplier.setColors(ConversionActivity.this);
        float factor_font = SettingsApplier.darker_factor_font;
        boolean stroke = true;

        //Default Case
        background = getResources().getDrawable(buttonshapeID);
        SettingsApplier.setColor(ConversionActivity.this,background, SettingsApplier.getColor_specials(ConversionActivity.this),buttonfüllung,stroke);
        int visual_unselect = SettingsApplier.manipulateColor(SettingsApplier.getColor_specials(ConversionActivity.this),factor_font);
        if(x instanceof Button) ((Button) x).setTextColor(visual_unselect);

        if(x instanceof EditText){
            int fontcolor = SettingsApplier.manipulateColor(SettingsApplier.getColor_conv(ConversionActivity.this),factor_font);
            SettingsApplier.setTextColor(x,fontcolor);
        }

        if(VIEW_CONV.contains(x)){
            background = getResources().getDrawable(buttonshapeID);
            SettingsApplier.setColor(ConversionActivity.this,background, SettingsApplier.getColor_conv(ConversionActivity.this),buttonfüllung,stroke);
            visual_unselect = SettingsApplier.manipulateColor(SettingsApplier.getColor_conv(ConversionActivity.this),factor_font);
            if(x instanceof Button) ((Button) x).setTextColor(visual_unselect);
        }


        x.setBackground(background);
    }

    void setBackgrounds(){
        conv_background.setBackgroundColor(SettingsApplier.getColor_background(ConversionActivity.this));
        for(View v: VIEW_CONV){
            setBackground(v);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        applySettings();

        try {
            CalcActivity_science.setBackgroundImage(ConversionActivity.this,conv_background);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);

        initMaps();

        conv_background = findViewById(R.id.conv_background);
        LV_Auswahl = findViewById(R.id.LV_Auswahl);
        btn_back = findViewById(R.id.btn_back);
        btn_save = findViewById(R.id.btn_save);


        eT_cur_const_val1 = findViewById(R.id.eT_cur_const_val1);
        eT_cur_const_val2 = findViewById(R.id.eT_cur_const_val2);
        btn_maßeinheit1 = findViewById(R.id.btn_maßeinheit1);
        btn_me1_me2 = findViewById(R.id.btn_me1_me2);
        btn_maßeinheit2 = findViewById(R.id.btn_maßeinheit2);
        btn_me2_me1 = findViewById(R.id.btn_me2_me1);

        VIEW_CONV = new HashSet<View>(Arrays.asList(btn_back,eT_cur_const_val1,eT_cur_const_val2,btn_maßeinheit1,btn_maßeinheit2,btn_me1_me2,btn_me2_me1,btn_save));
        ArrayList<View> list = new ArrayList<View>() {{addAll(VIEW_CONV);}};
        SettingsApplier.setFonts(ConversionActivity.this,list);



        //ArrayAdapter adapter_Meas = new ArrayAdapter<String>(this,R.layout.lvitem_layout, measAr);
        ArrayAdapter<String> adapter_Meas = new ArrayAdapter<String>(this, R.layout.spinner_shift_style, measAr){
            float factor_font = 0.5f;
            int fontcolor = SettingsApplier.manipulateColor(SettingsApplier.getColor_conv(ConversionActivity.this),factor_font);
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(fontsize);
                ((TextView) v).setBackgroundColor(SettingsApplier.getColor_background(ConversionActivity.this));
                ((TextView) v).setTypeface(FontSettingsActivity.getTypeFace(SettingsApplier.current_font_family,SettingsApplier.current_fontstlye));
                ((TextView) v).setTextColor(fontcolor);
                return v;
            }

        };
        LV_Auswahl.setBackgroundColor(SettingsApplier.getColor_background(ConversionActivity.this));
        LV_Auswahl.setAdapter(adapter_Meas);

        applySettings();

        btn_maßeinheit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(btn_maßeinheit1);
            }
        });
        btn_maßeinheit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select(btn_maßeinheit2);
            }
        });
        LV_Auswahl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if(zustand.equals("kategorie")){
                    setUpMeasure((String) LV_Auswahl.getItemAtPosition(position));
                    currentAuswahlKategorie = position;
                    zustand = "konstante";
                } else if(zustand.equals("konstante")){

                    //currentAuswahlKonstante=position;
                    //result = currentSet.get(LV_Auswahl.getItemAtPosition(position));

                    if(selected != null){
                        if(selected.equals(btn_maßeinheit1) || selected.equals(btn_maßeinheit2)) {
                            selected.setText(LV_Auswahl.getItemAtPosition(position).toString());
                        }
                    }

                }
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(zustand.equals("kategorie")){
                } else if(zustand.equals("konstante")){
                    //ArrayAdapter adapter_uni = new ArrayAdapter<String>(ConversionActivity.this, R.layout.lvitem_layout, measAr);
                    ArrayAdapter<String> adapter_uni = new ArrayAdapter<String>(ConversionActivity.this, R.layout.spinner_shift_style, measAr){
                        float factor_font = 0.5f;
                        int color_font = SettingsApplier.manipulateColor(SettingsApplier.getColor_conv(ConversionActivity.this),factor_font);
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);
                            ((TextView) v).setTextSize(fontsize);
                            ((TextView) v).setBackgroundColor(SettingsApplier.getColor_background(ConversionActivity.this));
                            ((TextView) v).setTypeface(FontSettingsActivity.getTypeFace(SettingsApplier.current_font_family,SettingsApplier.current_fontstlye));
                            ((TextView) v).setTextColor(color_font);
                            return v;
                        }

                    };
                    LV_Auswahl.setAdapter(adapter_uni);

                    zustand = "kategorie";

                    //nur zur sicherheit ...


                }
            }
        });

        btn_me1_me2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String me1 = btn_maßeinheit1.getText().toString();
                String me2 = btn_maßeinheit2.getText().toString();
                String e1val = eT_cur_const_val1.getText().toString();
                if(checkValid(me1,me2,e1val)>0){
                    String target = convert(new BigDecimal(e1val),me1,me2);
                    eT_cur_const_val2.setText(target);
                    currentConv = target;
                }
            }
        });
        btn_me2_me1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String me1 = btn_maßeinheit1.getText().toString();
                String me2 = btn_maßeinheit2.getText().toString();
                String e2val = eT_cur_const_val2.getText().toString();
                if(checkValid(me1,me2,e2val)>0){
                    String target = convert(new BigDecimal(e2val),me2,me1);

                    com.example.titancalculator.evalex.Expression e = new com.example.titancalculator.evalex.Expression(target);
                    e.setPrecision(precision);
                    try{
                        target = e.eval().toString();
                    } catch (Exception ex){
                    }


                    eT_cur_const_val1.setText(target);
                    currentConv = target;
                }
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


        try {
            CalcActivity_science.setBackgroundImage(ConversionActivity.this,conv_background);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initMaps(){
        laenge = new LinkedHashSet<>(Arrays.asList(LaengeUmrechnung.getUnits()));
        zeit = new LinkedHashSet<>(Arrays.asList(ZeitUmrechnung.getUnits()));
        geschwindigkeit = new LinkedHashSet<>(Arrays.asList(GeschwindigkeitUmrechnung.getUnits()));
        masse = new LinkedHashSet<>(Arrays.asList(MasseUmrechnung.getUnits()));
        volumen = new LinkedHashSet<>(Arrays.asList(VolumenUmrechnung.getUnits()));
        fläche = new LinkedHashSet<>(Arrays.asList(FlächenUmrechnung.getUnits()));
        temperatur = new LinkedHashSet<>(Arrays.asList(TemperaturUmrechnung.getUnits()));
        kraft = new LinkedHashSet<>(Arrays.asList(KraftUmrechnung.getUnits()));
        leistung = new LinkedHashSet<>(Arrays.asList(LeistungUmrechnung.getUnits()));
        stromstärke = new LinkedHashSet<>(Arrays.asList(StromstärkeUmrechnung.getUnits()));
        datenspeicher = new LinkedHashSet<>(Arrays.asList(DatenspeicherUmrechnung.getUnits()));
        datentransfer = new LinkedHashSet<>(Arrays.asList(DatentransferUmrechnung.getUnits()));

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
        else if(temperatur.contains(source) && temperatur.contains(target))return "Temperatur";
        else if(kraft.contains(source) && kraft.contains(target))return "Kraft";
        else if(leistung.contains(source) && leistung.contains(target))return "Leistung";
        else if(stromstärke.contains(source) && stromstärke.contains(target))return "Stromstärke";
        else if(datenspeicher.contains(source) && datenspeicher.contains(target))return "Datenspeicher";
        else if(datenspeicher.contains(source) && datenspeicher.contains(target))return "Datentransfer";
        return "";
    }







    private static String convert(BigDecimal quantS, String sourceM, String targetM){
        if(quantS == null)return "";

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
            btn_back.setText("back");
            btn_save.setText("SAVE");
            eT_cur_const_val1.setHint("Measure1");
            eT_cur_const_val2.setHint("Measure2");
            btn_maßeinheit1.setText("Measure1");
            btn_maßeinheit2.setText("Measure2");
        }
        else if(language.equals("german") || language.equals("deutsch")){
            btn_back.setText("zurück");
            btn_save.setText("speichern");
            eT_cur_const_val1.setHint("Maß1");
            eT_cur_const_val2.setHint("Maß2");
            btn_maßeinheit1.setText("Maß1");
            btn_maßeinheit2.setText("Maß2");
        }

        //buttonshape
        buttonshapeID = SettingsApplier.getButtonshapeID();


        //buttonfüllung
        if (PreferenceManager.getDefaultSharedPreferences(ConversionActivity.this).contains("buttonfüllung")) {
            buttonfüllung = PreferenceManager.getDefaultSharedPreferences(ConversionActivity.this).getString("buttonfüllung","voll");
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
        initMaps();
        //System.out.println(convert(new BigDecimal("54"),"Meter","Meter"));


        //System.out.println(Arrays.toString(zeit.toArray()));
        //System.out.println(getSystem("Minute","Minute"));
        //System.out.println(dict_basiseinheiten.get(getSystem("Sekunde","Minute")));
        //System.out.println(baseToTarget(new BigDecimal("300"),"Minute"));
        //System.out.println(InitConvMap.getZeit().get("Minute"));


        System.out.println(convert(new BigDecimal("300"),"Sekunde","Minute"));
    }
}
