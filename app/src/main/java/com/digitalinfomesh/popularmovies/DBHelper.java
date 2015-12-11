package com.digitalinfomesh.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ryangranlund on 12/10/15.
 */
public class DBHelper extends SQLiteOpenHelper {


    static final String dbName="MovieDB";
    static final int dbVersion = 33;
    static final String favoritesTable="MovieFavorite";
    static final String colID="ID";
    static final String colMovieID="MovieID";
    static final String colPosterPath = "PosterPath";
    static final String colTitle="Title";
    static final String colPlot="Plot";
    static final String colRating="Rating";
    static final String colRelease="Release";

    public DBHelper(Context context ) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        db.execSQL("CREATE TABLE " + favoritesTable + " (" + colID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + colMovieID + " Text , " + colPosterPath + " TEXT , " + colTitle + " TEXT , " + colPlot + " TEXT , " + colRating + " TEXT , " + colRelease + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        db.execSQL("DROP TABLE IF EXISTS "+favoritesTable);

        onCreate(db);
    }

}
