package com.example.titancalculator.helper.Math_String;

public class ContentString{
    String content="";

    public String getContent(){
        return content;
    }

    public boolean setContent(String a){
        content = a;
        return false;
    }

    public String trim(){return content.trim();}

    public void copy(ContentString c){
        this.content = c.content;
    }

    public String getDisplayableString(){
        return content;
    }

    public int getLength(){return content.length();}
}