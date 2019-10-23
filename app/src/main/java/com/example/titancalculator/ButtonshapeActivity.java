package com.example.titancalculator;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.helper.ButtonShapeCustomAdapter;
import com.example.titancalculator.helper.ButtonShapeImageModel;
import com.example.titancalculator.helper.MainDisplay.SettingsApplier;

import java.util.ArrayList;


public class ButtonshapeActivity extends AppCompatActivity {

    private ListView lv_form;
    private ListView lv_füllung;
    private ImageView iv_preview;

    private com.example.titancalculator.helper.ButtonShapeCustomAdapter ButtonShapeCustomAdapter;
    private ArrayList<ButtonShapeImageModel> ButtonShapeImageModelArrayList;

    private com.example.titancalculator.helper.ButtonShapeCustomAdapter ButtonFillingCustomAdapter;
    private ArrayList<ButtonShapeImageModel> ButtonFillingImageModelArrayList;

    private int[] formList = new int[]{R.drawable.buttonshape_round, R.drawable.buttonshape_square, R.drawable.buttonshape_circel,R.drawable.buttonshape_ring};
    private String[] formNameList = new String[]{"Round", "Square","Circle","Ring"};
    private int[] füllungList = new int[]{R.drawable.buttonshape_voll, R.drawable.buttonshape_leer};
    private String[] füllungNameList = new String[]{"voll", "leer"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttonshape);

        lv_form = (ListView) findViewById(R.id.lv_form);
        lv_füllung = (ListView) findViewById(R.id.lv_füllung);
        iv_preview = (ImageView) findViewById(R.id.iv_preview);

        ButtonShapeImageModelArrayList = populateFormList();
        if(ButtonShapeImageModelArrayList != null || ButtonShapeImageModelArrayList.size() != 0){
            ButtonFillingCustomAdapter = new ButtonShapeCustomAdapter(this,ButtonShapeImageModelArrayList);
            if(lv_form != null)lv_form.setAdapter(ButtonFillingCustomAdapter);
        }

        ButtonFillingImageModelArrayList = populateFüllungList();
        if(ButtonFillingImageModelArrayList != null || ButtonShapeImageModelArrayList.size() != 0){
            ButtonShapeCustomAdapter = new ButtonShapeCustomAdapter(this,ButtonFillingImageModelArrayList);
            if(lv_füllung != null)lv_füllung.setAdapter(ButtonShapeCustomAdapter);
        }

        /*
        String[] füllungsoptionen = {"leer","voll"};
        ArrayAdapter<String> aa_füllung = new ArrayAdapter<String>(ButtonshapeActivity.this, R.layout.spinner_shift_style, füllungsoptionen){
            float factor_font = 0.5f;
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(0x000000);
                ((TextView) v).setTextSize(20f);
                return v;
            }
        };
        lv_füllung.setAdapter(aa_füllung);
        */

        lv_form.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //Einstellung speichern
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ButtonshapeActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                int ID = ((ButtonShapeImageModel) ((ButtonShapeCustomAdapter) lv_form.getAdapter()).getItem(position)).getID();
                SettingsApplier.setButtonshapeID(ID);
                editor.putInt("buttonshape", ID);
                editor.commit();
                Toast.makeText(ButtonshapeActivity.this,"set shape to:"+getResources().getResourceEntryName(ID), Toast.LENGTH_SHORT).show();
                setPreviewImage();
            }
        });

        lv_füllung.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //Einstellung speichern
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ButtonshapeActivity.this);
                SharedPreferences.Editor editor = preferences.edit();


                String NAME = ((ButtonShapeImageModel) (lv_füllung.getAdapter()).getItem(position)).getName();
                SettingsApplier.setButtonfüllung(NAME);
                editor.putString("buttonfüllung", NAME);
                editor.commit();
                Toast.makeText(ButtonshapeActivity.this,"set filling to:"+NAME, Toast.LENGTH_SHORT).show();
                setPreviewImage();

                /*
                String inh = lv_füllung.getItemAtPosition(position).toString();
                editor.putString("buttonfüllung", inh);
                editor.commit();
                Toast.makeText(ButtonshapeActivity.this,"set füllung to:"+inh, Toast.LENGTH_SHORT).show();
                setPreviewImage();
                 */
            }
        });

    }

    private Drawable generateDrawable(int form, String füllung){
        Drawable d = getDrawable(R.drawable.buttonshape_square);
        d = getDrawable(form);

        SettingsApplier.setColor(ButtonshapeActivity.this,d,0xffc60aff,füllung,true);
        return d;

    }

    private void setPreviewImage(){
        int form = 0;
        String füllung = "voll";
        form =  SettingsApplier.getButtonshapeID();
        füllung = PreferenceManager.getDefaultSharedPreferences(ButtonshapeActivity.this).getString("buttonfüllung","voll");


        Rect imageBounds = iv_preview.getDrawable().getBounds();

        int dr_left = (int) Math.round(imageBounds.left*0.8);
        int dr_right = (int) Math.round(imageBounds.right*0.8);
        int dr_top = (int) Math.round(imageBounds.top*0.8);
        int dr_bottom = (int) Math.round(imageBounds.bottom*0.8);

        Drawable preview = generateDrawable(form,füllung);
        preview.setBounds(dr_left,dr_top,dr_right,dr_bottom);
        iv_preview.setImageDrawable(preview);
    }


    private ArrayList<ButtonShapeImageModel> populateFormList(){
        ArrayList<ButtonShapeImageModel> list = new ArrayList<>();
        for(int i = 0; i < Math.min(formNameList.length,formList.length); i++){
            ButtonShapeImageModel ButtonShapeImageModel = new ButtonShapeImageModel();
            ButtonShapeImageModel.setName(formNameList[i]);
            ButtonShapeImageModel.setImage_drawable(formList[i]);
            list.add(ButtonShapeImageModel);
        }
        return list;
    }

    private ArrayList<ButtonShapeImageModel> populateFüllungList(){
        ArrayList<ButtonShapeImageModel> list = new ArrayList<>();
        for(int i = 0; i < Math.min(füllungList.length,füllungList.length); i++){
            ButtonShapeImageModel ButtonShapeImageModel = new ButtonShapeImageModel();
            ButtonShapeImageModel.setName(füllungNameList[i]);
            ButtonShapeImageModel.setImage_drawable(füllungList[i]);
            list.add(ButtonShapeImageModel);
        }
        return list;
    }

}
