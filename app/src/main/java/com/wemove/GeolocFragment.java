package com.wemove;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Cats2 on 27/11/2015.
 */
public class GeolocFragment extends ListFragment implements LocationListener, SensorEventListener {

    private OnFragmentInteractionListener mListener;
    ListView listview;
    ArrayAdapter<String> adapter;
    ArrayList<String> item = new ArrayList<String>();
    private static final String QUERY_URL =  "http://wemove.herokuapp.com/sites.json";
    List<SiteLieu> sites = new ArrayList<SiteLieu>();
    Context context;
    Location myLocation  = new Location("point A");
    Location siteLocation  = new Location("point B");
    SensorManager senSensorManager;
    Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    public static GeolocFragment newInstance() {
        GeolocFragment fragment = new GeolocFragment();
        return fragment;
    }

    public GeolocFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();


        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // Crit�re pour le fournisseur
        Criteria critere = new Criteria();
        // On peut mettre ACCURACY_FINE pour une haute pr�cision ou ACCURACY_COARSE pour une moins bonne pr�cision
        //critere.setAccuracy(Criteria.ACCURACY_FINE);
        // Est-ce que le fournisseur doit �tre capable de donner une altitude ?
        critere.setAltitudeRequired(true);
        // Est-ce que le fournisseur doit �tre capable de donner une direction ?
        critere.setBearingRequired(true);
        // Est-ce que le fournisseur peut �tre payant ?
        critere.setCostAllowed(false);
        ArrayList<LocationProvider> providers = new ArrayList<LocationProvider>();
        ArrayList<String> names = (ArrayList<String>) locationManager.getProviders(critere, true);
        for (String name : names)
            providers.add(locationManager.getProvider(name));

        myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, this);

        senSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.localisation, container, false);

        //listview = (ListView)this.getActivity().findViewById(R.id.liste_loc);
        //On construit un objet qui va faire la passerelle entre le tableau d'objet java et la vue ListView
        //arrayAdapter = new ArrayAdapter<Book>(getActivity(), android.R.layout.simple_list_item_1, Books.getInstance().getBooks());
        search();
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_2,android.R.id.text2, item){
            @Override
        public View getView(int position, View convertView,
                ViewGroup parent) {
            View view =super.getView(position, convertView, parent);

            TextView textView=(TextView) view.findViewById(android.R.id.text2);

            /*YOUR CHOICE OF COLOR*/
            textView.setTextColor(Color.BLACK);

            return view;
        }
    };;
        adapter.notifyDataSetChanged();
        // On associe l'adapter a notre ListView
        //listview.setAdapter(adapter);
        this.setListAdapter(adapter);
        // action de selection d'un objet dans la liste

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float y = event.values[1];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 1000) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                int position = getListView().getFirstVisiblePosition();
                    double diff = y - last_y;
                    diff = diff*10;
                    Double d = new Double(diff);
                    System.out.println(d.intValue());
                    getListView().setSelection(position - d.intValue()/10);
                last_y = y;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }
    @Override
    public void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
        //public void onBookSelected(String id);
    }

    private void search()
    {
        AsyncHttpClient client = new AsyncHttpClient();
        //setProgressBarIndeterminateVisibility(true);
        Log.i("Query URL", QUERY_URL);
        client.get(QUERY_URL, new AsyncHttpResponseHandler() {
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
                        siteLocation.setLatitude(arr.getJSONObject(i).getDouble("latitude"));
                        siteLocation.setLongitude(arr.getJSONObject(i).getDouble("longitude"));

                        float distance = myLocation.distanceTo(siteLocation);
                        distance *= 0.01;
                        int dist = (int) distance;
                        distance = (float)(dist*0.1);
                        SiteLieu sl = new SiteLieu(arr.getJSONObject(i).getInt("id"), arr.getJSONObject(i).getString("nom"), distance);
                        sites.add(sl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Collections.sort(sites, new ComparatorSiteLieu());

                int i = 0;
                for (SiteLieu s : sites)
                {
                    item.add(s.toString());
                    i++;
                }
                adapter.notifyDataSetChanged();
                System.out.println(item);
            }
            @Override
            public void onFinish() {
                Log.i("Request", "Finish");
            }

        });
    }
}
