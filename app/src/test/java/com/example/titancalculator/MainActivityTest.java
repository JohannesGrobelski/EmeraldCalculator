package com.example.titancalculator;

import android.os.Build;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.internal.IShadow;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
        System.out.println(((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString());
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

    @Test public void testComplexFunctions(){
        assertTrue(testTerm("b^e8",String.valueOf(8)));

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

        idToViewMap.put("a",mainActivity.findViewById(R.id.btn_11));
        idToViewMap.put("b",mainActivity.findViewById(R.id.btn_12));
        idToViewMap.put("c",mainActivity.findViewById(R.id.btn_13));
        idToViewMap.put("d",mainActivity.findViewById(R.id.btn_14));
        idToViewMap.put("e",mainActivity.findViewById(R.id.btn_15));
        idToViewMap.put("f",mainActivity.findViewById(R.id.btn_16));

        idToViewMap.put("g",mainActivity.findViewById(R.id.btn_21));
        idToViewMap.put("h",mainActivity.findViewById(R.id.btn_22));
        idToViewMap.put("i",mainActivity.findViewById(R.id.btn_23));
        idToViewMap.put("j",mainActivity.findViewById(R.id.btn_24));
        idToViewMap.put("k",mainActivity.findViewById(R.id.btn_25));
        idToViewMap.put("l",mainActivity.findViewById(R.id.btn_26));
    }

    @Test public void testNavigationButtons(){
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.findViewById(R.id.btn_1).performClick();
        int pos1 = ((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getSelectionStart();
        mainActivity.findViewById(R.id.btn_LINKS).performClick();
        mainActivity.findViewById(R.id.btn_LINKS).performClick();
        int pos2 = ((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getSelectionStart();
        mainActivity.findViewById(R.id.btn_RECHTS).performClick();
        mainActivity.findViewById(R.id.btn_RECHTS).performClick();
        int pos3 = ((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getSelectionStart();
        assertEquals(pos1,pos3);
        assertEquals(pos1,pos2+1);

        mainActivity.findViewById(R.id.btn_0).performClick();
        mainActivity.findViewById(R.id.btn_LINKS).performClick();
        mainActivity.findViewById(R.id.btn_clear).performClick();
        String i1 = ((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        String i2 = ((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString();
        assertEquals(i1,"0");
        assertEquals(i2,"");
    }

    @Test public void testANSFunction(){
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.findViewById(R.id.btn_ANS).performClick();
        mainActivity.findViewById(R.id.btn_eq).performClick();
        String ans1 = ((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString();

        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_1).performClick();
        mainActivity.findViewById(R.id.btn_add).performClick();
        mainActivity.findViewById(R.id.btn_1).performClick();
        mainActivity.findViewById(R.id.btn_eq).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_ANS).performClick();
        mainActivity.findViewById(R.id.btn_eq).performClick();
        String ans2 = ((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString();

        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_ANS).performClick();
        mainActivity.findViewById(R.id.btn_mul).performClick();
        mainActivity.findViewById(R.id.btn_2).performClick();
        mainActivity.findViewById(R.id.btn_eq).performClick();
        String ans3 = ((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString();

        assertEquals(ans1,"Math Error");
        assertEquals(ans2,"2");
        assertEquals(ans3,"4");
    }

    @Test public void displayFunctions(){
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        Button[] b = new Button[]{mainActivity.findViewById(R.id.btn_11),mainActivity.findViewById(R.id.btn_12),mainActivity.findViewById(R.id.btn_13),
                                 mainActivity.findViewById(R.id.btn_14),mainActivity.findViewById(R.id.btn_15),mainActivity.findViewById(R.id.btn_16),
                                 mainActivity.findViewById(R.id.btn_21),mainActivity.findViewById(R.id.btn_22),mainActivity.findViewById(R.id.btn_23),
                                 mainActivity.findViewById(R.id.btn_24),mainActivity.findViewById(R.id.btn_25),mainActivity.findViewById(R.id.btn_26)};
        Button[] s = new Button[]{mainActivity.findViewById(R.id.btn_clearall),mainActivity.findViewById(R.id.btn_eq)};
        EditText input = mainActivity.findViewById(R.id.eT_eingabe);
        EditText output = mainActivity.findViewById(R.id.eT_ausgabe);


        mainActivity.findViewById(R.id.btn_FUN).performClick();
        for(View v: b)assertFalse(v.isShown());
        mainActivity.findViewById(R.id.btn_FUN).performClick();
        for(View v: b)assertTrue(v.isShown());

        String[] fun1 = new String[]{"π","e","^","LOG","LN","LB","³√","√","x³","x²","10^x","!"};
        for(int i=0; i<fun1.length; i++){
            assertEquals(((Button) b[i]).getText().toString(),fun1[i]);
        }
        fun1 = new String[]{"π","e","^","LOG","LN","LB","³√","√","³","²","10^","!"};
        for(int i=0; i<fun1.length; i++){
            b[i].performClick(); assertEquals(input.getText().toString(),fun1[i]); s[0].performClick(); assertEquals(output.getText().toString(),input.getText().toString()); s[1].performClick();
        }

        MenuItem menuItem = new RoboMenuItem(R.id.basic2);
        mainActivity.onOptionsItemSelected(menuItem);

        String[] fun2 = new String[]{"PFZ","GCD","LCM","∑","∏","",">%","A/B","x⁻¹","+/-","MIN","MAX"};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) b[i]).getText().toString(),fun2[i]);
        }
        fun1 = new String[]{"Math Error","GCD(,)","LCM(,)","∑(,)","∏(,)","Math Error","Math Error","Math Error","Math Error","Math Error","Math Error","Math Error"};
        for(int i=0; i<fun1.length; i++){
            b[i].performClick(); assertEquals(input.getText().toString(),fun1[i]); s[0].performClick(); assertEquals(output.getText().toString(),input.getText().toString()); s[1].performClick();
        }

        //TODO: trigo, ...
    }



}
