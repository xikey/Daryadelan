package daryadelan.sandogh.zikey.com.daryadelan.tools;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import daryadelan.sandogh.zikey.com.daryadelan.BuildConfig;
import daryadelan.sandogh.zikey.com.daryadelan.data.UserData;


/**
 * Created by Zikey on 07/06/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // DATABASE
    private static final String DATABASE_NAME = "daryadel.db";
    private static final int DATABASE_VERSION = BuildConfig.VERSION_CODE;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(UserData.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + UserData.TABLE_USER);

    }

    public static void clearDatabase(SQLiteDatabase db) {
        db.beginTransaction();
        db.delete( UserData.TABLE_USER,null,null);
        db.endTransaction();
        db.close();



    }

    public static void deleteDatabase(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS " + UserData.TABLE_USER);

        db.execSQL(UserData.CREATE_TABLE);


    }

}
