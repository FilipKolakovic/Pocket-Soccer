package com.example.kf150605d.pocketsoccer.bazaPodataka.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "match_table")
public class Match {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "playerOnename")
    private String playerOne;

    @ColumnInfo(name = "playerTwoname")
    private String playerTwo;

    @ColumnInfo(name = "outcome")
    private int outcome = -1;

    @ColumnInfo(name = "playerOnePoint")
    private int playerOnePoint;

    @ColumnInfo(name = "playerTwoPoint")
    private int playerTwoPoint;

    @ColumnInfo(name = "timeLeft")
    private long timeLeft = 100;


    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }


    public String getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
    }

    public String getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }

    public int getPlayerTwoPoint() {
        return playerTwoPoint;
    }

    public void setPlayerTwoPoint(int playerTwoPoint) {
        this.playerTwoPoint = playerTwoPoint;
    }

    public int getOutcome() {
        return outcome;
    }

    public void setOutcome(int outcome) {
        this.outcome = outcome;
    }

    public int getPlayerOnePoint() {
        return playerOnePoint;
    }

    public void setPlayerOnePoint(int playerOnePoint) {
        this.playerOnePoint = playerOnePoint;
    }


}