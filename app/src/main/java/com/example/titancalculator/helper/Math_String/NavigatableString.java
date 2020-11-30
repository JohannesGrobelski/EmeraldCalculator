package com.example.titancalculator.helper.Math_String;

import com.example.titancalculator.geplanteFeatures.MathString.ContentString;

public class NavigatableString{

    private ContentString contentString;

    public NavigatableString(){
        contentString = new NumberString();
    }

    public void clearAll(){
        contentString.setContent("");
    }

    public void clear(int position){
        String c = contentString.getContent();
        if(c.equals(""))return;
        else{
            String res = removeCharacter(c,position);
            contentString.setContent(res);
        }
    }

    public int getLength(){return contentString.getLength();}

    public void addText(String a,int position) {
        if(a == null || a.isEmpty() || a.equals("") || position < 0)return;
        int cursorPosition = position;
        String newText = insertString(contentString.getContent(),a,cursorPosition);
        contentString.setContent(newText);
    }

    public void concatenateText(String a){
        if(a == null || a.isEmpty() || a.equals(""))return;
        contentString.setContent(contentString.getContent() + a);
    }

    public void setText(String a) {
        contentString.setContent(a);
    }

    private String removeCharacter(String originalString, int index){
        if(index < 0 || index > originalString.length())return originalString;
        StringBuffer newString = new StringBuffer(originalString);
        newString.deleteCharAt(index);
        String c = newString.toString();
        return newString.toString();
    }

    public static String insertString(String originalString,String stringToBeInserted, int index){
        if(index < 0 || index > originalString.length())return originalString;
        StringBuffer newString = new StringBuffer(originalString);
        newString.insert(index, stringToBeInserted);
        return newString.toString();
    }

    public String getResult(){    return ((NumberString) contentString).getResult();  }

    public String normalToScientific(){
        return ((NumberString) contentString).normalToScientific();
    }

    public String scientificToNormal(){
        return ((NumberString) contentString).scientificToNormal();
    }

    public String getPercent(){
        if(contentString instanceof NumberString) return((NumberString)contentString).getPercent();
        else return "";
    }

    public String getRAD(){
        if(contentString instanceof NumberString) return((NumberString)contentString).getRAD();
        else return "";
    }

    public String getDEG(){
        if(contentString instanceof NumberString) return((NumberString)contentString).getDEG();
        else return "";
    }

    public String getInvert(){
        if(contentString instanceof NumberString) return ((NumberString)contentString).getInvert();
        else return "";
    }

    public String getReciproke(){
        if(contentString instanceof NumberString) return ((NumberString)contentString).getReciproke();
        else return "";
    }

    public String getPFZ(){
        if(contentString instanceof NumberString) return ((NumberString)contentString).getPFZ();
        else return "";
    }

    public String getBruch(){
        if(contentString instanceof NumberString) return ((NumberString)contentString).toFraction();
        else return "";
    }

    public String getDisplayableString(){   return ((NumberString)contentString).getContent();  }

    public void setMeanMode(String mode){((NumberString)contentString).setMeanMode(mode);}
    public String getMeanMode(){return ((NumberString)contentString).getMeanMode();}


    public void setVarMode(String mode){
        ((NumberString)contentString).setVarMode(mode);
    }
    public String getVarMode(){return ((NumberString)contentString).getVarMode();}

}
