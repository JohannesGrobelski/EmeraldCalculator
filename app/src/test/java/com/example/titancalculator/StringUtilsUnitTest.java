package com.example.titancalculator;

import com.example.titancalculator.helper.StringUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.example.titancalculator.helper.StringUtils.deleteSpan;
import static com.example.titancalculator.helper.StringUtils.insertString;
import static com.example.titancalculator.helper.StringUtils.randomString;
import static com.example.titancalculator.helper.StringUtils.repeat;
import static com.example.titancalculator.helper.StringUtils.replace;
import static com.example.titancalculator.helper.StringUtils.split;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StringUtilsUnitTest {
    int testIterations = 100;

    @Test
    public void testRandomString(){
        assertEquals("",randomString(-1));
        assertEquals("",randomString(0));
        for(int ti=0;ti<testIterations;ti++){
            for(int i=0; i<10; i++) {
                int randomLength = ((int) (Math.random() * Math.max(20,testIterations))) + 1;
                String randomString = randomString(randomLength);
                assertEquals(randomLength, randomString.length());
            }
        }
    }

    @Test
    public void testSplit(){
        System.out.println("\\");
        for(int ti=0;ti<testIterations;ti++) {
            String randomInput = StringUtils.randomString(Math.max(36,((int) (Math.random()*testIterations))+1));
            String[] randomDelimiters = StringUtils.randomString(Math.max(5,((int) (Math.random()*10)))).split("");
            randomDelimiters =  Arrays.copyOf(Arrays.stream(randomDelimiters).distinct().toArray(), Arrays.stream(randomDelimiters).distinct().toArray().length, String[].class);

            System.out.println(randomInput+" "+Arrays.toString(randomDelimiters));
            String[] splitted = split(randomInput,randomDelimiters);

            int lengthSplitted = 0;
            for(String subsplit: splitted){
                for(String d: randomDelimiters){
                    if(subsplit.contains(d)){
                        System.out.println(d+" in "+Arrays.toString(splitted));
                        assertTrue(!subsplit.contains(d));
                    }
                }
                lengthSplitted += subsplit.length();
            }
            assertTrue(lengthSplitted<=randomInput.length());
        }
    }

    //actualy used

    @Test
    public void testDeleteSpan(){
        for(int ti=0;ti<testIterations;ti++){
            String randomInput = StringUtils.randomString(((int) (Math.max(20,testIterations))+1));
            assertEquals(randomInput,deleteSpan(randomInput,0,0));
            assertEquals(randomInput,deleteSpan(randomInput,-1,0));
            assertEquals(randomInput,deleteSpan(randomInput,0,-1));
            assertEquals(randomInput,deleteSpan(randomInput,1,0));
            assertEquals(randomInput,deleteSpan(randomInput,randomInput.length()+1,randomInput.length()));
            for(int spanlength=1;spanlength<=randomInput.length();spanlength++){
                for(int i=0; i<randomInput.length();i++){
                    int end = Math.min(i+spanlength,randomInput.length());
                    String substring = deleteSpan(randomInput,i,end);
                    assertEquals(randomInput.length(),substring.length()+(end-i));
                }
            }
        }
    }



    @Test
    public void testRepeat(){
        for(int ti=0;ti<testIterations;ti++) {
            String randomInput = StringUtils.randomString(Math.max(36,((int) (Math.random()*testIterations))+1));
            assertEquals(randomInput,repeat(randomInput,-1));
            assertEquals("",repeat(randomInput,0));
            assertEquals(randomInput,repeat(randomInput,1));
            int repetitions = ((int) (Math.random()*10)+5);
            String repeat = repeat(randomInput,repetitions);
            for(int i=0;i<repetitions;i++)repeat = repeat.replaceFirst(randomInput,"");
            assertEquals("",repeat.replace(randomInput,""));
        }
    }

    @Test
    public void testReplace(){
        for(int ti=0;ti<testIterations;ti++) {
            String randomInput = StringUtils.randomString(Math.max(36,((int) (Math.random()*testIterations))+5));
            String replacement = StringUtils.randomString(randomInput.length()/2);
            int start = ((int) (Math.random()*10)+5);
            int end = start + 2;

            String replaced = randomInput.substring(start,end);
            String result = replace(randomInput,replacement,start,end);;
            assertTrue(result.contains(replacement));
            assertEquals(randomInput,result.replace(replacement,replaced));
        }
    }

    @Before
    public void testInsertString(){
        for(int ti=0;ti<testIterations;ti++) {
            String randomInput = StringUtils.randomString(Math.max(36,((int) (Math.random()*testIterations))+5));
            String randomInsert = StringUtils.randomString(randomInput.length()/2);
            int start = ((int) (Math.random()*10)+5);
            String result = insertString(randomInput,randomInsert,start);;
            assertTrue(result.contains(randomInsert));
            assertEquals(randomInput,result.replace(randomInsert,""));
        }
    }

    @Test
    public void testOccurences(){
        for(int ti=0;ti<testIterations;ti++) {
            String randomInput = StringUtils.randomString(Math.max(36,((int) (Math.random()*testIterations))+10));
            String randomInsert = StringUtils.randomString(((int) (Math.random()*5)));
            System.out.println(randomInput+" "+randomInsert);
            String inserted = randomInput;
            int numberInsertions = 0;
            for(int i=0;i<randomInput.length();i+=randomInsert.length()){
                inserted = insertString(inserted,randomInsert,i);
                numberInsertions++;
            }
            System.out.println(inserted);


        }
    }

    hier weitermachen


}
