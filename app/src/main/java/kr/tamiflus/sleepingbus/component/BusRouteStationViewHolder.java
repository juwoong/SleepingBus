package kr.tamiflus.sleepingbus.component;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.tamiflus.sleepingbus.R;

/**
 * Created by midmo on 2016-08-15.
 */
public class BusRouteStationViewHolder {
    @BindView(R.id.arrowImgView) ImageView arrowImage;
    @BindView(R.id.busStationNameText) TextView name;
    @BindView(R.id.busStationLocationText) TextView location;
    @BindView(R.id.busStationIdText) TextView id;

    public BusRouteStationViewHolder(View v) {
        ButterKnife.bind(this, v);
    }
}
