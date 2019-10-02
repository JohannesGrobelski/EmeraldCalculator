package com.example.titancalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.titancalculator.helper.MainDisplay.SettingsApplier;
import com.example.titancalculator.helper.Math_String.NumberString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FunctionGroupSettingsActivity extends AppCompatActivity {
    //Settings
    String language = "";


    private static String groups[] = new String[1];
    private static final String defaultgroup[] = new String[]{"BASIC","BASIC2","TRIGO","HYPER","LOGIC","STATISTIC"};

    ListView lv_group;
    EditText tv_selected_group;
    Button btn_new;
    Button btn_delete;
    Button btn_rename;
    Button btn_default;
    Button btn_moveUp;
    Button btn_moveDown;

    String sel_group_name = "";
    private int buttonshapeID;
    private String buttonfüllung;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_group_settings);

        lv_group = findViewById(R.id.lv_group);
        tv_selected_group  = findViewById(R.id.tv_selected_group);
        btn_new  = findViewById(R.id.btn_new);
        btn_delete  = findViewById(R.id.btn_delete);
        btn_rename  = findViewById(R.id.btn_rename);
        btn_default = findViewById(R.id.btn_default);
        btn_moveUp = findViewById(R.id.btn_moveUp);
        btn_moveDown = findViewById(R.id.btn_moveDown);


        groups = getGroups(FunctionGroupSettingsActivity.this);
        applySettings();

        ArrayAdapter adapter_uni = new ArrayAdapter<String>(this, R.layout.lvitem_layout, groups);
        lv_group.setAdapter(adapter_uni);

        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sel_group_name = lv_group.getItemAtPosition(i).toString();
                tv_selected_group.setText(sel_group_name);
            }
        });

        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGroup(tv_selected_group.getText().toString());
                tv_selected_group.setText("");
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delGroup(tv_selected_group.getText().toString());
                tv_selected_group.setText("");
            }
        });

        btn_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renameGroup(sel_group_name,tv_selected_group.getText().toString());
            }
        });

        btn_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groups = getDefault();
                ArrayAdapter adapter_uni = new ArrayAdapter<String>(getBaseContext(), R.layout.lvitem_layout, groups);
                lv_group.setAdapter(adapter_uni);
                savePref(FunctionGroupSettingsActivity.this,groups);
            }
        });

        btn_moveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inh = tv_selected_group.getText().toString();
                groups = moveElementOneUp(groups,inh);
                ArrayAdapter adapter_uni = new ArrayAdapter<String>(getBaseContext(), R.layout.lvitem_layout, groups);
                lv_group.setAdapter(adapter_uni);
                savePref(FunctionGroupSettingsActivity.this,groups);
            }
        });

        btn_moveDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inh = tv_selected_group.getText().toString();
                groups = moveElementOneDown(groups,inh);
                ArrayAdapter adapter_uni = new ArrayAdapter<String>(getBaseContext(), R.layout.lvitem_layout, groups);
                lv_group.setAdapter(adapter_uni);
                savePref(FunctionGroupSettingsActivity.this,groups);
            }
        });



    }

    private static void savePref(Context c,String[] Groups){
        //Einstellung speichern
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("functiongroups", arrayToString(Groups));
        editor.commit();
    }

    private static String[] getDefault(){
        return Arrays.copyOf(defaultgroup,defaultgroup.length);
    }

    private static String[] removeElements(String[] input, String deleteMe) {
        if(contains(defaultgroup,deleteMe)){
            return input;
        }
        List<String> result = new LinkedList<>();

        for(String item : input)
            if(!deleteMe.equals(item))
                result.add(item);

        input = new String[result.size()];
        for(int i=0; i<input.length; i++){
            input[i] = result.get(i);
        }

        return result.toArray(input);
    }

    private static String[] entailElement(String[] input,String e) {
        List<String> result = new LinkedList<>();
        for(String item : input){
            result.add(item);
        }
        result.add(e);
        input = new String[result.size()];
        for(int i=0; i<input.length; i++){
            input[i] = result.get(i);
        }
        return result.toArray(input);
    }

    private static String[] addElement(Context c,String[] input,String name) {
        List<String> result = new LinkedList<>();

        for(String item : input){
            result.add(item);
        }
        if(result.contains(name)){
            Toast.makeText(c,"Group already defined: "+name,Toast.LENGTH_SHORT).show();
           return input;
        } else{
            result.add(name);
        }

        input = new String[result.size()];
        for(int i=0; i<input.length; i++){
            input[i] = result.get(i);
        }

        return result.toArray(input);
    }

    private static boolean equals(String[] A, String[] B){
        if(A==null || B==null || A.length != B.length){
            return false;
        }
        for(int i=0; i<A.length; i++){
            if(!A[i].equals(B[i]))return false;
        }
        return true;
    }

    private static boolean contains(String ar[], String a){
        for(String s: ar){
            if(a.equals(s))return true;
        }
        return false;
    }

    private static String[] moveElementOneUp(String[] A, String e){
        for(int i=0; i<A.length; i++){
            if(A[i].equals(e) && i > 0){
                String tausch = A[i-1];
                A[i-1] = A[i];
                A[i] = tausch;
                break;
            }
        }
        return A;
    }

    private static String[] moveElementOneDown(String[] A, String e){
        for(int i=0; i<A.length; i++){
            if(A[i].equals(e) && i < A.length-1){
                String tausch = A[i+1];
                A[i+1] = A[i];
                A[i] = tausch;
                break;
            }
        }
        return A;
    }

    public static void main(String[] a){
        /*
        String test[] = {"a","b","c"};
        System.out.println(Arrays.toString(test));
        test = moveElementOneDown(test,"a");
        System.out.println(Arrays.toString(test));
        test = moveElementOneUp(test,"a");
        System.out.println(Arrays.toString(test));
         */
    }

    public static String arrayToString(String[] ar){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ar.length; i++) {
            sb.append(ar[i]).append(",");
        }
        return sb.toString();
    }

    public static String[] stringToArray(String A){
        return A.split(",");
    }

    public static String[] getGroups(Context c){
        String g;
        if((g = PreferenceManager.getDefaultSharedPreferences(c).getString("functiongroups", null)) == null){
            g = arrayToString(getDefault());
            savePref(c,stringToArray(g));
            return stringToArray(g);
        }
        else return stringToArray(g);
    }

    public static String[] getUserGroups(Context c){
        String UserGroups[] = new String[0];
        for(String g: getGroups(c)){
            if(!contains(defaultgroup,g)){
                UserGroups = entailElement(UserGroups,g);
            }
        }
        return UserGroups;
    }


    public static String[] translateGroup(String[] groups, String language){
        if(language.equals("german") || language.equals("deutsch")){
            for(int i=0; i<groups.length; i++){
                groups[i] = groups[i].replace("BASIC","STANDART");
                groups[i] = groups[i].replace("LOGIC","LOGISCHE");
                groups[i] = groups[i].replace("STATISTIC","STATISTIK");
                groups[i] = groups[i].replace("USER","NUTZER");
            }
        }
        return groups;
    }



    private void applySettings(){
        //language
        language = PreferenceManager.getDefaultSharedPreferences(FunctionGroupSettingsActivity.this).getString("pref_lang","english");
        if(language.equals("english") || language.equals("englisch")){
            btn_new.setText("new");
            btn_delete.setText("delete");
            btn_rename.setText("rename");
            btn_default.setText("default");
            btn_moveUp.setText("move up");
            btn_moveDown.setText("move down");
        }
        else if(language.equals("german") || language.equals("deutsch")){
            btn_new.setText("neu");
            btn_delete.setText("löschen");
            btn_rename.setText("umbennen");
            btn_default.setText("standart");
            btn_moveUp.setText("nach oben");
            btn_moveDown.setText("nach unten");
        }

        //buttonshape
        if (PreferenceManager.getDefaultSharedPreferences(FunctionGroupSettingsActivity.this).contains("buttonshape")) {
            String form = PreferenceManager.getDefaultSharedPreferences(FunctionGroupSettingsActivity.this).getString("buttonshape","round");
            if(form != null){
                switch(form){
                    case "Round": {
                        buttonshapeID = R.drawable.buttonshape_round;
                        break;
                    }
                    case "Square": {
                        buttonshapeID = R.drawable.buttonshape_square;
                        break;
                    }
                }
            }
            else Toast.makeText(FunctionGroupSettingsActivity.this,"no buttonshape settings",Toast.LENGTH_SHORT).show();
        }

        //buttonfüllung
        if (PreferenceManager.getDefaultSharedPreferences(FunctionGroupSettingsActivity.this).contains("buttonfüllung")) {
            buttonfüllung = PreferenceManager.getDefaultSharedPreferences(FunctionGroupSettingsActivity.this).getString("buttonfüllung","voll");
        }

        lv_group = findViewById(R.id.lv_group);
        tv_selected_group  = findViewById(R.id.tv_selected_group);
        setBackground(btn_new,SettingsApplier.color_act);
        setBackground(btn_delete,SettingsApplier.color_act);
        setBackground(btn_rename,SettingsApplier.color_act);
        setBackground(btn_default,SettingsApplier.color_act);
        setBackground(btn_moveUp,SettingsApplier.color_fkt);
        setBackground(btn_moveDown,SettingsApplier.color_fkt);

        //Fonts
        //Typeface font = Typeface.createFromAsset(getAssets(), "Crashed Scoreboard.ttf");
        ArrayList<View> list = new ArrayList<View>() {{add(btn_new);add(btn_delete);add(btn_rename);add(btn_default);add(btn_moveUp);add(btn_moveDown);}};

        SettingsApplier.setFonts(FunctionGroupSettingsActivity.this,list);
    }

    public void setBackground(View v, int color){
        Drawable background = getResources().getDrawable(buttonshapeID);
        MainActivity.setColor(background, color,buttonfüllung,true);
        int darker = ButtonSettingsActivity.manipulateColor(color,0.6f);
        if(v instanceof Button) ((Button) v).setTextColor(darker);
    }

    private void newGroup(String name){
        if(name.isEmpty())return;
        String g[] = addElement(FunctionGroupSettingsActivity.this,groups,name);
        if(equals(g,groups))return;
        else groups = g;

        ArrayAdapter adapter_uni = new ArrayAdapter<String>(getBaseContext(), R.layout.lvitem_layout, groups);
        lv_group.setAdapter(adapter_uni);
        savePref(FunctionGroupSettingsActivity.this,groups);

        //Preference Gruppe erzeugen
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(FunctionGroupSettingsActivity.this);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(name+"_btn11", "btn11");
        editor.putString(name+"_btn12", "btn12");
        editor.putString(name+"_btn13", "btn13");
        editor.putString(name+"_btn14", "btn14");
        editor.putString(name+"_btn15", "btn15");
        editor.putString(name+"_btn16", "btn16");
        
        editor.putString(name+"_btn11", "btn21");
        editor.putString(name+"_btn12", "btn22");
        editor.putString(name+"_btn13", "btn23");
        editor.putString(name+"_btn14", "btn24");
        editor.putString(name+"_btn15", "btn25");
        editor.putString(name+"_btn16", "btn26");
        /*
        for(int i=1; i<=2; i++){
            for(int j=1; j<=6; j++){
                String key = name+"_btn"+String.valueOf(i)+String.valueOf(j);
                //Einstellung speichern
                editor.putString(key, "btn"+String.valueOf(i)+String.valueOf(j));
            }
        }
        */

        editor.commit();
    }

    private void delGroup(String name) {
        groups = removeElements(groups,name);
        ArrayAdapter adapter_uni = new ArrayAdapter<String>(getBaseContext(), R.layout.lvitem_layout, groups);
        lv_group.setAdapter(adapter_uni);
        savePref(FunctionGroupSettingsActivity.this,groups);

        //Preference Gruppe löschen
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 6; j++) {
                String key = name + "_btn" + String.valueOf(i) + String.valueOf(j);
                //Einstellung speichern
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(FunctionGroupSettingsActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(key);
                editor.commit();
            }
        }
    }

    private void renameGroup(String old,String neu){
        if(contains(defaultgroup,sel_group_name))return;
        for(int i=0; i<groups.length; i++){
            if(groups[i].equals(sel_group_name)){
                groups[i] = neu;
            }
        }
        ArrayAdapter adapter_uni = new ArrayAdapter<String>(getBaseContext(), R.layout.lvitem_layout, groups);
        lv_group.setAdapter(adapter_uni);
        savePref(FunctionGroupSettingsActivity.this,groups);

        //Preference Gruppe löschen
        //Einstellung speichern
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(FunctionGroupSettingsActivity.this);
        SharedPreferences.Editor editor = preferences.edit();
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 6; j++) {
                String oldkey = old + "_btn" + String.valueOf(i) + String.valueOf(j);
                String newkey = old + "_btn" + String.valueOf(i) + String.valueOf(j);
                String oldvalue = PreferenceManager.getDefaultSharedPreferences(FunctionGroupSettingsActivity.this).getString(oldkey,"btn" + String.valueOf(i) + String.valueOf(j));

                editor.putString(newkey, oldvalue);
                editor.remove(oldkey);
            }
        }
        editor.commit();
    }


}
