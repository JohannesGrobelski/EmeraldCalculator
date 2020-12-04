package com.example.titancalculator;

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
import org.robolectric.fakes.RoboMenuItem;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class IntegrationTestMath {
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
            "Zn","Zb","NCR","NPR","MEAN","VAR","S",
            "M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6",
            "L","R"
    };
    private HashMap<String,Integer> translateFunctionToNumberParameters;
    private MainActivity mainActivity;
    private RoboMenuItem currentMode;



    /**
     * Terms that involve +,-,*,/
     */
    @Test
    public void testSimpleTerms(){
        calcTerm("-48.370724610365045+-15.606885665491266");

        for(int i=0; i<iterationsSubtests; i++){
            int op1 = (int) (Math.random()*1000) + 1;
            int op2 = (int) (Math.random()*1000) + 1;
            //Check the addition of two integer numbers.
            Assert.assertTrue(testEquation(op1+"+"+op2,String.valueOf(op1+op2)));
            //Check the subtraction of two integer numbers.
            Assert.assertTrue(testEquation(op1+"-"+op2,String.valueOf(op1-op2)));
            //Check the multiplication of two integer numbers.
            Assert.assertTrue(testEquation(op1+"*"+op2,String.valueOf(op1*op2)));
            //Check the division of two integer numbers.
            Assert.assertTrue(testEquation(op1+"/"+op2,String.valueOf(op1/op2)));
        }

        for(int i=0; i<iterationsSubtests; i++){
            double op1 = Math.random()*iterationsSubtests;
            double op2 = Math.random()*iterationsSubtests;
            //Check the addition of two negative numbers.
            Assert.assertTrue(testEquation("-"+op1+"+-"+op2,String.valueOf(-op1+-op2)));
            //Check the addition of one positive and one negative number.
            Assert.assertTrue(testEquation("-"+op1+"+"+op2,String.valueOf(-op1+op2)));
            //Check the subtraction of two negative numbers.
            Assert.assertTrue(testEquation("-"+op1+"--"+op2,String.valueOf(-op1-(-op2))));
            //Check the subtraction of one negative and one positive number.
            Assert.assertTrue(testEquation(+op1+"--"+op2,String.valueOf(op1-(-op2))));
            //Check the multiplication of two negative numbers.
            Assert.assertTrue(testEquation("-"+op1+"*-"+op2,String.valueOf(-op1*-op2)));
            //Check the multiplication of one negative and one positive number.
            Assert.assertTrue(testEquation("-"+op1+"*"+op2,String.valueOf(-op1*op2)));
            //Check the division of two negative numbers.
            Assert.assertTrue(testEquation("-"+op1+"/-"+op2,String.valueOf(-op1/-op2)));
            //Check the division of one positive number and one integer number.
            int op3 = (int) (Math.random()*1000);
            Assert.assertTrue(testEquation("-"+op1+"/"+op3,String.valueOf(-op1/op3)));
            //Check the division of a number by zero.
            Assert.assertTrue(testEquation(op1+"/0","Math Error"));
            //Check the division of a number by negative number.
            Assert.assertTrue(testEquation("-"+op1+"/-"+op2,String.valueOf(-op1/-op2)));
            //Check the division of zero by any number.
            Assert.assertTrue(testEquation("0/"+op1,"0"));
        }


    }

    @Test public void testBasicFunctions(){
        currentMode = new RoboMenuItem(R.id.basic);

        Assert.assertTrue(testEquation("π","3.141592653589793"));
        Assert.assertTrue(testEquation("e","2.718281828459045"));
        Assert.assertTrue(testEquation("e^0","1"));
        Assert.assertTrue(testEquation("LN(1)","0"));
        Assert.assertTrue(testEquation("LN(e)","1"));
        Assert.assertTrue(testEquation("0!","1"));
        Assert.assertTrue(testEquation("0.5!","Math Error"));
        Assert.assertTrue(testEquation("1!","1"));

        for(int i=0; i<iterationsSubtests; i++) {
            double op1 = (Math.random() * 10) - 0;
            double op2 = (Math.random() * 10) - 0;
            double op3 = Math.random() * 10;
            Assert.assertTrue(testEquation(op1+"+"+op2,op2+"+"+op1)); //a+b = b+a
            Assert.assertTrue(testEquation(op1+"*"+op2,op2+"*"+op1)); //a*b = b*a
            Assert.assertTrue(testEquation(op1+"+("+op2+"+"+op3+")","("+op1+"+"+op2+")+"+op3)); //a+(b+c) = (a+b)+c
            Assert.assertTrue(testEquation(op1+"*("+op2+"*"+op3+")","("+op1+"*"+op2+")*"+op3)); //a*(b*c) = (a*b)*c
            Assert.assertTrue(testEquation(op1+"*("+op2+"+"+op3+")",op1+"*"+op2+"+"+op1+"*"+op3));//a*(b+c) = a*b + a*c
            Assert.assertTrue(testEquation("("+op1+"+"+op2+")*"+op3,op1+"*"+op3+"+"+op2+"*"+op3));//(a+b)*c = a*c + b*c

            Assert.assertTrue(testEquation(op1+"^0","1")); //a^0 = 1
            Assert.assertTrue(testEquation(op1+"^1",String.valueOf(op1))); //a^1 = a
            Assert.assertTrue(testEquation(op1+"^-1","1/"+op1)); //a ^ -1 = 1/a
            Assert.assertTrue(testEquation(op1+"^-"+op2,"1/"+op1+"^"+op2)); //a^-b = 1/a^b
            Assert.assertTrue(testEquation(op1+"√"+op2,op2+"^(1/"+op1+")")); //a√b = b^(1/a)
            Assert.assertTrue(testEquation(op3+"^("+op1+"+"+op2+")","("+op3+"^"+op2+")*("+op3+"^"+op1+")"));  //a^(b+c) = a^c + a^c
            Assert.assertTrue(testEquation(op3+"^("+op1+"-"+op2+")","("+op3+"^"+op1+")/("+op3+"^"+op2+")")); //a^(b-c) = a^b / a^c
            Assert.assertTrue(testEquation("("+op1+"^"+op2+")^"+op3,op1+"^("+op2+"*"+op3+")")); //(a^b)^c = a^(b^c)
            Assert.assertTrue(testEquation("("+op1+"*"+op2+")^"+op3,op1+"^"+op3+"*"+op2+"^"+op3)); //(a*b)^c = a^c * b^c
            Assert.assertTrue(testEquation(op1+"^"+op3+"/"+op2+"^"+op3,"("+op1+"/"+op2+")^"+op3)); //a^c / b^c = (a/b)^c

            Assert.assertTrue(testEquation("e^"+op1+"*"+"e^"+op2,"e^("+op1+"+"+op2+")")); //e^a*e^b = e^(b+c)
            Assert.assertTrue(testEquation("e^"+op1+"/"+"e^"+op2,"e^("+op1+"-"+op2+")")); //e^a/e^b = e^(b-c)
            Assert.assertTrue(testEquation("(e^"+op1+")"+"^"+op2,"e^("+op1+"*"+op2+")")); //(e^a)^b = e^(a*b)
            Assert.assertTrue(testEquation("e^-"+op1,"1/e^"+op1)); //e^-a = 1/e^a
            Assert.assertTrue(testEquation("LN(e^"+op1+")",String.valueOf(op1))); //LN(e^a) = a
            Assert.assertTrue(testEquation("LN("+op1+")+LN("+op2+")","LN("+op1+"*"+op2+")")); //LN(a) + LN(b) = LN(a*b)
            Assert.assertTrue(testEquation("LN("+op1+")-LN("+op2+")","LN("+op1+"/"+op2+")")); //LN(a) - LN(b) = LN(a/b)
            Assert.assertTrue(testEquation("LN("+op1+"^"+op2+")",op2+"*LN("+op1+")")); //LN(a^b) = b*LN(a)

            Assert.assertTrue(testEquation("LOG("+op1+")+LOG("+op2+")","LOG("+op1+"*"+op2+")")); //LOG(a) + LOG(b) = LOG(a*b)
            Assert.assertTrue(testEquation("LOG("+op1+")-LOG("+op2+")","LOG("+op1+"/"+op2+")")); //LOG(a) - LOG(b) = LOG(a/b)
            Assert.assertTrue(testEquation("LOG("+op1+"^"+op2+")",op2+"*LOG("+op1+")")); //LOG(a^b) = b*LOG(a)
        }
    }

    @Test public void testBasic2Functions() {
        currentMode = new RoboMenuItem(R.id.basic2);

        for(int i=0; i<3; i++) {
            int inta = (int) ((Math.random() * 1000) - 500);
            int intb = (int) ((Math.random() * 1000) - 500);
            int intc = (int) ((Math.random() * 1000) - 500);
            int intm = (int) ((Math.random() * 1000));

            double op1 = (Math.random() * 10) - 0;
            double op2 = (Math.random() * 10) - 0;
            double op3 = (Math.random() * 10) - 0;
            double d = (Math.random() * 10) - 0;

            Assert.assertTrue(testEquation("GCD" + inta + "R0", String.valueOf(Math.abs(inta)))); //gcd(a, 0) = |a|, for a ≠ 0
            for(int div = 1; div<Math.min(Math.abs(inta),Math.abs(intb)); div++){ //Every common divisor of a and b is a divisor of gcd(a, b).
                if(Math.abs(inta) % div == 0 && Math.abs(intb) % div == 0){
                    Assert.assertTrue(Integer.valueOf(calcTerm("GCD"+inta+"R"+intb)) % div == 0);
                }
            }
            Assert.assertTrue(testEquation("GCD"+inta+"R"+intb,"GCD"+intb+"R"+inta)); // gcd(a,b) = gcd(b, a).
            Assert.assertTrue(testEquation("LCM"+inta+"R"+intb,"LCM"+intb+"R"+inta)); // lcm(a,b) = lcm(b, a).
            Assert.assertTrue(testEquation("GCD"+inta+"RGCD"+intb+"R"+intc,"GCD"+intc+"RGCD"+inta+"R"+intb)); //gcd(a, gcd(b, c)) = gcd(gcd(a, b), c)
            Assert.assertTrue(testEquation("LCM"+inta+"RLCM"+intb+"R"+intc,"LCM"+intc+"RLCM"+inta+"R"+intb)); //lcm(a, lcm(b, c)) = lcm(lcm(a, b), c)
            Assert.assertTrue(testEquation("LCM"+inta+"RGCD"+inta+"R"+intb,String.valueOf(Math.abs(inta))));//lcm(a,gcd(a,b)) = |a|
            Assert.assertTrue(testEquation("GCD"+inta+"RLCM"+inta+"R"+intb,String.valueOf(Math.abs(inta))));//gcd(a,lcm(a,b)) = |a|
            Assert.assertTrue(testEquation("GCD"+inta+"R"+inta,String.valueOf(Math.abs(inta)))); //gcd(a,a) = |a|
            Assert.assertTrue(testEquation("LCM"+inta+"R"+inta,String.valueOf(Math.abs(inta)))); //lcm(a,a) = |a|
            Assert.assertTrue(testEquation("GCD"+intm+"*"+inta+"R"+intm+"*"+intb,"(GCD"+inta+"R"+intb+")*"+intm)); //gcd(m⋅a, m⋅b) = m⋅gcd(a, b).
            Assert.assertTrue(testEquation("LCM"+intm+"*"+inta+"R"+intm+"*"+intb,"(LCM"+inta+"R"+intb+")*"+intm)); //lcm(m⋅a, m⋅b) = m⋅lcm(a, b).
            Assert.assertTrue(testEquation("GCD"+inta+"+("+intm+"*"+intb+")R"+intb,"(GCD"+inta+"R"+intb)); //gcd(a + m⋅b, b) = gcd(a, b).
            Assert.assertTrue(testEquation("LCM"+inta+"+("+intm+"*"+intb+")R"+intb,"(LCM"+inta+"R"+intb)); //lcm(a + m⋅b, b) = lcm(a, b).
            Assert.assertTrue(testEquation(String.valueOf(Integer.valueOf(calcTerm("(GCD"+inta+"R"+intb+")"))*Integer.valueOf(calcTerm("(LCM"+inta+"R"+intb+")"))),String.valueOf(Math.abs(inta*intb))));
            Assert.assertTrue(testEquation("GCD"+inta+"R"+"LCM"+intb+"R"+intc,"LCM"+"GCD"+inta+"R"+intb+"R"+"GCD"+inta+"R"+intc)); //gcd(a, lcm(b, c)) = lcm(gcd(a, b), gcd(a, c))
            Assert.assertTrue(testEquation("LCM"+inta+"R"+"GCD"+intb+"R"+intc,"GCD"+"LCM"+inta+"R"+intb+"R"+"LCM"+inta+"R"+intc)); //lcm(a, gcd(b, c)) = gcd(lcm(a, b), lcm(a, c))
            Assert.assertTrue(testEquation("∑"+"1"+"R"+intm,"("+intm+"("+intm+"+1))/2")); //∑1..m = m(m+1) / 2
            Assert.assertTrue(testEquation("MAX"+op1+"R"+op2,"-MIN"+"-"+op1+"R-"+op2)); //max(a,b) = -min(-a,-b)
            Assert.assertTrue(testEquation("MIN"+op1+"R"+op2,"-MAX"+"-"+op1+"R-"+op2)); //min(a,b) = -max(-a,-b)
            Assert.assertTrue(testEquation("(MAX"+op1+"R"+op2+")+"+op3,"MAX"+op1+"+"+op3+"R"+op2+"+"+op3)); //max(a,b)+c = max(a+c,b+c)
            Assert.assertTrue(testEquation("(MIN"+op1+"R"+op2+")+"+op3,"MIN"+op1+"+"+op3+"R"+op2+"+"+op3)); //min(a,b)+c = min(a+c,b+c)
            Assert.assertTrue(testEquation("(MAX"+op1+"R"+op2+")*"+d,"MAX"+op1+"*"+d+"R"+op2+"*"+d)); //d*max(a,b) = max(d*a,d*b)
            Assert.assertTrue(testEquation("(MIN"+op1+"R"+op2+")*"+d,"MIN"+op1+"*"+d+"R"+op2+"*"+d)); //d*min(a,b) = min(d*a,d*b)
        }
    }

    @Test public void testTrigoAndHyperFunctions() {
        currentMode = new RoboMenuItem(R.id.trigo);

        for(int i=0; i<iterationsSubtests; i++) {
            double x = (Math.random() * 10) + 1;
            double y = (Math.random() * 10) + 1;
            int a = (int) ((Math.random() * 50) - 25);
            int b = (int) ((Math.random() * 50) - 25);

            Assert.assertTrue(MathEvaluator.resembles(calcTerm("SIN("+x*Math.toRadians(Math.PI)+")"),"0",2));
            if(x == ((double) ((int) x)))x += .1;
            double complex = (a*Math.toRadians(Math.PI))+(0.5*Math.toRadians(Math.PI));
            Assert.assertTrue(testEquation("COS("+complex+")","0"));
            Assert.assertTrue(testEquation("TAN("+x*Math.toRadians(Math.PI)+")","0"));

            Assert.assertTrue(testEquation("SIN("+x+")/COS("+x+")","TAN("+x+")")); //tan(x) = sin(x) / cot(x)
            Assert.assertTrue(testEquation("COT("+(Math.toDegrees(1.5*Math.PI))+")","0"));
            Assert.assertTrue(testEquation("1/TAN("+x+")","COT("+x+")")); //cot(x) = 1 / tan(x)

            Assert.assertTrue(testEquation("SIN("+Math.toDegrees(Math.PI/2)+"-"+x+")","COS("+x+")")); //cos(x) = sin(π/2 - x)
            Assert.assertTrue(testEquation("COS("+Math.toDegrees(Math.PI/2)+"-"+x+")","SIN("+x+")")); //sin(x) = cos(π/2 - x)
            Assert.assertTrue(testEquation("TAN("+Math.toDegrees(Math.PI/2)+"-"+x+")","COT("+x+")")); //cot(x) = tan(π/2 - x)
            Assert.assertTrue(testEquation("COT("+Math.toDegrees(Math.PI/2)+"-"+x+")","TAN("+x+")")); //tan(x) = cot(π/2 - x)
            Assert.assertTrue(testEquation("SEC("+Math.toDegrees(Math.PI/2)+"-"+x+")","CSC("+x+")")); //sec(x) = csc(π/2 - x)
            Assert.assertTrue(testEquation("SEC("+Math.toDegrees(Math.PI/2)+"-"+x+")","CSC("+x+")")); //sec(x) = csc(π/2 - x)
            Assert.assertTrue(testEquation("CSC("+Math.toDegrees(Math.PI/2)+"-"+x+")","SEC("+x+")")); //csc(x) = sec(π/2 - x)
            Assert.assertTrue(testEquation("SIN(-"+x+")","-SIN("+x+")")); //sin(-x) = -sin(x)
            Assert.assertTrue(testEquation("COS(-"+x+")","COS("+x+")")); //cos(-x) = cos(x)
            Assert.assertTrue(testEquation("TAN(-"+x+")","-TAN("+x+")")); //tan(-x) = -tan(x)
            Assert.assertTrue(testEquation("SIN("+x+"+"+y+")","SIN("+x+")*COS("+y+")+COS("+x+")*SIN("+y+")")); //sin(x+y) = sin(x)*cos(y)+cos(x)*sin(y)
            Assert.assertTrue(testEquation("COS("+x+"+"+y+")","COS("+x+")*COS("+y+")-SIN("+x+")*SIN("+y+")")); //cos(x+y) = cos(x)*cos(y)-sin(x)*sin(y)
            Assert.assertTrue(testEquation("SIN("+x+"-"+y+")","SIN("+x+")*COS("+y+")-COS("+x+")*SIN("+y+")")); //sin(x-y) = cos(x)*cos(y)-sin(x)*sin(y)
            Assert.assertTrue(testEquation("COS("+x+"-"+y+")","COS("+x+")*COS("+y+")+SIN("+x+")*SIN("+y+")")); //cos(x-y) = cos(x)*cos(y)+sin(x)*sin(y)
            double x1 = Math.random() - 0.5;
            Assert.assertTrue(testEquation("SIN(ASIN("+x1+"))",String.valueOf(x1))); //sin(asin(x)) = x
            Assert.assertTrue(testEquation("COS(ACOS("+x1+"))",String.valueOf(x1))); //cos(acos(x)) = x
            Assert.assertTrue(testEquation("TAN(ATAN("+x1+"))",String.valueOf(x1))); //tan(atan(x)) = x
            Assert.assertTrue(testEquation("COT(ACOT("+x1+"))",String.valueOf(x1))); //cot(acot(x)) = x
            Assert.assertTrue(testEquation("SEC(ASEC("+x+"))",String.valueOf(x))); //sec(asec(x)) = x
            Assert.assertTrue(testEquation("CSC(ACSC("+x+"))",String.valueOf(x))); //csc(acsc(x)) = x
            Assert.assertTrue(MathEvaluator.resembles(MathEvaluator.toDEG(MathEvaluator.toRAD("0")),"0")); //DEG(RAD(0)) = 0
            Assert.assertTrue(MathEvaluator.resembles(MathEvaluator.toDEG(MathEvaluator.toRAD(String.valueOf(a))),String.valueOf(a))); //DEG(RAD(0)) = 0
        }
        Assert.assertTrue(MathEvaluator.resembles(MathEvaluator.toDEG("0"),"0")); //DEG(0) = 0
        Assert.assertTrue(MathEvaluator.resembles(MathEvaluator.toDEG(calcTerm("π")),"180")); //DEG(π) = 180
        Assert.assertTrue(MathEvaluator.resembles(MathEvaluator.toDEG(calcTerm("2*π")),"360")); //DEG(2π) = 360
        Assert.assertTrue(MathEvaluator.resembles(MathEvaluator.toRAD("0"),"0")); //RAD(0) = 0
        Assert.assertTrue(MathEvaluator.resembles(MathEvaluator.toRAD(("180")),calcTerm("π"))); //RAD(180) = π
        Assert.assertTrue(MathEvaluator.resembles(MathEvaluator.toRAD(("360")),calcTerm("2*π"))); //RAD(360) = 2π
    }



    @Test public void testLogicFunctions() {
        currentMode = new RoboMenuItem(R.id.trigo);

        for(int i=0; i<iterationsSubtests; i++) {
            int a = (int) ((Math.random() * 1000) - 500);
            int b = (int) ((Math.random() * 1000) - 500);
            int c = (int) ((Math.random() * 1000) - 500);
            /*
            assertTrue(testEquation("ANDLL"+a+"R"+b,"ANDLL"+b+"R"+a));    //a and b = b and a
            assertTrue(testEquation("ANDLL"+a+"R"+b,"ANDLL"+b+"R"+a));    //a or b = b or a
            assertTrue(testEquation("AND"+a+"RAND"+b+"R"+c,"AND"+c+"RAND"+a+"R"+b)); //and(a, and(b, c)) = and(and(a, b), c)
            assertTrue(testEquation("OR"+a+"ROR"+b+"R"+c,"OR"+c+"ROR"+a+"R"+b)); //or(a, or(b, c)) = or(or(a, b), c)
            assertTrue(testEquation("ANDLL"+a+"R0","0"));    //a and 0 = 0

            assertTrue(testEquation("ANDLL"+a+"RORLLL"+a+"R1",String.valueOf(a)));    //a and a or 1 = a
            assertTrue(testEquation("ANDLL"+a+"RORLLL"+a+"R1","ANDLL"+a+"R1"));    //a and a or 1 = a or 1
             */
            Assert.assertTrue(testEquation("NOTLNOT"+a,String.valueOf(a)));    //not(not(a)) = a

            Assert.assertTrue(testEquation("ANDLL"+a+"R"+"NOT("+a+"))","0"));    //a and not(a) = 0
            Assert.assertTrue(testEquation("NOT(1)","-2"));    //not(0) = 1
            Assert.assertTrue(testEquation("NOT(0)","-1"));    //not(1) = 0
        }


    }


    @Test public void testStatisticFunctions() {
        currentMode = new RoboMenuItem(R.id.statistic);

        for(int i=0; i<iterationsSubtests; i++) {
            int a = ((int) ((Math.random() * 1000))) + 10;
            int b = a+100;

            int n = (int) ((Math.random() * 1000));
            int r = (int) ((Math.random() * 1000));

            double x = ((Math.random() * 1000) - 500);

            calcTerm("Zn"+a);
            String output = ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString();
            Assert.assertTrue("output: "+output+", "+a,output.matches("[0-9]+"));
            Integer result = Integer.valueOf(output);
            Assert.assertTrue(result > 0); Assert.assertTrue(result <= a);  //0 < result < a
            calcTerm("Zb("+a+"R"+b+")");  result = Integer.valueOf(((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString());
            Assert.assertTrue(result > a); Assert.assertTrue(result <= b);  //0 < result < a
            Assert.assertTrue(testEquation("NPR("+n+"R"+r+")",n+"!"+"/("+n+"-"+r+")!")); //nPr(n,r)=n!/(n−r)!
            Assert.assertTrue(testEquation("NCR("+n+"R"+r+")","NPR("+n+"R"+r+")/"+r+"!")); //nCr(n,r)=nPr(n,r)/r!

        }

        //mean
        Assert.assertTrue(MathEvaluator.resembles(calcTerm("MEAN()"),"Math Error"));
        Assert.assertTrue(MathEvaluator.resembles(calcTerm("MEAN(-1,0,1)"),calcTerm("0")));
        Assert.assertTrue(MathEvaluator.resembles(calcTerm("MEAN(1,2,3)"),calcTerm("MEAN(2,1,3)")));
        Assert.assertTrue(MathEvaluator.resembles(calcTerm("MEAN(1,2)"),calcTerm("MEAN(2,1,0)")));
        Assert.assertTrue(MathEvaluator.resembles(calcTerm("MEAN(1,23,32523,12,124,154,-123,12.124)"),"4090.7655"));
        Assert.assertTrue(MathEvaluator.resembles(calcTerm("MEAN(1,23,0,-12,124,12,2.3125,13.154,-123,12.124)"),"5.25905"));

        //var
        Assert.assertTrue(MathEvaluator.resembles(calcTerm("VAR(1,2,3)"),calcTerm("VAR(2,1,3)")));

        //s
        Assert.assertTrue(MathEvaluator.resembles(calcTerm("SRRRR0"),"0"));
        Assert.assertTrue(MathEvaluator.resembles(calcTerm("SRRRR1"),"0"));
        Assert.assertTrue(MathEvaluator.resembles(calcTerm("SRRRR-1.214"),"0"));

        Assert.assertTrue(MathEvaluator.resembles(calcTerm("SRRRR1,2,3"),"0.81649658092772603273242802"));
        Assert.assertTrue(MathEvaluator.resembles(calcTerm("SRRRR1.12,124.12,12.241,-12.124124,40783420,-792.2"),"15199132.952412"));
    }

    @Test public void testMemoryFunctions() {
        currentMode = new RoboMenuItem(R.id.memory);

        calcTerm("");
        mainActivity.findViewById(R.id.btn_21).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_11).performClick();
        Assert.assertEquals("",((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
        mainActivity.findViewById(R.id.btn_clearall).performClick();

        calcTerm("1+1");
        mainActivity.findViewById(R.id.btn_22).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_12).performClick();
        Assert.assertEquals("1+1",((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
        mainActivity.findViewById(R.id.btn_clearall).performClick();

        calcTerm("1+224*124");
        mainActivity.setSelectionInput(0,5);
        mainActivity.findViewById(R.id.btn_23).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_13).performClick();
        Assert.assertEquals("1+224",((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
        mainActivity.findViewById(R.id.btn_clearall).performClick();

        calcTerm("1+224*124");
        mainActivity.setSelectionInput(2,9);
        mainActivity.findViewById(R.id.btn_23).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_13).performClick();
        Assert.assertEquals("224*124",((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
        mainActivity.findViewById(R.id.btn_clearall).performClick();

        calcTerm("101*9");
        mainActivity.findViewById(R.id.eT_output).requestFocus();
        mainActivity.setSelectionOutput(0,3);
        mainActivity.findViewById(R.id.btn_24).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_14).performClick();
        Assert.assertEquals("909",((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
        mainActivity.findViewById(R.id.btn_clearall).performClick();

        calcTerm("101*9");
        mainActivity.findViewById(R.id.eT_output).requestFocus();
        mainActivity.setSelectionOutput(0,2);
        mainActivity.findViewById(R.id.btn_25).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_15).performClick();
        Assert.assertEquals("90",((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
        mainActivity.findViewById(R.id.btn_clearall).performClick();

        calcTerm("101*9");
        mainActivity.findViewById(R.id.eT_output).requestFocus();
        mainActivity.setSelectionOutput(1,3);
        mainActivity.findViewById(R.id.btn_25).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_15).performClick();
        Assert.assertEquals("09",((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
        mainActivity.findViewById(R.id.btn_clearall).performClick();
    }

    @Test public void testVoids(){
        currentMode = new RoboMenuItem(R.id.basic2);
        for(int i=0; i<iterationsSubtests; i++) {
            int a = (int) ((Math.random() * 1000) - 500);
            double x = ((Math.random() * 1000) - 500);

            Assert.assertTrue(MathEvaluator.resembles(calcTerm(x+">%"),calcTerm(((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString()+"*100"))); mainActivity.findViewById(R.id.btn_clearall).performClick();
            Assert.assertTrue(MathEvaluator.resembles(calcTerm(x+">+/-"),calcTerm(((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString()+"*-1"))); mainActivity.findViewById(R.id.btn_clearall).performClick();
            Assert.assertTrue(MathEvaluator.resembles(calcTerm("3"+">x\u207B\u00B9"),calcTerm("1/3"/*+((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString()*/))); mainActivity.findViewById(R.id.btn_clearall).performClick();

            calcTerm(x+">A/B"); String output = ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString(); String result = calcTerm(output);
            Assert.assertTrue(MathEvaluator.resembles(String.valueOf(x),result,4)); mainActivity.findViewById(R.id.btn_clearall).performClick();

            calcTerm(a+"PFZ"); output = ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString(); result = calcTerm(output.replace("(","").replace(")","").replace(",","*"));
            Assert.assertTrue(MathEvaluator.resembles(String.valueOf(a),result)); mainActivity.findViewById(R.id.btn_clearall).performClick();
        }
    }

    @Test public void testNavigationButtons(){
        mainActivity.findViewById(R.id.btn_1).performClick();
        int pos1 = ((EditText) mainActivity.findViewById(R.id.eT_input)).getSelectionStart();
        mainActivity.findViewById(R.id.btn_LINKS).performClick();
        mainActivity.findViewById(R.id.btn_LINKS).performClick();
        int pos2 = ((EditText) mainActivity.findViewById(R.id.eT_input)).getSelectionStart();
        mainActivity.findViewById(R.id.btn_RECHTS).performClick();
        mainActivity.findViewById(R.id.btn_RECHTS).performClick();
        int pos3 = ((EditText) mainActivity.findViewById(R.id.eT_input)).getSelectionStart();
        Assert.assertEquals(pos1,pos3);
        Assert.assertEquals(pos1,pos2+1);

        mainActivity.findViewById(R.id.btn_0).performClick();
        mainActivity.findViewById(R.id.btn_LINKS).performClick();
        mainActivity.findViewById(R.id.btn_clear).performClick();
        System.out.println(mainActivity.getInputText());
        String i1 = ((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        String i2 = ((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString();
        Assert.assertEquals(i1,"0");
        Assert.assertEquals(i2,"");
    }

    @Test public void testANSFunction(){
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_1).performClick();
        mainActivity.findViewById(R.id.btn_add).performClick();
        mainActivity.findViewById(R.id.btn_1).performClick();
        mainActivity.findViewById(R.id.btn_eq).performClick();
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_ANS).performClick();
        mainActivity.findViewById(R.id.btn_eq).performClick();
        String ans2 = ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString();

        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.findViewById(R.id.btn_ANS).performClick();
        mainActivity.findViewById(R.id.btn_mul).performClick();
        mainActivity.findViewById(R.id.btn_2).performClick();
        mainActivity.findViewById(R.id.btn_eq).performClick();
        String ans3 = ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString();

        Assert.assertEquals(ans2,"2");
        Assert.assertEquals(ans3,"4");
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
        System.out.println("testEquation result: "+output+" ?= "+expectedResult);
        Assert.assertTrue(output.matches("Math Error") || output.matches("[0-9]+") || output.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"));
        Assert.assertTrue(expectedResult.matches("Math Error") || expectedResult.matches("[0-9]+") || expectedResult.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"));

        if(output.contains("E") && !output.contains("Math Error")){
            mainActivity.findViewById(R.id.btn_eq).performLongClick();
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
        mainActivity.findViewById(R.id.btn_clearall).performClick();
        mainActivity.onOptionsItemSelected(currentMode);
        RoboMenuItem mode = currentMode;
        mainActivity.setSelectionInput(0,0);
        mainActivity.eT_input.requestFocus();

        System.out.println("calcTerm: \""+term+"\"");
        String[] splitted = StringUtils.splitTokens(term);
        boolean inputShouldEqualtTerm = true; boolean containsOutputFunctions = false;
        System.out.println("cT  "+ Arrays.toString(splitted));
        Assert.assertEquals("",mainActivity.eT_input.getText().toString()); Assert.assertEquals("",mainActivity.eT_output.getText().toString());
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
                    for(int i=0; i<s.length(); i++)mainActivity.findViewById(R.id.btn_RECHTS).performClick();
                }
            } else {
                System.out.println("unknown lexical unit: "+s);
                Assert.assertTrue("calcTerm: Button doesnt exist: "+s,false);
            }
        }
        System.out.println("cT final input: "+((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString()+"\n\n");
        System.out.println("cT final output: "+((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString()+"\n\n");
        if(!containsOutputFunctions)mainActivity.findViewById(R.id.btn_eq).performClick();
        if(inputShouldEqualtTerm) Assert.assertEquals(((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString(),term);
        return ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString();
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
        fun1 = new String[]{"Zn","Zb","NCR","NPR","MEAN","VAR","S","","","","",""};
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.statistic));}
        fun1 = new String[]{"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;idToViewMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.memory));}
        idToViewMap.put("L",mainActivity.findViewById(R.id.btn_LINKS));
        idToViewMap.put("R",mainActivity.findViewById(R.id.btn_RECHTS));


    }
}
