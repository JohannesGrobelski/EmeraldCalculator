package com.example.titancalculator;

import com.example.titancalculator.helper.Math_String.MathEvaluator;
import com.example.titancalculator.helper.Math_String.NumberString;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MathUnitTest1 {
    @Test
    public void mathEv1() {


        assertEquals(MathEvaluator.evaluate("LOG(2,AriVAR(2,3))",10),"-2");
        assertEquals(MathEvaluator.evaluate("AriVAR(2,3)",10),"0.25");

        assertEquals(4, 2 + 2);
        assertEquals(MathEvaluator.evaluate(NumberString.getCalcuableString("2√2√2√256"),10),"2");
        //assertEquals(MathEvaluator.evaluate(NumberString.getCalcuableString("3√3√(3^3^3)"),10),"3");

        assertEquals(MathEvaluator.evaluate("78588558.2121",4,5),"7.85886E7");
        assertEquals(MathEvaluator.evaluate("LOG(LOG(2))",4,5),"-0.52139");

        assertEquals(MathEvaluator.evaluate("2.121^32",10),"2.81385E10");

        //assertEquals(toBase("1412432",16,20));
        //assertEquals(NumberString.findLongestParenthesisable("LOGLOG9"));



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

    }



}