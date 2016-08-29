package kr.tamiflus.sleepingbus.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.tamiflus.sleepingbus.R;

/**
 * Created by tamiflus on 16. 8. 20..
 */
public class HomeBookMarkViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.cardView) public View cardView;
    @BindView(R.id.bookMarkRouteName) public TextView routeName;
    @BindView(R.id.bookMarkName) public TextView name;
    @BindView(R.id.bookMarkCourse) public TextView course;
    @BindView(R.id.bookMarkBusLeftSeat) public TextView leftSeat;
    @BindView(R.id.bookMarkBusLeftTime) public TextView leftTime;

    public HomeBookMarkViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }
}
