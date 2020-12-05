package com.example.titancalculator;

import android.os.SystemClock;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.titancalculator.helper.Math_String.StringUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;

import java.util.HashMap;
import java.util.Map;

import static com.example.titancalculator.CalcModel.modesModesText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityUnitTest {
    private static int iterationsSubtests = 10;
    private static double toleranceDigits = 10;
    private Map<String, View> idToViewMap = new HashMap<>();
    private Map<String, RoboMenuItem> idToModeMap = new HashMap<>();
    private String[] delimiters = new String[]{"1","2","3","4","5","6","7","8","9","0",".",",","ANS","*","/","+","-","(",")",
            "π","e","^","LOG","LN","LB","³√","√","³","²","10^x","!",
            "PFZ","GCD","LCM","∑","∏",">%",">A/B",">x\u207B\u00B9",">+/-","MIN","MAX",
            "SIN","COS","TAN","COT","SEC","CSC","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC",
            "SINH","COSH","TANH","ASINH","ACOSH","ATANH",">DEG",">RAD",
            "AND","OR","XOR","NOT",">BIN",">OCT",">DEC",">HEX",
            "Zn","Zb","NCR","NPR","MEAN","VAR","S",
            "M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6",
            "L","R"
    };
    private HashMap<String,Integer> translateFunctionToNumberParameters;
    private MainActivity mainActivity;
    private RoboMenuItem currentMode;


    @Before public void setUp(){
        mainActivity = Robolectric.setupActivity(MainActivity.class);
        MockitoAnnotations.initMocks(this);
        currentMode = new RoboMenuItem(R.id.basic);
    }

    @Test public void testDisplayFunctions(){
        Button[] B = new Button[]{mainActivity.findViewById(R.id.btn_11),mainActivity.findViewById(R.id.btn_12),mainActivity.findViewById(R.id.btn_13),
                mainActivity.findViewById(R.id.btn_14),mainActivity.findViewById(R.id.btn_15),mainActivity.findViewById(R.id.btn_16),
                mainActivity.findViewById(R.id.btn_21),mainActivity.findViewById(R.id.btn_22),mainActivity.findViewById(R.id.btn_23),
                mainActivity.findViewById(R.id.btn_24),mainActivity.findViewById(R.id.btn_25),mainActivity.findViewById(R.id.btn_26)};
        Button[] A =  new Button[]{mainActivity.findViewById(R.id.btn_1),mainActivity.findViewById(R.id.btn_2),mainActivity.findViewById(R.id.btn_3),
                mainActivity.findViewById(R.id.btn_4),mainActivity.findViewById(R.id.btn_5),mainActivity.findViewById(R.id.btn_6),
                mainActivity.findViewById(R.id.btn_7),mainActivity.findViewById(R.id.btn_8),mainActivity.findViewById(R.id.btn_9),
                mainActivity.findViewById(R.id.btn_0),mainActivity.findViewById(R.id.btn_com),mainActivity.findViewById(R.id.btn_sep),
                mainActivity.findViewById(R.id.btn_open_bracket),mainActivity.findViewById(R.id.btn_close_bracket),mainActivity.findViewById(R.id.btn_mul),
                mainActivity.findViewById(R.id.btn_div),mainActivity.findViewById(R.id.btn_add),mainActivity.findViewById(R.id.btn_sub),
                mainActivity.findViewById(R.id.btn_clear_all)};
        Button[] S = new Button[]{mainActivity.findViewById(R.id.btn_clearall),mainActivity.findViewById(R.id.btn_eq_ANS)};
        EditText INPUT  = mainActivity.findViewById(R.id.eT_input);
        EditText OUTPUT = mainActivity.findViewById(R.id.eT_output);

        for(View v: B)assertTrue(v.isShown());
        mainActivity.findViewById(R.id.btn_FUN).performClick();
        for(View v: B){
            if(v.isShown())System.out.println(v.getTag());
            assertFalse(v.isShown());
        }
        mainActivity.findViewById(R.id.btn_FUN).performClick();
        for(View v: B)assertTrue(v.isShown());

        String[] fun1 = new String[]{"1","2","3","4","5","6","7","8","9","0",".",",","(",")","*","/","+","-","ANS"};
        for(int i=0; i<fun1.length; i++){
            assertEquals(((Button) A[i]).getText().toString(),fun1[i]);
        }
        fun1 = new String[]{"1","2","3","4","5","6","7","8","9","0",".",",","(",")","*","/","+","-","ANS"};
        for(int i=0; i<fun1.length; i++){
            A[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        //not tested in functionality: FUN, deleteAll, delete, L, R, =
        Button[] nT = new Button[]{mainActivity.findViewById(R.id.btn_FUN),mainActivity.findViewById(R.id.btn_clear),mainActivity.findViewById(R.id.btn_clearall),
                                   mainActivity.findViewById(R.id.btn_LINKS),mainActivity.findViewById(R.id.btn_RECHTS),mainActivity.findViewById(R.id.btn_eq_ANS) };
        for(Button button: nT) {button.performClick();} mainActivity.findViewById(R.id.btn_eq_ANS).performLongClick();

        fun1 = new String[]{"π","e","^","LOG","LN","LB","³√","√","x³","x²","10^x","!"};
        for(int i=0; i<fun1.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun1[i]);
        }
        fun1 = new String[]{"π","e","^","LOG","LN","LB","³√","√","³","²","10^","!"};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        MenuItem menuItem = new RoboMenuItem(R.id.basic2);
        mainActivity.onOptionsItemSelected(menuItem);
        String[] fun2 = new String[]{"PFZ","GCD","LCM","∑","∏","",">%",">A/B",">x⁻¹",">+/-","MIN","MAX"};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun2[i]);
        }
        fun1 = new String[]{"","GCD(,)","LCM(,)","∑(,)","∏(,)","","","","","","MIN(,)","MAX(,)"};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.trigo));
        fun2 = new String[]{"SIN","COS","TAN","COT","SEC","CSC","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC"};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun2[i]);
        }
        fun1 = new String[]{"SIN","COS","TAN","COT","SEC","CSC","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC"};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.hyper));
        fun2 = new String[]{"SINH","COSH","TANH","ASINH","ACOSH","ATANH",">DEG",">RAD","","","",""};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun2[i]);
        }
        fun1 = new String[]{"SINH","COSH","TANH","ASINH","ACOSH","ATANH","","","","","",""};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.logic));
        fun2 = new String[]{"AND","OR","XOR","NOT","","","","","","","",""};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun2[i]);
        }
        fun1 = new String[]{"AND(,)","OR(,)","XOR(,)","NOT()","","","","","","","",""};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.statistic));
        fun2 = new String[]{"ZN","ZB","NCR","NPR","MEAN","VAR","S","","","","",""};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun2[i]);
        }
        fun1 = new String[]{"Zn()","Zb(,)","C","P","MEAN()","VAR()","√(VAR())","","","","",""};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.memory));
        fun2 = new String[]{"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};
        for(int i=0; i<fun2.length; i++){
            assertEquals(((Button) B[i]).getText().toString(),fun2[i]);
        }
        fun1 = new String[]{"","","","","","","","","","","",""};
        for(int i=0; i<fun1.length; i++){
            B[i].performClick(); assertEquals(INPUT.getText().toString(),fun1[i]); S[0].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[1].performClick();
        }

        //test setButtonText
        fun2 = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l"};
        for(int i=0; i<fun2.length; i++){
            mainActivity.setBtnText((((10*((i/6)+1))+(i%6))+1),fun2[i]);
        }
        for(int i=0; i<fun1.length; i++){
            assertEquals(fun2[i],mainActivity.getBtnText((((10*((i/6)+1))+(i%6))+1)));
        }
    }

    @Test public void testTouch() throws InterruptedException {
        Button one = mainActivity.findViewById(R.id.btn_1);

        one.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), (int) MotionEvent.ACTION_DOWN, 0, 0, 0));
        assertEquals(1,one.getTypeface().getStyle());
        one.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), (int) MotionEvent.ACTION_UP, 0, 0, 0));
        assertEquals(0,one.getTypeface().getStyle());
    }


    @Test public void testText(){
        String random1 = StringUtils.randomString(10);
        String random2 = StringUtils.randomString(5);

        //test: setSelectionStartInput(start),setSelectionStartInput(start,end),getSelectionStartInput,getSelectionEndInput
        mainActivity.requestFocusInput();
        assertEquals(mainActivity.getInputText(),"");
        assertEquals(mainActivity.getSelectionStartInput(), mainActivity.getSelectionEndInput());
        assertEquals(0,mainActivity.getSelectionStartInput());
        mainActivity.setSelectionInput(0);
        assertEquals(0,mainActivity.getSelectionStartInput()); assertEquals(0,mainActivity.getSelectionEndInput());
        int end = ((int) Math.random()*mainActivity.getInputText().length()); mainActivity.setSelectionInput(0,end);
        assertEquals(0,mainActivity.getSelectionStartInput()); assertEquals(end,mainActivity.getSelectionEndInput());

        //test: setSelectionStartOutput(start),setSelectionStartOutput(start,end),getSelectionStartOutput,getSelectionEndOutput
        mainActivity.requestFocusOutput();
        assertEquals(mainActivity.getOutputText(),"");
        assertEquals(mainActivity.getSelectionStartOutput(), mainActivity.getSelectionEndOutput());
        assertEquals(0,mainActivity.getSelectionStartOutput());
        mainActivity.setSelectionOutput(0);
        assertEquals(0,mainActivity.getSelectionStartOutput()); assertEquals(0,mainActivity.getSelectionEndOutput());
        end = ((int) Math.random()*mainActivity.getOutputText().length()); mainActivity.setSelectionOutput(0,end);
        assertEquals(0,mainActivity.getSelectionStartOutput()); assertEquals(end,mainActivity.getSelectionEndOutput());

        mainActivity.requestFocusInput();
        mainActivity.setInputText("1+2");
        mainActivity.setSelectionInput(0,0); assertEquals("",mainActivity.getSelection());
        mainActivity.setSelectionInput(0,1); assertEquals("1",mainActivity.getSelection());
        mainActivity.setSelectionInput(2,3); assertEquals("2",mainActivity.getSelection());

    }

    @Test public void testModeSwitch(){
        RoboMenuItem[] modes = new RoboMenuItem[]{
                new RoboMenuItem(R.id.basic), new RoboMenuItem(R.id.basic2), new RoboMenuItem(R.id.trigo), new RoboMenuItem(R.id.hyper),
                new RoboMenuItem(R.id.logic), new RoboMenuItem(R.id.statistic), new RoboMenuItem(R.id.memory)
        };
        //functions
        for(int mode=0;mode<7;mode++){
            mainActivity.onOptionsItemSelected(modes[mode]);
            for(int i=0; i<12; i++){assertEquals(modesModesText[mode][i],mainActivity.getBtnText(10*((i/6)+1) + (i%6)+1));}
        }
    }





}
