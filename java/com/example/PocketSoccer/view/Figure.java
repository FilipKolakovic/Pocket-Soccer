package com.example.kf150605d.pocketsoccer.view;

import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.HashMap;

public abstract class Figure {

    private static int staticId = 0;
    private static final int mass = 65;
    private static HashMap<String,Float> mHashMap;
    private int id;
    protected Oval mOval;
    protected int width;
    protected float dirX, dirY, velX, velY, degVelX, degVelY;
    protected int time;
    protected CV_Game mGameCV;
    protected Rect coordinate;


    public Figure(){
        id = ++staticId;
        mHashMap = new HashMap<>(15);
    }

    public void setInitCoordinate(Rect coordinate){
        this.coordinate = new Rect(coordinate);
        this.width = coordinate.width();
        mOval = new Oval(coordinate.exactCenterX(), coordinate.exactCenterY(), width / 2);
    }

    public void changeDirX(){
        dirX *= -1;
    }

    public void changeDirY(){
        dirY *= -1;
    }

    public float distance(Figure figure) {
        return (float)Math.sqrt(Math.pow(figure.mOval.getX() - mOval.getX(), 2) + Math.pow(figure.mOval.getY() - mOval.getY(), 2));
    }

    public void checkEdgeCollision(Rect canvasRect) {
        if(coordinate.left < canvasRect.left )
        {
            coordinate.left = canvasRect.left;
            changeDirX();
        }
        else if (coordinate.right > canvasRect.right)
        {
            coordinate.right = canvasRect.right;
            changeDirX();
        }
        else if(coordinate.top < canvasRect.top)
        {
            coordinate.top = canvasRect.top;
            changeDirY();
        }
        else if(coordinate.bottom > canvasRect.bottom)
        {
            coordinate.bottom = canvasRect.bottom;
            changeDirY();
        }
              if(coordinate.right - coordinate.left < width){ coordinate.right = coordinate.left + width; } // proveriti
              if(coordinate.bottom - coordinate.top < width){ coordinate.bottom = coordinate.top + width; } // proveriti
    }

    public void setDirection(double directionX, double directionY){
        if(directionX < 0){
            dirX = -1;
            directionX = -directionX;
        } else dirX = 1;
        if(directionY < 0){
            dirY = -1;
            directionY = -directionY;
        } else dirY = 1;

        velX = (float)directionX / 2f;
        velY = (float)directionY / 2f;
        degVelX = velX * 0.005f;
        degVelY = velY * 0.005f;
    }

    public void setHash(String impact, float distance){

        Figure.mHashMap.put(impact, distance);
    }

    public float getHashOldValue(String impact){
        Float ret = Figure.mHashMap.get(impact);
        if(ret==null) return 0;
        return ret;
    }

    public void resetAllParam(){
        velY = 0;
        velX = 0;
        degVelX = 0;
        degVelY = 0;

        mHashMap.clear();  // proveriti
    }

    public double getAngle(){
        double mX = velX * dirX;
        double mY = velY * dirY;
        if(mX==0 && mY==0) return 0;

        return Math.atan(mY / mX);
    }

    public static int getMass() {
        return mass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static HashMap<String, Float> getmHashMap() {
        return mHashMap;
    }

    public Rect getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Rect coordinate) {
        this.coordinate = coordinate;
    }

    public Oval getmOval() {
        return mOval;
    }

    public void setmOval(Oval mOval) {
        this.mOval = mOval;
    }

    public float getDirX() {
        return dirX;
    }

    public void setDirX(float dirX) {
        this.dirX = dirX;
    }

    public float getDirY() {
        return dirY;
    }

    public void setDirY(float dirY) {
        this.dirY = dirY;
    }

    public float getVelX() {
        return velX * dirX;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public float getVelY() {
        return velY * dirY;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time / 10;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(int width) { this.width = width; }

    public CV_Game getmGameCV() {
        return mGameCV;
    }

    public void setmGameCV(CV_Game mGameCV) {
        this.mGameCV = mGameCV;
    }



    public abstract void paint(Canvas canvas);

    public abstract boolean select(float x, float y);

    public abstract void step();


}
