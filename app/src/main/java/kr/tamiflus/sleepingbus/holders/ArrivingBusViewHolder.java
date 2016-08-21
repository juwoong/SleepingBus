package kr.tamiflus.sleepingbus.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.tamiflus.sleepingbus.R;
import kr.tamiflus.sleepingbus.structs.ArrivingBus;

/**
 * Created by tamiflus on 16. 8. 21..
 */
public class ArrivingBusViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.busRouteName) public TextView nameView;
    @BindView(R.id.busRouteWay) public TextView wayView;
    @BindView(R.id.busLeftStation) public  TextView leftStView;
    @BindView(R.id.busLeftTime) public TextView leftTimeView;
    @BindView(R.id.item) public View item;

    public ArrivingBusViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }
}
