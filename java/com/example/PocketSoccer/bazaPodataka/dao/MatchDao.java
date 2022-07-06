package com.example.kf150605d.pocketsoccer.bazaPodataka.dao;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.kf150605d.pocketsoccer.bazaPodataka.entity.Match;
import java.util.List;

@Dao
public interface MatchDao {

    @Query("SELECT * FROM match_table WHERE outcome <> -1")
    LiveData<List<Match>> getAllMatches();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMatch(Match match);

    @Query("SELECT * FROM match_table " +
            "WHERE (playerOnename = :playerOne AND playerTwoname = :playerTwo) " +
            "OR (playerOnename = :playerOne AND playerTwoname = :playerTwo)")
    LiveData<List<Match>> getAllPreviousMatches(String playerOne, String playerTwo);

    @Query("DELETE FROM match_table")
    void deleteAllMatches();

    @Query("DELETE FROM match_table "+
            "WHERE (playerOnename = :player1 AND playerTwoname = :player2) " +
            "OR (playerOnename = :player2 AND playerTwoname = :player1)")
    void deleteAllPreviousMatches(String player1, String player2);

    @Query("SELECT * FROM match_table WHERE outcome = -1")
    LiveData<Match> getCurrentMatch();

    @Update
    void updateMatch(Match match);
}

