package com.wemove;

/**
 * Created by utilisateur on 08/02/2015.
 */



public class EnumImg {

    public enum Image {

        GRANET(R.drawable.museegranet),
        MARINELAND(R.drawable.marineland),
        CALANQUE(R.drawable.calanque),
        NATUROSCOPE(R.drawable.museegranet),
        OKCORRAL(R.drawable.museegranet),
        ZOOBARBEN(R.drawable.museegranet),
        CANTINI(R.drawable.museegranet),
        SEAQUARIUM(R.drawable.museegranet),
        TOURCREST(R.drawable.tourdecrest),
        SENANQUE(R.drawable.senanque),
        ARCORANGE(R.drawable.arcdorange),
        ARENEARLES(R.drawable.arenedarles),
        DAMEGARDE(R.drawable.notredamelagarde),
        CHARITE (R.drawable.vieillecharite),
        TEST (R.drawable.vieillecharite);


        private int drawable;

        //Constructeur

        Image(int drawable){
            this.drawable = drawable;

        }

        public int getDrawable()
        {
            return drawable;
        }


    }
}
