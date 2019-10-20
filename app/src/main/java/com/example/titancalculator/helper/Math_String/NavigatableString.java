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

    public String getResult(int base){
        return ((NumberString) contentString).getResult(base);
    }



    public String getPercent(int base){
        if(contentString instanceof NumberString) return((NumberString)contentString).getPercent(base);
        else return "";
    }

    public String getBIN(int base){
        if(contentString instanceof NumberString) return((NumberString)contentString).getBIN(base);
        else return "";
    }

    public String getOCT(int base){
        if(contentString instanceof NumberString) return((NumberString)contentString).getOCT(base);
        else return "";
    }

    public String getDEC(int base){
        if(contentString instanceof NumberString) return((NumberString)contentString).getDEC(base);
        else return "";
    }

    public String getHEX(int base){
        if(contentString instanceof NumberString) return((NumberString)contentString).getHEX(base);
        else return "";
    }

    public String getRAD(int base){
        if(contentString instanceof NumberString) return((NumberString)contentString).getRAD(base);
        else return "";
    }

    public String getDEG(int base){
        if(contentString instanceof NumberString) return((NumberString)contentString).getDEG(base);
        else return "";
    }

    public String getInvert(int base){
        if(contentString instanceof NumberString) return ((NumberString)contentString).getInvert(base);
        else return "";
    }

    public String getReciproke(int base){
        if(contentString instanceof NumberString) return ((NumberString)contentString).getReciproke(base);
        else return "";
    }

    public String getPFZ(int base){
        if(contentString instanceof NumberString) return ((NumberString)contentString).getPFZ(base);
        else return "";
    }

    public void setArgument(){
        if(contentString instanceof NumberString) ((NumberString)contentString).setArgument();
    }

    public String getBruch(int base){
        if(contentString instanceof NumberString) return ((NumberString)contentString).getBruch(base);
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
