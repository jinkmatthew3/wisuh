package id.ac.umn.wisuh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class SqliteHelper extends SQLiteOpenHelper {

    //DATABASE NAME
    public static final String DATABASE_NAME = "WisuhAndCo";

    //DATABASE VERSION
    public static final int DATABASE_VERSION = 1;

    //TABLE NAME
    public static final String TABLE_CUSTOMERS = "customers";

    //TABLE CUSTOMERS COLUMNS
    //ID COLUMN @primaryKey
    public static final String KEY_ID_Customer = "id";

    //COLUMN first name
    public static final String KEY_First_Name = "fName";

    //COLUMN last name
    public static final String KEY_Last_Name = "lName";

    //COLUMN phone number
    public static final String KEY_Phone_Number = "pNumber";

    //COLUMN email
    public static final String KEY_EMAIL = "email";

    //COLUMN password
    public static final String KEY_PASSWORD = "password";

    //SQL for creating customers table
    public static final String SQL_TABLE_CUSTOMERS = " CREATE TABLE " + TABLE_CUSTOMERS
            + " ( "
            + KEY_ID_Customer + " INTEGER PRIMARY KEY, "
            + KEY_First_Name + " TEXT, "
            + KEY_Last_Name + " TEXT, "
            + KEY_Phone_Number + " TEXT, "
            + KEY_EMAIL + " TEXT, "
            + KEY_PASSWORD + " TEXT"
            + " ) ";


    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create Table when oncreate gets called
        sqLiteDatabase.execSQL(SQL_TABLE_CUSTOMERS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop table to create new one if database version updated
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
    }

    //using this method we can add users to user table
    public void addCustomer(Customer customer) {

        //get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to insert
        ContentValues values = new ContentValues();

        //Put first name in  @values
        values.put(KEY_First_Name, customer.fName);

        //Put last name in  @values
        values.put(KEY_Last_Name, customer.lName);

        //Put phone number in  @values
        values.put(KEY_Phone_Number, customer.pNumber);

        //Put email in  @values
        values.put(KEY_EMAIL, customer.email);

        //Put password in  @values
        values.put(KEY_PASSWORD, customer.password);

        // insert row
        long todo_id = db.insert(TABLE_CUSTOMERS, null, values);
    }

    public Customer Authenticate(Customer customer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOMERS,// Selecting Table
                new String[]{KEY_ID_Customer, KEY_First_Name, KEY_Last_Name, KEY_Phone_Number, KEY_EMAIL, KEY_PASSWORD},//Selecting columns want to query
                KEY_EMAIL + "=?",
                new String[]{customer.email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email
            Customer user1 = new Customer(cursor.getString(0),cursor.getString(1),cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

            //Match both passwords check they are same or not
            if (customer.password.equalsIgnoreCase(user1.password)) {
                return user1;
            }
        }

        //if user password does not matches or there is no record with that email then return @false
        return null;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CUSTOMERS,// Selecting Table
                new String[]{KEY_ID_Customer, KEY_First_Name, KEY_Last_Name, KEY_Phone_Number, KEY_EMAIL, KEY_PASSWORD},//Selecting columns want to query
                KEY_EMAIL + "=?",
                new String[]{email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }

        //if email does not exist return false
        return false;
    }
}