package com.example.titancalculator;

import org.junit.Test;
import androidx.fragment.app.testing.*;

import com.example.titancalculator.helper.Math_String.NumberString;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class NumberStringUnitTest {


    @Test
    public void paraIn2Test(){
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
    public void parathentiseTest(){
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