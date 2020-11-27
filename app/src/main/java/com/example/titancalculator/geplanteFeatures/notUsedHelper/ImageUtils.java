package com.example.titancalculator.geplanteFeatures.notUsedHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;

public class ImageUtils {
    private static final int[] FROM_COLOR = new int[]{49, 179, 110};
    private static final int THRESHOLD = 3;

    /** simply resizes a given drawable resource to the given width and height */
    public static Drawable resizeImage(Context ctx, int resId, int iconWidth,
                                       int iconHeight) {

        // load the origial Bitmap
        //Bitmap BitmapOrg = BitmapFactory.decodeResource(ctx.getResources(),resId);
        Bitmap BitmapOrg = getBitmap(ctx,resId,iconWidth,iconHeight);

        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = iconWidth;
        int newHeight = iconHeight;

        // calculate the scale
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the Bitmap
        matrix.postScale(scaleWidth, scaleHeight);

        // if you want to rotate the Bitmap
        // matrix.postRotate(45);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);

        // make a Drawable from Bitmap to allow to set the Bitmap
        // to the ImageView, ImageButton or what ever
        return new BitmapDrawable(BitmapOrg);

    }

    private static Bitmap getBitmap(Context context,int drawableRes, int width, int height) {
        Drawable drawable = context.getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

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
