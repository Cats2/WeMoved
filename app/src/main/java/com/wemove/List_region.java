package com.wemove;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class List_region extends ListActivity {

    ListView listview = null;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_region);

        new DLTask().execute("http://wemove.herokuapp.com/regions.json");
        adapter = new ArrayAdapter<String>(this,R.layout.list_region,R.id.labreg, listItems);
        this.setListAdapter(adapter);
        listview = getListView();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView <?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listview.getItemAtPosition(position);
                Intent intent = new Intent(List_region.this, Categorie.class);
                intent.putExtra("region", itemValue);
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
            System.out.println(result);
            JSONArray arr = null;
            try {
                arr = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(arr);
            for (int i = 0; i < arr.length(); i++)
            {
                try {
                    String nom = arr.getJSONObject(i).getString("nom");
                    listItems.add(nom);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            Collections.sort(listItems);
            adapter.notifyDataSetChanged();

        }
    }
}
