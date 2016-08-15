package kr.tamiflus.sleepingbus.component;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.tamiflus.sleepingbus.R;

/**
 * Created by midmo on 2016-08-15.
 */
public class BusRouteStationViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.arrowImgView) ImageView arrowImage;
    @BindView(R.id.busStationNameText) TextView name;
    @BindView(R.id.busStationLocationText) TextView location;
    @BindView(R.id.busStationIdText) TextView id;

    public BusRouteStationViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }
}
