package kr.tamiflus.sleepingbus.component;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.tamiflus.sleepingbus.R;

/**
 * Created by tamiflus on 16. 8. 15..
 */
public class BusRouteStationHeaderViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.busRouteStationSearchEditText) EditText text;

    public BusRouteStationHeaderViewHolder(View v){
        super(v);
        ButterKnife.bind(this, v);
    }
}
