package com.example.titancalculator.unittests;

import androidx.core.widget.TextViewCompat;

import com.example.titancalculator.helper.Math_String.MathEvaluator;
import com.example.titancalculator.helper.Math_String.StringUtils;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.titancalculator.helper.Math_String.MathEvaluator.evaluate;
import static com.example.titancalculator.helper.Math_String.MathEvaluator.logToLogb;
import static com.example.titancalculator.helper.Math_String.StringUtils.concatenate;
import static com.example.titancalculator.helper.Math_String.StringUtils.deleteSpan;
import static com.example.titancalculator.helper.Math_String.StringUtils.findLongestMatch;
import static com.example.titancalculator.helper.Math_String.StringUtils.getParameterNumber;
import static com.example.titancalculator.helper.Math_String.StringUtils.insertString;
import static com.example.titancalculator.helper.Math_String.StringUtils.occurences;
import static com.example.titancalculator.helper.Math_String.StringUtils.paraInAtomic;
import static com.example.titancalculator.helper.Math_String.StringUtils.paraInComplex;
import static com.example.titancalculator.helper.Math_String.StringUtils.parenthesise;
import static com.example.titancalculator.helper.Math_String.StringUtils.randomString;
import static com.example.titancalculator.helper.Math_String.StringUtils.repeat;
import static com.example.titancalculator.helper.Math_String.StringUtils.replace;
import static com.example.titancalculator.helper.Math_String.StringUtils.splitTokens;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringUtilsUnitTest {
    int testIterations = 1000;
    private int testIterationen = 100;

    @Test
    public void testRandomString(){
        assertEquals("",randomString(-1));
        assertEquals("",randomString(0));
        for(int ti=0;ti<testIterations;ti++){
            for(int i=0; i<10; i++) {
                int randomLength = ((int) (Math.random() * Math.max(20,testIterations))) + 1;
                String randomString = randomString(randomLength);
                assertEquals(randomLength, randomString.length());
            }
        }
    }



    @Test
    public void testSplit(){

        //assertArrayEquals("1+2".split(""),split("1+2"));
        //assertArrayEquals(new String[]{""},split(""));
        //assertArrayEquals("1+2".split(""),split("1+2","".split("")));


        for(int ti=0;ti<testIterations;ti++) {
            String randomInput = StringUtils.randomString(Math.min(100,((int) (Math.random()*testIterations/10))+1));
            //System.out.println(ti+": "+randomInput);
            String[] splitted1 = splitTokens(randomInput);
            String[] splitted2 = splitTokens(randomInput);

            //System.out.println(ti+": "+Arrays.toString(splitted));
            int lengthSplitted1 = 0; int lengthSplitted2 = 0;
            assertEquals(splitted1.length,splitted2.length);
            for(String subsplit: splitted1){
                lengthSplitted1 += subsplit.length();
            }
            for(String subsplit: splitted2){
                lengthSplitted2 += subsplit.length();
            }
            assertTrue(lengthSplitted1<=randomInput.length());
            assertEquals(lengthSplitted1,lengthSplitted2);
        }






    }

    //actualy used
    @Test
    public void testDeleteSpan(){
        for(int ti=0;ti<((int) (Math.sqrt(testIterations)));ti++){
            String randomInput = StringUtils.randomString(((int) (Math.max(20,Math.sqrt(testIterations)))+1));
            assertEquals(randomInput,deleteSpan(randomInput,0,0));
            assertEquals(randomInput,deleteSpan(randomInput,-1,0));
            assertEquals(randomInput,deleteSpan(randomInput,0,-1));
            assertEquals(randomInput,deleteSpan(randomInput,1,0));
            assertEquals(randomInput,deleteSpan(randomInput,randomInput.length()+1,randomInput.length()));
            for(int spanlength=1;spanlength<=randomInput.length();spanlength++){
                for(int i=0; i<randomInput.length();i++){
                    int end = Math.min(i+spanlength,randomInput.length());
                    String substring = deleteSpan(randomInput,i,end);
                    assertEquals(randomInput.length(),substring.length()+(end-i));
                }
            }
        }
    }



    @Test
    public void testRepeat(){
        for(int ti=0;ti<testIterations;ti++) {
            String randomInput = StringUtils.randomString(Math.max(36,((int) (Math.random()*testIterations))+1));
            assertEquals(randomInput,repeat(randomInput,-1));
            assertEquals("",repeat(randomInput,0));
            assertEquals(randomInput,repeat(randomInput,1));

            int repetitions = ((int) (Math.random()*10)+5);
            String repeat = repeat(randomInput,repetitions);
            assertEquals(repetitions*randomInput.length(),repeat.length());
            repeat = repeat.replace(randomInput,"");
            assertEquals("",repeat.replace(randomInput,""));
        }
    }

    @Test
    public void testReplace(){
        for(int ti=0;ti<testIterations;ti++) {
            String randomInput = StringUtils.randomString(Math.max(36,((int) (Math.random()*testIterations))+5));
            String replacement = StringUtils.randomString(randomInput.length()/2);
            int start = ((int) (Math.random()*10)+5);
            int end = start + 2;

            String replaced = randomInput.substring(start,end);
            String result = replace(randomInput,replacement,start,end);;
            assertTrue(result.contains(replacement));
            assertEquals(randomInput,result.replace(replacement,replaced));
        }
    }

    @Test
    public void testInsertString(){
        assertEquals("ASINH(698)",insertString("ASINH()","698","ASINH()".length()-1));

        for(int ti=0;ti<testIterations;ti++) {
            String randomInput = StringUtils.randomString(Math.max(36,((int) (Math.random()*testIterations))+5));
            String randomInsert = StringUtils.randomString(randomInput.length()/2);
            int start = ((int) (Math.random()*10)+5);
            String result = insertString(randomInput,randomInsert,start);;
            assertTrue(result.contains(randomInsert));
            assertEquals(randomInput,result.replace(randomInsert,""));
        }
    }

    @Test
    public void testDisplayableString(){
        assertEquals("√√4",StringUtils.getDisplayableString("ROOTROOT4"));
    }

    @Test
    public void testOccurences(){
        for(int ti=0;ti<testIterations;ti++) {
            String randomInput = "";
            while(!randomInput.contains("A")){
                randomInput = StringUtils.randomString(Math.max(36,((int) (Math.random()*testIterations))+10));
            }
            String randomInsert = StringUtils.randomString(((int) (Math.random()*5))+1);

            while(randomInput.contains(randomInsert))randomInput = randomInput.replace(randomInsert,"");
            assertEquals(0, occurences(randomInput,randomInsert));


            String copy = randomInput;
            //number of A's
                String noA = "";
                while(noA.isEmpty() || noA.contains("A"))noA = copy.replaceAll("A","");
                int occurences = copy.length() - noA.length();
            assertEquals(occurences,occurences(randomInput,"A"));

            //randomInput = randomInput.replace("A",randomInsert);
            //assertEquals(occurences, occurences(randomInput,randomInsert));
        }
    }

    @Test
    public void testFindLongestMatchTest() {
        for (int ti = 0; ti < testIterations; ti++) {
            String repeat = new String(new char[20]).replace("\0", "1+2FGHI");
            String random = shuffleString(repeat);

            String searchRegex = "A+";
            String searchString = ""; int longestMatch = 0;
            while(true){
                if(random.indexOf(searchString+"A") != -1){
                    ++longestMatch;
                    searchString = new String(new char[longestMatch]).replace("\0", "A");
                }
                else break;
            }
            assertEquals(longestMatch,findLongestMatch(searchString,random).length());
        }
    }



    @Test
    public void testConcatenate() {
        assertArrayEquals(new String[0],concatenate(new String[0],new String[0]));
        for (int ti = 0; ti < testIterations; ti++) {
            String a = randomString(((int) (Math.random()*10)+1));
            String b = randomString(((int) (Math.random()*20)+2));
            String[] A = a.split("");
            String[] B = b.split("");
            assertArrayEquals(A,concatenate(new String[0],A));
            assertArrayEquals(A,concatenate(A,new String[0]));
            assertArrayEquals((a+b).split(""),concatenate(A,B));
            assertArrayEquals((b+a).split(""),concatenate(B,A));
        }
    }


    private String shuffleString(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        String shuffled = "";
        for (String letter : letters) {
            shuffled += letter;
        }
        return shuffled;
    }

    @Test
    public void paraInAtomicTest(){
        for(String function: StringUtils.functions_paraIn){
            String number1 = String.valueOf(Math.random()*1000);
            String number2 = String.valueOf(Math.random()*1000);

            assertEquals(function.replace("SQRT","ROOT")+"("+number1+","+number2+")",paraInAtomic(number1+function+number2));
            assertEquals(number1,paraInAtomic(number1));
        }

    }

    @Test
    public void paraIn2SimpleTest(){
        assertEquals("ROOT(9,ROOT(7,5))",paraInComplex("9SQRT7SQRT5"));
        assertTrue("wrong: 768SQRT22",paraInComplex("768SQRT22").equals("ROOT(768,22)"));
        assertTrue("wrong: 768C22",paraInComplex("768C22").equals("C(768,22)"));
        assertTrue("wrong: 1.2SQRT3SQRT4",paraInComplex("1.2SQRT3SQRT4").equals("ROOT(1.2,ROOT(3,4))"));
        for(int i=0; i<testIterationen; i++){
            for(String para: StringUtils.functions_paraIn){
                String number1 = numbergenerator();
                String number2 = numbergenerator();
                String result = paraInComplex(number1+para+number2);
                assertTrue("wrong: "+result,result.equals(para.replace("SQRT","ROOT")+"("+number1+","+number2+")"));
            }
        }
    }

    @Test
    public void paraIn2SimpleEncapsulation(){
        // assertTrue("wrong: ³√³√9",("³√³√9").equals("ROOT(3,ROOT(3,9))"));
        // assertTrue("wrong: 1.2ROOT3ROOT4",("1.2ROOT3ROOT4").equals("ROOT(1.2,ROOT(3,4))"));

        assertEquals("ASINACOT.",paraInComplex("ASINACOT."));
        assertEquals("Math Error", MathEvaluator.evaluate("ASINACOT."));
        assertEquals("ASIN(ACOT(.2))",parenthesise("ASINACOT.2"));
        assertEquals(MathEvaluator.evaluate("ASIN(ACOT(.2))"),MathEvaluator.evaluate("ASIN(ACOT(0.2))"));
        assertEquals("ASIN(ACOT(2.))",parenthesise("ASINACOT2."));
        assertEquals(MathEvaluator.evaluate("ASIN(ACOT(2.))"),MathEvaluator.evaluate("ASIN(ACOT(2.0))"));

        assertEquals("ASIN(ACOT(2))",parenthesise("ASINACOT2"));
        assertEquals("ASIN(ACOT(-2))",parenthesise("ASINACOT-2"));
        assertEquals("ASIN(ACOT(2.2))",parenthesise("ASINACOT2.2"));
        assertEquals("ASIN(ACOT(-2.2))",parenthesise("ASINACOT-2.2"));
    }

    @Test
    public void paraIn2ComplexTest(){
        //produces a String like this
        String input = "";
        for(int ti=0;ti<testIterationen;ti++){
            int numberOfGroups = (int)(Math.random() * 10) + 1;
            for(int nog=0; nog<numberOfGroups; nog++){
                //produces a String like this: numberXnumber ... Xnumber with X element of functions_paraIn (like ROOT or LOG)
                int groupLength = (int)(Math.random() * 10) + 1;
                String group = "";
                for(int element=0; element<groupLength; element++) {
                    String para = StringUtils.functions_paraIn[(int)(Math.random()* StringUtils.functions_paraIn.length)];
                    String number1 = numbergenerator();
                    group += number1+para;
                }
                group += numbergenerator();

                String transformed = StringUtils.paraInComplex(group);
                if(group.matches(transformed)){
                    StringUtils.debug = true;
                    StringUtils.paraInComplex(group);
                    assertTrue(false);
                }
                assertTrue(StringUtils.sameOpeningClosingBrackets(group));
                input += group + " + 2*2 + ";
            }
            input += "9";
        }

    }



    @Test
    public void parathentiseSimpleTest(){
        for(int i=0; i<testIterationen; i++) {
            for (String para : StringUtils.functions_parentIn) {
                String number1 = numbergenerator();
                String result = parenthesise(para + number1);
                assertTrue("wrong: " + result+", right:"+para+"("+number1+")", result.equals(para + "(" + number1 + ")"));
            }
        }
    }

    @Test
    public void parathentiseComplexTest(){
        for(int i=0; i<Math.sqrt(testIterationen); i++) {
            for (String para1 : StringUtils.functions_parentIn) {
                for (String para2 : StringUtils.functions_parentIn) {
                    String number1 = numbergenerator();
                    String result = parenthesise(para1+para2+number1);
                    //System.out.println(result+" = "+para1+"("+para2+"("+number1+"))");
                    assertTrue("wrong: " + result, result.equals(para1+ "(" + para2 + "(" + number1 + "))"));
                }
            }
        }
    }

    @Test
    public void parathentiseSimpleTestPreBrackets(){
        String groupPattern = "((ROOT|LOG|P|C|R)\\([0-9]*(\\.)?[0-9]+,)+[0-9]*(\\.)?[0-9]+\\)+";

        assertTrue(StringUtils.paraInComplex("3SQRT(8)").matches(groupPattern));
        assertTrue(StringUtils.paraInComplex("3SQRT3SQRT(8)").matches(groupPattern));
        assertTrue(StringUtils.paraInComplex("3SQRT(3SQRT(8))").matches(groupPattern));
    }

    @Test
    public void testTransformations(){
        for(int it=0;it<testIterationen;it++){
            String function = StringUtils.functions_parentIn[((int) (Math.random()*StringUtils.functions_parentIn.length))];
            int numberParameter = ((int) (Math.random()*20))-10;
            if(numberParameter <=0)assertEquals(0,StringUtils.getParameterNumber(function,0));
            else {
               StringBuilder input = new StringBuilder(function+"(");
               for(int i=0;i<numberParameter;i++)input.append(numbergenerator()+",");
               input.deleteCharAt(input.length()-1); input.append(")");
               System.out.println(input);
               assertEquals(numberParameter,getParameterNumber(input.toString(),0));
            }
        }

        String input = "LOG(LOG(13))";
        assertEquals("LOG10(LOG10(13))",logToLogb(input));
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
