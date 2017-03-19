package com.example.adrian.avance;

import android.Manifest;
import android.app.Activity;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

public class Mapa extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleMap mapa;

    private Double longitudReferencia = -72.6377405;
    private Double latitudReferencia = -38.7290173;
    private FloatingActionButton locacionCercana;


    private static final String LOGTAG = "android-localizacion";
    private LocationRequest locRequest;
    private static final int PETICION_PERMISO_LOCALIZACION = 101;
    private static final int PETICION_CONFIG_UBICACION = 201;
    private GoogleApiClient apiClient;


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


        TareaWSObtener tarea = new TareaWSObtener();
        tarea.execute();

        Toast toast = Toast.makeText(getContext(), tarea.nombre, Toast.LENGTH_LONG);
        toast.show();

        //LatLng referencia = new LatLng(tarea.latitud,tarea.longitud);
       // mapa.addMarker(new MarkerOptions().position(referencia).title(tarea.nombre)
            //    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).flat(true));


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



        return view;


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
                PolylineOptions rectOptions = new PolylineOptions()
                        .add(new LatLng(latitudReferencia,longitudReferencia))
                        .add(marker.getPosition());

                Polyline polyline = mapa.addPolyline(rectOptions);

                Intent intent = new Intent(getContext(),descLugar.class);
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



    private class TareaWSObtener extends AsyncTask<String,Integer,Boolean> {

        private double longitud;
        private double latitud;
        private String nombre;
        private String valoracion;

        protected Boolean doInBackground(String... params) {

            boolean resul = true;

            HttpClient httpClient = new DefaultHttpClient();



            HttpGet del =
                    new HttpGet("http://inclusivapp.esy.es/controllers/establecimiento.php");


            //del.setHeader("content-type", "application/json");

            try
            {
                HttpResponse resp = httpClient.execute(del);
                String respStr = EntityUtils.toString(resp.getEntity());

                JSONArray array = new JSONArray(respStr);


                Toast toast2 = Toast.makeText(getContext(), "3434535"+respStr, Toast.LENGTH_LONG);
                toast2.show();

                nombre = array.getJSONObject(0).getString("nombre");
                valoracion =array.getJSONObject(0).getString("valoracion");
                longitud = array.getJSONObject(0).getDouble("longitud");
                latitud = array.getJSONObject(0).getDouble("latitud");
            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                resul = false;
            }

            return resul;
        }

        protected void onPostExecute(Boolean result) {

            if (result)
            {

            }
        }
    }



}

