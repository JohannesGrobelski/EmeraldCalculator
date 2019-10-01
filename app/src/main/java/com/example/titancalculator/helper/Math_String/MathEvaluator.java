package com.example.titancalculator.helper.Math_String;

import com.example.titancalculator.evalex.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Evaluates Expressions with doubles ([0-9]+,?[0-9]*and Operators /,*,+,-
 * @author Johannes
 *
 */
public class MathEvaluator {
	
	String contentString = "123";
	int marker = 1;
	
	public static void main(String[] args) {
        System.out.println(MathEvaluator.evaluate("3!",10));
        System.out.println(MathEvaluator.evaluate("SUME(1,6)",10));
        System.out.println(MathEvaluator.evaluate("SUME(1,6)",10));


        //System.out.println(MathEvaluator.evaluate("179910.2528331071280870501336296069"));

		//System.out.println(Integer.toString(Integer.parseInt(evaluate("14"),10),16));
		
		//System.out.println(StatUtils.variance(new double[] {1,2,3}));

		//System.out.println(toBruch("0.2534"));
	}

	public static String toRAD(String dec) {
		dec = dec.replace(',','.');
		return String.valueOf(Math.toRadians(Double.valueOf(dec)));
	}

	public static String toDEG(String dec) {
		dec = dec.replace(',','.');
		return String.valueOf(Math.toDegrees(Double.valueOf(dec)));
	}

    // Parse EVERY number with source radix
    // and transform in specified radix(base)
    public static String baseConversion(String input, int sBase, int dBase) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("[0-9A-Z]+").matcher(input);
        while (m.find()) {
            allMatches.add(m.group());
        }
        for(String s: allMatches.toArray(new String[allMatches.size()])) {
            String number = Integer.toString(Integer.parseInt(s, sBase), dBase);
            input = input.replace(s,number);
        }
        return input;
    }


    public static String toBruch(String dec) {
		dec = dec.replace(',','.');

		String gesamtbruch="";
		List<String> brueche = new ArrayList<String>();
		double decD = Double.parseDouble(dec);
		String beforeCom="";
		String afterCom="";
		if(dec.contains(".")) {
			beforeCom = dec.substring(0,dec.indexOf('.'));
			afterCom = dec.substring(dec.indexOf('.')+1);
		}
		else {beforeCom = dec;}
		int exp = beforeCom.length();
		for(char c: beforeCom.toCharArray()) {
			String b = String.valueOf(Character.digit(c, 10)*Math.pow(10,exp-1))+"/"+"1";
			brueche.add(b);
			--exp;
		}
		for(char c: afterCom.toCharArray()) {
			String b = Character.digit(c, 10)+"/"+String.valueOf(Math.pow(10,exp+1));
			brueche.add(b);
			++exp;
		}
		
		double div = Math.pow(10,exp);	
		double n = 0;
		for(String bruch: brueche) {
			n += Double.parseDouble(bruch.substring(0,bruch.indexOf('/')))*div / Double.parseDouble(bruch.substring(bruch.indexOf('/')+1));
		}
		
		gesamtbruch = String.valueOf(n)+"/"+String.valueOf(div);
		gesamtbruch = kuerzen(gesamtbruch);
		return gesamtbruch;
	}
	
	private static String kuerzen(String bruch) {
		while(true) {
			int A = (int) Double.parseDouble(bruch.substring(0,bruch.indexOf('/')));
			int B = (int) Double.parseDouble(bruch.substring(bruch.indexOf('/')+1));
			bruch = A+"/"+B;

			int ggT = ggT(A,B);
			if(ggT==1)break;
			A = A / ggT;
			B = B / ggT;
			///System.out.println("k�rzen: "+A+"/"+B);
			
			bruch = A+"/"+B;
		}
		return bruch;
	}
	
	private static int ggT(double a, double b) {
		int A = (int) a;
		int B = (int) b;

		int ggT = 0;
		for(int i=1;i<=Math.max(A, B)/2;i++) {
			if((A % i == 0) && (B % i == 0)) {
				ggT = i;
			}
		}
		
		return ggT;
	}

	public static String evaluate(String input,int base) {
        input = rootToSqrt(input);
        if(input.contains("!"))input = facToMul(input);
        //input = baseConversion(input,base,10);

        Expression expression = new Expression(input);
        try {
            String res = expression.eval().toString();
            return baseConversion(res,10,base);
        }
        catch (Exception e) {
            return "";
        }

    }

    private static String facToMul(String input) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("[0-9]+\\!").matcher(input);
        while (m.find()) {
            allMatches.add(m.group());
        }
        for(String s: allMatches.toArray(new String[allMatches.size()])) {
            Double number = Double.valueOf(s.replace("!", ""));

            if (!(number == Math.floor(number)) || Double.isInfinite(number) || number < 0 || number > 1000) {
                return "";
            } else {
                String newnumber = "";
                for(int i=1; i<=Math.floor(number); i++) {
                    newnumber += String.valueOf(i)+"*";
                }
                newnumber = newnumber.substring(0,newnumber.length()-1);
                Integer i =(int) Math.floor(number);
                input = input.replace(String.valueOf(i)+"!",newnumber);
            }

        }
        return input;
    }

    private static String rootToSqrt(String input) {
        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile("ROOT\\([^,]+\\)").matcher(input);
        while (m.find()) {
            allMatches.add(m.group());
        }
        for(String s: allMatches.toArray(new String[allMatches.size()])) {
            String match = s.replace("ROOT","SQRT");
            input = input.replace(s,match);
        }
        return input;
    }


	
	
	void clear(){
        String c = contentString;
        if(c.equals(""))return;
        else{
            if(marker > 0) {

                String s = "";

                if(marker > 1){
                    s+=c.substring(0,marker-1);
                }
                if(marker < c.length()){
                    s+=c.substring(marker);
                }
                
                --marker;

                System.out.println("DEL: "+s);
            }
        }
    }



	
	
	
	

}
