package com.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
public class Test extends SQLiteOpenHelper {

	private SQLiteDatabase myDataBase;
	private final Context myContext;
	private static final String DATABASE_NAME = "info";// "db.sqlite";
	@SuppressLint("SdCardPath")
	public final static String DATABASE_PATH = "/data/data/com.android/databases/";
	public static final int DATABASE_VERSION = 1;
	private static final String TABAL_SURVEY = "users";

	public Test(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			Log.v("DB Exists", "db exists");

		} else {

			this.getReadableDatabase();
			try {
				this.close();
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}

		}

	}

	// Check database already exist or not
	private boolean checkDataBase() {
		boolean checkDB = false;
		try {
			String myPath = DATABASE_PATH + DATABASE_NAME;
			File dbfile = new File(myPath);
			checkDB = dbfile.exists();
		} catch (SQLiteException e) {
			System.out.println("delete database file.");
		}
		return checkDB;
	}

	// Copies your database from your local assets-folder to the just created
	// empty database in the system folder
	private void copyDataBase() throws IOException {

		String outFileName = DATABASE_PATH + DATABASE_NAME;

		OutputStream myOutput = new FileOutputStream(outFileName);
		InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myInput.close();
		myOutput.flush();
		myOutput.close();
	}

	// Open database
	public void openDatabase() throws SQLException {
		String myPath = DATABASE_PATH + DATABASE_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	public synchronized void closeDataBase() throws SQLException {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	public void insert(Integer rollnumber,String name, Integer age,String gender,String mobile,String address) {
		openDatabase();

		String quiry = "insert into " + TABAL_SURVEY
				+ " (rollnumber,name,age,gender,mobile,address)" + " values ('" + rollnumber + "' ,'" + name
				+ "','" + age + "','" + gender
				+ "','" + mobile
				+ "','"+ address +"')";

		Log.v("quiry", "" + quiry);
		try {

			myDataBase.execSQL(quiry);
			Log.v("success", "sucess");

		} catch (Exception e) {
			// TODO: handle exception

			Log.v("success", "" + e.toString());

		}

		closeDataBase();
	}

	public void update(String id, String password) {
		openDatabase();
		String quiryup = "update  " + TABAL_SURVEY + " SET password='"
				+ password + "' WHERE id='" + id + "';";

		Log.v("quiry", "" + quiryup);
		try {

			myDataBase.execSQL(quiryup);
			Log.v("success", "sucess");

		} catch (Exception e) {
			// TODO: handle exception
			Log.v("success", "" + e.toString());
		}

		closeDataBase();
	}

	public void delete(Integer id) {
		openDatabase();

		String quy = "delete from " + TABAL_SURVEY + " WHERE rollnumber='" + id + "';";

		Log.v("quiry", "" + quy);
		try {

			myDataBase.execSQL(quy);
			Log.v("success", "sucess");
		} catch (Exception e) {
			Log.v("success", "" + e.toString());
		}
		closeDataBase();

	}

	public List<Datatype> getAlldata() {
		openDatabase();

		String quiry = "Select * from " + TABAL_SURVEY;

		Cursor c = myDataBase.rawQuery(quiry, null);
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

		return tmplist;


	}


	public boolean isValidUser(String type,String id,String pwd) {
		openDatabase();

		String quiry = "Select * from " + TABAL_SURVEY +" where " + "type" + "='" + type + "' AND "  + "id" + "='" + id + "'AND "  + "pwd" + "='" + pwd + "';";

		Log.e("query:",quiry);
		Cursor c = myDataBase.rawQuery(quiry, null);
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

		if (tmplist.size()>0)
		{
			return true;
		}
		else
		{
			return false;
		}

		

	}

	public String showdata(String name) {
		String returns = "";

		String quiry = "Select * from " + TABAL_SURVEY + " WHERE username='"
				+ name + "'";

		Cursor c = myDataBase.rawQuery(quiry, null);

		if (c.getCount() > 0) {

			if (c.moveToFirst()) {

				do {
					returns = c.getString(2);

				} while (c.moveToNext());

			}

		}

		return returns;

	}

}
