package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class SeenMoviesActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seen_movies);

        String[] setit;
        String content = readFromFile("file.txt");
        System.out.println(content);

        setit = content.split("\n");

        listView =findViewById(R.id.list);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, setit);

        listView.setAdapter(arrayAdapter);          // Asettaa katsottujen elokuvien nimet listViewin elementeiksi
    }

    public String readFromFile (String fileName)    {       // Lukee tiedostosta käyttäjän katsomat elokuvat
        File path = getApplicationContext().getFilesDir();
        System.out.println(getApplicationContext().getFilesDir());
        File readFrom = new File (path, fileName);
        byte[] content = new byte[(int) readFrom.length()];

        try {
            FileInputStream stream = new FileInputStream(readFrom);
            stream.read(content);
            return new String(content);

        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}

