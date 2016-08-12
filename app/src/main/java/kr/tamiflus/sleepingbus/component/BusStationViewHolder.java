package kr.tamiflus.sleepingbus.component;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.tamiflus.sleepingbus.R;

/**
 * Created by tamiflus on 2016. 7. 31..
 */
public class BusStationViewHolder  {
    @BindView(R.id.busStationNameText) TextView nameView;
    @BindView(R.id.busStationIdText) TextView idView;
    @BindView(R.id.busStationLocationText) TextView locationView;

    public BusStationViewHolder(View v) {
        ButterKnife.bind(this, v);
    }
}
