package com.example.titancalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class FunctionGroupSettingsActivity extends AppCompatActivity {

    private static String groups[] = new String[1];
    private static final String defaultgroup[] = new String[]{"BASIC","BASIC2","TRIGO","HYPER","LOGIC","STATISTIC","USER"};

    ListView lv_group;
    EditText tv_selected_group;
    Button btn_new;
    Button btn_delete;
    Button btn_rename;
    Button btn_default;
    Button btn_moveUp;
    Button btn_moveDown;

    String sel_group_name = "";


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
                groups = addElement(groups);
                ArrayAdapter adapter_uni = new ArrayAdapter<String>(getBaseContext(), R.layout.lvitem_layout, groups);
                lv_group.setAdapter(adapter_uni);
                savePref(FunctionGroupSettingsActivity.this);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inh = tv_selected_group.getText().toString();
                groups = removeElements(groups,inh);

                ArrayAdapter adapter_uni = new ArrayAdapter<String>(getBaseContext(), R.layout.lvitem_layout, groups);
                lv_group.setAdapter(adapter_uni);
                savePref(FunctionGroupSettingsActivity.this);
            }
        });

        btn_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inh = tv_selected_group.getText().toString();
                if(contains(defaultgroup,sel_group_name))return;
                for(int i=0; i<groups.length; i++){
                    if(groups[i].equals(sel_group_name)){
                        groups[i] = inh;
                    }
                }
                ArrayAdapter adapter_uni = new ArrayAdapter<String>(getBaseContext(), R.layout.lvitem_layout, groups);
                lv_group.setAdapter(adapter_uni);
                savePref(FunctionGroupSettingsActivity.this);
            }
        });

        btn_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToDefault();
                ArrayAdapter adapter_uni = new ArrayAdapter<String>(getBaseContext(), R.layout.lvitem_layout, groups);
                lv_group.setAdapter(adapter_uni);
                savePref(FunctionGroupSettingsActivity.this);
            }
        });

        btn_moveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inh = tv_selected_group.getText().toString();
                groups = moveElementOneUp(groups,inh);
                ArrayAdapter adapter_uni = new ArrayAdapter<String>(getBaseContext(), R.layout.lvitem_layout, groups);
                lv_group.setAdapter(adapter_uni);
                savePref(FunctionGroupSettingsActivity.this);
            }
        });

        btn_moveDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inh = tv_selected_group.getText().toString();
                groups = moveElementOneDown(groups,inh);
                ArrayAdapter adapter_uni = new ArrayAdapter<String>(getBaseContext(), R.layout.lvitem_layout, groups);
                lv_group.setAdapter(adapter_uni);
                savePref(FunctionGroupSettingsActivity.this);
            }
        });



    }

    private static void savePref(Context c){
        //Einstellung speichern
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(c);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("functiongroups", arrayToString(groups));
        editor.commit();
    }

    private static void setToDefault(){
        groups = Arrays.copyOf(defaultgroup,defaultgroup.length);
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

    private static String[] addElement(String[] input) {
        List<String> result = new LinkedList<>();

        int usergroupindex=1;
        for(String item : input){
            result.add(item);
        }
        while(result.contains("USER"+String.valueOf(usergroupindex))){
                ++usergroupindex;
        }

        result.add("newUsergroup"+String.valueOf(usergroupindex));

        input = new String[result.size()];
        for(int i=0; i<input.length; i++){
            input[i] = result.get(i);
        }

        return result.toArray(input);
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
        if(PreferenceManager.getDefaultSharedPreferences(c).getString("functiongroups", null) == null){
            setToDefault();
            savePref(c);
        }
        return stringToArray(PreferenceManager.getDefaultSharedPreferences(c).getString("functiongroups",  null));
    }

    public static String[] translateGroup(String[] groups, String language){
        List<String> result = new LinkedList<>();

        if(language.equals("german") || language.equals("deutsch")){
            for(int i=0; i<groups.length; i++){
                groups[i] = groups[i].replace("BASIC","STANDART");
                groups[i] = groups[i].replace("LOGIC","LOGISCHE");
                groups[i] = groups[i].replace("STATISTIC","STATISTIK");
                groups[i] = groups[i].replace("USER","NUTZER");
            }
        }


        groups = new String[result.size()];
        for(int i=0; i<groups.length; i++){
            groups[i] = result.get(i);
        }

        return groups;
    }
}
