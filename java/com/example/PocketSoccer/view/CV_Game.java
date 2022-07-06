package com.example.kf150605d.pocketsoccer.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.example.kf150605d.pocketsoccer.ComputerPlayer;
import com.example.kf150605d.pocketsoccer.CalculationT;
import com.example.kf150605d.pocketsoccer.Game;
import com.example.kf150605d.pocketsoccer.ImageResources;
import com.example.kf150605d.pocketsoccer.R;
import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.Match;
import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.User;

import static android.content.Context.MODE_PRIVATE;
import static com.example.kf150605d.pocketsoccer.Settings.MY_PREFS_NAME;

public class CV_Game extends AppCompatImageView {
    private static class MoveChecker extends AsyncTask<Void, Void, Void> {
        private CV_Game mGame;
        private long mMoves;

        public MoveChecker(CV_Game game, long moves) {
            mGame = game;
            mMoves = moves;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            final int moveSlice = 8;
            for (int i = 0; i < moveSlice; ++i) {
                try {
                    Thread.sleep(MOVE_TIME / moveSlice);
                    if (isCancelled()) break;
                } catch (InterruptedException e) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (!isCancelled()
                    && mMoves == mGame.numberOfMov
                    && !mGame.isGoal) {
                mGame.changeTeamPlaying();
            }
        }
    }

    private float goalPostUp, goalPostDown, goalPostLeft, goalPostRight;
    private Drawable backgroundP;
    private Drawable goalsP;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Rect canvasR;
    private Figure[] figures;
    private CalculationT calculationT;
    private Thread timer;
    private ComputerPlayer pOneBot, pTwoBot;
    private MoveChecker mMoveTimer;
    private int isPlaying;
    private long numberOfMov;
    private boolean isGoal;
    private boolean finished, timerGamePause;
    private String teren, tim1, tim2;
    private int brzinaIgre, uslovZavrsetka;
    private ImageResources imageResources;
    private Game mGame;
    private Match mMatch;
    private User mUser1, mUser2;
    private String player1Name, player2Name;
    private static final String gLftUp = "LeftUp";
    private static final String gLftDwn = "LeftDown";
    private static final String gRghtUp = "RightUp";
    private static final String gRghtDwn = "RightDown";
    private static final String customFontPath = "fonts/Lato-Bold.ttf";
    private static final Paint scoreBoardDataPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final int MAX_GOALS = 4, MOVE_TIME = 3800;


    public CV_Game(Context context) {
        super(context);
        initView();
    }

    public CV_Game(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CV_Game(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        SharedPreferences sp = getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        teren = sp.getString("teren", "Trava");
        uslovZavrsetka = sp.getInt("uslovZavrsetka",1);
        brzinaIgre = sp.getInt("brzinaIgre", 30);
        tim1 = sp.getString("tim1", "Canada");
        tim2 = sp.getString("tim2", "China" );
        player1Name = sp.getString("player1Name", "Dobrivoje");
        player2Name = sp.getString("player2Name", "Zivan");
        scoreBoardDataPaint.setTextSize(50);
        scoreBoardDataPaint.setColor(Color.WHITE);
        mPaint.setTextSize(190);
        mPaint.setColor(Color.BLUE);

        Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(),  customFontPath);
        scoreBoardDataPaint.setTypeface(custom_font);

        canvasR = new Rect();
        if(goalsP == null)
        {
            goalsP = ResourcesCompat.getDrawable(getResources(), R.drawable.golovi,null);
        }
        if(backgroundP == null)
        {
            switch (teren)
            {
                case "Trava":
                    backgroundP = ResourcesCompat.getDrawable(getResources(), R.drawable.trava, null);
                    break;
                case "Beton":
                    backgroundP = ResourcesCompat.getDrawable(getResources(), R.drawable.beton2 , null);
                    break;
                case "Parket":
                    backgroundP = ResourcesCompat.getDrawable(getResources(), R.drawable.parket, null);
                    break;
            }
        }
    }


    public void setInitCoordinates(){
        figures[0].setInitCoordinate(new Rect(450, 150, 600,300));
        figures[1].setInitCoordinate(new Rect(630, 360,780, 510));
        figures[2].setInitCoordinate(new Rect(450, 560,600, 710));
        figures[3].setInitCoordinate(new Rect(canvasR.right - 600, 150, canvasR.right - 450,300));
        figures[4].setInitCoordinate(new Rect(canvasR.right - 780, 360, canvasR.right - 630, 510));
        figures[5].setInitCoordinate(new Rect(canvasR.right - 600, 560, canvasR.right - 450, 710));

        figures[6].setInitCoordinate(new Rect(canvasR.right / 2 - 50, canvasR.bottom / 2 - 50, canvasR.right / 2 + 50, canvasR.bottom / 2 + 50));


        goalPostLeft = canvasR.width() * 0.0625f;
        goalPostRight = canvasR.width() * 0.9375f;
        goalPostUp = canvasR.height() * 0.354167f;
        goalPostDown = canvasR.height() * 0.64583f;

    }

    private void instantFig() {

        figures = new Figure[7];
        imageResources = new ImageResources(getContext());
        Drawable player1team = imageResources.getPictureOfTeam(tim1);
        Drawable player2team = imageResources.getPictureOfTeam(tim2);

        figures[0] = new Player(player1team, mUser1, getContext());
        figures[1] = new Player(player1team, mUser1, getContext());
        figures[2] = new Player(player1team, mUser1, getContext());
        figures[3] = new Player(player2team, mUser2, getContext());
        figures[4] = new Player(player2team, mUser2, getContext());
        figures[5] = new Player(player2team, mUser2, getContext());

        figures[6] = new Ball(getResources().getDrawable(ImageResources.ballDrawable));

        for(Figure figure: figures){
            figure.setTime(brzinaIgre);
            figure.setmGameCV(this);
        }
    }

    private Thread createGameTimer(){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                long timeExist = mMatch.getTimeLeft();
                while(timeExist > 1){
                    try {
                        Thread.sleep(1000);
                        while(timerGamePause || isGoal);
                    } catch (InterruptedException e) {}
                    mMatch.setTimeLeft(--timeExist);
                }
                finished = true;
            }
        });
    }

    public Player select(float x, float y) {
        for(int i = 0; i < figures.length - 1; i++){
            if(figures[i].select(x, y)) {
                return (Player) figures[i];
            }
        }
        return null;
    }

    public void gameInteraction(Figure playerObj) {
        Oval playerO = playerObj.getmOval();
        Rect player = playerObj.getCoordinate();

        for(Figure figure : figures){
            if(figure == playerObj) continue;
            Rect other = figure.getCoordinate();
            Oval otherO = figure.getmOval();

            if(playerO.intersected(otherO)){
                float distXVector = otherO.getX() - playerO.getX();
                float distYVector = otherO.getY() - playerO.getY();
                float distSqrtVector = (float)Math.sqrt(Math.pow(distXVector, 2) + Math.pow(distYVector, 2));
                float minDistance = playerO.getR() + otherO.getR();

                float hashedDistance = playerObj.getHashOldValue(figure.toString());
                playerObj.setHash(figure.toString(), distSqrtVector);
                figure.setHash(playerObj.toString(), distSqrtVector);

                if(hashedDistance < distSqrtVector){
                    float distanceCorrection = (minDistance - distSqrtVector) / distSqrtVector;
                    float correctionVectorX = distXVector * distanceCorrection;
                    float correctionVectorY = distYVector * distanceCorrection;

                    otherO.setX(otherO.getX() + correctionVectorX);
                    otherO.setY(otherO.getY() + correctionVectorY);
                    playerO.setX(playerO.getX() - correctionVectorX);
                    playerO.setY(playerO.getY() - correctionVectorY);

                    double angle = Math.atan((double)distYVector / (double)distXVector);
                    double sinAngle = Math.sin(angle);
                    double cosAngle = Math.cos(angle);

                    double mAngle = playerObj.getAngle();
                    double oAngle = figure.getAngle();

                    double mCosAngle = Math.cos(mAngle - angle);
                    double mSinAngle = Math.sin(mAngle - angle);
                    double oCosAngle = Math.cos(angle - oAngle);
                    double oSinAngle = Math.sin(angle - oAngle);

                    double mXVel = playerObj.velX * playerObj.dirX;
                    double mYVel = playerObj.velY * playerObj.dirY;
                    double oXVel = figure.velX * figure.dirX;
                    double oYVel = figure.velY * figure.dirY;

                    double mMass = playerObj.getMass();
                    double oMass = figure.getMass();

                    double v1YTemp = (mYVel * mCosAngle * (mMass - oMass) + 2 * oMass * oYVel * oCosAngle) * sinAngle / (mMass + oMass);
                    double v1XTemp = (mXVel * mCosAngle * (mMass - oMass) + 2 * oMass * oXVel * oCosAngle) * cosAngle / (mMass + oMass);
                    double v1TempY = mYVel * mSinAngle;
                    double v1TempX = mXVel * mSinAngle;

                    double v2YTemp = (oYVel * oCosAngle * (mMass - oMass) + 2 * mMass * mYVel * mCosAngle) * sinAngle / (mMass + oMass);
                    double v2XTemp = (oXVel * oCosAngle * (mMass - oMass) + 2 * mMass * mXVel * mCosAngle) * cosAngle / (mMass + oMass);
                    double v2TempY = oYVel * oSinAngle;
                    double v2TempX = oXVel * oSinAngle;

                    playerO.updateCoordinate(player);
                    otherO.updateCoordinate(other);

                    playerObj.setDirection(v1XTemp + v1TempX * sinAngle, v1YTemp + v1TempY * cosAngle);
                    figure.setDirection(v2XTemp + v2TempX * sinAngle, v2YTemp + v2TempY * cosAngle);
                    goalCollision(playerObj);
                    goalCollision(figure);

                    if(playerObj instanceof Ball || figure instanceof Ball){
                        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.collide);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mp.start();
                            }
                        }).start();
                    }
                }
            } else{
                playerObj.setHash(figure.toString(), 0);
                figure.setHash(playerObj.toString(), 0);
            }
        }
        playerObj.checkEdgeCollision(canvasR);
        goalCollision(playerObj);
    }

    private void goalCollision(Figure figure) {
        Rect currentCoordinate = figure.coordinate;
        float hashedValue;
        boolean change = false;

        if (currentCoordinate.top <= goalPostUp && currentCoordinate.bottom >= goalPostUp &&  (currentCoordinate.right <= canvasR.right && currentCoordinate.right >= goalPostRight)) {
            hashedValue = figure.getHashOldValue(gRghtUp);
            if(hashedValue == 0){
                if(figure.velY < 1) figure.changeDirX();
                figure.changeDirY();
                change = true;

                figure.setHash(gRghtUp, 1);
            }
        }

        if (currentCoordinate.bottom >= goalPostDown && currentCoordinate.top <= goalPostDown &&  (currentCoordinate.right <= canvasR.right && currentCoordinate.right >= goalPostRight)) {
            hashedValue = figure.getHashOldValue(gRghtDwn);
            if(hashedValue == 0){
                if(figure.velY < 1) figure.changeDirX();
                figure.changeDirY();
                change = true;

                figure.setHash(gRghtDwn, 1);
            }
        }

        if (currentCoordinate.bottom >= goalPostDown && currentCoordinate.top <= goalPostDown && (currentCoordinate.left >= canvasR.left && currentCoordinate.left <= goalPostLeft)) {
            hashedValue = figure.getHashOldValue(gLftDwn);
            if(hashedValue == 0){
                if(figure.velY < 1) figure.changeDirX();
                figure.changeDirY();
                change = true;

                figure.setHash(gLftDwn, 1);
            }
        }

        if (currentCoordinate.top <= goalPostUp &&  currentCoordinate.bottom >= goalPostUp && (currentCoordinate.left >= canvasR.left && currentCoordinate.left <= goalPostLeft)) {
            hashedValue = figure.getHashOldValue(gLftUp);
            if(hashedValue == 0){
                if(figure.velY < 1) figure.changeDirX();
                figure.changeDirY();
                change = true;

                figure.setHash(gLftUp, 1);
            }
        }
        if(change == false ){
            figure.setHash(gRghtUp, 0);
            figure.setHash(gRghtDwn,0);
            figure.setHash(gLftUp,  0);
            figure.setHash(gLftDwn, 0);

        }
        if (figure instanceof Ball) {

            float x = figure.getmOval().getX();
            float y = figure.getmOval().getY();
            float radius = figure.getmOval().getR();

            if((y - radius) > goalPostUp && (y + radius) < goalPostDown && ((x + radius) < goalPostLeft || (x - radius) > goalPostRight)){
                final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.crowd);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mp.start();
                    }
                }).start();
                if(InwhichTeamGoal(figure.getmOval()) == 1){
                    mMatch.setPlayerTwoPoint(mMatch.getPlayerTwoPoint() + 1);
                } else{
                    mMatch.setPlayerOnePoint(mMatch.getPlayerOnePoint() + 1);
                }
                isGoal = true;
                calculationT.setGoal();
            }
        }
    }

    public void checkIfFinished() {
        if( mMatch != null){
        if (timer != null && !finished) return;
        if (finished) {
            try {
                if (timer != null) {
                    timer.interrupt();
                    timer.join();
                }
            } catch (InterruptedException e) {}

            calculationT.finishThread();

            int p1 = mMatch.getPlayerOnePoint();
            int p2 = mMatch.getPlayerTwoPoint();
            int outcome = 0;
            if (p1 > p2) outcome = 1;
            if (p1 < p2) outcome = 2;
            mMatch.setOutcome(outcome);
            return;
        }
        if (mMatch.getPlayerOnePoint() >= MAX_GOALS) {
            mMatch.setOutcome(1);
            finished = true;
            calculationT.finishThread();
        }
        if (mMatch.getPlayerTwoPoint() >= MAX_GOALS) {
            mMatch.setOutcome(2);
            finished = true;
            calculationT.finishThread();
        }
    }
    }


    private int InwhichTeamGoal(Oval oval){
        float x = oval.getX(), y = oval.getY();
        float radius = oval.getR();

        if((y - radius) > goalPostUp && (y + radius) < goalPostDown && (x + radius) < goalPostLeft){
            return 1;
        }
        else {
            return 2;
        }
    }

    public boolean continueIfIsGoal(){
        if(isGoal) {
            isGoal = false;
            for (Figure figure : figures) {
                figure.resetAllParam();
            }
            invalidate();
            calculationT.resetGoal();
            return true;
        }
        return false;
    }


    public String getScore(){
        return mUser1.getName() + "  " + mMatch.getPlayerOnePoint() + ":" + mMatch.getPlayerTwoPoint() + " " + mUser2.getName();
    }

    public void stopWorker(){
        calculationT.interrupt();
        try {
            calculationT.join();
        } catch (InterruptedException e) {}
    }

    public void setActivity(Game gameActivity){
        mGame = gameActivity;
        mMatch = mGame.getMatch();
        mUser1 = mGame.getPlayer1();
        mUser2 = mGame.getPlayer2();

        instantFig();

        canvasR = new Rect();
        calculationT = new CalculationT(this, figures, canvasR);
        calculationT.start();

        if(uslovZavrsetka == 0 && mMatch.getTimeLeft() > 0){
            synchronized (this){
                if(timer == null){
                    timer = createGameTimer();
                    timer.start();
                }
            }
        }

        if(!getContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).getBoolean("gamePaused", false)){
            if(mUser1.isBot()){
                pOneBot = new ComputerPlayer(this, new Figure[]{figures[0], figures[1], figures[2]}, (Ball) figures[6]);
                pOneBot.start();
            }
            if(mUser2.isBot()){
                pTwoBot = new ComputerPlayer(this,
                        new Figure[]{figures[3], figures[4], figures[5]},
                        (Ball) figures[6]);
                pTwoBot.start();
            }
            if(isPlaying==0) isPlaying = 1;
            else isPlaying = isPlaying == 1 ? 2 : 1;

            switch (isPlaying){
                case 1:
                    ((Player)figures[0]).setCanPlayTrue();
                    ((Player)figures[1]).setCanPlayTrue();
                    ((Player)figures[2]).setCanPlayTrue();
                    ((Player)figures[3]).setCanPlayFalse();
                    ((Player)figures[4]).setCanPlayFalse();
                    ((Player)figures[5]).setCanPlayFalse();

                if(pOneBot != null){
                    pOneBot.onMove();
                }

                    break;
                case 2:
                    ((Player)figures[0]).setCanPlayFalse();
                    ((Player)figures[1]).setCanPlayFalse();
                    ((Player)figures[2]).setCanPlayFalse();
                    ((Player)figures[3]).setCanPlayTrue();
                    ((Player)figures[4]).setCanPlayTrue();
                    ((Player)figures[5]).setCanPlayTrue();

                if(pTwoBot != null){
                    pTwoBot.onMove();
                }

                    break;
            }
            if (mMoveTimer != null && !mMoveTimer.isCancelled()) mMoveTimer.cancel(true);
            mMoveTimer = new MoveChecker(this, numberOfMov);
            mMoveTimer.execute();
        }

    }

    public void incNumberOfMoves(){
        numberOfMov++;
    }

    public void changeTeamPlaying() {
        if(isPlaying==0) isPlaying = 1;
        else isPlaying = isPlaying == 1 ? 2 : 1;

        switch (isPlaying){
            case 1:
                ((Player)figures[0]).setCanPlayTrue();
                ((Player)figures[1]).setCanPlayTrue();
                ((Player)figures[2]).setCanPlayTrue();
                ((Player)figures[3]).setCanPlayFalse();
                ((Player)figures[4]).setCanPlayFalse();
                ((Player)figures[5]).setCanPlayFalse();

                if(pOneBot != null)
                    pOneBot.onMove();

                break;
            case 2:
                ((Player)figures[0]).setCanPlayFalse();
                ((Player)figures[1]).setCanPlayFalse();
                ((Player)figures[2]).setCanPlayFalse();
                ((Player)figures[3]).setCanPlayTrue();
                ((Player)figures[4]).setCanPlayTrue();
                ((Player)figures[5]).setCanPlayTrue();

                if(pTwoBot != null)
                    pTwoBot.onMove();

                break;
        }
        if (mMoveTimer != null && !mMoveTimer.isCancelled()) mMoveTimer.cancel(true);
        mMoveTimer = new MoveChecker(this, numberOfMov);
        mMoveTimer.execute();
    }

    public void finish(){
        mGame.finishGA(1);
    }

    public void finishTouchListener(){
        mGame.finishTouchListener();
    }

    public void onPause() {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
        editor.putInt("uslovZavrsetka", uslovZavrsetka);
        editor.putString("Teren", teren);
        editor.putInt("brzinaIgre", brzinaIgre);
        editor.putBoolean("gamePaused", true);
        editor.apply();

        if(mMoveTimer!=null)  mMoveTimer.cancel(true);

        if(pOneBot != null){
            pOneBot.finishGame();
            try {
                pOneBot.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(timer!=null)  timerGamePause = true;

    }

    public void onResume() {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
        editor.putBoolean("gamePaused", false);
        editor.apply();

        if(uslovZavrsetka == 0){
            timerGamePause = false;
             if (timer == null && canvasR != null && mMatch != null) {
                    timer = createGameTimer();
                    timer.start();
             }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(canvasR != null) {
            canvas.getClipBounds(canvasR);

            backgroundP.setBounds(canvasR);

            backgroundP.draw(canvas);
            if (timer != null) {
                canvas.drawText("" + mMatch.getTimeLeft(), canvasR.right - 75, canvasR.top + 75, scoreBoardDataPaint);
            }
            if(figures != null) {
                for (Figure figure : figures) {
                    figure.paint(canvas);
                }
            }
            goalsP.setBounds(canvasR);
            goalsP.draw(canvas);
            if(mUser1 != null && mUser2 != null && mMatch != null)
            canvas.drawText(getScore(), canvasR.left + 40, canvasR.top + 75, scoreBoardDataPaint);
            if (isGoal) {
                canvas.drawText("GOOOOOAAL", 600, 450, mPaint);
            }
        }
        super.onDraw(canvas);
    }
}
