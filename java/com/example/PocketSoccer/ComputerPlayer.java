package com.example.kf150605d.pocketsoccer;

import com.example.kf150605d.pocketsoccer.view.Ball;
import com.example.kf150605d.pocketsoccer.view.CV_Game;
import com.example.kf150605d.pocketsoccer.view.Figure;
import com.example.kf150605d.pocketsoccer.view.Player;

public class ComputerPlayer extends Thread {
    private static final long SIMULATION_SWIPE_TIME = 49;
    private static final int THROUGH_THRESH = 2;
    private static final float SLOW_DOWN = 0.995f;
    private static final int MOVE_TIME = 3800;
    private static final int THREAD_SLEEP_MAX = 498, THREAD_SLEEP_MIN = (int)(MOVE_TIME * 0.5);
    private boolean onMove, gameIsOn;
    private Figure mBall;
    private Figure[] mFigures;

    public ComputerPlayer(CV_Game game, Figure[] figures, Ball ball){
        onMove = false;
        mFigures = figures;
        mBall = ball;
        gameIsOn = true;

    }

    @Override
    public void run() {
        Figure mCurrentPlayer;

        float destinationX, destinationY;
        while(gameIsOn){
            if(onMove){
                try{
                    Thread.sleep((long) (Math.random() * THREAD_SLEEP_MAX + THREAD_SLEEP_MIN));

                    float minDistance = mFigures[0].distance(mBall);
                    mCurrentPlayer = mFigures[0];
                    for (int i = 1; i < 3; ++i) {
                        if (mFigures[i].distance(mBall) < minDistance) mCurrentPlayer = mFigures[i];
                    }

                    final float velocityY = (mBall.getmOval().getY() - mCurrentPlayer.getmOval().getY()) / 100f;
                    final float velocityX = (mBall.getmOval().getX() - mCurrentPlayer.getmOval().getX()) / 100f;
                    float dt = minDistance / ((float) Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2)));
                    destinationX = (float) (mBall.getmOval().getX() + mBall.getVelX() * dt * Math.pow(SLOW_DOWN, dt));
                    if (destinationX > mCurrentPlayer.getmOval().getX()) destinationX += mBall.getmOval().getR() * THROUGH_THRESH;
                    else if (destinationX < mCurrentPlayer.getmOval().getX()) destinationX -= mBall.getmOval().getR() * THROUGH_THRESH;
                    destinationY = (float) (mBall.getmOval().getY() + mBall.getVelY() * dt * Math.pow(SLOW_DOWN, dt));
                    if (destinationY > mCurrentPlayer.getmOval().getY()) destinationY += mBall.getmOval().getR() * THROUGH_THRESH;
                    else if (destinationY < mCurrentPlayer.getmOval().getY()) destinationY -= mBall.getmOval().getR() * THROUGH_THRESH;

                    Player playerToMove = (Player) mCurrentPlayer;
                    playerToMove.setCanPlayTrue();
                    Thread.sleep(SIMULATION_SWIPE_TIME);
                    playerToMove.setCanPlayFalse();
                    playerToMove.makeMove(destinationX, (int)(destinationX / Math.abs(destinationX)), destinationY, (int)(destinationY / Math.abs(destinationY)));
                }
                catch (InterruptedException e){}
                finishMove();
            }
        }
    }
    public synchronized void finishGame(){
        gameIsOn = false;
    }

    public synchronized void onMove(){

        onMove = true;
    }

    public synchronized void finishMove(){

        onMove = false;
    }


}
