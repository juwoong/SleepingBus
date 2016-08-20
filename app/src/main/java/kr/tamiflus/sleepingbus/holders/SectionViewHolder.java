package kr.tamiflus.sleepingbus.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.tamiflus.sleepingbus.R;

/**
 * Created by tamiflus on 16. 8. 20..
 */
public class SectionViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.sectionName)
    public TextView sectionName;

    public SectionViewHolder(View v){
        super(v);
        ButterKnife.bind(this, v);
    }
}
