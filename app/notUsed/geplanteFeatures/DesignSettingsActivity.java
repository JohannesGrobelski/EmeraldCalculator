package com.example.calcitecalculator.geplanteFeatures;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.calcitecalculator.R;
import com.example.calcitecalculator.geplanteFeatures.helper.MainDisplay.DesignApplier;
import com.example.calcitecalculator.geplanteFeatures.helper.MainDisplay.SettingsApplier;

import java.util.ArrayList;

public class DesignSettingsActivity extends AppCompatActivity {


    ArrayList<View> VIEW_ALL = new ArrayList<>();
    LinearLayout design_background;
    ListView lv_design;
    TextView tv_selected_design;
    Button btn_save, btn_delete, btn_rename, btn_set;


    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_settings);

        design_background = findViewById(R.id.design_background);
        lv_design = findViewById(R.id.lv_design);
        tv_selected_design = findViewById(R.id.tv_selected_design);
        btn_save = findViewById(R.id.btn_save);
        btn_delete = findViewById(R.id.btn_delete);
        btn_rename = findViewById(R.id.btn_rename);
        btn_set = findViewById(R.id.btn_set);

        VIEW_ALL.add(tv_selected_design);
        VIEW_ALL.add(btn_save);
        VIEW_ALL.add(btn_delete);
        VIEW_ALL.add(btn_rename);
        VIEW_ALL.add(btn_set);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(DesignSettingsActivity.this, R.layout.spinner_shift_style, DesignApplier.designs);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(DesignSettingsActivity.this, R.layout.spinner_shift_style, DesignApplier.designs)
                {

                    float factor_font = SettingsApplier.getDarker_factor_font(DesignSettingsActivity.this);
                    int darker = SettingsApplier.manipulateColor(Color.GRAY,factor_font);

                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);

                        ((TextView) v).setTextSize(20);
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
        lv_design.setAdapter(adapter);

        lv_design.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DesignApplier.apply_theme(DesignSettingsActivity.this, lv_design.getItemAtPosition(i).toString());
                tv_selected_design.setText(lv_design.getItemAtPosition(i).toString());
            }
        });

        applySettings();
        setBackgrounds();
    }

    public void setBackgrounds(){
        design_background.setBackgroundColor(Color.WHITE);
        for(View b: VIEW_ALL){
            SettingsApplier.setFonts(DesignSettingsActivity.this,b);
            SettingsApplier.setViewDesign(DesignSettingsActivity.this,b, Color.GRAY);
        }
    }

    private void applySettings() {
        //language
        language = PreferenceManager.getDefaultSharedPreferences(DesignSettingsActivity.this).getString("pref_lang", "english");
        if (language.equals("english") || language.equals("englisch")) {
            btn_save.setText(R.string.saveDesignEN);
            btn_delete.setText(R.string.deleteDesignEN);
            btn_rename.setText(R.string.renameDesignEN);
            btn_set.setText(R.string.setDesignEN);
        } else if (language.equals("german") || language.equals("deutsch")) {
            btn_save.setText(R.string.saveDesignDE);
            btn_delete.setText(R.string.deleteDesignDE);
            btn_rename.setText(R.string.renameDesignDE);
            btn_set.setText(R.string.setDesignDE);
        }

    }


}
