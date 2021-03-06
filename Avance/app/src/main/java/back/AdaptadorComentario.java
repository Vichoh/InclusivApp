package back;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.adrian.avance.R;

import java.util.ArrayList;

/**
 * Created by Adrian on 23/04/2017.
 */

public class AdaptadorComentario extends ArrayAdapter {

    private Context context;
    private ArrayList<String> comentarios = new ArrayList<>();

    public AdaptadorComentario(Context context, ArrayList datos) {
        super(context, R.layout.comentario, datos);
        // Guardamos los parámetros en variables de clase.
        this.context = context;
        this.comentarios = datos;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // En primer lugar "inflamos" una nueva vista, que será la que se
        // mostrará en la celda del ListView. Para ello primero creamos el
        // inflater, y después inflamos la vista.
       // LayoutInflater inf =(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.comentario, null);



        // Recogemos el TextView para mostrar el nombre y establecemos el
        // nombre.
        TextView nombre = (TextView) item.findViewById(R.id.comentario);
        nombre.setText("1"+comentarios.get(position));


        // Devolvemos la vista para que se muestre en el ListView.
        return item;
    }
}
