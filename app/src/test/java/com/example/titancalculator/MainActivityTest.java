package com.example.titancalculator;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.internal.IShadow;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest {
    Map<String, View> idToViewMap = new HashMap<>();

    /**
     * Terms that involve +,-,*,/
     */
    @Test public void testSimpleTerms(){
        for(int i=0; i<10; i++){
            int op1 = (int) (Math.random()*1000);
            int op2 = (int) (Math.random()*1000);
            //Check the addition of two integer numbers.
            assertTrue(testTerm(op1+"+"+op2,String.valueOf(op1+op2)));
            //Check the subtraction of two integer numbers.
            assertTrue(testTerm(op1+"-"+op2,String.valueOf(op1-op2)));
            //Check the multiplication of two integer numbers.
            assertTrue(testTerm(op1+"*"+op2,String.valueOf(op1*op2)));
            //Check the division of two integer numbers.
            assertTrue(testTerm(op1+"/"+op2,String.valueOf(op1/op2)));
        }

        for(int i=0; i<10; i++){
            double op1 = Math.random()*10;
            double op2 = Math.random()*10;
            //Check the addition of two negative numbers.
            assertTrue(testTerm("-"+op1+"+-"+op2,String.valueOf(-op1+-op2)));
            //Check the addition of one positive and one negative number.
            assertTrue(testTerm("-"+op1+"+"+op2,String.valueOf(-op1+op2)));
            //Check the subtraction of two negative numbers.
            assertTrue(testTerm("-"+op1+"--"+op2,String.valueOf(-op1-(-op2))));
            //Check the subtraction of one negative and one positive number.
            assertTrue(testTerm(+op1+"--"+op2,String.valueOf(op1-(-op2))));
            //Check the multiplication of two negative numbers.
            assertTrue(testTerm("-"+op1+"*-"+op2,String.valueOf(-op1*-op2)));
            //Check the multiplication of one negative and one positive number.
            assertTrue(testTerm("-"+op1+"*"+op2,String.valueOf(-op1*op2)));
            //Check the division of two negative numbers.
            assertTrue(testTerm("-"+op1+"/-"+op2,String.valueOf(-op1/-op2)));
            //Check the division of one positive number and one integer number.
            int op3 = (int) (Math.random()*1000);
            assertTrue(testTerm("-"+op1+"/"+op3,String.valueOf(-op1/op3)));
            //Check the division of a number by zero.
            assertTrue(testTerm(op1+"/0","Math Error"));
            //Check the division of a number by negative number.
            assertTrue(testTerm("-"+op1+"/-"+op2,String.valueOf(-op1/-op2)));
            //Check the division of zero by any number.
            assertTrue(testTerm("0/"+op1,"0"));
        }



            //assertTrue(testTerm("³√77=4.254320865115005776"));
    }


    @Test public void complexTerms() {

        for (int i = 0; i < 10; i++) {
            double op1 = (Math.random() * 1000) - 500;
            //e^ln(x) = x
            assertTrue(testTerm("E^LN("+op1+")",String.valueOf(op1)));

        }
    }



    private boolean testTerm(String term, String result) {
        return testTerm(term+"="+result);
    }

    private boolean testTerm(String equation){
        assertTrue(equation.contains("="));
        String term = equation.split("=")[0]; String expectedResult = equation.split("=")[1];
        assertTrue(expectedResult.matches("Math Error") || expectedResult.matches("[0-9]+") || expectedResult.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"));

        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        initMapIdToView(mainActivity);

        for(String s: term.split("")){
            if(idToViewMap.containsKey(s) && (idToViewMap.get(s) instanceof Button)){
                idToViewMap.get(s).performClick();
            } else {Log.e("testTerm", "Button doesnt exist"); return false;}
        }

        mainActivity.findViewById(R.id.btn_eq).performClick();
        assertEquals(((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString(),term);
        String output = ((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString();
        if(output.contains("E")){
            mainActivity.findViewById(R.id.btn_eq).performLongClick();
            output = ((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString();
        }
        if(!output.equals("Math Error")){
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(100);
            expectedResult = df.format(Double.valueOf(expectedResult));
        }


        int minlength = Math.min(Math.min(expectedResult.length(),output.length()),20);

        if(output.equals("Math Error")){
            return expectedResult.equals(output);
        } else {
            double outputD = Double.valueOf(output.substring(0,minlength));
            double expectedD = Double.valueOf(expectedResult.substring(0,minlength));
            System.out.println(outputD+" "+expectedD);
            assertTrue(Math.abs(outputD - expectedD) < 0.00000000001);
        }
        return true;
    }

    @Test public void addNumbers(){
        //Setup conditions
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);

        //execute code
        mainActivity.findViewById(R.id.btn_1).performClick();
        mainActivity.findViewById(R.id.btn_add).performClick();
        mainActivity.findViewById(R.id.btn_1).performClick();
        mainActivity.findViewById(R.id.btn_eq).performClick();

        //make assertions
        assertEquals(((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString(),"1+1");
        assertEquals(((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString(),"2");
    }

    private void initMapIdToView(MainActivity mainActivity){
        idToViewMap.put("1",mainActivity.findViewById(R.id.btn_1));
        idToViewMap.put("2",mainActivity.findViewById(R.id.btn_2));
        idToViewMap.put("3",mainActivity.findViewById(R.id.btn_3));
        idToViewMap.put("4",mainActivity.findViewById(R.id.btn_4));
        idToViewMap.put("5",mainActivity.findViewById(R.id.btn_5));
        idToViewMap.put("6",mainActivity.findViewById(R.id.btn_6));
        idToViewMap.put("7",mainActivity.findViewById(R.id.btn_7));
        idToViewMap.put("8",mainActivity.findViewById(R.id.btn_8));
        idToViewMap.put("9",mainActivity.findViewById(R.id.btn_9));
        idToViewMap.put("0",mainActivity.findViewById(R.id.btn_0));
        idToViewMap.put(",",mainActivity.findViewById(R.id.btn_sep));
        idToViewMap.put(".",mainActivity.findViewById(R.id.btn_com));

        idToViewMap.put("+",mainActivity.findViewById(R.id.btn_add));
        idToViewMap.put("-",mainActivity.findViewById(R.id.btn_sub));
        idToViewMap.put("*",mainActivity.findViewById(R.id.btn_mul));
        idToViewMap.put("/",mainActivity.findViewById(R.id.btn_div));
        idToViewMap.put("(",mainActivity.findViewById(R.id.btn_open_bracket));
        idToViewMap.put(")",mainActivity.findViewById(R.id.btn_close_bracket));

        idToViewMap.put("PI",mainActivity.findViewById(R.id.btn_11));
        idToViewMap.put("E",mainActivity.findViewById(R.id.btn_12));
        idToViewMap.put("^",mainActivity.findViewById(R.id.btn_13));
        idToViewMap.put("LOG",mainActivity.findViewById(R.id.btn_14));
        idToViewMap.put("LN",mainActivity.findViewById(R.id.btn_15));
        idToViewMap.put("LB",mainActivity.findViewById(R.id.btn_16));

        idToViewMap.put("³√",mainActivity.findViewById(R.id.btn_21));
        idToViewMap.put("√",mainActivity.findViewById(R.id.btn_22));
        idToViewMap.put("³",mainActivity.findViewById(R.id.btn_23));
        idToViewMap.put("²",mainActivity.findViewById(R.id.btn_24));
        idToViewMap.put("^10",mainActivity.findViewById(R.id.btn_25));
        idToViewMap.put("!",mainActivity.findViewById(R.id.btn_26));
    }

}
