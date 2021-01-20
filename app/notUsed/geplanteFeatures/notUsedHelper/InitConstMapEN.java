package com.example.calcitecalculator.geplanteFeatures.notUsedHelper;

import android.content.Context;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class InitConstMapEN {

    private static HashMap<String, String> ALLKAr = new HashMap<>();
    private static HashMap<String, String> uniKAr = new HashMap<>();
    private static HashMap<String, String> matKAr = new HashMap<>();
    private static HashMap<String, String> phyKAr = new HashMap<>();
    private static HashMap<String, String> chKAr = new HashMap<>();
    private static HashMap<String, String> FavKAr = new HashMap<>();
    private static HashMap<String, String> eigKAr = new HashMap<>();


    private static Set<String> FavKonst;

    public static void init(Context context){
        FavKonst = PreferenceManager.getDefaultSharedPreferences(context).getStringSet("FAV_KONST", new HashSet<String>());

        initUniKAr();
        initMatKAr();
        initPhyKAr();
        initChKAr();
        initEigKAr();
        initFavKAr();
    }

    public static void initEigKAr(){


    }

    public static void initUniKAr(){
        matKAr.put("Wellenwiderstand Vakuum",new String("376.730313667"));
        matKAr.put("Gravitationskonstante",new String("376.730313667"));

    }
    
    public static void initMatKAr(){
        matKAr.put("PI",String.valueOf(Math.PI));
        matKAr.put("e",String.valueOf(Math.E));
        matKAr.put("golden ratio",new String("1.6180339887"));
        matKAr.put("Euler-Mascheroni constant",new String("0.5772156649"));
        matKAr.put("Apéry constant",new String("1.2020569031"));
        matKAr.put("Erd's-Borwein-Constant",new String("1.6066951524"));
        matKAr.put("Ramanujan-Soldner-Constant",new String("1.4513692348"));
        matKAr.put("Lemniskatic constant",new String("2.6220575542"));
        matKAr.put("Legendre constant",new String("1.08366"));
        matKAr.put("Laplace limit",new String("0.6627434193"));
        matKAr.put("Catalan constant",new String("0.9159655941"));
        matKAr.put("Meissel-Mertens constant",new String("0.2614972128"));
        matKAr.put("Glaisher-Kinkelin-Constant",new String("1.2824271291"));
        matKAr.put("Cahen constant",new String("0.6434105462"));
        matKAr.put("Sierpisski constant",new String("2.5849817595"));
        matKAr.put("Landau-Ramanujan constant",new String("0.7642236535"));
        matKAr.put("Gieseking constant",new String("1.0149416064"));
        matKAr.put("Amber Constant",new String("0.2801694990"));
        matKAr.put("Brun's Constant",new String("1.90216058"));
        matKAr.put("Prime twin constant",new String("0.66016181584686957392"));
        matKAr.put("Landau constant",new String("0.54325896534297670695"));
        matKAr.put("Golomb-Dickman constant",new String("0.62432998854355087099"));
        matKAr.put("Chinchinconstant Constant",new String("2.68545200106530644530"));
        matKAr.put("Chinchinin-Lévy constant",new String("1.18656911041562545282"));
        matKAr.put("Mills constant",new String("1.30637788386308069046"));
        matKAr.put("Love ice cube constant",new String("1.53960071783900203869"));
        matKAr.put("Niven constant",new String("1.70521114010536776428"));
        matKAr.put("Gauss-Kusmin-Wirsing-Constant",new String("0.30366300289873265859"));
        matKAr.put("Porter constant",new String("1.46707807943397547289"));
        matKAr.put("Chaitin's Constant",new String("0.007874999978123844"));
        matKAr.put("Alladi-Grinstead constant",new String("0.80939402054063913071"));
        matKAr.put("1. Fig Tree Constant",new String("4.66920160910299067185"));
        matKAr.put("2. Fig Tree Constant",new String("2.50290787509589282228"));
        matKAr.put("Fransén-Robinson constant",new String("2.80777024202851936522"));
        matKAr.put("Lengyel constant",new String("1.09868580552518701"));
        matKAr.put("Hafner-Sarnak-McCurley-Constant",new String("0.35323637185499598454"));
        matKAr.put("Backhouse Constant",new String("1.45607494858268967139"));
        matKAr.put("Viswanath constant",new String("1.1319882487943"));
        matKAr.put("Embree-Trefethen constant",new String("0.70258"));
    }

    public static void initPhyKAr(){
        phyKAr.put("Atomic Mass Unit","1.660538782*10^-27");
        phyKAr.put("Energy equivalent for 1u","931.494028");
        phyKAr.put("Avogadro number"," 6.02214179*10^-26");
        phyKAr.put("Boltzmann constant","1.3806504*10^-23");
        phyKAr.put("Electric field constant","8.854187817*10^-12");
        phyKAr.put("Elementary Charge","1.602176487*10^-19");
        phyKAr.put("Case Acceleration - Equator", "9.802");
        phyKAr.put("Case Acceleration - Pol ", "9.867");
        phyKAr.put("Case Acceleration - Europe","9.807");
        phyKAr.put("Faraday Constant","9.64853399*10^7");
        phyKAr.put("Gas constant (general)","8.314472*10^-3");
        phyKAr.put("gravitational constant","6.67*10^-11");
        phyKAr.put("Hubble constant","74.2");
        phyKAr.put("Speed of light (vacuum)","2.99792458*108");
        phyKAr.put("Magnetic field constant","4*PI*10^-7");
        phyKAr.put("Planck Constant"," 6.62606896*10^-34");
        phyKAr.put("Rydberg constant","1.0973732*10^-7");
        phyKAr.put("Rydberg constant (hydrogen)","1.0967758*10^-7");
        phyKAr.put("Stefan-Boltzmann-Constant","5.670400*10^-8");
        phyKAr.put("Vienna shift constant","2.897769*10^-3");

    }

    public static void initChKAr(){
        chKAr.put("absolute zero (C)",new String("-273.15"));
        chKAr.put("molares standard volume",new String("22.414"));
        chKAr.put("Standard Pressure(Pa)",new String("101325"));
        chKAr.put("Standard Pressure(Bar)",new String("1.01325"));
        chKAr.put("Standard Case Acceleration",new String("9.80665"));
        chKAr.put("Standard Temperature(C)",new String("0"));
    }

    public static void initFavKAr(){
        ALLKAr.putAll(uniKAr);
        ALLKAr.putAll(matKAr);
        ALLKAr.putAll(phyKAr);
        ALLKAr.putAll(chKAr);

        for(String s: FavKonst){
            if(s == null)continue;
            FavKAr.put(s,ALLKAr.get(s));
            Log.v("FAV: ",s+": "+ALLKAr.get(s));
        }


    }

    public static HashMap<String, String> getUniKAr(){
        return uniKAr;
    }

    public static HashMap<String, String> getMatKAr(){
        return matKAr;
    }

    public static HashMap<String, String> getPhyKAr(){
        return phyKAr;
    }

    public static HashMap<String, String> getChKAr(){
        return chKAr;
    }

    public static HashMap<String, String> geteignKAr(){
        return eigKAr;
    }

    public static HashMap<String, String> getFavKAr(){
        return FavKAr;
    }


}
