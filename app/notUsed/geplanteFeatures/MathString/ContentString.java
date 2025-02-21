package com.example.titancalculator.geplanteFeatures.MathString;

public class ContentString{
    protected String content="";

    public String getContent(){
        return content;
    }

    public void setContent(String a) {
        content = a;
    }

    public void copy(ContentString c){
        this.content = c.content;
    }

    public int getLength(){return content.length();}
}