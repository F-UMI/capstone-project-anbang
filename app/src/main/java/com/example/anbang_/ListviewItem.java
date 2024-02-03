package com.example.anbang_;

import android.graphics.drawable.Drawable;

public class ListviewItem {
    private Drawable drawable;
    private String text;

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }
}
