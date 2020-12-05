package com.example.titancalculator;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.example.titancalculator.helper.Math_String.StringUtils;

import java.util.Arrays;

/**
 * a subset of the
 */
public class PersistentModel {
    private String[] memory = new String[6];

    public String[] getMemory() {return memory;}
    public String getMemory(int index) {if(index<6 && index>=0)return memory[index]; else {System.out.println(index); assert(false); return "";}}
    public void setMemory(String[] memory) {if(memory.length == memory.length){this.memory = memory;saveMemory();}}
    public void setMemory(String mem, int index) {if(index<6 && index>=0){memory[index] = mem;saveMemory();}}

    public void PersistenModel(){
        loadMemory();
    }

    public void saveMemory() {
        if(MainActivity.getContext() != null){
            String MEMS = StringUtils.arrayToString(this.memory);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("MEMORY", MEMS);
            editor.commit();
        }

    }

    public String[] loadMemory() {
        String[] res = new String[6];
        if(MainActivity.getContext() != null){
            String MEMS = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext()).getString("MEMORY", "");
            String[] memarray = StringUtils.stringToArray(MEMS);
            for (int i = 0; i < memory.length; i++) {
                if (i < memarray.length) res[i] = memarray[i];
                else res[i] = "";
            }
        }
        return res;
    }




}
