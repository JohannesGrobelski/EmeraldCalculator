package com.example.titancalculator.helper.Math_String;

public class NavigatableString{

    private ContentString contentString;

    public NavigatableString(String mode){
        contentString = new NumberString();
    }

    public void clearAll(){
        contentString.setContent("");
    }

    public void clear(int position){
        String c = contentString.getContent();
        if(c.equals(""))return;
        else{
            String res = removeCharacter(c,position-1);
            contentString.setContent(res);
        }
    }

    public void addText(String a,int position) {
        if(a == null || a.isEmpty() || a.equals(""))return;
        int cursorPosition = position;
        String newText = insertString(contentString.getContent(),a,cursorPosition);
        contentString.setContent(newText);
    }

    public void setText(String a) {
        contentString.setContent(a);
    }

    public static String removeCharacter(String originalString, int index){
        if(index < 0 || index > originalString.length())return originalString;
        StringBuffer newString = new StringBuffer(originalString);
        newString.deleteCharAt(index);
        String c = newString.toString();
        /*
        //fix
        c = c.replace("P","");
        c = c.replace("I","");
        c = c.replace("Z","");
        c = c.replace("n","");
        c = c.replace("b","");
        c = c.replace("P","");
        c = c.replace("P","");
        c = c.replace("\u00B3","");
        c = c.replace("LO","");
        c = c.replace("LG","");
        c = c.replace("OG","");
        c = c.replace("L","");
        c = c.replace("B","");
        */

        return newString.toString();
    }

    public static String insertString(String originalString,String stringToBeInserted, int index){
        if(index < 0 || index > originalString.length())return originalString;
        StringBuffer newString = new StringBuffer(originalString);
        newString.insert(index, stringToBeInserted);
        return newString.toString();
    }

    public String getResult(){
        return ((NumberString) contentString).getResult();
    }

    public String getPercent(){
        if(contentString instanceof NumberString) return((NumberString)contentString).getPercent();
        else return "";
    }

    public String getBIN(){
        if(contentString instanceof NumberString) return((NumberString)contentString).getBIN();
        else return "";
    }

    public String getOCT(){
        if(contentString instanceof NumberString) return((NumberString)contentString).getOCT();
        else return "";
    }

    public String getDEC(){
        if(contentString instanceof NumberString) return((NumberString)contentString).getDEC();
        else return "";
    }

    public String getHEX(){
        if(contentString instanceof NumberString) return((NumberString)contentString).getHEX();
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

    public void setArgument(){
        if(contentString instanceof NumberString) ((NumberString)contentString).setArgument();
    }

    public String getBruch(){
        if(contentString instanceof NumberString) return ((NumberString)contentString).getBruch();
        else return "";
    }

    public String getDisplayableString(){
        return ((NumberString)contentString).getDisplayableString(((NumberString)contentString).content);
    }

    public void setMeanMode(String mode){
        ((NumberString)contentString).setMean_mode(mode);
    }

    public void setVarMode(String mode){
        ((NumberString)contentString).setVar_mode(mode);
    }
}
