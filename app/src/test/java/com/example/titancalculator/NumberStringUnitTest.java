package com.example.titancalculator;

import org.junit.Test;

import com.example.titancalculator.helper.Math_String.MathEvaluator;
import com.example.titancalculator.helper.Math_String.NumberString;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class NumberStringUnitTest {
    int testIterationen = 100;

    @Test
    public void paraIn2SimpleTest(){
        assertTrue("wrong: 768ROOT22",NumberString.getCalcuableString("768ROOT22").equals("ROOT(768,22)"));
        assertTrue("wrong: 768C22",NumberString.getCalcuableString("768C22").equals("C(768,22)"));
        assertTrue("wrong: 1.2ROOT3ROOT4",NumberString.getCalcuableString("1.2ROOT3ROOT4").equals("ROOT(1.2,ROOT(3,4))"));
        for(int i=0; i<testIterationen; i++){
            for(String para: NumberString.functions_paraIn){
                String number1 = numbergenerator();
                String number2 = numbergenerator();
                String result = NumberString.getCalcuableString(number1+para+number2);
                assertTrue("wrong: "+result,result.equals(para+"("+number1+","+number2+")"));
            }
        }
    }

    @Test
    public void paraIn2SimpleEncapsulation(){
       // assertTrue("wrong: ³√³√9",NumberString.getCalcuableString("³√³√9").equals("ROOT(3,ROOT(3,9))"));
       // assertTrue("wrong: 1.2ROOT3ROOT4",NumberString.getCalcuableString("1.2ROOT3ROOT4").equals("ROOT(1.2,ROOT(3,4))"));

        assertEquals("ASINACOT.",NumberString.getCalcuableString("ASINACOT."));
        assertEquals("Math Error",MathEvaluator.evaluate("ASINACOT."));
        assertEquals("ASIN(ACOT(.2))",NumberString.getCalcuableString("ASINACOT.2"));
        assertEquals(MathEvaluator.evaluate("ASIN(ACOT(.2))"),MathEvaluator.evaluate("ASIN(ACOT(0.2))"));
        assertEquals("ASIN(ACOT(2.))",NumberString.getCalcuableString("ASINACOT2."));
        assertEquals(MathEvaluator.evaluate("ASIN(ACOT(2.))"),MathEvaluator.evaluate("ASIN(ACOT(2.0))"));

        assertEquals("ASIN(ACOT(2))",NumberString.getCalcuableString("ASINACOT2"));
        assertEquals("ASIN(ACOT(-2))",NumberString.getCalcuableString("ASINACOT-2"));
        assertEquals("ASIN(ACOT(2.2))",NumberString.getCalcuableString("ASINACOT2.2"));
        assertEquals("ASIN(ACOT(-2.2))",NumberString.getCalcuableString("ASINACOT-2.2"));
    }

    @Test
    public void paraIn2ComplexTest(){
        String input = "650.271701ROOT828.584434ROOT292LOG36LOG270LOG163.295501"; //
        System.out.println(NumberString.paraInComplex(input));

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

        input = "";
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
    }


    @Test
    public void parathentiseSimpleTest(){
        for(int i=0; i<testIterationen; i++) {
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
        for(int i=0; i<testIterationen; i++) {
            for (String para1 : NumberString.functions_parentIn) {
                for (String para2 : NumberString.functions_parentIn) {
                    String number1 = numbergenerator();
                    String result = NumberString.getCalcuableString(para1+para2+number1);
                    //System.out.println(result+" = "+para1+"("+para2+"("+number1+"))");
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

    @Test
    public void testScientific(){
        NumberString numberString = new NumberString();
        String[] inputs = {"1","5.5","10^10"};
        String[] outputsNormal = {"1","5.5","10000000000"};
        String[] outputsScientific = {"1","5.5","10E10"};

        for(int i=0;i<2;i++){
            numberString.setContent(inputs[i]);
            assertEquals(outputsNormal[i], numberString.scientificToNormal());
            assertEquals(outputsScientific[i], numberString.normalToScientific());
        }
    }

    @Test
    public void testANS(){
        NumberString numberString = new NumberString();
        numberString.setContent("1+1"); String ans = numberString.scientificToNormal();
        assertEquals("2",ans);
        numberString.setContent("ANS*2"); ans = numberString.scientificToNormal(); assertEquals("4",ans);
        numberString.setContent("ATANSIN8"); ans = numberString.normalToScientific(); assertEquals("7.9231381",ans);

        numberString.setContent("ANS"); numberString.normalToScientific(); assertEquals("7.9231381",ans);

    }

    @Test
    public void testGetterSetter(){
        NumberString numberString = new NumberString();
        //getPercent(), toPercent(..), getInvert(), toInvert(..), getReciproke(), toReciproke(..), toFraction(), getRAD(), getDEG(), getPFZ()

        numberString.setContent("1.1"); assertEquals("110", numberString.getPercent());
        assertEquals("-250", numberString.toPercent("-2.5"));
        numberString.setContent("25"); assertEquals("-25", numberString.getInvert());
        assertEquals("222.2", numberString.toInvert("-222.2"));
        numberString.setContent("5"); assertEquals("0.2", numberString.getReciproke());
        assertEquals("0.02", numberString.toReciproke("50"));
        numberString.setContent("0.5"); assertEquals("1/2", numberString.toFraction());
        numberString.setContent("20"); assertEquals("0.3490658503988659", numberString.getRAD());
        numberString.setContent("20"); assertEquals("1145.9155902616465", numberString.getDEG());
        numberString.setContent("8"); assertEquals("(2,2,2)", numberString.getPFZ());
        numberString.setContent("7.2"); assertEquals("7.2", numberString.getPFZ());
        numberString.setContent("1.22"); assertEquals("1.22", numberString.getPFZ());

        //errors
        String[] inputs = {"%","a",""};
        for(int i=0;i<3;i++) {
            numberString.setContent(inputs[i]);
            assertEquals("Math Error", numberString.getPercent());
            assertEquals("Math Error", numberString.toPercent(inputs[i]));
            assertEquals("Math Error", numberString.getInvert());
            assertEquals("Math Error", numberString.toInvert(inputs[i]));
            assertEquals("Math Error", numberString.getReciproke());
            assertEquals("Math Error", numberString.toReciproke(inputs[i]));
            assertEquals("Math Error", numberString.toFraction());
            assertEquals("Math Error", numberString.getRAD());
            assertEquals("Math Error", numberString.getDEG());
            assertEquals("Math Error", numberString.getPFZ());
        }

        //setVar_mode(), setMean_mode()
        String[] modes = new String[]{"Ari","Geo","Har"};
        for(String mode: modes){
            numberString.setMean_mode(mode+"Mit"); assertEquals(mode+"Mit",NumberString.mean_mode);
            numberString.setVar_mode(mode+"Var"); assertEquals(mode+"Var",NumberString.var_mode);
        }
        numberString.setMean_mode("unknown"); assertEquals("HarMit",NumberString.mean_mode);
        numberString.setVar_mode("unknown"); assertEquals("HarVar",NumberString.var_mode);
        //getContent(), setContent(...)
        String content = "1+1"; numberString.setContent(content); assertEquals(content,numberString.getContent());

        //getDisplayableString()


    }

}