package com.example.titancalculator;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import static com.example.titancalculator.geplanteFeatures.CalcActivity_science.getBase;

/** The Presenter is the mediator between Model and View.
  * It retrieves data from Model and returns it formatted to the View.
 *  It also decides what happens when you interact with the View.
  * Generally there is a one to one mapping between View and Presenter, with the possibility to use multiple Presenters for complex Views.
  */
public class Presenter {
    CalcModel calcModel;
    View view;

    public Presenter(View view, Context context){
        this.view = view;
        calcModel = new CalcModel(context);
    }


    public void setMode(String mode){calcModel.setMode(mode);}
    public String getMode(){return calcModel.getMode();}
    public void resetMemory(){calcModel.setMemory(new String[6]);}
    public void toogleScientificNotation(){calcModel.toogleScientificNotation();}
    public void setNavI(String text){calcModel.setIText(text);}


    public void eingabeAddText(String text){
        //wenn cursor in mitte klappt es noch nicht ganz
        if(view.getSelectionEndEingabe() != view.getSelectionStartEingabe()){text = deleteSpan(text,view.getSelectionEndEingabe(),view.getSelectionStartEingabe());}
        calcModel.eingabeAddText(text,view.getSelectionStartEingabe());
        int selection = view.getSelectionEndEingabe() + 1;
        view.eingabeSetText(calcModel.getIText());
        if(selection >= 0)view.setSelectionEingabe(selection);
    }

    public String inputBtn(String identifier){
        String ret = "";
        String[] otherIdentifiers = new String[]{".",",","ANS","(",")","+","-","*","/"};

        if(identifier.matches("[1-2][1-6]")){
            int nr = Integer.valueOf(identifier);
            switch (nr){
                case 11: {
                    if(calcModel.getMode().equals("MEMORY")){calcModel.setMemory(getSelection(),0); calcModel.saveMemory(calcModel.getMemory()); break;}
                    ret = calcModel.translInputBtn11(); if(!ret.contains(">"))eingabeAddText(calcModel.translInputBtn11()); break;}
                case 12: {
                    if(calcModel.getMode().equals("MEMORY")){calcModel.setMemory(getSelection(),1); calcModel.saveMemory(calcModel.getMemory()); break;}
                    ret = calcModel.translInputBtn12(); if(!ret.contains(">"))eingabeAddText(calcModel.translInputBtn12()); break;}
                case 13: {
                    if(calcModel.getMode().equals("MEMORY")){calcModel.setMemory(getSelection(),2); calcModel.saveMemory(calcModel.getMemory()); break;}
                    ret = calcModel.translInputBtn13(); if(!ret.contains(">"))eingabeAddText(calcModel.translInputBtn13()); break;}
                case 14: {
                    if(calcModel.getMode().equals("MEMORY")){calcModel.setMemory(getSelection(),3); calcModel.saveMemory(calcModel.getMemory()); break;}
                    ret = calcModel.translInputBtn14(); if(!ret.contains(">"))eingabeAddText(calcModel.translInputBtn14()); break;}
                case 15: {
                    if(calcModel.getMode().equals("MEMORY")){calcModel.setMemory(getSelection(),4); calcModel.saveMemory(calcModel.getMemory()); break;}
                    ret = calcModel.translInputBtn15(); if(!ret.contains(">"))eingabeAddText(calcModel.translInputBtn15()); break;}
                case 16: {
                    if(calcModel.getMode().equals("MEMORY")){calcModel.setMemory(getSelection(),5); calcModel.saveMemory(calcModel.getMemory()); break;}
                    ret = calcModel.translInputBtn16(); if(!ret.contains(">"))eingabeAddText(calcModel.translInputBtn16()); break;}
                case 21: {
                    if(calcModel.getMode().equals("MEMORY")){replaceSelection(calcModel.getMemory(0)); eingabeAddText(calcModel.translInputBtn21()); break;}
                    ret = calcModel.translInputBtn21(); if(!ret.contains(">"))eingabeAddText(calcModel.translInputBtn21()); break;}
                case 22: {
                    if(calcModel.getMode().equals("MEMORY")){replaceSelection(calcModel.getMemory(1)); eingabeAddText(calcModel.translInputBtn22()); break;}
                    ret = calcModel.translInputBtn22(); if(!ret.contains(">"))eingabeAddText(calcModel.translInputBtn22()); break;}
                case 23: {
                    if(calcModel.getMode().equals("MEMORY")){replaceSelection(calcModel.getMemory(2)); eingabeAddText(calcModel.translInputBtn23()); break;}
                    ret = calcModel.translInputBtn23(); if(!ret.contains(">"))eingabeAddText(calcModel.translInputBtn23()); break;}
                case 24: {
                    if(calcModel.getMode().equals("MEMORY")){replaceSelection(calcModel.getMemory(3)); eingabeAddText(calcModel.translInputBtn24()); break;}
                    ret = calcModel.translInputBtn24(); if(!ret.contains(">"))eingabeAddText(calcModel.translInputBtn24()); break;}
                case 25: {
                    if(calcModel.getMode().equals("MEMORY")){replaceSelection(calcModel.getMemory(4)); eingabeAddText(calcModel.translInputBtn25()); break;}
                    ret = calcModel.translInputBtn25(); if(!ret.contains(">"))eingabeAddText(calcModel.translInputBtn25()); break;}
                case 26: {
                    if(calcModel.getMode().equals("MEMORY")){replaceSelection(calcModel.getMemory(5)); eingabeAddText(calcModel.translInputBtn26()); break;}
                    ret = calcModel.translInputBtn26(); if(!ret.contains(">"))eingabeAddText(calcModel.translInputBtn26()); break;}
            }
        }  else if(Arrays.asList(otherIdentifiers).contains(identifier) || identifier.matches("[0-9]")) {
            eingabeAddText(identifier);
            ret =  identifier;
        }

        Log.d("pfz2",ret);
        Log.d("pfz2",ret.substring(0,1));
        Log.d("pfz3",calcModel.getOutputString());

        if(ret.substring(0,1).equals(">")){ //TODO: schlechtes Signal das Output da ist, besser?: CalcModel benachrichtig Presenter direkt?
            Log.d("pfz3",calcModel.getOutputString());
            view.ausgabeSetText(calcModel.getOutputString());
            return "";
        }

        return "";
    }



    public void inputClearOne(){
        //wenn cursor in mitte klappt es noch nicht ganz
        String text = view.eingabeGetText(); int start = view.getSelectionStartEingabe(); int end = view.getSelectionEndEingabe();
        if(start != -1){
            if(end == start){
                text = text.substring(0,Math.max(0,start - 1)) + text.substring(start);
            } else {
                text = deleteSpan(text,start,end);
            }
        }
        else text = text.substring(0,Math.max(0,text.length()-1));
        calcModel.setIText(text);
        int selection = Math.max(0,start- 1);
        view.eingabeSetText(calcModel.getIText());
        if(selection >= 0)view.setSelectionEingabe(selection);

        /*TODO solve_inst_pref
        if (!calcModel.noImmidiateOps.contains(calcModel.inputStringGetDisplayableString().trim())) {
            if (calcModel.solve_inst_pref) {
                String answer = calcModel.inputStringGetResult();
                if (!answer.equals("Math Error")) view.ausgabeSetText(answer);
            }
        }*/
    }

    public void inputClearAll(){
        view.eingabeClearAll();
        setNavI("");
    }

    public void inputEqual(){
        if (!view.eingabeGetText().equals(calcModel.inputStringGetDisplayableString())) {
            setNavI(view.eingabeGetText());
        }
        String answer;
        if(calcModel.isScientificNotation()){
            answer = calcModel.inputStringNormalToScientific();
        } else {
            answer = calcModel.inputStringScientificToNormal();
        }
        calcModel.setOutputString(answer);
        view.ausgabeSetText(calcModel.getOutputString());
    }



    public void assignModeFct() {
        if (calcModel.getMode().equals("trigo")) {
            view.setBtn11Text("SIN"); view.setBtn12Text("COS"); view.setBtn13Text("TAN"); view.setBtn14Text("COT"); view.setBtn15Text("ASIN"); view.setBtn16Text("ACOS");
            view.setBtn21Text("ATAN"); view.setBtn22Text("ACOT"); view.setBtn23Text(">DEG"); view.setBtn24Text(">RAD"); view.setBtn25Text(">Polar"); view.setBtn26Text(">Cart");
        }
        if (calcModel.getMode().equals("hyper")) {
            view.setBtn11Text("SINH"); view.setBtn12Text("COSH"); view.setBtn13Text("TANH"); view.setBtn14Text("ASINH"); view.setBtn15Text("ASINH"); view.setBtn16Text("ASINH");
            view.setBtn21Text("");  view.setBtn22Text("");  view.setBtn23Text("");  view.setBtn24Text("");  view.setBtn25Text("");  view.setBtn26Text("");
        } else if (calcModel.getMode().equals("statistic")) {
            view.setBtn11Text("ZN(N)"); view.setBtn12Text("ZB(X;Y)"); view.setBtn13Text("NCR"); view.setBtn14Text("NPR"); view.setBtn15Text("MEAN"); view.setBtn16Text("VAR");
            view.setBtn21Text("E"); view.setBtn22Text("S"); view.setBtn23Text(""); view.setBtn24Text(""); view.setBtn25Text(""); view.setBtn26Text("");
        } else if (calcModel.getMode().equals("logic")) {
            view.setBtn11Text("AND"); view.setBtn12Text("OR"); view.setBtn13Text("XOR"); view.setBtn14Text("NOT"); view.setBtn15Text(">BIN"); view.setBtn16Text(">OCT");
            view.setBtn21Text(">DEC"); view.setBtn22Text(">HEX"); view.setBtn23Text(""); view.setBtn24Text(""); view.setBtn25Text(""); view.setBtn26Text("");
        } else if (calcModel.getMode().equals("memory")) {
            view.setBtn11Text("M1"); view.setBtn12Text("M2"); view.setBtn13Text("M3"); view.setBtn14Text("M4"); view.setBtn15Text("M5"); view.setBtn16Text("M6");
            view.setBtn21Text(">M1"); view.setBtn22Text(">M2"); view.setBtn23Text(">M3"); view.setBtn24Text(">M4"); view.setBtn25Text(">M5"); view.setBtn26Text(">M6");
        } else if (calcModel.getMode().equals("basic")) {
            view.setBtn11Text("π"); view.setBtn12Text("e"); view.setBtn13Text("^"); view.setBtn14Text("LOG"); view.setBtn15Text("LN"); view.setBtn16Text("LB");
            view.setBtn21Text("³√"); view.setBtn22Text("√"); view.setBtn23Text("x³"); view.setBtn24Text("x²"); view.setBtn25Text("10^x"); view.setBtn26Text("!");
        } else if (calcModel.getMode().equals("basic2")) {
            //L1 normal: PI,E,->DEC,->BIN,->OCT
            view.setBtn11Text("PFZ");
            if (calcModel.getLanguage().equals("german") || calcModel.getLanguage().equals("deutsch")) {view.setBtn12Text("GGT");}
            else {view.setBtn12Text("GCD");}
            if (calcModel.equals("german") || calcModel.equals("deutsch")) {view.setBtn13Text("KGV");}
            else {view.setBtn13Text("LCM");}
            view.setBtn14Text("∑"); view.setBtn15Text("∏"); view.setBtn16Text("");
            //L3 normal: %,!,^,a/b,x^-1,+/-
            view.setBtn21Text(">%"); view.setBtn22Text("A/B");
            /*TODO pruefe ob funzt*/ view.setBtn23Text("x\u207B\u00B9");
            view.setBtn24Text("+/-"); view.setBtn25Text("MIN"); view.setBtn26Text("MAX");
        }
        if (calcModel.getMode().equals("PROGRAM") || calcModel.getMode().equals("PROGRAMM")) {
            view.setBtn11Text("P1"); view.setBtn12Text("P2"); view.setBtn13Text("P3"); view.setBtn14Text("P4"); view.setBtn15Text("P5"); view.setBtn16Text("P6");
            view.setBtn21Text(">P1"); view.setBtn22Text(">P1"); view.setBtn23Text(">P3"); view.setBtn24Text(">P4"); view.setBtn25Text(">P5"); view.setBtn26Text(">P6");
        }
    }

    public void replaceSelection(String input){view.replaceSelection(input);}

    public String getSelection(){return view.getSelection();}

    public String deleteSpan(String input, int start, int end){
        if(start < 0 || start > input.length() - 1 || end < 0 || end > input.length() || end <= start)return input;
        else{
            return (input = input.substring(0,start) + input.substring(Math.min(end,input.length() - 1),Math.max(end,input.length()-1)));
        }
    }

    public interface View{
        void setBtn11Text(String text);
        void setBtn12Text(String text);
        void setBtn13Text(String text);
        void setBtn14Text(String text);
        void setBtn15Text(String text);
        void setBtn16Text(String text);

        void setBtn21Text(String text);
        void setBtn22Text(String text);
        void setBtn23Text(String text);
        void setBtn24Text(String text);
        void setBtn25Text(String text);
        void setBtn26Text(String text);


        int getSelectionStartEingabe();
        int getSelectionEndEingabe();
        void setSelectionEingabe(int selectionEingabe);

        void eingabeAddText(String i);
        void eingabeClearOne();
        void eingabeClearAll();
        void eingabeSetText(String text);
        String eingabeGetText();
        void ausgabeSetText(String text);
        
        void replaceSelection(String input);
        String getSelection();
    }
}
