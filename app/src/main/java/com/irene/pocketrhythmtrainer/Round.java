package com.irene.pocketrhythmtrainer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Irene on 21/05/2016.
 */
public class Round implements Comparable<Round>{

    private static final String TAG = Round.class.getCanonicalName();

    private int id;
    private String nameP;
    private String nameG;
    private Long score;

    public static final String TABLE_NAME = Round.class.getSimpleName();
    public static final String COL_ID = "_id";
    public static final String COL_PLAYER = "player";
    public static final String COL_GAME = "game";
    public static final String COL_POINTS = "score";

    public static String createTableString(){
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PLAYER + " TEXT NOT NULL, " +
                COL_GAME + " TEXT NOT NULL, " +
                COL_POINTS + " REAL NOT NULL);";
    }

    public static String dropTableString(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    private static String[] allColumns(){
        return new String[]{COL_ID, COL_PLAYER, COL_GAME, COL_POINTS};
    }

    public Round(){}

    private static Round roundFromCursor(Cursor cursor){
        Round round = new Round();
        round.id = cursor.getInt(cursor.getColumnIndex(COL_ID));
        round.nameP = cursor.getString(cursor.getColumnIndex(COL_PLAYER));
        round.nameG = cursor.getString(cursor.getColumnIndex(COL_GAME));
        round.score = cursor.getLong(cursor.getColumnIndex(COL_POINTS));
        return round;
    }

    public static ArrayList<Round> listAll(){
        return Round.queryAll("");
    }

    public static ArrayList<Round> queryAll(String query){
        Log.d(TAG, "qAll");
        return Round.query(query);
    }

    public static ArrayList<Round> query(String query){
        Log.d(TAG, "QUERY=" +query);
        SQLiteDatabase db = Trainer.getDatabaseInstance();
        ArrayList<Round> rounds = new ArrayList<>();
        String whereClause = "("+COL_PLAYER + " LIKE '%"+query+"%' OR "; //TODO Adaptar a lo necesario
        whereClause += COL_GAME+ " LIKE '%"+query+"%')";

        String orderBy = COL_ID;
        Cursor cursor = db.query(TABLE_NAME, allColumns(), whereClause, null, null, null, orderBy);

        while(cursor.moveToNext()){
            Round round = roundFromCursor(cursor);
            rounds.add(round);
            Log.d(TAG, "queringRounds=" + round.toString());
        }

        Collections.sort(rounds);
        return rounds;
    }

    public void save(){
        SQLiteDatabase db = Trainer.getDatabaseInstance();
        db.beginTransaction();

        try{
            ContentValues values = new ContentValues();
            values.put(COL_PLAYER, this.getNameP());
            values.put(COL_GAME, this.getNameG());
            values.put(COL_POINTS, this.getScore());
            db.insert(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public void deleteAll() {
        Log.d(TAG, "DELETE ALL");
        SQLiteDatabase db = Trainer.getDatabaseInstance();
        db.delete(TABLE_NAME, null, null);
    }

    public static int delete(long id){
        Log.d(TAG, "DELETE ROUND N " + id);
        String whereClause = COL_ID + "= ?";
        String[] whereArgs = { String.valueOf(id)};
        SQLiteDatabase db = Trainer.getDatabaseInstance();
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    public int getId() {
        return id;
    }

    public String getNameP() {
        return nameP;
    }

    public String getNameG() {
        return nameG;
    }

    public Long getScore() {
        return score;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Round(String nameP, String nameG, Long score){
        this.nameP = nameP;
        this.nameG = nameG;
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("Round(%d){%s, %s, %,d}",
                this.id, this.nameP, this.nameG, this.score);
    }

    @Override
    public int compareTo(Round round) {
        return 0;
    }
}
