package kr.tamiflus.sleepingbus;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        CoordinatorLayout layout = (CoordinatorLayout) findViewById(R.id.coordinator);
        TextView summary = (TextView) findViewById(R.id.summaryText);
        TextView boardBtn = (TextView) findViewById(R.id.boardBtn);

        layout.setBackgroundColor(getResources().getColor(R.color.jikhang));
        boardBtn.setTextColor(getResources().getColor(R.color.jikhang));
        summary.setText(String.format("%s 행\n%s번 버스", "천호역.천호사거리", "1113-1"));

        boardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: 다 설정되었을 때 알람 실행하기
            }
        });
    }

}
