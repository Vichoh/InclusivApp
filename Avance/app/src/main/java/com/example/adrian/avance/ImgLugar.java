package com.example.adrian.avance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class ImgLugar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_lugar);


        /*
        Recibiendo el identificador de la imagen
         */
        Intent i = getIntent();
        int position = i.getIntExtra("position", -1);// -1 si no se encontró la referencia
        ImagenAdapter adapter = new ImagenAdapter(this);

        /*
        Seteando el recurso en el ImageView
         */
        ImageView originalImage = (ImageView)findViewById(R.id.originalImage);
        originalImage.setImageResource(adapter.getThumbId(position));
    }
}
