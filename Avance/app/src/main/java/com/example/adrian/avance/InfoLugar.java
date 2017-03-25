package com.example.adrian.avance;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class InfoLugar extends AppCompatActivity {

    private TextView categoria;
    private Button cargarInfo;
    private EditText infoCalle;
    private EditText infoNumero;
    private EditText infoCiudad;

    private Button enviarInfo;
    private EditText infoNombre;
    private EditText infoTelefono;
    private EditText infoWeb;
    private EditText nota;
    private EditText infoEmail;
    private RatingBar valoracion;

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

    final String categorias [] = {"Centros Comerciales","Farmacias", "Bancos",
            "Supermercados","Restaurantes" ,"Estacionamiento","Hoteles/Hostales" ,"Servivios",};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_lugar);

        setToolbar();



        //obtener la categoria desde el activity anterior
        final int nombreCategoria = Integer.parseInt(getIntent().getStringExtra("categoria"))+1;
        String latitude = getIntent().getStringExtra("latitud");
        String longitude = getIntent().getStringExtra("longitud");
        categoria = (TextView) findViewById(R.id.nombreCategia);
        categoria.setText(categorias[nombreCategoria-1]);


        final String[] datos =
                new String[]{"Bueno","Malo"};
        final String[] datos2 =
                new String[]{"inaccesible","ajustado","amplio"};
        final String[] datos3 =
                new String[]{"inaccesible","ajustado","amplio"};
        final String[] datos4 =
                new String[]{"ajustado","amplio"};


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



        infoCalle = (EditText) findViewById(R.id.infoCalle);
        infoNumero = (EditText) findViewById(R.id.infoNumero);
        infoCiudad = (EditText) findViewById(R.id.infoCiudad);


        final double yourLatitude, yourLongitude;
        yourLatitude = Double.parseDouble(latitude);
        yourLongitude = Double.parseDouble(longitude);
        // boton cargar informacion mas su clic accion
        cargarInfo = (Button) findViewById(R.id.botonDatosUbicacion);


        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        cargarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 List<Address> yourAddresses = new ArrayList<>();

                try {
                    yourAddresses= geocoder.getFromLocation(yourLatitude, yourLongitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (yourAddresses.size() > 0)
                {
                    String yourAddress = yourAddresses.get(0).getAddressLine(0);
                    String yourCity = yourAddresses.get(0).getAddressLine(1);

                    String[] direccion;
                    direccion = yourAddress.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

                    infoCalle.setText(direccion[0]);
                    infoNumero.setText(direccion[1]);
                    infoCiudad.setText(yourCity);
                }



            }
        });


        infoTelefono = (EditText) findViewById(R.id.infoTelefono);
        infoWeb = (EditText) findViewById(R.id.infoWeb);
        nota = (EditText) findViewById(R.id.notas);
        infoEmail = (EditText) findViewById(R.id.infoEmail);
        valoracion = (RatingBar) findViewById(R.id.valoracion) ;
        infoNombre = (EditText) findViewById(R.id.infoNombre);
        enviarInfo = (Button) findViewById(R.id.botonEnviarInfo);

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



        enviarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TareaWSInsertar tarea = new TareaWSInsertar();
                tarea.execute(
                        infoNombre.getText().toString(),//0
                        infoCalle.getText().toString(),//1
                        infoNumero.getText().toString(),//2
                        nombreCategoria+"",//3
                        infoCiudad.getText().toString(),//4
                        infoEmail.getText().toString(),//5
                        infoWeb.getText().toString(),//6
                        infoTelefono.getText().toString(),//7
                        ""+yourLatitude,//8
                        ""+yourLongitude,//9
                        nota.getText().toString(),//10
                        ""+valoracion.getRating(),//11

                        String.valueOf(estacionamientoDisc.isChecked()),//12
                        String.valueOf(rampaAcceso.isChecked()),//13
                        estadoRampa.getSelectedItem().toString(),//14
                        String.valueOf(bandaAntiAcces.isChecked()),//15
                        String.valueOf(barraApoyoAcces.isChecked()),//16

                        anchoPuerta.getSelectedItem().toString(),//17
                        String.valueOf(banoDiscap.isChecked()),//18
                        anchopuertaBano.getSelectedItem().toString(),//19
                        String.valueOf(bandaAntiComod.isChecked()),//20
                        String.valueOf(barrasApoyoComod.isChecked()),//21
                        espacio.getSelectedItem().toString(),//22
                        String.valueOf(lavamanos.isChecked())//23
                );

                Toast.makeText(getApplicationContext(),String.valueOf(estacionamientoDisc.isChecked()),Toast.LENGTH_LONG).show();






            }
        });





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





    private class TareaWSInsertar extends AsyncTask<String,Integer,Boolean> {

        String respuesta;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();

            HttpPost post = new HttpPost("http://192.168.1.2/InclusivApp/controllers/establecimiento.php");
                //post.setHeader("content-type", "application/json");

                List<NameValuePair> datos = new ArrayList<>();

                try
                {
                    //Construimos el objeto cliente en formato JSON

                    datos.add(new BasicNameValuePair("nombre", params[0]));
                    datos.add(new BasicNameValuePair("calle",params[1]));
                    datos.add(new BasicNameValuePair("numero",params[2]));
                    datos.add(new BasicNameValuePair("cod_categoria",params[3]));
                    datos.add(new BasicNameValuePair("hora_in","0"));
                    datos.add(new BasicNameValuePair("hora_ter","0"));
                    datos.add(new BasicNameValuePair("telefono",params[7]));
                    datos.add(new BasicNameValuePair("sitio_web",params[6]));
                    datos.add(new BasicNameValuePair("latitud",params[8]));
                    datos.add(new BasicNameValuePair("longitud",params[9]));
                    datos.add(new BasicNameValuePair("nota",params[10]));
                    datos.add(new BasicNameValuePair("valoracion",params[11]));



                    post.setEntity(new UrlEncodedFormEntity(datos));



                    HttpResponse resp = httpClient.execute(post);

                respuesta = EntityUtils.toString(resp.getEntity());

                JSONObject jsonObject = new JSONObject(respuesta);

                int cod_establecimiento = jsonObject.getInt("cod_establecimiento");



                //
                post = new HttpPost("http://192.168.1.2/InclusivApp/controllers/accesibilidad.php");

                    datos = new ArrayList<>();

                    datos.add(new BasicNameValuePair("cod_establecimiento", cod_establecimiento+""));
                    datos.add(new BasicNameValuePair("estacionamiento", params[12]));
                    datos.add(new BasicNameValuePair("rampa", params[13]));
                    datos.add(new BasicNameValuePair("banda", params[15]));
                    datos.add(new BasicNameValuePair("barra", params[16]));

                    post.setEntity(new UrlEncodedFormEntity(datos));

                     resp = httpClient.execute(post);


                     respuesta = EntityUtils.toString(resp.getEntity());




                    //
                    post = new HttpPost("http://192.168.1.2/InclusivApp/controllers/comodidad.php");

                    datos = new ArrayList<>();

                    datos.add(new BasicNameValuePair("cod_establecimiento", cod_establecimiento+""));
                    datos.add(new BasicNameValuePair("ancho_puerta", params[17]));
                    datos.add(new BasicNameValuePair("bano", params[18]));
                    datos.add(new BasicNameValuePair("banda", params[20]));
                    //datos.add(new BasicNameValuePair("rampa", params[19]));
                    datos.add(new BasicNameValuePair("barra", params[21]));
                    datos.add(new BasicNameValuePair("espacio", params[22]));
                    datos.add(new BasicNameValuePair("lavamano", params[23]));


                    post.setEntity(new UrlEncodedFormEntity(datos));

                    resp = httpClient.execute(post);


                    respuesta = EntityUtils.toString(resp.getEntity());

                resul = true;
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
                respuesta = ex.getMessage().toString();
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if(result){
                Toast.makeText(getApplicationContext(),respuesta,Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getApplicationContext(),respuesta,Toast.LENGTH_LONG).show();
            }


        }
    }

}




