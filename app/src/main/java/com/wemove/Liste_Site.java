package com.wemove;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    private static final String QUERY_URL =  "http://wemove.herokuapp.com/sites.json?categorie=";
    private static final String QUERY_URL2 =  "&region_nom=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liste_site);

        Bundle i = getIntent().getExtras();
        if (i != null) {
            categ= i.getString("categ");
            region= i.getString("region");
        }
        search(categ, region);
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

    private void search(String searchcateg, String searchRegion)
    {
        String urlStringCateg = "";
        String urlStringReg = "";
        try{
            urlStringCateg = URLEncoder.encode(searchcateg, "UTF-8");
            urlStringReg = URLEncoder.encode(searchRegion, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            //Toast.makeText(this.getActivity(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        //setProgressBarIndeterminateVisibility(true);
        Log.i("Query URL", QUERY_URL + urlStringCateg + QUERY_URL2 + urlStringReg);
        client.get(QUERY_URL + urlStringCateg + QUERY_URL2 + urlStringReg, new AsyncHttpResponseHandler() {
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
            @Override
            public void onFinish() {
                Log.i("Request", "Finish");
            }

        });
    }
}
