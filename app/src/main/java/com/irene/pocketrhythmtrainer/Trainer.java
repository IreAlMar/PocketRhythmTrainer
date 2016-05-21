package com.irene.pocketrhythmtrainer;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Irene on 21/05/2016.
 */
public class Trainer extends Application {

    private static Context context;
    private static Trainer application;

    private static SQLiteOpenHelper dbHelper;
    private static SQLiteDatabase database;

    public void onCreate() {
        super.onCreate();
        Trainer.context = getApplicationContext();
        Trainer.application = this;
        dbHelper = new TrainerDBHelper(this);
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        dbHelper.close();
        dbHelper = null;
    }

    public static Trainer getApplication() {
        return Trainer.application;
    }

    public static SQLiteDatabase getDatabaseInstance() {
        return Trainer.database;
    }
}
