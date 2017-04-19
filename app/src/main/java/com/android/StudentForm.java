package com.android;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import static com.android.Main2Activity.CurrentUser;

public class StudentForm extends AppCompatActivity {
    private DBManager dbManager;
    private SimpleDateFormat dateFormatter;
    Calendar calendar = Calendar.getInstance(Locale.getDefault());
    MyUtility utility;
    EditText et_studentname,et_gradesection,et_parentemail1,et_parentemail2;
    Button btnsave,btncancle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        Log.e("CurrentUser:",CurrentUser);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        dbManager = new DBManager(this);

        dbManager.open();

        utility=new MyUtility(StudentForm.this);


        et_studentname=(EditText)findViewById(R.id.et_studentname);
        et_gradesection=(EditText)findViewById(R.id.et_gradesection);
        et_parentemail1=(EditText)findViewById(R.id.et_parentemail1);
        et_parentemail2=(EditText)findViewById(R.id.et_parentemail2);
        btnsave=(Button)findViewById(R.id.btnsave);
        btncancle=(Button)findViewById(R.id.btncancle);


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (et_studentname.getText().toString().equals("") ){
                    et_studentname.setError("Enter Valid  Name");
                    utility.editTextWatcher(et_studentname);
                }
                else if (et_gradesection.getText().toString().equals("") ){
                    et_gradesection.setError("Enter DOB");
                    utility.editTextWatcher(et_gradesection);
                }

                else if (!et_parentemail1.getText().toString().contains("@"))
                {
                    et_parentemail1.setError("Enter valid mail id");
                    utility.editTextWatcher(et_parentemail1);
                }
                else if (!et_parentemail2.getText().toString().contains("@"))
                {
                    et_parentemail2.setError("Enter valid mail id");
                    utility.editTextWatcher(et_parentemail2);
                }
                else
                {
                    dbManager.insertStudent(et_studentname.getText().toString(),et_gradesection.getText().toString(),et_parentemail1.getText().toString(),et_parentemail2.getText().toString(),dateFormatter.format(calendar.getTime()));

                    Cursor cursor=dbManager.fetchStudents();
                    cursor.moveToFirst();
                    Log.e("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));


                    createPdf();
                    Toast.makeText(StudentForm.this, "Add  Successfull", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }


            }
        });


        btncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });









    }



    private void createPdf() {
        // TODO Auto-generated method stub
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();


        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BuddyPdf";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            Log.d("PDFCreator", "PDF Path: " + path);


//            Calendar c = Calendar.getInstance();
//            SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
//            String formattedDate = df.format(c.getTime());


            File file = new File(dir, Main2Activity.CurrentUser+et_gradesection.getText().toString()+"StudentDetailForm.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            Constant.filepath=file.getAbsolutePath().toString();
            PdfWriter.getInstance(document, fOut);

            //open the document
            document.open();


            Font f3 = new Font(Font.FontFamily.TIMES_ROMAN,40.0f, Font.BOLD, BaseColor.BLACK);

            Font font = new Font(Font.FontFamily.TIMES_ROMAN,25.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk c3 = new Chunk("   Student Detail Form Detail   ", f3);
            c3.setBackground(BaseColor.WHITE);
            Paragraph p1 = new Paragraph("");

            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.add(c3);
            document.add(p1);

            Paragraph p22 = new Paragraph("Absent Student Name :"+et_studentname.getText().toString());
            Font paraFont2= new Font(Font.FontFamily.COURIER,20.0f,0, CMYKColor.GREEN);
            p22.setAlignment(Paragraph.ALIGN_CENTER);
            p22.setFont(font);
            document.add(p22);

            Paragraph p222 = new Paragraph("Grade/Section :"+et_gradesection.getText().toString());
            p222.setAlignment(Paragraph.ALIGN_CENTER);
            p222.setFont(font);
            document.add(p222);

            Paragraph p2 = new Paragraph("Parent Email 1 :"+et_parentemail1.getText().toString());
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p2.setFont(font);
            document.add(p2);


            Paragraph p33 = new Paragraph("Parent Email 2 :"+et_parentemail2.getText().toString());
            p33.setAlignment(Paragraph.ALIGN_CENTER);
            p33.setFont(font);
            document.add(p33);



        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally
        {
            document.close();
        }

    }



}

