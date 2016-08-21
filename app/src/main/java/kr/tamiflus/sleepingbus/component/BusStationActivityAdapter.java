package kr.tamiflus.sleepingbus.component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kr.tamiflus.sleepingbus.R;
import kr.tamiflus.sleepingbus.holders.ArrivingBusViewHolder;
import kr.tamiflus.sleepingbus.holders.BusRouteStationHeaderViewHolder;
import kr.tamiflus.sleepingbus.holders.BusRouteStationViewHolder;
import kr.tamiflus.sleepingbus.holders.SectionViewHolder;
import kr.tamiflus.sleepingbus.structs.ArrivingBus;
import kr.tamiflus.sleepingbus.structs.Bus;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.structs.BusTag;
import kr.tamiflus.sleepingbus.utils.ColorMap;

/**
 * Created by tamiflus on 16. 8. 21..
 */
public class BusStationActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    Context context;
    LayoutInflater inflater;
    ArrayList<Bus> list = new ArrayList<>();

    public BusStationActivityAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void addAll(List<Bus> list) {
        this.list.addAll(list);
    }

    public void add(Bus obj) {
        this.list.add(obj);
    }

    public void clear() {
        this.list.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_bus_component, parent, false);
            return new SectionViewHolder(v);
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
        if(vh instanceof SectionViewHolder) {
            SectionViewHolder holder = (SectionViewHolder) vh;
            BusTag tag = (BusTag) list.get(position);
            holder.sectionName.setText(tag.type);
        }
        else{
            ArrivingBusViewHolder holder = (ArrivingBusViewHolder) vh;
            ArrivingBus bus = (ArrivingBus) list.get(position);
            holder.nameView.setText(bus.getRouteName());
            holder.nameView.setTextColor(context.getResources().getColor(ColorMap.byID.get(bus.getRouteTypeCd())));
            //TODO : ArrivingBus 객체 수정. 필요한 사항(버스 방향, 남은 시간, 남은 정류장)
            //TODO : 남은 시간 정류장 없을 시 분기 조절해서 뜨지 않으 남은 시간 > '한참 뒤' 로 바꿔주기
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position) instanceof BusTag)
            return TYPE_HEADER;
        return TYPE_ITEM;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}