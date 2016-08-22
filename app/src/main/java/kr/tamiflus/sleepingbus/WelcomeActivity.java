package kr.tamiflus.sleepingbus;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import kr.tamiflus.sleepingbus.fragment.AlarmIntroFragment;
import kr.tamiflus.sleepingbus.fragment.BaseFullScreenFragment;
import kr.tamiflus.sleepingbus.fragment.BookMarkFragment;
import kr.tamiflus.sleepingbus.fragment.StartButtonFragment;
import kr.tamiflus.sleepingbus.fragment.WelcomeFragment;
import kr.tamiflus.sleepingbus.views.CustomViewPager;
import kr.tamiflus.sleepingbus.views.IndicatorView;

public class WelcomeActivity extends AppCompatActivity {

    private CustomViewPager pager;
    private LinearLayout indicatorLayout;
    private int selected;

    private BaseFullScreenFragment fragments[];
    private WelcomeFragment welcomeFragment;
    private AlarmIntroFragment alarmIntroFragment;
    private BookMarkFragment bookMarkFragment;
    private StartButtonFragment startButtonFragment;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        welcomeFragment = WelcomeFragment.newInstance();
        alarmIntroFragment = AlarmIntroFragment.newInstance();
        bookMarkFragment = bookMarkFragment.newInstance();
        startButtonFragment = startButtonFragment.newInstance();


        pager = (CustomViewPager) findViewById(R.id.viewPager);
        indicatorLayout = (LinearLayout) findViewById(R.id.indicatorLayout);

        fragments = new BaseFullScreenFragment[] {
                welcomeFragment,
                alarmIntroFragment,
                bookMarkFragment,
                startButtonFragment
        };

        for (int i = 0; i < fragments.length; i++) {
            final int index = i;
            IndicatorView indicatorView = new IndicatorView(this);
            indicatorView.setOnClickListener(v ->
                    pager.setCurrentItem(index)
            );
            indicatorLayout.addView(indicatorView);

            // set tag
            fragments[i].setIndex(i);

        }

        selected = 0;
        indicatorLayout.getChildAt(selected).setSelected(true);

        pager.setOffscreenPageLimit(fragments.length);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                indicatorLayout.getChildAt(selected).setSelected(false);
                indicatorLayout.getChildAt(position).setSelected(true);
                selected = position;
            }

            @Override
            public void onPageScrolled(int pos, float offset, int px) {
                pager.getParent().requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        pager.setPageTransformer(false, (page, position) -> {
            if (position <= -1 || position > 1) return;

            int pageWidth = page.getWidth(), index = (int) page.getTag();
            int pageHeight = page.getHeight();


            if (selected == 0) {
                if (index == 1) {
                    welcomeFragment.getBusView().setTranslationX((pageWidth / 2) * (1 - position));
                }
            }

            if (selected >= 0 && selected <= 2) {
                if (index == 1) {
                    alarmIntroFragment.getBusStopView().setTranslationY(pageHeight * 0.8f * Math.abs(position));
                    alarmIntroFragment.getBusView().setTranslationX(pageHeight * -0.65f * Math.abs(position));
                } else if(index == 2) {
                    bookMarkFragment.getPassView().setTranslationY(pageHeight * 0.65f * Math.abs(position));
                }
            }
        });

        adapter = new PagerAdapter(getSupportFragmentManager(), true);
        pager.setAdapter(adapter);

        pager.setOnTouchListener((v, event) -> {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
        );
    }

    public void goNext() {
        int next = pager.getCurrentItem() + 1;
        if (next < fragments.length)
            pager.setCurrentItem(next, true);
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        int count = fragments.length;
        public PagerAdapter(FragmentManager fm, boolean preventHydeScroll) {
            super(fm);
//            if (preventHydeScroll) count = 3;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}
