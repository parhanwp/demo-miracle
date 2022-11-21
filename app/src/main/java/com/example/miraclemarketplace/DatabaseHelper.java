package com.example.miraclemarketplace;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String TABLE_USER = "user";
    public static String TABLE_TRANSACTION = "transaction_table";
    public static String TABLE_ITEM = "item";

    static final String DB_NAME = "market_place";
    static final int DB_VERSION = 1;

    public static final String NAME = "name";
    public static final String USER_ID = "user_id";
    public static final String _ID = "_id";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String GENDER = "gender";
    public static final String BALANCE = "balance";
    public static final String ITEM_ID = "id_item";
    public static final String PRICE = "price";
    public static final String STOCK = "stock";
    public static final String QUANTITY = "quantity";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String TRANSACTION_DATE = "transaction_date";

    private static final String CREATE_TABLE_USER = "create table " + TABLE_USER + " ("
                    + _ID + " integer primary key autoincrement, "
                    + NAME + " text, "
                    + EMAIL + " text unique, "
                    + PASSWORD + " text, "
                    + PHONE_NUMBER + " text, "
                    + GENDER + " text, "
                    + BALANCE + " integer);";

    private static final String CREATE_TABLE_ITEM = "create table " + TABLE_ITEM + " ("
            + _ID + " integer primary key autoincrement, "
            + NAME + " text, "
            + PRICE + " integer, "
            + STOCK + " integer); ";

    private static final String CREATE_TABLE_TRANSACTION = "create table " + TABLE_TRANSACTION + " ("
            + _ID + " integer primary key autoincrement, "
            + USER_ID + " integer not null, "
            + ITEM_ID + " integer not null, "
            + QUANTITY + " integer, "
            + TRANSACTION_DATE + " text, "
            + "foreign key ("+USER_ID+") references " + TABLE_USER + "(" + _ID + "), "
            + "foreign key ("+ITEM_ID+") references " + TABLE_ITEM + "(" + _ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_ITEM);
        db.execSQL(CREATE_TABLE_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        onCreate(db);
    }
}
