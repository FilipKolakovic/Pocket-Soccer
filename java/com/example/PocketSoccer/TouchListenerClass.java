package com.example.kf150605d.pocketsoccer;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.kf150605d.pocketsoccer.view.CV_Game;
import com.example.kf150605d.pocketsoccer.view.Player;

public class TouchListenerClass implements View.OnTouchListener {

    private CV_Game cv_game;
    private GestureListener gestureListener;
    private final GestureDetector gestureDetector;

    public TouchListenerClass(Context context, CV_Game cv_game){
        gestureListener = new TouchListenerClass.GestureListener();
        gestureDetector = new GestureDetector(context, gestureListener);
        this.cv_game = cv_game;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
       return gestureDetector.onTouchEvent(event);
    }

    public void signalFinish() {
        gestureListener.signalFinish();
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener{
        private boolean finished = false;

        public void signalFinish(){
            finished = true;
        }
        private Player selectP;
        public boolean onDown(MotionEvent e){
            if(finished){
                finished = false;
                cv_game.finish();
                return false;
            }
           if(cv_game.continueIfIsGoal()) return false;
            Player touched = cv_game.select(e.getX(), e.getY());
            if(touched != null){
                if(selectP != null && selectP != touched) selectP.resetSelected();
                selectP = touched;
                if(selectP.canPlay() /*&& !selectP.isBot()*/){
                    selectP.setSelected();
                    return true;
                }
            }
            selectP = null;
            return false;
            }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
            if(selectP == null)
            {
                return false;
            }
            int x1 = (int)e1.getX();
            int x2 = (int)e2.getX();
            int y1 = (int)e1.getY();
            int y2 = (int)e2.getY();
            int directX = 1, directY = 1, changePositionX = 0, changePositionY = 0;
            if(y1 > y2){
                changePositionY = y1 - y2;
                directY = -1;
            }
            else{
                changePositionY = y2 - y1;
            }
            if(x1 > x2){
                changePositionX = x1 - x2;
                directX = -1;

            }
            else {
                changePositionX = x2 - x1;
            }
            selectP.makeMove(changePositionX,directX,changePositionY,directY);
            selectP.resetSelected();
            return true;
        }

    }
}
