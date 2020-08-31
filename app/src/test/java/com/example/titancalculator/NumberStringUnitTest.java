package com.example.titancalculator;

import org.junit.Assert;
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
        assertTrue("wrong: 768ROOT22",NumberString.getCalcuableString("768ROOT22").equals("ROOT(768,22)"));
        assertTrue("wrong: 768C22",NumberString.getCalcuableString("768C22").equals("C(768,22)"));
        //assertTrue("wrong: 1.2ROOT3ROOT4",NumberString.getCalcuableString("1.2ROOT3ROOT4").equals("ROOT(1.2,ROOT(3,4))"));


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
        //String input = "650.271701ROOT828.584434R292LOG36LOG270LOG163.295501"; //
        //System.out.println(NumberString.paraInComplex(input));

        //produces a String like this
        String patternFct = "("; for(String s: NumberString.functions_parentIn){patternFct+=s+"|";} patternFct = patternFct.substring(0,patternFct.length()-1); patternFct += ")";
        String patternNumber = "[0-9]*(\\.)?[0-9]+";
        String groupPattern = "("+patternFct+"\\("+patternNumber+",)+"+patternNumber+"\\)+";

        assertFalse("ROOT(411,LOG(975,".matches(groupPattern));
        assertTrue("LOG(857.335529,C(895,C(791.921591,989)))".matches(groupPattern));
        assertTrue("ROOT(509.670383,245.959239)".matches(groupPattern));
        assertTrue("P(32,317.570490)".matches(groupPattern));
        assertTrue("LOG(582,ROOT(719,LOG(278,R(109,R(897,LOG(46,P(380,LOG(82,LOG(63,762)))))))))".matches(groupPattern));
        assertTrue("C(442,C(997.632336,C(666,R(54,135))))".matches(groupPattern));

        String input = "";
        int numberOfGroups = (int)(Math.random() * 10) + 1;
        for(int nog=0; nog<numberOfGroups; nog++){
            //produces a String like this: numberXnumber ... Xnumber with X element of functions_paraIn (like ROOT or LOG)
            int groupLength = (int)(Math.random() * 10) + 1;
            String group = "";
            for(int element=0; element<groupLength; element++) {
                String para = NumberString.functions_paraIn[(int)(Math.random()*NumberString.functions_paraIn.length)];
                String number1 = numbergenerator();
                group += number1+para;
            }
            group += numbergenerator();
            group = NumberString.paraInComplex(group);
            assertTrue(group.matches(groupPattern));
            assertTrue(NumberString.sameOpeningClosingBrackets(group));
            input += group + " + 2*2 + ";
        }
        input += "9";
        //System.out.println(input);
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

    @Test
    public void parathentiseSimpleTestPreBrackets(){
        String patternFct = "("; for(String s: NumberString.functions_parentIn){patternFct+=s+"|";} patternFct = patternFct.substring(0,patternFct.length()-1); patternFct += ")";
        String patternNumber = "[0-9]*(\\.)?[0-9]+";
        String groupPattern = "("+patternFct+"\\("+patternNumber+",)+"+patternNumber+"\\)+";

        assertTrue(NumberString.paraInComplex("3ROOT(8)").matches(groupPattern));
        assertTrue(NumberString.paraInComplex("3ROOT3ROOT(8)").matches(groupPattern));
        assertTrue(NumberString.paraInComplex("3ROOT(3ROOT(8))").matches(groupPattern));
        System.out.println(NumberString.paraInComplex("3ROOT(3ROOT(8))"));
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