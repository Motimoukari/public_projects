package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

class Event implements Serializable {           // Luokka johon kerätään elokuvan tietoja

String title;
String year;
String startTime;
String length;
String ratingUrl;
String posterUrl;

Event(String title, String year, String startTime, String length, String ratingUrl, String posterUrl) {
    this.title = title;
    this.year = year;
    this.startTime = startTime;
    this.length = length;
    this.ratingUrl = ratingUrl;
    this.posterUrl = posterUrl;
}

}

public class MainActivity extends AppCompatActivity {

    Context context = null;
    ListView listView;
    Event event = new Event("", "", "", "", "", "");
    ArrayList<Event> events = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Button seenButton = findViewById(R.id.seenButton);

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            String urlString = "https://www.finnkino.fi/xml/Schedule/?area=1041&dt=31.07.2022";

            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            System.out.println("Root Element: " + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getDocumentElement().getElementsByTagName("Show");

            for (int i = 0; i < nList.getLength() ; i++)  {         // Looppi joka tallentaa jokaisen elokuvan tiedot olioon

                Node node = nList.item(i);
                System.out.println("Element in this: " + node.getNodeName());

                if (node.getNodeType() == Node.ELEMENT_NODE)  {
                    Element element = (Element) node;


                    events.add(new Event(element.getElementsByTagName("Title").item(0).getTextContent(), element.getElementsByTagName("ProductionYear").item(0).getTextContent(), element.getElementsByTagName("dttmShowStart").item(0).getTextContent(), element.getElementsByTagName("LengthInMinutes").item(0).getTextContent(),
                            element.getElementsByTagName("RatingImageUrl").item(0).getTextContent(), element.getElementsByTagName("EventMediumImagePortrait").item(0).getTextContent()  ));
                    titles.add(element.getElementsByTagName("Title").item(0).getTextContent());

                }

            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        catch (SAXException e)  {
            e.printStackTrace();
        }
        catch (ParserConfigurationException e)  {
            e.printStackTrace();
        }
        finally {
            System.out.println("### DONE ###");
        }

        listView=findViewById(R.id.list);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,titles);         // Voisiko otsikot esittää fiksummin kuin tekemällä niitä varten uuden arraylistin "titles"?

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()   {
            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)   {           // Avaa Aktiviteetin jossa näytetään yksittäisen elokuvan tietoja
                System.out.println(events.get(i).title);
                System.out.println(events.get(i).year);

                Intent intent = new Intent(getBaseContext(), DetailActivity.class);

                intent.putExtra("details", events.get(i));

                startActivity(intent);

            }
        }) ;

        seenButton.setOnClickListener(new View.OnClickListener() {              // Avaa aktiviteetin jossa näytetään käyttäjän katsomat elokuvat ja niiden arvostelut
            @Override
            public void onClick(View view) {

                System.out.println("testi");

                Intent intent = new Intent(getBaseContext(), SeenMoviesActivity.class);

                startActivity(intent);

            }
        });



    }



}