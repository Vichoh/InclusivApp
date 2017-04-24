package back;

import android.content.Context;
import android.graphics.Color;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adrian.avance.Mapa;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.client.utils.URLEncodedUtils;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

/**
 * Created by Adrian on 21/04/2017.
 */

public class Rutas {

    private String latitudDestino;
    private String longitudDestino;
    private String latitudActual;
    private String longitudActual;


    private Context contexto;



    public Rutas(String latitudDestino, String longitudDestino, String latitudActual, String longitudActual, Context contexto) {
        this.latitudDestino = latitudDestino;
        this.longitudDestino = longitudDestino;
        this.latitudActual = latitudActual;
        this.longitudActual = longitudActual;
        this.contexto = contexto;


    }

    public Rutas(){

    }





    public void addRoutes (){




        RequestQueue requestQueue = Volley.newRequestQueue(contexto);
        StringRequest request = new StringRequest(Request.Method.GET, construccionGet(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try{
                            JSONObject jsonObj = new JSONObject(response);


                            drawRoutes(parse(jsonObj));


                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(contexto,"Error en la carga "+ex.getMessage(),Toast.LENGTH_SHORT).show();
                        }


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(contexto,"error"+error.toString(),Toast.LENGTH_SHORT).show();


            }
        });
        requestQueue.add(request);


    }

    private String construccionGet(){

        String GET = "https://maps.googleapis.com/maps/api/directions/json?";

        List<BasicNameValuePair> paramsUrl = new LinkedList<BasicNameValuePair>();
        paramsUrl.add(new BasicNameValuePair("origin", this.latitudActual+","+this.longitudActual));
        paramsUrl.add(new BasicNameValuePair("destination", this.latitudDestino+","+this.longitudDestino));
        paramsUrl.add(new BasicNameValuePair("sensor", "false"));
        paramsUrl.add(new BasicNameValuePair("mode", "driving"));
        paramsUrl.add(new BasicNameValuePair("alternatives", "true"));

        String strParams = URLEncodedUtils.format(paramsUrl, "utf-8");

        return GET + strParams;
    }

    private List<List<HashMap<String,String>>> parse(JSONObject jObject){
        //Este método PARSEA el JSONObject que retorna del API de Rutas de Google devolviendo
        //una lista del lista de HashMap Strings con el listado de Coordenadas de Lat y Long,
        //con la cual se podrá dibujar pollinas que describan la ruta entre 2 puntos.
        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for(int l=0;l<list.size();l++){
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }
        return routes;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    private void drawRoutes(List<List<HashMap<String, String>>> result) {
        LatLng center = null;
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;


        // recorriendo todas las rutas
        for(int i=0;i<result.size();i++){
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Obteniendo el detalle de la ruta
            List<HashMap<String, String>> path = result.get(i);

            // Obteniendo todos los puntos y/o coordenadas de la ruta
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                if (center == null) {
                    //Obtengo la 1ra coordenada para centrar el mapa en la misma.
                    center = new LatLng(lat, lng);
                }
                points.add(position);
            }

            // Agregamos todos los puntos en la ruta al objeto LineOptions
            lineOptions.addAll(points);
            //Definimos el grosor de las Polilíneas
            lineOptions.width(4);
            //Definimos el color de la Polilíneas
            lineOptions.color(Color.BLACK);
        }

        // Dibujamos las Polilineas en el Google Map para cada ruta

        Mapa mapa = new Mapa();
        mapa.mapa.addPolyline(lineOptions);
        mapa.mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 18));
    }


}
