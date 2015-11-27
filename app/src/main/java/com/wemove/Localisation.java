package com.wemove;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by utilisateur on 14/12/2014.
 */
public class Localisation extends Activity implements LocationListener{

    double mylatitude;
    double mylongitude;
    ListView listview;
    Location locationB = new Location("point B");
    Location locationA = new Location("point A");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.localisation);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
        // Pour indiquer la consommation d'�nergie demand�e
        // Criteria.POWER_HIGH pour une haute consommation, Criteria.POWER_MEDIUM pour une consommation moyenne et Criteria.POWER_LOW pour une basse consommation
        //critere.setPowerRequirement(Criteria.POWER_HIGH);
        // Est-ce que le fournisseur doit �tre capable de donner une vitesse ?
        //critere.setSpeedRequired(true);

        ArrayList<LocationProvider> providers = new ArrayList<LocationProvider>();
        ArrayList<String> names = (ArrayList<String>) locationManager.getProviders(critere, true);
        for (String name : names)
            providers.add(locationManager.getProvider(name));

        locationA = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 100, this);

        System.out.println("GPS: Latitude " + locationA.getLatitude() + " et longitude " + locationA.getLongitude());


        locationB.setLatitude(43.525672);
        locationB.setLongitude(5.452469);

        float distance = locationA.distanceTo(locationB);
        distance *= 0.01;
        int dist = (int) distance;
        distance = (float)(dist*0.1);
        //listview = (ListView) findViewById(R.id.list);

        String[] item = new String[]{"Mus�e Granet  :" + distance + " km"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1, item);
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);

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
    public void onLocationChanged(Location location) {
        mylatitude = location.getLatitude();
        mylongitude = location.getLongitude();
        Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());

        locationA.setLatitude(mylatitude);
        locationB.setLongitude(mylongitude);
        locationB.setLatitude(43.525672);
        locationB.setLongitude(5.452469);

        float distance = locationA.distanceTo(locationB);
        distance *= 0.01;
        int dist = (int) distance;
        distance = (float)(dist*0.1);
        //listview = (ListView) findViewById(R.id.list);

        String[] item = new String[]{"Mus�e Granet  :" + distance + " km"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1, item);
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);
        listview.invalidateViews();

    }
}
