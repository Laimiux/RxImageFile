package com.laimiux.android.rximagefile;

import android.test.InstrumentationTestCase;

import org.junit.Assert;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class RxImageFileTest extends InstrumentationTestCase {

  public static final String BIG_IMAGE = "img_2.jpg";
  public static final String SMALL_IMAGE = "img_1.jpg";

  public void test_atMost_smallImageInHalf() throws IOException {
    reduceSize(SMALL_IMAGE, 2);
  }

  public void test_atMost_bigImageInHalf() throws IOException {
    reduceSize(BIG_IMAGE, 2);
  }

  public void test_atMost_tenthOfBigImage() throws IOException {
    reduceSize(BIG_IMAGE, 10);
  }

  public void reduceSize(String resourceName, int divider) throws IOException {
    final byte[] resource = open(resourceName);

    // Cut in half
    final int maxSize = resource.length / divider;
    final byte[] result = RxImageFile.atMost(resource, maxSize).toBlocking().first();
    Assert.assertTrue(result.length < maxSize);
  }


  private byte[] open(String resourceName) throws IOException {
    InputStream is = null;
    try {
      is = getInstrumentation().getContext().getAssets().open(resourceName);
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();

      int nRead;
      byte[] data = new byte[is.available()];

      while ((nRead = is.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
      }

      buffer.flush();


      return buffer.toByteArray();
    } finally {
      if (is != null) {
        is.close();
      }
    }
  }
}
