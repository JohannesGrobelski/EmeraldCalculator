package com.example.titancalculator;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.titancalculator.helper.PathUtils;

import java.io.File;
import java.net.URISyntaxException;

public class SettingBackgroundimageActivity extends AppCompatActivity {

    Button addPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_backgroundimage2);

        addPath = findViewById(R.id.add_image);

        addPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData(); //The uri with the location of the file

            if(isImageFile(selectedfile)){
                String path = ""; // "/mnt/sdcard/FileName.mp3"
                try {
                    path = PathUtils.getPath(this,selectedfile);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    Toast t =  Toast.makeText(SettingBackgroundimageActivity.this,"invalid File URI:"+path,Toast.LENGTH_LONG);
                    t.show();
                }
                if(path.startsWith("/"))path = path.replaceFirst("/","");

                try {
                    File f = new File(path);
                } catch (Exception e){
                    Toast t =  Toast.makeText(SettingBackgroundimageActivity.this,"Something went wrong:"+path,Toast.LENGTH_LONG);
                    t.show();
                }
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingBackgroundimageActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("backgroundimage", path);
                editor.commit();
            } else{
                Toast t =  Toast.makeText(SettingBackgroundimageActivity.this,"Selected File is not an image",Toast.LENGTH_LONG);
                t.show();
            }


        }
    }

    public boolean isImageFile(Uri uri) {
        ContentResolver cR = this.getContentResolver();
        String mimeType = cR.getType(uri);

        //Toast t =  Toast.makeText(SettingBackgroundimageActivity.this,"Imagetype: "+mimeType,Toast.LENGTH_LONG);
        //t.show();
        return mimeType != null && mimeType.startsWith("image");
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("getRealPathFromURI: ", "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
