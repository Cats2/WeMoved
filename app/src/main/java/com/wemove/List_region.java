package com.wemove;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

public class List_region extends ListActivity {

    ListView listview = null;
    ArrayAdapter<String> adapter;
    ArrayList<String> listItems=new ArrayList<String>();

    private static final String QUERY_URL =  "http://wemove.herokuapp.com/regions.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_region);

        //new DLTask().execute("http://wemove.herokuapp.com/regions.json");
        search("");
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
                System.out.println(response);
                JSONArray arr = null;
                try {
                    arr = new JSONArray(response);
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
                //setProgressBarIndeterminateVisibility(false);
            }
            @Override
            public void onFinish() {
                Log.i("Request", "Finish");
            }
            /*@Override
            public void onFailure(int statusCode, Throwable e, JSONArray errorResponse) {
                Log.e("Erreur request", "Failure");
                //getActivity().setProgressBarIndeterminateVisibility(false);
            }*/
        });
    }
}
