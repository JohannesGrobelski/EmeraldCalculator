package com.example.titancalculator;

import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.helper.ButtonShapeCustomAdapter;
import com.example.titancalculator.helper.ButtonShapeImageModel;

import java.util.ArrayList;

public class ButtonshapeActivity extends AppCompatActivity {

    private ListView lv_form;
    private ListView lv_füllung;
    private ImageView iv_preview;

    private ButtonShapeCustomAdapter ButtonShapeCustomAdapter;
    private ArrayList<ButtonShapeImageModel> ButtonShapeImageModelArrayList;
    private int[] myImageList = new int[]{R.drawable.buttonshape_round, R.drawable.buttonshape_square};
    private String[] myImageNameList = new String[]{"Round", "Square"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttonshape);

        lv_form = (ListView) findViewById(R.id.lv_form);
        lv_füllung = (ListView) findViewById(R.id.lv_füllung);
        iv_preview = (ImageView) findViewById(R.id.iv_preview);

        ButtonShapeImageModelArrayList = populateFormList();
        if(ButtonShapeImageModelArrayList != null || ButtonShapeImageModelArrayList.size() != 0){
            ButtonShapeCustomAdapter = new ButtonShapeCustomAdapter(this,ButtonShapeImageModelArrayList);
            if(lv_form != null)lv_form.setAdapter(ButtonShapeCustomAdapter);
        }

        String[] füllungsoptionen = {"leer","voll"};
        ArrayAdapter<String> aa_füllung = new ArrayAdapter<String>(this,R.layout.lvitem_layout, füllungsoptionen);
        lv_füllung.setAdapter(aa_füllung);

        lv_form.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //Einstellung speichern
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ButtonshapeActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                String inh = ((ButtonShapeImageModel) ((ButtonShapeCustomAdapter) lv_form.getAdapter()).getItem(position)).getName();
                editor.putString("buttonshape", inh);
                editor.commit();
                Toast.makeText(ButtonshapeActivity.this,"set shape to:"+inh,Toast.LENGTH_SHORT).show();
                setPreviewImage();
            }
        });

        lv_füllung.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //Einstellung speichern
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ButtonshapeActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                String inh = lv_füllung.getItemAtPosition(position).toString();
                editor.putString("buttonfüllung", inh);
                editor.commit();
                Toast.makeText(ButtonshapeActivity.this,"set füllung to:"+inh,Toast.LENGTH_SHORT).show();
                setPreviewImage();
            }
        });

    }

    private Drawable generateDrawable(String form, String füllung){
        Drawable d = getDrawable(R.drawable.buttonshape_square);
        if(form.equals("Round")){
            d = getDrawable(R.drawable.buttonshape_round);
        } else if(form.equals("Square")){
            d = getDrawable(R.drawable.buttonshape_square);
        }

        CalcActivity_normal.setColor(d,0xffc60aff,füllung,true);

        return d;

    }

    private void setPreviewImage(){
        String form = "";
        String füllung = "";
        form = PreferenceManager.getDefaultSharedPreferences(ButtonshapeActivity.this).getString("buttonshape","Round");
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

        for(int i = 0; i < Math.min(myImageNameList.length,myImageList.length); i++){
            ButtonShapeImageModel ButtonShapeImageModel = new ButtonShapeImageModel();
            ButtonShapeImageModel.setName(myImageNameList[i]);
            ButtonShapeImageModel.setImage_drawable(myImageList[i]);
            list.add(ButtonShapeImageModel);
        }

        return list;

    }
}
