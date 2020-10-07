package com.example.titancalculator.geplanteFeatures;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;


public class ChooserActivity extends AppCompatActivity {

    public static ArrayList<String> modes = new ArrayList<>(Arrays.asList("science","normal","small"));
    String current_input="";
    String current_output="";
    String current_verlauf="";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mode = getMode();
        startMode(mode);
        finish();
    }

    private String getMode(){
        Intent intent = getIntent();
        String swipeDir = intent.getStringExtra("swipeDir");
        String layout = intent.getStringExtra("layout");
        current_input = intent.getStringExtra("input");
        current_output = intent.getStringExtra("output");
        current_verlauf = intent.getStringExtra("verlauf");
        //layout zurücksetzen
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChooserActivity.this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("layout", "");
            editor.commit();

        if(swipeDir == null || swipeDir.equals("") || layout == null || layout.equals("")){
            return loadLayout();
        } else {
            int i = posMode(layout);
            if(swipeDir.equals("left"))return modes.get(turnMod((i-1),modes.size()));
            else if(swipeDir.equals("right"))return modes.get(turnMod((i+1),modes.size()));
            else return modes.get(turnMod((i),modes.size()));
        }
    }

    private int posMode(String m){
        for(int i=0; i<modes.size(); i++){
            if(modes.get(i).equals(m)){
                return i;
            }
        }
        //exit
        Log.e("wrong mode",m);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        return -1;
    }

    private void startMode(String mode){
        saveLayout(mode);
        Intent conversionIntent;

        if(mode.equals("small")){
            conversionIntent = new Intent(ChooserActivity.this, CalcActivity_small.class);
        } else if(mode.equals("normal")){
            conversionIntent = new Intent(ChooserActivity.this, CalcActivity_normal.class);
        } else if(mode.equals("science")){
            conversionIntent = new Intent(ChooserActivity.this, CalcActivity_science.class);
        } else {
            return;
        }
        conversionIntent.putExtra("verlauf",current_verlauf);
        conversionIntent.putExtra("input",current_input);
        conversionIntent.putExtra("output",current_output);
        startActivity(conversionIntent);
    }

    private int turnMod(int x, int y){
        if(x >= 0)return x%y;
        else {
            while(x < 0){
                x += y;
            }
            return x%y;
        }
    }

    private void saveLayout(String layout){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ChooserActivity.this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("last_layout", layout);
        editor.commit();
        //Toast.makeText(MainActivity.this,"set font style to:"+inh,Toast.LENGTH_SHORT).show();
        return;
    }

    private String loadLayout(){
        String l = PreferenceManager.getDefaultSharedPreferences(ChooserActivity.this).getString("last_layout", "normal");
        return l;
    }

}