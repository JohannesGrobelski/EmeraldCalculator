package com.example.titancalculator.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;

import com.example.titancalculator.CalcActivity_science;

public class ImageUtils {
    private static final int[] FROM_COLOR = new int[]{49, 179, 110};
    private static final int THRESHOLD = 3;


    public static Drawable adjust(Context c, VectorDrawable d){
        int to = Color.RED;
        //Need to copy to ensure that the bitmap is mutable.
        Bitmap src = (vectorToBitmap(c,d)).getBitmap();
        Bitmap bitmap = src.copy(Bitmap.Config.ARGB_8888, true);
        for(int x = 0;x < bitmap.getWidth();x++)
            for(int y = 0;y < bitmap.getHeight();y++)
                if(match(bitmap.getPixel(x, y)))
                    bitmap.setPixel(x, y, to);
        return new BitmapDrawable(bitmap);
    }
    public static boolean match(int pixel)
    {
        //There may be a better way to match, but I wanted to do a comparison ignoring
        //transparency, so I couldn't just do a direct integer compare.
        return Math.abs(Color.red(pixel) - FROM_COLOR[0]) < THRESHOLD &&
                Math.abs(Color.green(pixel) - FROM_COLOR[1]) < THRESHOLD &&
                Math.abs(Color.blue(pixel) - FROM_COLOR[2]) < THRESHOLD;
    }
    public static android.graphics.drawable.BitmapDrawable vectorToBitmap(Context c, VectorDrawable drawable){
        try {
            Bitmap bitmap;
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return new BitmapDrawable(c.getResources(), bitmap);
        } catch (OutOfMemoryError e) {
            // Handle the error
            return null;
        }
    }

}
