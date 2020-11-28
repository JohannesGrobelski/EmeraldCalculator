package com.example.titancalculator;

import com.example.titancalculator.helper.StringUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.example.titancalculator.helper.StringUtils.concatenate;
import static com.example.titancalculator.helper.StringUtils.deleteSpan;
import static com.example.titancalculator.helper.StringUtils.findLongestMatch;
import static com.example.titancalculator.helper.StringUtils.insertString;
import static com.example.titancalculator.helper.StringUtils.occurences;
import static com.example.titancalculator.helper.StringUtils.randomString;
import static com.example.titancalculator.helper.StringUtils.repeat;
import static com.example.titancalculator.helper.StringUtils.replace;
import static com.example.titancalculator.helper.StringUtils.split;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StringUtilsUnitTest {
    int testIterations = 10000;

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
        assertArrayEquals(new String[0],split("abcde","abcde".split("")));
        assertArrayEquals(new String[0],split("","abcde".split("")));
        assertArrayEquals(new String[0],split("","".split("")));
        assertArrayEquals("abcde".split(""),split("abcde","".split("")));
        assertArrayEquals(new String[]{"abcde"},split("abcde",new String[0]));

        assertArrayEquals(new String[]{"PR9QFYH62N38XQKPN", "PPF0P", "I40AG", "LM2QNDR9NWC2A", "V0DM957PDZ", "VFQFQ", "35A"},
                split("UI40AGSVFQFQJ35ASLM2QNDR9NWC2AUV0DM957PDZBPR9QFYH62N38XQKPNSBPPF0PBJJ", new String[]{"O", "S", "B", "U", "J"}));


        for(int ti=0;ti<testIterations;ti++) {
            String randomInput = StringUtils.randomString(Math.max(36,((int) (Math.random()*testIterations))+1));
            String[] randomDelimiters = StringUtils.randomString(Math.max(5,((int) (Math.random()*10)))).split("");
            randomDelimiters =  Arrays.copyOf(Arrays.stream(randomDelimiters).distinct().toArray(), Arrays.stream(randomDelimiters).distinct().toArray().length, String[].class);
            //System.out.println(randomInput+" "+Arrays.toString(randomDelimiters));
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
        for(int ti=0;ti<Math.sqrt(testIterations);ti++){
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

    @Test
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
            String randomInput = "";
            while(!randomInput.contains("A")){
                randomInput = StringUtils.randomString(Math.max(36,((int) (Math.random()*testIterations))+10));
            }
            String randomInsert = StringUtils.randomString(((int) (Math.random()*5))+1);

            while(randomInput.contains(randomInsert))randomInput = randomInput.replace(randomInsert,"");
            assertEquals(0, occurences(randomInput,randomInsert));


            String copy = randomInput;
            //number of A's
                String noA = "";
                while(noA.isEmpty() || noA.contains("A"))noA = copy.replaceAll("A","");
                int occurences = copy.length() - noA.length();
            assertEquals(occurences,occurences(randomInput,"A"));

            //randomInput = randomInput.replace("A",randomInsert);
            //assertEquals(occurences, occurences(randomInput,randomInsert));
        }
    }

    @Test
    public void findLongestMatchTest() {
        for (int ti = 0; ti < testIterations; ti++) {
            String repeat = new String(new char[20]).replace("\0", "ABCDEFGHI");
            System.out.println();
            String random = shuffleString(repeat);

            String searchRegex = "A+";
            String searchString = ""; int longestMatch = 0;
            while(true){
                if(random.indexOf(searchString+"A") != -1){
                    ++longestMatch;
                    searchString = new String(new char[longestMatch]).replace("\0", "A");
                }
                else break;
            }
            assertEquals(longestMatch,findLongestMatch(searchString,random).length());
        }
    }



    @Test
    public void concatenateTest() {
        assertArrayEquals(new String[0],concatenate(new String[0],new String[0]));
        for (int ti = 0; ti < testIterations; ti++) {
            String a = randomString(((int) (Math.random()*10)+1));
            String b = randomString(((int) (Math.random()*20)+2));
            String[] A = a.split("");
            String[] B = b.split("");
            assertArrayEquals(A,concatenate(new String[0],A));
            assertArrayEquals(A,concatenate(A,new String[0]));
            assertArrayEquals((a+b).split(""),concatenate(A,B));
            assertArrayEquals((b+a).split(""),concatenate(B,A));
        }
    }


    private String shuffleString(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        String shuffled = "";
        for (String letter : letters) {
            shuffled += letter;
        }
        return shuffled;
    }

    private static String[] shuffle(String[] input){
        List<String> asList = Arrays.asList(input);
        Collections.shuffle(asList);
        return asList.toArray(new String[0]);
    }

    private String shuffleString2(String string){
        for(int i=0; i<string.length()*100; i++){
            int randomCut1=0; int randomCut2=0;
            randomCut1 = ((int) (Math.random()*(string.length()-2)))+1;
            randomCut2 = ((int) (Math.random()*(string.length()-2)))+1;
            while(randomCut1==randomCut2)randomCut2 = ((int) (Math.random()*(string.length()-2)))+1;
            int r1 = Math.min(randomCut1,randomCut2); int r2 = Math.max(randomCut1,randomCut2);
            String subA = string.substring(0,r1);
            String subB = string.substring(r1,r2);
            String subC = string.substring(r2);
            assertEquals(subA.length()+subB.length()+subC.length(),string.length());
            int r = (int) (Math.random()*5);
            switch (r){
                case 0:string = subA+subC+subB;break;
                case 1:string = subB+subA+subC;break;
                case 2:string = subB+subC+subA;break;
                case 3:string = subC+subA+subB;break;
                case 4:string = subC+subB+subA;break;
            }
        }
        return string;
    }
}