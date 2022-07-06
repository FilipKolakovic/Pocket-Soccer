package com.example.kf150605d.pocketsoccer.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.Bundle;

import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.Match;
import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.User;

import java.util.List;

public class AppViewModel extends BundleViewModel {
    private Repository mRepository;

    public AppViewModel(Application application) {
        super(application);
        mRepository = new Repository(application);
    }

    @Override
    public void WriteTo(Bundle bundle) {

    }

    @Override
    public void ReadFrom(Bundle bundle) {

    }


    public LiveData<List<Match>> getAllMatches () {return mRepository.getAllMatches();}

    public LiveData<List<User>> getCurrentUsers() {return mRepository.getCurrentUsers();}

    public LiveData<Match> getCurrentMatch() {return mRepository.getCurrentMatch();}

    public void updateMatch(final Match match){mRepository.updateMatch(match);}

    public void updateUser(final User user){mRepository.updateUser(user);}

    public void insertMatch(final Match match) {mRepository.insertMatch(match);}

    public void insertUser(final User user){mRepository.insertUser(user);}

    public void deleteAllUsers(){mRepository.deleteAllUsers();}

    public void deleteAllMatches(){mRepository.deleteAllMatches();}


}
