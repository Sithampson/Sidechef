package com.example.sidechef.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.example.sidechef.Model.User;

import java.io.ByteArrayOutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String Database_name = "Side_Chef.db";
    private static final String logTable_name = "Login";
    private static final String logcol_1 = "Username";
    private static final String logcol_2 = "FullName";
    private static final String logcol_3 = "Password";

    private static final String recTable_name = "Recipe";
    private static final String reccol_1 = "RecID";
    private static final String reccol_2 = "RecName";
    private static final String reccol_3 = "MealType";
    private static final String reccol_4 = "Cuisine";
    private static final String reccol_5 = "RecDescrip";
    private static final String reccol_6 = "Ingredients";
    private static final String reccol_7 = "RecProc";
    private static final String reccol_8 = "Image";
    public static final String reccol_9 = "Viewcount";


    public DataBaseHelper(@Nullable Context context) {
        super(context, Database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+logTable_name+" (Username varchar(30) PRIMARY KEY, FullName varchar(30), Password varchar(30))");
        db.execSQL("CREATE TABLE "+recTable_name+" (RecID INTEGER primary key AUTOINCREMENT, RecName varchar(30), MealType varchar(30), Cuisine varchar(30), RecDescrip TEXT, Ingredients TEXT, RecProc TEXT , Image BLOB, Viewcount INTEGER DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + logTable_name);
        db.execSQL("DROP TABLE IF EXISTS " + recTable_name);
        onCreate(db);

    }

    // For Signup button
    public long addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(logcol_1, user.getUsername());
        cv.put(logcol_2,user.getFullname());
        cv.put(logcol_3, user.getPassword());
        long res = db.insert(logTable_name, null, cv);
        db.close();
        return res;
    }

    // For Login Button
    public boolean checkUser(String name, String pass){
        String[] columns = { logcol_1 };
        SQLiteDatabase db = getReadableDatabase();
        String selection = logcol_1 + "=?" + " and " + logcol_3 + "=?";
        String[] selectionArgs = { name, pass };
        Cursor cursor = db.query(logTable_name,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0;
    }

    // For inerstion of Recipe
    public long addrecipe(String name, String meal, String cuisine, String descrip, String ingrdnt, String proc, Bitmap imgtostorebitmap){
        SQLiteDatabase db = this.getWritableDatabase();

        ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
        imgtostorebitmap.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
        byte[] imginbyte = objectByteArrayOutputStream.toByteArray();

        ContentValues cv = new ContentValues();
        cv.put(reccol_2, name);
        cv.put(reccol_3, meal);
        cv.put(reccol_4, cuisine);
        cv.put(reccol_5, descrip);
        cv.put(reccol_6, ingrdnt);
        cv.put(reccol_7, proc);
        cv.put(reccol_8, imginbyte);
        long res = db.insert(recTable_name, null, cv);
        db.close();
        return res;
    }

    public Cursor getdata(String sql){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, null);
    }

    public void update_viewcount(int id){
        SQLiteDatabase db = getWritableDatabase();
        int vcount = 0;
        Cursor c = getdata("SELECT Viewcount from Recipe where RecID = " + id);
        while (c.moveToNext()){
            vcount = c.getInt(0);
        }
        vcount = vcount + 1;
        ContentValues cv = new ContentValues();
        cv.put(reccol_9, vcount);
        db.update(recTable_name, cv, "RecID="+id, null);
        db.close();
    }

//    delete
    public void deleterecipe (int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(recTable_name, "RecID="+id, null);
        db.close();
    }
}
