package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qry1 = "create table users( username text, email text, password text, studentid text)";

        sqLiteDatabase.execSQL(qry1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void register (String username, String email, String password, String studentid){
        ContentValues cv= new ContentValues();
        cv.put("username", username);
        cv.put("email", email);
        cv.put("password", password);
        cv.put("studentid", studentid);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("users",null ,cv);
        db.close();
    }
    public int login(String email, String password){
        int result = 0;
        String str[] = new String[2];
        str[0]=email;
        str[1]=password;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c= db.rawQuery("select * from users where email=? and password=?", str);
        if(c.moveToFirst()){
            result = 1;
        }
        return result;
    }

    public String ret_si(String email){
        String str[] = new String[1];
        str[0]=email;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c= db.rawQuery("select studentid from users where email=?", str);
        if(c.moveToFirst()){
            String id = c.getString(0);
            c.close();
            return id;
        }
        c.close();
        return null;
    }


}
