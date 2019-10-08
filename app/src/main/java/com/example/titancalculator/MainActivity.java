package com.example.titancalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;


public class MainActivity extends AppCompatActivity {

    static String modes[] = {"science","normal","small"};
    String current_input="";
    String current_output="";
    String current_verlauf="";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mode = getMode();
        Log.e("mode",mode);
        Toast.makeText(this,"MODE: "+mode,Toast.LENGTH_LONG).show();
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
        if(swipeDir == null || swipeDir.equals("") || layout == null || layout.equals("")){
            return loadLayout();
        } else {
            int i = posMode(layout);
            if(swipeDir.equals("left"))return modes[turnMod((i-1),modes.length)];
            else if(swipeDir.equals("right"))return modes[turnMod((i+1),modes.length)];
            else return modes[turnMod((i),modes.length)];
        }
    }

    private int posMode(String m){
        for(int i=0; i<modes.length; i++){
            if(modes[i].equals(m)){
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
            conversionIntent = new Intent(MainActivity.this, CalcActivity_small.class);
        } else if(mode.equals("normal")){
            conversionIntent = new Intent(MainActivity.this, CalcActivity_normal.class);
        } else {
            conversionIntent = new Intent(MainActivity.this, CalcActivity_science.class);
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("last_layout", layout);
        editor.commit();
        //Toast.makeText(MainActivity.this,"set font style to:"+inh,Toast.LENGTH_SHORT).show();
        return;
    }

    private String loadLayout(){
        String l = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("last_layout", "normal");
        return l;
    }

}