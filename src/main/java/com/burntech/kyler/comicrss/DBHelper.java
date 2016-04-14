package com.burntech.kyler.comicrss;

/**
 * Created by Kyler J. Burnett on 4/10/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    // DATABASE NAME.
    public static final String DATABASE_NAME    = "Comics.db";

    // TABLE 1 - comics: contains the comic.
    public static final String TABLE_NAME       = "comics";
    public static final String COLUMN_TITLE     = "title";
    public static final String COLUMN_PUBLISHER = "publisher";
    public static final String COLUMN_LOCATION  = "location";
    public static final String COLUMN_URL       = "url";
    public static final String COLUMN_DATE      = "date";
    public static final String COLUMN_WEEK      = "week";

    // TABLE 2 - dates: a list of dates that the app has already downloaded.
    public static final String TABLE_DATES      = "dates";
    public static final String COLUMN_DOWNLOADED= "downloaded";
    public static final String COLUMN_ENTRIES   = "entries";

    // TABLE 3 - publishers: a list of publishers and their id values available.
    public static final String TABLE_PUBLISHERS     = "publishers";
    public static final String COLUMN_ID            = "id";
    public static final String COLUMN_PUBLISHERNAME = "publishername";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table comics " +
                        "(title text primary key, " +
                        "publisher text, " +
                        "location text , " +
                        "url text, " +
                        "date text, " +
                        "week text)"
        );

        db.execSQL(
                "create table dates " +
                        "(downloaded text primary key, entries text)"
        );

        db.execSQL(
                "create table publishers " +
                        "(id text primary key, " +
                        "publishername text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS comics");
        onCreate(db);
    }

    public boolean insertComic(String title, String publisher, String location, String url, String date, String week) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("title", title);
        contentValues.put("publisher", publisher);
        contentValues.put("location", location);
        contentValues.put("url", url);
        contentValues.put("date", date);
        contentValues.put("week", week);

        db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        //db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean dropTable(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS table_name");
        return true;
    }

    public Cursor getData(String publisher) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from comics where publisher="+publisher+"", null);
        return res;
    }

    public ArrayList<Comic> getComics(String publisher, String week) {
        ArrayList<Comic> comics = new ArrayList<Comic>();

        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("select * from comics where publisher=\""+publisher+"\"", null);
        Cursor cursor = db.query(TABLE_NAME, null, "publisher=? and week=?", new String[] {publisher, week}, null, null, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String title    = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_TITLE));
            String location = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_LOCATION));
            String url      = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_URL));
            String date     = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DATE));
            Comic comic     = new Comic(title, publisher, location, url, date);
            comics.add(comic);
            cursor.moveToNext();
        }
        return comics;
    }

    public boolean insertDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("downloaded", date);

        db.insertWithOnConflict(TABLE_DATES, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        return true;
    }

    public ArrayList<String> getDownloadedDates() {
        ArrayList<String> downloadedDates = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from dates", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String date = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_DOWNLOADED));
            downloadedDates.add(date);
            cursor.moveToNext();
        }
        return downloadedDates;
    }

    public boolean insertPublisher(String id, String publishername) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("id", id);
        contentValues.put("publishername", publishername);

        db.insertWithOnConflict(TABLE_PUBLISHERS, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        return true;
    }

    public ArrayList<Publisher> getPublishers() {
        ArrayList<Publisher> pubs = new ArrayList<Publisher>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from publishers", null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            String id = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ID));
            String publishername = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PUBLISHERNAME));
            pubs.add(new Publisher(id, publishername));
            cursor.moveToNext();
        }
        return pubs;
    }

    public boolean publishersEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM publishers", null);
        int count = 0;
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            count++;
            cursor.moveToNext();
        }
        if (count > 0)
            return false;
        else return true;
    }
}

