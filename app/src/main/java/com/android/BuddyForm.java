package com.android;
import com.android.R;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.Main2Activity;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import com.android.R;
import static com.android.Main2Activity.CurrentUser;

public class BuddyForm extends AppCompatActivity {

    private android.app.DatePickerDialog fromDatePickerDialog;
    private android.app.DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    Calendar calendar = Calendar.getInstance(Locale.getDefault());
    private com.android.DBManager dbManager;
    private RadioButton rdyes, rdno;
    private RadioGroup radioGroup;
    com.android.MyUtility utility;
    Button submitbuddy;
    String isQuiz;
    EditText et_subject, et_todaydate, et_grade, et_featureTest, et_workedOn, et_handouts, et_assignDate, et_dueDate, et_teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buddy_form);

        Log.e("CurrentUser:", CurrentUser);


        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        submitbuddy = (Button) findViewById(R.id.submit_buddy);

        dbManager = new com.android.DBManager(this);

        dbManager.open();
        radioGroup = (RadioGroup) findViewById(R.id.radioquiz);
        rdno = (RadioButton) findViewById(R.id.rdno);
        rdyes = (RadioButton) findViewById(R.id.rdyes);

        et_todaydate = (EditText) findViewById(R.id.et_todaydate);
        et_grade = (EditText) findViewById(R.id.et_grade);
        et_subject = (EditText) findViewById(R.id.et_subject);
//        et_testName=(EditText)findViewById(R.id.et_testName) ;
        et_featureTest = (EditText) findViewById(R.id.et_featureTest);
        et_workedOn = (EditText) findViewById(R.id.et_workedOn);
        et_handouts = (EditText) findViewById(R.id.et_handouts);
        et_assignDate = (EditText) findViewById(R.id.et_assignDate);
        et_dueDate = (EditText) findViewById(R.id.et_dueDate);
        et_teacher = (EditText) findViewById(R.id.et_teacher);
        utility = new com.android.MyUtility(BuddyForm.this);


        setDateTimeField();


        et_todaydate.setText(dateFormatter.format(calendar.getTime()));

        et_featureTest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    fromDatePickerDialog.show();


                }
            }
        });

        et_dueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    toDatePickerDialog.show();


                }
            }
        });


        submitbuddy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_grade.getText().toString().equals("")) {
                    et_grade.setError("Enter Grade");
                    utility.editTextWatcher(et_grade);
                } else if (et_subject.getText().toString().equals("")) {
                    et_subject.setError("Enter subject");
                    utility.editTextWatcher(et_subject);
                } else if (et_teacher.getText().toString().equals("")) {
                    et_teacher.setError("Enter Teacher");
                    utility.editTextWatcher(et_teacher);
                }
//                else if (et_testName.getText().toString().equals("") ) {
//
//                    et_testName.setError("Enter testName");
//                    utility.editTextWatcher(et_testName);
//
//                }

                else if (et_featureTest.getText().toString().equals("")) {

                    et_featureTest.setError("Enter FeatureTest Date");
                    utility.editTextWatcher(et_featureTest);

                } else if (et_workedOn.getText().toString().equals("")) {
                    et_workedOn.setError("Enter Worked On");
                    utility.editTextWatcher(et_workedOn);
                } else if (et_assignDate.getText().toString().equals("")) {
                    et_assignDate.setError("Enter Assignment");
                    utility.editTextWatcher(et_assignDate);
                } else if (et_dueDate.getText().toString().equals("")) {
                    et_dueDate.setError("Enter Due Date");
                    utility.editTextWatcher(et_dueDate);
                } else {


                    if (radioGroup.getCheckedRadioButtonId() == R.id.rdyes) {
                        isQuiz = "yes";

                    } else {
                        isQuiz = "no";
                    }

                    createPdf();
                    dbManager.insertBuddy(et_todaydate.getText().toString(), et_grade.getText().toString(), et_subject.getText().toString(), et_teacher.getText().toString(), isQuiz, et_featureTest.getText().toString(), et_workedOn.getText().toString(), et_handouts.getText().toString(), et_assignDate.getText().toString(), et_dueDate.getText().toString());

                    Cursor cursor = dbManager.fetchBuddy();
                    cursor.moveToFirst();
                    Log.e("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));

                    Toast.makeText(BuddyForm.this, "Add  Successfull", Toast.LENGTH_LONG).show();

                    onBackPressed();

                }


            }
        });
    }


    private void showMessageDialog(final boolean exit) {                       //Exit Dialog
        final android.app.Dialog popuplayout = new android.app.Dialog(BuddyForm.this);
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


    private void createPdf() {
        // TODO Auto-generated method stub
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();


        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BuddyPdf";

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);


//            Calendar c = Calendar.getInstance();
//            SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
//            String formattedDate = df.format(c.getTime());


            File file = new File(dir, Main2Activity.CurrentUser + et_grade.getText().toString() + "BuddyForm.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(document, fOut);

            //open the document
            document.open();


            Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 40.0f, Font.BOLD, BaseColor.BLACK);

            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 25.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk c3 = new Chunk("   Student Form Detail   ", f3);
            c3.setBackground(BaseColor.WHITE);
            Paragraph p1 = new Paragraph("");

            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.add(c3);
            document.add(p1);

            Paragraph p22 = new Paragraph("Current Date :" + et_todaydate.getText().toString());
            Font paraFont2 = new Font(Font.FontFamily.COURIER, 20.0f, 0, CMYKColor.GREEN);
            p22.setAlignment(Paragraph.ALIGN_CENTER);
            p22.setFont(font);
            document.add(p22);

            Paragraph p222 = new Paragraph("Grade/Element :" + et_grade.getText().toString());
            p222.setAlignment(Paragraph.ALIGN_CENTER);
            p222.setFont(font);
            document.add(p222);

            Paragraph p2 = new Paragraph("Subject Name :" + et_subject.getText().toString());
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p2.setFont(font);
            document.add(p2);


            Paragraph p33 = new Paragraph("Teacher :" + et_teacher.getText().toString());
            p33.setAlignment(Paragraph.ALIGN_CENTER);
            p33.setFont(font);
            document.add(p33);

            Paragraph p34 = new Paragraph("Quiz Today :" + isQuiz);
            p34.setAlignment(Paragraph.ALIGN_CENTER);
            p34.setFont(font);
            document.add(p34);

            Paragraph p35 = new Paragraph("Feature Test :" + et_featureTest.getText().toString());
            p35.setAlignment(Paragraph.ALIGN_CENTER);
            p35.setFont(font);
            document.add(p35);


            Paragraph p5 = new Paragraph("Worked On :" + et_workedOn.getText().toString());

            p5.setAlignment(Paragraph.ALIGN_CENTER);
            p5.setFont(font);
            document.add(p5);

            Paragraph p6 = new Paragraph("Handouts :" + et_handouts.getText().toString());

            p6.setAlignment(Paragraph.ALIGN_CENTER);
            p6.setFont(font);
            document.add(p6);

            Paragraph p7 = new Paragraph("Assignment :" + et_assignDate.getText().toString());

            p7.setAlignment(Paragraph.ALIGN_CENTER);
            p7.setFont(font);
            document.add(p7);

            Paragraph p8 = new Paragraph("Due Date :" + et_dueDate.getText().toString());

            p8.setAlignment(Paragraph.ALIGN_CENTER);
            p8.setFont(font);
            document.add(p8);


        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            document.close();
        }

    }


    private void setDateTimeField() {


        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new android.app.DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_featureTest.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new android.app.DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_dueDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


}