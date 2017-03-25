package back;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adrian.avance.R;

import java.util.ArrayList;

/**
 * Created by Adrian on 23/03/2017.
 */

public class AdaptadorEstablecimiento extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Establecimiento> establecimientoArrayList;
    Context context;

    public AdaptadorEstablecimiento(Activity activity, ArrayList<Establecimiento> establecimientoArrayList){

        this.activity = activity;
        this.establecimientoArrayList = establecimientoArrayList;
    }

    @Override
    public int getCount() {
        return establecimientoArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return establecimientoArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return establecimientoArrayList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = view;

        if  (view == null){
           LayoutInflater inf =(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater inf =LayoutInflater.from(context);
            v = inf.inflate(R.layout.item_lista, null);


        }

        Establecimiento establecimiento = establecimientoArrayList.get(i);


        ImageView imageView = (ImageView) v.findViewById(R.id.imagenLista);


        TextView textViewNombre = (TextView) v.findViewById(R.id.nombreLugarLista);
        textViewNombre.setText(establecimiento.getNombre());

        TextView textViewCalle = (TextView) v.findViewById(R.id.calleEstablecimientoLista);
        textViewCalle.setText(establecimiento.getCalle()+ " "+ establecimiento.getNumero()+", "+establecimiento.getCiudad());

        return v;
    }
}
