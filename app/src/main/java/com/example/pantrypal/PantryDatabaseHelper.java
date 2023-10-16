package com.example.pantrypal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PantryDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pantry.db";
    private static final int DATABASE_VERSION = 1;

    public PantryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables and define the schema for your database
        db.execSQL("CREATE TABLE IF NOT EXISTS FoodItems (id INTEGER PRIMARY KEY, name TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema upgrades if needed
    }
}
