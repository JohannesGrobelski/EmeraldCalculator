package com.example.titancalculator;

import android.content.Context;

import com.example.titancalculator.CalcModel;
import com.example.titancalculator.MainActivity;
import com.example.titancalculator.Presenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.example.titancalculator.CalcModel.modes;
import static com.example.titancalculator.CalcModel.modesModesFunctionality;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class PresenterUnitTest {
    CalcModel calcModel;
    Presenter presenter;

    private MainActivity mainActivity;
    private String[][] btn = new String[2][6];

    @Before
    public void init(){
        mainActivity = Robolectric.setupActivity(MainActivity.class);
        calcModel = new CalcModel();
        presenter = new Presenter();
        presenter.attachView(mainActivity);
    }

    @Test
    public void testInput(){
    //inputClearOne with Selection
        assertTrue(mainActivity.hasFocusInput());
        presenter.inputButton("⌧"); presenter.addInputText("1+1");
        mainActivity.setSelectionInput(0);
        mainActivity.requestFocusInput();
        presenter.inputButton("⌫"); inputEquals("1+1");
        mainActivity.setSelectionInput(1);
        presenter.inputButton("⌫"); inputEquals("+1");
        mainActivity.setSelectionInput(2);
        presenter.inputButton("⌫"); inputEquals("+");
        presenter.inputButton("⌧"); presenter.addInputText("1+1");
        mainActivity.setSelectionInput(0,1);
        mainActivity.requestFocusInput();
        presenter.inputButton("⌫"); inputEquals("+1");

    //addInputText, inputClearOne, inputClearAll, sinputEqual
        presenter.inputButton("⌧"); inputEquals("");
        presenter.addInputText("hello"); inputEquals("hello"); presenter.inputButton("="); calcModel.getOutputString().equals("Math Error");
        presenter.inputButton("⌧"); inputEquals("");
        presenter.addInputText("1+1"); presenter.inputButton("="); calcModel.getOutputString().equals("2");
        for(int i=0;i<"1+1".length()+1;i++){
            inputEquals("1+1".substring(0,"1+1".length()-i));
            presenter.inputButton("⌫");
        }
        presenter.inputButton("⌫"); inputEquals("");
        presenter.inputButton("⌧"); inputEquals("");

        presenter.addInputText("1+1"); calcModel.setInputText(""); presenter.inputButton("="); calcModel.getOutputString().equals("2");
        presenter.addInputText("10^10"); presenter.inputButton("="); calcModel.getOutputString().equals("10000000000");
        presenter.inputButtonLongClick("="); presenter.inputButton("="); calcModel.getOutputString().equals("10E10");
        presenter.inputButtonLongClick("="); presenter.inputButton("="); calcModel.getOutputString().equals("10000000000");


    }

    @Test
    public void testInputBtn(){
        calcModel.enableLog=false;

    //functions
        for(int mode = 0; mode< modes.length; mode++){
            if(modes[mode].equals("memory"))continue;
            calcModel.setMode(modes[mode]);
            presenter.setMode(modes[mode]);
            for(int i=0; i<12; i++){
                btn[i/6][i%6] = presenter.inputButton(String.valueOf((i/6)+1)+String.valueOf((i%6)+1));
                if(!modesModesFunctionality[mode][i].contains(">"))inputEquals(modesModesFunctionality[mode][i]); presenter.inputButton("⌧");
            }
            for(int i=0; i<12; i++){if(!modesModesFunctionality[mode][i].contains(">"))assertEquals(modesModesFunctionality[mode][i],btn[i/6][i%6]);}
        }

        calcModel.setMode("basic2"); presenter.setMode("basic2"); calcModel.setLanguage("german");
        assertEquals("GGT(,)",presenter.inputButton("12"));
        assertEquals("KGV(,)",presenter.inputButton("13"));

    //numbers
        for(int i=0; i<=9; i++){assertEquals(String.valueOf(i),presenter.inputButton(String.valueOf(i)));}

    //other identifiers
        String[] otherIdentifiers = new String[]{".",",","ANS","(",")","+","-","*","/"};
        for(String id: otherIdentifiers){assertEquals(id,presenter.inputButton(id));}
    }


    @Test
    public void testAssignModeFct(){
        modes = new String[]{"basic","basic2","trigo","hyper","logic","statistic","memory","unknown"};
        calcModel.enableLog=false;

    //functions
        for(int mode = 0; mode< modes.length; mode++){
            presenter.setMode(modes[mode]);
            assertEquals(modes[mode],presenter.getMode());
        }
    }

    @Test
    public void testGetterSetter(){
    //getMode(),setMode(String),inputButtonLongClick("=")
        assertEquals("basic",presenter.getMode()); presenter.setMode("basic2"); assertEquals("basic2",presenter.getMode());
        assertFalse(calcModel.isScientificNotation()); presenter.inputButtonLongClick("="); assertTrue(calcModel.isScientificNotation());
    }

    private void inputEquals(String input){
        assertEquals(input,calcModel.getInputText());
        assertEquals(input,mainActivity.getInputText());
    }
}
