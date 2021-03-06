package com.wemove;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;

/**
 * Created by utilisateur on 13/12/2014.
 */
public class Site extends Activity {

    HorizontalScrollView horizontalScroll;
    TextView info;
    TextView descr;
    TextView nom_site;
    TextView addr;
    TextView adr_web;
    TextView tel;
    TextView tarif;
    String adresse;
    String nom;
    String web;
    String description;
    String telephone;
    String tarif_normal;
    CheckBox reduc;
    String reduction;
    CheckBox groupe_box;
    String groupe;
    CheckBox audio_box;
    String audioguide;
    CheckBox guide_box;
    String guide;
    ImageView img;
    int id;
    private static final String QUERY_URL =  "http://wemove.herokuapp.com/sites.json?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site);
        Bundle i = getIntent().getExtras();
        if (i != null) {
            nom= i.getString("nom");
            id= i.getInt("id");
        }
        System.out.println("nom = " + nom);
        String urlparam = Integer.toString(id);
        search(urlparam);
        // Nom du site
        nom_site = (TextView) findViewById(R.id.nom_site);
        addr = (TextView) findViewById(R.id.addr);
        adr_web = (TextView) findViewById(R.id.web);
        tel = (TextView) findViewById(R.id.tel);
        img = (ImageView) findViewById(R.id.imageView);

        //Description
        descr = (TextView) findViewById(R.id.descr);
        descr.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(descr.getMaxLines() == 4)
                    descr.setMaxLines(30);
                else
                    descr.setMaxLines(4);
            }
        });

        //descr.setText(nom);

        // Rating bar
        //RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        //ratingBar.setRating(Float.parseFloat("4.0"));
        //LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        //stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);

        // Information : Tarif
        info = (TextView) findViewById(R.id.info);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout tarif = (LinearLayout) findViewById(R.id.layout_info);

                if (tarif.getVisibility() == View.GONE)
                {
                    tarif.setVisibility(LinearLayout.VISIBLE);
                }
                else
                {
                    tarif.setVisibility(LinearLayout.GONE);
                }
            }

        });

        tarif = (TextView) findViewById(R.id.tarif);
        reduc = (CheckBox) findViewById(R.id.reduc);
        groupe_box = (CheckBox) findViewById(R.id.groupe);
        audio_box = (CheckBox) findViewById(R.id.audioguide);
        guide_box = (CheckBox) findViewById(R.id.guide);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.site_menu, menu);
        return true;
    }

    private void search(String searchString)
    {
        String urlString = "";
        try{
            urlString = URLEncoder.encode(searchString, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            //Toast.makeText(this.getActivity(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        //setProgressBarIndeterminateVisibility(true);
        Log.i("Query URL", QUERY_URL + urlString);
        client.get(QUERY_URL + urlString, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                Log.i("Request", "Success");
                JSONArray arr = null;
                try {
                    arr = new JSONArray(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < arr.length(); i++)
                {
                    try {
                        nom = arr.getJSONObject(i).getString("nom");
                        adresse = arr.getJSONObject(i).getString("adresse");
                        web = arr.getJSONObject(i).getString("web");
                        description = arr.getJSONObject(i).getString("description");
                        telephone = arr.getJSONObject(i).getString("tel");
                        tarif_normal = arr.getJSONObject(i).getString("tarif");
                        reduction = arr.getJSONObject(i).getString("reduction");
                        groupe = arr.getJSONObject(i).getString("reduction");
                        audioguide = arr.getJSONObject(i).getString("reduction");
                        guide = arr.getJSONObject(i).getString("reduction");
                        id = arr.getJSONObject(i).getInt("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                System.out.println("id : " + EnumImg.Image.values()[id]);
                nom_site.setText(nom);
                descr.setText(description);
                addr.setText(adresse);
                adr_web.setText(web);
                tel.setText(telephone);
                img.setImageResource(EnumImg.Image.values()[id-1].getDrawable());
                tarif.setText("Tarif Normal : " + tarif_normal);
                if(reduction.equals("1"))
                {
                    reduc.setChecked(true);
                }
                if(groupe.equals("1"))
                {
                    groupe_box.setChecked(true);
                }
                if(audioguide.equals("1"))
                {
                    audio_box.setChecked(true);
                }
                if(guide.equals("1"))
                {
                    guide_box.setChecked(true);
                }
            }
            @Override
            public void onFinish() {
                Log.i("Request", "Finish");
            }
        });
    }

}
