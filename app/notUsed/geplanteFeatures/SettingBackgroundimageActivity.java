package com.example.calcitecalculator.geplanteFeatures;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import com.example.calcitecalculator.R;
import com.example.calcitecalculator.geplanteFeatures.notUsedHelper.PathUtils;

import java.io.File;
import java.net.URISyntaxException;

public class SettingBackgroundimageActivity extends AppCompatActivity {

    Button addPath;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_backgroundimage2);

        ActivityCompat.requestPermissions(SettingBackgroundimageActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        if (ContextCompat.checkSelfPermission(SettingBackgroundimageActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
             // Permission is not granted
            ActivityCompat.requestPermissions(SettingBackgroundimageActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            Toast.makeText(SettingBackgroundimageActivity.this, "The app needs permission to access storage to find the image.", Toast.LENGTH_LONG).show();
        }


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
                    if(path != null) {
                        if (path.startsWith("/")) path = path.replaceFirst("/", "");
                        try {
                            File f = new File(path);
                        } catch (Exception e) {
                           Toast.makeText(SettingBackgroundimageActivity.this, "Something went wrong:" + path, Toast.LENGTH_LONG).show();
                        }
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingBackgroundimageActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("backgroundimage", path);
                        editor.commit();
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    Toast t =  Toast.makeText(SettingBackgroundimageActivity.this,"invalid File URI:"+path,Toast.LENGTH_LONG);
                    t.show();
                }


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
