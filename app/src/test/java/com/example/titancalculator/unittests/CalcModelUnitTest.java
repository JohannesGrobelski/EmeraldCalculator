package com.example.titancalculator.unittests;

import com.example.titancalculator.model.CalcModel;

import org.junit.Before;
import org.junit.Test;

import static com.example.titancalculator.model.CalcModel.modes;
import static com.example.titancalculator.model.CalcModel.modesModesFunctionality;
import static com.example.titancalculator.model.CalcModel.modesModesText;
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
            calcModel.setMode(mode);
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

        calcModel.enableLog=false;
        calcModel.setMode(7);
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
            calcModel.setMode(mode);
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

        calcModel.enableLog=false;
        calcModel.setMode(7);
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
        String[] memTest = new String[]{"1","2","3","4","5","6"};
        calcModel.setMemory(memTest);
        assertArrayEquals(calcModel.getMemory(),memTest);//wrong arraysize
        memTest[2] = "11"; calcModel.setMemory("11",2); assertArrayEquals(calcModel.getMemory(),memTest); calcModel.setMemory("11",-2);
        for(int i=0;i<6;i++){assertEquals(calcModel.getMemory(i),memTest[i]);}
    }

    @Test
    public void testGettersAndSetters(){
        //getMode(),setMode(String),getLanguage(),setLanguage(String),isScientificNotation(),setOutputString(String),setInput()
        init();
        assertEquals(0,calcModel.getMode()); calcModel.setMode(0); assertEquals(0,calcModel.getMode());
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

        //setModes
        CalcModel.setModes(new String[]{"test"}); assertArrayEquals(new String[]{"test"},CalcModel.modes);

        //translatePrimeFactorization,
        CalcModel.translatePrimeFactorization("123"); assertEquals("123",CalcModel.modeAdvancedText[0]);
        CalcModel.translateGreatestCommonDenominator("123"); assertEquals("123",CalcModel.modeAdvancedText[1]);
        CalcModel.translateLeastCommonMultiply("123"); assertEquals("123",CalcModel.modeAdvancedText[2]);
        CalcModel.translateRandomNumber("123"); assertEquals("123",CalcModel.modeStatisticText[0]);
        assertEquals("123",CalcModel.modeStatisticText[1]);
    }

    @Test public void testModeSetter(){
        calcModel.setMode(0);
        //nextMode, previousMode
        for(int i=0;i<1000;i++){
            int currentMode = calcModel.getMode();
            calcModel.nextMode();
            assertEquals((currentMode+1)%modes.length,calcModel.getMode());
            assertTrue(calcModel.getMode()>=0 && calcModel.getMode()<=modes.length);
        }
        for(int i=0;i<1000;i++){
            int currentMode = calcModel.getMode();
            calcModel.previousMode();
            assertEquals(fixedModulo(currentMode-1,modes.length),calcModel.getMode());
            assertTrue(calcModel.getMode()>=0 && calcModel.getMode()<=modes.length);
        }
        assertEquals(0,calcModel.getMode());
    }

    private int fixedModulo(int a, int m){
        if(a>=0)return a%m;
        else return (a+m)%m;
    }

    @Test public void testGetInverseFunctionality(){
        for(int mode=0;mode<modes.length;mode++){
            for(int b=0;b<12;b++){
                int btnIndex = (((b/6)+1)*10) + (b%6)+1;
                String btnFun = calcModel.getFunctionButtonFunctionality(btnIndex);
                if(CalcModel.inverseFunction.containsKey(btnFun))assertEquals(CalcModel.inverseFunction.get(btnFun),calcModel.getInverseFunctionButtonFunctionality(btnIndex));
                else assertEquals("",calcModel.getInverseFunctionButtonFunctionality(btnIndex));
            }
        }

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

    @Test public void testGetCalculableString(){
        String input = "TANH>A/B>PFZCOSH>M6COSHπCOT>M1∏(,)>M4MEAN()CSCHGCD(,)M5>M4√(VAR())>PFZCOTCOTHCSC^MIN(,)∑(,)MEAN()SECHTAN!πCOSM3SINHXOR(,)LOGM4COSπ∏(,)MEAN()>M2√eCOSC>M1>M1COSH>M5M3>+/-MEAN()NOT()AND(,)>M2SECHVAR()COTHM5M1>%>PFZSINHM4∑(,)TANTANH>M2";

        System.out.println(calcModel.getCalcuableString(input));


        /*
        List<String> list = new ArrayList<>();
        for(int i = 0; i < calcModel.modesModesFunctionality.length; i++) {
            list.addAll(Arrays.asList(modesModesFunctionality[i])); // java.util.Arrays
        }
        String[] inputTerms = new String[list.size()];
        inputTerms = list.toArray(inputTerms);


        int testIterations = 10000;
        for(int it=0;it<testIterations;it++){
            input = "";
            for(int l=0; l<100; l++){
                int randomIndex = ((int) (Math.random()*inputTerms.length));
                input += inputTerms[randomIndex];
            }
            try {
                System.out.println(input);
                calcModel.getCalcuableString(input);
            } catch (Exception e){
            }
            if(it>0 && it%1000==0)System.out.println(it);
        }


         */



    }
}
