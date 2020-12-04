package com.example.titancalculator.unittests;

import android.content.Context;

import com.example.titancalculator.CalcModel;

import org.junit.Before;
import org.junit.Test;

import static com.example.titancalculator.CalcModel.modes;
import static com.example.titancalculator.CalcModel.modesModesFunctionality;
import static com.example.titancalculator.CalcModel.modesModesText;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class CalcModelUnitTest {

    CalcModel calcModel;
    private String[][] btn = new String[2][6];

    @Before
    public void init(){
        calcModel = new CalcModel();
    }

    @Test
    public void testGetFunctionButtonText(){
        for(int mode=0; mode<modes.length; mode++){
            //System.out.println("mode: "+modes[mode]);
            calcModel.setMode(modes[mode]);
            btn[0][0] = calcModel.getFunctionButtonText(11); btn[0][1] = calcModel.getFunctionButtonText(12); btn[0][2] = calcModel.getFunctionButtonText(13);
            btn[0][3] = calcModel.getFunctionButtonText(14); btn[0][4] = calcModel.getFunctionButtonText(15); btn[0][5] = calcModel.getFunctionButtonText(16);
            btn[1][0] = calcModel.getFunctionButtonText(21); btn[1][1] = calcModel.getFunctionButtonText(22); btn[1][2] = calcModel.getFunctionButtonText(23);
            btn[1][3] = calcModel.getFunctionButtonText(24); btn[1][4] = calcModel.getFunctionButtonText(25); btn[1][5] = calcModel.getFunctionButtonText(26);

            for(int i=0; i<12; i++){
                //System.out.println("mode: "+btn[i/6][i%6]+", transl:"+modesModes[mode][i]);
                assertEquals(modesModesText[mode][i],btn[i/6][i%6]);
            }
            //System.out.println("\n\n");
        }
        calcModel.setMode("basic2"); calcModel.setLanguage("german");
        assertEquals("GGT",calcModel.getFunctionButtonText(12));
        assertEquals("KGV",calcModel.getFunctionButtonText(13));

        calcModel.enableLog=false;
        calcModel.setMode("unknown");
        btn[0][0] = calcModel.getFunctionButtonText(11); btn[0][1] = calcModel.getFunctionButtonText(12); btn[0][2] = calcModel.getFunctionButtonText(13);
        btn[0][3] = calcModel.getFunctionButtonText(14); btn[0][4] = calcModel.getFunctionButtonText(15); btn[0][5] = calcModel.getFunctionButtonText(16);
        btn[1][0] = calcModel.getFunctionButtonText(21); btn[1][1] = calcModel.getFunctionButtonText(22); btn[1][2] = calcModel.getFunctionButtonText(23);
        btn[1][3] = calcModel.getFunctionButtonText(24); btn[1][4] = calcModel.getFunctionButtonText(25); btn[1][5] = calcModel.getFunctionButtonText(26);
        for(int i=0; i<12; i++){assertEquals("",btn[i/6][i%6]);}

    }

    @Test
    public void testGetFunctionButtonFunctionality(){
        for(int mode=0; mode<modes.length-1; mode++){
            //System.out.println("mode: "+modes[mode]);
            calcModel.setMode(modes[mode]);
            btn[0][0] = calcModel.getFunctionButtonFunctionality(11); btn[0][1] = calcModel.getFunctionButtonFunctionality(12); btn[0][2] = calcModel.getFunctionButtonFunctionality(13);
            btn[0][3] = calcModel.getFunctionButtonFunctionality(14); btn[0][4] = calcModel.getFunctionButtonFunctionality(15); btn[0][5] = calcModel.getFunctionButtonFunctionality(16);
            btn[1][0] = calcModel.getFunctionButtonFunctionality(21); btn[1][1] = calcModel.getFunctionButtonFunctionality(22); btn[1][2] = calcModel.getFunctionButtonFunctionality(23);
            btn[1][3] = calcModel.getFunctionButtonFunctionality(24); btn[1][4] = calcModel.getFunctionButtonFunctionality(25); btn[1][5] = calcModel.getFunctionButtonFunctionality(26);

            for(int i=0; i<12; i++){
                //System.out.println("mode: "+btn[i/6][i%6]+", transl:"+modesModes[mode][i]);
                assertEquals(modesModesFunctionality[mode][i],btn[i/6][i%6]);
            }
            //System.out.println("\n\n");
        }
        calcModel.setMode("basic2"); calcModel.setLanguage("german");
        assertEquals("GGT(,)",calcModel.getFunctionButtonFunctionality(12));
        assertEquals("KGV(,)",calcModel.getFunctionButtonFunctionality(13));

        calcModel.enableLog=false;
        calcModel.setMode("unknown");
        btn[0][0] = calcModel.getFunctionButtonFunctionality(11); btn[0][1] = calcModel.getFunctionButtonFunctionality(12); btn[0][2] = calcModel.getFunctionButtonFunctionality(13);
        btn[0][3] = calcModel.getFunctionButtonFunctionality(14); btn[0][4] = calcModel.getFunctionButtonFunctionality(15); btn[0][5] = calcModel.getFunctionButtonFunctionality(16);
        btn[1][0] = calcModel.getFunctionButtonFunctionality(21); btn[1][1] = calcModel.getFunctionButtonFunctionality(22); btn[1][2] = calcModel.getFunctionButtonFunctionality(23);
        btn[1][3] = calcModel.getFunctionButtonFunctionality(24); btn[1][4] = calcModel.getFunctionButtonFunctionality(25); btn[1][5] = calcModel.getFunctionButtonFunctionality(26);
        for(int i=0; i<12; i++){assertEquals("",btn[i/6][i%6]);}
    }

    @Test
    public void testSnEingabeAusgabeOps(){
        //toogleScientificNotation,addInputText,setOuputString,

        assertFalse(calcModel.isScientificNotation());
        calcModel.toogleScientificNotation(); assertTrue(calcModel.isScientificNotation());
        calcModel.toogleScientificNotation(); assertFalse(calcModel.isScientificNotation());

        calcModel.setOuputString(""); assertTrue(calcModel.getOutputString().isEmpty());
        calcModel.setOuputString("1+1"); assertTrue(calcModel.getOutputString().equals("1+1"));
        calcModel.addInputText("2+2",0); assertTrue(calcModel.getInputText().equals("2+2"));
    }

    @Test
    public void testMemoryOps(){
        calcModel.setMemory(new String[6]);
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
        //getMode(),setMode(String),getLanguage(),setLanguage(String),isScientificNotation(),setOutputString(String),setInput()
        init();
        assertEquals("basic",calcModel.getMode()); calcModel.setMode("basic"); assertEquals("basic",calcModel.getMode());
        assertEquals("",calcModel.getLanguage()); calcModel.setLanguage("english"); assertEquals("english",calcModel.getLanguage());
        assertEquals(false,calcModel.isScientificNotation()); calcModel.toogleScientificNotation(); assertEquals(true,calcModel.isScientificNotation());
        assertEquals("",calcModel.getOutputString()); calcModel.setOutputString("2"); assertEquals("2",calcModel.getOutputString());
        assertEquals("",calcModel.getInputText()); calcModel.setInputText("1+1");
            assertEquals("1+1",calcModel.getInputText());
            assertEquals("2",calcModel.getResult());
            assertEquals("2",calcModel.getResult());
            assertEquals("2",calcModel.getScientificResult());
    }


    @Test
    public void testScientific(){
        String[] inputs = {"1","5.5","10^10"};
        String[] outputsNormal = {"1","5.5","10000000000"};
        String[] outputsScientific = {"1","5.5","1E10"};
        for(int i=0;i<inputs.length;i++){
            calcModel.setInputText(inputs[i]);
            assertEquals(outputsNormal[i], calcModel.getResult());
            assertEquals(outputsScientific[i], calcModel.getScientificResult());
        }
    }

    @Test
    public void testANS(){
        calcModel.setInputText("1+1"); String ans = calcModel.getResult();
        assertEquals("2",ans);
        calcModel.setInputText("ANS*2"); ans = calcModel.getResult(); assertEquals("4",ans);
        calcModel.setInputText("ATANSIN8"); ans = calcModel.getResult(); assertEquals("7.9231380575",ans);

        calcModel.setInputText("ANS"); calcModel.getScientificResult(); assertEquals("7.9231380575",ans);
    }

    @Test
    public void testGetterSetter(){
        //setVarMode(), setMeanMode()
        String[] modes = new String[]{"Ari","Geo","Har"};
        for(String mode: modes){
            calcModel.setMeanMode(mode+"Mit"); assertEquals(mode+"Mit",calcModel.mean_mode); assertEquals(calcModel.mean_mode,calcModel.getMeanMode());
            calcModel.setVarMode(mode+"Var"); assertEquals(mode+"Var",calcModel.var_mode); assertEquals(calcModel.var_mode,calcModel.getVarMode());
        }
        calcModel.setMeanMode("unknown"); assertEquals("HarMit",calcModel.mean_mode); assertEquals(calcModel.mean_mode,calcModel.getMeanMode());
        calcModel.setVarMode("unknown"); assertEquals("HarVar",calcModel.var_mode); assertEquals(calcModel.var_mode,calcModel.getVarMode());
        //getInputText(), setContent(...)
        String content = "1+1"; calcModel.setInputText(content); assertEquals(content,calcModel.getInputText());
    }

    @Test public void testMeanVarModes(){
        //setVarMode(), setMeanMode()
        String[] modes = new String[]{"Ari","Geo","Har"};
        for(String mode: modes){
            calcModel.setMeanMode(mode+"Mit"); assertEquals(mode+"Mit", calcModel.getMeanMode());
            calcModel.setVarMode(mode+"Var"); assertEquals(mode+"Var", calcModel.getVarMode());
        }
        calcModel.setMeanMode("unknown"); assertEquals("HarMit", calcModel.getMeanMode());
        calcModel.setVarMode("unknown"); assertEquals("HarVar", calcModel.getVarMode());
        //toContent(), setContent(...)
        String content = "1+1"; calcModel.setInputText(content); assertEquals(content, calcModel.getInputText());
    }
}
