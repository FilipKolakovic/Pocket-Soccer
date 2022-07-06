package com.example.kf150605d.pocketsoccer.view;

import android.graphics.Rect;

public class Oval {

    private float x, y, r;

    public Oval(float xx, float yy, float rr)
    {
        x = xx;
        y = yy;
        r = rr;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }


    public void updateCoordinate(Rect coordinate) {
        coordinate.left = (int)x - (int)r;
        coordinate.right = (int)x + (int)r;
        coordinate.top = (int)y - (int)r;
        coordinate.bottom = (int)y + (int)r;
    }

    public boolean intersected(Oval figure){
        return ((float)Math.sqrt(Math.pow(figure.x - x, 2) + Math.pow(figure.y - y, 2))) < (r + figure.r);
    }


    public void updateXandY(Rect coordinate){
        x = coordinate.exactCenterX();
        y = coordinate.exactCenterY();
    }


}
