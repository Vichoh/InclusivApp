package com.example.adrian.avance;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import back.AdaptadorEstablecimiento;
import back.Establecimiento;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class Mapa extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener, android.widget.SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener{

    public static GoogleMap mapa;

    private Double longitudReferencia = -72.6164245;
    private Double latitudReferencia = -38.7457485;
    private String markerLatitud = "-38.7339569";
    private String markerLongitud = "-72.6109954";
    private FloatingActionButton locacionCercana;
    private ArrayList<Establecimiento> establecimientos = new ArrayList<>();

    private static final String LOGTAG = "android-localizacion";
    private LocationRequest locRequest;
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;
    private GoogleApiClient apiClient;


    private String dirEstablecimiento="http://cffca80a.ngrok.io/InclusivApp/controllers/establecimiento.php";
    private String dirBusqueda = "http://cffca80a.ngrok.io/InclusivApp/controllers/busqueda.php";

    private ArrayList<Establecimiento> establecimientosBusqueda = new ArrayList<>();
    ArrayList<Establecimiento> establecimientosBusquedaVacio = new ArrayList<>();
    private Context mContext;
    ListView listView;

    private List<List<HashMap<String, String>>>  listaRutas;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);


    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_mapa, container, false);
        ((Principal) getActivity()).getSupportActionBar().setTitle("");
        apiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();




        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        //DESCARGO TODOS LOS ESTABLECIMIENTOS Y LOS AGREGO AL MAPA
        cargarEstablecimientos();

        locacionCercana = (FloatingActionButton) view.findViewById(R.id.fBtnLocation);
        locacionCercana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLocationUpdates(true);


                LatLng referencia = new LatLng(latitudReferencia,longitudReferencia);


                mapa.addMarker(new MarkerOptions().position(referencia).title("Tu estas aquí")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).flat(true));

                CameraPosition cameraPosition =  new CameraPosition.Builder()
                        .target(referencia)
                        .zoom(15)
                        .build();


                CameraUpdate camUpd3 =
                        CameraUpdateFactory.newCameraPosition(cameraPosition);

                mapa.animateCamera(camUpd3);



            }
        });

        // rutas

        //Rutas rutas = new Rutas(markerLatitud,markerLongitud,""+latitudReferencia,""+longitudReferencia,getContext());
        //rutas.addRoutes();



        //busqueda

        listView = (ListView) view.findViewById(R.id.list_buscar_mapa);
        TextView emptyTextView = (TextView) view.findViewById(R.id.vacio);
        listView.setEmptyView(emptyTextView);


        return view;


    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.buscar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);

        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public void onMapReady(GoogleMap map) {

        toggleLocationUpdates(true);
        LatLng referencia = new LatLng(latitudReferencia,longitudReferencia);
        //map.addMarker(new MarkerOptions().position(referencia).title("Tu estas aqui"));
        map.moveCamera(CameraUpdateFactory.newLatLng(referencia));
        CameraPosition cameraPosition =  new CameraPosition.Builder()
                .target(referencia)
                .zoom(5)
                .build();


        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(cameraPosition);

        map.animateCamera(camUpd3);



        mapa = map;

        mapa.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                Intent intent = new Intent (getContext(), Categoria.class);

                intent.putExtra("latitud", ""+latLng.latitude);
                intent.putExtra("longitud", ""+latLng.longitude);
                startActivity(intent);




            }
        });


        mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {



                Establecimiento establecimiento = buscarEstablecimientoPorltdlgt(marker.getPosition().latitude,marker.getPosition().longitude);

                markerLatitud = ""+marker.getPosition().latitude;
                markerLongitud = ""+marker.getPosition().longitude;
                Intent intent = new Intent(getContext(),descLugar.class);


                intent.putExtra("nombreLugar", establecimiento.getNombre());
                intent.putExtra("valoracion_centro_accesibilidad", establecimiento.getNombre());
                intent.putExtra("valoracion_centro_comodidad", establecimiento.getNombre());
                intent.putExtra("direccionDesc", establecimiento.getCalle());
                intent.putExtra("horariDesc", establecimiento.getNombre());
                intent.putExtra("telefonoDesc", establecimiento.getTelefono());
                intent.putExtra("calificacionPromedio", establecimiento.getNombre());
                intent.putExtra("progress_bar_accesibilidad", establecimiento.getNombre());
                intent.putExtra("progress_bar_comodidad", establecimiento.getNombre());
                intent.putExtra("lat", ""+marker.getPosition().latitude);
                intent.putExtra("lon", ""+marker.getPosition().longitude);
                intent.putExtra("myLat", ""+latitudReferencia);
                intent.putExtra("myLon", ""+longitudReferencia);
                intent.putExtra("codCategoria", establecimiento.getCategoria());

                startActivity( intent );


                return false;
            }
        });
    }

    private void updateUI(Location loc) {

        if (loc != null) {

            latitudReferencia=loc.getLatitude();
            longitudReferencia=loc.getLongitude();
        } else {


        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result){

        Log.e(LOGTAG, "Error grave al conectar con google play services");
    }

    @Override
    public void onConnected (@Nullable Bundle bundle){
        //Conectado correctamente

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        }else{

            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);

            updateUI(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i){
        Log.e(LOGTAG,"Se ha interrumpido");
    }




    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == PETICION_PERMISO_LOCALIZACION){
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                @SuppressWarnings("MissingPermission")
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(apiClient);

                updateUI(lastLocation);
            }else{
                Log.e(LOGTAG, "Permiso denegado");
            }
        }
    }

    public void toggleLocationUpdates(boolean enable){
        if(enable){
            enableLocationUpdates();
        }else{
            disableLocationUpdates();
        }
    }

    private void enableLocationUpdates(){
        locRequest = new LocationRequest();
        locRequest.setInterval(2000);
        locRequest.setFastestInterval(1000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        final LocationSettingsRequest locationSettingsRequest =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locRequest)
                        .build();

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(apiClient, locationSettingsRequest);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch(status.getStatusCode()){
                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.i(LOGTAG, "Configuración correcta");
                        startLocationUpdates();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try{
                            Log.i(LOGTAG, "Se requiere accion");
                            status.startResolutionForResult(getActivity(), PETICION_CONFIG_UBICACION);
                        }catch (IntentSender.SendIntentException e){
                            toggleLocationUpdates(false);
                            Log.i(LOGTAG, "Error");
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(LOGTAG, "No se puede cumplir configuracion");
                        toggleLocationUpdates(false);
                        break;
                }
            }
        });
    }

    private void startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            Log.i(LOGTAG, "Inicio de recepcion");
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locRequest, Mapa.this);
        }
    }

    private void disableLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case PETICION_CONFIG_UBICACION:
                switch(resultCode){
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(LOGTAG, "El usuario no ha realizado los cambios necesarios");
                        toggleLocationUpdates(false);
                        break;
                }
                break;
        }
    }



    @Override
    public void onLocationChanged(Location location){
        Log.i(LOGTAG, "Recibida nueva ubicación");
        updateUI(location);
    }

    @Override
    public void onStart(){
        super.onStart();
        if(apiClient != null){
            apiClient.connect();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(apiClient!= null && apiClient.isConnected()){
            apiClient.stopAutoManage(getActivity());
            apiClient.disconnect();
        }
    }

    // metodos del busacaddor

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {


        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {


        if (newText == null || newText.trim().isEmpty()) {
            resetSearch();
            return false;
        }

            Toast toast2 = Toast.makeText(getContext(), newText, Toast.LENGTH_LONG);
            toast2.show();

            final ObtenerBusqueda obtenerBusqueda = new ObtenerBusqueda(getContext(),getActivity());
            obtenerBusqueda.setBusqueda(newText);
            obtenerBusqueda.execute();





        return false;
    }
    public void resetSearch() {

        AdaptadorEstablecimiento adaptadorEstablecimiento = new AdaptadorEstablecimiento(getActivity(),
                establecimientosBusquedaVacio);


        listView.setAdapter(adaptadorEstablecimiento);
    }





    public void cargarEstablecimientos(){

        //CREO LAS CONEXIONES
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET,dirEstablecimiento,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray array = new JSONArray(response.toString());

                            for (int i = 0; i<array.length(); i++) {

                                Establecimiento establecimiento = new Establecimiento();


                                establecimiento.setNombre(array.getJSONObject(i).getString("nombre"));
                                establecimiento.setCalle(array.getJSONObject(i).getString("calle"));
                                establecimiento.setCategoria(array.getJSONObject(i).getString("cod_categoria"));
                                establecimiento.setTelefono(array.getJSONObject(i).getString("telefono"));
                                establecimiento.setSitioWeb(array.getJSONObject(i).getString("sitio_web"));
                                establecimiento.setLatitud(array.getJSONObject(i).getDouble("latitud"));
                                establecimiento.setLongitud(array.getJSONObject(i).getDouble("longitud"));
                                establecimiento.setId(array.getJSONObject(i).getInt("cod_establecimiento"));
                                establecimientos.add(establecimiento);
                            }

                            addEstablecimientos(establecimientos);
                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(getContext(),"Error en la carga de establecimientos"+ex.getMessage(),Toast.LENGTH_SHORT).show();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(),"error al cargar establecimientos"+error.toString(),Toast.LENGTH_SHORT).show();


            }
        });

        requestQueue.add(request);

    }


    public void addEstablecimientos(ArrayList<Establecimiento> establecimiento){

        for (int i=0; i<establecimiento.size(); i++) {



            switch (establecimientos.get(i).getCategoria()){
                case "1" :
                    LatLng referencia = new LatLng(establecimientos.get(i).getLatitud(), establecimiento.get(i).getLongitud());
                    mapa.addMarker(new MarkerOptions().position(referencia).title(establecimiento.get(i).getNombre())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_centrocomercial)).flat(true));

                    break;
                case "2" :
                    LatLng referencia2 = new LatLng(establecimientos.get(i).getLatitud(), establecimiento.get(i).getLongitud());
                    mapa.addMarker(new MarkerOptions().position(referencia2).title(establecimiento.get(i).getNombre())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_farmacia)).flat(true));

                    break;
                case "3" :
                    LatLng referencia3 = new LatLng(establecimientos.get(i).getLatitud(), establecimiento.get(i).getLongitud());
                    mapa.addMarker(new MarkerOptions().position(referencia3).title(establecimiento.get(i).getNombre())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_banco)).flat(true));

                    break;
                case "4" :
                    LatLng referencia4 = new LatLng(establecimientos.get(i).getLatitud(), establecimiento.get(i).getLongitud());
                    mapa.addMarker(new MarkerOptions().position(referencia4).title(establecimiento.get(i).getNombre())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_supermercado)).flat(true));

                    break;
                case "5" :
                    LatLng referencia5 = new LatLng(establecimientos.get(i).getLatitud(), establecimiento.get(i).getLongitud());
                    mapa.addMarker(new MarkerOptions().position(referencia5).title(establecimiento.get(i).getNombre())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant)).flat(true));

                    break;
                case "6" :
                    LatLng referencia6 = new LatLng(establecimientos.get(i).getLatitud(), establecimiento.get(i).getLongitud());
                    mapa.addMarker(new MarkerOptions().position(referencia6).title(establecimiento.get(i).getNombre())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_estacionamiento)).flat(true));

                    break;
                case "7" :
                    LatLng referencia7 = new LatLng(establecimientos.get(i).getLatitud(), establecimiento.get(i).getLongitud());
                    mapa.addMarker(new MarkerOptions().position(referencia7).title(establecimiento.get(i).getNombre())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_hotel)).flat(true));

                    break;
                case "8" :
                    LatLng referencia8 = new LatLng(establecimientos.get(i).getLatitud(), establecimiento.get(i).getLongitud());
                    mapa.addMarker(new MarkerOptions().position(referencia8).title(establecimiento.get(i).getNombre())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_iconservicios)).flat(true));

                    break;
            }

        }
    }


    public Establecimiento buscarEstablecimientoPorltdlgt(double latitud,double longitud){
        Establecimiento respuesta = null;
        for (int i=0; i<this.establecimientos.size();i++){
            if  (establecimientos.get(i).getLatitud() == latitud && establecimientos.get(i).getLongitud() == longitud){
                respuesta = establecimientos.get(i);
            }


        }

        return respuesta;
    }










    public class ObtenerBusqueda extends AsyncTask<String,Integer,Boolean> {

        private String url = dirBusqueda;

        private String busqueda;
        private String respStr;
        private Context context;
        private Activity activity;



        public ObtenerBusqueda(Context context, Activity activity) {
            this.context = context;
            this.activity = activity;
            establecimientosBusqueda.clear();
        }



        public void setBusqueda(String busqueda) {
            this.busqueda = busqueda;
        }






        @Override
        protected Boolean doInBackground(String... strings) {
            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();


            HttpGet del =
                    new HttpGet(url+"?busqueda="+this.busqueda);




            //del.setHeader("content-type", "application/json");

            try {
                HttpResponse resp = httpClient.execute(del);
                respStr = EntityUtils.toString(resp.getEntity());


                JSONArray array = new JSONArray(respStr);


                for (int i = 0; i < array.length(); i++) {

                    Establecimiento establecimiento = new Establecimiento();

                    establecimiento.setNombre(array.getJSONObject(i).getString("nombre"));
                    establecimiento.setCalle(array.getJSONObject(i).getString("calle"));
                    establecimiento.setCategoria(array.getJSONObject(i).getString("cod_categoria"));
                    establecimiento.setTelefono(array.getJSONObject(i).getString("telefono"));
                    establecimiento.setSitioWeb(array.getJSONObject(i).getString("sitio_web"));
                    establecimiento.setLatitud(array.getJSONObject(i).getDouble("latitud"));
                    establecimiento.setLongitud(array.getJSONObject(i).getDouble("longitud"));



                    establecimientosBusqueda.add(establecimiento);
                }





            } catch (Exception ex) {
                Log.e("ServicioRest", "Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                if (establecimientosBusqueda != null) {

                    Toast toast2 = Toast.makeText(context, respStr, Toast.LENGTH_LONG);
                    toast2.show();

                    resetSearch();

                    AdaptadorEstablecimiento adaptadorEstablecimiento = new AdaptadorEstablecimiento(this.activity,
                            establecimientosBusqueda);


                    listView.setAdapter(adaptadorEstablecimiento);
                }
            }
        }



    }
}

