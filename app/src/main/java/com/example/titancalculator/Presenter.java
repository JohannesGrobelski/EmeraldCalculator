package com.example.titancalculator;

import android.content.Context;

import com.example.titancalculator.helper.Math_String.StringUtils;

import java.util.Arrays;

import static com.example.titancalculator.helper.Math_String.StringUtils.deleteSpan;

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

    /**
     * set Button Text for mode
     * @param mode
     */
    public void setMode(String mode){
        calcModel.setMode(mode);

        if (calcModel.getMode().equals("trigo")) {
            view.setBtnText(11,"SIN"); view.setBtnText(12,"COS"); view.setBtnText(13,"TAN"); view.setBtnText(14,"COT"); view.setBtnText(15,"SEC"); view.setBtnText(16,"CSC");
            view.setBtnText(21,"ASIN"); view.setBtnText(22,"ACOS"); view.setBtnText(23,"ATAN"); view.setBtnText(24,"ACOT"); view.setBtnText(25,"ASEC"); view.setBtnText(26,"ACSC");
        }
        if (calcModel.getMode().equals("hyper")) {
            view.setBtnText(11,"SINH"); view.setBtnText(12,"COSH"); view.setBtnText(13,"TANH"); view.setBtnText(14,"ASINH"); view.setBtnText(15,"ACOSH"); view.setBtnText(16,"ATANH");
            view.setBtnText(21,">DEG"); view.setBtnText(22,">RAD"); view.setBtnText(23,""); view.setBtnText(24,"");  view.setBtnText(25,"");  view.setBtnText(26,"");
        } else if (calcModel.getMode().equals("statistic")) {
            view.setBtnText(11,"ZN"); view.setBtnText(12,"ZB"); view.setBtnText(13,"NCR"); view.setBtnText(14,"NPR"); view.setBtnText(15,"MEAN"); view.setBtnText(16,"VAR");
            view.setBtnText(21,"S"); view.setBtnText(22,""); view.setBtnText(23,""); view.setBtnText(24,""); view.setBtnText(25,""); view.setBtnText(26,"");
        } else if (calcModel.getMode().equals("logic")) {
            view.setBtnText(11,"AND"); view.setBtnText(12,"OR"); view.setBtnText(13,"XOR"); view.setBtnText(14,"NOT");
            view.setBtnText(15,""); view.setBtnText(16,""); view.setBtnText(21,""); view.setBtnText(22,""); //view.setBtnText(15,">BIN"); view.setBtnText(16,">OCT"); view.setBtnText(21,">DEC"); view.setBtnText(22,">HEX");
            view.setBtnText(23,""); view.setBtnText(24,""); view.setBtnText(25,""); view.setBtnText(26,"");
        } else if (calcModel.getMode().equals("memory")) {
            view.setBtnText(11,"M1"); view.setBtnText(12,"M2"); view.setBtnText(13,"M3"); view.setBtnText(14,"M4"); view.setBtnText(15,"M5"); view.setBtnText(16,"M6");
            view.setBtnText(21,">M1"); view.setBtnText(22,">M2"); view.setBtnText(23,">M3"); view.setBtnText(24,">M4"); view.setBtnText(25,">M5"); view.setBtnText(26,">M6");
        } else if (calcModel.getMode().equals("basic")) {
            view.setBtnText(11,"π"); view.setBtnText(12,"e"); view.setBtnText(13,"^"); view.setBtnText(14,"LOG"); view.setBtnText(15,"LN"); view.setBtnText(16,"LB");
            view.setBtnText(21,"³√"); view.setBtnText(22,"√"); view.setBtnText(23,"x³"); view.setBtnText(24,"x²"); view.setBtnText(25,"10^x"); view.setBtnText(26,"!");
        } else if (calcModel.getMode().equals("basic2")) {
            view.setBtnText(11,"PFZ");
            if (calcModel.getLanguage().equals("german") || calcModel.getLanguage().equals("deutsch")) {view.setBtnText(12,"GGT");}
            else {view.setBtnText(12,"GCD");}
            if (calcModel.equals("german") || calcModel.equals("deutsch")) {view.setBtnText(13,"KGV");}
            else {view.setBtnText(13,"LCM");}
            view.setBtnText(14,"∑"); view.setBtnText(15,"∏"); view.setBtnText(16,"");
            view.setBtnText(21,">%"); view.setBtnText(22,">A/B");
            view.setBtnText(23,">x\u207B\u00B9");
            view.setBtnText(24,">+/-"); view.setBtnText(25,"MIN"); view.setBtnText(26,"MAX");
        }
    }
    public String getMode(){return calcModel.getMode();}

    /**
     * replace selected text with text
     * @param input
     */
    public void replaceSelection(String input) {
        if (input == null || input.isEmpty()) return;
        int selStart = view.getSelectionStartInput();
        int selEnd =  view.getSelectionEndInput();
        if (view.hasFocusInput()) {
            if (selStart >= 0 && selEnd >= 0 && selStart <= selEnd && selStart <= view.getInputText().length() && selEnd <= view.getInputText().length()) {
                String etE_text = view.getInputText();
                etE_text = StringUtils.replace(etE_text, input, selStart, selEnd);
                view.setInputText(etE_text);
            } else {
                view.setInputText(input);
            }
            view.setSelectionInput(selEnd);
            setInputText(view.getInputText());
        } else {
            addInputText(input);
        }
    }

    public String getSelection(){return view.getSelection();}
    public void setInputText(String text){calcModel.setInputText(text);}

    /**
     * add text to et_input (delete selection, add text)
     * first update model, set et_input to inputstring (model)
     * @param text
     */
    public void addInputText(String text){
        if(view.getSelectionEndInput() != view.getSelectionStartInput()){
            String delete = deleteSpan(view.getInputText(),view.getSelectionStartInput(),view.getSelectionEndInput());
            calcModel.setInputText(delete);
        }
        int selection = view.getSelectionStartInput();
        calcModel.addInputText(text,selection);
        view.setInputText(calcModel.getInputText());
        if(selection >= 0)view.setSelectionInput(selection+text.length());
    }

    public void inputButtonLongClick(String identifier){
        if(identifier.equals("="))calcModel.toogleScientificNotation();
    }

    /**
     * propagates input from view to calcmodel and updates views output
     * @param identifier
     * @return
     */
    public String inputButton(String identifier){
        //special cases
        if(identifier.equals("L") || identifier.equals("R")){inputSelectionMove(identifier);return identifier;}
        else if(identifier.equals("⌫")){inputClearOne(); return "⌫";}
        else if(identifier.equals("⌧")){inputClearAll(); return "⌧";}
        else if(identifier.equals("=")){inputEqual(); return "=";}

        String ret = "";
        String[] otherIdentifiers = new String[]{".",",","ANS","(",")","+","-","*","/"};

        if(identifier.matches("[1-2][1-6]")){
            int nr = Integer.valueOf(identifier);
            switch (nr){
                case 11: {
                    if(calcModel.getMode().equals("memory")){replaceSelection(calcModel.getMemory(0)); break;}
                    ret = calcModel.translateInputButton(11);
                    if(!ret.contains(">")) addInputText(calcModel.translateInputButton(11)); break;}
                case 12: {
                    if(calcModel.getMode().equals("memory")){replaceSelection(calcModel.getMemory(1)); break;}
                    ret = calcModel.translateInputButton(12);
                    if(!ret.contains(">")) addInputText(calcModel.translateInputButton(12)); break;}
                case 13: {
                    if(calcModel.getMode().equals("memory")){replaceSelection(calcModel.getMemory(2)); break;}
                    ret = calcModel.translateInputButton(13);
                    if(!ret.contains(">")) addInputText(calcModel.translateInputButton(13)); break;}
                case 14: {
                    if(calcModel.getMode().equals("memory")){replaceSelection(calcModel.getMemory(3)); break;}
                    ret = calcModel.translateInputButton(14);
                    if(!ret.contains(">")) addInputText(calcModel.translateInputButton(14)); break;}
                case 15: {
                    if(calcModel.getMode().equals("memory")){replaceSelection(calcModel.getMemory(4)); break;}
                    ret = calcModel.translateInputButton(15);
                    if(!ret.contains(">")) addInputText(calcModel.translateInputButton(15)); break;}
                case 16: {
                    if(calcModel.getMode().equals("memory")){replaceSelection(calcModel.getMemory(5)); break;}
                    ret = calcModel.translateInputButton(16);
                    if(!ret.contains(">")) addInputText(calcModel.translateInputButton(16)); break;}
                case 21: {
                    if(calcModel.getMode().equals("memory")){calcModel.setMemory(getSelection(),0); /*calcModel.saveMemory(calcModel.getMemory));*/}
                    ret = calcModel.translateInputButton(21);
                    if(!ret.contains(">")) addInputText(calcModel.translateInputButton(21)); break;}
                case 22: {
                    if(calcModel.getMode().equals("memory")){calcModel.setMemory(getSelection(),1); /*calcModel.saveMemory(calcModel.getMemory));*/}
                    ret = calcModel.translateInputButton(22);
                    if(!ret.contains(">")) addInputText(calcModel.translateInputButton(22)); break;}
                case 23: {
                    if(calcModel.getMode().equals("memory")){calcModel.setMemory(getSelection(),2); /*calcModel.saveMemory(calcModel.getMemory));*/}
                    ret = calcModel.translateInputButton(23);
                    if(!ret.contains(">")) addInputText(calcModel.translateInputButton(23)); break;}
                case 24: {
                    if(calcModel.getMode().equals("memory")){calcModel.setMemory(getSelection(),3); /*calcModel.saveMemory(calcModel.getMemory));*/}
                    ret = calcModel.translateInputButton(24);
                    if(!ret.contains(">")) addInputText(calcModel.translateInputButton(24)); break;}
                case 25: {
                    if(calcModel.getMode().equals("memory")){calcModel.setMemory(getSelection(),4); /*calcModel.saveMemory(calcModel.getMemory));*/}
                    ret = calcModel.translateInputButton(25);
                    if(!ret.contains(">")) addInputText(calcModel.translateInputButton(25)); break;}
                case 26: {
                    if(calcModel.getMode().equals("memory")){calcModel.setMemory(getSelection(),5); /*calcModel.saveMemory(calcModel.getMemory));*/}
                    ret = calcModel.translateInputButton(26);
                    if(!ret.contains(">")) addInputText(calcModel.translateInputButton(26)); break;}
            }
        }  else if(Arrays.asList(otherIdentifiers).contains(identifier) || identifier.matches("[0-9]")) {
            addInputText(identifier);
            ret =  identifier;
        }
        if(!ret.isEmpty() && ret.substring(0,1).equals(">")){ //TODO: schlechtes Signal das Output da ist, besser?: CalcModel benachrichtigt Presenter direkt?
            view.setOutputText(calcModel.getOutputString());
            return "";
        }
        return ret;
    }



    /**
     * moves the selection input one by left/right
     * @param input
     */
    private void inputSelectionMove(String input){
        if(input.equals("L"))view.setSelectionInput(Math.max(0, view.getSelectionStartInput() - 1));
        else if(input.equals("R"))view.setSelectionInput(Math.min(view.getInputText().length(), view.getSelectionStartInput() + 1));
    }

    /**
     * deletes the selection or the character left of the selection
     */
    private void inputClearOne(){
        //wenn cursor in mitte klappt es noch nicht ganz
        String text = view.getInputText(); int start = view.getSelectionStartInput(); int end = view.getSelectionEndInput();
        if(start != -1){
            if(end == start){
                text = text.substring(0,Math.max(0,start - 1)) + text.substring(start);
            } else {
                text = deleteSpan(text,start,end);
            }
        } else { text = text.substring(0,Math.max(0,text.length()-1));}
        calcModel.setInputText(text);
        int selection = Math.max(0,start- 1);
        view.setInputText(calcModel.getInputText());
        if(selection >= 0)view.setSelectionInput(selection);


        int pos = view.getSelectionStartInput();
        //altered
        if(pos != -1 && pos > 0) view.setInputText(view.getInputText().substring(0,pos-1));
        else {view.setInputText(view.getInputText().substring(0,Math.max(0,view.getInputText().length()-2)));}
        view.clearFocusInput();
        //original//TODO: test and delete
        //setInputText("");
        //eT_input.setSelection(Math.max(0, pos - 1));
    }

    /**
     * deletes text of et_input and et_output
     */
    private void inputClearAll(){
        setInputText("");
        view.setInputText(""); view.clearFocusInput();
        view.setOutputText(""); view.clearFocusOutput();
    }

    /**
     * calculates the String in et_input
     */
    private void inputEqual(){
        if (!view.getInputText().equals(calcModel.getInputText())) {
            setInputText(view.getInputText());
        }
        String answer = calcModel.getResult();
        calcModel.setOutputString(answer);
        view.setOutputText(calcModel.getOutputString());
    }

    public interface View{
        void setBtnText(int index, String text);
        String getBtnText(int i);

        int getSelectionStartInput();
        int getSelectionEndInput();
        int getSelectionStartOutput();
        int getSelectionEndOutput();
        String getSelection();

        void setSelectionInput(int selectionInput);
        void setSelectionInput(int selectionInputStart, int selectionInputEnd);
        void setSelectionOutput(int selectionOutput);
        void setSelectionOutput(int selectionOutputStart, int selectionOutputEnd);

        void setInputText(String text);
        String getInputText();
        void setOutputText(String text);

        void clearFocusInput();
        void clearFocusOutput();
        boolean hasFocusInput();
        boolean hasFocusOutput();
    }
}
