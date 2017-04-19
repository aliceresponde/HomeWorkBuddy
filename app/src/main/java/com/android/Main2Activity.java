package com.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    List<Datatype> ls;

    public EditText editTextUserName;
    public EditText editTextPassword;
    Button btnLogin;
    Spinner spinner;
    private DBManager dbManager;

    public static String CurrentUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        if (PermissionsHelper.areExplicitPermissionsRequired()) {
            PermissionsHelper.show(Main2Activity.this);

        }

        editTextUserName = (EditText) findViewById(R.id.login_username);
        editTextPassword = (EditText) findViewById(R.id.login_password);
////        Context context=getApplication();
////        InputMethodManager inputManager =
////                (InputMethodManager) context.
////                        getSystemService(Context.INPUT_METHOD_SERVICE);
////        inputManager.hideSoftInputFromWindow(
////                this.getCurrentFocus().getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);
        btnLogin = (Button) findViewById(R.id.login_btn);


        dbManager = new DBManager(this);

        dbManager.open();

        if (dbManager.fetch().getCount() <= 0) {
            dbManager.insert("admin", "admin", "admin");
            dbManager.insert("teacher", "teacher1", "teacher1");
            dbManager.insert("teacher", "teacher2", "teacher2");
            dbManager.insert("teacher", "teacher3", "teacher3");
            dbManager.insert("student", "student1", "student1");
            dbManager.insert("student", "student2", "student2");
            dbManager.insert("student", "student3", "student3");
        }
        
        spinner = (Spinner) findViewById(R.id.android_material_design_spinner);
//        spinner.getBackground().setColorFilter(Color.parseColor("#053589"), PorterDuff.Mode.SRC_ATOP);
//        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Admin");
        categories.add("Teacher");
        categories.add("Student");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // get The User name and Password
                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();


//               Cursor cursor=dbManager.fetch();
//                cursor.moveToFirst();
//                Log.e("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));

                if (dbManager.isValidUser(spinner.getSelectedItem().toString().toLowerCase(), userName, password)) {

                    Log.e("Position:", spinner.getSelectedItemPosition() + "");
                    if (spinner.getSelectedItemPosition() == 0) {
                        CurrentUser = editTextUserName.getText().toString();
                        Log.e("Position:", spinner.getSelectedItemPosition() + "");
                        Intent i = new Intent(Main2Activity.this, AdminForm.class);
                        startActivity(i);


                    } else if (spinner.getSelectedItemPosition() == 1) {
                        CurrentUser = editTextUserName.getText().toString();
                        Log.e("Position:", spinner.getSelectedItemPosition() + "");
                        Intent i = new Intent(Main2Activity.this, TeacherForm.class);
                        startActivity(i);
                    } else {

                        CurrentUser = editTextUserName.getText().toString();
                        Log.e("Position:", spinner.getSelectedItemPosition() + "");
                        Intent i = new Intent(Main2Activity.this, BuddyForm.class);
                        startActivity(i);

                    }
                } else {
                    Toast.makeText(Main2Activity.this, "Invalid UserID or Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onBackPressed() {

        showMessageDialog(true);
    }

    private void showMessageDialog(final boolean exit) {                       //Exit Dialog
        final android.app.Dialog popuplayout = new android.app.Dialog(Main2Activity.this);
        popuplayout.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popuplayout.setContentView(R.layout.exit_app_dialog);
        popuplayout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button btnYes = (Button) popuplayout.findViewById(R.id.btnYes);
        Button btnNo = (Button) popuplayout.findViewById(R.id.btnNo);
        TextView tittle = (TextView) popuplayout.findViewById(R.id.txtdialogtitle);
        tittle.setText("");
        TextView msg = (TextView) popuplayout.findViewById(R.id.txtdialogMsg);
        if (!exit)
            msg.setText("Do you want to exit from application?");
        else
            msg.setText("Do you want to exit from application?");
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        popuplayout.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(popuplayout.getWindow().getAttributes());
        lp.width = (int) (width - (width * 0.20));
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        popuplayout.getWindow().setAttributes(lp);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popuplayout.cancel();

                if (!exit) {
                    finish();
                } else {
                    moveTaskToBack(true);
                    finish();
                }
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popuplayout.cancel();
            }
        });
    }



}
