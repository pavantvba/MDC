package com.mdc.mdc;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.app.Application;
import android.database.Cursor;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.ContentValues;
import android.widget.Toast;

/**
 * Created by pavant on 10/12/15.
 */


public class SaveData extends SQLiteOpenHelper{

    public static final String DatabaseName = "MDC_DB_New";
    public static final int DatabaseVersion = 1;
    public static final String MDC_TableName = "MDC_Data_New";
    SQLiteDatabase sqLiteDatabase;
    SQLiteDatabase sqLiteDatabaseRead;
    SQLiteDatabase sqLiteDatabaseWrite;
    String CLASSNAME = this.getClass().getName();

    public static final String tableCreate = "CREATE TABLE "+ MDC_TableName + "(ColumnId INTEGER PRIMARY KEY autoincrement, BussinessName TEXT NOT NULL, Password TEXT NOT NULL, CodeString TEXT NOT NULL, ExpensePercentage INTEGER NOT NULL, SellingPercentage INTEGER NOT NULL, PwdhintQuestion TEXT NOT NULL, Pwdhintanswer TEXT NOT NULL)";


    public SaveData(Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
        Log.d(CLASSNAME, "In Constructor... Database location : " + context.getDatabasePath(DatabaseName));
        sqLiteDatabaseRead = getReadableDatabase();
        sqLiteDatabaseWrite = getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase){
        Log.d(CLASSNAME, "OnCreate - Before Database / Table create" + sqLiteDatabase);
        Log.d(CLASSNAME, "Table Create: " + tableCreate);
        sqLiteDatabase.execSQL(tableCreate);
        Log.d(CLASSNAME, "OnCreate - After Database / Table create");
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(CLASSNAME,
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MDC_TableName);
        onCreate(sqLiteDatabase);

    }

    /*
    public void onOpen(){


    }
 */

    public boolean storeData(String quertyType, ContentValues contentValues, String bussinessName){

        long insertStatus = 0;
        Log.d(CLASSNAME,"contentValues in Database " + contentValues.toString());

        if(quertyType.equalsIgnoreCase("Insert")) {
            insertStatus = sqLiteDatabaseWrite.insert(MDC_TableName, null, contentValues);
        } else if (quertyType.equalsIgnoreCase("Update")) {
            insertStatus = sqLiteDatabaseWrite.update(MDC_TableName, contentValues, "BussinessName = ?", new String[]{bussinessName});
        } else {
          Log.d(CLASSNAME, "Other operation requested, which is not supported " + quertyType);
        }

        if(insertStatus > 0) {
            Log.d(CLASSNAME,"contentValues Successfully stored .. " + insertStatus);
            return true;
        } else {
            Log.d(CLASSNAME, "contentValues Successfully stored .. " + insertStatus);

            //Toast.makeText(this, "Values are not updated in the database..", Toast.LENGTH_SHORT).show();
            return false;
        }

    }


    public ContentValues fetchData(String key, String value){

        String[] colNames = {"ColumnId", "BussinessName", "Password", "CodeString", "ExpensePercentage", "SellingPercentage", "Pwdhintanswer"};
        Cursor dbCursor;
        if (value.equalsIgnoreCase("All")) {
            Log.d(CLASSNAME,"FetchData : Control in All condition " );
            dbCursor = sqLiteDatabaseRead.query("MDC_Data_New", colNames, null, null, null, null, null);
        } else {
            Log.d(CLASSNAME,"FetchData : " + key + " : " + value);
            dbCursor = sqLiteDatabaseRead.query("MDC_Data_New", colNames, "BussinessName = ?", new String[]{value}, null, null, null);
        }
        ContentValues conValues = new ContentValues();
        //if(dbCursor.getCount() > 0) {
        for(int i=0; i < dbCursor.getCount(); i++) {
            dbCursor.moveToNext();
            long colId = dbCursor.getLong(dbCursor.getColumnIndexOrThrow("ColumnId"));
            if (key.equalsIgnoreCase("Password"))
                conValues.put(key, dbCursor.getString(dbCursor.getColumnIndexOrThrow("Password")));
            if (key.equalsIgnoreCase("CodeString"))
                conValues.put(key, dbCursor.getString(dbCursor.getColumnIndexOrThrow("CodeString")));
            if (key.equalsIgnoreCase("ExpensePercentage"))
                conValues.put(key, dbCursor.getString(dbCursor.getColumnIndexOrThrow("ExpensePercentage")));
            if (key.equalsIgnoreCase("SellingPercentage"))
                conValues.put(key, dbCursor.getString(dbCursor.getColumnIndexOrThrow("SellingPercentage")));
            if (key.equalsIgnoreCase("Pwdhintanswer"))
                conValues.put(key, dbCursor.getString(dbCursor.getColumnIndexOrThrow("Pwdhintanswer")));
            if (key.equalsIgnoreCase("BussinessName"))
                conValues.put(key + "" + i, dbCursor.getString(dbCursor.getColumnIndexOrThrow("BussinessName")));
            if (key.equalsIgnoreCase("PwdhintQuestion"))
                conValues.put(key, dbCursor.getString(dbCursor.getColumnIndexOrThrow("PwdhintQuestion")));

            Log.d("MainActivity", "Red Data from Database Column Id: " + colId + " " + key + ": " + conValues.toString());
        }
        return conValues;
    }
}

