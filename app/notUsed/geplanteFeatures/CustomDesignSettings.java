/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.calcitecalculator.geplanteFeatures;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.app.NavUtils;

import com.example.calcitecalculator.R;
import com.example.calcitecalculator.geplanteFeatures.helper.MainDisplay.SettingsApplier;

import java.util.List;


/**
 * A PreferenceActivity that presents a set of application settings. On handset
 * devices, settings are presented as a single list. On tablets, settings are
 * split by category, with category headers shown to the left of the list of
 * settings.
 */

public class CustomDesignSettings extends AppCompatPreferenceActivity {



    static String language = "english";
    static Typeface current_typeface;
    static String current_fontsize;

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener
            sBindPreferenceSummaryToValueListener =
            new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object value) {

                    String stringValue = value.toString();

                    if (preference instanceof ListPreference) {
                        // For list preferences, look up the correct display value in
                        // the preference's 'entries' list.
                        ListPreference listPreference = (ListPreference) preference;
                        int index = listPreference.findIndexOfValue(stringValue);

                        // Set the summary to reflect the new value.
                        preference.setSummary(
                                index >= 0
                                        ? listPreference.getEntries()[index]
                                        : null);

                    } else if (preference instanceof RingtonePreference) {
                        // For ringtone preferences, look up the correct display value
                        // using RingtoneManager.
                        if (TextUtils.isEmpty(stringValue)) {
                            // Empty values correspond to 'silent' (no ringtone).
                            //preference.setSummary(R.string.pref_ringtone_silent);

                        } else {
                            Ringtone ringtone = RingtoneManager.getRingtone(
                                    preference.getContext(), Uri.parse(stringValue));

                            if (ringtone == null) {
                                // Clear the summary if there was a lookup error.
                                preference.setSummary(null);
                            } else {
                                // Set the summary to reflect the new ringtone display
                                // name.
                                String name = ringtone.getTitle(preference.getContext());
                                preference.setSummary(name);
                            }
                        }

                    } else {
                        // For all other preferences, set the summary to the value's
                        // simple string representation.
                        preference.setSummary(stringValue);
                    }
                    return true;
                }
            };

    /**
     * Determines if the device has an extra-large screen. For example, 10"
     * tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(
                sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener
                .onPreferenceChange(preference, PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * Sets up the action bar for the activity.
     *
     * @param savedInstanceState Saved instance state bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applySettings();
        setupActionBar();

        //Toast.makeText(SettingsActivity.this, "lang: "+language, Toast.LENGTH_SHORT).show();
    }




    @Override
    protected void onResume() {
        super.onResume();
        String lang = language;
        applySettings();
        setupActionBar();

        //TODO: besser: header neu laden, aber wie?
        if(!lang.equals(language)){
            Intent settingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            finish();
        }

        /*
        List<Header> target = new ArrayList<>();
        if(language.equals("english") || language.equals("englisch"))loadHeadersFromResource(R.xml.pref_headers_en,target);
        else if(language.equals("deutsch") || language.equals("german"))loadHeadersFromResource(R.xml.pref_headers_de,target);
        */
    }

    /**
     * Set up the android.app.ActionBar, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void applySettings(){
        //language
        language = androidx.preference.PreferenceManager.getDefaultSharedPreferences(CustomDesignSettings.this).getString("pref_lang","english");

        current_fontsize = androidx.preference.PreferenceManager.getDefaultSharedPreferences(CustomDesignSettings.this).getString("fontsize", "20");
        String current_font_family = androidx.preference.PreferenceManager.getDefaultSharedPreferences(CustomDesignSettings.this).getString("fontfamily", "monospace");
        String current_fontstlye = androidx.preference.PreferenceManager.getDefaultSharedPreferences(CustomDesignSettings.this).getString("fontstyle", "normal");
        current_typeface = FontSettingsActivity.getTypeFace(current_font_family,current_fontstlye);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        applySettings();
        if(language.equals("english") || language.equals("englisch"))loadHeadersFromResource(R.xml.pref_custom_design_headers_en, target);
        else if(language.equals("deutsch") || language.equals("german"))loadHeadersFromResource(R.xml.pref_custom_design_headers_de, target);
        //setBackground();
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        Log.e("act name", this.getLocalClassName());
        return PreferenceFragment.class.getName().equals(fragmentName)
                || ColorActFragment
                .class.getName().equals(fragmentName)
                || ButtoncolorSettingsFragment
                .class.getName().equals(fragmentName)
                || FontPreferenceFragment
                .class.getName().equals(fragmentName);

    }



    /**
     * Dieses Fragment lässt den Nutzer Farbeinstellungen verändern.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ColorActFragment
            extends PreferenceFragment {

        SharedPreferences mPrefs;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(language.equals("english") || language.equals("englisch"))addPreferencesFromResource(R.xml.pref_act_colors_en);
            else if(language.equals("deutsch") || language.equals("german"))addPreferencesFromResource(R.xml.pref_act_colors_de);
            setHasOptionsMenu(true);
            getActivity().setTitle("Color Settings");


            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            final SwitchPreference defaultColors = (SwitchPreference) findPreference("actcolor_default_switch");

            if (defaultColors != null){
                defaultColors.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(Preference arg0, Object areDefaultColorsEnabled) {

                        boolean isDefaultColorsOn = ((Boolean) areDefaultColorsEnabled).booleanValue();

                        if(isDefaultColorsOn){
                            Toast.makeText(getActivity(), "Colors changed to default!", Toast.LENGTH_SHORT).show();
                            SharedPreferences S = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            for(String key: S.getAll().keySet()){
                                S.edit().remove(key).commit();
                            }

                            PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_act_colors_en, true);
                            SettingsApplier.setDefaultColors();
                        }

                        return true;
                    }
                });
            }
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(
                        new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }





    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class FontPreferenceFragment
            extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(language.equals("english") || language.equals("englisch"))addPreferencesFromResource(R.xml.pref_general_en);
            else if(language.equals("deutsch") || language.equals("german"))addPreferencesFromResource(R.xml.pref_general_de);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(
                        new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Dieses Fragment lässt den Nutzer Farbeinstellungen verändern.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class ButtoncolorSettingsFragment
            extends PreferenceFragment {

        SharedPreferences mPrefs;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if(language.equals("english") || language.equals("englisch"))addPreferencesFromResource(R.xml.pref_buttoncolors_en);
            else if(language.equals("deutsch") || language.equals("german"))addPreferencesFromResource(R.xml.pref_buttoncolors_de);
            setHasOptionsMenu(true);
            getActivity().setTitle("Color Settings");


            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            final SwitchPreference defaultColors = (SwitchPreference) findPreference("color_default_switch");

            if (defaultColors != null){
                defaultColors.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

                    @Override
                    public boolean onPreferenceChange(Preference arg0, Object areDefaultColorsEnabled) {

                        boolean isDefaultColorsOn = ((Boolean) areDefaultColorsEnabled).booleanValue();


                        if(isDefaultColorsOn){
                            Toast.makeText(getActivity(), "Colors changed to default!", Toast.LENGTH_SHORT).show();
                            SharedPreferences S = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            for(String key: S.getAll().keySet()){
                                S.edit().remove(key).commit();
                            }

                            PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_buttoncolors_en, true);
                            SettingsApplier.setDefaultColors();
                        }



                        return true;
                    }
                });
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(
                        new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }


}
