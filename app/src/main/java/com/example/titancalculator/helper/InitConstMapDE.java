package com.example.titancalculator.helper;

import android.content.Context;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class InitConstMapDE {

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
        matKAr.put("goldener Schnitt",new String("1.6180339887"));
        matKAr.put("Euler-Mascheroni-Konstante",new String("0.5772156649"));
        matKAr.put("Apéry-Konstante",new String("1.2020569031"));
        matKAr.put("Erdős-Borwein-Konstante",new String("1.6066951524"));
        matKAr.put("Ramanujan-Soldner-Konstante",new String("1.4513692348"));
        matKAr.put("Lemniskatische Konstante",new String("2.6220575542"));
        matKAr.put("Legendre-Konstante",new String("1.08366"));
        matKAr.put("Grenzwert von Laplace",new String("0.6627434193"));
        matKAr.put("Catalansche Konstante",new String("0.9159655941"));
        matKAr.put("Meissel-Mertens-Konstante",new String("0.2614972128"));
        matKAr.put("Glaisher-Kinkelin-Konstante",new String("1.2824271291"));
        matKAr.put("Cahen-Konstante",new String("0.6434105462"));
        matKAr.put("Sierpiński-Konstante",new String("2.5849817595"));
        matKAr.put("Landau-Ramanujan-Konstante",new String("0.7642236535"));
        matKAr.put("Gieseking-Konstante",new String("1.0149416064"));
        matKAr.put("Bernstein-Konstante",new String("0.2801694990"));
        matKAr.put("Brunsche Konstante",new String("1.90216058"));
        matKAr.put("Primzahlzwillingskonstante",new String("0.66016181584686957392"));
        matKAr.put("Landau-Konstante",new String("0.54325896534297670695"));
        matKAr.put("Golomb-Dickman-Konstante",new String("0.62432998854355087099"));
        matKAr.put("Chintschin-Konstante",new String("2.68545200106530644530"));
        matKAr.put("Chintschin-Lévy-Konstante",new String("1.18656911041562545282"));
        matKAr.put("Mills-Konstante",new String("1.30637788386308069046"));
        matKAr.put("Liebs Eiswürfelkonstante",new String("1.53960071783900203869"));
        matKAr.put("Niven-Konstante",new String("1.70521114010536776428"));
        matKAr.put("Gauß-Kusmin-Wirsing-Konstante",new String("0.30366300289873265859"));
        matKAr.put("Porter-Konstante",new String("1.46707807943397547289"));
        matKAr.put("Chaitinsche Konstante",new String("0.0078749969978123844"));
        matKAr.put("Alladi-Grinstead-Konstante",new String("0.80939402054063913071"));
        matKAr.put("1. Feigenbaum-Konstante",new String("4.66920160910299067185"));
        matKAr.put("2. Feigenbaum-Konstante",new String("2.50290787509589282228"));
        matKAr.put("Fransén-Robinson-Konstante",new String("2.80777024202851936522"));
        matKAr.put("Lengyel-Konstante",new String("1.09868580552518701"));
        matKAr.put("Hafner-Sarnak-McCurley-Konstante",new String("0.35323637185499598454"));
        matKAr.put("Backhouse-Konstante",new String("1.45607494858268967139"));
        matKAr.put("Viswanath-Konstante",new String("1.1319882487943"));
        matKAr.put("Embree-Trefethen-Konstante",new String("0.70258"));
    }

    public static void initPhyKAr(){
        phyKAr.put("Atomare Masseneinheit","1.660538782*10^-27");
        phyKAr.put("Energieäquivalent für 1u","931.494028");
        phyKAr.put("Avogadrozahl"," 6.02214179*10^26");
        phyKAr.put("Boltzmannkonstante","1.3806504*10^-23");
        phyKAr.put("Elektrische Feldkonstante","8.854187817*10^-12");
        phyKAr.put("Elementarladung","1.602176487*10^-19");
        phyKAr.put("Fallbeschleunigung - Äquator ","9.802");
        phyKAr.put("Fallbeschleunigung - Pol ","9.867");
        phyKAr.put("Fallbeschleunigung - Europa","9.807");
        phyKAr.put("Faraday Konstante","9.64853399*10^7");
        phyKAr.put("Gaskonstante (allgemein)","8.314472*10^3");
        phyKAr.put("Gravitationskonstante","6.67*10^-11");
        phyKAr.put("Hubble-Konstante","74,2");
        phyKAr.put("Lichtgeschwindigkeit (Vakuum)","2.99792458*108");
        phyKAr.put("Magnetische Feldkonstante","4*PI*10^-7");
        phyKAr.put("Planck Konstante"," 6.62606896*10^-34");
        phyKAr.put("Rydberg-Konstante (m → ∞)"," 1.0973732*10^7");
        phyKAr.put(" Rydberg-Konstante (Wasserstoff)","1.0967758*10^7");
        phyKAr.put(" Stefan-Boltzmann-Konstante","5.670400*10^-8");
        phyKAr.put(" Wien-Verschiebungskonstante","2.897769*10^-3");

    }

    public static void initChKAr(){
        chKAr.put("absoluter Nullpunkt(C)",new String("-273.15"));
        chKAr.put("molares Normvolumen",new String("22.414"));
        chKAr.put("Normdruck(Pa)",new String("101325"));
        chKAr.put("Normdruck(Bar)",new String("1.01325"));
        chKAr.put("Normfallbeschleunigung",new String("9.80665"));
        chKAr.put("Normtemperatur(C)",new String("0"));
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
