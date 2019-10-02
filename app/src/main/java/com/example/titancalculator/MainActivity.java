package com.example.titancalculator;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    static String modes[] = {"normal","middle"};

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startMode(getMode());
        finish();
    }

    private String getMode(){
        Intent intent = getIntent();
        String swipeDir = intent.getStringExtra("swipeDir");
        String layout = intent.getStringExtra("layout");
        if(swipeDir == null || swipeDir.equals("") || layout == null || layout.equals("")){
            return "middle";
        } else {
            int i = posMode(layout);
            if(swipeDir.equals("left"))return modes[turnMod((i-1),modes.length)];
            if(swipeDir.equals("right"))return modes[turnMod((i+1),modes.length)];
        } return "middle";
    }

    private int posMode(String m){
        for(int i=0; i<modes.length; i++){
            if(modes[i].equals(m)){
                return i;
            }
        }
        return -1;
    }

    private void startMode(String mode){
        if(mode.equals("middle")){
            Intent conversionIntent = new Intent(MainActivity.this, CalcActivity_middle.class);
            startActivity(conversionIntent);
        }
        else {
            Intent conversionIntent = new Intent(MainActivity.this, CalcActivity_normal.class);
            startActivity(conversionIntent);
        }
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

}