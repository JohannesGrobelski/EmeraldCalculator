package com.example.titancalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.helper.MainDisplay.SettingsApplier;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {


    Button btn_save;
    ListView lv_verlauf;
    String item = "";
    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verlauf);
        applySettings();

        btn_save = findViewById(R.id.btn_set);
        lv_verlauf = findViewById(R.id.lv_verlauf);

        ArrayList<View> list = new ArrayList<View>() {{add(btn_save);}};
        SettingsApplier.setFonts(HistoryActivity.this,list);

        Intent myIntent = getIntent(); // gets the previously created intent
        String[] arrayVerlauf = myIntent.getStringArrayExtra("verlauf");
        //Toast.makeText(HistoryActivity.this, Arrays.toString(arrayVerlauf), Toast.LENGTH_LONG).show();

        ArrayAdapter<String> adapter_verlauf = new ArrayAdapter<String>(HistoryActivity.this, R.layout.spinner_shift_style, arrayVerlauf){
            float factor_font = 0.5f;
            int darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.color_fops,factor_font);
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(16);
                ((TextView) v).setTypeface(FontSettingsActivity.getTypeFace(SettingsApplier.current_font_family,SettingsApplier.current_fontstlye));
                ((TextView) v).setTextColor(darker);
                return v;
            }
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundResource(R.drawable.buttonshape_square);
                ((TextView) v).setTextColor(darker);
                ((TextView) v).setTypeface(FontSettingsActivity.getTypeFace(SettingsApplier.current_font_family,SettingsApplier.current_fontstlye));
                ((TextView) v).setGravity(Gravity.CENTER);
                return v;
            }
        };
        lv_verlauf.setAdapter(adapter_verlauf);

        lv_verlauf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                item = lv_verlauf.getItemAtPosition(position).toString();
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

    }

}
