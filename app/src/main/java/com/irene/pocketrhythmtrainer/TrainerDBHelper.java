package com.irene.pocketrhythmtrainer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Irene on 21/05/2016.
 */
public class TrainerDBHelper extends SQLiteOpenHelper {
    TrainerDBHelper(Context context) {
        super(context, Round.DATABASE_NAME, null, Round.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Round.DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Round.TABLE_NAME);
        onCreate(db);
    }
}