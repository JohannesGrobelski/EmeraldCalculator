package com.example.titancalculator;

import org.junit.Test;
import androidx.fragment.app.testing.*;

import com.example.titancalculator.helper.Math_String.NumberString;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class NumberStringUnitTest {


    @Test
    public void paraIn2SimpleTest(){
        for(int i=0; i<100; i++){
            for(String para: NumberString.functions_paraIn){
                String number1 = numbergenerator();
                String number2 = numbergenerator();
                String result = NumberString.getCalcuableString(number1+para+number2);
                assertTrue("wrong: "+result,result.equals(para+"("+number1+","+number2+")"));
            }
        }
    }

    @Test
    public void paraIn2ComplexTest(){

    }


    @Test
    public void parathentiseSimpleTest(){
        for(int i=0; i<100; i++) {
            for (String para : NumberString.functions_parentIn) {
                String number1 = numbergenerator();
                String result = NumberString.getCalcuableString(para + number1);
                if (para.equals("MEAN")) result = result.replaceAll(NumberString.mean_mode, "MEAN");
                if (para.equals("VAR")) result = result.replaceAll(NumberString.var_mode, "VAR");
                assertTrue("wrong: " + result, result.equals(para + "(" + number1 + ")"));
            }
        }
    }

    @Test
    public void parathentiseComplexTest(){
        for(int i=0; i<10; i++) {
            for (String para1 : NumberString.functions_parentIn) {
                for (String para2 : NumberString.functions_parentIn) {
                    String number1 = numbergenerator();
                    String result = NumberString.getCalcuableString(para1+para2+number1);
                    result = result.replaceAll(NumberString.mean_mode, "MEAN");
                    result = result.replaceAll(NumberString.var_mode, "VAR");
                    assertTrue("wrong: " + result, result.equals(para1+ "(" + para2 + "(" + number1 + "))"));
                }
            }
        }
    }

    public String numbergenerator(){
        boolean isInteger = true;
        if(Math.random()*2 > 1) isInteger = false;
        if(isInteger){
            return String.valueOf((int) (Math.random()*1000));
        }
        else{
            String prefix = String.valueOf((int) (Math.random()*1000));
            String postfix = String.valueOf((int) (Math.random()*1000000));
            return prefix+"."+postfix;
        }
    }

}