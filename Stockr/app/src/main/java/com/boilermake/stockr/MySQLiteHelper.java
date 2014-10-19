package com.boilermake.stockr;

/**
 * Created by ilee on 10/18/14.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "SubscriptionDB";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_SUBS_TABLE = "CREATE TABLE subs ( " +
                "id INTEGER PRIMARY KEY, " +
                "type integer, " +
                "value double," +
                "symbol TEXT," +
                "association integer, " +
                "timewindow integer " +
                " )";

        // create books table
        db.execSQL(CREATE_SUBS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS subs");

        // create fresh books table
        this.onCreate(db);
    }

    // Books table name
    private static final String TABLE_SUBS = "subs";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_VALUE = "value";
    private static final String KEY_SYMBOL = "symbol";
    private static final String KEY_ASSOC = "association";
    private static final String KEY_TIME = "timewindow";

    private static final String[] COLUMNS = {KEY_ID, KEY_TYPE, KEY_VALUE, KEY_SYMBOL, KEY_ASSOC, KEY_TIME};


    public void addSubscribe(Subscribe subs) {
        Log.d("addSubscribe", subs.getSymbol());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ID, subs.getId());
        values.put(KEY_TYPE, subs.getType());
        values.put(KEY_VALUE, subs.getValue());
        values.put(KEY_SYMBOL, subs.getSymbol());
        values.put(KEY_ASSOC, subs.getAssociation());
        values.put(KEY_TIME, subs.getTimewindow());

        // 3. insert
        db.insert(TABLE_SUBS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Subscribe getSubs(int id) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_SUBS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build subscribe object

        Subscribe subs = new Subscribe(id, Integer.parseInt(cursor.getString(1)), java.lang.Double.parseDouble(cursor.getString(2)), cursor.getString(3), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));


        Log.d("getSubscribe(" + id + ")", subs.getSymbol());

        // 5. return subs
        return subs;
    }

    // Get All Subs
    public List<Subscribe> getAllSubs() {
        List<Subscribe> subss = new LinkedList<Subscribe>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_SUBS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        Subscribe subs = null;
        if (cursor.moveToFirst()) {
            do {
                subs = new Subscribe(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)), java.lang.Double.parseDouble(cursor.getString(2)), cursor.getString(3), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)));


                // Add book to books
                subss.add(subs);
            } while (cursor.moveToNext());
        }

        Log.d("getAllBooks()", subss.get(0).getSymbol());

        // return books
        return subss;
    }


    // Updating single subs
    public int updateSubs(Subscribe subs) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, subs.getType());
        values.put(KEY_VALUE, subs.getValue());
        values.put(KEY_SYMBOL, subs.getSymbol());
        values.put(KEY_ASSOC, subs.getAssociation());
        values.put(KEY_TIME, subs.getTimewindow());

        // 3. updating row
        int i = db.update(TABLE_SUBS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(subs.getId()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single book
    public void deleteSub(Subscribe subs) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_SUBS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(subs.getId()) });

        // 3. close
        db.close();

        Log.d("deleteBook", subs.getSymbol());

    }


}