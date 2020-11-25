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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
/**
 * - test properties with random values
 * TODO: test random values
 * TODO: test extreme values
 * TODO: test incorrect values (result: Math Error): for all:
        if (parameters.get(0).doubleValue() == 0) {
            throw new ExpressionException("Number must not be 0");
        }
 */
public class MainActivityTest {
    static int iterationsSubtests = 20; //TODO: hohe iterationen => JRE EXCEPTION_ACCESS_VIOLATION
    private static double toleranceDigits = 10;
    Map<String, View> idToViewMap = new HashMap<>();
    Map<String, RoboMenuItem> idToModeMap = new HashMap<>();
    String[] delimiters = new String[]{"1","2","3","4","5","6","7","8","9","0",".",",","ANS","*","/","+","-","(",")",
            "π","e","^","LOG","LN","LB","³√","√","³","²","10^x","!",
            "PFZ","GCD","LCM","∑","∏",">%",">A/B",">x\u207B\u00B9",">+/-","MIN","MAX",
            "SIN","COS","TAN","COT","SEC","CSC","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC",
            "SINH","COSH","TANH","ASINH","ACOSH","ATANH",">DEG",">RAD","","",
            "AND","OR","XOR","NOT",">BIN",">OCT",">DEC",">HEX",
            "ZN","ZB","NCR","NPR","MEAN","VAR","S",
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
            return resembles(expectedResult,output);
        }
        return true;
    }



    private String calcTerm(String term){
        mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.onOptionsItemSelected(currentMode);
        RoboMenuItem mode = currentMode;
        initMapIdToView(mainActivity); //TODO: ineffizient

        String[] splitted = StringUtils.split(term,delimiters);
        boolean inputShouldEqualtTerm = true; boolean containsOutputFunctions = false;
        //System.out.println("cT  "+Arrays.toString(splitted));
        for(String s: splitted){
            if(s.isEmpty())break;
            if(idToViewMap.containsKey(s) && (idToViewMap.get(s) instanceof Button)){
                if(s.contains(">"))containsOutputFunctions = true;
                if(idToModeMap.containsKey(s))mainActivity.onOptionsItemSelected(idToModeMap.get(s));
                idToViewMap.get(s).performClick();
                if(s.length() > 1 || s.equals("∑") || s.equals("Π") || s.equals("S")){
                    inputShouldEqualtTerm = false;
                    for(int i=0; i<s.length(); i++)mainActivity.findViewById(R.id.btn_RECHTS).performClick();
                }
            } else {
                //System.out.println("unknown lexical unit: "+s);
                assertTrue("calcTerm: Button doesnt exist: "+s,false);
            }
            //System.out.println("cT input: "+((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString());
            //System.out.println("cT output: "+((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString());
        }
        if(!containsOutputFunctions)mainActivity.findViewById(R.id.btn_eq).performClick();
        //System.out.println("cT final output: "+((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString()+"\n\n");
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
            assertTrue(testEquation("LOG("+op1+")-LOG("+op2+")","LOG("+op1+"/"+op2+")")); //LOG(a) - LOG(b) = LOG(a/b)
            assertTrue(testEquation("LOG("+op1+"^"+op2+")",op2+"*LOG("+op1+")")); //LOG(a^b) = b*LOG(a)

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

        //random values
        assertTrue(resembles(calcTerm("COS(9)"),"0.9876883405951377261900402476934372607584068615898804349239048016"));
        assertTrue(resembles(calcTerm("SIN(7.2284977183385335)"),"0.12582667507334807"));


        for(int i=0; i<iterationsSubtests; i++) {
            double x = (Math.random() * 10) + 1; //TODO: negative numbers
            double y = (Math.random() * 10) + 1; //TODO: bigger numbers
            int a = (int) ((Math.random() * 50) - 25); //TODO: negative numbers
            int b = (int) ((Math.random() * 50) - 25); //TODO: negative numbers

            assertTrue(testEquation("SIN("+a*Math.toRadians(Math.PI)+")","0"));
            assertTrue(testEquation("COS("+a*Math.toRadians(Math.PI)+0.5*Math.toRadians(Math.PI)+")","0"));
            assertTrue(testEquation("TAN("+a*Math.toRadians(Math.PI)+")","0"));
            assertTrue(testEquation("COT("+a*Math.toRadians(Math.PI)+Math.toRadians(0.5*Math.PI)+")","0"));

            assertTrue(testEquation("SIN("+x+Math.toRadians(2*Math.PI)+")","SIN("+x+")"));//sin(x + 2k*π) = sinx
            assertTrue(testEquation("COS("+x+"+(2*π))","COS("+x+")"));//cos(x + 2k*π) = cosx

            assertTrue(testEquation("SIN("+x+")/COS("+x+")","TAN("+x+")")); //tan(x) = sin(x) / cot(x)
            assertTrue(testEquation("COS("+x+")/SIN("+x+")","COT("+x+")")); //cot(x) = cos(x) / sin(x)
            assertTrue(testEquation("1/TAN("+x+")","COT("+x+")")); //cot(x) = 1 / tan(x)

            assertTrue(testEquation("SIN(π/2-"+x+")","COS("+x+")")); //cos(x) = sin(π/2 - x)
            assertTrue(testEquation("COS(π/2-"+x+")","SIN("+x+")")); //sin(x) = cos(π/2 - x)
            assertTrue(testEquation("TAN(π/2-"+x+")","COT("+x+")")); //cot(x) = tan(π/2 - x)
            assertTrue(testEquation("COT(π/2-"+x+")","TAN("+x+")")); //tan(x) = cot(π/2 - x)
            assertTrue(testEquation("SEC(π/2-"+x+")","CSC("+x+")")); //sec(x) = csc(π/2 - x)
            assertTrue(testEquation("CSC(π/2-"+x+")","SEC("+x+")")); //csc(x) = sec(π/2 - x)

            assertTrue(testEquation("SIN("+x+"+π*2)","SIN("+x+")")); //sin(x) = sin(x + 2π)
            assertTrue(testEquation("COS("+x+"+π*2)","COS("+x+")")); //cos(x) = cos(x + 2π)
            assertTrue(testEquation("TAN("+x+"+π)","TAN("+x+")")); //tan(x) = tan(x + 2π)

            assertTrue(testEquation("SIN(-"+x+")","-SIN("+x+")")); //sin(-x) = -sin(x)
            assertTrue(testEquation("COS(-"+x+")","COS("+x+")")); //cos(-x) = cos(x)
            assertTrue(testEquation("TAN(-"+x+")","-TAN("+x+")")); //tan(-x) = -tan(x)

            assertTrue(testEquation("SIN("+x+"+"+y+")","SIN("+x+")*COS("+y+")+COS("+x+")*SIN("+y+")")); //sin(x+y) = sin(x)*cos(y)+cos(x)*sin(y)
            assertTrue(testEquation("COS("+x+"+"+y+")","COS("+x+")*COS("+y+")-SIN("+x+")*SIN("+y+")")); //cos(x+y) = cos(x)*cos(y)-sin(x)*sin(y)
            assertTrue(testEquation("SIN("+x+"-"+y+")","SIN("+x+")*COS("+y+")-COS("+x+")*SIN("+y+")")); //sin(x-y) = cos(x)*cos(y)-sin(x)*sin(y)
            assertTrue(testEquation("COS("+x+"-"+y+")","COS("+x+")*COS("+y+")+SIN("+x+")*SIN("+y+")")); //cos(x-y) = cos(x)*cos(y)+sin(x)*sin(y)

            //more identities: http://www2.clarku.edu/faculty/djoyce/trig/identities.html

            double x1 = Math.random() - 0.5;

            assertTrue(testEquation("SIN(ASIN("+x1+"))",String.valueOf(x1))); //sin(asin(x)) = x
            assertTrue(testEquation("COS(ACOS("+x1+"))",String.valueOf(x1))); //cos(acos(x)) = x
            assertTrue(testEquation("TAN(ATAN("+x1+"))",String.valueOf(x1))); //tan(atan(x)) = x
            assertTrue(testEquation("COT(ACOT("+x1+"))",String.valueOf(x1))); //cot(acot(x)) = x
            assertTrue(testEquation("SEC(ASEC("+x+"))",String.valueOf(x))); //sec(asec(x)) = x
            assertTrue(testEquation("CSC(ACSC("+x+"))",String.valueOf(x))); //csc(acsc(x)) = x

            String expected,result;
            calcTerm(x+">RAD"); expected = ((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString();
            calcTerm(x+">DEG"); String deg = ((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString();
            result = calcTerm("(π*"+deg+")/180");
            assertEquals(expected,result);//rad(x) = (π*deg(x)) / 180
        }


        assertTrue(resembles(calcTerm("π>DEG))"),"180",3)); //csc(acsc(x)) = x
        assertTrue(resembles(calcTerm("0>DEG))"),"0",3)); //csc(acsc(x)) = x
        assertTrue(resembles(calcTerm("2*π>DEG))"),"360",3)); //csc(acsc(x)) = x


    }

    @Test public void testLogicFunctions() {
        currentMode = new RoboMenuItem(R.id.trigo);

        for(int i=0; i<iterationsSubtests; i++) {
            int a = (int) ((Math.random() * 1000) - 500);
            int b = (int) ((Math.random() * 1000) - 500);
            int c = (int) ((Math.random() * 1000) - 500);

            //properties
                assertTrue(testEquation("AND("+a+"R"+b+")","AND("+b+"R"+a+")"));    //a and b = b and a
                assertTrue(testEquation("OR("+a+"R"+b+")","OR("+b+"R"+a+")"));    //a or b = b or a
                assertTrue(testEquation("AND"+a+"RAND"+b+"R"+c,"AND"+c+"RAND"+a+"R"+b)); //and(a, and(b, c)) = and(and(a, b), c)
                assertTrue(testEquation("OR"+a+"ROR"+b+"R"+c,"OR"+c+"ROR"+a+"R"+b)); //or(a, or(b, c)) = or(or(a, b), c)
                assertTrue(testEquation("AND("+a+"R0)","0"));    //a and 0 = 0
                assertTrue(testEquation("OR("+a+"R0)",String.valueOf(a)));    //a or 0 = a
                assertTrue(testEquation("AND("+a+"ROR("+a+"R1))",String.valueOf(a)));    //a and a or 1 = a
                assertTrue(testEquation("OR("+a+"ROR("+a+"R1))","OR("+a+"R1)"));    //a and a or 1 = a or 1
                assertTrue(testEquation("NOT(NOT("+a+"))",String.valueOf(a)));    //not(not(a)) = a
                assertTrue(testEquation("AND("+a+"R"+"NOT("+a+"))","0"));    //a and not(a) = 0
                assertTrue(testEquation("NOT(1)","-2"));    //not(0) = 1
                assertTrue(testEquation("NOT(0)","-1"));    //not(1) = 0


            //assertTrue(testEquation("NOT(AND("+a+"R"+b+"))","OR("+"NOT("+a+")"+"R"+"NOT("+b+"))"));    //not(a and b) = not(a) or not(b)

            // assertTrue(testEquation("OR("+a+"R1)",String.valueOf(a)));    //a or 1 = a

        }
    }

    @Test public void testStatisticFunctions() {
        currentMode = new RoboMenuItem(R.id.statistic);
        for(int i=0; i<iterationsSubtests; i++) {
            int a = (int) ((Math.random() * 1000));
            int b = a+100;

            int n = (int) ((Math.random() * 1000));
            int r = (int) ((Math.random() * 1000));

            double x = ((Math.random() * 1000) - 500);

            calcTerm("ZN("+a+")"); int result = Integer.valueOf(((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString());
            assertTrue(result > 0); assertTrue(result <= a);  //0 < result < a

            calcTerm("ZB("+a+"R"+b+")");  result = Integer.valueOf(((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString());
            assertTrue(result > a); assertTrue(result <= b);  //0 < result < a

            assertTrue(testEquation("NPR("+n+"R"+r+")",n+"!"+"/("+n+"-"+r+")!")); //nPr(n,r)=n!/(n−r)!
            assertTrue(testEquation("NCR("+n+"R"+r+")","NPR("+n+"R"+r+")/"+r+"!")); //nCr(n,r)=nPr(n,r)/r!

        }

        //extreme values
        //mean
            assertTrue(resembles(calcTerm("MEAN()"),"Math Error"));
            assertTrue(resembles(calcTerm("MEAN(-1,0,1)"),calcTerm("0")));
            assertTrue(resembles(calcTerm("MEAN(1,2,3)"),calcTerm("MEAN(2,1,3)")));
            assertTrue(resembles(calcTerm("MEAN(1,2)"),calcTerm("MEAN(2,1,0)")));
            assertTrue(resembles(calcTerm("MEAN(1,23,32523,12,124,154,-123,12.124)"),"4090.7655"));
            assertTrue(resembles(calcTerm("MEAN(1,23,0,-12,124,12,2.3125,13.154,-123,12.124)"),"5.25905"));

        //var
            assertTrue(resembles(calcTerm("VAR(1,2,3)"),calcTerm("VAR(2,1,3)")));

        //s
        assertTrue(resembles(calcTerm("SRRRR0"),"0"));
        assertTrue(resembles(calcTerm("SRRRR1"),"0"));
        assertTrue(resembles(calcTerm("SRRRR-1.214"),"0"));

        assertTrue(resembles(calcTerm("SRRRR1,2,3"),"0.81649658092772603273242802"));
        assertTrue(resembles(calcTerm("SRRRR1.12,124.12,12.241,-12.124124,40783420,-792.2"),"15199132.952412"));

    }

    @Test public void testMemoryFunctions() {
        currentMode = new RoboMenuItem(R.id.memory);

        calcTerm("");
        mainActivity.findViewById(R.id.btn_21).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_11).performClick();
        assertEquals("",((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString());
        mainActivity.findViewById(R.id.btn_clearall).performClick();

        calcTerm("1+1");
        mainActivity.findViewById(R.id.btn_22).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_12).performClick();
        assertEquals("1+1",((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString());
        mainActivity.findViewById(R.id.btn_clearall).performClick();

        calcTerm("1+224*124");
        mainActivity.setSelectionEingabe(0,5);
        mainActivity.findViewById(R.id.btn_23).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_13).performClick();
        assertEquals("1+224",((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString());
        mainActivity.findViewById(R.id.btn_clearall).performClick();

        calcTerm("1+224*124");
        mainActivity.setSelectionEingabe(2,9);
        mainActivity.findViewById(R.id.btn_23).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_13).performClick();
        assertEquals("224*124",((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString());
        mainActivity.findViewById(R.id.btn_clearall).performClick();

        calcTerm("101*9");
        mainActivity.findViewById(R.id.eT_ausgabe).requestFocus();
        mainActivity.setSelectionAusgabe(0,3);
        mainActivity.findViewById(R.id.btn_24).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_14).performClick();
        assertEquals("909",((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString());
        mainActivity.findViewById(R.id.btn_clearall).performClick();

        calcTerm("101*9");
        mainActivity.findViewById(R.id.eT_ausgabe).requestFocus();
        mainActivity.setSelectionAusgabe(0,2);
        mainActivity.findViewById(R.id.btn_25).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_15).performClick();
        assertEquals("90",((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString());
        mainActivity.findViewById(R.id.btn_clearall).performClick();

        calcTerm("101*9");
        mainActivity.findViewById(R.id.eT_ausgabe).requestFocus();
        mainActivity.setSelectionAusgabe(1,3);
        mainActivity.findViewById(R.id.btn_25).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_15).performClick();
        assertEquals("09",((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString());
        mainActivity.findViewById(R.id.btn_clearall).performClick();
    }

    @Test public void testVoids(){
        currentMode = new RoboMenuItem(R.id.basic2);
        for(int i=0; i<iterationsSubtests; i++) {
            int a = (int) ((Math.random() * 1000) - 500);
            double x = ((Math.random() * 1000) - 500);

            assertTrue(resembles(calcTerm(x+">%"),calcTerm(((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString()+"*100"))); mainActivity.findViewById(R.id.btn_clearall).performClick();
            assertTrue(resembles(calcTerm(x+">+/-"),calcTerm(((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString()+"*-1"))); mainActivity.findViewById(R.id.btn_clearall).performClick();
            assertTrue(resembles(calcTerm("3"+">x\u207B\u00B9"),calcTerm("1/3"/*+((EditText) mainActivity.findViewById(R.id.eT_eingabe)).getText().toString()*/))); mainActivity.findViewById(R.id.btn_clearall).performClick();

            calcTerm(x+">A/B"); String output = ((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString(); String result = calcTerm(output);
            assertTrue(resembles(String.valueOf(x),result,4)); mainActivity.findViewById(R.id.btn_clearall).performClick();

            calcTerm(a+"PFZ"); output = ((EditText) mainActivity.findViewById(R.id.eT_ausgabe)).getText().toString(); result = calcTerm(output.replace("(","").replace(")","").replace(",","*"));
            assertTrue(resembles(String.valueOf(a),result)); mainActivity.findViewById(R.id.btn_clearall).performClick();
        }
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

        String[] fun1 = new String[]{"π","e","^","LOG","LN","LB","³√","√","³","²","10^x","!"};
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i], new RoboMenuItem(R.id.basic));}
        fun1 = new String[]{"PFZ","GCD","LCM","∑","∏","",">%",">A/B",">x⁻¹",">+/-","MIN","MAX"};
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.basic2));}
        fun1 = new String[]{"SIN","COS","TAN","COT","SEC","CSC","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC"};
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.trigo));}
        fun1 = new String[]{"SINH","COSH","TANH","ASINH","ACOSH","ATANH",">DEG",">RAD","","","",""};
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.hyper));}
        fun1 = new String[]{"AND","OR","XOR","NOT",">BIN",">OCT",">DEC",">HEX","","","",""};
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.logic));}
        fun1 = new String[]{"ZN","ZB","NCR","NPR","MEAN","VAR","S","","","","",""};
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
        fun1 = new String[]{"","GCD(,)","LCM(,)","∑(,)","∏(,)","","","","","","MIN(,)","MAX(,)"};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.trigo));
        fun2 = new String[]{"SIN","COS","TAN","COT","SEC","CSC","ASIN","ACOS","ATAN","ACOT","",""};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun2[i]);
        }
        fun1 = new String[]{"SIN","COS","TAN","COT","SEC","CSC","ASIN","ACOS","ATAN","ACOT","",""};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.hyper));
        fun2 = new String[]{"SINH","COSH","TANH","ASINH","ACOSH","ATANH",">DEG",">RAD","","","",""};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun2[i]);
        }
        fun1 = new String[]{"SINH","COSH","TANH","ASINH","ACOSH","ATANH","","","","","",""};
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
        fun2 = new String[]{"ZN","ZB","NCR","NPR","MEAN","VAR","E","S","","","",""};
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

    public static boolean resembles(String output, String expectedResult){
        double tolerance = 1/Math.pow(10,toleranceDigits);

        if(expectedResult.equals("Math Error") || output.equals("Math Error")){
            System.out.println("resembleS Math Error: "+output+" "+expectedResult);
            return expectedResult.equals(output);
        }
        int minlength = Math.min(Math.min(expectedResult.length(),output.length()),20);
        if(expectedResult.startsWith(".") || expectedResult.startsWith("-."))expectedResult=expectedResult.replace(".","0."); //TODO: solve better
        if(output.startsWith(expectedResult.substring(0,Math.min(minlength,CalcModel.precisionDigits)))){return true;}
        else{
            if(Math.abs(Double.valueOf(output) - Double.valueOf(expectedResult)) < tolerance){return true;
            } else{
                System.out.println("resembleS false: "+output+" "+expectedResult);
                return false;
            }
        }
    }

    public static boolean resembles(String expectedResult, String output,int toleranceDigits){
        double tolerance = 1/Math.pow(10,toleranceDigits);

        int minlength = Math.min(Math.min(expectedResult.length(),output.length()),20);
        if(expectedResult.startsWith(".") || expectedResult.startsWith("-."))expectedResult=expectedResult.replace(".","0."); //TODO: solve better
        if(output.startsWith(expectedResult.substring(0,Math.min(minlength,CalcModel.precisionDigits)))){return true;}
        else{
            if(Math.abs(Double.valueOf(output) - Double.valueOf(expectedResult)) < tolerance){return true;
            } else{
                System.out.println("resembleS false: "+output+" "+expectedResult+" dif:"+Math.abs(Double.valueOf(output) - Double.valueOf(expectedResult)));
                return false;
            }
        }
    }

    public static boolean resembles(double expectedResult, double output){
        if(Math.abs(Double.valueOf(output) - Double.valueOf(expectedResult)) < 0.0000000001){return true;
            } else{
                System.out.println("resembles false: "+output+" "+expectedResult);
                return false;
        }
    }

}
