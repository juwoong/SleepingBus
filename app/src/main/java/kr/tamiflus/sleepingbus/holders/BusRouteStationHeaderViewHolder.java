package kr.tamiflus.sleepingbus.holders;

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
    @BindView(R.id.busRouteStationSearchEditText) public EditText text;
    @BindView(R.id.cancelBtn) public View btn;

    public BusRouteStationHeaderViewHolder(View v){
        super(v);
        ButterKnife.bind(this, v);
    }
}
