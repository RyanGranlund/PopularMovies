package com.digitalinfomesh.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ryangranlund on 12/10/15.
 */
public class MovieRepo {
    private DBHelper dbHelper;
    static final String favoritesTable="MovieFavorite";
    static final String colID="ID";
    static final String colMovieID="MovieID";
    static final String colPosterPath = "PosterPath";
    static final String colTitle="Title";
    static final String colPlot="Plot";
    static final String colRating="Rating";
    static final String colRelease="Release";

    public MovieRepo(Context context){
        dbHelper = new DBHelper(context);
    }

    public void addFavorite(String movieID, String path, String title, String plot, String rating, String release) {

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(colMovieID, movieID);
        cv.put(colPosterPath, path);
        cv.put(colTitle, title);
        cv.put(colPlot, plot);
        cv.put(colRating, rating);
        cv.put(colRelease, release);
        db.insert(favoritesTable, "null", cv);
        db.close();

    }

    public void getFavorites() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "Select * FROM " + favoritesTable;
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        String Title = cursor.getString(3);
        cursor.moveToLast();

        Title = cursor.getString(3);
        db.close();


    }


}
