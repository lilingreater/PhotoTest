package com.lilin.test.phototest;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/photo/";

    /**
     * 保存bitmap到本地
     * @param bitmap
     * @return
     */
    public static String saveBitmapFile(Bitmap bitmap) {
        String fileName = String.valueOf(System.currentTimeMillis());
        String imageFilePath = null;
        File file = null;
        try {
            imageFilePath = createSDDir("temp").getPath() + "/" + fileName + ".JPEG";
            file = new File(imageFilePath);//将要保存图片的路径
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    /**
     * 创建目录
     * @param dirName
     * @return
     * @throws IOException
     */
    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(SDPATH + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return dir;
    }

}
