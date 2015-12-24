package com.laimiux.android.rximagefile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.laimiux.android.rximagefile.internal.BitmapUtils;
import com.laimiux.android.rximagefile.internal.Files;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxImageFile {
  private RxImageFile() {
  }

  /**
   * Retrieves byte array of the image that is at most 'x' bytes
   *
   * @param imageFile File
   * @param maxSize
   * @return
   */
  public static Observable<byte[]> atMost(final File imageFile, final int maxSize) {
    return toBytes(imageFile).flatMap(new Func1<byte[], Observable<byte[]>>() {
      @Override public Observable<byte[]> call(byte[] bytes) {
        return atMost(bytes, maxSize);
      }
    });
  }

  public static Observable<byte[]> atMost(final byte[] initial, final int sizeInBytes) {
    return Observable.create(new Observable.OnSubscribe<byte[]>() {
      @Override public void call(Subscriber<? super byte[]> subscriber) {
        try {
          byte[] current = initial;
          long bytesToRemove = current.length - sizeInBytes;
          int times = 0;
          while (bytesToRemove > 0 && !subscriber.isUnsubscribed()) {
            if (times > 3) {
              // Increase by 10% because we might be in infinite loop otherwise
              bytesToRemove *= 1.1f;
            }

            current = removeBytes(current, bytesToRemove);
            bytesToRemove = current.length - sizeInBytes;
            times += 1;
          }

          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(current);
            subscriber.onCompleted();
          }

        } catch (IOException e) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onError(e);
          }
        }
      }
    });
  }

  public static Observable<byte[]> toBytes(final File imageFile) {
    return Observable.defer(new Func0<Observable<byte[]>>() {
      @Override public Observable<byte[]> call() {
        try {
          final byte[] bytes = Files.toByteArray(imageFile);
          return Observable.just(bytes);
        } catch (IOException e) {
          return Observable.error(e);
        }
      }
    }).subscribeOn(Schedulers.io());
  }

  private static byte[] removeBytes(byte[] originalImageBytes, long bytesToRemove) throws IOException, OutOfMemoryError {
    Bitmap bitmap = BitmapFactory.decodeByteArray(originalImageBytes, 0, originalImageBytes.length);
    int pixelsToRemove = (int) (bytesToRemove / BitmapUtils.getBytesPerPixel(bitmap.getConfig()));

    final int currentHeight = bitmap.getHeight();
    final int currentWidth = bitmap.getWidth();

    float aspectRatio = (float) currentWidth / (float) currentHeight;

    final double totalPixelCount = (double) (currentHeight * currentWidth);
    final double newPixelCount = totalPixelCount - pixelsToRemove;


    final int targetHeight = (int) Math.sqrt(newPixelCount / aspectRatio);
    final int targetWidth = (int) (aspectRatio * targetHeight);

    Bitmap resizedImage = BitmapUtils.getResizedBitmap(bitmap, targetHeight, targetWidth);

    if (resizedImage != bitmap) {
      bitmap.recycle();
    }

    ByteArrayOutputStream stream = new ByteArrayOutputStream(resizedImage.getByteCount());
    resizedImage.compress(Bitmap.CompressFormat.JPEG, 90, stream);
    final byte[] imageBytes = stream.toByteArray();

    // Recycle used image
    resizedImage.recycle();


    return imageBytes;
  }

}
