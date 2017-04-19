package com.android;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.R;
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
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.android.Main2Activity.CurrentUser;

public class TeacherForm extends AppCompatActivity {
    MyUtility utility;
    private SimpleDateFormat dateFormatter;
    Calendar calendar = Calendar.getInstance(Locale.getDefault());
    private DBManager dbManager;
    Button SubmitTeacher,btn_upload;
EditText et_teachername,et_grade,et_handoutile,et_filepath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_form);

        Log.e("CurrentUser:",CurrentUser);
        dbManager = new DBManager(this);

        dbManager.open();

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        utility=new MyUtility(TeacherForm.this);


        SubmitTeacher=(Button)findViewById(R.id.submit_teacher);
        btn_upload=(Button)findViewById(R.id.btn_upload);
        et_teachername=(EditText)findViewById(R.id.et_teachername);
        et_grade=(EditText)findViewById(R.id.et_grade);
        et_handoutile=(EditText)findViewById(R.id.et_handoutile);
        et_filepath=(EditText)findViewById(R.id.et_filepath);


        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDocument();
            }
        });

        SubmitTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_teachername.getText().toString().equals("") ){
                    et_teachername.setError("Enter Valid  Name");
                    utility.editTextWatcher(et_teachername);
                }
                else if (et_grade.getText().toString().equals("") ){
                    et_grade.setError("Enter Grade");
                    utility.editTextWatcher(et_grade);
                }
                else if (et_handoutile.getText().toString().equals("") ){
                    et_handoutile.setError("Enter Handout File Name");
                    utility.editTextWatcher(et_handoutile);
                }
                else if (et_filepath.getText().toString().equals("") ){
                    et_filepath.setError("Select File");
                    utility.editTextWatcher(et_filepath);
                }
                else
                {

                    dbManager.insertTeacher(et_teachername.getText().toString(),et_grade.getText().toString(),et_handoutile.getText().toString(),et_filepath.getText().toString(),dateFormatter.format(calendar.getTime()));
                    Cursor cursor=dbManager.fetchTeacher();
                    cursor.moveToFirst();
                    Log.e("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));

                    createPdf();

                    Toast.makeText(TeacherForm.this, "Add  Successfull", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }
        });
    }


    private void getDocument()
    {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("application/pdf");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
//        startActivityForResult(intent, 444);



        Intent intent = new Intent();
        //intent.setType("pdf/*");
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 555);



    }


    @Override
    protected void onActivityResult(int req, int result, Intent data)
    {
        // TODO Auto-generated method stub
        super.onActivityResult(req, result, data);

        Log.e("result",""+result);
        if (result == RESULT_OK)
        {


            Uri fileuri = data.getData();

            try {
                et_filepath.setText(getFilePath(TeacherForm.this,fileuri));


//                SharedPreferences prefs = getSharedPreferences("HomeBuddy", MODE_PRIVATE);
//                editor.putString("name", "Elena");
//                editor.putInt("idName", 12);
//                editor.commit();

                SharedPreferences preferences = getSharedPreferences("HomeBuddy", getApplicationContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("filepath", getFilePath(TeacherForm.this,fileuri));
                editor.commit();

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

// get file path




    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
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


            File file = new File(dir, CurrentUser+et_grade.getText().toString()+"TeacherForm.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(document, fOut);

            //open the document
            document.open();


            Font f3 = new Font(Font.FontFamily.TIMES_ROMAN,40.0f, Font.BOLD, BaseColor.BLACK);

            Font font = new Font(Font.FontFamily.TIMES_ROMAN,25.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk c3 = new Chunk("   Teacher Form Detail   ", f3);
            c3.setBackground(BaseColor.WHITE);
            Paragraph p1 = new Paragraph("");

            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.add(c3);
            document.add(p1);

            Paragraph p22 = new Paragraph("Teacher Name :"+et_teachername.getText().toString());
            Font paraFont2= new Font(Font.FontFamily.COURIER,20.0f,0, CMYKColor.GREEN);
            p22.setAlignment(Paragraph.ALIGN_CENTER);
            p22.setFont(font);
            document.add(p22);

            Paragraph p222 = new Paragraph("Grade :"+et_grade.getText().toString());
            p222.setAlignment(Paragraph.ALIGN_CENTER);
            p222.setFont(font);
            document.add(p222);

            Paragraph p2 = new Paragraph("Handout File :"+et_handoutile.getText().toString());
            p2.setAlignment(Paragraph.ALIGN_CENTER);
            p2.setFont(font);
            document.add(p2);


            Paragraph p33 = new Paragraph("Uploaded File :"+et_filepath.getText().toString());
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
