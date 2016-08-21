package kr.tamiflus.sleepingbus.holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.tamiflus.sleepingbus.R;

/**
 * Created by tamiflus on 16. 8. 18..
 */
public class HomeNearStationListViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.nearStationName) public TextView name;
    @BindView(R.id.cardView) public CardView view;

    public HomeNearStationListViewHolder (View v) {
        super(v);
        ButterKnife.bind(this, v);
    }
}
