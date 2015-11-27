package com.wemove;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Cats2 on 27/11/2015.
 */
public class GeoLocalisation extends Activity implements GeolocFragment.OnFragmentInteractionListener {

    GeolocFragment firstFrag;
    @Override
    public void onFragmentInteraction(Uri uri) {
        // do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.loc_detail);

        // on peut creer le fragment
         firstFrag = GeolocFragment.newInstance();
        // on lui passe les arguments de l'intent qui a déclencher le oncreate
        //firstFrag.setArguments(getIntent().getExtras());

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fgt_container, firstFrag)
                .commit();
    }


}
