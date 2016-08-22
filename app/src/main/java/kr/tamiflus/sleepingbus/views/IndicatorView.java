package kr.tamiflus.sleepingbus.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.widget.LinearLayout;

import kr.tamiflus.sleepingbus.utils.Utils;

/**
 * Created by tamiflus on 16. 8. 22..
 */
public class IndicatorView extends LinearLayout {

    private ShapeDrawable shapeDrawable;
    private int defaultColor;
    private int selectedColor;

    public IndicatorView(Context context) {
        super(context);

        int indicatorSize = (int) Utils.dp2px(context, 5);
        int margin = (int) Utils.dp2px(context, 3.5f);
        LayoutParams layoutParams = new LayoutParams(indicatorSize, indicatorSize);
        layoutParams.setMargins(margin, margin, margin, margin);
        setLayoutParams(layoutParams);

        shapeDrawable = new ShapeDrawable(new OvalShape());
        setBackgroundDrawable(shapeDrawable);

        defaultColor = Color.parseColor("#CCCCCC");
        selectedColor = Color.parseColor("#9A9A9A");

        setSelected(false);
    }


    private void setColor(int color) {
        shapeDrawable.getPaint().setColor(color);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        refreshColor();
    }

    private void refreshColor() {
        setColor(isSelected() ? selectedColor : defaultColor);
        invalidate();
    }

}