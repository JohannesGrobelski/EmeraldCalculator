package com.example.titancalculator;

import com.example.titancalculator.helper.Math_String.NavigatableString;
import com.example.titancalculator.helper.Math_String.NumberString;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class NavigatableStringUnitTest {
    NavigatableString navigatableString;

    @Before
    public void before(){
        navigatableString = new NavigatableString();
    }

    @Test
    public void testText(){
        //clearAll,clear,getLength,addText,concatenateText,setText
        navigatableString.setText("1+1"); assertEquals("1+1",navigatableString.getDisplayableString());

        for(int i=0;i<"1+1".length();i++){
            String before = navigatableString.getDisplayableString();
            navigatableString.clear(0);
            assertEquals(before.substring(1),navigatableString.getDisplayableString());
            assertEquals(3-i-1,navigatableString.getLength());
        }
        for(int i=0; i<((int) Math.random()*100); i++){
            navigatableString.addText("a",0);
            navigatableString.clear(0);
        }
        assertEquals("",navigatableString.getDisplayableString());
        navigatableString.setText("1");
        for(int i=0; i<navigatableString.getLength()/2;i++){
            navigatableString.addText("2",i);
        }
        assertTrue(navigatableString.getDisplayableString().matches("[12]+"));

        navigatableString.setText("1+1"); navigatableString.clearAll(); assertEquals("",navigatableString.getDisplayableString());
        navigatableString.setText("1+1"); assertEquals(3,navigatableString.getLength());
        navigatableString.clearAll();

        String test = "aaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
        navigatableString.concatenateText(test.substring(0,test.indexOf('b')));
        navigatableString.concatenateText(test.substring(test.indexOf('b')));
        assertEquals(test,navigatableString.getDisplayableString());
    }

    @Test
    public void testNumberStringFunctions(){
        //getPercent(), toPercent(..), getInvert(), toInvert(..), getReciproke(), toReciproke(..), toFraction(), getRAD(), getDEG(), getPFZ()
        navigatableString.setText("1.1"); assertEquals("110", navigatableString.getPercent());
        navigatableString.setText("25"); assertEquals("-25", navigatableString.getInvert());
        navigatableString.setText("5"); assertEquals("0.2", navigatableString.getReciproke());
        navigatableString.setText("20"); assertEquals("0.3490658503988659", navigatableString.getRAD());
        navigatableString.setText("20"); assertEquals("1145.9155902616465", navigatableString.getDEG());
        navigatableString.setText("8"); assertEquals("(2,2,2)", navigatableString.getPFZ());
        navigatableString.setText("7.2"); assertEquals("7.2", navigatableString.getPFZ());
        navigatableString.setText("1.22"); assertEquals("1.22", navigatableString.getPFZ());

        //errors
        String[] inputs = {"%","a",""};
        for(int i=0;i<3;i++) {
            navigatableString.setText(inputs[i]);
            assertEquals("Math Error", navigatableString.getPercent());
            assertEquals("Math Error", navigatableString.getInvert());
            assertEquals("Math Error", navigatableString.getReciproke());
            assertEquals("Math Error", navigatableString.getRAD());
            assertEquals("Math Error", navigatableString.getDEG());
            assertEquals("Math Error", navigatableString.getPFZ());
        }

        //setVarMode(), setMeanMode()
        String[] modes = new String[]{"Ari","Geo","Har"};
        for(String mode: modes){
            navigatableString.setMeanMode(mode+"Mit"); assertEquals(mode+"Mit",navigatableString.getMeanMode());
            navigatableString.setVarMode(mode+"Var"); assertEquals(mode+"Var",navigatableString.getVarMode());
        }
        navigatableString.setMeanMode("unknown"); assertEquals("HarMit",navigatableString.getMeanMode());
        navigatableString.setVarMode("unknown"); assertEquals("HarVar",navigatableString.getVarMode());
        //getContent(), setContent(...)
        String content = "1+1"; navigatableString.setText(content); assertEquals(content,navigatableString.getDisplayableString());
    }
}
