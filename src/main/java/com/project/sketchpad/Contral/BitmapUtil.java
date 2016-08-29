package com.project.sketchpad.Contral;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.jerry.nsis.utils.L;
import com.jerry.nsis.utils.T;
import com.project.sketchpad.view.BoardView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

public class BitmapUtil {
    @SuppressWarnings("unused")
    private BoardView canvasView;


    /**
     * 对图片生成一备份
     *
     * @param bmpSrc
     * @return
     */
    public static Bitmap copyBitmap(Bitmap bmpSrc) {
        if (bmpSrc == null) {
            return null;
        }
        int width = bmpSrc.getWidth();
        int height = bmpSrc.getHeight();

        Bitmap bmpDest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        if (bmpDest != null) {
            Canvas canvas = new Canvas(bmpDest);
            final Rect rect = new Rect(0, 0, width, height);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bmpSrc, rect, rect, null);
        }
        return bmpDest;
    }

    /**
     * 位图和字节数组的相互转换，便于存储于读取
     *
     * @param array
     * @return
     */
    public static Bitmap byteArrayToBitmap(byte[] array) {
        if (null == array) {
            return null;
        }

        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    public static byte[] bitampToByteArray(Bitmap bitmap) {
        byte[] array = null;
        try {
            if (null != bitmap) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                array = os.toByteArray();
                os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return array;
    }

    /**
     * 将图片保存到SDCard
     *
     * @param bmp
     * @param strPath
     */
    public static void saveBitmapToSDCard(Bitmap bmp, String strPath) {
        if (null != bmp && null != strPath && !strPath.equalsIgnoreCase("")) {
            try {
                L.i("文件路径：" + strPath);
                File file = new File(strPath);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = BitmapUtil.bitampToByteArray(bmp);
                fos.write(buffer);
                fos.close();
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从sdcard读取图片
     *
     * @param strPath
     * @return
     */
    public static Bitmap loadBitmapFromSDCard(String strPath) {
        File file = new File(strPath);

        try {
            FileInputStream fis = new FileInputStream(file);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 2;   //width，hight设为原来的二分一
            Bitmap btp = BitmapFactory.decodeStream(fis, null, options);
            return btp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
