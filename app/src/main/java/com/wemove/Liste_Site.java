package com.wemove;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by utilisateur on 09/12/2014.
 */
public class Liste_Site extends ListActivity {

    ListView lv;
    AdaptateurListe adaptateur;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayList<Site_select> listId=new ArrayList<Site_select>();
    String categ= "";
    String region="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_site);

        Bundle i = getIntent().getExtras();
        if (i != null) {
            categ= i.getString("categ");
            region= i.getString("region");
        }
        String url = "http://wemove.herokuapp.com/sites.json?categorie=" + categ+"&region_nom=" + region;
        new DLTask().execute(url);
       //values = new String[] {"Device", "Musee Granet"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        adaptateur = new AdaptateurListe(this, listItems);
        this.setListAdapter(adaptateur);

        lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView <?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) lv.getItemAtPosition(position);
                //Site_Select st = new Site_Select(itemValue, listId[itemPosition]);
                Intent intent = new Intent(Liste_Site.this, Site.class);
                intent.putExtra("nom", itemValue);
                intent.putExtra("id", listId.get(itemPosition).getId());
                startActivity(intent);

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public class DLTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {
            try {
                DLUrl dlurl = new DLUrl();
                return dlurl.downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page";
            }
        }

        @Override
        protected void onPostExecute(String result)
        {

            JSONArray arr = null;
            try {
                arr = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < arr.length(); i++)
            {
                try {
                    String nom = arr.getJSONObject(i).getString("nom");
                    int id = arr.getJSONObject(i).getInt("id");
                    listItems.add(nom);
                    listId.add(new Site_select(nom, id));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            Collections.sort(listItems);
            Collections.sort(listId, new Comparatorsite());
            adaptateur.notifyDataSetChanged();

        }
    }
}
