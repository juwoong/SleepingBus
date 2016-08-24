package kr.tamiflus.sleepingbus.component;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.tamiflus.sleepingbus.BusRouteStationListActivity;
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
    ArrayList<BusStation> origin = new ArrayList<>();
    Handler handler;

    public BusRouteStationAdapter(Context context, Handler handler) {
        //super(context, R.layout.busstation_route_listview);
        this.context = context;
        this.handler = handler;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addAll(List<BusStation> list) {
        this.list.addAll(list);
        list.remove(0);
        this.origin.addAll(list);
    }

    public void add(BusStation obj) {
        this.list.add(obj);
    }

    public void clear() {
//        this.listCopy.clear();
        for(int i=this.list.size()-1; i>=1; i--) { this.list.remove(i); }
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
            holder.text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    String value = textView.getText().toString();
                    Log.i("Change", value);
                    //TODO: 현 노선에서 정류장 검색 할 수 있게 만들기.
                    if(i == EditorInfo.IME_ACTION_SEARCH) {
                        ArrayList<BusStation> stlist = new ArrayList<BusStation>();
                        for (BusStation st : list) {
                            if (st.getName() != null &&st.getName().contains(value)) {
                                Log.d("Find", st.toString());
                                stlist.add(st);
                            }
                        }
                        clear();
                        list.addAll(stlist);
                        //TODO 여기 부분 고치기.
                        notifyDataSetChanged();
                    }
                    return true;
                }
            });
            holder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.text.setText("");
                    clear();
                    list.addAll(origin);
                    notifyDataSetChanged();
                }
            });
        }
        else{
            final BusStation st = list.get(position);
            BusRouteStationViewHolder holder = (BusRouteStationViewHolder) vh;

            holder.name.setText(st.getName());
            holder.id.setText(st.getId());
            holder.location.setText(st.getRegion());
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: 목적지 정류장이 선택되었을 때 할 일 만들기
                    Message msg = new Message();
                    msg.what = BusRouteStationListActivity.NEXT_ACTIVITY;
                    msg.obj = st;
                    handler.sendMessage(msg);
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
