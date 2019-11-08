package com.zmh.fd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author skymhzhang on 2019-11-08.
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "WeakerAccess"})
public class FileUtils {
    private final static String TAG = "FileUtils";

    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] childList = file.listFiles();
            if (childList != null) {
                for (File child : childList) {
                    deleteFile(child);
                }
            }
            file.delete();
        } else {
            boolean result = file.delete();
            if (!result) {
                android.util.Log.w(TAG, "deleteFile failed " + file.getAbsolutePath());
            }
        }
    }

    public static boolean write2File(String info, String dest) {
        byte[] data = info.getBytes();
        File file = new File(dest);
        if (file.exists()) {
            deleteFile(file);
        }
        FileOutputStream fos = null;
        try {
            if (!file.createNewFile()) {
                return false;
            }

            fos = new FileOutputStream(file);
            fos.write(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
