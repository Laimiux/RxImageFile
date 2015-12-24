package com.laimiux.android.rximagefile.internal;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapUtils {
  /**
   * A helper function to return the byte usage per pixel of a bitmap based on its configuration.
   */
  public static int getBytesPerPixel(Bitmap.Config config) {
    if (config == Bitmap.Config.ARGB_8888) {
      return 4;
    } else if (config == Bitmap.Config.RGB_565) {
      return 2;
    } else if (config == Bitmap.Config.ARGB_4444) {
      return 2;
    } else if (config == Bitmap.Config.ALPHA_8) {
      return 1;
    }
    return 1;
  }

  public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
    int width = bm.getWidth();
    int height = bm.getHeight();
    float scaleWidth = ((float) newWidth) / width;
    float scaleHeight = ((float) newHeight) / height;
    // CREATE A MATRIX FOR THE MANIPULATION
    Matrix matrix = new Matrix();
    // RESIZE THE BIT MAP
    matrix.postScale(scaleWidth, scaleHeight);

    // "RECREATE" THE NEW BITMAP
    return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
  }
}
