package kr.tamiflus.sleepingbus.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import kr.tamiflus.sleepingbus.R;
import kr.tamiflus.sleepingbus.structs.BusStation;

public class BusRouteStationAdapter extends ArrayAdapter<BusStation> {
    Context context;
    LayoutInflater inflater;

    public BusRouteStationAdapter(Context context) {
        super(context, R.layout.busstation_route_listview);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        BusStation st = this.getItem(position);

        BusRouteStationViewHolder holder;
        if (view != null) {
            holder = (BusRouteStationViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.busstation_route_listview, parent, false);
            holder = new BusRouteStationViewHolder(view);
            view.setTag(holder);
        }

        holder.name.setText(st.getName());
        holder.id.setText(st.getId());
        holder.location.setText(st.getRegion());

        if(position == 0) holder.arrowImage.setImageResource(R.drawable.start);
        else if(position == (this.getCount() - 1)) holder.arrowImage.setImageResource(R.drawable.end);
        else holder.arrowImage.setImageResource(R.drawable.midd);

        return view;
    }
}
