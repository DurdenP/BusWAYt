package app.durdenp.com.buswayt.client;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.durdenp.com.buswayt.R;

/**
 * Created by Marco on 25/05/2015.
 */
public class BusListAdapter extends ArrayAdapter {

    private Context context;
    private boolean useList = true;

    /**
     *
     * @param context
     * @param items
     */
    public BusListAdapter(Context context, List items) {
        super(context, android.R.layout.simple_list_item_2, items);
        //super(context, R.layout.fragment_buslistfragment_list, items);
        this.context = context;
    }

    /**
     * Holder for the list items.
     */
    private class ViewHolder{
        TextView idText;
        TextView lineaText;
        TextView speedText;
        TextView arrivoPrevisto;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        BusListItem item = (BusListItem)getItem(position);
        View viewToUse = null;

        // This block exists to inflate the settings list item conditionally based on whether
        // we want to support a grid or list view.
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            /*MARCO
            if(useList){
                viewToUse = mInflater.inflate(R.layout.fragment_buslistfragment_list, null);
            } else {
                viewToUse = mInflater.inflate(R.layout.fragment_buslistfragment_grid, null);
            }
            */
            viewToUse = mInflater.inflate(R.layout.bus_list_layout, null);

            holder = new ViewHolder();
            holder.idText = (TextView)viewToUse.findViewById(R.id.busIdText);
            holder.lineaText = (TextView)viewToUse.findViewById(R.id.busLineaText);
            holder.speedText = (TextView)viewToUse.findViewById(R.id.busSpeedText);
            holder.arrivoPrevisto = (TextView)viewToUse.findViewById(R.id.arrivoPrevistoText);
            viewToUse.setTag(holder);

        } else {
            viewToUse = convertView;
            holder = (ViewHolder) viewToUse.getTag();
        }

        holder.idText.setText(item.getId());
        holder.lineaText.setText(item.getLinea());
        holder.speedText.setText(Double.toString(item.getSpeed()));
        holder.arrivoPrevisto.setText(Integer.toString(item.getArrivoPrevisto()));
        return viewToUse;
    }

}
