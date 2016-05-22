package com.irene.pocketrhythmtrainer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Irene on 21/05/2016.
 */
public class TrainerDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "trainer";
    public static final int DATABASE_VERSION = 1;

    TrainerDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Round.createTableString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Round.dropTableString());
        db.execSQL(Round.createTableString());
    }
}