package com.example.titancalculator;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.titancalculator.helper.StringUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest {
    static int iterationsSubtests = 3;
    Map<String, View> idToViewMap = new HashMap<>();
    Map<String, RoboMenuItem> idToModeMap = new HashMap<>();
    String[] delimiters = new String[]{"1","2","3","4","5","6","7","8","9","0",".",",","ANS","*","/","+","-","(",")",
            "π","e","^","LOG","LN","LB","³√","√","x³","x²","10^x","!",
            "PFZ","GCD","LCM","∑","∏",">%","A/B","x\u207B\u00B9","+/-","MIN","MAX",
            "SIN","COS","TAN","COT","ASIN","ACOS","ATAN","ACOT","DEG",">RAD",">Polar",">Cart",
            "SINH","COSH","TANH","ASINH","ACOSH","ATANH",
            "AND","OR","XOR","NOT",">BIN",">OCT",">DEC",">HEX",
            "ZN()","ZB()","NCR","NPR","MEAN","VAR","E","S",
            "M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6",
            "L","R"
    };
    MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
    RoboMenuItem currentMode = new RoboMenuItem(R.id.basic);

    /**
     * Terms that involve +,-,*,/
     */
    @Test public void testSimpleTerms(){
        for(int i=0; i<iterationsSubtests; i++){
            int op1 = (int) (Math.random()*1000);
            int op2 = (int) (Math.random()*1000);
            //Check the addition of two integer numbers.
            assertTrue(testEquation(op1+"+"+op2,String.valueOf(op1+op2)));
            //Check the subtraction of two integer numbers.
            assertTrue(testEquation(op1+"-"+op2,String.valueOf(op1-op2)));
            //Check the multiplication of two integer numbers.
            assertTrue(testEquation(op1+"*"+op2,String.valueOf(op1*op2)));
            //Check the division of two integer numbers.
            assertTrue(testEquation(op1+"/"+op2,String.valueOf(op1/op2)));
        }

        for(int i=0; i<iterationsSubtests; i++){
            double op1 = Math.random()*iterationsSubtests;
            double op2 = Math.random()*iterationsSubtests;
            //Check the addition of two negative numbers.
            assertTrue(testEquation("-"+op1+"+-"+op2,String.valueOf(-op1+-op2)));
            //Check the addition of one positive and one negative number.
            assertTrue(testEquation("-"+op1+"+"+op2,String.valueOf(-op1+op2)));
            //Check the subtraction of two negative numbers.
            assertTrue(testEquation("-"+op1+"--"+op2,String.valueOf(-op1-(-op2))));
            //Check the subtraction of one negative and one positive number.
            assertTrue(testEquation(+op1+"--"+op2,String.valueOf(op1-(-op2))));
            //Check the multiplication of two negative numbers.
            assertTrue(testEquation("-"+op1+"*-"+op2,String.valueOf(-op1*-op2)));
            //Check the multiplication of one negative and one positive number.
            assertTrue(testEquation("-"+op1+"*"+op2,String.valueOf(-op1*op2)));
            //Check the division of two negative numbers.
            assertTrue(testEquation("-"+op1+"/-"+op2,String.valueOf(-op1/-op2)));
            //Check the division of one positive number and one integer number.
            int op3 = (int) (Math.random()*1000);
            assertTrue(testEquation("-"+op1+"/"+op3,String.valueOf(-op1/op3)));
            //Check the division of a number by zero.
            assertTrue(testEquation(op1+"/0","Math Error"));
            //Check the division of a number by negative number.
            assertTrue(testEquation("-"+op1+"/-"+op2,String.valueOf(-op1/-op2)));
            //Check the division of zero by any number.
            assertTrue(testEquation("0/"+op1,"0"));
        }
    }



    private boolean testEquation(String term, String result) {
        return testEquation(term+"="+result);
    }

    private boolean testEquation(String equation){
        assertTrue(equation.contains("="));
        String term = equation.split("=")[0]; String expectedResult = equation.split("=")[1];
        if(!(expectedResult.matches("Math Error") || expectedResult.matches("[0-9]+") || expectedResult.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"))){
            expectedResult = calcTerm(expectedResult);
        }
        String output = calcTerm(term);
        if(output.equals(expectedResult)){ return true; }
        //System.out.println("tE output: "+output);
        //System.out.println("tE result: "+expectedResult);
        assertTrue(output.matches("Math Error") || output.matches("[0-9]+") || output.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"));
        assertTrue(expectedResult.matches("Math Error") || expectedResult.matches("[0-9]+") || expectedResult.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"));

        if(output.contains("E") && !output.contains("Math Error")){
            mainActivity.findViewById(R.id.btn_eq).performLongClick();
            output = ((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString();
        }
        if(!expectedResult.equals("Math Error")){
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(20);
            expectedResult = df.format(Double.valueOf(expectedResult));
        }
        int minlength = Math.min(Math.min(expectedResult.length(),output.length()),20);

        if(!output.equals(expectedResult)){
            if(expectedResult.startsWith(".") || expectedResult.startsWith("-."))expectedResult=expectedResult.replace(".","0."); //TODO: solve better
            if(output.startsWith(expectedResult.substring(0,Math.min(minlength,CalcModel.precisionDigits))))return true;
            else{
                System.out.println(output+" "+expectedResult);
                return false;
            }
        }
        return true;
    }

    private String calcTerm(String term){
        mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.onOptionsItemSelected(currentMode);
        RoboMenuItem mode = currentMode;
        initMapIdToView(mainActivity); //TODO: ineffizient

        String[] splitted = StringUtils.split(term,delimiters);
        boolean inputShouldEqualtTerm = true;
        //System.out.println("  "+Arrays.toString(splitted));
        for(String s: splitted){
            if(s.isEmpty())break;
            if(idToViewMap.containsKey(s) && (idToViewMap.get(s) instanceof Button)){
                if(idToModeMap.containsKey(s))mainActivity.onOptionsItemSelected(idToModeMap.get(s));
                idToViewMap.get(s).performClick();
                if(s.length() > 1 || s.equals("∑") || s.equals("Π")){
                    inputShouldEqualtTerm = false;
                    for(int i=0; i<s.length(); i++)mainActivity.findViewById(R.id.btn_RECHTS).performClick();
                }
            } else {assertTrue("calcTerm: Button doesnt exist: "+s,false);}
            //System.out.println("    "+((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString());
        }
        mainActivity.findViewById(R.id.btn_eq).performClick();
        if(inputShouldEqualtTerm)assertEquals(((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString(),term);
        return ((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString();
    }

    @Test public void testBasicFunctions(){
        currentMode = new RoboMenuItem(R.id.basic);

        assertTrue(testEquation("π","3.141592653589793"));
        assertTrue(testEquation("e","2.718281828459045"));
        assertTrue(testEquation("e^0","1"));
        assertTrue(testEquation("LN(1)","0"));
        assertTrue(testEquation("LN(e)","1"));
        assertTrue(testEquation("0!","1"));
        assertTrue(testEquation("0.5!","Math Error"));
        assertTrue(testEquation("1!","1"));

        for(int i=0; i<iterationsSubtests; i++) {
            double op1 = (Math.random() * 10) - 0; //TODO: negative numbers
            double op2 = (Math.random() * 10) - 0; //TODO: bigger numbers
            double op3 = Math.random() * 10;
            assertTrue(testEquation(op1+"+"+op2,op2+"+"+op1)); //a+b = b+a
            assertTrue(testEquation(op1+"*"+op2,op2+"*"+op1)); //a*b = b*a
            assertTrue(testEquation(op1+"+("+op2+"+"+op3+")","("+op1+"+"+op2+")+"+op3)); //a+(b+c) = (a+b)+c
            assertTrue(testEquation(op1+"*("+op2+"*"+op3+")","("+op1+"*"+op2+")*"+op3)); //a*(b*c) = (a*b)*c
            assertTrue(testEquation(op1+"*("+op2+"+"+op3+")",op1+"*"+op2+"+"+op1+"*"+op3));//a*(b+c) = a*b + a*c
            assertTrue(testEquation("("+op1+"+"+op2+")*"+op3,op1+"*"+op3+"+"+op2+"*"+op3));//(a+b)*c = a*c + b*c

            assertTrue(testEquation(op1+"^0","1")); //a^0 = 1
            assertTrue(testEquation(op1+"^1",String.valueOf(op1))); //a^1 = a
            assertTrue(testEquation(op1+"^-1","1/"+op1)); //a ^ -1 = 1/a
            assertTrue(testEquation(op1+"^-"+op2,"1/"+op1+"^"+op2)); //a^-b = 1/a^b
            assertTrue(testEquation(op1+"√"+op2,op2+"^(1/"+op1+")")); //a√b = b^(1/a)
            assertFalse(testEquation(op1+"√"+op2,op2+"^1/"+op1)); //a√b != b^1/a
            assertTrue(testEquation(op3+"^("+op1+"+"+op2+")","("+op3+"^"+op2+")*("+op3+"^"+op1+")"));  //a^(b+c) = a^c + a^c
            assertFalse(testEquation(op3+"^"+op1+"+"+op2,+op3+"^"+op2+"*"+op3+"^"+op1)); //a^b+c != a^c + a^c
            assertTrue(testEquation(op3+"^("+op1+"-"+op2+")","("+op3+"^"+op1+")/("+op3+"^"+op2+")")); //a^(b-c) = a^b / a^c
            assertFalse(testEquation(op3+"^"+op1+"-"+op2,+op3+"^"+op1+"/"+op3+"^"+op2)); //a^b-c != a^c / a^c
            assertTrue(testEquation("("+op1+"^"+op2+")^"+op3,op1+"^("+op2+"*"+op3+")")); //(a^b)^c = a^(b^c)
            //assertFalse(testEquation(+op1+"^"+op2+"^"+op3,op1+"^"+op2+"*"+op3));
            //assertFalse(testEquation(op1+"^"+op3+"^"+op2,op1+"^"+op2+"*"+op3));
            assertTrue(testEquation("("+op1+"*"+op2+")^"+op3,op1+"^"+op3+"*"+op2+"^"+op3)); //(a*b)^c = a^c * b^c
            //assertFalse(testEquation(op1+"*"+op2+"^"+op3,op1+"^"+op3+"*"+op2+"^"+op3));
            assertTrue(testEquation(op1+"^"+op3+"/"+op2+"^"+op3,"("+op1+"/"+op2+")^"+op3)); //a^c / b^c = (a/b)^c

            assertTrue(testEquation("e^"+op1+"*"+"e^"+op2,"e^("+op1+"+"+op2+")")); //e^a*e^b = e^(b+c)
            assertTrue(testEquation("e^"+op1+"/"+"e^"+op2,"e^("+op1+"-"+op2+")")); //e^a/e^b = e^(b-c)
            assertTrue(testEquation("(e^"+op1+")"+"^"+op2,"e^("+op1+"*"+op2+")")); //(e^a)^b = e^(a*b)
            assertTrue(testEquation("e^-"+op1,"1/e^"+op1)); //e^-a = 1/e^a
            assertTrue(testEquation("LN(e^"+op1+")",String.valueOf(op1))); //LN(e^a) = a
            assertTrue(testEquation("LN("+op1+")+LN("+op2+")","LN("+op1+"*"+op2+")")); //LN(a) + LN(b) = LN(a*b)
            assertTrue(testEquation("LN("+op1+")-LN("+op2+")","LN("+op1+"/"+op2+")")); //LN(a) - LN(b) = LN(a/b)
            assertTrue(testEquation("LN("+op1+"^"+op2+")",op2+"*LN("+op1+")")); //LN(a^b) = b*LN(a)


            assertTrue(testEquation("LOG("+op1+")+LOG("+op2+")","LOG("+op1+"*"+op2+")")); //LOG(a) + LOG(b) = LOG(a*b)

            //TODO: https://www.mathe-online.at/mathint/log/i_lexikon_rechenregelnLog.html
        }
    }

    @Test public void testBasic2Functions() {
        currentMode = new RoboMenuItem(R.id.basic2);

        for(int i=0; i<3; i++) {
            int inta = (int) ((Math.random() * 1000) - 500); //TODO: negative numbers
            int intb = (int) ((Math.random() * 1000) - 500); //TODO: negative numbers
            int intc = (int) ((Math.random() * 1000) - 500); //TODO: negative numbers
            int intm = (int) ((Math.random() * 1000));

            double op1 = (Math.random() * 10) - 0; //TODO: negative numbers
            double op2 = (Math.random() * 10) - 0; //TODO: bigger numbers
            double op3 = (Math.random() * 10) - 0; //TODO: bigger numbers
            double d = (Math.random() * 10) - 0;

            assertTrue(testEquation("GCD" + inta + "R0", String.valueOf(Math.abs(inta)))); //gcd(a, 0) = |a|, for a ≠ 0
            for(int div = 1; div<Math.min(Math.abs(inta),Math.abs(intb)); div++){ //Every common divisor of a and b is a divisor of gcd(a, b).
                if(Math.abs(inta) % div == 0 && Math.abs(intb) % div == 0){
                    assertTrue(Integer.valueOf(calcTerm("GCD"+inta+"R"+intb)) % div == 0);
                }
            }
            assertTrue(testEquation("GCD"+inta+"R"+intb,"GCD"+intb+"R"+inta)); // gcd(a,b) = gcd(b, a).
            assertTrue(testEquation("LCM"+inta+"R"+intb,"LCM"+intb+"R"+inta)); // lcm(a,b) = lcm(b, a).
            assertTrue(testEquation("GCD"+inta+"RGCD"+intb+"R"+intc,"GCD"+intc+"RGCD"+inta+"R"+intb)); //gcd(a, gcd(b, c)) = gcd(gcd(a, b), c)
            assertTrue(testEquation("LCM"+inta+"RLCM"+intb+"R"+intc,"LCM"+intc+"RLCM"+inta+"R"+intb)); //lcm(a, lcm(b, c)) = lcm(lcm(a, b), c)
            assertTrue(testEquation("LCM"+inta+"RGCD"+inta+"R"+intb,String.valueOf(Math.abs(inta))));//lcm(a,gcd(a,b)) = |a|
            assertTrue(testEquation("GCD"+inta+"RLCM"+inta+"R"+intb,String.valueOf(Math.abs(inta))));//gcd(a,lcm(a,b)) = |a|
            assertTrue(testEquation("GCD"+inta+"R"+inta,String.valueOf(Math.abs(inta)))); //gcd(a,a) = |a|
            assertTrue(testEquation("LCM"+inta+"R"+inta,String.valueOf(Math.abs(inta)))); //lcm(a,a) = |a|
            assertTrue(testEquation("GCD"+intm+"*"+inta+"R"+intm+"*"+intb,"(GCD"+inta+"R"+intb+")*"+intm)); //gcd(m⋅a, m⋅b) = m⋅gcd(a, b).
            assertTrue(testEquation("LCM"+intm+"*"+inta+"R"+intm+"*"+intb,"(LCM"+inta+"R"+intb+")*"+intm)); //lcm(m⋅a, m⋅b) = m⋅lcm(a, b).
            assertTrue(testEquation("GCD"+inta+"+("+intm+"*"+intb+")R"+intb,"(GCD"+inta+"R"+intb)); //gcd(a + m⋅b, b) = gcd(a, b).
            assertTrue(testEquation("LCM"+inta+"+("+intm+"*"+intb+")R"+intb,"(LCM"+inta+"R"+intb)); //lcm(a + m⋅b, b) = lcm(a, b).
            //TODO: assertTrue(testEquation("GCD"+inta+"+"+intm+"*"+intb+"R"+intb,"(GCD"+inta+"R"+intb));
            assertTrue(testEquation(String.valueOf(Integer.valueOf(calcTerm("(GCD"+inta+"R"+intb+")"))*Integer.valueOf(calcTerm("(LCM"+inta+"R"+intb+")"))),String.valueOf(Math.abs(inta*intb))));
            //TODO: assertTrue(testEquation("(LCM"+inta+"R"+intb+")"+"*"+"(GCD"+inta+"R"+intb+")",String.valueOf(Math.abs(inta*intb))));
            assertTrue(testEquation("GCD"+inta+"R"+"LCM"+intb+"R"+intc,"LCM"+"GCD"+inta+"R"+intb+"R"+"GCD"+inta+"R"+intc)); //gcd(a, lcm(b, c)) = lcm(gcd(a, b), gcd(a, c))
            assertTrue(testEquation("LCM"+inta+"R"+"GCD"+intb+"R"+intc,"GCD"+"LCM"+inta+"R"+intb+"R"+"LCM"+inta+"R"+intc)); //lcm(a, gcd(b, c)) = gcd(lcm(a, b), lcm(a, c))

            assertTrue(testEquation("∑"+"1"+"R"+intm,"("+intm+"("+intm+"+1))/2")); //∑1..m = m(m+1) / 2

            //https://www.mathebibel.de/summenzeichen
            //https://www.mathebibel.de/produktzeichen

            assertTrue(testEquation("MAX"+op1+"R"+op2,"-MIN"+"-"+op1+"R-"+op2)); //max(a,b) = -min(-a,-b)
            assertTrue(testEquation("MIN"+op1+"R"+op2,"-MAX"+"-"+op1+"R-"+op2)); //min(a,b) = -max(-a,-b)
            assertTrue(testEquation("(MAX"+op1+"R"+op2+")+"+op3,"MAX"+op1+"+"+op3+"R"+op2+"+"+op3)); //max(a,b)+c = max(a+c,b+c)
            assertTrue(testEquation("(MIN"+op1+"R"+op2+")+"+op3,"MIN"+op1+"+"+op3+"R"+op2+"+"+op3)); //min(a,b)+c = min(a+c,b+c)
            assertTrue(testEquation("(MAX"+op1+"R"+op2+")*"+d,"MAX"+op1+"*"+d+"R"+op2+"*"+d)); //d*max(a,b) = max(d*a,d*b)
            assertTrue(testEquation("(MIN"+op1+"R"+op2+")*"+d,"MIN"+op1+"*"+d+"R"+op2+"*"+d)); //d*min(a,b) = min(d*a,d*b)

        }

    }

    @Test public void testTrigoAndHyperFunctions() {
        currentMode = new RoboMenuItem(R.id.trigo);

        for(int i=0; i<3; i++) {
            double x = (Math.random() * 10) - 0; //TODO: negative numbers
            double y = (Math.random() * 10) - 0; //TODO: bigger numbers
            int a = (int) ((Math.random() * 1000) - 500); //TODO: negative numbers
            int b = (int) ((Math.random() * 1000) - 500); //TODO: negative numbers

            assertTrue(testEquation("SIN("+x+"+2*"+a+"*π)","SIN("+x+")"));//sin(x + 2k*π) = sinx
            //cos(x + 2k*π) = x

        }
            //TODO
    }

    @Test public void testLogicFunctions() {
        //TODO
    }

    @Test public void testStatisticFunctions() {
        //TODO
    }

    @Test public void testMemoryFunctions() {
        //TODO
    }

    @Test public void testVoids(){
        //TODO: PFZ
        //TODO: >%             assertEquals(calcTerm(op1+">%"),calcTerm(mainActivity.findViewById(R.id.eT_ausgabe)+"*100"));
        //TODO: >A/B
        //TODO: >x^-1
        //TODO: >+/-

    }

    private void initMapIdToView(MainActivity mainActivity){
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

        String[] fun1 = new String[]{"π","e","^","LOG","LN","LB","³√","√","x³","x²","10^x","!"};
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i], new RoboMenuItem(R.id.basic));}
        fun1 = new String[]{"PFZ","GCD","LCM","∑","∏","",">%","A/B","x⁻¹","+/-","MIN","MAX"};
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.basic2));}
        fun1 = new String[]{"SIN","COS","TAN","COT","ASIN","ACOS","ATAN","ACOT",">DEG",">RAD",">Polar",">Cart"};
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.trigo));}
        fun1 = new String[]{"SINH","COSH","TANH","ASINH","ACOSH","ATANH","","","","","",""};
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.hyper));}
        fun1 = new String[]{"AND","OR","XOR","NOT",">BIN",">OCT",">DEC",">HEX","","","",""};
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.logic));}
        fun1 = new String[]{"ZN()","ZB()","NCR","NPR","MEAN","VAR","E","S","","","",""};
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.statistic));}
        fun1 = new String[]{"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.memory));}
        idToViewMap.put("L",mainActivity.findViewById(R.id.btn_LINKS));
        idToViewMap.put("R",mainActivity.findViewById(R.id.btn_RECHTS));
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

        assertEquals(ans2,"2");
        assertEquals(ans3,"4");
    }

    @Test public void displayFunctions(){
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        Button[] B = new Button[]{mainActivity.findViewById(R.id.btn_11),mainActivity.findViewById(R.id.btn_12),mainActivity.findViewById(R.id.btn_13),
                mainActivity.findViewById(R.id.btn_14),mainActivity.findViewById(R.id.btn_15),mainActivity.findViewById(R.id.btn_16),
                mainActivity.findViewById(R.id.btn_21),mainActivity.findViewById(R.id.btn_22),mainActivity.findViewById(R.id.btn_23),
                mainActivity.findViewById(R.id.btn_24),mainActivity.findViewById(R.id.btn_25),mainActivity.findViewById(R.id.btn_26)};
        Button[] S = new Button[]{mainActivity.findViewById(R.id.btn_clearall),mainActivity.findViewById(R.id.btn_eq)};
        EditText INPUT  = mainActivity.findViewById(R.id.eT_eingabe);
        EditText OUTPUT = mainActivity.findViewById(R.id.eT_ausgabe);


        for(View v: B)assertTrue(v.isShown());
        mainActivity.findViewById(R.id.btn_FUN).performClick();
        for(View v: B){
            if(v.isShown())System.out.println(v.getTag());
            assertFalse(v.isShown());
        }
        mainActivity.findViewById(R.id.btn_FUN).performClick();
        for(View v: B)assertTrue(v.isShown());

        String[] fun1 = new String[]{"π","e","^","LOG","LN","LB","³√","√","x³","x²","10^x","!"};
        for(int i=0; i<fun1.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun1[i]);
        }
        fun1 = new String[]{"π","e","^","LOG","LN","LB","³√","√","³","²","10^","!"};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        MenuItem menuItem = new RoboMenuItem(R.id.basic2);
        mainActivity.onOptionsItemSelected(menuItem);
        String[] fun2 = new String[]{"PFZ","GCD","LCM","∑","∏","",">%","A/B","x⁻¹","+/-","MIN","MAX"};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun2[i]);
        }
        fun1 = new String[]{"","GCD(,)","LCM(,)","∑(,)","∏(,)","","","","","","MIN()","MAX()"};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.trigo));
        fun2 = new String[]{"SIN","COS","TAN","COT","ASIN","ACOS","ATAN","ACOT",">DEG",">RAD",">Polar",">Cart"};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun2[i]);
        }
        fun1 = new String[]{"SIN","COS","TAN","COT","ASIN","ACOS","ATAN","ACOT","","","toPolar(,)","toCart(,)"};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.hyper));
        fun2 = new String[]{"SINH","COSH","TANH","ASINH","ACOSH","ATANH","","","","","",""};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun2[i]);
        }
        fun1 = fun2.clone();
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.logic));
        fun2 = new String[]{"AND","OR","XOR","NOT",">BIN",">OCT",">DEC",">HEX","","","",""};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun2[i]);
        }
        fun1 = new String[]{"AND(,)","OR(,)","XOR(,)","NOT()","","","","","","","",""};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.statistic));
        fun2 = new String[]{"ZN()","ZB()","NCR","NPR","MEAN","VAR","E","S","","","",""};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun2[i]);
        }
        fun1 = new String[]{"Zn()","Zb(,)","C","P","MEAN()","VAR()","E()","2√(VAR())","","","",""};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.memory));
        fun2 = new String[]{"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun2[i]);
        }
        fun1 = new String[]{"","","","","","","","","","","",""};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

    }
}
