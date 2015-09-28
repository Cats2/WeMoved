package com.wemove;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by utilisateur on 09/12/2014.
 */
public class Categorie extends Activity {

    String region = "";
    TextView titre_categ = null;

    private Button bmusee = null;
    private Button bsitenat = null;
    private Button bdiv = null;
    private Button bmonum = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categorie);

        Bundle i = getIntent().getExtras();
        if (i != null) {
            region= i.getString("region");
        }
        titre_categ = (TextView) findViewById(R.id.textView);
        titre_categ.setText(region);

        bsitenat = (Button) findViewById(R.id.sitenat);
        bdiv = (Button) findViewById(R.id.div);
        bmusee = (Button) findViewById(R.id.musee);
        bmonum = (Button) findViewById(R.id.monum);

        bmusee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                Intent intent = new Intent(Categorie.this, Liste_Site.class);
                // On rajoute un extra
                intent.putExtra("categ", "musee");
                intent.putExtra("region", region);
                // Puis on lance l'intent !
                startActivity(intent);
            }
        });

        bmonum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                Intent intent = new Intent(Categorie.this, Liste_Site.class);
                // On rajoute un extra
                intent.putExtra("categ", "monument");
                intent.putExtra("region", region);
                // Puis on lance l'intent !
                startActivity(intent);
            }
        });

        bdiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                Intent intent = new Intent(Categorie.this, Liste_Site.class);
                // On rajoute un extra
                intent.putExtra("categ", "divertissement");
                intent.putExtra("region", region);
                // Puis on lance l'intent !
                startActivity(intent);
            }
        });

        bsitenat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Le premier paramètre est le nom de l'activité actuelle
                // Le second est le nom de l'activité de destination
                Intent intent = new Intent(Categorie.this, Liste_Site.class);
                // On rajoute un extra
                intent.putExtra("categ", "naturel");
                intent.putExtra("region", region);
                // Puis on lance l'intent !
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

}
