package com.example.adrian.avance;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class descLugar extends AppCompatActivity {

    private CollapsingToolbarLayout nombre_Intitucion_lista;


    private TextView nombreEstablecimiento;
    private TextView calificacion;
    private FloatingActionButton ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc_lugar);

        setToolbar();


        nombre_Intitucion_lista = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
       nombreEstablecimiento = (TextView) findViewById(R.id.nombreLugar);
        calificacion = (TextView) findViewById(R.id.calificacion);






        ruta = (FloatingActionButton) findViewById(R.id.Truta);


        ruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                Log.i("ActionBar", "Atrás!");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
