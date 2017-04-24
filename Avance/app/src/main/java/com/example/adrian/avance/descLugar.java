package com.example.adrian.avance;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import back.GaleriaAdaptador;
import back.Rutas;

public class descLugar extends AppCompatActivity {

    private CollapsingToolbarLayout nombre_Intitucion_lista;
    private TextView nombreEstablecimiento;
    private RatingBar calificacionPromedio;
    private TextView valoracion_centro_accesibilidad;
    private TextView valoracion_centro_comodidad;
    private TextView direccionDesc;
    private TextView horariDesc;
    private TextView telefonoDesc;
    private FloatingActionButton ruta;
    private ProgressBar progress_bar_accesibilidad;
    private ProgressBar progress_bar_comodidad;
    private String myLat;
    private String myLon;
    private String latLugar;
    private String lonLugar;
    private String codCategoria;
    private String  cod_establecimiento;
    private ListView list_comentarios;

    private ViewPager galeria;
    private ArrayAdapter<String> namesAA;
    private GaleriaAdaptador galeriaAdaptador;
    private String dirImg = "http://a5343472.ngrok.io/InclusivApp/img/";
    private String servicioImg = "http://a5343472.ngrok.io/InclusivApp/controllers/img.php";
    private String servicioComentarios = "http://a5343472.ngrok.io/InclusivApp/controllers/nota.php";
    private static ArrayList<String> comentarios = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc_lugar);

        setToolbar();

        //galeria

        galeria = (ViewPager) findViewById(R.id.viewPager);
        String codEstablecimiento = getIntent().getStringExtra("cod_establecimiento");
        Toast.makeText(getApplicationContext(),codEstablecimiento,Toast.LENGTH_SHORT).show();
        cargarImagenes(codEstablecimiento);

        //cargo lso comentarios



        nombre_Intitucion_lista = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        valoracion_centro_accesibilidad = (TextView) findViewById(R.id.valoracion_centro_accesibilidad);
        valoracion_centro_comodidad = (TextView) findViewById(R.id.valoracion_centro_comodidad);
        direccionDesc = (TextView) findViewById(R.id.direccionDesc);

        nombreEstablecimiento = (TextView) findViewById(R.id.nombreLugar);
        telefonoDesc = (TextView) findViewById(R.id.telefonoDesc);
        calificacionPromedio = (RatingBar) findViewById(R.id.valoracionPromedio);
        progress_bar_accesibilidad = (ProgressBar) findViewById(R.id.progress_bar_accesibilidad);
        progress_bar_comodidad = (ProgressBar) findViewById(R.id.progress_bar_comodidad);


        nombreEstablecimiento.setText(getIntent().getStringExtra("nombreLugar"));
        valoracion_centro_accesibilidad.setText(getIntent().getStringExtra("valoracion_centro_accesibilidad"));
        valoracion_centro_comodidad.setText(getIntent().getStringExtra("valoracion_centro_comodidad"));
        direccionDesc.setText(getIntent().getStringExtra("direccionDesc"));

        telefonoDesc.setText(getIntent().getStringExtra("telefonoDesc"));
        //calificacionPromedio.setRating(Float.parseFloat(getIntent().getStringExtra("calificacionPromedio")));
       // progress_bar_accesibilidad.setProgress(Integer.parseInt(getIntent().getStringExtra("progress_bar_accesibilidad"))*2);
      //  progress_bar_comodidad.setProgress(Integer.parseInt(getIntent().getStringExtra("progress_bar_comodidad"))*2);

        nombre_Intitucion_lista.setTitle("Establecimiento");


        cod_establecimiento = getIntent().getStringExtra("cod_establecimiento");
        codCategoria = getIntent().getStringExtra("codCategoria");
        myLat = getIntent().getStringExtra("myLat");
        myLon = getIntent().getStringExtra("myLon");
        latLugar = getIntent().getStringExtra("lat");
        lonLugar = getIntent().getStringExtra("lon");
        Toast.makeText(getApplication(), myLat+myLon+latLugar+lonLugar, Toast.LENGTH_LONG).show();

        ruta = (FloatingActionButton) findViewById(R.id.Truta);


        ruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplication(), "boton rutas fue pulsado", Toast.LENGTH_LONG).show();

                Rutas rutas = new Rutas(latLugar,lonLugar,""+myLat,""+myLon,getApplicationContext());
                rutas.addRoutes();

                Intent intent = new Intent (getApplicationContext(), Principal.class);
                startActivity(intent);




            }
        });


        Button botonValorar = (Button) findViewById(R.id.botonValorar);

        botonValorar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext(), Valorar.class);
                intent.putExtra("codCategoria", codCategoria);
                intent.putExtra("cod_Establecimiento", codCategoria);
                startActivity(intent);
            }
        });


        //comentarios
        list_comentarios = (ListView) findViewById(R.id.list_comentarios) ;
        cargarComentarios(codEstablecimiento);
       // AdaptadorComentario adaptadorComentario = new AdaptadorComentario(this, comentarios);
       // Toast.makeText(getApplicationContext(),"asdasdasgag"+comentarios.get(0),Toast.LENGTH_SHORT).show();
       // comentarios.add("si tiene un dato ahora");
       // ArrayAdapter<String> namesAA = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1, comentarios );

        //list_comentarios.setAdapter(namesAA);
    }



    private void cargarImagenes(final String codEstablecimiento){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, servicioImg+"?cod_establecimiento="+codEstablecimiento,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();




                        try {
                            JSONArray array = new JSONArray(response.toString());

                            List<String> urls = new ArrayList<String>();

                            for(int i=0;i<array.length();i++){

                                urls.add(dirImg+array.getJSONObject(i).getString("url"));
                                //Toast.makeText(getApplicationContext(),urls.get(i),Toast.LENGTH_SHORT).show();
                            }

                            galeriaAdaptador = new GaleriaAdaptador(urls,getApplicationContext());
                            galeria.setAdapter(galeriaAdaptador);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(getApplication(),response.toString(),Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplication(),error.toString(),Toast.LENGTH_SHORT).show();


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String, String>();

                map.put("cod_establecimiento",codEstablecimiento);

                return map;
            }
        };

        requestQueue.add(request);

    }

    private void cargarComentarios(final String codEstablecimiento){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, servicioComentarios+"?cod_establecimiento="+codEstablecimiento,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_SHORT).show();




                        try {
                            JSONArray array = new JSONArray(response.toString());

                            ArrayList<String> urls = new ArrayList<String>();

                            for(int i=0;i<array.length();i++){

                                urls.add(array.getJSONObject(i).getString("nota"));

                            }

                            comentarios = urls;
                            urls.add("si tieen un comentarioo");
                             namesAA = new ArrayAdapter<String>( getApplicationContext(), android.R.layout.simple_list_item_1, urls );
                            Toast.makeText(getApplicationContext(),"aaaaaaaaaaa"+comentarios.get(0),Toast.LENGTH_SHORT).show();
                            list_comentarios.setAdapter(namesAA);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplication(),error.toString(),Toast.LENGTH_SHORT).show();


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<String, String>();

                map.put("cod_establecimiento",codEstablecimiento);

                return map;
            }
        };

        requestQueue.add(request);

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
