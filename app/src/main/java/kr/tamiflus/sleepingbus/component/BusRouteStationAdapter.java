package kr.tamiflus.sleepingbus.component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.tamiflus.sleepingbus.R;
import kr.tamiflus.sleepingbus.structs.BusStation;

public class BusRouteStationAdapter extends RecyclerView.Adapter<BusRouteStationViewHolder> {
    Context context;
    LayoutInflater inflater;
    ArrayList<BusStation> list = new ArrayList<>();

    public BusRouteStationAdapter(Context context) {
        //super(context, R.layout.busstation_route_listview);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addAll(List<BusStation> list) {
        this.list.addAll(list);
    }

    public void add(BusStation obj) {
        this.list.add(obj);
    }

    public void clear() {
        this.list.clear();
    }


    @Override
    public BusRouteStationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.busstation_route_listview, parent, false);
        BusRouteStationViewHolder holder = new BusRouteStationViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(BusRouteStationViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        BusStation st = list.get(position);

        holder.name.setText(st.getName());
        holder.id.setText(st.getId());
        holder.location.setText(st.getRegion());

        if(position == 0) holder.arrowImage.setImageResource(R.drawable.start);
        else if(position == (list.size() - 1)) holder.arrowImage.setImageResource(R.drawable.end);
        else holder.arrowImage.setImageResource(R.drawable.midd);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
