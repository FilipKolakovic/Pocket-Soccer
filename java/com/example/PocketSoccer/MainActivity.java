package com.example.kf150605d.pocketsoccer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.Match;
import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.User;
import com.example.kf150605d.pocketsoccer.model.AppViewModel;

import java.util.List;

import static com.example.kf150605d.pocketsoccer.Settings.MY_PREFS_NAME; // shared preference koji je deklarisan u Settings

public class MainActivity extends AppCompatActivity {
    public static final int REQUESTCODESETTING = 4;
    public static final int REQUESTCODETEAMSELECT = 5;
    public static final int STATS_ACTIVITY = 6;
    public static final int REQUESTRESUME = 11;



    private String izgledTerena;
    private int uslovZavrsetka; // istek vremena = 0 ili broj golova = 1  - promeniti mozda u string
    private int brzinaIgre;  //promeniti mozda u drugi tip;
    private String uslovZ =""; //

    private Button resumeB;
    private Button newGameB;
    private Button statisticB;
    private Button settingsB;
    SharedPreferences prefs;

    private Match mMatch;
    private User user1, user2;
    private AppViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resumeB = findViewById(R.id.resumeB);      // staviti mozda ova 4 dugmeta van onCreate()
        resumeB.setEnabled(false);
        newGameB = findViewById(R.id.newgameB);
        statisticB = findViewById(R.id.statisticB);
        settingsB = findViewById(R.id.settingsB);

        izgledTerena = "trava"; // default izgled trava
        uslovZavrsetka = 1; // default na golove
        brzinaIgre = 30; // default - PROMENITI POSLE !!!!!!!!!!!!

        ViewModelProvider modelProvider = ViewModelProviders.of(this);
        mViewModel = modelProvider.get(AppViewModel.class);

        if(mMatch == null) {
            final MainActivity self = this;
            mViewModel.getCurrentMatch().observe(this, new Observer<Match>() {
                @Override
                public void onChanged(@Nullable final Match match) {
                    if (match == null) return;

                    mMatch = match;
                    mViewModel.getCurrentUsers().observe(self, new Observer<List<User>>() {
                        @Override
                        public void onChanged(@Nullable List<User> users) {
                            User u1 = users.get(0), u2 = users.get(1);
                            if (u1.getName().equals(mMatch.getPlayerOne())) {
                                user1 = u1;
                                user2 = u2;
                            } else {
                                user2 = u1;
                                user1 = u2;
                            }
                            resumeB.setEnabled(true);


                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeB.setEnabled(false);
        if(mMatch != null)
            if(mMatch.getOutcome() == -1)
            resumeB.setEnabled(true);
    }

    public void resumeGame(View view) {
            Intent intent = new Intent();
            intent.setClass(this, Game.class);
            startActivityForResult(intent, REQUESTRESUME);

    }

    public void newGameStart(View view) {
        Intent teamChoose = new Intent(this, TeamSelect.class);
        startActivityForResult(teamChoose, REQUESTCODETEAMSELECT);
    }

    public void viewStatistic(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, StatisticActivtiy.class);
        startActivityForResult(intent, STATS_ACTIVITY);
    }

    public void settings(View view) {
        Intent intent = new Intent(this,Settings.class);
        startActivityForResult(intent,REQUESTCODESETTING);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUESTCODESETTING){ // ako je requestCODE zahtev za podesavanje aktivnosti - REQUESTECODESETTINGS
            if(resultCode == RESULT_OK) {
                prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                izgledTerena = prefs.getString("teren", "trava");
                uslovZavrsetka = prefs.getInt("uslovZavrsetka", 1);
                if(uslovZavrsetka == 1) uslovZ = "naGolove";
                else uslovZ = " naVreme";
                brzinaIgre = prefs.getInt("brzinaIgre", 30);
            }
        }
        if(requestCode == REQUESTCODETEAMSELECT){
            if(resultCode == RESULT_OK){

            if(mMatch != null){
            mMatch.setOutcome(3);
            mMatch.setTimeLeft(-1);

            if(user1 != null){
                user1.setCurrent(false);
                mViewModel.updateUser(user1);
            }
            if(user2 != null){
                user2.setCurrent(false);
                mViewModel.updateUser(user2);
            }
            mViewModel.updateMatch(mMatch);
        }

                prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                boolean isBot = prefs.getBoolean("isBot", false);
                user1 = new User(prefs.getString("player1Name", "player1Name"));
                user1.setTeam(prefs.getString("tim1", "Canada"));
                if(isBot){
                    user1.setBot(true);
                }

                user2 = new User(prefs.getString("player2Name", "player2Name"));
                user2.setTeam(prefs.getString("tim2", "China"));

                mViewModel.insertUser(user1);
                mViewModel.insertUser(user2);

                Match match = new Match();
                match.setPlayerOne(user1.getName());
                match.setPlayerTwo(user2.getName());

                mViewModel.insertMatch(match);
                mMatch = match;

                Intent intent = new Intent();
                intent.setClass(this, Game.class);
                startActivity(intent);
            }
        }
        if(STATS_ACTIVITY == requestCode && RESULT_OK == resultCode){}
        // ostali zahtevi za new, resume itd.
        if(REQUESTRESUME == requestCode && RESULT_OK == resultCode){}
    }

    public String getIzgledTerena() { return izgledTerena;}
    public int getUslovZavrsetka() { return uslovZavrsetka;}
    public int getBrzinaIgre() { return brzinaIgre;}
}
