package com.irene.pocketrhythmtrainer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Irene on 21/05/2016.
 */
public class ItemDbAdapter {
    public static final int  DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "score_list"; //lista_puntuaciones
    private static final String TABLE_NAME = "scores"; //puntuaciones
    public static final String COL_PLAYER = "player";
    public static final String COL_GAME = "game";
    public static final String COL_POINTS = "score";
    public static final String COL_ID = "_id";
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_PLAYER + " TEXT NOT NULL, " +
                    COL_GAME + " TEXT NOT NULL, " +
                    COL_POINTS + " REAL NOT NULL);";
    private final Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private static class DBHelper extends SQLiteOpenHelper {
        DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
    public ItemDbAdapter(Context c) {
        this.context = c;
    }
    public ItemDbAdapter open () {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        dbHelper.close();
    }
    //INSERT INTO scores (player, game, score) VALUES (nameP, nameG, points);
    public long insert(String nameP, String nameG, float points) {
        ContentValues values = new ContentValues();
        values.put(COL_PLAYER, nameP);
        values.put(COL_GAME, nameG);
        values.put(COL_POINTS, points);
        return db.insert(TABLE_NAME, null, values);
    }
    //UPDATE scores SET player = nameP WHERE _id = id;
    public int update(long id, String nameP, String nameG, float points) {
        ContentValues values = new ContentValues();
        values.put(COL_PLAYER, nameP);
        values.put(COL_GAME, nameG);
        values.put(COL_POINTS, points);
        String whereClause = COL_ID + "= ?"; //se reelmplaza por los args
        String[] selectionArgs = {String.valueOf(id)};
        return db.update(TABLE_NAME, values, whereClause, selectionArgs);
    }
    // DELETE FROM scores WHERE _id = id;
    public int delete(long id){
        String whereClause = COL_ID + "= ?"; //se reelmplaza por los args
        String[] whereArgs = { String.valueOf(id)};
        return db.delete(TABLE_NAME, whereClause, whereArgs);
    }
    //DELETE FROM scores;
    public void deleteAll() {
        db.delete(TABLE_NAME, null, null);
    }
    //SELECT * FROM scores;
    public Cursor selectAll() {
        String[] columns = null; // null = Todas
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        return db.query(TABLE_NAME, columns, selection,
                selectionArgs, groupBy, having, orderBy);
    }
    //SELECT _id, player, game, score FROM scores
//ORDER BY score DESC, player ASC;
    public Cursor selectAllOrdered() {
        String[] columns = new String[]{COL_ID, COL_PLAYER, COL_GAME, COL_POINTS};
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        String orderBy = String.format("%s DESC, %s ASC", COL_POINTS, COL_PLAYER);
        return db.query(TABLE_NAME, columns, selection,
                selectionArgs, groupBy, having, orderBy);
    }
    //SELECT DISTINCT * FROM scores WHERE _id = id;
    public Cursor selectById(long id){
        boolean distinct = true; //filas unicas
        String[] columns = null;
        String selection = COL_ID + "= ?";
        String[] selectionArgs = {String.valueOf(id)};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        Cursor cursor = db.query(distinct,TABLE_NAME, columns, selection,
                selectionArgs, groupBy, having, orderBy, limit);
        if (cursor != null) cursor.moveToFirst();
        return cursor;
    }
}
