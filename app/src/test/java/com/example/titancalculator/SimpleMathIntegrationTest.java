package com.example.titancalculator;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.titancalculator.helper.Math_String.MathEvaluator;
import com.example.titancalculator.helper.Math_String.StringUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Config(sdk = 28)
@RunWith(RobolectricTestRunner.class)
public class SimpleMathIntegrationTest {
    private String[][] btn = new String[2][6];

    private static int iterationsSubtests = 10;
    private Map<String, View> idToViewMap = new HashMap<>();
    private Map<String, RoboMenuItem> idToModeMap = new HashMap<>();
    private String[] delimiters = new String[]{"1","2","3","4","5","6","7","8","9","0",".",",","ANS","*","/","+","-","(",")",
            "π","e","^","LOG","LN","LB","³√","√","³","²","10^x","!",
            "PFZ","GCD","LCM","∑","∏",">%",">A/B",">x\u207B\u00B9",">+/-","MIN","MAX",
            "SIN","COS","TAN","COT","SEC","CSC","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC",
            "SINH","COSH","TANH","ASINH","ACOSH","ATANH",">DEG",">RAD",
            "AND","OR","XOR","NOT",">BIN",">OCT",">DEC",">HEX",
            "RAND","RANDB","NCR","NPR","MEAN","VAR","S",
            "M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6",
            "L","R"
    };
    private HashMap<String,Integer> translateFunctionToNumberParameters;
    private MainActivity mainActivity;
    private RoboMenuItem currentMode;


    /**
     * do some basic calculations
     */
    @Test
    public void testCalculation(){
        for(int a=0;a<10;a++) {
            for (int b = 0; b < 10; b++) {
                assertEquals(String.valueOf(a+b),calcTerm(a+"+"+b));
                assertEquals(String.valueOf(a-b),calcTerm(a+"-"+b));
                assertEquals(String.valueOf(a*b),calcTerm(a+"*"+b));
                assertEquals(String.valueOf(a/b),calcTerm(a+"/"+b));
            }
        }
        assertEquals("0",calcTerm("(((0)))"));
        assertEquals("Math Error",calcTerm("((0)"));
    }

    /**
     * test if all input buttons work correct
     */
    @Test
    public void testInputButtonMatchesFunctionality(){
        MenuItem[] modes = new MenuItem[]{
                new RoboMenuItem(R.id.basic), new RoboMenuItem(R.id.advanced), new RoboMenuItem(R.id.trigo),
                new RoboMenuItem(R.id.statistic), new RoboMenuItem(R.id.logic),
        };

        mainActivity.setInputText(""); mainActivity.setOutputText("");
        for(int mode=0;mode<CalcModel.modesModesText.length;mode++){
            mainActivity.onOptionsItemSelected(modes[mode]);
            for(int function=0;function<CalcModel.modesModesText[mode].length;function++){
                System.out.println(mode+" "+function+" "+CalcModel.modesModesText[mode][function]);
                idToViewMap.get(CalcModel.modesModesText[mode][function]).performClick();
                assertEquals(CalcModel.modesModesFunctionality[mode][function],mainActivity.getInputText());
                mainActivity.findViewById(R.id.btn_clear_all).performLongClick();
            }

        }
    }



    /**
     * test if ANS Button works
     */
    @Test
    public void testANSButton(){
        mainActivity.setInputText("1+11");
        mainActivity.findViewById(R.id.btn_eq_ANS).performClick();
        assertEquals("12",mainActivity.getOutputText());
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        mainActivity.findViewById(R.id.btn_eq_ANS).performLongClick();
        assertEquals("ANS",mainActivity.getInputText());
        mainActivity.findViewById(R.id.btn_eq_ANS).performClick();
        assertEquals("12",mainActivity.getOutputText());
    }

    /**
     * test if clear / clear all works
     */
    @Test
    public void testClearButtons(){
        mainActivity.setInputText("1+11");
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        assertEquals("1+1",mainActivity.getInputText());
        mainActivity.setOutputText("2");
        mainActivity.findViewById(R.id.btn_clear_all).performLongClick();
        assertEquals("",mainActivity.getInputText());
        assertEquals("",mainActivity.getOutputText());
    }

    @Test public void testMemoryFunctions() {
        currentMode = new RoboMenuItem(R.id.memory);

        calcTerm("");
        mainActivity.findViewById(R.id.btn_21).performClick();
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        mainActivity.findViewById(R.id.btn_11).performClick();
        assertEquals("",((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
        mainActivity.findViewById(R.id.btn_clear_all).performClick();

        calcTerm("1+1");
        mainActivity.findViewById(R.id.btn_22).performClick();
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        mainActivity.findViewById(R.id.btn_12).performClick();
        assertEquals("1+1",((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
        mainActivity.findViewById(R.id.btn_clear_all).performClick();

        calcTerm("1+224*124");
        mainActivity.setSelectionInput(0,5);
        mainActivity.findViewById(R.id.btn_23).performClick();
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        mainActivity.findViewById(R.id.btn_13).performClick();
        assertEquals("1+224",((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
        mainActivity.findViewById(R.id.btn_clear_all).performClick();

        calcTerm("1+224*124");
        mainActivity.setSelectionInput(2,9);
        mainActivity.findViewById(R.id.btn_23).performClick();
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        mainActivity.findViewById(R.id.btn_13).performClick();
        assertEquals("224*124",((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
        mainActivity.findViewById(R.id.btn_clear_all).performClick();

        calcTerm("101*9");
        mainActivity.findViewById(R.id.eT_output).requestFocus();
        mainActivity.setSelectionOutput(0,3);
        mainActivity.findViewById(R.id.btn_24).performClick();
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        mainActivity.findViewById(R.id.btn_14).performClick();
        assertEquals("909",((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
        mainActivity.findViewById(R.id.btn_clear_all).performClick();

        calcTerm("101*9");
        mainActivity.findViewById(R.id.eT_output).requestFocus();
        mainActivity.setSelectionOutput(0,2);
        mainActivity.findViewById(R.id.btn_25).performClick();
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        mainActivity.findViewById(R.id.btn_15).performClick();
        assertEquals("90",((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
        mainActivity.findViewById(R.id.btn_clear_all).performClick();

        calcTerm("101*9");
        mainActivity.findViewById(R.id.eT_output).requestFocus();
        mainActivity.setSelectionOutput(1,3);
        mainActivity.findViewById(R.id.btn_25).performClick();
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        mainActivity.findViewById(R.id.btn_15).performClick();
        assertEquals("09",((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
    }

    @Test public void testVoids(){
        currentMode = new RoboMenuItem(R.id.advanced);
        for(int i=0; i<iterationsSubtests; i++) {
            int a = (int) ((Math.random() * 1000) - 500);
            double x = ((Math.random() * 1000) - 500);

            Assert.assertTrue(MathEvaluator.resembles(calcTerm(x+">%"),calcTerm(((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString()+"*100"))); mainActivity.findViewById(R.id.btn_clear_all).performClick();
            Assert.assertTrue(MathEvaluator.resembles(calcTerm(x+">+/-"),calcTerm(((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString()+"*-1"))); mainActivity.findViewById(R.id.btn_clear_all).performClick();
            Assert.assertTrue(MathEvaluator.resembles(calcTerm("3"+">x\u207B\u00B9"),calcTerm("1/3"/*+((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString()*/))); mainActivity.findViewById(R.id.btn_clear_all).performClick();

            calcTerm(x+">A/B"); String output = ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString(); String result = calcTerm(output);
            Assert.assertTrue(MathEvaluator.resembles(String.valueOf(x),result,4)); mainActivity.findViewById(R.id.btn_clear_all).performClick();

            calcTerm(a+"PFZ"); output = ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString(); result = calcTerm(output.replace("(","").replace(")","").replace(",","*"));
            Assert.assertTrue(MathEvaluator.resembles(String.valueOf(a),result)); mainActivity.findViewById(R.id.btn_clear_all).performClick();
        }
    }

    @Test public void testNavigationButtons(){
        mainActivity.findViewById(R.id.btn_1).performClick();
        int pos1 = ((EditText) mainActivity.findViewById(R.id.eT_input)).getSelectionStart();
        mainActivity.btn_left.performClick();
        mainActivity.btn_left.performClick();
        int pos2 = ((EditText) mainActivity.findViewById(R.id.eT_input)).getSelectionStart();
        mainActivity.btn_right.performClick();
        mainActivity.btn_right.performClick();
        int pos3 = ((EditText) mainActivity.findViewById(R.id.eT_input)).getSelectionStart();
        assertEquals(pos1,pos3);
        assertEquals(pos1,pos2+1);

        mainActivity.findViewById(R.id.btn_0).performClick();
        mainActivity.btn_left.performClick();
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        System.out.println(mainActivity.getInputText());
        String i1 = ((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString();
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        String i2 = ((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString();
        assertEquals(i1,"0");
        assertEquals(i2,"");
    }

    @Test public void testANSFunction(){
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        mainActivity.findViewById(R.id.btn_1).performClick();
        mainActivity.findViewById(R.id.btn_add).performClick();
        mainActivity.findViewById(R.id.btn_1).performClick();
        mainActivity.findViewById(R.id.btn_eq_ANS).performClick();
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        mainActivity.findViewById(R.id.btn_eq_ANS).performClick();
        String ans2 = ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString();

        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        mainActivity.findViewById(R.id.btn_mul).performClick();
        mainActivity.findViewById(R.id.btn_2).performClick();
        mainActivity.findViewById(R.id.btn_eq_ANS).performClick();
        String ans3 = ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString();

        assertEquals(ans2,"2");
        assertEquals(ans3,"4");
    }


    @Before
    public void Before(){
        mainActivity = Robolectric.setupActivity(MainActivity.class);
        currentMode = new RoboMenuItem(R.id.basic);

        Button[] B = new Button[]{mainActivity.findViewById(R.id.btn_11),mainActivity.findViewById(R.id.btn_12),mainActivity.findViewById(R.id.btn_13),
                mainActivity.findViewById(R.id.btn_14),mainActivity.findViewById(R.id.btn_15),mainActivity.findViewById(R.id.btn_16),
                mainActivity.findViewById(R.id.btn_21),mainActivity.findViewById(R.id.btn_22),mainActivity.findViewById(R.id.btn_23),
                mainActivity.findViewById(R.id.btn_24),mainActivity.findViewById(R.id.btn_25),mainActivity.findViewById(R.id.btn_26)};

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

        String[] fun1 = CalcModel.modeBasicText;
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i], new RoboMenuItem(R.id.basic));}
        fun1 = CalcModel.modeAdvancedText;
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.advanced));}
        fun1 = CalcModel.modeTrigoText;
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.trigo));}
        fun1 = CalcModel.modeStatisticText;
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.statistic));}
        fun1 = CalcModel.modeLogicText;
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.logic));}
        fun1 = CalcModel.modeMemoryText;
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.memory));}
        idToViewMap.put("L",mainActivity.btn_left);
        idToViewMap.put("R",mainActivity.btn_right);
    }

    private boolean testEquation(String term, String result) {
        return testEquation(term+"="+result);
    }

    private boolean testEquation(String equation){
        Assert.assertTrue(equation.contains("="));
        //System.out.println("testEquation input: "+equation);
        String term = equation.split("=")[0]; String expectedResult = equation.split("=")[1];
        if(!(expectedResult.matches("Math Error") || expectedResult.matches("[0-9]+") || expectedResult.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"))){
            expectedResult = calcTerm(expectedResult);
        }
        String output = calcTerm(term);
        if(output.equals(expectedResult)){ return true; }
        //System.out.println("testEquation result: "+output+" ?= "+expectedResult);
        Assert.assertTrue(output.matches("Math Error") || output.matches("[0-9]+") || output.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"));
        Assert.assertTrue(expectedResult.matches("Math Error") || expectedResult.matches("[0-9]+") || expectedResult.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"));

        if(output.contains("E") && !output.contains("Math Error")){
            mainActivity.findViewById(R.id.btn_eq_ANS).performLongClick();
            output = ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString();
        }
        if(!expectedResult.equals("Math Error")){
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(20);
            expectedResult = df.format(Double.valueOf(expectedResult));
        }
        int minlength = Math.min(Math.min(expectedResult.length(),output.length()),20);

        if(!output.equals(expectedResult)){
            return MathEvaluator.resembles(expectedResult,output);
        }
        return true;
    }

    private String calcTerm(String term){
        mainActivity.findViewById(R.id.btn_clear_all).performClick();
        mainActivity.onOptionsItemSelected(currentMode);
        RoboMenuItem mode = currentMode;
        mainActivity.setSelectionInput(0,0);
        mainActivity.eT_input.requestFocus();

        System.out.println("calcTerm: \""+term+"\"");
        String[] splitted = StringUtils.splitTokens(term);
        boolean inputShouldEqualtTerm = true; boolean containsOutputFunctions = false;
        System.out.println("cT  "+ Arrays.toString(splitted));
        mainActivity.findViewById(R.id.btn_clear_all).performLongClick();
        for(String s: splitted){
            System.out.println("cT input["+mainActivity.getSelectionStartInput()+","+mainActivity.getSelectionEndInput()+"]: "+s+" = "+
                    ((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
            System.out.println("cT output: "+((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString());
            if(s.isEmpty())break;
            if(idToViewMap.containsKey(s) && (idToViewMap.get(s) instanceof Button)){
                if(s.contains(">"))containsOutputFunctions = true;
                if(idToModeMap.containsKey(s))mainActivity.onOptionsItemSelected(idToModeMap.get(s));
                System.out.println("click: "+((Button) idToViewMap.get(s)).getText());
                idToViewMap.get(s).performClick();
                if(s.length() > 1 || s.equals("∑") || s.equals("Π") || s.equals("S")){
                    inputShouldEqualtTerm = false;
                    for(int i=0; i<s.length(); i++)mainActivity.btn_right.performClick();
                }
            } else {
                System.out.println("unknown lexical unit: "+s);
                Assert.assertTrue("calcTerm: Button doesnt exist: "+s,false);
            }
        }
        System.out.println("cT final input: "+((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString()+"\n\n");
        System.out.println("cT final output: "+((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString()+"\n\n");
        if(!containsOutputFunctions)mainActivity.findViewById(R.id.btn_eq_ANS).performClick();
        if(inputShouldEqualtTerm) assertEquals(((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString(),term);
        return ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString();
    }
}
