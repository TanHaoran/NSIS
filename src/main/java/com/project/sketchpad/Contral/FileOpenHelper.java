package com.project.sketchpad.Contral;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.jerry.nsis.utils.L;
import com.project.sketchpad.entity.Picture;

public class FileOpenHelper {

    public static final String PICTURE_DIR = "/NSIS_Board/";

    private static final int MAX_NUMBER = 100;


    /**
     * 取得指定目录下所有的图片
     *
     * @return
     */
    public static List<Picture> getPictures() {
        List<Picture> pictureList = new ArrayList<>();
        String strDir = getPicturePath();
        File dir = new File(strDir);
        File[] files = dir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String name = files[i].getName();
                String path = files[i].getAbsolutePath();
                Picture picture = new Picture(name, path);
                pictureList.add(picture);
            }
        }
        Collections.sort(pictureList, new Comparator<Picture>() {
            @Override
            public int compare(Picture lhs, Picture rhs) {
                String left = lhs.getName();
                String right = rhs.getName();
                int length = Math.min(left.length(), right.length());
                for (int i = 0; i < length; i++) {
                    if (left.charAt(i) < right.charAt(i)) {
                        return 1;
                    } else if (left.charAt(i) > right.charAt(i)) {
                        return -1;
                    } else {
                        continue;
                    }
                }
                return -1;
            }
        });
        return pictureList;
    }




    /**
     * 取得指定目录下所有的图片
     *
     * @return
     */
    public static Bitmap[] getPictureBitmaps() {
        String strDir = getPicturePath();
        Bitmap[] bitmaps = new Bitmap[MAX_NUMBER];
        File dir = new File(strDir);
        File[] files = dir.listFiles();
        int max = (files.length > MAX_NUMBER) ? MAX_NUMBER : files.length;
        for (int i = 0; i < max; i++) {
            if (files[i].exists()) {
                bitmaps[i] = BitmapUtil.loadBitmapFromSDCard(files[i].getPath());
            }
        }
        return bitmaps;
    }

    /**
     * 取得指定目录下所有文件的名称
     *
     * @return
     */
    public static String[] getPictureNames() {
        String strDir = getPicturePath();
        String[] pictureNames = new String[MAX_NUMBER];
        File dir = new File(strDir);
        File[] files = dir.listFiles();
        int max = (files.length > MAX_NUMBER) ? MAX_NUMBER : files.length;
        for (int i = 0; i < max; i++) {
            if (files[i].exists()) {
                L.e("FileName：" + files[i].getName());
                String str = files[i].getName();
                pictureNames[i] = str;
            }
        }
        return pictureNames;
    }

    /**
     * 取得指定目录的路径
     *
     * @return 路径
     */
    public static String getPicturePath() {
        File sdcardDir = Environment.getExternalStorageDirectory();
        String strDir = sdcardDir.getPath() + PICTURE_DIR;
        File file = new File(strDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return strDir;
    }
}

