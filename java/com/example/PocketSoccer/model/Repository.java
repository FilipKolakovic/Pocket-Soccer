package com.example.kf150605d.pocketsoccer.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.kf150605d.pocketsoccer.bazaPodataka.AppRoomDatabase;
import com.example.kf150605d.pocketsoccer.bazaPodataka.dao.MatchDao;
import com.example.kf150605d.pocketsoccer.bazaPodataka.dao.UserDao;
import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.Match;
import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.User;

import java.util.List;

public class Repository {
    private UserDao mUserDao;
    private MatchDao mMatchDao;

    public Repository(Application application) {
        AppRoomDatabase database = AppRoomDatabase.getDatabase(application);
        mMatchDao = database.matchDao();
        mUserDao = database.userDao();
    }


    public LiveData<List<User>> getCurrentUsers() {return mUserDao.getCurrentUsers();}

    public LiveData<Match> getCurrentMatch() {return mMatchDao.getCurrentMatch();}


    public void updateUser(final User user) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mUserDao.updateUser(user);
            }
        }).start();
    }

    public void insertMatch(final Match match){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mMatchDao.insertMatch(match);
            }
        }).start();
    }

    public void deleteAllUsers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mUserDao.deleteAllUsers();
            }
        }).start();
    }

    public void insertUser(final User user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mUserDao.insertUser(user);
            }
        }).start();
    }

    public LiveData<List<Match>> getAllMatches(){return mMatchDao.getAllMatches();}


    public void deleteAllMatches() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mMatchDao.deleteAllMatches();
            }
        }).start();
    }

    public void updateMatch(final Match match) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mMatchDao.updateMatch(match);
            }
        }).start();
    }


}