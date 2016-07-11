package tw.com.hismax.test.hospinfosys;

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

/**
 * Created by u0124021 on 2015/7/17.
 */
public class Sqlite1 extends ContentProvider {
    public static final Uri CONTENT_ACCOUNT= Uri.parse("content://com.example.u0124021.mysqllite/account");
    public static  final String ID="_id";
    public static final String TABLE_ACCOUNT = "account";
    public static final String COL_ACCOUNT_NAME = "name";
    public static final String COL_ACCOUNT_EMAIL="email";
    public static final String COL_ACCOUNT_PASSWORD="password";

    private static final int ACCOUNT_ALLROWS = 1 ;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.u0124021.mysqllite","account",ACCOUNT_ALLROWS);

    }

    private DbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        long  id;
        switch (uriMatcher.match(uri)){
            case ACCOUNT_ALLROWS:
                qb.setTables(TABLE_ACCOUNT);
                break;
            default:
                throw new IllegalArgumentException("URI="+uri);

        }
        //where a = 1 java
        Cursor c = qb.query(db,projection,selection,selectionArgs,null,null,sortOrder );
        c.setNotificationUri(getContext().getContentResolver(),uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long id;

        switch (uriMatcher.match(uri)){
            case ACCOUNT_ALLROWS:
                id = db.insertOrThrow(TABLE_ACCOUNT ,null , contentValues);
                break;
            default:
                throw new IllegalArgumentException("URI="+uri);
        }

        Uri insertUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(insertUri, null);
        return insertUri;
    }

    @Override
    public int delete(Uri uri, String selections, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int count;
        switch (uriMatcher.match(uri)){
            case ACCOUNT_ALLROWS:
                count = db.delete(TABLE_ACCOUNT,selections,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI="+uri);

        }
        getContext().getContentResolver().notifyChange(uri,null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        SQLiteDatabase db=dbHelper.getReadableDatabase();
        int count;
        switch (uriMatcher.match(uri)){
            case ACCOUNT_ALLROWS:
                count=db.update(TABLE_ACCOUNT,contentValues,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI="+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    private static final String DATABASE_NAME="sqlit.db";
    private static final int DATABASE_VWESION =  1;


    private class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context){
            super (context , DATABASE_NAME,null,DATABASE_VWESION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table account( _id integer primary key autoincrement, name varchar(5),email text,password varchar(10))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("drop table if exists information");
            onCreate(sqLiteDatabase);
        }
    }
}
