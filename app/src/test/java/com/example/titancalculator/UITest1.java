package com.example.titancalculator;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class UITest1 {
    int testIterationen = 10000;
    MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);


    View[] views;

    @Before
    public void beforeTest(){
        mainActivity.setContentView(R.layout.activity_main);

        views = new View[]{
            //Buttons
                mainActivity.findViewById(R.id.btn_11),mainActivity.findViewById(R.id.btn_12),mainActivity.findViewById(R.id.btn_13),
                mainActivity.findViewById(R.id.btn_14),mainActivity.findViewById(R.id.btn_15),mainActivity.findViewById(R.id.btn_16),
                mainActivity.findViewById(R.id.btn_21),mainActivity.findViewById(R.id.btn_22),mainActivity.findViewById(R.id.btn_23),
                mainActivity.findViewById(R.id.btn_24),mainActivity.findViewById(R.id.btn_25),mainActivity.findViewById(R.id.btn_26),
                mainActivity.findViewById(R.id.btn_FUN),mainActivity.findViewById(R.id.btn_clear),mainActivity.findViewById(R.id.btn_clearall),
                mainActivity.findViewById(R.id.btn_LINKS),mainActivity.findViewById(R.id.btn_RECHTS),
                mainActivity.findViewById(R.id.btn_1),mainActivity.findViewById(R.id.btn_2),mainActivity.findViewById(R.id.btn_2),
                mainActivity.findViewById(R.id.btn_4),mainActivity.findViewById(R.id.btn_5),mainActivity.findViewById(R.id.btn_6),
                mainActivity.findViewById(R.id.btn_7),mainActivity.findViewById(R.id.btn_8),mainActivity.findViewById(R.id.btn_9),
                mainActivity.findViewById(R.id.btn_0),mainActivity.findViewById(R.id.btn_com),mainActivity.findViewById(R.id.btn_sep),
                mainActivity.findViewById(R.id.btn_open_bracket),mainActivity.findViewById(R.id.btn_close_bracket),
                mainActivity.findViewById(R.id.btn_mul),mainActivity.findViewById(R.id.btn_div),
                mainActivity.findViewById(R.id.btn_sub),mainActivity.findViewById(R.id.btn_add),
                mainActivity.findViewById(R.id.btn_ANS),mainActivity.findViewById(R.id.btn_eq),
            //inputs
                mainActivity.findViewById(R.id.eT_eingabe),mainActivity.findViewById(R.id.eT_ausgabe),mainActivity.findViewById(R.id.toolbar)
        };
    }

    /**
     * click everything that is clickable
     */
    @Test
    public void testRandomInput(){
        for(int ti=0;ti<testIterationen;ti++){
            ((View) views[((int) (Math.random()*views.length))]).performClick();
        }
    }

    @Test
    public void testScenarioInput(){

    }




    //helpfunctions
    private void actionRandomSelection(){

    }
}
