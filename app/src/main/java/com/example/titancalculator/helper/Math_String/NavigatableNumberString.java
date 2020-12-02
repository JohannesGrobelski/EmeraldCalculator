package com.example.titancalculator.helper.Math_String;

import com.example.titancalculator.geplanteFeatures.MathString.ContentString;

public class NavigatableNumberString {

    private ContentString contentString;

    public NavigatableNumberString(){
        contentString = new NumberString();
    }

    public static String getDisplayableString(String a) {a = a.replace("ROOT","√");return a;}
    public String getContent(){return contentString.getContent();}
    public int getLength(){return contentString.getLength();}
    public void setText(String a) {
        contentString.setContent(a);
    }
    public String getDisplayableString(){ return ((NumberString)contentString).getContent();}

    public void clearAll(){contentString.setContent("");}

    public void clear(int position){
        String c = contentString.getContent();
        if(c.equals(""))return;
        else{
            String res = removeCharacter(c,position);
            contentString.setContent(res);
        }
    }

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

    private String removeCharacter(String originalString, int index){
        if(index < 0 || index > originalString.length())return originalString;
        StringBuffer newString = new StringBuffer(originalString);
        newString.deleteCharAt(index);
        return newString.toString();
    }

    public static String insertString(String originalString,String stringToBeInserted, int index) {
        if (index < 0 || index > originalString.length()) return originalString;
        StringBuffer newString = new StringBuffer(originalString);
        newString.insert(index, stringToBeInserted);
        return newString.toString();
    }
}
