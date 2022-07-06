package com.example.kf150605d.pocketsoccer.view;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

public class Ball extends Figure {
    private Drawable mPictureDrawable;

    public Ball(Drawable drawable){

        mPictureDrawable = drawable;
    }

    @Override
    public void paint(Canvas canvas) {
        if (coordinate != null) {
            mPictureDrawable.setBounds(coordinate);
            mPictureDrawable.draw(canvas);
        }
    }

    @Override
    public boolean select(float x, float y) {
        return false;
    }

    @Override
    public synchronized void step() {
        if (velX != 0 && velY != 0) {

            float xOffset;
            float yOffset;

            yOffset = dirY * velY * time;
            xOffset = dirX * velX * time;
            coordinate.top += yOffset;
            coordinate.left += xOffset;
            coordinate.bottom += yOffset;
            coordinate.right += xOffset;
            mOval.updateXandY(coordinate);

            mGameCV.gameInteraction(this);

            velY -= degVelY;
            velX -= degVelX;
            if (velX < 0) velX = 0;
            if (velY < 0) velY = 0;
        }
    }
}
