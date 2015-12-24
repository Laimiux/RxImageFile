package com.laimiux.android.rximagefile.internal;

/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class Files {
    private Files() {
    }


    /**
     * Reads all bytes from a file into a byte array.
     *
     * @param file the file to read from
     * @return a byte array containing all the bytes from file
     * @throws IllegalArgumentException if the file is bigger than the largest
     *                                  possible byte array (2^31 - 1)
     * @throws IOException              if an I/O error occurs
     */
    public static byte[] toByteArray(File file) throws IOException {
        FileInputStream fileInputStream = null;

        byte[] fileByteArray = new byte[(int) file.length()];

        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileByteArray);
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        }

        return fileByteArray;
    }
}

