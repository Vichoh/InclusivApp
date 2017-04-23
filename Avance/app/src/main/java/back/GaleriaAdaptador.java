package back;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adrian.avance.R;
import com.github.snowdream.android.widget.SmartImage;
import com.github.snowdream.android.widget.SmartImageView;

import java.util.List;

/**
 * Created by Xiterss on 25-03-2017.
 */

public class GaleriaAdaptador extends PagerAdapter {

    private List<String> imagenes;
    private Context context;
    private LayoutInflater layoutInflater;
    private SmartImageView img;

    public GaleriaAdaptador(List<String> imagenes,Context context){
        this.imagenes = imagenes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imagenes.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view== (RelativeLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View item = layoutInflater.inflate(R.layout.galeria_layout,container,false);
        img = (SmartImageView) item.findViewById(R.id.imgViewPager);
        TextView contador = (TextView) item.findViewById(R.id.txtContador);

        Rect rect = new Rect(img.getLeft(),img.getTop(),img.getRight(),img.getBottom());

        contador.setText((position+1)+"/"+imagenes.size());
        img.setImageUrl(imagenes.get(position),rect);

        container.addView(item);

        return item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}
