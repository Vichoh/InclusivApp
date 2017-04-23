package com.example.adrian.avance;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import back.CircleTransform;

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
    private Button cargarImagenes;

    private String servicio = "http://84c513c9.ngrok.io/InclusivApp/";
    private String dirEstablecimiento = "controllers/establecimiento.php";
    private String dirAccesibilidad = "controllers/accesibilidad.php";
    private String dirComodidad = "controllers/comodidad.php";
    private String dirImg = "controllers/img.php";
    private ArrayList<String> imgsBit;

    final String categorias [] = {"Centros Comerciales","Farmacias", "Bancos",
            "Supermercados","Restaurantes" ,"Estacionamiento","Hoteles/Hostales" ,"Servivios",};


    private ImageView imageViewPhoto;


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
        //valoracion = (RatingBar) findViewById(R.id.valoracion) ;
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

        cargarImagenes = (Button) findViewById(R.id.btnImg);


        enviarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadEstablecimiento(nombreCategoria+"",""+yourLatitude,""+yourLongitude);


            }
        });

        cargarImagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent,1);


            }
        });


        if (AccessToken.getCurrentAccessToken() != null) {

            Profile profileDefault = Profile.getCurrentProfile();
            imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
            Picasso.with(InfoLugar.this).load(profileDefault.getProfilePictureUri(100,100)).transform(new CircleTransform()).into(imageViewPhoto);


        }


    }



    public void uploadEstablecimiento(final String categoria, final String latitud, final String longitud){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, servicio + dirEstablecimiento,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response.toString());
                            String cod_establecimiento = jsonObject.getString("cod_establecimiento");

                            uploadAccesibilidad(cod_establecimiento);
                            uploadComodidad(cod_establecimiento);
                            uploadImages(cod_establecimiento);

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

                map.put("nombre",infoNombre.getText().toString());
                map.put("calle",infoCalle.getText().toString());
                map.put("numero",infoNumero.getText().toString());
                map.put("cod_categoria",categoria);
                map.put("hora_in","");
                map.put("hora_ter","");
                map.put("telefono",infoTelefono.getText().toString());
                map.put("sitio_web",infoWeb.getText().toString());
                map.put("latitud",latitud);
                map.put("longitud",longitud);



                return map;
            }
        };

        requestQueue.add(request);
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

    public void uploadImages(final String codEstablecimiento){
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, servicio + dirImg,
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
                JSONObject item;
                JSONArray array = new JSONArray();
                JSONObject json = new JSONObject();

                try {
                    for (int i=0;i<imgsBit.size();i++){
                        item = new JSONObject();
                        item.put("img",imgsBit.get(i));
                        array.put(item);
                    }
                    //json.put("imagenes",array);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                map.put("cod_establecimiento",codEstablecimiento);
                map.put("img",array.toString());

                return map;
            }
        };

        requestQueue.add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imgsBit = new ArrayList<String>();



        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);


        // When an Image is picked
        if (requestCode == 1 && resultCode == RESULT_OK
                && null != data) {

            if(data.getData()!= null){

                agregarImagen(data.getData());


            }else{
                if(data.getClipData()!=null){
                    ClipData mClipData=data.getClipData();
                    for(int i=0;i<mClipData.getItemCount();i++){

                        ClipData.Item item = mClipData.getItemAt(i);

                        agregarImagen(item.getUri());
                    }

                }

            }

        } else {
            Toast.makeText(this, "Problemas al cargar imagen",
                    Toast.LENGTH_LONG).show();
        }

    }

    private void agregarImagen(Uri uri){

        Bitmap bitmap;

        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        // Get the cursor
        Cursor cursor = getContentResolver().query(uri,
                filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String ruta = cursor.getString(columnIndex);
        cursor.close();

        bitmap = BitmapFactory.decodeFile(ruta);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);

        byte[] array = stream.toByteArray();
        imgsBit.add(Base64.encodeToString(array,0));


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







}




