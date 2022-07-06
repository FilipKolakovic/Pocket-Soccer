package com.example.kf150605d.pocketsoccer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.Match;
import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.User;
import com.example.kf150605d.pocketsoccer.model.AppViewModel;
import com.example.kf150605d.pocketsoccer.view.CV_Game;

import java.util.List;

public class Game extends AppCompatActivity {


    private CV_Game mGame;
    private AppViewModel mViewModel;
    private User mUser1, mUser2;
    private TouchListenerClass touchListener;
    private Match mMatch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGame = findViewById(R.id.custom_view_game);
        touchListener = new TouchListenerClass(this, mGame);
        mGame.setOnTouchListener(touchListener);
        ViewModelProvider provider = ViewModelProviders.of(this);
        mViewModel = provider.get(AppViewModel.class);

        loadDB();
    }

    private void loadDB() {
        final Game self = this;
        mViewModel.getCurrentMatch().observe(this, new Observer<Match>() {
            @Override
            public void onChanged(@Nullable final Match match) {
                if (match != null){

                    mMatch = match;
                mViewModel.getCurrentUsers().observe(self, new Observer<List<User>>() {
                    @Override
                    public void onChanged(@Nullable List<User> users) {
                        if (users != null) {
                            User user1 = users.get(0);
                            User user2 = users.get(1);
                            if (user1.getName().equals(mMatch.getPlayerOne())) {
                                mUser1 = user1;
                                mUser2 = user2;
                            } else {
                                mUser2 = user1;
                                mUser1 = user2;
                            }
                            mGame.setActivity(Game.this);
                        }
                    }
                });
              }
            }
        });
    }


    private void storeInDB(){
        if(mMatch != null) {
            if (mMatch.getOutcome() != -1) {
                if (mUser1 != null) {
                    mUser1.setCurrent(false);
                    mViewModel.updateUser(mUser1);
                    mUser1 = null;
                }
                if (mUser2 != null) {
                    mUser2.setCurrent(false);
                    mViewModel.updateUser(mUser2);
                    mUser2 = null;
                }
                mViewModel.updateMatch(mMatch);

                mMatch = null;
            } else {
                mViewModel.updateMatch(mMatch);
            }
        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        mGame.onPause();
        storeInDB();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mGame.onResume();
    }


    @Override
    public void onBackPressed() {
        storeInDB();
        mGame.stopWorker();
        Intent replyIntent = new Intent();
        setResult(RESULT_OK, replyIntent);
        finish();

        super.onBackPressed();
    }

    public void finishTouchListener(){
        touchListener.signalFinish();
    }

    public void finishGA(int code) {
        storeInDB();
        if (code == 0) {
            mGame.stopWorker();

            Intent replyIntent = new Intent();
            setResult(RESULT_OK, replyIntent);
            finish();
        } else {
            Intent replyIntent = new Intent();
            replyIntent.setClass(this, MainActivity.class);
            startActivity(replyIntent);
            finish();

        }

    }
        public Match getMatch() {
            return mMatch;
        }

        public User getPlayer1() {
            return mUser1;
        }

        public User getPlayer2() {
            return mUser2;

    }


}