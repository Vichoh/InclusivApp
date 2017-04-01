package com.example.adrian.avance;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Adrian on 13/03/2017.
 */

public class ImagenAdapter extends BaseAdapter {
    // Contexto de la aplicación
    private Context mContext;
    LayoutInflater layoutInflater;

    // Array de identificadores
    private Integer[] mThumbIds = {
            R.drawable.comercial,R.drawable.farmacia, R.drawable.banco,
            R.drawable.supermercado,R.drawable.restaurantes,R.drawable.estacionamiento,R.drawable.hotel,R.drawable.servicios
    };

    public ImagenAdapter(Context c) {
        mContext = c;
        layoutInflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public int getThumbId(int position){return mThumbIds[position];}


    public View getView(int position, View convertView, ViewGroup parent) {
        //ImageView a retornar

        /*

        convertView = layoutInflater.inflate(R.layout.activity_img_lugar,null);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.originalImage);
        RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relative);

        relativeLayout.setBackgroundColor(Color.BLACK);

        */

        ImageView imageView;





        if (convertView == null) {

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(360,200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setColorFilter(Color.argb(255,255,255,255));
            imageView.setPadding(30,30,30,30);

            imageView.setAdjustViewBounds(true);

            switch (position){
                case 0:
                    imageView.setBackgroundColor(Color.parseColor("#FFD600"));
                    break;
                case 1:
                    imageView.setBackgroundColor(Color.parseColor("#B71C1C"));
                    break;
                case 2:
                    imageView.setBackgroundColor(Color.GRAY);
                    break;
                case 3:
                    imageView.setBackgroundColor(Color.parseColor("#2E7D32"));
                    break;
                case 4:
                    imageView.setBackgroundColor(Color.parseColor("#00ACC1"));
                    break;
                case 5:
                    imageView.setBackgroundColor(Color.parseColor("#8E24AA"));
                    break;
                case 6:
                    imageView.setBackgroundColor(Color.parseColor("#FB8C00"));
                    break;
                case 7:
                    imageView.setBackgroundColor(Color.parseColor("#1A237E"));
            }



        } else {
            imageView = (ImageView) convertView;
        }





        //Setear la imagen desde el recurso drawable
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

}
