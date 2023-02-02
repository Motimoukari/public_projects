package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DetailActivity extends AppCompatActivity {
    private static final String FILE_NAME = "file.txt";
    EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Event eventDetail = (Event) getIntent().getSerializableExtra("details");

        TextView title = (TextView) findViewById(R.id.titleView);
        title.setText(eventDetail.title);

        TextView year = (TextView) findViewById(R.id.yearView);
        year.setText(eventDetail.year);

        TextView start = (TextView) findViewById(R.id.timeView);
        start.setText("Movie starts at: " + eventDetail.startTime);

        TextView length = (TextView) findViewById(R.id.lengthView);

        length.setText("Length of movie: " + eventDetail.length + " minuuttia");

        inputText = (EditText) findViewById(R.id.scoreForm);

        new DownloadImageFromInternet((ImageView) findViewById(R.id.ratingView)).execute(eventDetail.ratingUrl);

        new DownloadImageFromInternet((ImageView) findViewById(R.id.posterView)).execute(eventDetail.posterUrl);

        Button saveSeen = findViewById(R.id.seenButton);


        System.out.println(getApplicationContext().getFilesDir());

        saveSeen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                writeToFile("file.txt", eventDetail.title + ";" + inputText.getText().toString());

            }
        });

    }


    public void writeToFile(String fileName, String content)    {           // Kirjoittaa tiedostoon katsotun elokuvan nimen sekä annetun arvosanan

        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, fileName), true);
            writer.write(content.getBytes());
            writer.write(10);
            writer.flush();
            writer.close();
            Toast.makeText(getApplicationContext(), "Wrote to file: " + fileName, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {           // Lataa internetistä kuvan url:n perusteella
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;

        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
