package com.wemove;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by utilisateur on 09/12/2014.
 */
public class AdaptateurListe extends ArrayAdapter<String> {

    private Integer[] tab_images_pour_la_liste = {};
    Context context;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.liste_site, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        textView.setText(getItem(position));

        if(convertView != null )
            //imageView.setImageResource(tab_images_pour_la_liste[position]);
        //else
            rowView = (View)convertView;

        return rowView;
    }

    public AdaptateurListe(Context context, ArrayList<String> values) {
        super(context, R.layout.liste_site, values);
        this.context = context;
    }

}
