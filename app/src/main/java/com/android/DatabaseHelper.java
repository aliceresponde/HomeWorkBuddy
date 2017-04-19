package com.android;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "USERS";
    public static final String TABLE_Buddy = "Buddy";
    public static final String TABLE_Student = "Student";
    public static final String TABLE_Teacher = "TEACHER";

    // Buddy columns
    public static final String _ID = "_id";
    public static final String TODAYDATE = "todaydate";
    public static final String GRADEELEMENT = "grade";
    public static final String SUBJECT = "subject";
    public static final String TEACHER = "teacher";
    public static final String ISQUIZ = "isquiz";
    public static final String FEATURETEST = "featuretest";
    public static final String WORKEDON = "workedon";
    public static final String HANDOUTS = "handouts";
    public static final String ASSIGNMENT = "assignment";
    public static final String DUEDATE = "duedate";

    //Users columns

    public static final String TYPE = "type";
    public static final String ID = "id";
    public static final String PWD = "pwd";



//    Students columns

    public static final String STUDENTNAME = "studentname";
    public static final String GRADE = "grade";
    public static final String PARENTEMAIL1 = "parentemail1";
    public static final String PARENTEMAIL2 = "parentemail2";
    public static final String DATE = "date";

//    teacher columns

    public static final String TEACHERNAME = "teachername";
    public static final String GRADETeacher = "gradeteacher";
    public static final String HANDOUTFILE = "handoutfile";
    public static final String UPLOADFILE = "uploadfile";
    public static final String UPLOADDATE = "date";




    // Database Information
    static final String DB_NAME = "USERS.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TYPE + " TEXT NOT NULL, " + ID + " TEXT," + PWD + " TEXT);";

    private static final String CREATE_TABLE_BUDDY = "create table " + TABLE_Buddy + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TODAYDATE + " TEXT NOT NULL, " + GRADEELEMENT + " TEXT," + SUBJECT + " TEXT," +
            TEACHER +" TEXT," +
            ISQUIZ +" TEXT," +
            FEATURETEST +" TEXT," +
            WORKEDON +" TEXT," +
            HANDOUTS +" TEXT," +
            ASSIGNMENT +" TEXT," +
            DUEDATE + " TEXT);";
    private static final String CREATE_TABLE_STUDENT = "create table " + TABLE_Student + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + STUDENTNAME + " TEXT NOT NULL, " + GRADE + " TEXT," + PARENTEMAIL1 + " TEXT," +
            PARENTEMAIL2 + " TEXT,"+ DATE + " TEXT);";

    private static final String CREATE_TABLE_TEACHER = "create table " + TABLE_Teacher + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TEACHERNAME + " TEXT NOT NULL, " + GRADETeacher + " TEXT," + HANDOUTFILE + " TEXT," + UPLOADFILE + " TEXT," +
            UPLOADDATE + " TEXT);";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_BUDDY);
        db.execSQL(CREATE_TABLE_STUDENT);
        db.execSQL(CREATE_TABLE_TEACHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Buddy);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TEACHER);
        onCreate(db);
    }



}
