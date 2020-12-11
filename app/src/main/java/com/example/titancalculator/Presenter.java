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
    private CalcModel calcModel;
    private View view;

    public Presenter(){
        calcModel = new CalcModel();
    }

    public void attachView(View view){
        this.view = view;
    }

    public void detachView(){
        this.view = null;
    }


    public String inputButtonLong(String identifier){
        String ret = "";
        if(identifier.matches("[1-2][1-6]")){
            int nr = Integer.valueOf(identifier);
            ret = calcModel.getInverseFunctionButtonFunctionality(nr);
            System.out.println(identifier+" "+calcModel.getFunctionButtonText(nr)+" "+ret);
            if(!ret.isEmpty())addInputText(ret);
        }
        return ret;
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
        String[] otherIdentifiers = new String[]{".",",","ANS","(",")","+","-","×","÷"};

        if(identifier.matches("[1-2][1-6]")){
            int nr = Integer.valueOf(identifier);
            System.out.println("mode: "+calcModel.getMode());
            if(calcModel.getMode() == 5){
                if(Integer.valueOf(identifier)>20){
                    calcModel.setMemory(view.getSelection(),Integer.valueOf((nr-1)%10));
                }
                else {replaceSelection(calcModel.getMemory((nr-1)%10));}
            } else {
                ret = calcModel.getFunctionButtonFunctionality(nr);
                if(!ret.contains(">")){ addInputText(calcModel.getFunctionButtonFunctionality(nr));}
            }
        }  else if(Arrays.asList(otherIdentifiers).contains(identifier) || identifier.matches("[0-9]")) {
            addInputText(identifier);
            ret =  identifier;
        }
        if(!ret.isEmpty() && ret.substring(0,1).equals(">")){
            view.setOutputText(calcModel.getOutputString());
            return "";
        }
        System.out.println("inputButton: "+ret);
        return ret;
    }

    public void setMode(int mode){calcModel.setMode(mode);}
    public void nextMode(){calcModel.nextMode();}
    public void previousMode(){calcModel.previousMode();}

    public String getFunctionButtonText(int index){return calcModel.getFunctionButtonText(index);}
    public int getMode(){return calcModel.getMode();}

    /**
     * replace selected text with text
     * @param input
     */
    public void replaceSelection(String input) {
        if (input == null || input.isEmpty()) return;
        int selStart = view.getSelectionStartInput();
        int selEnd =  view.getSelectionEndInput();
        System.out.println(input);
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
    }

    /**
     * deletes text of et_input and et_output
     */
    private void inputClearAll(){
        setInputText("");
        view.setInputText("");
        view.setOutputText("");
        view.clearFocusOutput();
        view.requestFocusInput();
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

    public interface View{
        void setBtnText(int index, String text);
        String getBtnText(int i);

        String getOutputText();

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

        void requestFocusInput();

        void requestFocusOutput();

        String getInputText();
        void setOutputText(String text);

        void clearFocusInput();
        void clearFocusOutput();
        boolean hasFocusInput();
        boolean hasFocusOutput();
    }
}
