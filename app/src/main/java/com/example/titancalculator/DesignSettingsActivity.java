package com.example.titancalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.titancalculator.helper.MainDisplay.SettingsApplier;
import com.example.titancalculator.helper.Math_String.NumberString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DesignSettingsActivity extends AppCompatActivity {

    static final String[] designs = {""};

    ArrayList<View> VIEW_ALL;
    LinearLayout design_background;
    ListView lv_design;
    TextView tv_selected_design;
    Button btn_save, btn_delete, btn_rename, btn_set;


    private String language;
    int buttonshapeID = R.drawable.buttonshape_square;
    String buttonfüllung = "voll";


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

        VIEW_ALL = new ArrayList<>(Arrays.asList(new View[]{tv_selected_design,btn_save,btn_delete,btn_rename,btn_set}));
        SettingsApplier.setFonts(this, VIEW_ALL);

        applySettings();
        //setBackgrounds();
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


        //buttonshape
        if (PreferenceManager.getDefaultSharedPreferences(DesignSettingsActivity.this).contains("buttonshape")) {
            String form = PreferenceManager.getDefaultSharedPreferences(DesignSettingsActivity.this).getString("buttonshape", "round");
            if (form != null) {
                switch (form) {
                    case "Round": {
                        buttonshapeID = R.drawable.buttonshape_round;
                        break;
                    }
                    case "Square": {
                        buttonshapeID = R.drawable.buttonshape_square;
                        break;
                    }
                }
            } else
                Toast.makeText(DesignSettingsActivity.this, "no buttonshape settings", Toast.LENGTH_SHORT).show();
        }

        //buttonfüllung
        if (PreferenceManager.getDefaultSharedPreferences(DesignSettingsActivity.this).contains("buttonfüllung")) {
            buttonfüllung = PreferenceManager.getDefaultSharedPreferences(DesignSettingsActivity.this).getString("buttonfüllung", "voll");
        }

        //Fonts
        //Typeface font = Typeface.createFromAsset(getAssets(), "Crashed Scoreboard.ttf");
        Typeface monospace = Typeface.create("MONOSPACE", Typeface.NORMAL);
        Typeface sansSerif = Typeface.create("SANS_SERIF", Typeface.NORMAL);
        Typeface serif = Typeface.create("SERIF", Typeface.NORMAL);
    }

    void setBackground(View x) {
        if (buttonshapeID == 0) applySettings();
        Drawable background;
        SettingsApplier.setColors(DesignSettingsActivity.this);
        float factor_font = 0.5f;
        boolean stroke = true;

        //Default Case
        background = getResources().getDrawable(buttonshapeID);
        CalcActivity_science.setColor((DesignSettingsActivity.this), background, SettingsApplier.getColor_specials(DesignSettingsActivity.this), buttonfüllung, stroke);
        int darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.getColor_specials(DesignSettingsActivity.this), factor_font);
        if (x instanceof Button) ((Button) x).setTextColor(darker);


        if (x.equals(tv_selected_design)) {
            background = getResources().getDrawable(buttonshapeID);
            CalcActivity_science.setColor((DesignSettingsActivity.this), background, SettingsApplier.getColor_display(DesignSettingsActivity.this), buttonfüllung, stroke);
            tv_selected_design.setTextColor(SettingsApplier.getColor_displaytext(DesignSettingsActivity.this));
            x.setBackground(background);
        }

        if (VIEW_ALL.contains(x)) {
            background = getResources().getDrawable(buttonshapeID);
            CalcActivity_science.setColor((DesignSettingsActivity.this), background, SettingsApplier.getColor_act(DesignSettingsActivity.this), buttonfüllung, stroke);
            darker = ButtonSettingsActivity.manipulateColor(SettingsApplier.getColor_numbers(DesignSettingsActivity.this), factor_font);
            if (x instanceof Button) ((Button) x).setTextColor(darker);
        }

        x.setBackground(background);

    }

    
    void setBackgrounds() {
        design_background.setBackgroundColor(SettingsApplier.getColor_background(DesignSettingsActivity.this));

        for (View v : VIEW_ALL) {
            setBackground(v);
        }
    }
}
