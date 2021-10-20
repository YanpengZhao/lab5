package com.example.lab5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity3 extends AppCompatActivity {
    int noteid=-1;
    EditText EditText3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        EditText3=(EditText) findViewById(R.id.EditText3);
        Intent intent=getIntent();
        noteid=intent.getIntExtra("noteid",-1);

        if(noteid!=-1){
            Note note=MainActivity2.notes.get(noteid);
            String noteContent=note.getContent();
            Log.d("file name",""+noteContent);
            Log.d("file content",readContent(noteContent));
            EditText3.setText(readContent(noteContent));
        }

    }
    public String readContent(String filename) {

        StringBuilder content = new StringBuilder();
        try {
            FileInputStream fis = this.openFileInput(filename);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);

            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();
            while (line != null) {
                content.append(line).append("\n");
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }
    public void saveMethod(View view){
        EditText3=(EditText) findViewById(R.id.EditText3);
        String content=EditText3.getText().toString();
        String fileContents=EditText3.getText().toString();
        Context context=getApplicationContext();
        SQLiteDatabase sqLiteDatabase=context.openOrCreateDatabase("notes",Context.MODE_PRIVATE,null);
        DBHelper dbHelper=new DBHelper(sqLiteDatabase);
        SharedPreferences sharedPreferences=getSharedPreferences("com.example.lab5",Context.MODE_PRIVATE);
        String str=sharedPreferences.getString(MainActivity.usernameKey,"");
        String username=str;
        String title;
        DateFormat dateFormat=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String date=dateFormat.format(new Date());
        if(noteid==-1){
            title="NOTE_"+(MainActivity2.notes.size()+1);
            content=username+title;
            try(FileOutputStream fileOutputStream = getApplicationContext().openFileOutput(content, Context.MODE_PRIVATE)) {
                fileOutputStream.write(fileContents.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dbHelper.saveNotes(username,title,content,date);
        }else{
            title="NOTE_"+(noteid+1);
            content=username+title;
            try(FileOutputStream fileOutputStream = getApplicationContext().openFileOutput(content, Context.MODE_PRIVATE)) {
                fileOutputStream.write(fileContents.getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            dbHelper.updateNote(title,date,content,username);
        }
        Intent intent=new Intent(this,MainActivity2.class);
        startActivity(intent);
    }
}