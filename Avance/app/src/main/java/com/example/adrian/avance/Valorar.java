package com.example.adrian.avance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import back.CircleTransform;

public class Valorar extends AppCompatActivity {

    private SwitchCompat estacionamientoDisc ;
    private SwitchCompat rampaAcceso ;
    private Spinner estadoRampa ;
    private SwitchCompat bandaAntiAcces ;
    private SwitchCompat barraApoyoAcces ;

    private Spinner anchoPuerta ;
    private SwitchCompat  banoDiscap ;
    private Spinner anchopuertaBano ;
    private SwitchCompat bandaAntiComod ;
    private SwitchCompat barrasApoyoComod ;
    private Spinner espacio ;
    private SwitchCompat lavamanos ;

    private Button enviarInfo;
    private EditText nota;
    private RatingBar valoracion;
    private String codCategoria;
    private String cod_Establecimiento;
    private ImageView imageViewPhotoValorar;

    private String servicio = "http://a5343472.ngrok.io/InclusivApp/";
    private String dirEstablecimiento = "controllers/establecimiento.php";
    private String dirAccesibilidad = "controllers/accesibilidad.php";
    private String dirComodidad = "controllers/comodidad.php";
    private String dirImg = "controllers/img.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_valorar);

        setToolbar();


        codCategoria = getIntent().getStringExtra("codCategoria");
        cod_Establecimiento = getIntent().getStringExtra("codCategoria");

        final String[] datos =
                new String[]{"Bueno","Malo"};
        final String[] datos2 =
                new String[]{"Inaccesible","Ajustado","Amplio"};
        final String[] datos3 =
                new String[]{"Inaccesible","Ajustado","Amplio"};
        final String[] datos4 =
                new String[]{"Ajustado","Amplio"};

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, datos);
        ArrayAdapter<String> adaptador1 =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, datos2);
        ArrayAdapter<String> adaptador2 =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, datos3);
        ArrayAdapter<String> adaptador3 =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, datos4);

        adaptador.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        adaptador1.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        adaptador2.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        adaptador3.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);



        estacionamientoDisc = (SwitchCompat) findViewById(R.id.SwichEstacionamiento);
        rampaAcceso =(SwitchCompat) findViewById(R.id.SwichrampaAcceso);
        estadoRampa =(Spinner) findViewById(R.id.estadoBarras);
        bandaAntiAcces =(SwitchCompat) findViewById(R.id.SwichBandaAnti);
        barraApoyoAcces =(SwitchCompat) findViewById(R.id.SwichBarraApoyoAcces);

        anchoPuerta =(Spinner) findViewById(R.id.opcionesAnchoPuertas);
        banoDiscap =(SwitchCompat) findViewById(R.id.SwichbanoDiscapacitados);
        anchopuertaBano =(Spinner) findViewById(R.id.opcionesAnchoPuertaBano);
        bandaAntiComod =(SwitchCompat) findViewById(R.id.SwichbandasAntiComodidades);
        barrasApoyoComod =(SwitchCompat) findViewById(R.id.SwichbarraApoyoComod);
        espacio =(Spinner) findViewById(R.id.opcionesespacioBano);
        lavamanos =(SwitchCompat) findViewById(R.id.SwichlavamanosAjustado);


        estadoRampa.setAdapter(adaptador);
        anchoPuerta.setAdapter(adaptador1);
        anchopuertaBano.setAdapter(adaptador2);
        espacio.setAdapter(adaptador3);

        nota = (EditText) findViewById(R.id.notas);
       // valoracion = (RatingBar) findViewById(R.id.valoracion) ;
        enviarInfo = (Button) findViewById(R.id.botonEnviarInfo);


        enviarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadAccesibilidad (cod_Establecimiento);
                uploadComodidad(cod_Establecimiento);




            }
        });


        if (AccessToken.getCurrentAccessToken() != null) {

            Profile profileDefault = Profile.getCurrentProfile();
            imageViewPhotoValorar = (ImageView) findViewById(R.id.imageViewPhotoValorar);
            Picasso.with(Valorar.this).load(profileDefault.getProfilePictureUri(100,100)).transform(new CircleTransform()).into(imageViewPhotoValorar);


        }
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarInformacion);
        toolbar.setTitle("Informacion del lugar");
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


    public void uploadAccesibilidad(final String codEstablecimiento){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, servicio + dirAccesibilidad,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                map.put("estacionamiento",String.valueOf(estacionamientoDisc.isChecked()));
                map.put("rampa",String.valueOf(rampaAcceso.isChecked()));
                map.put("banda",String.valueOf(bandaAntiAcces.isChecked()));
                map.put("barra",String.valueOf(barraApoyoAcces.isChecked()));
                map.put("nota",nota.getText().toString());
                map.put("valoracion","1");

                return map;
            }
        };

        requestQueue.add(request);


    }

    public void uploadComodidad(final String codEstablecimiento){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, servicio + dirComodidad,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                map.put("ancho_puerta",anchoPuerta.getSelectedItem().toString());
                map.put("bano",String.valueOf(banoDiscap.isChecked()));
                map.put("banda",String.valueOf(bandaAntiComod.isChecked()));
                map.put("barra",String.valueOf(barrasApoyoComod.isChecked()));
                map.put("espacio",espacio.getSelectedItem().toString());
                map.put("lavamano",String.valueOf(lavamanos.isChecked()));

                return map;
            }
        };

        requestQueue.add(request);

    }
}
