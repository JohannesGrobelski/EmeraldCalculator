package com.example.titancalculator;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.titancalculator.helper.Math_String.MathEvaluator;
import com.example.titancalculator.helper.Math_String.NumberString;
import com.example.titancalculator.helper.StringUtils;

import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.fakes.RoboMenuItem;

import java.text.DecimalFormat;

import static com.example.titancalculator.MainActivityTest.resembles;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MathUnitTest1 {
    static int iterationsSubtests = 100;


    @Test
    public void mathEv1() {
        assertEquals(MathEvaluator.evaluate("LOG(2,AriVAR(2,3))",10),"-2");
        assertEquals(MathEvaluator.evaluate("AriVAR(2,3)",10),"0.25");

        assertEquals(4, 2 + 2);
        assertTrue(testEquation(MathEvaluator.evaluate(NumberString.getCalcuableString("2√2√2√256"),10),"2"));
        //assertEquals(MathEvaluator.evaluate(NumberString.getCalcuableString("3√3√(3^3^3)"),10),"3");

        assertEquals(MathEvaluator.evaluate("78588558.2121",4,5),"7.85886E7");
        assertEquals(MathEvaluator.evaluate("LOG(LOG(2))",4,5),"-0.52139");

        assertEquals(MathEvaluator.evaluate("2.121^32",10),"2.81385E10");

        //assertEquals(toBase("1412432",16,20));
        //assertEquals(NumberString.findLongestParenthesisable("LOGLOG9"));


        assertEquals(MathEvaluator.evaluate("AriMit(18.920683057342217,236.94690914832142,172.22050990703872,-229.6619378948368,323.8524514829659,479.01654184960853,425.4397364292351)",4,12),String.valueOf(203.819270568525));

    }



    @Test
    public void mathEvTime(){
        //assertEquals(MathEvaluator.evaluate(NumberString.getCalcuableString("√4"),10),"2");
        long start,end; start = System.currentTimeMillis();
        MathEvaluator.evaluate(NumberString.getCalcuableString("√√√5"),10);
        System.out.println("time: "+String.valueOf(System.currentTimeMillis() - start));
    }

    @Test
    public void mathEv2() {
        //Testfälle absolut große/kleine zahlen, ganze/bruchzahlen, negative/positive zahlen
        System.out.println(MathEvaluator.evaluate("15rootToSqrt.25*22! + 7.90^sin(22) + 3.12^23",10));
        System.out.println(MathEvaluator.evaluate("69.2*37122.44",10));
        System.out.println(MathEvaluator.evaluate("89*9*62.2",10));
        System.out.println(MathEvaluator.evaluate("MAX(1,2,3)",10));
        System.out.println("ARI: "+MathEvaluator.evaluate("AriVar(1,2,3,4,5,6)",10));
        System.out.println(MathEvaluator.evaluate("AriMit(1,2,3,4,5,6)",10));


        System.out.println(MathEvaluator.evaluate("10000!",10));

        System.out.println(MathEvaluator.evaluate("-8^32",10));
        System.out.println(MathEvaluator.evaluate("8^32",10));

        System.out.println(MathEvaluator.evaluate("-8^32 + 0.123456789",10));
        System.out.println(MathEvaluator.evaluate("8^32 - 0.123456789",10));

        System.out.println(MathEvaluator.evaluate("-150",10));
        System.out.println(MathEvaluator.evaluate("150",10));

        System.out.println(MathEvaluator.evaluate("-150 + 0.123456789",10));
        System.out.println(MathEvaluator.evaluate("150 - 0.123456789",10));

        System.out.println(MathEvaluator.evaluate("SEC(ASEC(0.08635282754123619))",10));



    }

    @Test public void testBasicFunctions(){
        assertTrue(testEquation("π","3.141592653589793"));
        //TODO: assertTrue(testEquation("e","2.718281828459045"));
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
        for(int i=0; i<3; i++) {
            int inta = (int) ((Math.random() * 1000) - 500); //TODO: negative numbers
            int intb = (int) ((Math.random() * 1000) - 500); //TODO: negative numbers
            int intc = (int) ((Math.random() * 1000) - 500); //TODO: negative numbers
            int intm = (int) ((Math.random() * 1000));

            double op1 = (Math.random() * 10) - 0; //TODO: negative numbers
            double op2 = (Math.random() * 10) - 0; //TODO: bigger numbers
            double op3 = (Math.random() * 10) - 0; //TODO: bigger numbers
            double d = (Math.random() * 10) - 0;

            assertTrue(testEquation("GCD(" + inta + ",0)", String.valueOf(Math.abs(inta)))); //gcd(a, 0) = |a|, for a ≠ 0
            for(int div = 1; div<Math.min(Math.abs(inta),Math.abs(intb)); div++){ //Every common divisor of a and b is a divisor of gcd(a, b).
                if(Math.abs(inta) % div == 0 && Math.abs(intb) % div == 0){
                    assertTrue(Integer.valueOf(calcTerm("GCD("+inta+","+intb+")")) % div == 0);
                }
            }
            assertTrue(testEquation("GCD("+inta+","+intb+")","GCD("+intb+","+inta+")")); // gcd(a,b) = gcd(b, a).
            assertTrue(testEquation("LCM("+inta+","+intb+")","LCM("+intb+","+inta+")")); // lcm(a,b) = lcm(b, a).
            assertTrue(testEquation("GCD("+inta+",GCD("+intb+","+intc+")","GCD("+intc+",GCD("+inta+","+intb+")")); //gcd(a, gcd(b, c)) = gcd(gcd(a, b), c)
            assertTrue(testEquation("LCM("+inta+",LCM("+intb+","+intc+")","LCM("+intc+",LCM("+inta+","+intb+")")); //lcm(a, lcm(b, c)) = lcm(lcm(a, b), c)
            assertTrue(testEquation("LCM("+inta+",GCD("+inta+","+intb+"))",String.valueOf(Math.abs(inta))));//lcm(a,gcd(a,b)) = |a|
            assertTrue(testEquation("GCD("+inta+",LCM("+inta+","+intb+"))",String.valueOf(Math.abs(inta))));//gcd(a,lcm(a,b)) = |a|
            assertTrue(testEquation("GCD("+inta+","+inta+")",String.valueOf(Math.abs(inta)))); //gcd(a,a) = |a|
            assertTrue(testEquation("LCM("+inta+","+inta+")",String.valueOf(Math.abs(inta)))); //lcm(a,a) = |a|
            assertTrue(testEquation("GCD("+intm+"*"+inta+","+intm+"*"+intb+")","(GCD("+inta+","+intb+")*"+intm+")")); //gcd(m⋅a, m⋅b) = m⋅gcd(a, b).
            assertTrue(testEquation("LCM("+intm+"*"+inta+","+intm+"*"+intb+")","(LCM("+inta+","+intb+")*"+intm+")")); //lcm(m⋅a, m⋅b) = m⋅lcm(a, b).
            assertTrue(testEquation("GCD("+inta+"+("+intm+"*"+intb+"),"+intb+")","GCD("+inta+","+intb+")")); //gcd(a + m⋅b, b) = gcd(a, b).
            //TODO:assertTrue(testEquation("LCM("+inta+"+("+intm+"*"+intb+"),"+intb+")","LCM("+inta+","+intb+")")); //lcm(a + m⋅b, b) = lcm(a, b).
            //TODO: assertTrue(testEquation("GCD("+inta+"+"+intm+"*"+intb+","+intb,"(GCD("+inta+","+intb));
            assertTrue(testEquation(String.valueOf(Integer.valueOf(calcTerm("GCD("+inta+","+intb+")"))*Integer.valueOf(calcTerm("LCM("+inta+","+intb+")"))),String.valueOf(Math.abs(inta*intb))));
            //TODO: assertTrue(testEquation("(LCM("+inta+","+intb+")"+"*"+"(GCD("+inta+","+intb+")",String.valueOf(Math.abs(inta*intb))));
            assertTrue(testEquation("GCD("+inta+","+"LCM("+intb+","+intc+")","LCM("+"GCD("+inta+","+intb+","+"GCD("+inta+","+intc)); //gcd(a, lcm(b, c)) = lcm(gcd(a, b), gcd(a, c))
            assertTrue(testEquation("LCM("+inta+","+"GCD("+intb+","+intc+")","GCD("+"LCM("+inta+","+intb+","+"LCM("+inta+","+intc)); //lcm(a, gcd(b, c)) = gcd(lcm(a, b), lcm(a, c))

            assertTrue(testEquation("SUME("+"1"+","+intm+")","("+intm+"("+intm+"+1))/2")); //∑1..m = m(m+1) / 2

            //https://www.mathebibel.de/summenzeichen
            //https://www.mathebibel.de/produktzeichen

            assertTrue(testEquation("MAX"+op1+","+op2,"-MIN"+"-"+op1+",-"+op2)); //max(a,b) = -min(-a,-b)
            assertTrue(testEquation("MIN"+op1+","+op2,"-MAX"+"-"+op1+",-"+op2)); //min(a,b) = -max(-a,-b)
            assertTrue(testEquation("(MAX"+op1+","+op2+")+"+op3,"MAX"+op1+"+"+op3+","+op2+"+"+op3)); //max(a,b)+c = max(a+c,b+c)
            assertTrue(testEquation("(MIN"+op1+","+op2+")+"+op3,"MIN"+op1+"+"+op3+","+op2+"+"+op3)); //min(a,b)+c = min(a+c,b+c)
            assertTrue(testEquation("(MAX"+op1+","+op2+")*"+d,"MAX"+op1+"*"+d+","+op2+"*"+d)); //d*max(a,b) = max(d*a,d*b)
            assertTrue(testEquation("(MIN"+op1+","+op2+")*"+d,"MIN"+op1+"*"+d+","+op2+"*"+d)); //d*min(a,b) = min(d*a,d*b)
        }

    }

    @Test public void testTrigoAndHyperFunctions() {
        //random values
        assertTrue(resembles(calcTerm("COS(9)"),"0.9876883405951377261900402476934372607584068615898804349239048016"));
        assertTrue(resembles(calcTerm("SIN(7.2284977183385335)"),"0.12582667507334807"));


        /*
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

            //TODO: RAD
            //TODO: DEG

            //TODO: >toPolar
            //TODO: >toCart

            //TODO: >DEG
            //TODO: >RAD


        }




        assertTrue(resembles(calcTerm("π>DEG))"),"180",3)); //csc(acsc(x)) = x
        assertTrue(resembles(calcTerm("0>DEG))"),"0",3)); //csc(acsc(x)) = x
        assertTrue(resembles(calcTerm("2*π>DEG))"),"360",3)); //csc(acsc(x)) = x

        */
    }

    @Test public void testLogicFunctions() {
        for(int i=0; i<iterationsSubtests; i++) {
            int a = (int) ((Math.random() * 1000) - 500);
            int b = (int) ((Math.random() * 1000) - 500);
            int c = (int) ((Math.random() * 1000) - 500);

            //properties
            assertTrue(testEquation("AND("+a+","+b+")","AND("+b+","+a+")"));    //a and b = b and a
            assertTrue(testEquation("OR("+a+","+b+")","OR("+b+","+a+")"));    //a or b = b or a
            assertTrue(testEquation("AND"+a+",AND"+b+","+c,"AND"+c+",AND"+a+","+b)); //and(a, and(b, c)) = and(and(a, b), c)
            assertTrue(testEquation("OR"+a+",OR"+b+","+c,"OR"+c+",OR"+a+","+b)); //or(a, or(b, c)) = or(or(a, b), c)
            assertTrue(testEquation("AND("+a+",0)","0"));    //a and 0 = 0
            assertTrue(testEquation("OR("+a+",0)",String.valueOf(a)));    //a or 0 = a
            assertTrue(testEquation("AND("+a+",OR("+a+",1))",String.valueOf(a)));    //a and a or 1 = a
            assertTrue(testEquation("OR("+a+",OR("+a+",1))","OR("+a+",1)"));    //a and a or 1 = a or 1
            assertTrue(testEquation("NOT(NOT("+a+"))",String.valueOf(a)));    //not(not(a)) = a
            assertTrue(testEquation("AND("+a+","+"NOT("+a+"))","0"));    //a and not(a) = 0
            assertTrue(testEquation("NOT(1)","-2"));    //not(0) = 1
            assertTrue(testEquation("NOT(0)","-1"));    //not(1) = 0


            //assertTrue(testEquation("NOT(AND("+a+","+b+"))","OR("+"NOT("+a+")"+","+"NOT("+b+"))"));    //not(a and b) = not(a) or not(b)

            // assertTrue(testEquation("OR("+a+",1)",String.valueOf(a)));    //a or 1 = a

        }
    }

    @Test public void testStatisticFunctions() {
        for(int i=0; i<iterationsSubtests; i++) {
            int a = (int) ((Math.random() * 1000));
            int b = a+100;

            int n = (int) ((Math.random() * 1000));
            int r = (int) ((Math.random() * 1000));

            double x = ((Math.random() * 1000) - 500);

            int result = Integer.valueOf(calcTerm("ZN("+a+")"));
            assertTrue(result > 0); assertTrue(result <= a);  //0 < result < a

            result = Integer.valueOf(calcTerm("ZB("+a+","+b+")"));
            assertTrue(result > a); assertTrue(result <= b);  //0 < result < a

            assertTrue(testEquation("NPR("+n+","+r+")",n+"!"+"/("+n+"-"+r+")!")); //nPr(n,r)=n!/(n−r)!
            assertTrue(testEquation("NCR("+n+","+r+")","NPR("+n+","+r+")/"+r+"!")); //nCr(n,r)=nPr(n,r)/r!

        }

        //extreme values
        //mean
        assertTrue(resembles(calcTerm("MEAN()"),"Math Error"));
        assertTrue(resembles(calcTerm("MEAN(-1,0,1)"),"0"));
        assertTrue(resembles(calcTerm("MEAN(1,2,3)"),calcTerm("MEAN(2,1,3)")));
        assertTrue(resembles(calcTerm("MEAN(1,2)"),calcTerm("MEAN(2,1,0)")));
        assertTrue(resembles(calcTerm("MEAN(1,23,32523,12,124,154,-123,12.124)"),"4090.7655"));
        assertTrue(resembles(calcTerm("MEAN(1,23,0,-12,124,12,2.3125,13.154,-123,12.124)"),"5.25905"));

        //var
        assertTrue(resembles(calcTerm("VAR(1,2,3)"),calcTerm("VAR(2,1,3)")));

        //s
        assertTrue(resembles(calcTerm("ROOT(VAR(0))"),"0"));
        assertTrue(resembles(calcTerm("ROOT(VAR(1))"),"0"));
        assertTrue(resembles(calcTerm("ROOT(VAR(-1.214))"),"0"));

        assertTrue(resembles(calcTerm("ROOT(VAR(1,2,3))"),"0.81649658092772603273242802"));
        assertTrue(resembles(calcTerm("ROOT(VAR(1.12,124.12,12.241,-12.124124,40783420,-792.2))"),"15199132.952412"));

    }


    @Test public void testVoids(){
        for(int i=0; i<iterationsSubtests; i++) {
            int a = (int) ((Math.random() * 1000) - 500);
            double x = ((Math.random() * 1000) - 500);

            //TODO: >%
            //TODO: >+/-
            //TODO: >A/B
            //TODO: >PFZ
        }
    }

    private String calcTerm(String term){
        term = NumberString.getCalcuableString(term);
        return MathEvaluator.evaluate(term,100,1000);
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

        if(!expectedResult.equals("Math Error")){
            DecimalFormat df = new DecimalFormat("#");
            df.setMaximumFractionDigits(20);
            expectedResult = df.format(Double.valueOf(expectedResult));
        }
        int minlength = Math.min(Math.min(expectedResult.length(),output.length()),20);
        output = output.replace(",","."); expectedResult = expectedResult.replace(",",".");
        if(!output.equals(expectedResult)){
            return resembles(expectedResult,output);
        }
        return true;
    }

}