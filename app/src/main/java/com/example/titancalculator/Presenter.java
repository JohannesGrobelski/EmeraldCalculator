package com.example.titancalculator;

import android.content.Context;

import static com.example.titancalculator.geplanteFeatures.CalcActivity_science.getBase;

/** The Presenter is the mediator between Model and View.
  * It retrieves data from Model and returns it formatted to the View.
 *  It also decides what happens when you interact with the View.
  * Generally there is a one to one mapping between View and Presenter, with the possibility to use multiple Presenters for complex Views.
  */
public class Presenter {
    CalcModel calcModel = new CalcModel();
    View view;

    public Presenter(View view){
        this.view = view;
    }

    public void setMode(String mode){calcModel.setMode(mode);}
    public String getMode(){return calcModel.getMode();}

    public void resetMemory(){calcModel.setMEMORY(new String[6]);}
    public void toogleScientificNotation(){calcModel.toogleScientificNotation();}

    public void setNavI(String text){calcModel.setIText(text);}

    public void eingabeAddText(String text){
        wenn cursor in mitte klappt es noch nicht ganz
        calcModel.eingabeAddText(text,view.getSelectionStartEingabe());
        view.eingabeSetText(calcModel.getIText());
    }

    public String inputBtn11(){
        if(calcModel.englishMode.equals("MEMORY")){calcModel.setMEMORY(getSelection(),0); view.saveMemory(calcModel.getMEMORY());}
        eingabeAddText(calcModel.inputBtn11());
        return calcModel.inputBtn11();
    }
    public String inputBtn12(){
        if(calcModel.englishMode.equals("MEMORY")){calcModel.setMEMORY(getSelection(),1); view.saveMemory(calcModel.getMEMORY());}
        eingabeAddText(calcModel.inputBtn12());
        return calcModel.inputBtn12();
    }
    public String inputBtn13(){
        if(calcModel.englishMode.equals("MEMORY")){calcModel.setMEMORY(getSelection(),2); view.saveMemory(calcModel.getMEMORY());}
        eingabeAddText(calcModel.inputBtn13());
        return calcModel.inputBtn13();
    }
    public String inputBtn14(){
        if(calcModel.englishMode.equals("MEMORY")){calcModel.setMEMORY(getSelection(),3); view.saveMemory(calcModel.getMEMORY());}
        eingabeAddText(calcModel.inputBtn14());
        return calcModel.inputBtn14();
    }
    public String inputBtn15(){
        if(calcModel.englishMode.equals("MEMORY")){calcModel.setMEMORY(getSelection(),4); view.saveMemory(calcModel.getMEMORY());}
        eingabeAddText(calcModel.inputBtn15());
        return calcModel.inputBtn15();
    }
    public String inputBtn16(){
        if(calcModel.englishMode.equals("MEMORY")){calcModel.setMEMORY(getSelection(),5); view.saveMemory(calcModel.getMEMORY());}
        eingabeAddText(calcModel.inputBtn16());
        return calcModel.inputBtn16();
    }
    public String inputBtn21(){
        if(calcModel.englishMode.equals("MEMORY")){replaceSelection(calcModel.getMEMORY(0));}
        eingabeAddText(calcModel.inputBtn21());
        return calcModel.inputBtn21();
    }
    public String inputBtn22(){
        if(calcModel.englishMode.equals("MEMORY")){replaceSelection(calcModel.getMEMORY(1));}
        eingabeAddText(calcModel.inputBtn22());
        return calcModel.inputBtn22();
    }
    public String inputBtn23(){
        if(calcModel.englishMode.equals("MEMORY")){replaceSelection(calcModel.getMEMORY(2));}
        eingabeAddText(calcModel.inputBtn23());
        return calcModel.inputBtn23();
    }
    public String inputBtn24(){
        if(calcModel.englishMode.equals("MEMORY")){replaceSelection(calcModel.getMEMORY(3));}
        eingabeAddText(calcModel.inputBtn24());
        return calcModel.inputBtn24();
    }
    public String inputBtn25(){
        if(calcModel.englishMode.equals("MEMORY")){replaceSelection(calcModel.getMEMORY(4));}
        eingabeAddText(calcModel.inputBtn25());
        return calcModel.inputBtn25();
    }
    public String inputBtn26(){
        if(calcModel.englishMode.equals("MEMORY")){replaceSelection(calcModel.getMEMORY(5));}
        eingabeAddText(calcModel.inputBtn26());
        return calcModel.inputBtn26();
    }

    public void inputClear(){
        view.eingabeClear();
        view.ausgabeSetText("");

        if (!calcModel.noImmidiateOps.contains(calcModel.inputStringGetDisplayableString().trim())) {
            if (calcModel.solve_inst_pref) {
                String answer = calcModel.inputStringGetResult();
                if (!answer.equals("Math Error")) view.ausgabeSetText(answer);
            }
        }
    }

    public void inputEqual(){
        if (!view.eingabeGetText().equals(calcModel.inputStringGetDisplayableString())) {
            setNavI(view.eingabeGetText());
        }
        String answer = calcModel.inputStringGetResult();
        if(calcModel.scientificNotation){
            answer = calcModel.inputStringNormalToScientific();
        } else {
            answer = calcModel.inputStringScientificToNormal();
        }
        view.ausgabeSetText(answer);
    }




    public void assignModeFct() {
        if (calcModel.englishMode.equals("TRIGO")) {
            view.setBtn11Text("SIN"); view.setBtn12Text("COS"); view.setBtn13Text("TAN"); view.setBtn14Text("COT"); view.setBtn15Text("ASIN"); view.setBtn16Text("ACOS");
            view.setBtn21Text("ATAN"); view.setBtn22Text("ACOT"); view.setBtn23Text(">DEG"); view.setBtn24Text(">RAD"); view.setBtn25Text(">Polar"); view.setBtn26Text(">Cart");
        }
        if (calcModel.englishMode.equals("HYPER")) {
            view.setBtn11Text("SINH"); view.setBtn12Text("COSH"); view.setBtn13Text("TANH"); view.setBtn14Text("ASINH"); view.setBtn15Text("ASINH"); view.setBtn16Text("ASINH");
            view.setBtn21Text("");  view.setBtn22Text("");  view.setBtn23Text("");  view.setBtn24Text("");  view.setBtn25Text("");  view.setBtn26Text("");
        } else if (calcModel.englishMode.equals("STATS")) {
            view.setBtn11Text("ZN(N)"); view.setBtn12Text("ZB(X;Y)"); view.setBtn13Text("NCR"); view.setBtn14Text("NPR"); view.setBtn15Text("MEAN"); view.setBtn16Text("VAR");
            view.setBtn21Text("E"); view.setBtn22Text("S"); view.setBtn23Text(""); view.setBtn24Text(""); view.setBtn25Text(""); view.setBtn26Text("");
        } else if (calcModel.englishMode.equals("LOGIC")) {
            view.setBtn11Text("AND"); view.setBtn12Text("OR"); view.setBtn13Text("XOR"); view.setBtn14Text("NOT"); view.setBtn15Text(">BIN"); view.setBtn16Text(">OCT");
            view.setBtn21Text(">DEC"); view.setBtn22Text(">HEX"); view.setBtn23Text(""); view.setBtn24Text(""); view.setBtn25Text(""); view.setBtn26Text("");
        } else if (calcModel.englishMode.equals("MEMORY")) {
            view.setBtn11Text("M1"); view.setBtn12Text("M2"); view.setBtn13Text("M3"); view.setBtn14Text("M4"); view.setBtn15Text("M5"); view.setBtn16Text("M6");
            view.setBtn21Text(">M1"); view.setBtn22Text(">M2"); view.setBtn23Text(">M3"); view.setBtn24Text(">M4"); view.setBtn25Text(">M5"); view.setBtn26Text(">M6");
        } else if (calcModel.englishMode.equals("BASIC")) {
            view.setBtn11Text("π"); view.setBtn12Text("e"); view.setBtn13Text("^"); view.setBtn14Text("LOG"); view.setBtn15Text("LN"); view.setBtn16Text("LB");
            view.setBtn21Text("³√"); view.setBtn22Text("√"); view.setBtn23Text("x³"); view.setBtn24Text("x²"); view.setBtn25Text("10^x"); view.setBtn26Text("!");
        } else if (calcModel.englishMode.equals("BASIC2")) {
            //L1 normal: PI,E,->DEC,->BIN,->OCT
            view.setBtn11Text("PFZ");
            if (calcModel.language.equals("german") || calcModel.language.equals("deutsch")) {view.setBtn12Text("GGT");}
            else {view.setBtn12Text("GCD");}
            if (calcModel.equals("german") || calcModel.equals("deutsch")) {view.setBtn13Text("KGV");}
            else {view.setBtn13Text("LCM");}
            view.setBtn14Text("∑"); view.setBtn15Text("∏"); view.setBtn16Text("");
            //L3 normal: %,!,^,a/b,x^-1,+/-
            view.setBtn21Text(">%"); view.setBtn22Text("A/B");
            /*TODO pruefe ob funzt*/ view.setBtn23Text("x\\u207B\\u00B9");
            view.setBtn24Text("+/-"); view.setBtn25Text("MIN"); view.setBtn26Text("MAX");
        }
        if (calcModel.englishMode.equals("PROGRAM") || calcModel.englishMode.equals("PROGRAMM")) {
            view.setBtn11Text("P1"); view.setBtn12Text("P2"); view.setBtn13Text("P3"); view.setBtn14Text("P4"); view.setBtn15Text("P5"); view.setBtn16Text("P6");
            view.setBtn21Text(">P1"); view.setBtn22Text(">P1"); view.setBtn23Text(">P3"); view.setBtn24Text(">P4"); view.setBtn25Text(">P5"); view.setBtn26Text(">P6");
        }
    }

    public void replaceSelection(String input){view.replaceSelection(input);}


    public String getSelection(){return view.getSelection();}

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

        void eingabeAddText(String i);
        void eingabeClear();    
        void eingabeSetText(String text);
        String eingabeGetText();
        void ausgabeSetText(String text);
        
        
        void replaceSelection(String input);
        String getSelection();

        void saveMemory(String[] Memory);
        String[] loadMemory();
    }
}
