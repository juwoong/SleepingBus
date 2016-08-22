package kr.tamiflus.sleepingbus.component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.tamiflus.sleepingbus.R;
import kr.tamiflus.sleepingbus.holders.BusRouteStationHeaderViewHolder;
import kr.tamiflus.sleepingbus.holders.BusRouteStationViewHolder;
import kr.tamiflus.sleepingbus.structs.Bus;
import kr.tamiflus.sleepingbus.structs.BusStation;

public class BusRouteStationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    Context context;
    LayoutInflater inflater;
    ArrayList<BusStation> list = new ArrayList<>();
    ArrayList<BusStation> listCopy = new ArrayList<>(); // store original pure list

    public BusRouteStationAdapter(Context context) {
        //super(context, R.layout.busstation_route_listview);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addAll(List<BusStation> list) {
        this.list.addAll(list);
        this.listCopy.addAll(list);
    }

    public void add(BusStation obj) {
        this.list.add(obj);
        this.listCopy.add(obj);
    }

    public void clear() {
        this.list.clear();
        this.listCopy.clear();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_HEADER)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.busstation_route_search_component, parent, false);
            return new BusRouteStationHeaderViewHolder(v);
        }
        else{
            View view = inflater.inflate(R.layout.busstation_route_listview, parent, false);
            return new BusRouteStationViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(vh instanceof BusRouteStationHeaderViewHolder) {
            BusRouteStationHeaderViewHolder holder = (BusRouteStationHeaderViewHolder) vh;
            holder.text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String value = charSequence.toString();
                    Log.i("Change", value);
                    //TODO: 현 노선에서 정류장 검색 할 수 있게 만들기.
                    if(value.length() > 0) {
                        list = new ArrayList<>();
                        list.add(listCopy.get(0));
                        for(int k = 1; k<listCopy.size(); k++) {
                            if(listCopy.get(k).getName().contains(charSequence)) {
                                list.add(listCopy.get(k));
                            }
                        }
                    } else {
                        list = new ArrayList<>();
                        list.add(listCopy.get(0));
                        for(int k = 1; k<listCopy.size(); k++) {
                            list.add(listCopy.get(k));
                        }
                    }

                    //debug

                    for(int k = 0; k<list.size(); k++) {

                    }
                    notifyDataSetChanged();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
        else{
            BusStation st = list.get(position);
            BusRouteStationViewHolder holder = (BusRouteStationViewHolder) vh;

            holder.name.setText(st.getName());
            holder.id.setText(st.getId());
            holder.location.setText(st.getRegion());
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: 목적지 정류장이 선택되었을 때 할 일 만들기
                }
            });

            if (position == 1) holder.arrowImage.setImageResource(R.drawable.start);
            else if (position == (list.size() - 1))
                holder.arrowImage.setImageResource(R.drawable.end);
            else holder.arrowImage.setImageResource(R.drawable.midd);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return TYPE_HEADER;
        return TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
