package kr.tamiflus.sleepingbus.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.tamiflus.sleepingbus.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AlarmIntroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlarmIntroFragment extends BaseFullScreenFragment {
    public static AlarmIntroFragment newInstance() {
        AlarmIntroFragment fragment = new AlarmIntroFragment();
        return fragment;
    }

    private View busView;
    private View busStopView;

    public AlarmIntroFragment() {
        // Required empty public constructor
    }

    public View getBusView() {
        return busView;
    }
    public View getBusStopView() { return busStopView; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStatusBarColor(R.color.theme);

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_alarm_intro, container, false);

        busView = root.findViewById(R.id.busImage);
        busStopView =  root.findViewById(R.id.busStopPannel);

        return root;
    }
}
