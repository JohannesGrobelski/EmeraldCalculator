package com.example.titancalculator.geplanteFeatures;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.R;
import com.example.titancalculator.geplanteFeatures.notUsedHelper.ArrayUtils;
import com.example.titancalculator.geplanteFeatures.helper.MainDisplay.SettingsApplier;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    public static final String SAVEWORD_HITORY = "HISTORY";
    public static final int CAPACITY_HISTORY = 1000;
    private static ArrayList<String> history = new ArrayList<>();

    Button btn_save;
    Button btn_clear;

    View hist_background;
    ListView lv_verlauf;
    TextView tV_selection;
    String item = "";


    private String language;

    //Display
    private Float fontsize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(HistoryActivity.this);
        setContentView(R.layout.activity_verlauf);

        hist_background = findViewById(R.id.hist_background);
        btn_save = findViewById(R.id.btn_save);
        btn_clear = findViewById(R.id.btn_clear);
        lv_verlauf = findViewById(R.id.lv_verlauf);
        tV_selection = findViewById(R.id.tV_selection);

        applySettings();




        Intent myIntent = getIntent(); // gets the previously created intent
        //Toast.makeText(HistoryActivity.this, Arrays.toString(arrayVerlauf), Toast.LENGTH_LONG).show();

        setHistAdap();

        lv_verlauf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent=new Intent();
                intent.putExtra("RESULT_STRING", lv_verlauf.getItemAtPosition(position).toString().replace("=","").trim());
                setResult(RESULT_OK, intent);
                finish();
                /*
                item = lv_verlauf.getItemAtPosition(position).toString();
                tV_selection.setText(item);
                 */
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
                history = new ArrayList<String>(0);
                setHistAdap();
                saveHistory(HistoryActivity.this);
            }
        });


    }

    private void setHistAdap(){
        String[] arrayVerlauf = history.toArray(new String[history.size()]);
        SettingsApplier.setArrayAdapter(HistoryActivity.this,lv_verlauf,arrayVerlauf,SettingsApplier.getColor_hist(HistoryActivity.this));
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
       fontsize = SettingsApplier.getCurrentFontsize(this);
       setBackgrounds();
    }

    public static void init(Context context){
        String h = PreferenceManager.getDefaultSharedPreferences(context).getString(SAVEWORD_HITORY,"");
        //Toast.makeText(context, "load: "+h, Toast.LENGTH_SHORT).show();
        history =  ArrayUtils.stringToList(h);
    }


    public static void addHistory(Context context, String term, String answer){
        if(history == null)init(context);
        //if(!history.isEmpty() && ("= "+answer).equals(history.get(history.size()-1)))return;
        if(!history.isEmpty() && term.equals(history.get(history.size()-1)))return;
        history.add(term);
        //history.add("= "+answer);
        if(history.size() > CAPACITY_HISTORY){
            history = ArrayUtils.sublistLastN(history,CAPACITY_HISTORY);
        }
        history = saveHistory(context);
    }

    public static ArrayList<String> getHistory(){
        return history;
    }

    public static ArrayList<String> saveHistory(Context c){
        if(history == null)init(c);
        ArrayList<String> save = new ArrayList<>(history);
        if(history.size() > 10){
            save = new ArrayList<String>(history.subList(history.size()-10,history.size()-1));
        }
        history = save;
        String histString = ArrayUtils.listToString(save);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVEWORD_HITORY, histString);
        editor.commit();
        return history;
    }


    void setBackgrounds(){

        hist_background.setBackgroundColor(SettingsApplier.getColor_background(HistoryActivity.this));

        Drawable background;
        SettingsApplier.setColors(HistoryActivity.this);
        float factor_font = 0.5f;
        boolean stroke = true;

        int visual_unselect = SettingsApplier.manipulateColor(SettingsApplier.getColor_specials(HistoryActivity.this),factor_font);


        background = getResources().getDrawable(SettingsApplier.getButtonshapeID());
        SettingsApplier.setColor((HistoryActivity.this),background, SettingsApplier.getColor_hist(HistoryActivity.this),SettingsApplier.getButtonfüllung(),stroke);
        visual_unselect = SettingsApplier.manipulateColor(SettingsApplier.getColor_hist(HistoryActivity.this),factor_font);
        if(btn_save instanceof Button) ((Button) btn_save).setTextColor(visual_unselect);
        if(btn_clear instanceof Button) ((Button) btn_clear).setTextColor(visual_unselect);

        SettingsApplier.setFonts(HistoryActivity.this,btn_clear);
        SettingsApplier.setFonts(HistoryActivity.this,btn_save);

        btn_clear.setBackground(background);
        btn_save.setBackground(background);
    }

}
