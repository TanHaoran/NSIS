package com.project.sketchpad.entity;

import android.graphics.Bitmap;

/**
 * Created by Jerry on 2016/4/21.
 */
public class Picture {
    private String name;
    private String path;

    public Picture() {

    }

    public Picture(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
