package com.example.kf150605d.pocketsoccer.bazaPodataka;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.kf150605d.pocketsoccer.bazaPodataka.dao.MatchDao;
import com.example.kf150605d.pocketsoccer.bazaPodataka.dao.UserDao;
import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.Match;
import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.User;


@Database(entities = {User.class, Match.class}, version = 2, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {
    private static AppRoomDatabase instance;

    public abstract UserDao userDao();
    public abstract MatchDao matchDao();

    public static AppRoomDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (AppRoomDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppRoomDatabase.class,
                            "baza_podataka").fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };
}
