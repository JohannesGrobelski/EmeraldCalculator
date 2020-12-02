package com.example.titancalculator.unittests;

import com.example.titancalculator.helper.Math_String.NavigatableNumberString;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class NavigatableNumberStringUnitTest {
    NavigatableNumberString navigatableNumberString;

    @Before
    public void before(){
        navigatableNumberString = new NavigatableNumberString();
    }

    @Test
    public void testText(){
        //clearAll,clear,getLength,addText,concatenateText,setText
        navigatableNumberString.setText("1+1"); assertEquals("1+1", navigatableNumberString.getDisplayableString());

        for(int i=0;i<"1+1".length();i++){
            String before = navigatableNumberString.getDisplayableString();
            navigatableNumberString.clear(0);
            assertEquals(before.substring(1), navigatableNumberString.getDisplayableString());
            assertEquals(3-i-1, navigatableNumberString.getLength());
        }
        for(int i=0; i<((int) Math.random()*100); i++){
            navigatableNumberString.addText("a",0);
            navigatableNumberString.clear(0);
        }
        assertEquals("", navigatableNumberString.getDisplayableString());
        navigatableNumberString.setText("1");
        for(int i = 0; i< navigatableNumberString.getLength()/2; i++){
            navigatableNumberString.addText("2",i);
        }
        assertTrue(navigatableNumberString.getDisplayableString().matches("[12]+"));

        navigatableNumberString.setText("1+1"); navigatableNumberString.clearAll(); assertEquals("", navigatableNumberString.getDisplayableString());
        navigatableNumberString.setText("1+1"); assertEquals(3, navigatableNumberString.getLength());
        navigatableNumberString.clearAll();

        String test = "aaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb";
        navigatableNumberString.concatenateText(test.substring(0,test.indexOf('b')));
        navigatableNumberString.concatenateText(test.substring(test.indexOf('b')));
        assertEquals(test, navigatableNumberString.getDisplayableString());
    }


}
