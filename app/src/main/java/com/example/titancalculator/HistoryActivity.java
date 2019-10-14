package com.example.titancalculator;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
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

import com.example.titancalculator.helper.MainDisplay.SettingsApplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HistoryActivity extends AppCompatActivity {


    Button btn_save;
    Button btn_clear;

    View hist_background;
    ListView lv_verlauf;
    TextView tV_selection;
    String item = "";


    private String language;

    //Display
    int buttonshapeID = R.drawable.buttonshape_square;
    private String buttonfüllung = "voll";
    private Float fontsize;

    String[] arrayVerlauf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verlauf);

        hist_background = findViewById(R.id.hist_background);
        btn_save = findViewById(R.id.btn_save);
        btn_clear = findViewById(R.id.btn_clear);
        lv_verlauf = findViewById(R.id.lv_verlauf);
        tV_selection = findViewById(R.id.tV_selection);

        applySettings();


        ArrayList<View> list = new ArrayList<View>() {{add(btn_save);add(btn_clear);}};
        SettingsApplier.setFonts(HistoryActivity.this,list);

        Intent myIntent = getIntent(); // gets the previously created intent
        arrayVerlauf = myIntent.getStringArrayExtra("history");
        //Toast.makeText(HistoryActivity.this, Arrays.toString(arrayVerlauf), Toast.LENGTH_LONG).show();
        if(arrayVerlauf == null)arrayVerlauf=new String[0];

        setHistAdap();

        lv_verlauf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                item = lv_verlauf.getItemAtPosition(position).toString();
                tV_selection.setText(item);
            }

        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("RESULT_STRING", item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> history = new ArrayList<String>(0);
                arrayVerlauf =  CalcActivity_science.saveHistory(HistoryActivity.this,history).toArray(new String[0]);
                setHistAdap();
            }
        });
    }

    private void setHistAdap(){
        ArrayAdapter<String> adapter_verlauf = new ArrayAdapter<String>(HistoryActivity.this, R.layout.spinner_shift_style, arrayVerlauf){
            float factor_font = 0.5f;
            int darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.getColor_hist(HistoryActivity.this),factor_font);
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(fontsize);
                ((TextView) v).setBackgroundColor(SettingsApplier.getColor_background(HistoryActivity.this));
                ((TextView) v).setTypeface(FontSettingsActivity.getTypeFace(SettingsApplier.current_font_family,SettingsApplier.current_fontstlye));
                ((TextView) v).setTextColor(darker);
                return v;
            }
        };
        lv_verlauf.setAdapter(adapter_verlauf);
    }

    private void applySettings(){
        language = PreferenceManager.getDefaultSharedPreferences(HistoryActivity.this).getString("pref_lang","english");
        //Toast t =  Toast.makeText(SettingsActivity.this,"Lang: "+language,Toast.LENGTH_LONG);
        //t.show();
        if(language.equals("english") || language.equals("englisch")){
            btn_save.setText("SAVE");
        }
        else if(language.equals("german") || language.equals("deutsch")){
            btn_save.setText("speichern");
        }

        //DisplaySettings
        //buttonshape
        buttonshapeID = SettingsApplier.getButtonshapeID();


        //buttonfüllung
        if (PreferenceManager.getDefaultSharedPreferences(HistoryActivity.this).contains("buttonfüllung")) {
            buttonfüllung = PreferenceManager.getDefaultSharedPreferences(HistoryActivity.this).getString("buttonfüllung","voll");
        }

        fontsize = SettingsApplier.getCurrentFontsize(this);

        setBackgrounds();
    }



    void setBackgrounds(){

        hist_background.setBackgroundColor(SettingsApplier.getColor_background(HistoryActivity.this));

        Drawable background;
        SettingsApplier.setColors(HistoryActivity.this);
        float factor_font = 0.5f;
        boolean stroke = true;

        int visual_unselect = ButtonSettingsActivity.manipulateColor(SettingsApplier.getColor_specials(HistoryActivity.this),factor_font);


        background = getResources().getDrawable(buttonshapeID);
        SettingsApplier.setColor((HistoryActivity.this),background, SettingsApplier.getColor_hist(HistoryActivity.this),buttonfüllung,stroke);
        visual_unselect = ButtonSettingsActivity.manipulateColor(SettingsApplier.getColor_hist(HistoryActivity.this),factor_font);
        if(btn_save instanceof Button) ((Button) btn_save).setTextColor(visual_unselect);
        if(btn_clear instanceof Button) ((Button) btn_clear).setTextColor(visual_unselect);

        btn_clear.setBackground(background);
        btn_save.setBackground(background);
    }

}
