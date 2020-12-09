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
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@Config(sdk = 28)
@RunWith(RobolectricTestRunner.class)
public class ComplexMathIntegrationTest {
    private static boolean debug = true;
    private String[][] btn = new String[2][6];

    private static int iterationsSubtests = 10;
    private Map<String, View> idToViewClickMap = new HashMap<>();
    private Map<String, View> idToViewLongClickMap = new HashMap<>();
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


    @Before
    public void Before(){
        mainActivity = Robolectric.setupActivity(MainActivity.class);
        currentMode = new RoboMenuItem(R.id.basic);

        Button[] B = new Button[]{mainActivity.findViewById(R.id.btn_11),mainActivity.findViewById(R.id.btn_12),mainActivity.findViewById(R.id.btn_13),
                mainActivity.findViewById(R.id.btn_14),mainActivity.findViewById(R.id.btn_15),mainActivity.findViewById(R.id.btn_16),
                mainActivity.findViewById(R.id.btn_21),mainActivity.findViewById(R.id.btn_22),mainActivity.findViewById(R.id.btn_23),
                mainActivity.findViewById(R.id.btn_24),mainActivity.findViewById(R.id.btn_25),mainActivity.findViewById(R.id.btn_26)};

        idToViewClickMap.put("1",mainActivity.findViewById(R.id.btn_1));
        idToViewClickMap.put("2",mainActivity.findViewById(R.id.btn_2));
        idToViewClickMap.put("3",mainActivity.findViewById(R.id.btn_3));
        idToViewClickMap.put("4",mainActivity.findViewById(R.id.btn_4));
        idToViewClickMap.put("5",mainActivity.findViewById(R.id.btn_5));
        idToViewClickMap.put("6",mainActivity.findViewById(R.id.btn_6));
        idToViewClickMap.put("7",mainActivity.findViewById(R.id.btn_7));
        idToViewClickMap.put("8",mainActivity.findViewById(R.id.btn_8));
        idToViewClickMap.put("9",mainActivity.findViewById(R.id.btn_9));
        idToViewClickMap.put("0",mainActivity.findViewById(R.id.btn_0));
        idToViewClickMap.put(",",mainActivity.findViewById(R.id.btn_sep));
        idToViewClickMap.put(".",mainActivity.findViewById(R.id.btn_com));

        idToViewClickMap.put("+",mainActivity.findViewById(R.id.btn_add));
        idToViewClickMap.put("-",mainActivity.findViewById(R.id.btn_sub));
        idToViewClickMap.put("*",mainActivity.findViewById(R.id.btn_mul));
        idToViewClickMap.put("/",mainActivity.findViewById(R.id.btn_div));
        idToViewClickMap.put("(",mainActivity.findViewById(R.id.btn_open_bracket));
        idToViewClickMap.put(")",mainActivity.findViewById(R.id.btn_close_bracket));

        String[] fun1 = CalcModel.modeBasicText;
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;
            idToViewClickMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i], new RoboMenuItem(R.id.basic));}
        fun1 = CalcModel.modeAdvancedText;
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;
            idToViewClickMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.advanced));}
        fun1 = CalcModel.modeTrigoText;
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;
            idToViewClickMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.trigo));}
        fun1 = CalcModel.modeTrigoFunctionalityInverse;
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;
            idToViewLongClickMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.trigo));}
        fun1 = CalcModel.modeStatisticText;
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;
            idToViewClickMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.statistic));}
        fun1 = CalcModel.modeLogicText;
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;
            idToViewClickMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.logic));}
        fun1 = CalcModel.modeMemoryText;
        for(int i=0; i<12; i++) {if(fun1[i].equals(""))continue;
            idToViewClickMap.put(fun1[i], B[i]);idToModeMap.put(fun1[i],  new RoboMenuItem(R.id.memory));}
        idToViewClickMap.put("L",mainActivity.btn_left);
        idToViewClickMap.put("R",mainActivity.btn_right);
    }


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
            double op1 = (Math.random() * 10) - 0;
            double op2 = (Math.random() * 10) - 0;
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
            assertTrue(testEquation(op3+"^("+op1+"+"+op2+")","("+op3+"^"+op2+")*("+op3+"^"+op1+")"));  //a^(b+c) = a^c + a^c
            assertTrue(testEquation(op3+"^("+op1+"-"+op2+")","("+op3+"^"+op1+")/("+op3+"^"+op2+")")); //a^(b-c) = a^b / a^c
            assertTrue(testEquation("("+op1+"^"+op2+")^"+op3,op1+"^("+op2+"*"+op3+")")); //(a^b)^c = a^(b^c)
            assertTrue(testEquation("("+op1+"*"+op2+")^"+op3,op1+"^"+op3+"*"+op2+"^"+op3)); //(a*b)^c = a^c * b^c
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
        currentMode = new RoboMenuItem(R.id.advanced);

        for(int i=0; i<3; i++) {
            int inta = (int) ((Math.random() * 1000) - 500);
            int intb = (int) ((Math.random() * 1000) - 500);
            int intc = (int) ((Math.random() * 1000) - 500);
            int intm = (int) ((Math.random() * 1000));

            double op1 = (Math.random() * 10) - 0;
            double op2 = (Math.random() * 10) - 0;
            double op3 = (Math.random() * 10) - 0;
            double d = (Math.random() * 10) - 0;

            assertTrue(testEquation("GCDLL" + inta + "R0", String.valueOf(Math.abs(inta)))); //gcd(a, 0) = |a|, for a ≠ 0
            for(int div = 1; div<Math.min(Math.abs(inta),Math.abs(intb)); div++){ //Every common divisor of a and b is a divisor of gcd(a, b).
                if(Math.abs(inta) % div == 0 && Math.abs(intb) % div == 0){
                    assertTrue(Integer.valueOf(calcTerm("GCDLL"+inta+"R"+intb)) % div == 0);
                }
            }
            assertTrue(testEquation("GCDLL"+inta+"R"+intb,"GCDLL"+intb+"R"+inta)); // gcd(a,b) = gcd(b, a).
            assertTrue(testEquation("LCMLL"+inta+"R"+intb,"LCMLL"+intb+"R"+inta)); // lcm(a,b) = lcm(b, a).
            assertTrue(testEquation("GCDLL"+inta+"RGCDLLL"+intb+"R"+intc,"GCDLL"+intc+"RGCDLLL"+inta+"R"+intb)); //gcd(a, gcd(b, c)) = gcd(gcd(a, b), c)
            assertTrue(testEquation("LCMLL"+inta+"RLCMLLL"+intb+"R"+intc,"LCMLL"+intc+"RLCMLLL"+inta+"R"+intb)); //lcm(a, lcm(b, c)) = lcm(lcm(a, b), c)
            assertTrue(testEquation("LCMLL"+inta+"RGCDLLL"+inta+"R"+intb,String.valueOf(Math.abs(inta))));//lcm(a,gcd(a,b)) = |a|
            assertTrue(testEquation("GCDLL"+inta+"RLCMLLL"+inta+"R"+intb,String.valueOf(Math.abs(inta))));//gcd(a,lcm(a,b)) = |a|
            assertTrue(testEquation("GCDLL"+inta+"R"+inta,String.valueOf(Math.abs(inta)))); //gcd(a,a) = |a|
            assertTrue(testEquation("LCMLL"+inta+"R"+inta,String.valueOf(Math.abs(inta)))); //lcm(a,a) = |a|
            assertTrue(testEquation("GCDLL"+intm+"*"+inta+"R"+intm+"*"+intb,"(GCDLL"+inta+"R"+intb+")*"+intm)); //gcd(m⋅a, m⋅b) = m⋅gcd(a, b).
            assertTrue(testEquation("LCMLL"+intm+"*"+inta+"R"+intm+"*"+intb,"(LCMLL"+inta+"R"+intb+")*"+intm)); //lcm(m⋅a, m⋅b) = m⋅lcm(a, b).
            //Assert.assertTrue(testEquation("GCDLL"+inta+"+("+intm+"*"+intb+")R"+intb,"GCDLL"+inta+"R"+intb)); //gcd(a + m⋅b, b) = gcd(a, b).
            //Assert.assertTrue(testEquation("LCMLL"+inta+"+("+intm+"*"+intb+")R"+intb,"LCMLL"+inta+"R"+intb)); //lcm(a + m⋅b, b) = lcm(a, b).
            //Assert.assertTrue(testEquation(String.valueOf(Integer.valueOf(calcTerm("(GCDLL"+inta+"R"+intb+")"))*Integer.valueOf(calcTerm("(LCMLL"+inta+"R"+intb+")"))),String.valueOf(Math.abs(inta*intb))));
            assertTrue(testEquation("GCDLL"+inta+"R"+"LCMLL"+intb+"R"+intc,"LCMLL"+"GCDLL"+inta+"R"+intb+"R"+"GCDLL"+inta+"R"+intc)); //gcd(a, lcm(b, c)) = lcm(gcd(a, b), gcd(a, c))
            assertTrue(testEquation("LCMLL"+inta+"R"+"GCDLL"+intb+"R"+intc,"GCDLL"+"LCMLL"+inta+"R"+intb+"R"+"LCMLL"+inta+"R"+intc)); //lcm(a, gcd(b, c)) = gcd(lcm(a, b), lcm(a, c))
            assertTrue(testEquation("∑LL"+"1"+"R"+intm,"("+intm+"("+intm+"+1))/2")); //∑1..m = m(m+1) / 2
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

        for(int i=0; i<iterationsSubtests; i++) {
            double x = (Math.random() * 10) + 1;
            double y = (Math.random() * 10) + 1;
            int a = (int) ((Math.random() * 50) - 25);
            int b = (int) ((Math.random() * 50) - 25);

            assertTrue(MathEvaluator.resembles(calcTerm("SIN("+x*Math.toRadians(Math.PI)+")"),"0",2));
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
            double x1 = 0.25;
            assertTrue(testEquation("SIN(ASIN("+x1+"))",String.valueOf(x1))); //sin(asin(x)) = x
            assertTrue(testEquation("COS(ACOS("+x1+"))",String.valueOf(x1))); //cos(acos(x)) = x
            assertTrue(testEquation("TAN(ATAN("+x1+"))",String.valueOf(x1))); //tan(atan(x)) = x
            assertTrue(testEquation("COT(ACOT("+x1+"))",String.valueOf(x1))); //cot(acot(x)) = x
            assertTrue(testEquation("SEC(ASEC("+x+"))",String.valueOf(x))); //sec(asec(x)) = x
            assertTrue(testEquation("CSC(ACSC("+x+"))",String.valueOf(x))); //csc(acsc(x)) = x
            assertTrue(MathEvaluator.resembles(MathEvaluator.toDEG(MathEvaluator.toRAD("0")),"0")); //DEG(RAD(0)) = 0
            assertTrue(MathEvaluator.resembles(MathEvaluator.toDEG(MathEvaluator.toRAD(String.valueOf(a))),String.valueOf(a))); //DEG(RAD(0)) = 0
        }
        assertTrue(MathEvaluator.resembles(MathEvaluator.toDEG("0"),"0")); //DEG(0) = 0
        assertTrue(MathEvaluator.resembles(MathEvaluator.toDEG(calcTerm("π")),"180")); //DEG(π) = 180
        assertTrue(MathEvaluator.resembles(MathEvaluator.toDEG(calcTerm("2*π")),"360")); //DEG(2π) = 360
        assertTrue(MathEvaluator.resembles(MathEvaluator.toRAD("0"),"0")); //RAD(0) = 0
        assertTrue(MathEvaluator.resembles(MathEvaluator.toRAD(("180")),calcTerm("π"))); //RAD(180) = π
        assertTrue(MathEvaluator.resembles(MathEvaluator.toRAD(("360")),calcTerm("2*π"))); //RAD(360) = 2π
    }



    @Test public void testLogicFunctions() {
        currentMode = new RoboMenuItem(R.id.trigo);

        for(int i=0; i<iterationsSubtests; i++) {
            int a = (int) ((Math.random() * 1000) - 500);
            int b = (int) ((Math.random() * 1000) - 500);
            int c = (int) ((Math.random() * 1000) - 500);

            assertTrue(testEquation("ANDLL"+a+"R"+b,"ANDLL"+b+"R"+a));    //a and b = b and a
            assertTrue(testEquation("ANDLL"+a+"R"+b,"ANDLL"+b+"R"+a));    //a or b = b or a
            assertTrue(testEquation("AND"+a+"RAND"+b+"R"+c,"AND"+c+"RAND"+a+"R"+b)); //and(a, and(b, c)) = and(and(a, b), c)
            assertTrue(testEquation("OR"+a+"ROR"+b+"R"+c,"OR"+c+"ROR"+a+"R"+b)); //or(a, or(b, c)) = or(or(a, b), c)
            assertTrue(testEquation("ANDLL"+a+"R0","0"));    //a and 0 = 0

            assertTrue(testEquation("ANDLL"+a+"RORLLL"+a+"R1",String.valueOf(a)));    //a and a or 1 = a
            assertTrue(testEquation("NOTLLRNOTLL"+a,String.valueOf(a)));    //not(not(a)) = a

            assertTrue(testEquation("ANDLL"+a+"R"+"NOTLL"+a,"0"));    //a and not(a) = 0
            assertTrue(testEquation("NOTL1","-2"));    //not(0) = 1
            assertTrue(testEquation("NOTL0","-1"));    //not(1) = 0
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

            calcTerm("RANDL"+a);
            String output = ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString();
            assertTrue("output: "+output+", "+a,output.matches("[0-9]+"));
            Integer result = Integer.valueOf(output);
            assertTrue(result > 0); assertTrue(result <= a);  //0 < result < a
            calcTerm("RANDBLL"+a+"R"+b);  result = Integer.valueOf(((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString());
            assertTrue(result > a); assertTrue(result <= b);  //0 < result < a
            assertTrue(MathEvaluator.resembles(calcTerm("nPr("+n+"R"+r+")"),calcTerm(n+"!"+"/("+n+"-"+r+")!"))); //nPr(n,r)=n!/(n−r)!
            assertTrue(MathEvaluator.resembles(calcTerm("nCr("+n+"R"+r+")"),calcTerm("nPr("+n+"R"+r+")/"+r+"!"))); //nCr(n,r)=nPr(n,r)/r!

        }

        //mean
        assertTrue(MathEvaluator.resembles(calcTerm("MEAN("),"Math Error"));
        assertTrue(MathEvaluator.resembles(calcTerm("MEANL-1,0,1"),calcTerm("0")));
        assertTrue(MathEvaluator.resembles(calcTerm("MEANL1,2,3"),calcTerm("MEANL2,1,3")));
        assertTrue(MathEvaluator.resembles(calcTerm("MEANL1,2"),calcTerm("MEANL2,1,0")));
        assertTrue(MathEvaluator.resembles(calcTerm("MEANL1,23,32523,12,124,154,-123,12.124"),"4090.7655"));
        assertTrue(MathEvaluator.resembles(calcTerm("MEANL1,23,0,-12,124,12,2.3125,13.154,-123,12.124"),"5.25905"));

        //var
        assertTrue(MathEvaluator.resembles(calcTerm("VARL1,2,3"),calcTerm("VARL2,1,3")));

        //s
        assertTrue(MathEvaluator.resembles(calcTerm("SLL0"),"0"));
        assertTrue(MathEvaluator.resembles(calcTerm("SLL5"),"0"));
        assertTrue(MathEvaluator.resembles(calcTerm("SLL-1.214"),"0"));

        assertTrue(MathEvaluator.resembles(calcTerm("SLL1,2,3"),"0.81649658092772603273242802"));
        assertTrue(MathEvaluator.resembles(calcTerm("SLL1.12,124.12,12.241,-12.124124,40783420,-792.2"),"15199132.952412"));
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

            calcTerm(a+"PF"); output = ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString(); result = calcTerm(output.replace("(","").replace(")","").replace(",","*"));
            Assert.assertTrue(MathEvaluator.resembles(String.valueOf(a),result)); mainActivity.findViewById(R.id.btn_clear_all).performClick();
        }
    }


    private boolean testEquation(String term, String result) {
        return testEquation(term+"="+result);
    }

    private boolean testEquation(String equation){
        assertTrue(equation.contains("="));
        //if(debug)System.out.println("testEquation input: "+equation);
        String term = equation.split("=")[0]; String expectedResult = equation.split("=")[1];
        if(!(expectedResult.matches("Math Error") || expectedResult.matches("[0-9]+") || expectedResult.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"))){
            expectedResult = calcTerm(expectedResult);
        }
        String output = calcTerm(term);
        if(output.equals(expectedResult)){ return true; }
        if(debug)System.out.println("testEquation result: "+output+" ?= "+expectedResult);
        assertTrue(output.matches("Math Error") || output.matches("[0-9]+") || output.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"));
        assertTrue(expectedResult.matches("Math Error") || expectedResult.matches("[0-9]+") || expectedResult.matches("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?"));

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
        mainActivity.requestFocusInput();

        if(debug)System.out.println("calcTerm: \""+term+"\"");
        String[] splitted = StringUtils.splitTokens(term);
        boolean inputShouldEqualtTerm = true; boolean containsOutputFunctions = false;
        if(debug)System.out.println("cT  "+ Arrays.toString(splitted));
        mainActivity.findViewById(R.id.btn_clear_all).performLongClick();
        for(String s: splitted){
            if(debug)System.out.println("cT input["+mainActivity.getSelectionStartInput()+","+mainActivity.getSelectionEndInput()+"]: "+s+" = "+
                    ((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString());
            if(debug)System.out.println("cT output: "+((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString());
            if(s.isEmpty())break;
            if(idToViewClickMap.containsKey(s) && (idToViewClickMap.get(s) instanceof Button)){
                if(s.contains(">"))containsOutputFunctions = true;
                if(idToModeMap.containsKey(s))mainActivity.onOptionsItemSelected(idToModeMap.get(s));
                if(debug)System.out.println("click: "+((Button) idToViewClickMap.get(s)).getText());
                idToViewClickMap.get(s).performClick();
                if(s.length() > 1 || s.equals("∑") || s.equals("Π") || s.equals("S") || s.equals("PF")){
                    inputShouldEqualtTerm = false;
                    for(int i=0; i<s.length(); i++)mainActivity.btn_right.performClick();
                }
            } else {
                if(idToViewLongClickMap.containsKey(s) && (idToViewLongClickMap.get(s) instanceof Button)){
                    if(idToModeMap.containsKey(s))mainActivity.onOptionsItemSelected(idToModeMap.get(s));
                    idToViewLongClickMap.get(s).performLongClick();
                } else {
                    if(debug)System.out.println("unknown lexical unit: "+s);
                    if(debug)System.out.println(idToViewClickMap.keySet().toString());
                    assertTrue("calcTerm: Button doesnt exist: "+s,false);
                }

            }
        }
        if(debug)System.out.println("cT final input: "+((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString()+"\n\n");
        if(debug)System.out.println("cT final output: "+((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString()+"\n\n");
        if(!containsOutputFunctions)mainActivity.findViewById(R.id.btn_eq_ANS).performClick();
        if(inputShouldEqualtTerm) Assert.assertEquals(((EditText) mainActivity.findViewById(R.id.eT_input)).getText().toString(),term);
        return ((EditText) mainActivity.findViewById(R.id.eT_output)).getText().toString();
    }
}
