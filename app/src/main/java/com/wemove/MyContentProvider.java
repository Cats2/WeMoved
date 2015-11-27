package com.wemove;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by w7 on 30/09/2015.
 */
public class MyContentProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.wemove.MyContentProvider");

    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;

    private static UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("fr.esiea.myapplication", "elements", ALLROWS);
        uriMatcher.addURI("fr.esiea.myapplication", "elements/#", SINGLE_ROW);
    }

    private static final String KEY_ID = "_id";
    private static final String NAME = "name";
    private static final String DESCR = "description";
    private static final String ADRESSE = "adresse";
    private static final String TEL = "tel";
    private static final String WEB = "web";
    private static final String TARIF = "tarif";
    private static final String REDUC = "reduc";
    private static final String GRP = "groupe";
    private static final String AUDIO = "audio";
    private static final String GUIDE = "guide";

    private SiteDBHelper mySiteHelper;

    public boolean onCreate() {
        mySiteHelper = new SiteDBHelper(getContext(), SiteDBHelper.DATABASE_NAME, null, SiteDBHelper.DATAVASE_VERSION);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mySiteHelper.getWritableDatabase();

        String groupBy = null;
        String having  = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SiteDBHelper.DATABASE_TABLE);

        switch (uriMatcher.match(uri))
        {
            case SINGLE_ROW: String rowId = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(KEY_ID + "=" + rowId);

            default: break;
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case ALLROWS: return "vnd.android.cursor.dir/vnd.paad.elemental";
            case SINGLE_ROW: return "vnd.android.cursor.item/vnd.paad.elemental";
            default: throw new IllegalArgumentException("URI non reconnue");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mySiteHelper.getWritableDatabase();
        String nullColumnHack = null;
        long id = db.insert(SiteDBHelper.DATABASE_TABLE, nullColumnHack, values);
        if(id > -1)
        {
            Uri instertedId = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(instertedId, null);
            return instertedId;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mySiteHelper.getWritableDatabase();
        switch (uriMatcher.match(uri))
        {
            case SINGLE_ROW: String rowId = uri.getPathSegments().get(1);
                selection = KEY_ID + "=" + rowId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
            default: break;
        }
        if (selection == null)
        {
            selection = "1";
        }
        int deleteCount = db.delete(SiteDBHelper.DATABASE_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mySiteHelper.getWritableDatabase();
        switch(uriMatcher.match(uri))
        {
            case SINGLE_ROW: String rowId = uri.getPathSegments().get(1);
                selection = KEY_ID + "=" + rowId + (!TextUtils.isEmpty(selection) ?  " AND (" + selection + ')' : "");
            default: break;
        }
        if(selection == null)
        {
            selection = "1";
        }
        int updateCount = db.update(SiteDBHelper.DATABASE_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }

    private class SiteDBHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "myDatabase";
        public static final String DATABASE_TABLE = "Site";
        public static final int DATAVASE_VERSION = 1;


        private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" + KEY_ID + " integer primary key, " +
                NAME + " text not null, " +
                DESCR + " text not null, " +
                ADRESSE + " text not null, " +
                TEL + " text not null, " +
                WEB + " text not null, " +
                TARIF + " text not null, " +
                REDUC + " text not null, " +
                GRP + " text not null, " +
                AUDIO + " text not null, " +
                GUIDE + " text not null); ";

        public SiteDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("DATABASE", "Mise à jour de la version " + oldVersion + " vers la version " + newVersion + ": toutes les données seront perdues.");
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }


}
