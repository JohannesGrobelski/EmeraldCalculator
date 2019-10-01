package com.example.titancalculator.helper.MainDisplay;

import android.content.Context;
import android.util.DisplayMetrics;

import com.example.titancalculator.R;

public class DisplaySetupHelper {
        public static float getDefaultTextSize(Context context){
        switch (context.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return context.getResources().getDimension(R.dimen.textsize_low);
            case DisplayMetrics.DENSITY_MEDIUM:
                return context.getResources().getDimension(R.dimen.textsize_medium);
            case DisplayMetrics.DENSITY_TV:
            case DisplayMetrics.DENSITY_HIGH:
                return context.getResources().getDimension(R.dimen.textsize_high);
            case DisplayMetrics.DENSITY_260:
            case DisplayMetrics.DENSITY_280:
            case DisplayMetrics.DENSITY_300:
            case DisplayMetrics.DENSITY_XHIGH:
                return context.getResources().getDimension(R.dimen.textsize_xhigh);
            case DisplayMetrics.DENSITY_340:
            case DisplayMetrics.DENSITY_360:
            case DisplayMetrics.DENSITY_400:
            case DisplayMetrics.DENSITY_420:
            case DisplayMetrics.DENSITY_440:
            case DisplayMetrics.DENSITY_XXHIGH:
                return context.getResources().getDimension(R.dimen.textsize_xxhigh);
            case DisplayMetrics.DENSITY_560:
            case DisplayMetrics.DENSITY_XXXHIGH:
                return context.getResources().getDimension(R.dimen.textsize_xxxhigh);
            default: return 10f;
        }
    }










}
