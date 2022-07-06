package com.example.kf150605d.pocketsoccer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import com.example.kf150605d.pocketsoccer.ImageResources;
import com.example.kf150605d.pocketsoccer.R;
import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.User;

public class Player extends Figure {
    private Paint mPaint;
    private Context mContext;
    private Drawable mDrawable;
    private boolean canPlay;
    private static final int CANNOT_PLAY = 140, CAN_PLAY = 250;
    private boolean selected;
    private User mUserPlayer;

    public Player(Drawable drawable, User userPlayer , Context context)
    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
        mDrawable = drawable;
        mContext = context;
        mUserPlayer = userPlayer;
    }

    @Override
    public void paint(Canvas canvas) {
        if(coordinate == null) return;
        mDrawable.setBounds(coordinate);
        if(!canPlay)
        {
            mDrawable.setAlpha(CANNOT_PLAY);
        }
        else
        {
            mDrawable.setAlpha(CAN_PLAY);
        }
        if(selected)
        {
           ImageResources.selectedPlayerDrawable = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.selectplayer, null);
           ImageResources.selectedPlayerDrawable.setBounds(coordinate.left - 15, coordinate.top - 15, coordinate.right + 15, coordinate.bottom + 15);
           ImageResources.selectedPlayerDrawable.draw(canvas);
        }
        mDrawable.draw(canvas);
    }

    @Override
    public synchronized boolean select(float x, float y) {
        return coordinate.contains((int)x, (int)y);
    }

    @Override
    public void step()
    {
        if (velX != 0 && velY != 0) {

            float offsetY;
            float offsetX;
            offsetX = time * velX * dirX;
            offsetY = time * velY * dirY;
            coordinate.left += offsetX;
            coordinate.top += offsetY;
            coordinate.bottom += offsetY;
            coordinate.right += offsetX;
            mOval.updateXandY(coordinate);

            mGameCV.gameInteraction(this);
            velY -= degVelY;
            velX -= degVelX;
            if (velY <= 0) {
                velY = 0;
            }
            if (velX <= 0) {
                velX = 0;
            }
        }
    }

    public void setSelected()
    {
        selected = true;
    }
    public void resetSelected()
    {
        selected = false;
    }

    public void setCanPlayTrue()
    {
        canPlay = true;
    }

    public void setCanPlayFalse()
    {
        canPlay = false;
    }

    public boolean canPlay()
    {
        return canPlay;
    }

    public synchronized void makeMove(float changePosX, int directX, float changePosY, int directY)
    {
        this.velY = changePosY / 100f;
        this.velX = changePosX / 100f;
        this.degVelY = velY * 0.005f;
        this.degVelX = velX * 0.005f;

        this.dirX = directX;
        this.dirY = directY;

        mGameCV.incNumberOfMoves();
        mGameCV.changeTeamPlaying();
    }
}
