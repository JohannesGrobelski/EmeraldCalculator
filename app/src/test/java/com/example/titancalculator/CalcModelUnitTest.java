package com.example.titancalculator;

import android.content.Context;

import com.example.titancalculator.CalcModel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class CalcModelUnitTest {

    CalcModel calcModel;
    final private String[] modes = {"basic","basic2","trigo","hyper","logic","statistic","memory"};
    final private String[] modeBasic = {"π","e","^","LOG","LN","LB","³√","√","³","²","10^","!"};
    final private String[] modeBasic2 = {">PFZ","GCD(,)","LCM(,)","∑(,)","∏(,)","",">%",">A/B",">x\u207B\u00B9",">+/-","MIN(,)","MAX(,)"};
    final private String[] modeTrigo = {"SIN","COS","TAN","COT","SEC","CSC","ASIN","ACOS","ATAN","ACOT","ASEC","ACSC"};
    final private String[] modeHyper = {"SINH","COSH","TANH","ASINH","ACOSH","ATANH",">DEG",">RAD","","","",""};
    final private String[] modeLogic = {"AND(,)","OR(,)","XOR(,)","NOT()","","","","","","","",""};
    final private String[] modeStatistic = {"Zn()","Zb(,)","C","P","MEAN()","VAR()","√(VAR())","","","","",""};
    final private String[] modeMemory = {"M1","M2","M3","M4","M5","M6",">M1",">M2",">M3",">M4",">M5",">M6"};
    final private String[][] modesModes = {modeBasic,modeBasic2,modeTrigo,modeHyper,modeLogic,modeStatistic,modeMemory};
    private String[][] btn = new String[2][6];

    @Before
    public void init(){
        Context context = mock(Context.class);
        calcModel = new CalcModel(context);
    }

    @Test
    public void testModes(){
        for(int mode=0; mode<modes.length; mode++){
            //System.out.println("mode: "+modes[mode]);
            calcModel.setMode(modes[mode]);
            btn[0][0] = calcModel.translInputBtn11(); btn[0][1] = calcModel.translInputBtn12(); btn[0][2] = calcModel.translInputBtn13();
            btn[0][3] = calcModel.translInputBtn14(); btn[0][4] = calcModel.translInputBtn15(); btn[0][5] = calcModel.translInputBtn16();
            btn[1][0] = calcModel.translInputBtn21(); btn[1][1] = calcModel.translInputBtn22(); btn[1][2] = calcModel.translInputBtn23();
            btn[1][3] = calcModel.translInputBtn24(); btn[1][4] = calcModel.translInputBtn25(); btn[1][5] = calcModel.translInputBtn26();

            for(int i=0; i<12; i++){
                //System.out.println("mode: "+btn[i/6][i%6]+", transl:"+modesModes[mode][i]);
                assertEquals(modesModes[mode][i],btn[i/6][i%6]);
            }
            //System.out.println("\n\n");
        }
        calcModel.setMode("basic2"); calcModel.setLanguage("german");
        assertEquals("GGT(,)",calcModel.translInputBtn12());
        assertEquals("KGV(,)",calcModel.translInputBtn13());

        calcModel.enableLog=false;
        calcModel.setMode("unknown");
        btn[0][0] = calcModel.translInputBtn11(); btn[0][1] = calcModel.translInputBtn12(); btn[0][2] = calcModel.translInputBtn13();
        btn[0][3] = calcModel.translInputBtn14(); btn[0][4] = calcModel.translInputBtn15(); btn[0][5] = calcModel.translInputBtn16();
        btn[1][0] = calcModel.translInputBtn21(); btn[1][1] = calcModel.translInputBtn22(); btn[1][2] = calcModel.translInputBtn23();
        btn[1][3] = calcModel.translInputBtn24(); btn[1][4] = calcModel.translInputBtn25(); btn[1][5] = calcModel.translInputBtn26();
        for(int i=0; i<12; i++){assertEquals("",btn[i/6][i%6]);}
    }

    @Test
    public void testSnEingabeAusgabeOps(){
        //toogleScientificNotation,eingabeAddText,ausgabeSetText,

        assertFalse(calcModel.isScientificNotation());
        calcModel.toogleScientificNotation(); assertTrue(calcModel.isScientificNotation());
        calcModel.toogleScientificNotation(); assertFalse(calcModel.isScientificNotation());

        calcModel.ausgabeSetText(""); assertTrue(calcModel.getOutputString().isEmpty());
        calcModel.ausgabeSetText("1+1"); assertTrue(calcModel.getOutputString().equals("1+1"));
        calcModel.eingabeAddText("2+2",0); assertTrue(calcModel.getIText().equals("2+2"));
    }

    @Test
    public void testMemoryOps(){
        //getMemory(),getMemory(int),setMemory(int[]),setMemory(int,int)
        assertArrayEquals(calcModel.getMemory(),new String[6]);
        String[] memTest = new String[]{"1","2","3","4","5","6"}; calcModel.setMemory(memTest);
        assertArrayEquals(calcModel.getMemory(),memTest);
        calcModel.setMemory(new String[5]); assertArrayEquals(calcModel.getMemory(),memTest); //wrong arraysize
        memTest[2] = "11"; calcModel.setMemory("11",2); assertArrayEquals(calcModel.getMemory(),memTest); calcModel.setMemory("11",-2);
        for(int i=0;i<6;i++){assertEquals(calcModel.getMemory(i),memTest[i]);}
    }

    @Test
    public void testGettersAndSetters(){
        //getMode(),setMode(String),getLanguage(),setLanguage(String),isScientificNotation(),setOutputString(String),setIText()
        init();
        assertEquals("basic",calcModel.getMode()); calcModel.setMode("basic"); assertEquals("basic",calcModel.getMode());
        assertEquals("",calcModel.getLanguage()); calcModel.setLanguage("english"); assertEquals("english",calcModel.getLanguage());
        assertEquals(false,calcModel.isScientificNotation()); calcModel.toogleScientificNotation(); assertEquals(true,calcModel.isScientificNotation());
        assertEquals("",calcModel.getOutputString()); calcModel.setOutputString("2"); assertEquals("2",calcModel.getOutputString());
        assertEquals("",calcModel.getIText()); calcModel.setIText("1+1");
            assertEquals("1+1",calcModel.getIText());
            assertEquals("1+1",calcModel.inputStringGetDisplayableString());
            assertEquals("2",calcModel.inputStringGetResult());
            assertEquals("2",calcModel.inputStringNormalToScientific());
            assertEquals("2",calcModel.inputStringScientificToNormal());

    }

}
