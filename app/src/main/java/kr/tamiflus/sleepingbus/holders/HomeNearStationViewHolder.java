package kr.tamiflus.sleepingbus.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.tamiflus.sleepingbus.R;

/**
 * Created by tamiflus on 16. 8. 17..
 */
public class HomeNearStationViewHolder  extends RecyclerView.ViewHolder {
    @BindView(R.id.nearStationNameView) public TextView nearStationNameView;
    @BindView(R.id.nearStationDistanceView) public TextView nearStationDistanceView;

    public HomeNearStationViewHolder(View v){
        super(v);
        ButterKnife.bind(this, v);
    }
}
