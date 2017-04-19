package com.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.DBManager;
import com.android.StudentForm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AdminForm extends AppCompatActivity {

/*InputMethodManager inputManager =
        (InputMethodManager) context.
            getSystemService(Context.INPUT_METHOD_SERVICE);
inputManager.hideSoftInputFromWindow(
        this.getCurrentFocus().getWindowToken(),
        InputMethodManager.HIDE_NOT_ALWAYS);
InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
mgr.hideSoftInputFromWindow(curEditText.getWindowToken(), 0);*/

    String email, subject, message, attachmentFile;
    Uri URI = null;

    List<String> parentIds;
    Button btnstudentdetails, btnemail;
    private SimpleDateFormat dateFormatter;
    Calendar calendar = Calendar.getInstance(Locale.getDefault());
    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_form);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        dbManager = new DBManager(this);

        dbManager.open();

        parentIds=new ArrayList<String>();
        btnstudentdetails = (Button) findViewById(R.id.btnstudentdetails);
        btnemail = (Button) findViewById(R.id.btnemail);

        btnstudentdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminForm.this, StudentForm.class));
            }
        });
        btnemail.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // TODO Auto-generated method stub
                List<String> assignmentlist=new ArrayList<String>();
                assignmentlist=dbManager.fetchTodayAssignmentFilePath(dateFormatter.format(calendar.getTime()));

//                for (int i=0;i<assignmentlist.size();i++)
//                {
//                    Log.e("assignmentlist :"+i,assignmentlist.get(i));
//                }

                try {

                    List<String> emaillist=new ArrayList<String>();
                    emaillist=dbManager.fetchTodayStudents(dateFormatter.format(calendar.getTime()));

                    if(emaillist.size()>0)
                    {
                        SharedPreferences preferences = getSharedPreferences("HomeBuddy", getApplicationContext().MODE_PRIVATE);
                        String filespath=preferences.getString("filepath","filepath");

//                    URI = Uri.parse("file://" + Constant.filepath);
                        URI = Uri.parse("file://" + filespath);

                        subject = "Assignment";
                        message = "";

                        final Intent emailIntent = new Intent(
                                android.content.Intent.ACTION_SEND_MULTIPLE);
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                                emaillist.toArray(new String[emaillist.size()]));

                        ArrayList<Uri> uris = new ArrayList<Uri>();
                        for (int abc=0;abc<assignmentlist.size();abc++) {
                            String path = assignmentlist.get(abc);

                            uris.add(Uri.parse("file://" + path));

                        }

                        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                                subject);


                        emailIntent
                                .putExtra(android.content.Intent.EXTRA_TEXT, message);
                        startActivity(Intent.createChooser(emailIntent,
                                "Sending email..."));
                    }
                    else
                    {
                        Toast.makeText(AdminForm.this,"No Data Available Today",Toast.LENGTH_SHORT).show();
                    }



                } catch (Throwable t) {
                    Toast.makeText(AdminForm.this,
                            "Request failed try again: " + t.toString(),
                            Toast.LENGTH_LONG).show();
                }


//                Log.e("MAil sending to", DatabaseUtils.dumpCursorToString(cursor));

//                new Thread(new Runnable() {
//
//                    public void run() {
//
//                        try {
//
//                            GMailSender sender = new GMailSender(
//
//                                    "funapptech@gmail.com",
//
//                                    "test123456789");
//
//                            sender.addAttachment(Constant.filepath);
//
//                            sender.sendMail("Test mail", "This mail has been sent from android app along with attachment",
//
//                                    "funapptech@gmail.com",
//
//                                    "ravisharmabpit@gmail.com");
//
//                        } catch (Exception e) {
//
//                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
//
//                        }
//
//                    }
//
//                }).start();

            }

        });


    }

}