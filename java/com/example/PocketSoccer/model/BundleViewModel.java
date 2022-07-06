package com.example.kf150605d.pocketsoccer.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;

public abstract class BundleViewModel extends AndroidViewModel {


    public BundleViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract void WriteTo(Bundle bundle);
    public abstract void ReadFrom(Bundle bundle);

}
