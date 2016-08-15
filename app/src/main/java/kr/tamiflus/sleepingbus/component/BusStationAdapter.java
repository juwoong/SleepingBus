package kr.tamiflus.sleepingbus.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import kr.tamiflus.sleepingbus.R;
import kr.tamiflus.sleepingbus.structs.BusStation;

/**
 * Created by tamiflus on 2016. 7. 31..
 */
public class BusStationAdapter extends ArrayAdapter<BusStation>{

    Context context;
    LayoutInflater inflater;

    public BusStationAdapter(Context context) {
        super(context, R.layout.busstation_listview_component);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        BusStation st = this.getItem(position);

        BusStationViewHolder holder;
        if (view != null) {
            holder = (BusStationViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.busstation_listview_component, parent, false);
            holder = new BusStationViewHolder(view);
            view.setTag(holder);
        }

        holder.nameView.setText(st.getName());
        holder.idView.setText(st.getId());
        holder.locationView.setText(st.getRegion());



        return view;
    }
}
