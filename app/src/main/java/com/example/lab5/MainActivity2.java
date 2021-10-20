package com.example.lab5;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    TextView textView2;
    public static ArrayList<Note> notes=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView2=(TextView) findViewById(R.id.textview2);
        Intent intent=getIntent();
        SharedPreferences sharedPreferences=getSharedPreferences("com.example.lab5",Context.MODE_PRIVATE);
        String str=sharedPreferences.getString(MainActivity.usernameKey,"");
        textView2.setText("Welcome "+str+"!");
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("notes", Context.MODE_PRIVATE,null);
        DBHelper dbHelper=new DBHelper(sqLiteDatabase);
        notes=dbHelper.readNotes(str);
        ArrayList<String> displayNotes=new ArrayList<>();
        for (Note note:notes){
            displayNotes.add(String.format("Titles:%s\nDate%s",note.getTitle(),note.getDate()));
        }
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,displayNotes);
        ListView listView=(ListView) findViewById(R.id.notesListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent(getApplicationContext(), MainActivity3.class);
                intent.putExtra("noteid",position);
                startActivity(intent);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.logout){
            Intent  intent=new Intent(this,MainActivity.class);
            SharedPreferences sharedPreferences=getSharedPreferences("com.example.lab5", Context.MODE_PRIVATE);
            sharedPreferences.edit().remove(MainActivity.usernameKey).apply();
            startActivity(intent);
            return true;
        }
        if(item.getItemId()==R.id.addNote){
            Intent intent=new Intent(getApplicationContext(),MainActivity3.class);
            startActivity(intent);
            return true;
        }
        return false;
//respond to menu item selection
    }

}