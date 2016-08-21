package kr.tamiflus.sleepingbus.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.tamiflus.sleepingbus.R;

/**
 * Created by tamiflus on 2016. 7. 31..
 */
public class BusStationViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.busStationNameText) public TextView nameView;
    @BindView(R.id.busStationIdText) public TextView idView;
    @BindView(R.id.busStationLocationText) public TextView locationView;
    @BindView(R.id.objectLayout) public LinearLayout layout;

    public BusStationViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }
}
