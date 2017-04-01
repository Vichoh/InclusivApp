package rest;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adrian.avance.R;

import org.json.JSONArray;

import java.util.ArrayList;

import back.AdaptadorEstablecimiento;
import back.Establecimiento;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * Created by Adrian on 24/03/2017.
 */

public class ObtenerBusqueda extends AsyncTask<String,Integer,Boolean> {

    private String url = "http://192.168.1.243/InclusivApp/controllers/establecimiento.php";
    private ArrayList<Establecimiento> establecimientosBusqueda = new ArrayList<>();
    private String busqueda;
    private String respStr;
    private Context context;
    private Activity activity;



    public ObtenerBusqueda(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Establecimiento> getEstablecimientosBusqueda() {
        return establecimientosBusqueda;
    }

    public void setEstablecimientosBusqueda(ArrayList<Establecimiento> establecimientosBusqueda) {
        this.establecimientosBusqueda = establecimientosBusqueda;
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
                establecimiento.setValoracion(array.getJSONObject(i).getString("valoracion"));
                establecimiento.setNota(array.getJSONObject(i).getString("nota"));


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


                AdaptadorEstablecimiento adaptadorEstablecimiento = new AdaptadorEstablecimiento(this.activity,
                        establecimientosBusqueda);


                View view;
                LayoutInflater inf =(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //LayoutInflater inf =LayoutInflater.from(context);
                view = inf.inflate(R.layout.activity_mapa, null);



                ListView listView = (ListView) view.findViewById(R.id.list_buscar_mapa);

                listView.setAdapter(adaptadorEstablecimiento);
            }
        }
    }



}