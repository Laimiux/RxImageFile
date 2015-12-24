package com.laimiux.android.rximagefile;


import android.test.AndroidTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class RxImageFileTest {
  @Test
  public void addition_isCorrect() throws Exception {
    assertEquals(4, 2 + 2);
  }


  @Test
  public void atMost_bigFiles() {
    URL url = this.getClass().getResource("/img_1.jpg");
    File first = new File(url.getFile());
    final int maxSize = 50000;
    final byte[] result = RxImageFile.atMost(first, maxSize).toBlocking().first();
    assertTrue(result.length < maxSize);
  }
}