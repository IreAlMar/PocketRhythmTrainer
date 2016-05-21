package com.irene.pocketrhythmtrainer;

/**
 * Created by Irene on 21/05/2016.
 */
public class Round {

    private int id;
    private String nameP;
    private String nameG;
    private Long score;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "score_list";
    public static final String TABLE_NAME = "scores";
    public static final String COL_ID = "_id";
    public static final String COL_PLAYER = "player";
    public static final String COL_GAME = "game";
    public static final String COL_POINTS = "score";

    public static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_PLAYER + " TEXT NOT NULL, " +
            COL_GAME + " TEXT NOT NULL, " +
            COL_POINTS + " REAL NOT NULL);";

}
