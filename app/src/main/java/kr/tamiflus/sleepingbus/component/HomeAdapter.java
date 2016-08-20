package kr.tamiflus.sleepingbus.component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.tamiflus.sleepingbus.R;
import kr.tamiflus.sleepingbus.holders.HomeBookMarkViewHolder;
import kr.tamiflus.sleepingbus.holders.HomeNearStationListViewHolder;
import kr.tamiflus.sleepingbus.holders.HomeNearStationViewHolder;
import kr.tamiflus.sleepingbus.structs.BookMark;
import kr.tamiflus.sleepingbus.structs.BusStation;
import kr.tamiflus.sleepingbus.structs.HomeObject;
import kr.tamiflus.sleepingbus.structs.NearStation;
import kr.tamiflus.sleepingbus.structs.NearTwoStation;
import kr.tamiflus.sleepingbus.utils.ColorMap;

/**
 * Created by tamiflus on 16. 8. 17..
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    LayoutInflater inflater;
    ArrayList<HomeObject> list = new ArrayList<>();
    private static final int NEAR_STATION_TYPE = 1;
    private static final int NEAR_STATION_DOUBLED_TYPE = 2;
    private static final int BOOKMARK_TYPE = 3;

    public HomeAdapter(Context context) {
        //super(context, R.layout.busstation_route_listview);
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == NEAR_STATION_TYPE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_home_nearstation, parent, false);
            return new HomeNearStationViewHolder(v);
        }
        else if(viewType == NEAR_STATION_DOUBLED_TYPE){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_home_nearstation_two, parent, false);
            return new HomeNearStationListViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_home_bookmark, parent, false);
            return new HomeBookMarkViewHolder(v);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position) {
        //TODO: 여기에서 OnClickListener 등을 구현할 것
        if(vh instanceof HomeNearStationViewHolder) {
            HomeNearStationViewHolder holder = (HomeNearStationViewHolder) vh;
            NearStation st = (NearStation) list.get(position);
            holder.nearStationNameView.setText(st.st.getName());
            holder.nearStationDistanceView.setText(Integer.toString(st.distance) + "m");
        } else if(vh instanceof HomeNearStationListViewHolder) {
            final HomeNearStationListViewHolder holder = (HomeNearStationListViewHolder) vh;
            final NearTwoStation st = (NearTwoStation) list.get(position);
            holder.layout.initLayout(true);
            holder.name.setText(st.name);
            holder.upName.setText(st.s1.getName());
            holder.downName.setText(st.s2.getName());
            holder.downDistance.setText(Integer.toString(st.d2)+"m");
            holder.upDistance.setText(Integer.toString(st.d1)+"m");
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.layout.isExpanded()){
                        holder.layout.collapse();
                        holder.btn.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    } else {
                        holder.layout.expand();
                        holder.btn.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    }

                }
            });

            holder.upView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, st.name + " : " + st.s1.getName(),Toast.LENGTH_LONG).show();
                }
            });

            holder.downView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, st.name + " : " + st.s2.getName(),Toast.LENGTH_LONG).show();

                }
            });
        } else {
            HomeBookMarkViewHolder holder = (HomeBookMarkViewHolder) vh;
            BookMark mark = (BookMark) list.get(position);

            holder.name.setText(mark.name);
            holder.routeName.setTextColor(ColorMap.byID.get(mark.arrivingBus.getRouteTypeCd()));
            holder.routeName.setText(mark.arrivingBus.getRouteName());
            holder.course.setText(mark.startSt.getName() + " > " + mark.endSt.getName());
            holder.leftSeat.setText(String.format("(남은 좌석 : %d)", 27));
            holder.leftTime.setText(Integer.toString(mark.arrivingBus.getTimeToWait()) + "분 전");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return this.list.get(position).type;
    }


    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public void addAll(List<HomeObject> list) {
        this.list.addAll(list);
    }

    public void add(HomeObject obj) {
        this.list.add(obj);
    }

    public void clear() {
        this.list.clear();
    }
}
