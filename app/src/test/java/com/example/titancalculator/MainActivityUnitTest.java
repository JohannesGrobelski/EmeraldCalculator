package com.example.titancalculator;

import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.graphics.Path;
import android.graphics.Point;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.titancalculator.helper.Math_String.StringUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.example.titancalculator.CalcModel.modeStatisticFunctionality;
import static com.example.titancalculator.CalcModel.modesModesText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

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

    Toolbar toolbar; TextView toolbarTitle;
    LinearLayout science_background;
    LinearLayout display;
    Button btn_clear_all; Button btn_left; Button btn_right;
    Button btn_11; Button btn_12;Button btn_13;Button btn_14;Button btn_15;Button btn_16;
    Button btn_21; Button btn_22; Button btn_23; Button btn_24; Button btn_25; Button btn_26;
    Button btn_1; Button btn_2; Button btn_3; Button btn_4; Button btn_5; Button btn_6; Button btn_7; Button btn_8; Button btn_9; Button btn_0;
    Button btn_open_bracket; Button btn_close_bracket; Button btn_add; Button btn_sub; Button btn_mul; Button btn_div; Button btn_com; Button btn_sep; Button btn_eq_ans;
    EditText eT_input; EditText eT_output;
    Button[] allButtons;

    @Before public void setUp(){
        mainActivity = Robolectric.setupActivity(MainActivity.class);
        MockitoAnnotations.initMocks(this);
        RoboMenuItem currentMode = new RoboMenuItem(R.id.basic);

        toolbar = (Toolbar) mainActivity.findViewById(R.id.toolbar);
        toolbarTitle = mainActivity.findViewById(R.id.toolbar_title);
        science_background = mainActivity.findViewById(R.id.science_background);
        eT_input = mainActivity.findViewById(R.id.eT_input);
        eT_output = mainActivity.findViewById(R.id.eT_output);
        display = mainActivity.findViewById(R.id.display);
        science_background = mainActivity.findViewById(R.id.science_background);
        btn_clear_all = mainActivity.findViewById(R.id.btn_clear_all);
        btn_11 = mainActivity.findViewById(R.id.btn_11);  btn_12 = mainActivity.findViewById(R.id.btn_12);  btn_13 = mainActivity.findViewById(R.id.btn_13);  btn_14 = mainActivity.findViewById(R.id.btn_14);  btn_15 = mainActivity.findViewById(R.id.btn_15);  btn_16 = mainActivity.findViewById(R.id.btn_16);
        btn_21 = mainActivity.findViewById(R.id.btn_21);  btn_22 = mainActivity.findViewById(R.id.btn_22);  btn_23 = mainActivity.findViewById(R.id.btn_23);  btn_24 = mainActivity.findViewById(R.id.btn_24);  btn_25 = mainActivity.findViewById(R.id.btn_25);  btn_26 = mainActivity.findViewById(R.id.btn_26);
        btn_1 = mainActivity.findViewById(R.id.btn_1);  btn_2 = mainActivity.findViewById(R.id.btn_2);  btn_3 = mainActivity.findViewById(R.id.btn_3);  btn_4 = mainActivity.findViewById(R.id.btn_4);  btn_5 = mainActivity.findViewById(R.id.btn_5);
        btn_6 = mainActivity.findViewById(R.id.btn_6);  btn_7 = mainActivity.findViewById(R.id.btn_7);  btn_8 = mainActivity.findViewById(R.id.btn_8);  btn_9 = mainActivity.findViewById(R.id.btn_9);  btn_0 = mainActivity.findViewById(R.id.btn_0);
        btn_com = mainActivity.findViewById(R.id.btn_com);  btn_sep = mainActivity.findViewById(R.id.btn_sep);
        btn_open_bracket = mainActivity.findViewById(R.id.btn_open_bracket);  btn_close_bracket = mainActivity.findViewById(R.id.btn_close_bracket);
        btn_add = mainActivity.findViewById(R.id.btn_add);  btn_sub = mainActivity.findViewById(R.id.btn_sub);  btn_mul = mainActivity.findViewById(R.id.btn_mul);  btn_div = mainActivity.findViewById(R.id.btn_div);  btn_eq_ans = mainActivity.findViewById(R.id.btn_eq_ANS);
        btn_left = new Button(mainActivity); btn_right = new Button(mainActivity);
        allButtons = new Button[] {btn_11,btn_12,btn_13,btn_14,btn_15,btn_16,btn_21,btn_22,btn_23,btn_24,btn_25,btn_26,btn_clear_all,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_open_bracket,btn_close_bracket,btn_add,btn_sub,btn_mul,btn_div,btn_com,btn_sep,btn_eq_ans};

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
                mainActivity.findViewById(R.id.btn_div),mainActivity.findViewById(R.id.btn_add),mainActivity.findViewById(R.id.btn_sub)};
        Button[] S = new Button[]{mainActivity.findViewById(R.id.btn_clear_all),mainActivity.findViewById(R.id.btn_eq_ANS)};
        EditText INPUT  = mainActivity.findViewById(R.id.eT_input);
        EditText OUTPUT = mainActivity.findViewById(R.id.eT_output);

        String[] fun1 = new String[]{"1","2","3","4","5","6","7","8","9","0",".",",","(",")","*","/","+","-"};
        for(int i=0; i<fun1.length; i++){
            assertEquals(((Button) A[i]).getText().toString(),fun1[i]);
        }
        fun1 = new String[]{"1","2","3","4","5","6","7","8","9","0",".",",","(",")","*","/","+","-"};
        for(int i=0; i<10; i++){
            A[i].performClick(); assertEquals(fun1[i],INPUT.getText().toString()); S[1].performClick(); assertEquals(OUTPUT.getText().toString(),INPUT.getText().toString()); S[0].performLongClick();
        }
        

        fun1 = CalcModel.modeBasicText;
        for(int i=0; i<fun1.length; i++){
            assertEquals(fun1[i],((Button) B[i]).getText().toString());
        }
        String[] fun2 = CalcModel.modeBasicFunctionality;
        for(int i=0; i<fun2.length; i++){
            if(fun2[i].startsWith(">"))continue;
            B[i].performClick();
            assertEquals(fun2[i],INPUT.getText().toString()); S[0].performLongClick();
        }

        MenuItem menuItem = new RoboMenuItem(R.id.advanced);
        mainActivity.onOptionsItemSelected(menuItem);
        fun1 = CalcModel.modeAdvancedText;
        for(int i=0; i<fun1.length; i++){
            assertEquals(fun1[i],((Button) B[i]).getText().toString());
        }
        fun2 = CalcModel.modeAdvancedFunctionality;
        for(int i=0; i<fun2.length; i++){
            if(fun2[i].startsWith(">"))continue;
            B[i].performClick(); assertEquals(fun2[i],INPUT.getText().toString()); S[0].performLongClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.trigo));
        fun1 = CalcModel.modeTrigoText;
        for(int i=0; i<fun1.length; i++){
            assertEquals(fun1[i],((Button) B[i]).getText().toString());
        }
        fun2 = CalcModel.modeTrigoFunctionality;
        for(int i=0; i<fun2.length; i++){
            if(fun2[i].startsWith(">"))continue;
            B[i].performClick(); assertEquals(fun2[i],INPUT.getText().toString()); S[0].performLongClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.statistic));
        fun1 = CalcModel.modeStatisticText;
        for(int i=0; i<fun1.length; i++){
            assertEquals(fun1[i],((Button) B[i]).getText().toString());
        }
        fun2 = CalcModel.modeStatisticFunctionality;
        for(int i=0; i<fun2.length; i++){
            if(fun2[i].startsWith(">"))continue;
            B[i].performClick(); assertEquals(fun2[i],INPUT.getText().toString()); S[0].performLongClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.logic));
        fun1 = CalcModel.modeLogicText;
        for(int i=0; i<fun1.length; i++){
            assertEquals(fun1[i],((Button) B[i]).getText().toString());
        }
        fun2 = CalcModel.modeLogicFunctionality;
        for(int i=0; i<fun2.length; i++){
            if(fun2[i].startsWith(">"))continue;
            B[i].performClick(); assertEquals(fun2[i],INPUT.getText().toString()); S[0].performLongClick();
        }

        mainActivity.onOptionsItemSelected(new RoboMenuItem(R.id.memory));
        fun1 = CalcModel.modeMemoryText;
        for(int i=0; i<fun1.length; i++){
            assertEquals(fun1[i],((Button) B[i]).getText().toString());
        }
        fun2 = CalcModel.modeMemoryFunctionality;
        for(int i=0; i<fun2.length; i++){
            if(fun2[i].startsWith(">"))continue;
            B[i].performClick(); assertEquals("",INPUT.getText().toString()); S[0].performLongClick();
        }

        //test setButtonText
        fun1 = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l"};
        for(int i=0; i<fun1.length; i++){
            mainActivity.setBtnText((((10*((i/6)+1))+(i%6))+1),fun1[i]);
        }
        for(int i=0; i<fun1.length; i++){
            assertEquals(fun1[i],mainActivity.getBtnText((((10*((i/6)+1))+(i%6))+1)));
        }
    }

    @Test public void testTouch() throws InterruptedException {
        Button one = mainActivity.findViewById(R.id.btn_1);

        one.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), (int) MotionEvent.ACTION_DOWN, 0, 0, 0));
        assertEquals(1,one.getTypeface().getStyle());
        one.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), (int) MotionEvent.ACTION_UP, 0, 0, 0));
        assertEquals(0,one.getTypeface().getStyle());
    }

    @Test
    public void testOrientationChange(){
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
    }

    @Test
    public void testModes(){
        for(int i=0;i<mainActivity.toolbar.getMenu().size();i++){
            mainActivity.toolbar.getMenu().getItem(i).setChecked(true);
            mainActivity.toolbar.getMenu().getItem(i).setChecked(false);
        }
    }


    @Test
    public void testInputCase(){
        btn_clear_all.performLongClick();
        String input = "518^COSACOS/(²√5COSACOS7ANSeTANATAN(2LOG10^√²-,559/e51e,(COSACOS3(61";
        mainActivity.setInputText(input);
        assertEquals(input,mainActivity.getInputText());
        btn_eq_ans.performLongClick();
    }

    /**
     * click n views with no error
     */
    @Test
    public void testClickRandomInput(){
        int testIterations = 1000;
        for(int it=0;it<testIterations;it++){
            int randomButtonIndex = ((int) (Math.random()*allButtons.length));
            System.out.println(it);
            System.out.println("input: "+mainActivity.getInputText());
            System.out.println("output: "+mainActivity.getOutputText());
            System.out.println("next button: "+allButtons[randomButtonIndex].getText().toString());
            try {
                allButtons[randomButtonIndex].performClick();
                allButtons[randomButtonIndex].performLongClick();
            } catch (Exception e){

            }
            System.out.println();
        }


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
        mainActivity.setSelectionInput(0,0); assertEquals("1+2",mainActivity.getSelection());
        mainActivity.setSelectionInput(0,1); assertEquals("1",mainActivity.getSelection());
        mainActivity.setSelectionInput(2,3); assertEquals("2",mainActivity.getSelection());
    }

    @Test public void testModeSwitch(){
        RoboMenuItem[] modes = new RoboMenuItem[]{
                new RoboMenuItem(R.id.basic), new RoboMenuItem(R.id.advanced), new RoboMenuItem(R.id.trigo),
                new RoboMenuItem(R.id.statistic),new RoboMenuItem(R.id.logic), new RoboMenuItem(R.id.memory)
        };
        //functions
        for(int mode=0;mode<6;mode++){
            mainActivity.onOptionsItemSelected(modes[mode]);
            for(int i=0; i<12; i++){assertEquals(modesModesText[mode][i],mainActivity.getBtnText(10*((i/6)+1) + (i%6)+1));}
        }
    }

    @Test public void testNavigation(){
        //L and R
        assertEquals(-1,mainActivity.getSelectionStartInput()); assertEquals(-1,mainActivity.getSelectionEndInput());

        mainActivity.requestFocusInput();
        int selection = 0; mainActivity.setSelectionInput(selection);
        mainActivity.btn_left.performClick();
        assertEquals(0,mainActivity.getSelectionStartInput()); assertEquals(0,mainActivity.getSelectionEndInput());
        mainActivity.btn_right.performClick();
        assertEquals(0,mainActivity.getSelectionStartInput()); assertEquals(0,mainActivity.getSelectionEndInput());

        selection = 0; mainActivity.setSelectionInput(selection);
        String input = "1+1"; mainActivity.setInputText(input);
        for(int i=0; i<input.length()+5; i++){mainActivity.btn_right.performClick();}
        assertEquals(input.length(),mainActivity.getSelectionEndInput());
        for(int i=0; i<input.length()+5; i++){mainActivity.btn_left.performClick();}
        assertEquals(0,mainActivity.getSelectionEndInput());
    }

    @Test public void testFocus(){
        assertEquals(false,mainActivity.hasFocusInput());
        assertEquals(false,mainActivity.hasFocusOutput());

        mainActivity.requestFocusOutput();
        assertEquals(false,mainActivity.hasFocusInput());
        assertEquals(true,mainActivity.hasFocusOutput());

        mainActivity.clearFocusInput();
        assertEquals(false,mainActivity.hasFocusInput());
        assertEquals(true,mainActivity.hasFocusOutput());
        mainActivity.clearFocusOutput();
        assertEquals(false,mainActivity.hasFocusInput());
        assertEquals(false,mainActivity.hasFocusOutput());

        mainActivity.requestFocusInput();
        assertEquals(true,mainActivity.hasFocusInput());
        assertEquals(false,mainActivity.hasFocusOutput());
        mainActivity.clearFocusOutput();
        assertEquals(true,mainActivity.hasFocusInput());
        assertEquals(false,mainActivity.hasFocusOutput());
        mainActivity.clearFocusInput();
        assertEquals(false,mainActivity.hasFocusInput());
        assertEquals(false,mainActivity.hasFocusOutput());
    }

    @Test public void testSelection(){
        assertEquals(false,mainActivity.hasFocusInput());
        assertEquals(false,mainActivity.hasFocusOutput());
        assertEquals(-1,mainActivity.getSelectionStartOutput());
        assertEquals(-1,mainActivity.getSelectionEndOutput());
        assertEquals(-1,mainActivity.getSelectionStartInput());
        assertEquals(-1,mainActivity.getSelectionEndInput());
    }









}
