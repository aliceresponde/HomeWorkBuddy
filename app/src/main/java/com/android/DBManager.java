package com.android;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.android.DatabaseHelper.TABLE_Buddy;
import static com.android.DatabaseHelper.TABLE_NAME;
import static com.android.DatabaseHelper.TABLE_Student;
import static com.android.DatabaseHelper.TABLE_Teacher;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String type, String id, String pwd) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.ID, id);
        contentValue.put(DatabaseHelper.TYPE, type);
        contentValue.put(DatabaseHelper.PWD, pwd);
        database.insert(TABLE_NAME, null, contentValue);
    }

    public void insertBuddy(String todaydate,String grade,String subject,String teacher,String isquiz,  String featuretest,String workedon, String handouts, String assigndate,String duedate) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.TODAYDATE, todaydate);
        contentValue.put(DatabaseHelper.GRADE, grade);
        contentValue.put(DatabaseHelper.SUBJECT, subject);
        contentValue.put(DatabaseHelper.TEACHER, teacher);
        contentValue.put(DatabaseHelper.ISQUIZ, isquiz);
        contentValue.put(DatabaseHelper.FEATURETEST, featuretest);
        contentValue.put(DatabaseHelper.WORKEDON, workedon);
        contentValue.put(DatabaseHelper.HANDOUTS, handouts);
        contentValue.put(DatabaseHelper.ASSIGNMENT, assigndate);
        contentValue.put(DatabaseHelper.DUEDATE, duedate);


        database.insert(TABLE_Buddy, null, contentValue);

    }

    public void insertStudent(String name, String grade, String email1, String email2,String date) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.STUDENTNAME, name);
        contentValue.put(DatabaseHelper.GRADE, grade);
        contentValue.put(DatabaseHelper.PARENTEMAIL1, email1);
        contentValue.put(DatabaseHelper.PARENTEMAIL2, email2);
        contentValue.put(DatabaseHelper.DATE, date);
        database.insert(TABLE_Student, null, contentValue);
    }

    public void insertTeacher(String name, String grade, String handout, String upload,String date) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.TEACHERNAME, name);
        contentValue.put(DatabaseHelper.GRADETeacher, grade);
        contentValue.put(DatabaseHelper.HANDOUTFILE, handout);
        contentValue.put(DatabaseHelper.UPLOADFILE, upload);
        contentValue.put(DatabaseHelper.UPLOADDATE, date);
        database.insert(TABLE_Teacher, null, contentValue);
    }


    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.TYPE, DatabaseHelper.ID,DatabaseHelper.PWD };
        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchBuddy() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.TODAYDATE, DatabaseHelper.GRADE,DatabaseHelper.SUBJECT
                ,DatabaseHelper.TEACHER
                ,DatabaseHelper.ISQUIZ
                ,DatabaseHelper.FEATURETEST
                ,DatabaseHelper.WORKEDON
                ,DatabaseHelper.HANDOUTS
                ,DatabaseHelper.ASSIGNMENT
                ,DatabaseHelper.DUEDATE


        };
        Cursor cursor = database.query(TABLE_Buddy, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public Cursor fetchStudents() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.STUDENTNAME, DatabaseHelper.GRADE,DatabaseHelper.PARENTEMAIL1
                ,DatabaseHelper.PARENTEMAIL2 ,DatabaseHelper.DATE


        };
        Cursor cursor = database.query(TABLE_Student, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchTeacher() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.TEACHERNAME, DatabaseHelper.GRADETeacher,DatabaseHelper.HANDOUTFILE
                ,DatabaseHelper.UPLOADFILE  ,DatabaseHelper.UPLOADDATE
        };
        Cursor cursor = database.query(TABLE_Teacher, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public List<String> fetchTodayAssignmentFilePath(String date) {

        List<String> list=new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + TABLE_Teacher +" WHERE date='" + date + "';";
        Cursor c      = database.rawQuery(selectQuery, null);

        c.moveToFirst();
        Log.e("Cursor Object", DatabaseUtils.dumpCursorToString(c));


        if (c.getCount() > 0) {

            if (c.moveToFirst()) {

                do {

                    list.add(c.getString(4));
                    Log.e("Cursor Object", c.getString(4));


                } while (c.moveToNext());

            }

        }
        return list;
    }

    public  List<String> fetchTodayStudents(String date) {

        List<String> list=new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + TABLE_Student +" WHERE date='" + date + "';";
        Cursor c      = database.rawQuery(selectQuery, null);
            if (c.getCount() > 0) {

            if (c.moveToFirst()) {

                do {

                    list.add(c.getString(3));
                    list.add(c.getString(4));



                } while (c.moveToNext());

            }

        }
        return list;
    }



    public int update(long _id, String type, String id, String pwd) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.ID, id);
        contentValues.put(DatabaseHelper.TYPE, type);
        contentValues.put(DatabaseHelper.PWD, pwd);
        int i = database.update(TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

    public boolean isValidUser(String type,String id,String pwd) {


        String quiry = "Select * from " + TABLE_NAME +" where " + "type" + "='" + type + "' AND "  + "id" + "='" + id + "'AND "  + "pwd" + "='" + pwd + "';";

        Log.e("query:",quiry);
        Cursor c = database.rawQuery(quiry, null);
        List<Datatype> tmplist = new ArrayList<Datatype>();

        if (c.getCount() > 0) {

            if (c.moveToFirst()) {

                do {
                    Datatype d = new Datatype();
                    d.setType(c.getString(0));
                    d.setId(c.getString(1));
                    d.setPwd(c.getString(2));



                    tmplist.add(d);

                } while (c.moveToNext());

            }

        }

        if (tmplist.size()==0)
        {
            return false;
        }
        else
        {
            return true;
        }



    }


}
