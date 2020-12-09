package com.example.titancalculator.unittests;

import com.example.titancalculator.helper.Math_String.MathEvaluator;
import com.example.titancalculator.helper.Math_String.StringUtils;

import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.titancalculator.helper.Math_String.MathEvaluator.evaluate;
import static com.example.titancalculator.helper.Math_String.MathEvaluator.resembles;
import static com.example.titancalculator.helper.Math_String.MathEvaluator.toDEG;
import static com.example.titancalculator.helper.Math_String.MathEvaluator.toFraction;
import static com.example.titancalculator.helper.Math_String.MathEvaluator.toInvert;
import static com.example.titancalculator.helper.Math_String.MathEvaluator.toPercent;
import static com.example.titancalculator.helper.Math_String.MathEvaluator.toRAD;
import static com.example.titancalculator.helper.Math_String.MathEvaluator.toReciproke;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * For all self-defined functions:
 *      - basic: "!"
 *      - basic2: "PFZ","GCD","LCM","∑","∏",">%",">A/B",">x\u207B\u00B9",">+/-","MIN","MAX",
 *      - statistic: "RAND","RANDB","NCR","NPR","MEAN","VAR","S",
 * - check definition and document it
 * - test math rules with random values
 */
public class MathUnitTest1 {
    static int iterationsSubtests = 100;

    @Test
    public void mathEvTime(){
        //assertEquals(MathEvaluator.evaluate(NumberString.getCalcuableString("√4"),10),"2");
        long start,end; start = System.currentTimeMillis(); int originalIterationsSubtests = iterationsSubtests; iterationsSubtests = 1;
        MathEvaluator.evaluate("ROOTROOTROOT5");
        testBasicFunctions();
        testBasic2Functions();
        testTrigoAndHyperFunctions();
        testLogicFunctions();
        testStatisticFunctions();
        testVoidFunctions();
        assertTrue(System.currentTimeMillis() - start < 3000);
        iterationsSubtests = originalIterationsSubtests;
    }

    @Test
    public void testNumberStringFunctions(){
        //getPercent(), toPercent(..), getInvert(), toInvert(..), getReciproke(), toReciproke(..), toFraction(), getRAD(), getDEG(), getPFZ()
        assertEquals("110", MathEvaluator.toPercent("1.1"));
        assertEquals("-25", MathEvaluator.toInvert("25"));
        assertEquals("0.2", MathEvaluator.toReciproke("5"));
        assertEquals("0.3490658503988659", MathEvaluator.toRAD("20"));
        assertEquals("1145.9155902616465", MathEvaluator.toDEG("20"));
        assertEquals("(2,2,2)", MathEvaluator.toPFZ("8"));
        assertEquals("7.2", MathEvaluator.toPFZ("7.2"));
        assertEquals("1.22", MathEvaluator.toPFZ("1.22"));

        //errors
        String[] inputs = {"%","a",""};
        for(int i=0;i<3;i++) {
            assertEquals("Math Error", MathEvaluator.toPercent(calcTerm(inputs[i])));
            assertEquals("Math Error", MathEvaluator.toInvert(calcTerm(inputs[i])));
            assertEquals("Math Error", MathEvaluator.toReciproke(calcTerm(inputs[i])));
            assertEquals("Math Error", MathEvaluator.toRAD(calcTerm(inputs[i])));
            assertEquals("Math Error", MathEvaluator.toDEG(calcTerm(inputs[i])));
            assertEquals("Math Error", MathEvaluator.toPFZ(calcTerm(inputs[i])));
        }

    }


    @Test public void testBasicFunctions(){
        assertTrue(testEquation("-0.086402098906667985852332933888574482159078964774695348",calcTerm("LOG(9,LOG(7,5))")));

        assertTrue(testEquation("π","3.141592653589793"));
        assertTrue(testEquation("e","2.718281828459045"));
        assertTrue(testEquation("e^0","1"));
        assertTrue(testEquation("LN(1)","0"));
        assertTrue(testEquation("LN(e)","1"));

        NumberFormat formatter = new DecimalFormat("###.#####################");
        for(int i=0; i<iterationsSubtests; i++) {
            double OP1 = Double.parseDouble(String.valueOf((Math.random() * 10) - 0)); 
            double OP2 = Double.parseDouble(String.valueOf((Math.random() * 10) - 0)); 
            String op1 = formatter.format(OP1).replace(",",".");
            String op2 = formatter.format(OP1).replace(",",".");
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
            //assertFalse(testEquation(op1+"√"+op2,op2+"^1/"+op1)); //a√b != b^1/a
            assertTrue(testEquation(op3+"^("+op1+"+"+op2+")","("+op3+"^"+op2+")*("+op3+"^"+op1+")"));  //a^(b+c) = a^c + a^c
            //assertFalse(testEquation(op3+"^"+op1+"+"+op2,+op3+"^"+op2+"*"+op3+"^"+op1)); //a^b+c != a^c + a^c
            assertTrue(testEquation(op3+"^("+op1+"-"+op2+")","("+op3+"^"+op1+")/("+op3+"^"+op2+")")); //a^(b-c) = a^b / a^c
            //assertFalse(testEquation(op3+"^"+op1+"-"+op2,+op3+"^"+op1+"/"+op3+"^"+op2)); //a^b-c != a^c / a^c
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
        //test random values
        assertTrue(testEquation("14!","87178291200"));
        assertTrue(testEquation("27!","10888869450418352160768000000"));
        assertTrue(testEquation("3!","6"));
        assertTrue(testEquation("5!","120"));
        assertTrue(testEquation("-12!","Math Error"));

        assertTrue(testEquation("0!","1"));
        assertTrue(testEquation("0.5!","Math Error"));
        assertTrue(testEquation("1!","1"));
    }

    @Test public void testBasic2Functions() {
        assertTrue(testEquation(String.valueOf(0),"SUME("+0+","+-161+")")); //sum(b,a) with b>a = 0


        for(int i=0; i<iterationsSubtests; i++) {
            int inta = (int) ((Math.random() * 1000) - 500); 
            int intb = (int) ((Math.random() * 1000) - 500); 
            int intc = (int) ((Math.random() * 1000) - 500); 
            int intm = (int) ((Math.random() * 1000));

            double op1 = (Math.random() * 10) - 0; 
            double op2 = (Math.random() * 10) - 0; 
            double op3 = (Math.random() * 10) - 0; 
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
            //assertTrue(testEquation("LCM("+inta+"+("+intm+"*"+intb+"),"+intb+")","LCM("+inta+","+intb+")")); //lcm(a + m⋅b, b) = lcm(a, b).
            //assertTrue(testEquation("GCD("+inta+"+"+intm+"*"+intb+","+intb,"(GCD("+inta+","+intb));
            assertTrue(testEquation(String.valueOf(Integer.valueOf(calcTerm("GCD("+inta+","+intb+")"))*Integer.valueOf(calcTerm("LCM("+inta+","+intb+")"))),String.valueOf(Math.abs(inta*intb))));
            //assertTrue(testEquation("(LCM("+inta+","+intb+")"+"*"+"(GCD("+inta+","+intb+")",String.valueOf(Math.abs(inta*intb))));
            assertTrue(testEquation("GCD("+inta+","+"LCM("+intb+","+intc+")","LCM("+"GCD("+inta+","+intb+","+"GCD("+inta+","+intc)); //gcd(a, lcm(b, c)) = lcm(gcd(a, b), gcd(a, c))
            assertTrue(testEquation("LCM("+inta+","+"GCD("+intb+","+intc+")","GCD("+"LCM("+inta+","+intb+","+"LCM("+inta+","+intc)); //lcm(a, gcd(b, c)) = gcd(lcm(a, b), lcm(a, c))

            assertTrue(testEquation("SUME("+"1"+","+intm+")","("+intm+"("+intm+"+1))/2")); //∑1..m = m(m+1) / 2
            int min = Math.min((inta),(intb)); int max = Math.max((inta),(intb)); int q = min + (int) (Math.random()*(Math.abs(min-max) / 2));
            assertTrue(min<=max);

            assertTrue(testEquation("SUME("+min+","+max+")","SUME("+min+","+q+")+SUME("+(q+1)+","+max+")")); //sum(i=1...n)ak = sum(i=1...m)ak+sum(i=m+1...n)
            if(max!=min){
                assertTrue(min!=max);
                assertTrue(testEquation(String.valueOf(0),"SUME("+max+","+min+")")); //sum(b,a) with b>a = 0
            }
            assertTrue(testEquation(String.valueOf(max),"SUME("+max+","+max+")")); //sum(a,a) = a

            max = min+((int) (Math.random()*15) + 1); q = min + (int) (Math.random()*(Math.abs(min-max) / 2));
            assertTrue(testEquation("MULP("+min+","+max+")","MULP("+min+","+q+")*MULP("+(q+1)+","+max+")")); //mul(i=1...n)ak = mul(i=1...m)ak*mul(i=m+1...n)
            assertTrue(testEquation(String.valueOf(1),"MULP("+max+","+(max-1)+")")); //mul(b,a) with b>a = 1
            assertTrue(testEquation(String.valueOf(max),"MULP("+max+","+max+")")); //mul(a,a) = a

            assertTrue(testEquation("MAX"+op1+","+op2,"-MIN"+"-"+op1+",-"+op2)); //max(a,b) = -min(-a,-b)
            assertTrue(testEquation("MIN"+op1+","+op2,"-MAX"+"-"+op1+",-"+op2)); //min(a,b) = -max(-a,-b)
            assertTrue(testEquation("(MAX"+op1+","+op2+")+"+op3,"MAX"+op1+"+"+op3+","+op2+"+"+op3)); //max(a,b)+c = max(a+c,b+c)
            assertTrue(testEquation("(MIN"+op1+","+op2+")+"+op3,"MIN"+op1+"+"+op3+","+op2+"+"+op3)); //min(a,b)+c = min(a+c,b+c)
            assertTrue(testEquation("(MAX"+op1+","+op2+")*"+d,"MAX"+op1+"*"+d+","+op2+"*"+d)); //d*max(a,b) = max(d*a,d*b)
            assertTrue(testEquation("(MIN"+op1+","+op2+")*"+d,"MIN"+op1+"*"+d+","+op2+"*"+d)); //d*min(a,b) = min(d*a,d*b)
        }
        assertTrue(testEquation("SUME(-143,-53)","SUME(-143,-143)+SUME(-142,-53)")); //sum(i=1...n)ak = sum(i=1...m)ak+sum(i=m+1...n)

    }

    @Test public void testTrigoAndHyperFunctions() {
        //random values
        //assertTrue(resembles(calcTerm("COS(9)"),"0.9876883405951377261900402476934372607584068615898804349239048016"));
        //assertTrue(resembles(calcTerm("SIN(7.2284977183385335)"),"0.12582667507334807"));
        assertTrue(resembles(MathEvaluator.toDEG(calcTerm("π")),"180")); //DEG(π) = 180

        for(int i=0; i<iterationsSubtests; i++) {
            double x = (Math.random() * 10) + 1;
            double y = (Math.random() * 10) + 1;
            int a = (int) ((Math.random() * 50) - 25);
            int b = (int) ((Math.random() * 50) - 25);

            assertTrue(resembles(calcTerm("SIN("+x*Math.toRadians(Math.PI)+")"),"0",2));
            if(x == ((double) ((int) x)))x += .1;
            double complex = (a*Math.toRadians(Math.PI))+(0.5*Math.toRadians(Math.PI));
            assertTrue(testEquation("COS("+complex+")","0"));
            assertTrue(testEquation("TAN("+x*Math.toRadians(Math.PI)+")","0"));

            assertTrue(testEquation("SIN("+x+")/COS("+x+")","TAN("+x+")")); //tan(x) = sin(x) / cot(x)
            assertTrue(testEquation("COT("+(Math.toDegrees(1.5*Math.PI))+")","0"));
            assertTrue(testEquation("1/TAN("+x+")","COT("+x+")")); //cot(x) = 1 / tan(x)

            assertTrue(testEquation("SIN("+Math.toDegrees(Math.PI/2)+"-"+x+")","COS("+x+")")); //cos(x) = sin(π/2 - x)
            assertTrue(testEquation("COS("+Math.toDegrees(Math.PI/2)+"-"+x+")","SIN("+x+")")); //sin(x) = cos(π/2 - x)
            assertTrue(testEquation("TAN("+Math.toDegrees(Math.PI/2)+"-"+x+")","COT("+x+")")); //cot(x) = tan(π/2 - x)
            assertTrue(testEquation("COT("+Math.toDegrees(Math.PI/2)+"-"+x+")","TAN("+x+")")); //tan(x) = cot(π/2 - x)
            assertTrue(testEquation("SEC("+Math.toDegrees(Math.PI/2)+"-"+x+")","CSC("+x+")")); //sec(x) = csc(π/2 - x)
            assertTrue(testEquation("SEC("+Math.toDegrees(Math.PI/2)+"-"+x+")","CSC("+x+")")); //sec(x) = csc(π/2 - x)
            assertTrue(testEquation("CSC("+Math.toDegrees(Math.PI/2)+"-"+x+")","SEC("+x+")")); //csc(x) = sec(π/2 - x)

            assertTrue(testEquation("SIN(-"+x+")","-SIN("+x+")")); //sin(-x) = -sin(x)
            assertTrue(testEquation("COS(-"+x+")","COS("+x+")")); //cos(-x) = cos(x)
            assertTrue(testEquation("TAN(-"+x+")","-TAN("+x+")")); //tan(-x) = -tan(x)

            assertTrue(testEquation("SIN("+x+"+"+y+")","SIN("+x+")*COS("+y+")+COS("+x+")*SIN("+y+")")); //sin(x+y) = sin(x)*cos(y)+cos(x)*sin(y)
            assertTrue(testEquation("COS("+x+"+"+y+")","COS("+x+")*COS("+y+")-SIN("+x+")*SIN("+y+")")); //cos(x+y) = cos(x)*cos(y)-sin(x)*sin(y)
            assertTrue(testEquation("SIN("+x+"-"+y+")","SIN("+x+")*COS("+y+")-COS("+x+")*SIN("+y+")")); //sin(x-y) = cos(x)*cos(y)-sin(x)*sin(y)
            assertTrue(testEquation("COS("+x+"-"+y+")","COS("+x+")*COS("+y+")+SIN("+x+")*SIN("+y+")")); //cos(x-y) = cos(x)*cos(y)+sin(x)*sin(y)

            //more identities: http://www2.clarku.edu/faculty/djoyce/trig/identities.html

            double x1 = Math.random() - 0.5;
            double x2 = Math.abs(Math.random());

            assertTrue(testEquation("SIN(ASIN("+x1+"))",String.valueOf(x1))); //sin(asin(x)) = x
            assertTrue(testEquation("COS(ACOS("+x1+"))",String.valueOf(x1))); //cos(acos(x)) = x
            assertTrue(testEquation("TAN(ATAN("+x1+"))",String.valueOf(x1))); //tan(atan(x)) = x
            assertTrue(testEquation("COT(ACOT("+x1+"))",String.valueOf(x1))); //cot(acot(x)) = x
            assertTrue(testEquation("SEC(ASEC("+x+"))",String.valueOf(x))); //sec(asec(x)) = x
            assertTrue(testEquation("CSC(ACSC("+x+"))",String.valueOf(x))); //csc(acsc(x)) = x

            assertTrue(testEquation("ASINH(SINH("+x1+"))",String.valueOf(x1))); //sin(asin(x)) = x
            assertTrue(testEquation("ACOSH(COSH("+(x2)+"))",String.valueOf(x2))); //cos(acos(x)) = x
            assertTrue(testEquation("ATANH(TANH("+x1+"))",String.valueOf(x1))); //tan(atan(x)) = x
            assertTrue(testEquation("ACOTH(COTH("+x1+"))",String.valueOf(x1))); //cot(acot(x)) = x
            assertTrue(testEquation("ASECH(SECH("+x+"))",String.valueOf(x))); //sec(asec(x)) = x
            assertTrue(testEquation("ACSCH(CSCH("+x+"))",String.valueOf(x))); //csc(acsc(x)) = x

            assertTrue(resembles(MathEvaluator.toDEG(MathEvaluator.toRAD("0")),"0")); //DEG(RAD(0)) = 0
            assertTrue(resembles(MathEvaluator.toDEG(MathEvaluator.toRAD(String.valueOf(a))),String.valueOf(a))); //DEG(RAD(0)) = 0

        }

        assertTrue(resembles(MathEvaluator.toDEG("0"),"0")); //DEG(0) = 0
        assertTrue(resembles(MathEvaluator.toDEG(calcTerm("π")),"180")); //DEG(π) = 180
        assertTrue(resembles(MathEvaluator.toDEG(calcTerm("2*π")),"360")); //DEG(2π) = 360
        assertTrue(resembles(MathEvaluator.toRAD("0"),"0")); //RAD(0) = 0
        assertTrue(resembles(MathEvaluator.toRAD(("180")),calcTerm("π"))); //RAD(180) = π
        assertTrue(resembles(MathEvaluator.toRAD(("360")),calcTerm("2*π"))); //RAD(360) = 2π
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
            assertTrue(b+" not higher than "+a,b>a);  //0 < result < a

            int n = (int) ((Math.random() * 1000));
            int r = (int) ((Math.random() * 1000));

            double x = ((Math.random() * 1000) - 500);

            int result = Integer.valueOf(calcTerm("RAND("+a+")"));

            assertTrue("result is negative"+result,result >= 0);
            assertTrue(result+" higher than "+a+1,result <= a);  //0 < result < a

            result = Integer.valueOf(calcTerm("RANDB("+a+","+b+")"));
            assertTrue(result >= a);
            assertTrue(result <= b);  //0 < result < a

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
        assertTrue(resembles(calcTerm("√(VAR(0))"),"0"));
        assertTrue(resembles(calcTerm("√(VAR(1))"),"0"));
        assertTrue(resembles(calcTerm("√(VAR(-1.214))"),"0"));

        assertTrue(resembles(calcTerm("√(VAR(1,2,3))"),"0.81649658092772603273242802"));
        assertTrue(resembles(calcTerm("√(VAR(1.12,124.12,12.241,-12.124124,40783420,-792.2))"),"15199132.952412"));

        int min = Integer.MAX_VALUE; int max = Integer.MIN_VALUE; int a = -24; int b = -12;
        for(int j=0;j<500; j++){
            int result = Integer.parseInt(MathEvaluator.evaluate("RANDB("+a+","+b+")"));
            if(result < min)min = result;
            if(result > max)max = result;
        }
        assertEquals(a,min);
        assertEquals(b,max);
    }


    @Test public void testVoidFunctions(){
        for(int i=0; i<iterationsSubtests; i++) {
            int a = (int) ((Math.random() * 1000) - 500);
            double x = ((Math.random() * 1000) - 500);

            assertTrue(resembles(MathEvaluator.toPercent(String.valueOf(x)),String.valueOf(x*100))); //toPercent(x) = x*100
            assertTrue(resembles(MathEvaluator.toInvert(String.valueOf(x)),String.valueOf(-x))); //inverse(x) = -x
            assertTrue(resembles(MathEvaluator.toReciproke(String.valueOf(x)),String.valueOf(1/x))); //inverse(x) = 1/x

            final int  p = (int) ((Math.random() * 1000) - 500);
            String PFZ = MathEvaluator.toPFZ(p);  PFZ = PFZ.replace(",","*").replace("(","").replace(")","");
            if(!resembles(calcTerm(PFZ),String.valueOf(p)))System.out.println(PFZ+" "+calcTerm(PFZ)+" "+p);

            assertTrue(resembles(calcTerm(PFZ),String.valueOf(p))); //calc(transformation(PFZ(x))) = x

            String fraction = MathEvaluator.toFraction(String.valueOf(x));
            BigDecimal counter = new BigDecimal(fraction.substring(0,fraction.indexOf('/'))); BigDecimal denominator = new BigDecimal(fraction.substring(fraction.indexOf('/')+1));
            String FRACTION = counter.divide(denominator).toString();
            String input = String.valueOf(x);
            assertTrue(FRACTION.equals(input));
        }
    }

    @Test public void testSpecialCases(){
        assertEquals("Math Error",calcTerm("PI8"));
        assertEquals("Math Error",calcTerm("e8"));
        assertEquals("Math Error",MathEvaluator.toFraction(MathEvaluator.evaluate("LOGLOG")));

        //realy big numbers:
        String bigNumber = "10^10000000";
        assertEquals("1E10000002",toPercent(bigNumber));
        assertEquals("Math Error",toDEG(bigNumber));
        assertEquals("Math Error",toRAD(bigNumber));
        assertEquals("0",toReciproke(bigNumber));
        assertEquals("-1E10000000",toInvert(bigNumber));
        assertEquals("1E10000000",toFraction(bigNumber));


    }


    private String calcTerm(String term){
        String input = getCalcuableString(term);
        return MathEvaluator.evaluate(input,100,1000);
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

    public String getCalcuableString(String a){
        //language settings
        a = a.replaceAll("KGV","LCM");
        a = a.replaceAll("GGT","GCD");

        a = a.replace("³√","3ROOT");
        for(String r: StringUtils.replacements.keySet()){
            a = a.replace(r, StringUtils.replacements.get(r));
        }

        //I: fix; sonst: PI -> P(I)
        a = StringUtils.paraInComplex(a);

        for(String f: StringUtils.functions_paraIn){
            a = a.replace(f.toLowerCase(),f);
        }

        a = StringUtils.parenthesise(a);

        //after paraIn (because of AT(ANS)INH)
        Matcher matcherANS = Pattern.compile("ANS").matcher(a);
        while(matcherANS.find()){
            if(matcherANS.group().matches("[^A-Z]*ANS[^A-Z]*")){ //excludes inputs like "ATAN(ASINH(57.860802) = atANSinh57.860802"
                String last_answer = "";
                a = a.replace("ANS", last_answer);
            }
            else {
                //System.out.println(a);
            }
        }


        //settings
        String mean_mode = "AriMit";
        a = a.replaceAll("MEAN", mean_mode);
        String var_mode = "AriVar";
        a = a.replaceAll("VAR", var_mode);


        return a;
    }
}