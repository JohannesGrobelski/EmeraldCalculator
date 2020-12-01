package com.example.titancalculator;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class PresenterUnitTest {
    Context context;
    CalcModel calcModel;
    Presenter presenter;
    private MainActivity mainActivity;

    private String[] modes = {"basic","basic2","trigo","hyper","logic","statistic","memory","unknown"};
    private String[] modeBasic = {"π","e","^","LOG","LN","LB","³√","√","³","²","10^","!"};
    private String[] modeBasic2 = {">PFZ","GCD(,)","LCM(,)","∑(,)","∏(,)","",">%",">A/B",">x\u207B\u00B9",">+/-","MIN(,)","MAX(,)"};
    private String[] modeTrigo = {"SIN","COS","TAN","COT","SEC","CSC","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC"};
    private String[] modeHyper = {"SINH","COSH","TANH","ASINH","ACOSH","ATANH",">DEG",">RAD","","","",""};
    private String[] modeLogic = {"AND(,)","OR(,)","XOR(,)","NOT()","","","","","","","",""};
    private String[] modeStatistic = {"Zn()","Zb(,)","C","P","MEAN()","VAR()","√(VAR())","","","","",""};
    private String[] modeMemory = {"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};
    private String[] modeUnknown = {"","","","","","","","","","","",""};
    private String[][] modesModes = {modeBasic,modeBasic2,modeTrigo,modeHyper,modeLogic,modeStatistic,modeMemory,modeUnknown};
    private String[][] btn = new String[2][6];

    @Before
    public void init(){
        mainActivity = Robolectric.setupActivity(MainActivity.class);
        context = mock(Context.class);
        calcModel = new CalcModel(context);
        presenter = new Presenter(mainActivity,context);
    }

    @Test
    public void testInput(){
    //inputClearOne with Selection
        assertTrue(((MainActivity) presenter.view).eT_input.hasFocus());
        presenter.inputButton("⌧"); presenter.addInputText("1+1");
        ((MainActivity) presenter.view).setSelectionInput(0);
        ((MainActivity) presenter.view).eT_input.requestFocus();
        presenter.inputButton("⌫"); inputEquals("1+1");
        ((MainActivity) presenter.view).setSelectionInput(1);
        presenter.inputButton("⌫"); inputEquals("+1");
        ((MainActivity) presenter.view).setSelectionInput(2);
        presenter.inputButton("⌫"); inputEquals("+");
        presenter.inputButton("⌧"); presenter.addInputText("1+1");
        ((MainActivity) presenter.view).setSelectionInput(0,1);
        ((MainActivity) presenter.view).eT_input.requestFocus();
        presenter.inputButton("⌫"); inputEquals("+1");


    //addInputText, inputClearOne, inputClearAll, sinputEqual
        presenter.inputButton("⌧"); inputEquals("");
        presenter.addInputText("hello"); inputEquals("hello"); presenter.inputButton("="); presenter.calcModel.getOutputString().equals("Math Error");
        presenter.inputButton("⌧"); inputEquals("");
        presenter.addInputText("1+1"); presenter.inputButton("="); presenter.calcModel.getOutputString().equals("2");
        for(int i=0;i<"1+1".length()+1;i++){
            inputEquals("1+1".substring(0,"1+1".length()-i));
            presenter.inputButton("⌫");
        }
        presenter.inputButton("⌫"); inputEquals("");
        presenter.inputButton("⌧"); inputEquals("");

        presenter.addInputText("1+1"); presenter.calcModel.setInputText(""); presenter.inputButton("="); presenter.calcModel.getOutputString().equals("2");
        presenter.addInputText("10^10"); presenter.inputButton("="); presenter.calcModel.getOutputString().equals("10000000000");
        presenter.inputButtonLongClick("="); presenter.inputButton("="); presenter.calcModel.getOutputString().equals("10E10");
        presenter.inputButtonLongClick("="); presenter.inputButton("="); presenter.calcModel.getOutputString().equals("10000000000");


    }

    @Test
    public void testInputBtn(){
        calcModel.enableLog=false;

    //functions
        for(int mode=0; mode<modes.length; mode++){
            if(modes[mode].equals("memory"))continue;
            calcModel.setMode(modes[mode]);
            presenter.setMode(modes[mode]);
            for(int i=0; i<12; i++){
                btn[i/6][i%6] = presenter.inputButton(String.valueOf((i/6)+1)+String.valueOf((i%6)+1));
                if(!modesModes[mode][i].contains(">"))inputEquals(modesModes[mode][i]); presenter.inputButton("⌧");
            }
            for(int i=0; i<12; i++){if(!modesModes[mode][i].contains(">"))assertEquals(modesModes[mode][i],btn[i/6][i%6]);}
        }

        calcModel.setMode("basic2"); presenter.setMode("basic2"); presenter.calcModel.setLanguage("german");
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
        modeBasic = new String[]{"π","e","^","LOG","LN","LB","³√","√","x³","x²","10^x","!"};
        modeBasic2 = new String[]{"PFZ","GCD","LCM","∑","∏","",">%",">A/B",">x\u207B\u00B9",">+/-","MIN","MAX"};
        modeTrigo = new String[]{"SIN","COS","TAN","COT","SEC","CSC","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC"};
        modeHyper = new String[]{"SINH","COSH","TANH","ASINH","ACOSH","ATANH",">DEG",">RAD","","","",""};
        modeLogic = new String[]{"AND","OR","XOR","NOT","","","","","","","",""};
        modeStatistic = new String[]{"ZN","ZB","NCR","NPR","MEAN","VAR","S","","","","",""};
        modeMemory = new String[]{"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};
        modeUnknown = new String[]{"","","","","","","","","","","",""};
        modesModes = new String[][]{modeBasic,modeBasic2,modeTrigo,modeHyper,modeLogic,modeStatistic,modeMemory,modeUnknown};
        calcModel.enableLog=false;

    //functions
        for(int mode=0; mode<modes.length; mode++){
            calcModel.setMode(modes[mode]);
            presenter.setMode(modes[mode]);
            if(modes[mode].equals("unknown"))for(int i=0; i<12; i++){assertEquals(modesModes[mode-1][i],presenter.view.getBtnText((((i/6)+1)*10)+((i%6)+1)));}
            else for(int i=0; i<12; i++){assertEquals(modesModes[mode][i],presenter.view.getBtnText((((i/6)+1)*10)+((i%6)+1)));}
        }

        calcModel.setMode("basic2"); presenter.setMode("basic2"); presenter.calcModel.setLanguage("german");
        assertEquals("GGT(,)",presenter.inputButton("12"));
        assertEquals("KGV(,)",presenter.inputButton("13"));

    //numbers
        for(int i=0; i<=9; i++){assertEquals(String.valueOf(i),presenter.inputButton(String.valueOf(i)));}

    //other identifiers
        String[] otherIdentifiers = new String[]{".",",","ANS","(",")","+","-","*","/"};
        for(String id: otherIdentifiers){assertEquals(id,presenter.inputButton(id));}
    }

    @Test
    public void testGetterSetter(){
    //getMode(),setMode(String),inputButtonLongClick("=")
        assertEquals("basic",presenter.getMode()); presenter.setMode("basic2"); assertEquals("basic2",presenter.getMode());
        assertFalse(presenter.calcModel.isScientificNotation()); presenter.inputButtonLongClick("="); assertTrue(presenter.calcModel.isScientificNotation());
    }

    private void inputEquals(String input){
        assertEquals(input,presenter.calcModel.getInputText());
        assertEquals(input,mainActivity.eT_input.getText().toString());
    }
}
