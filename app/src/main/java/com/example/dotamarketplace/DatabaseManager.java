package com.example.dotamarketplace;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        this.context = context;
    }

    public DatabaseManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() { dbHelper.close(); }

    public void register(String name, String username, String password, String phone, String gender) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME, name);
        contentValues.put(DatabaseHelper.USERNAME, username);
        contentValues.put(DatabaseHelper.PASSWORD, password);
        contentValues.put(DatabaseHelper.PHONE_NUMBER, phone);
        contentValues.put(DatabaseHelper.GENDER, gender);
        database.insert(DatabaseHelper.TABLE_USER, null, contentValues);
    }

    public int updateBalance(int id, int balance) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.BALANCE, balance);
        int i = database.update(DatabaseHelper.TABLE_USER, contentValues, DatabaseHelper._ID + " = " + id, null);
        return i;
    }

    public void storeItem(String name, int price, int stock) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME, name);
        contentValues.put(DatabaseHelper.PRICE, price);
        contentValues.put(DatabaseHelper.STOCK, stock);
        database.insert(DatabaseHelper.TABLE_ITEM, null, contentValues);
    }

    public int updateStock(int id, int stock) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.STOCK, stock);
        int i = database.update(DatabaseHelper.TABLE_ITEM, contentValues, DatabaseHelper._ID + " = " + id, null);
        return i;
    }

    public void storeTransaction(int userId, int itemId, int stock, String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.USER_ID, userId);
        contentValues.put(DatabaseHelper.ITEM_ID, itemId);
        contentValues.put(DatabaseHelper.QUANTITY, stock);
        contentValues.put(DatabaseHelper.TRANSACTION_DATE, date);
        database.insert(DatabaseHelper.TABLE_TRANSACTION, null, contentValues);
    }

    public Cursor fetchItem() {
        String query = "select * from " + DatabaseHelper.TABLE_ITEM;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getProfile(String username) {
        String query = "select * from " + DatabaseHelper.TABLE_USER + " WHERE " + DatabaseHelper.USERNAME + " = '" + username + "'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public void deleteHistory() {
        database.execSQL("delete from " + DatabaseHelper.TABLE_TRANSACTION);
    }

    public Cursor fetchTransaction() {
        String query = "select * from " + DatabaseHelper.TABLE_TRANSACTION;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getDetailItem(int id) {
        String query = "SELECT * FROM  " + DatabaseHelper.TABLE_ITEM + " WHERE " + DatabaseHelper._ID + " = " + id;
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor login(String username) {
        String query = "select * from " + DatabaseHelper.TABLE_USER + " WHERE " + DatabaseHelper.USERNAME + " = '" + username + "'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public boolean checkUsername(String username) {
        String query = "select * from " + DatabaseHelper.TABLE_USER + " WHERE " + DatabaseHelper.USERNAME + " = '" + username + "'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return false;
        }
        return true;
    }

    public boolean checkItemTable() {
        String query = "select count(*) from " + DatabaseHelper.TABLE_ITEM;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        return count == 0;
    }
}
