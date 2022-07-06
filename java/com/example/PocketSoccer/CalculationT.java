package com.example.kf150605d.pocketsoccer;

import android.graphics.Rect;
import android.os.AsyncTask;

import com.example.kf150605d.pocketsoccer.view.CV_Game;
import com.example.kf150605d.pocketsoccer.view.Figure;

public class CalculationT extends Thread {
    private static class Finisher extends AsyncTask<Void, Void, Void>{

        private CalculationT mThread;
        public Finisher(CalculationT thread){
            mThread = thread;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                mThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            mThread.mCVgame.finishTouchListener();
        }
    }


    private CV_Game mCVgame;
    private boolean isGoal;
    boolean isFinished;
    private Rect canvasR;
    private static final long sleepTime = 12;
    private Figure[] myFigures;


    public CalculationT(CV_Game mCVgame, Figure[] myFigures, Rect canvasR){
        this.mCVgame = mCVgame;
        this.myFigures = myFigures;
        this.canvasR = canvasR;
    }


    @Override
    public void run() {
        boolean figuresNotSet = true;
        while(true){
            try {
                synchronized (this) {
                    while(isGoal) {
                        wait();
                        figuresNotSet = true;
                    }
                    if(isFinished){
                        new Finisher(this).execute();
                        break;
                    }
                }
                if(figuresNotSet) {
                    if (canvasR.right > 0) {
                        mCVgame.setInitCoordinates();
                        figuresNotSet = false;
                    }
                }

                for(Figure figure : myFigures){
                    figure.step();
                }
                mCVgame.checkIfFinished();
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                break;
            }
            mCVgame.invalidate();
        }
    }

    public synchronized void setGoal() { isGoal = true; }
    public synchronized void finishThread() {isFinished = true;}
    public synchronized void resetGoal() { isGoal = false; notify(); }

}
